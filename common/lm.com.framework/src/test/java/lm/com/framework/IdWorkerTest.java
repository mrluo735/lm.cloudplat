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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import org.junit.Test;

import lm.com.framework.encrypt.Base64Encrypt;
import lm.com.framework.id.IdWorker;
import lm.com.framework.id.ObjectId;

/**
 * @author Administrator
 *
 */
public class IdWorkerTest {
	@Test
	public void test1() throws NoSuchAlgorithmException, UnsupportedEncodingException, Exception {
		//IdWorker idWorker = new IdWorker(1, 1);		
		//System.out.println(idWorker.nextId());
//		String ip = "3658237850";
//		String timestamp = "1496911832";
//		String fileSize = "2010200";
//		String fileCrc32 = "4094583719";
//		System.out.println(MAryUtil.tenTo64hex(3658237850L));
//		System.out.println(MAryUtil.tenTo64hex(1496911832L));
//		System.out.println(MAryUtil.tenTo64hex(2010200L));
//		System.out.println(MAryUtil.tenTo64hex(4094583719L));
//		System.out.println(MAryUtil.hex64ToTen("3Q34UQ"));
//
//		System.out.println(MAryUtil.tenTo62hex(10000L));
//		System.out.println(MAryUtil.hex62ToTen("2Bi"));
		
		ObjectId oId = ObjectId.newObjectId();
		System.out.println(oId.toString());
	}
}
