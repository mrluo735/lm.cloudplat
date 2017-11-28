/**
 * @title QueryStringParser.java
 * @description TODO
 * @package lm.com.framework.webmvc
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年1月6日下午6:19:18
 * @version v1.0
 */
package lm.com.framework.webmvc;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

/**
 * queryString解析器
 * <p>
 * 有序解析queryString
 * </p>
 * 
 * @author mrluo735
 *
 */
public class QueryStringParser {
	private Map<String, String[]> parameterMap = new LinkedHashMap<>();

	/**
	 * 重载+1 构造函数
	 * 
	 * @param queryString
	 */
	public QueryStringParser(String queryString) {
		this.parse(queryString);
	}

	/**
	 * 重载+2 构造函数
	 * 
	 * @param request
	 */
	public QueryStringParser(HttpServletRequest request) {
		String queryString = request.getQueryString();
		if (queryString == null)
			return;

		this.parse(queryString);
	}

	/**
	 * 获取parameterMap
	 * 
	 * @return
	 */
	public Map<String, String[]> getParameterMap() {
		return this.parameterMap;
	}

	/**
	 * 获取parameter
	 * <p>
	 * 如果有多个相同key对应的不同value,返回第一个
	 * <p>
	 * 
	 * @param key
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String getParameter(String key) throws UnsupportedEncodingException {
		if (this.parameterMap.size() < 1)
			return "";
		else
			return this.parameterMap.get(key)[0];
	}

	/**
	 * 解析queryString
	 * 
	 * @param queryString
	 */
	private void parse(String queryString) {
		if (queryString == null)
			return;

		StringTokenizer st = new StringTokenizer(queryString, "&");
		while (st.hasMoreTokens()) {
			String pairs = st.nextToken();
			String key = pairs.substring(0, pairs.indexOf('='));
			String value = pairs.substring(pairs.indexOf('=') + 1);

			if (!this.parameterMap.containsKey(key)) {
				this.parameterMap.put(key, new String[] { value });
				continue;
			}

			boolean exists = false;
			String[] values = this.parameterMap.get(key);
			for (String item : values) {
				if (item.equals(value)) {
					exists = true;
					break;
				}
			}

			if (exists)
				continue;
			// 新值
			String[] newValues = new String[values.length + 1];
			System.arraycopy(values, 0, newValues, 0, values.length);
			newValues[values.length] = value;
			this.parameterMap.put(key, newValues);
		}
	}
}
