/**
 * @title Crc32Encrypt.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.com.framework.encrypt
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年6月8日下午4:16:58
 * @version v1.0.0
 */
package lm.com.framework.encrypt;

import java.io.UnsupportedEncodingException;
import java.util.zip.CRC32;

/**
 * @ClassName: Crc32Encrypt
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年6月8日 下午4:16:58
 * 
 */
public final class Crc32Encrypt {
	/**
	 * 重载+1 计算CRC32值
	 * 
	 * @param str
	 * @return
	 */
	public static long encode(String str) {
		try {
			CRC32 crc32 = new CRC32();
			crc32.update(str.getBytes("UTF-8"));
			return crc32.getValue();
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 重载+2 计算CRC32值
	 * 
	 * @param byteArray
	 * @return
	 */
	public static long encode(byte[] byteArray) {
		CRC32 crc32 = new CRC32();
		crc32.update(byteArray);
		return crc32.getValue();
	}
}
