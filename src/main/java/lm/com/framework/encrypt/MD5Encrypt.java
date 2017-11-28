/**   
* @Title: md5Encrypt.java 
* @Package lm.com.framework.encrypt 
* @Description: md5加密
* @author mrluo735   
* @date 2016-07-07 15:02:25
* @version V1.0   
*/
package lm.com.framework.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @ClassName: md5加密
 * @Description: TODO
 * @author mrluo735
 * @date 2016-07-07 15:02:25
 */
public class MD5Encrypt {
	/**
	 * 16位加密
	 * 
	 * @param encryptString
	 *            待加密字符串
	 * @return 加密后字符串
	 */
	public static final String encode16(String encryptString) {
		return md5(encryptString, false);
	}

	/**
	 * 32位加密
	 * 
	 * @param encryptString
	 *            待加密字符串
	 * @return 加密后字符串
	 */
	public static final String encode32(String encryptString) {
		return md5(encryptString, true);
	}

	/**
	 * md5位加密
	 * 
	 * @param encryptString
	 *            待加密字符串
	 * @param is32
	 *            是否32位长度
	 * @return 加密后字符串
	 */
	private static final String md5(String encryptString, boolean is32) {
		StringBuffer buf = new StringBuffer("");
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(encryptString.getBytes());
			byte b[] = md5.digest();
			int i;
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			// System.out.println("32位result: " + buf.toString());//32位加密
			// System.out.println("16位result: " +
			// buf.toString().substring(8,24));//16位加密

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (is32)
			return buf.toString();
		return buf.toString().substring(8, 24);
	}

	/**
	 * 计算md5值
	 * 
	 * @param byteArray
	 * @return
	 */
	public static String encode(byte[] byteArray) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(byteArray);
			byte[] bytes = md5.digest();
			return EncryptUtil.byte2HexString(bytes);
		} catch (NoSuchAlgorithmException ex) {
			throw new RuntimeException(ex);
		}
	}
}
