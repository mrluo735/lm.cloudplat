package lm.com.aop.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.NoTransactionException;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import lm.com.aop.model.AopClass;
import lm.com.aop.model.EnumClassType;
import lm.com.aop.model.EnumLogType;
import lm.com.framework.JsonUtil;
import lm.com.framework.StringUtil;
import lm.com.framework.http.EnumHttpStatus;
import lm.com.framework.service.BaseCloudController;
import lm.com.framework.service.BaseServiceImpl;
import lm.com.framework.service.ResponseDTO;
import lm.com.framework.webmvc.BaseController;
import lm.com.framework.webmvc.result.FailureJsonResult;

/**
 * 切面工具类
 * 
 * @author mrluo735
 *
 */
public final class AspectJUtil {
	/**
	 * 打印入参
	 * 
	 * @param logger
	 * @param proceedingJoinPoint
	 */
	public static void printInparams(Logger logger, ProceedingJoinPoint proceedingJoinPoint) {
		HttpServletRequest request = ClassUtil.getServletRequest(proceedingJoinPoint);
		if (request == null)
			return;

		logger.info("入参如下: {}", JsonUtil.toJsonUseJackson(request));
	}

	/**
	 * 打印出参
	 * 
	 * @param logger
	 * @param result
	 */
	public static void printOutparams(Logger logger, Object result) {
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
	public static Map<String, String> getLog(AopClass aopClass, ProceedingJoinPoint proceedingJoinPoint,
			long totalMillisecond) {
		Map<String, String> map = new HashMap<String, String>();
		String runLog = "", actionLog = "", uploadLog = "";
		// 执行日志
		runLog = LogUtil.getRunLog(aopClass, totalMillisecond);
		// 前端控制器则写入操作日志和上传日志
		if (aopClass.getClassType() == EnumClassType.Controller
				|| aopClass.getClassType() == EnumClassType.RestController) {
			actionLog = LogUtil.getActionLog(aopClass);

			// 上传日志
			HttpServletRequest request = ClassUtil.getServletRequest(proceedingJoinPoint);
			if (request != null && !StringUtil.isNullOrWhiteSpace(request.getParameter("uploadLogJson"))) {
				uploadLog = request.getParameter("uploadLogJson");
			}
		}
		map.put(EnumLogType.Run.name(), runLog);
		map.put(EnumLogType.Action.name(), actionLog);
		map.put(EnumLogType.Upload.name(), uploadLog);
		return map;
	}

	/**
	 * catch 结果
	 * 
	 * @param aopClass
	 * @param ex
	 * @return
	 */
	public static Object catchResult(AopClass aopClass, Throwable ex) {
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
