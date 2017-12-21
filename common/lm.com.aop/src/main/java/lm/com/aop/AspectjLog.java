/**
 * @title AspectjLog.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.pro.aop
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年5月2日上午10:49:54
 * @version v1.0.0
 */
package lm.com.aop;

import java.util.HashMap;
import java.util.Map;

import lm.com.framework.DateTimeUtil;
import lm.com.framework.JsonUtil;
import lm.com.framework.StringUtil;
import lm.com.framework.WebUtil;
import lm.com.framework.webmvc.RuntimeContext;

/**
 * @ClassName: AspectjLog
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年5月2日 上午10:49:54
 * 
 */
public class AspectjLog {
	/**
	 * 执行开销日志
	 * 
	 * @param aopClass
	 * @param totalMillisecond
	 */
	public static String getRunLog(AopClass aopClass, long totalMillisecond) {
		RuntimeContext runtimeContext = RuntimeContext.getContext();
		if (null == runtimeContext)
			runtimeContext = new RuntimeContext();
		String ip = WebUtil.getClientIP(runtimeContext.getHttpServletRequest());
		String source = getSource(aopClass.getPackageName(), aopClass.getMethodFullName(),
				aopClass.getMethodParameter());
		String message = getRunMessage(aopClass.getMethodFullName(), totalMillisecond);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", "");
		map.put("appName", aopClass.getPackageName());
		map.put("ip", ip);
		map.put("ipLocation", "");
		map.put("userAgent", runtimeContext.getUserAgent());
		map.put("isMobile", runtimeContext.getIsMobile());
		map.put("os", runtimeContext.getOs() + runtimeContext.getOsVersion());
		map.put("browser", runtimeContext.getBrowser() + runtimeContext.getBrowserVersion());
		map.put("url", runtimeContext.getRequestUrl());
		map.put("source", source);
		map.put("message", message);
		map.put("stack", "");
		map.put("totalMillisecond", totalMillisecond);
		map.put("createOn", DateTimeUtil.getNow(DateTimeUtil.DEFAULT_PATTERN));
		map.put("remark", "");
		return JsonUtil.toJsonUseJackson(map);
	}

	/**
	 * 行为操作日志
	 * 
	 * @param aopClass
	 */
	public static String getActionLog(AopClass aopClass) {
		RuntimeContext runtimeContext = RuntimeContext.getContext();
		if (null == runtimeContext)
			runtimeContext = new RuntimeContext();
		String ip = WebUtil.getClientIP(runtimeContext.getHttpServletRequest());
		String source = getSource(aopClass.getPackageName(), aopClass.getMethodFullName(),
				aopClass.getMethodParameter());
		String message = getActionMessage(aopClass.getClassType(), runtimeContext.getLoginName(),
				aopClass.getMethodFullName(), aopClass.getMethodDescription());

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", "");
		map.put("appName", aopClass.getPackageName());
		map.put("ip", ip);
		map.put("ipLocation", "");
		map.put("userAgent", runtimeContext.getUserAgent());
		map.put("isMobile", runtimeContext.getIsMobile());
		map.put("os", runtimeContext.getOs() + runtimeContext.getOsVersion());
		map.put("browser", runtimeContext.getBrowser() + runtimeContext.getBrowserVersion());
		map.put("url", runtimeContext.getRequestUrl());
		map.put("source", source);
		map.put("message", message);
		map.put("createOn", DateTimeUtil.getNow(DateTimeUtil.DEFAULT_PATTERN));
		map.put("remark", "");
		return JsonUtil.toJsonUseJackson(map);
	}

	/**
	 * 行为操作日志
	 * 
	 * @param aopClass
	 * @param message
	 */
	public static String getErrorLog(AopClass aopClass, String message) {
		RuntimeContext runtimeContext = RuntimeContext.getContext();
		if (null == runtimeContext)
			runtimeContext = new RuntimeContext();
		String ip = WebUtil.getClientIP(runtimeContext.getHttpServletRequest());
		String source = getSource(aopClass.getPackageName(), aopClass.getMethodFullName(),
				aopClass.getMethodParameter());

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", "");
		map.put("appName", aopClass.getPackageName());
		map.put("ip", ip);
		map.put("ipLocation", "");
		map.put("userAgent", runtimeContext.getUserAgent());
		map.put("isMobile", runtimeContext.getIsMobile());
		map.put("os", runtimeContext.getOs() + runtimeContext.getOsVersion());
		map.put("browser", runtimeContext.getBrowser() + runtimeContext.getBrowserVersion());
		map.put("url", runtimeContext.getRequestUrl());
		map.put("source", source);
		map.put("message", message);
		map.put("stack", "");
		map.put("createOn", DateTimeUtil.getNow(DateTimeUtil.DEFAULT_PATTERN));
		map.put("remark", "");
		return JsonUtil.toJsonUseJackson(map);
	}

	// /**
	// * 获取上传日志
	// *
	// * @param aopClass
	// * @return
	// */
	// public static String getUploadLog(AopClass aopClass) {
	// Map<String, Object> map = new HashMap<>();
	// map.put("id", "");
	// map.put("tenantId", "");
	// map.put("type", "");
	// map.put("orginalName", "");
	// map.put("name", "");
	// map.put("nameWithoutExtension", "");
	// map.put("extension", "");
	// map.put("mimeType", "");
	// map.put("url", "");
	// map.put("urlWithouDomain", "");
	// map.put("size", "");
	// map.put("width", "");
	// map.put("height", "");
	// map.put("duration", "");
	// map.put("hash", "");
	// map.put("buckeName", "");
	// map.put("uploadOn", "");
	// map.put("createOn", System.currentTimeMillis());
	// return JsonUtil.toJsonUseJackson(map);
	// }

	private static String getSource(String packageName, String methodFullName, String parameter) {
		String source = String.format("包名=%s\n方法名=%s\n参数=%s", packageName, methodFullName, parameter);
		return source;
	}

	private static String getRunMessage(String methodFullName, long totalMillisecond) {
		String message = String.format("执行方法[%s]耗时 %s 毫秒", methodFullName, totalMillisecond);
		return message;
	}

	private static String getActionMessage(EnumClassType classType, String loginName, String methodFullName,
			String methodDescription) {
		if (StringUtil.isNullOrWhiteSpace(loginName))
			loginName = "游客";
		String message = String.format("用戶[%s]操作了[%s(%s)]", loginName, methodDescription, methodFullName);
		if (classType == EnumClassType.Common)
			message = String.format("用戶[%s]访问了[%s(%s)]", loginName, methodDescription, methodFullName);
		return message;
	}
}
