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
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

/**
 * @author Administrator
 *
 */
public class StringUtilTest {
	@Test
	public void test1() throws NoSuchAlgorithmException, UnsupportedEncodingException, Exception {
		Long i = null;
		BigInteger.valueOf(i);
		Object val = null;
		//String v = val.toString();
		String s = (String) val;
		
		String column = "[id, n]am[e][d]";
		System.out.println(StringUtil.trim(column, "[]"));
		System.out.println(StringUtil.trimEnd("sdfsdfsd AND AND OAD ".trim(), "ANDOR").trim());
	}
}
