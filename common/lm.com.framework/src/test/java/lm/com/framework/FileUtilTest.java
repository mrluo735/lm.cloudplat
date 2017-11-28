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

import lm.com.framework.io.FileUtil;

/**
 * @author Administrator
 *
 */
public class FileUtilTest {
	@Test
	public void test1() throws NoSuchAlgorithmException, UnsupportedEncodingException, Exception {
		FileUtil.createDir("E:/me/流梦架构/lm.cloudplat.v2017/lm.cloudplat/presentation/lm.res.com/target/classes/94/32");
		
		System.out.println();
	}
}
