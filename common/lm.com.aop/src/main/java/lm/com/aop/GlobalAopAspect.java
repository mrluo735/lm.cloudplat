/**
 * @title GlobalAopAspect.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.pro.aop
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年4月28日上午11:26:47
 * @version v1.0.0
 */
package lm.com.aop;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import lm.com.aop.model.AopClass;
import lm.com.aop.model.EnumClassType;
import lm.com.aop.util.ClassUtil;
import lm.com.aop.util.LogUtil;
import lm.com.framework.JsonUtil;
import lm.com.framework.StringUtil;
import lm.com.framework.http.EnumHttpStatus;
import lm.com.framework.service.BaseCloudController;
import lm.com.framework.service.BaseServiceImpl;
import lm.com.framework.service.ResponseDTO;
import lm.com.framework.webmvc.BaseController;
import lm.com.framework.webmvc.result.FailureJsonResult;

/**
 * 全局AOP拦截
 * 
 * @ClassName: GlobalAopAspect
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年4月28日 上午11:26:47
 * 
 */
// @Aspect
@Component
public class GlobalAopAspect {
	private static final Logger logger = LoggerFactory.getLogger(GlobalAopAspect.class);
	/**
	 * 执行日志
	 */
	private final String KAFKA_TOPIC_SYSTEM_RUNLOG = "KAFKA_TOPIC_SYSTEM_RUNLOG";
	/**
	 * 操作日志
	 */
	private final String KAFKA_TOPIC_SYSTEM_ACTIONLOG = "KAFKA_TOPIC_SYSTEM_ACTIONLOG";
	/**
	 * 错误日志
	 */
	private final String KAFKA_TOPIC_SYSTEM_ERRORLOG = "KAFKA_TOPIC_SYSTEM_ERRORLOG";
	/**
	 * 上传日志
	 */
	private final String KAFKA_TOPIC_SYSTEM_UPLOADLOG = "KAFKA_TOPIC_SYSTEM_UPLOADLOG";

	//	private final String POINTCUT = "execution(public * lm..controller..*(..)) || execution(public * lm..service.impl..*(..))";

	@Autowired
	private Map<String, String> methodDescription;
	//	@Autowired
	//	private ProducerHelper producerHelper;

	/**
	 * 定义切点,设置拦截规则
	 */
	//	@Pointcut(POINTCUT)
	//	private void pointcut() {
	//	}

	/**
	 * 前置通知
	 */
	@Before("pointcut()")
	public void before(JoinPoint joinPoint) {
		// logger.info("被拦截方法调用之前调用此方法");
	}

	/**
	 * 返回后通知
	 * 
	 * @param joinPoint
	 * @param result
	 */
	@AfterReturning(value = "pointcut()", returning = "returnValue")
	public void afterReturning(JoinPoint joinPoint) {
		// logger.info("一个方法没有抛出任何异常，正常返回");
	}

	/**
	 * 抛出异常后通知
	 * 
	 * @param joinPoint
	 * @param ex
	 */
	@AfterThrowing(value = "pointcut()", throwing = "exception")
	public void afterThrowing(JoinPoint joinPoint) {
		// logger.info("方法抛出异常退出时执行的通知");
	}

	/**
	 * 后置通知
	 * 
	 * @param joinPoint
	 */
	@AfterReturning("pointcut()")
	public void after(JoinPoint joinPoint) {
		// logger.info("不论是正常返回还是异常退出都通知");
	}

