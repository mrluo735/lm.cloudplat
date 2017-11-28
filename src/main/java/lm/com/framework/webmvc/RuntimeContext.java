/**
 * @title RuntimeContext.java
 * @description TODO
 * @package lm.com.framework.webmvc
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年10月12日上午11:31:11
 * @version v1.0
 */
package lm.com.framework.webmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 运行时上下文
 * 
 * @author mrluo735
 *
 */
public class RuntimeContext {
	private static ThreadLocal<RuntimeContext> contextThreadLocal = new ThreadLocal<RuntimeContext>() {
		@Override
		protected RuntimeContext initialValue() {
			return new RuntimeContext();
		}
	};

	private HttpServletRequest httpServletRequest;

	private HttpServletResponse httpServletResponse;

	private String loginName = "";

	private String userAgent = "";

	private boolean isMobile = false;

	private String os = "";

	private String osVersion = "";

	private String browser = "";

	private String browserVersion = "";

	/**
	 * 构造函数
	 */
	public RuntimeContext() {
	}

	/**
	 * 获取HttpServletRequest对象
	 * 
	 * @return
	 */
	public HttpServletRequest getHttpServletRequest() {
		return httpServletRequest;
	}

	/**
	 * 设置HttpServletRequest对象
	 * 
	 * @param httpServletRequest
	 */
	public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
		this.httpServletRequest = httpServletRequest;
	}

	/**
	 * 获取HttpServletResponse对象
	 * 
	 * @return
	 */
	public HttpServletResponse getHttpServletResponse() {
		return httpServletResponse;
	}

	/**
	 * 设置HttpServletResponse对象
	 * 
	 * @param httpServletResponse
	 */
	public void setHttpServletResponse(HttpServletResponse httpServletResponse) {
		this.httpServletResponse = httpServletResponse;
	}

	/**
	 * 获取请求Url
	 * 
	 * @return
	 */
	public String getRequestUrl() {
		if (null != this.httpServletRequest)
			return this.httpServletRequest.getRequestURL().toString();
		return "";
	}

	/**
	 * 获取登录名
	 * 
	 * @return
	 */
	public String getLoginName() {
		return loginName;
	}

	/**
	 * 设置登录名
	 * 
	 * @param loginName
	 */
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	/**
	 * 获取UserAgent
	 * 
	 * @return
	 */
	public String getUserAgent() {
		return userAgent;
	}

	/**
	 * 设置UserAgent
	 * 
	 * @param userAgent
	 */
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	/**
	 * 获取是否移动设备
	 * 
	 * @return
	 */
	public boolean getIsMobile() {
		return isMobile;
	}

	/**
	 * 设置是否移动设备
	 * 
	 * @param isMobile
	 */
	public void setIsMobile(boolean isMobile) {
		this.isMobile = isMobile;
	}

	/**
	 * 获取操作系统
	 * 
	 * @return
	 */
	public String getOs() {
		return os;
	}

	/**
	 * 设置操作系统
	 * 
	 * @param os
	 */
	public void setOs(String os) {
		this.os = os;
	}

	/**
	 * 获取操作系统版本
	 * 
	 * @return
	 */
	public String getOsVersion() {
		return osVersion;
	}

	/**
	 * 设置操作系统版本
	 * 
	 * @param osVersion
	 */
	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	/**
	 * 获取浏览器
	 * 
	 * @return
	 */
	public String getBrowser() {
		return browser;
	}

	/**
	 * 设置浏览器
	 * 
	 * @param browser
	 */
	public void setBrowser(String browser) {
		this.browser = browser;
	}

	/**
	 * 获取浏览器版本
	 * 
	 * @return
	 */
	public String getBrowserVersion() {
		return browserVersion;
	}

	/**
	 * 设置浏览器版本
	 * 
	 * @param browserVersion
	 */
	public void setBrowserVersion(String browserVersion) {
		this.browserVersion = browserVersion;
	}

	/**
	 * 获取上下文
	 * 
	 * @return
	 */
	public static RuntimeContext getContext() {
		return contextThreadLocal.get();
	}

	/**
	 * 设置上下文
	 * 
	 * @param context
	 */
	public static void setContext(RuntimeContext context) {
		contextThreadLocal.set(context);
	}

	/**
	 * 清除
	 */
	public static void clear() {
		contextThreadLocal.remove();
	}
}
