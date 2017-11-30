/**
 * @title AppTest.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.cloud.oauth.server
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年5月16日上午11:36:11
 * @version v1.0.0
 */
package lm.cloud.oauth.server;

import org.springframework.security.crypto.password.StandardPasswordEncoder;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @ClassName: AppTest
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年5月16日 上午11:36:11
 * 
 */
public class AppTest extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		System.out.println(new StandardPasswordEncoder().encode("lm"));
		assertTrue(true);
	}
}