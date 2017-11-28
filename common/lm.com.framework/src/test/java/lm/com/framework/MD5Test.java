package lm.com.framework;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lm.com.framework.encrypt.AESEncrypt;

@SuppressWarnings("unused")
public class MD5Test {
	private static Logger logger = LoggerFactory.getLogger(MD5Test.class);

	@Test
	public void test1() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		/*
		 * String md516 = MD5Encrypt.encode16("123456");
		 * System.out.println(md516);
		 * 
		 * String md532 = MD5Encrypt.encode32("123456");
		 * System.out.println(md532);
		 * 
		 * String md564 = MD5Encrypt.encode64("123456");
		 * System.out.println(md564);
		 */

		String content = "你好啊，加密呢We recommend you use a mirror to download our release builds, but you must verify the integrity of the downloaded files using signatures downloaded from our main distribution directories. Recent releases (48 hours) may not yet be available from the mirrors.You are currently using http://apache.fayea.com/. If you encounter a problem with this mirror, please select another mirror. If all mirrors are failing, there are backup mirrors (at the end of the mirrors list) that should be available. ";
		String password = "SHA1_A4D9FF3EF5EAB7B17B4259D060ACE8FA30596ADCBBC8324C1B83F3D8DCF09D3F9F226A60809F0A5BB8498729C5A24DC8CD3ADF4C7E438D73D539B108DC5EB56A_AES_A65FB2471C1FC5803711C15332DB45DA7CB5BECC8BA6CC6F";
		// 加密
		System.out.println("加密前：" + content);
		String encryptResult = AESEncrypt.encode(content, password);
		// String tt4 = Base64.encodeBase64String(encryptResult);
		System.out.println(encryptResult);

		// 解密
		String decryptResult = AESEncrypt.decode(encryptResult, password);
		System.out.println("解密后：" + decryptResult);
	}
}