	/**
	 * 环绕通知
	 * 
	 * @param proceedingJoinPoint
	 * @return
	 * @throws Throwable
	 */
	@Around(value = "pointcut()")
	public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		Object result = null;
		AopClass aopClass = ClassUtil.getBody(proceedingJoinPoint);
		try {
			this.printInparams(proceedingJoinPoint);

			long start = System.currentTimeMillis();
			result = proceedingJoinPoint.proceed();
			long finish = System.currentTimeMillis();

			long totalMillisecond = finish - start;
			logger.info("执行方法【{}】耗时: {} 毫秒", aopClass.getMethodFullName(), totalMillisecond);

			this.printOutparams(result);
			this.writeLog(aopClass, proceedingJoinPoint, totalMillisecond);
		} catch (Exception ex) {
			logger.error("拦截器around异常！异常原因：{}", ex);
			// 写错误日志
			String errorLog = LogUtil.getErrorLog(aopClass, ex.getMessage());
			//			this.producerHelper.send(this.KAFKA_TOPIC_SYSTEM_ERRORLOG, errorLog);

			result = this.catchResult(aopClass, ex);

			// 事务回滚
			if (null != TransactionAspectSupport.currentTransactionStatus())
				TransactionAspectSupport.currentTransactionStatus().isRollbackOnly();
		}
		return result;
	}

	/**
	 * 打印入参
	 * 
	 * @param proceedingJoinPoint
	 */
	private void printInparams(ProceedingJoinPoint proceedingJoinPoint) {
		HttpServletRequest request = ClassUtil.getServletRequest(proceedingJoinPoint);
		if (request == null)
			return;

		logger.info("入参如下: {}", JsonUtil.toJsonUseJackson(request));
	}

	/**
	 * 打印出参
	 * 
	 * @param result
	 */
	private void printOutparams(Object result) {
		if (result == null) {
			logger.info("出参如下: 返回为null");
			return;
		}
		logger.info("出参如下: {}", JsonUtil.toJsonUseJackson(result));
	}

	/**
	 * 写入日志
	 * 
	 * @param aopClass
	 * @param proceedingJoinPoint
	 * @param totalMillisecond
	 */
	private void writeLog(AopClass aopClass, ProceedingJoinPoint proceedingJoinPoint, long totalMillisecond) {
		// 执行日志
		String strLog = LogUtil.getRunLog(aopClass, totalMillisecond);
		//		this.producerHelper.send(this.KAFKA_TOPIC_SYSTEM_RUNLOG, strLog);
		// 前端控制器则写入操作日志和上传日志
		if (aopClass.getClassType() == EnumClassType.Controller
				|| aopClass.getClassType() == EnumClassType.RestController) {
			strLog = LogUtil.getActionLog(aopClass);
			//			this.producerHelper.send(this.KAFKA_TOPIC_SYSTEM_ACTIONLOG, strLog);

			// 上传日志
			HttpServletRequest request = ClassUtil.getServletRequest(proceedingJoinPoint);
			if (request != null && !StringUtil.isNullOrWhiteSpace(request.getParameter("uploadLogJson"))) {
				String uploadLogJson = request.getParameter("uploadLogJson");
				//				this.producerHelper.send(this.KAFKA_TOPIC_SYSTEM_UPLOADLOG, uploadLogJson);
			}
		}
	}

	/**
	 * catch 结果
	 * 
	 * @param aopClass
	 * @param ex
	 * @return
	 */
	private Object catchResult(AopClass aopClass, Exception ex) {
		// 前端控制器
		if (aopClass.getClassType() == EnumClassType.RestController && aopClass.getType() == BaseController.class) {
			FailureJsonResult failureJsonResult = new FailureJsonResult(EnumHttpStatus.Five_BadGateway.getValue(),
					ex.getMessage());
			return failureJsonResult;
		} else if (aopClass.getClassType() == EnumClassType.RestController
				&& aopClass.getType() == BaseCloudController.class) { // Cloud端控制器
			ResponseDTO responseDTO = new ResponseDTO(false, EnumHttpStatus.Five_BadGateway.getValue(),
					ex.getMessage());
			return responseDTO;
		} else if (aopClass.getClassType() == EnumClassType.Common && aopClass.getType() == BaseServiceImpl.class) { // Dubbo服务端
			ResponseDTO responseDTO = new ResponseDTO(false, EnumHttpStatus.Five_BadGateway.getValue(),
					ex.getMessage());
			return responseDTO;
		}
		return null;
	}
}
