/**
 * @title ShaEncrypt.java
 * @description TODO
 * @package lm.com.framework.encrypt
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年11月1日上午10:58:33
 * @version v1.0
 */
package lm.com.framework.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Sha加密
 * 
 * @author mrluo735
 *
 */
public class ShaEncrypt {
	/**
	 * sha加密
	 * 
	 * @param source
	 * @param SHA-1,
	 *            SHA-224, SHA-256, SHA-384, SHA-512
	 * @return
	 */
	public static String encode(String source, String algorithm) {
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			md.update(source.getBytes());
			byte[] bytes = md.digest();
			return EncryptUtil.byte2HexString(bytes);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * sha1加密
	 * 
	 * @param source
	 * @return
	 */
	public static String sha1(String source) {
		return encode(source, "SHA-1");
	}

	/**
	 * sha224加密
	 * 
	 * @param source
	 * @return
	 */
	public static String sha224(String source) {
		return encode(source, "SHA-224");
	}

	/**
	 * sha256加密
	 * 
	 * @param source
	 * @return
	 */
	public static String sha256(String source) {
		return encode(source, "SHA-256");
	}

	/**
	 * sha384加密
	 * 
	 * @param source
	 * @return
	 */
	public static String sha384(String source) {
		return encode(source, "SHA-384");
	}

	/**
	 * sha512加密
	 * 
	 * @param source
	 * @return
	 */
	public static String sha512(String source) {
		return encode(source, "SHA-512");
	}
}
