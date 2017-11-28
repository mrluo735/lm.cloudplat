package lm.com.framework;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Request 帮助类
 * 
 * @author mrluo735
 *
 */
public class RequestUtil {
	private static final Logger logger = LoggerFactory.getLogger(RequestUtil.class);
	private static final String URI_TEMPLATE_VARIABLES_ATTRIBUTE = "org.springframework.web.servlet.HandlerMapping.uriTemplateVariables";

	/**
	 * 将输入流转成String
	 * 
	 * @param request
	 * @return
	 */
	public static String inputStream2String(HttpServletRequest request) {
		byte[] b = null;
		try {
			ServletInputStream servletInputStream = request.getInputStream();
			int size = request.getContentLength();
			b = new byte[size];
			for (int i = 0; i < b.length; i++) {
				b[i] = (byte) servletInputStream.read();
			}
		} catch (Exception e) {
			logger.error("将输入流转成String出错", e);
		}
		return new String(b);
	}

	/**
	 * 重载+1 获取 PathVariable 参数
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> getPathVariable(HttpServletRequest request) {
		Object attribute = request.getAttribute(URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		if (null != attribute)
			return (Map<String, String>) attribute;
		return new HashMap<>();
	}

	/**
	 * 重载+2 获取 PathVariable 参数
	 * 
	 * @param request
	 * @param key
	 * @return
	 */
	public static String getPathVariable(HttpServletRequest request, String key) {
		Map<String, String> map = getPathVariable(request);
		return map.get(key);
	}
}
