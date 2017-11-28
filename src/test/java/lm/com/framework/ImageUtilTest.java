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

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;

import lm.com.framework.image.ImageResizeUtil;
import lm.com.framework.image.ImageUtil;
import lm.com.framework.image.ImageWaterMarkUtil;

/**
 * @author Administrator
 *
 */
public class ImageUtilTest {
	@Test
	public void test1() throws NoSuchAlgorithmException, UnsupportedEncodingException, Exception {
		File originalFile = new File("C:/Users/Administrator/Pictures/1001.jpg");
		File resizedFile = new File("C:/Users/Administrator/Pictures/1001_200x200.jpg");

		File originalFile2 = new File("C:/Users/Administrator/Pictures/1003.gif");
		File resizedFile2 = new File("C:/Users/Administrator/Pictures/1003_200x200.gif");
		// ImageResizeUtil.resize(originalFile2, resizedFile2, 200, 200);

		// ImageWaterMarkUtil.addWaterMark("C:/Users/Administrator/Pictures/1001.jpg",
		// "C:/Users/Administrator/Pictures/l01.png",
		// "C:/Users/Administrator/Pictures/1001_wm.jpg", 10, 10, 1,
		// 45);
		ImageWaterMarkUtil.addWaterMark("C:/Users/Administrator/Pictures/1001.jpg",
				"C:/Users/Administrator/Pictures/1001_vm2.jpg", "我爱你中国", "宋体", Font.PLAIN, 18, Color.RED, 10, 10, 0.8f,
				315);
	}
}
