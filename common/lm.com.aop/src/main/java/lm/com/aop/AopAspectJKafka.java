package lm.com.aop;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import lm.com.aop.model.AopClass;
import lm.com.aop.model.EnumLogType;
import lm.com.aop.util.AspectJUtil;
import lm.com.aop.util.ClassUtil;
import lm.com.aop.util.LogUtil;
import lm.com.framework.StringUtil;

/**
 * Aop切面Kafka
 * 
 * @author mrluo735
 *
 */
public class AopAspectJKafka extends AbstractAopAspectJ {
	private static final Logger logger = LoggerFactory.getLogger(AopAspectJKafka.class);

	@Autowired
	private KafkaTemplate<byte[], byte[]> kafkaTemplate;
	
	private Map<String, String> mdMap = new HashMap<String, String>();

	/**
	 * 
	 * @return
	 */
	public Map<String, String> getMdMap() {
		return this.mdMap;
	}

	/**
	 * 
	 * @param mdMap
	 */
	public void setMdMap(Map<String, String> mdMap) {
		this.mdMap = mdMap;
	}

	/**
	 * 前置通知
	 * 
	 * @param joinPoint
	 */
	@Override
	public void before(JoinPoint joinPoint) {
		// logger.info("被拦截方法调用之前调用此方法");
	}

	/**
	 * 返回后通知
	 * 
	 * @param joinPoint
	 */
	@Override
	public void afterReturning(JoinPoint joinPoint) {
		// logger.info("一个方法没有抛出任何异常，正常返回");
	}

	/**
	 * 抛出异常后通知
	 * 
	 * @param joinPoint
	 */
	@Override
	public void afterThrowing(JoinPoint joinPoint) {
		// logger.info("方法抛出异常退出时执行的通知");
	}

	/**
	 * 后置通知
	 * 
	 * @param joinPoint
	 */
	@Override
	public void after(JoinPoint joinPoint) {
		// logger.info("不论是正常返回还是异常退出都通知");
	}

	/**
	 * 环绕通知
	 * 
	 * @param proceedingJoinPoint
	 * @return
	 */
	@Override
	public Object around(ProceedingJoinPoint proceedingJoinPoint) {
		Object result = null;
		AopClass aopClass = ClassUtil.getBody(proceedingJoinPoint);
		try {
			AspectJUtil.printInparams(logger, proceedingJoinPoint);

			long start = System.currentTimeMillis();
			result = proceedingJoinPoint.proceed();
			long finish = System.currentTimeMillis();

			AspectJUtil.printOutparams(logger, result);

			long totalMillisecond = finish - start;
			logger.info("执行方法【{}】耗时: {} 毫秒", aopClass.getMethodFullName(), totalMillisecond);

			Map<String, String> logMap = AspectJUtil.getLog(aopClass, proceedingJoinPoint, totalMillisecond);
			for (Map.Entry<String, String> item : logMap.entrySet()) {
				if (StringUtil.isNullOrWhiteSpace(item.getValue()))
					continue;
				this.kafkaTemplate.send(AbstractAopAspectJ.LOG_SYSTEM_PREFIX + item.getKey().toUpperCase(),
						item.getValue().getBytes("utf-8"));
			}
		} catch (Throwable ex) {
			logger.error("拦截器around异常！异常原因：{}", ex);

			result = AspectJUtil.catchResult(aopClass, ex);
			// 写错误日志
			try {
				String errorLog = LogUtil.getErrorLog(aopClass, ex.getMessage());
				this.kafkaTemplate.send(AbstractAopAspectJ.LOG_SYSTEM_PREFIX + EnumLogType.Error.name().toUpperCase(),
						errorLog.getBytes("utf-8"));
			} catch (UnsupportedEncodingException ex2) {
			}
			// 事务回滚
			try {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			} catch (NoTransactionException ex2) {

			}
		}
		return result;
	}
}
