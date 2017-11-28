/**
 * @title HmacSha.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.com.framework.encrypt
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年6月5日上午11:46:17
 * @version v1.0.0
 */
package lm.com.framework.encrypt;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * @ClassName: HmacSha
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年6月5日 上午11:46:17
 * 
 */
public class HmacShaEncrypt {
	/**
	 * 重载+1 HmacSha加密
	 * 
	 * @param encryptStr
	 *            待加密字符串
	 * @param encryptKey
	 *            加密密钥
	 * @param algorithm
	 *            HmacSHA1, HmacSHA224, HmacSHA256, HmacSHA384, HmacSHA512,
	 *            HmacMD5
	 * @return
	 * @throws Exception
	 */
	public static String encode(String encryptStr, String encryptKey, String algorithm) {
		try {
			byte[] text = encryptStr.getBytes("UTF-8");
			byte[] keyData = encryptKey.getBytes("UTF-8");

			SecretKeySpec secretKey = new SecretKeySpec(keyData, algorithm);
			Mac mac = Mac.getInstance(secretKey.getAlgorithm());
			mac.init(secretKey);
			return EncryptUtil.byte2HexString(mac.doFinal(text));
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		} catch (NoSuchAlgorithmException ex) {
			throw new RuntimeException(ex);
		} catch (InvalidKeyException ex) {
			throw new RuntimeException(ex);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 重载+2 HmacSha加密
	 * 
	 * @param encryptStr
	 *            待加密字符串
	 * @param encryptKey
	 *            加密密钥
	 * @param algorithm
	 *            HmacSHA1, HmacSHA224, HmacSHA256, HmacSHA384, HmacSHA512,
	 *            HmacMD5
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	public static String encode(String encryptStr, String encryptKey, String algorithm, String charset) {
		try {
			byte[] text = encryptStr.getBytes(charset);
			byte[] keyData = encryptKey.getBytes(charset);

			SecretKeySpec secretKey = new SecretKeySpec(keyData, algorithm);
			Mac mac = Mac.getInstance(secretKey.getAlgorithm());
			mac.init(secretKey);
			return EncryptUtil.byte2HexString(mac.doFinal(text));
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		} catch (NoSuchAlgorithmException ex) {
			throw new RuntimeException(ex);
		} catch (InvalidKeyException ex) {
			throw new RuntimeException(ex);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * HmacSha1 加密
	 * 
	 * @param encryptText
	 *            待加密字符串
	 * @param encryptKey
	 *            加密密钥
	 * @return
	 * @throws Exception
	 */
	public static String hmacSha1(String encryptStr, String encryptKey) {
		return encode(encryptStr, encryptKey, "HmacSha1");
	}

	/**
	 * HmacSha224 加密
	 * 
	 * @param encryptText
	 *            待加密字符串
	 * @param encryptKey
	 *            加密密钥
	 * @return
	 * @throws Exception
	 */
	public static String hmacSha224(String encryptStr, String encryptKey) {
		return encode(encryptStr, encryptKey, "HmacSha224");
	}

	/**
	 * HmacSha256 加密
	 * 
	 * @param encryptText
	 *            待加密字符串
	 * @param encryptKey
	 *            加密密钥
	 * @return
	 * @throws Exception
	 */
	public static String hmacSha256(String encryptStr, String encryptKey) throws Exception {
		return encode(encryptStr, encryptKey, "HmacSha256");
	}

	/**
	 * HmacSha384 加密
	 * 
	 * @param encryptText
	 *            待加密字符串
	 * @param encryptKey
	 *            加密密钥
	 * @return
	 * @throws Exception
	 */
	public static String hmacSha384(String encryptStr, String encryptKey) throws Exception {
		return encode(encryptStr, encryptKey, "HmacSha384");
	}

	/**
	 * HmacSha512 加密
	 * 
	 * @param encryptText
	 *            待加密字符串
	 * @param encryptKey
	 *            加密密钥
	 * @return
	 * @throws Exception
	 */
	public static String hmacSha512(String encryptStr, String encryptKey) throws Exception {
		return encode(encryptStr, encryptKey, "HmacSha512");
	}

	/**
	 * HmacMD5 加密
	 * 
	 * @param encryptText
	 *            待加密字符串
	 * @param encryptKey
	 *            加密密钥
	 * @return
	 * @throws Exception
	 */
	public static String hmacMD5(String encryptStr, String encryptKey) throws Exception {
		return encode(encryptStr, encryptKey, "HmacMD5");
	}
}
