/**
 * @title AuthenticationFilter.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.cloud.storage.server.filter
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年6月14日上午10:34:17
 * @version v1.0.0
 */
package lm.cloud.storage.server.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import lm.cloud.storage.server.configurer.StorageConfig;
import lm.com.framework.LongUtil;
import lm.com.framework.StringUtil;
import lm.com.framework.WebUtil;
import lm.com.framework.encrypt.Base64Encrypt;
import lm.com.framework.encrypt.MD5Encrypt;

/**
 * @ClassName: AuthenticationFilter
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年6月14日 上午10:34:17
 * 
 */
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class AuthenticationFilter implements Filter {
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

	private static final String TOKEN_NAME = "UPLOAD_TOKEN";

	@Autowired
	private StorageConfig storageConfig;

	/**
	 * 
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	/**
	 * 
	 */
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		if (request.getRequestURI() == "/" || request.getRequestURI().equals("/index") || request.getRequestURI().equalsIgnoreCase("/crossdomain.xml")) {
			chain.doFilter(servletRequest, servletResponse);
			return;
		}

		String token = request.getHeader(TOKEN_NAME);
		if (StringUtil.isNullOrWhiteSpace(token))
			token = request.getParameter(TOKEN_NAME);

		if (StringUtil.isNullOrWhiteSpace(token)) {
			logger.info("没有找到上传token");
			this.returnResponse(response, "没有找到上传token");
			return;
		}
		String deToken = Base64Encrypt.decode2String(token);
		String keySecret = MD5Encrypt
				.encode32(String.format("%s:%s", this.storageConfig.appKey, this.storageConfig.appSecret));
		String tokenKey = deToken.substring(0, 32);
		if (!keySecret.equalsIgnoreCase(tokenKey)) {
			logger.info("无效的上传token值");
			this.returnResponse(response, "无效的上传token值");
			return;
		}
		long tokenLong = LongUtil.toLong(deToken.substring(32), 0L);
		// token过期 (有效期默认为:1小时)
		if (System.currentTimeMillis() - tokenLong > this.storageConfig.tokenValidity) {
			logger.info("上传token已过期");
			this.returnResponse(response, "上传token已过期");
			return;
		}
		chain.doFilter(servletRequest, servletResponse);
	}

	/**
	 * 
	 */
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	private void returnResponse(HttpServletResponse response, String message) {
		if (response.isCommitted())
			return;

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", HttpStatus.BAD_REQUEST);
		result.put("message", message);

		response.setStatus(HttpStatus.ACCEPTED.value());
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		response.setCharacterEncoding("UTF-8");
		WebUtil.setDisableCacheHeader(response);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			int length = JSON.writeJSONString(outputStream, Charset.forName("UTF-8"), result, new SerializerFeature[0]);
			response.setContentLengthLong(length);
			outputStream.writeTo(response.getOutputStream());
			outputStream.flush();
			response.getOutputStream().close();
		} catch (IOException ex) {
		} finally {
			try {
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
