/**
 * @title CsrfHeaderFilter.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.cloud.oauth
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年5月12日上午10:26:54
 * @version v1.0.0
 */
package lm.cloud.zuul.server.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

/** 
 * @ClassName: CsrfHeaderFilter 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author mrluo735 
 * @date 2017年5月12日 上午10:26:54 
 *  
 */
public class CsrfHeaderFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
		if (csrf != null) {
			Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
			String token = csrf.getToken();
			if (cookie == null || token != null && !token.equals(cookie.getValue())) {
				cookie = new Cookie("XSRF-TOKEN", token);
				cookie.setPath("/");
				response.addCookie(cookie);
			}
		}
		filterChain.doFilter(request, response);
	}
}
