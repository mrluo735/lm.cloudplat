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
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * @author Administrator
 *
 */
public class PagerTest {
	@Test
	public void test1() throws NoSuchAlgorithmException, UnsupportedEncodingException, Exception {
		final Map<String, Object> values = new HashMap<>();		
		values.put("a", "111");
		
		System.out.println(values.get("a"));
	}
}
