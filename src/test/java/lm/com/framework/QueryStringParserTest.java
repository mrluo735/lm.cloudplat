/**
 * @title StringUtilTest.java
 * @description TODO
 * @package lm.com.framework
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年12月27日上午9:46:10
 * @version v1.0
 */
package lm.com.framework;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

import lm.com.framework.webmvc.QueryStringParser;

/**
 * @author Administrator
 *
 */
public class QueryStringParserTest {
	@Test
	public void test1() throws NoSuchAlgorithmException, UnsupportedEncodingException, Exception {
		String queryString = "grp-1=1&name=1&code=a&code=a&remark=abcd&grp-1_=AND";
		QueryStringParser qsParser = new QueryStringParser(queryString);
		
		System.out.println(qsParser.getParameter("name"));
		System.out.println(qsParser.getParameter("remark"));		
		System.out.println(qsParser.getParameterMap().get("code").length);
	}
}
