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
import java.util.Locale;

import org.junit.Test;

/**
 * @author Administrator
 *
 */
public class JavaUtilTest {
	@Test
	public void test1() throws NoSuchAlgorithmException, UnsupportedEncodingException, Exception {
		System.out.println(JavaUtil.getJavaClassPath());
		System.out.println(this.getClass().getResource("/"));
		System.out.println(ClassLoader.getSystemResource(""));
		System.out.println(ThreadUtil.getCurrentClassName(2));
		System.out.println(ThreadUtil.getCurrentMethodName(2));
		System.out.println(new Locale("zh", "CN"));
		
		System.out.println("a.b.c".split("\\.").length);
	}
}
