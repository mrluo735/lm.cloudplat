package lm.com.framework;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cookie 操作辅助类
 * 
 * @author Administrator
 *
 */
public class CookieUtil {
	/**
	 * 设置cookie,如果cookie存在则更新
	 * 
	 * @param response
	 * @param name
	 *            cookie名字
	 * @param value
	 *            cookie值
	 * @param maxAge
	 *            cookie生命周期 以秒为单位(0:立即删除。负数:当浏览器关闭时自动删除)
	 * @param domain
	 *            cookie域
	 */
	public static void setCookie(HttpServletResponse response, String name, String value, int maxAge, String domain) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setMaxAge(maxAge);
		if (!StringUtil.isNullOrWhiteSpace(domain)) {
			cookie.setDomain(domain);
		}
		response.addCookie(cookie);
	}

	/**
	 * 获取Cookie
	 * 
	 * @param request
	 * @param cookieName
	 * @return
	 */
	public Cookie getCookie(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		Cookie cookie = null;
		try {
			if (cookies != null && cookies.length > 0) {
				for (int i = 0; i < cookies.length; i++) {
					cookie = cookies[i];
					if (cookie.getName().equals(cookieName)) {
						return cookie;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cookie;
	}

	public String getCookieValue(HttpServletRequest request, String cookieName) {
		Cookie cookie = getCookie(request, cookieName);
		if (cookie != null) {
			return cookie.getValue();
		}
		return "";
	}

	/**
	 * 删除Cookie
	 * 
	 * @param request
	 * @param response
	 * @param cookieName
	 */
	public void deleteCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
		Cookie cookie = getCookie(request, cookieName);
		if (cookie != null) {
			cookie.setMaxAge(0);// 如果0，就说明立即删除
			cookie.setPath("/");// 不要漏掉
			response.addCookie(cookie);
		}
	}
}
