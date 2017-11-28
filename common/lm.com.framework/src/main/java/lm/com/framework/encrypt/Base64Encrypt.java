package lm.com.framework.encrypt;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import lm.com.framework.StringUtil;
import sun.misc.*;

/**
 * Base64加解密
 * 
 * @author mrluo735
 *
 */
public class Base64Encrypt {
	private static final char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="
			.toCharArray();

	static private byte[] codes = new byte[256];
	static {
		for (int i = 0; i < 256; i++)
			codes[i] = -1;
		for (int i = 'A'; i <= 'Z'; i++)
			codes[i] = (byte) (i - 'A');
		for (int i = 'a'; i <= 'z'; i++)
			codes[i] = (byte) (26 + i - 'a');
		for (int i = '0'; i <= '9'; i++)
			codes[i] = (byte) (52 + i - '0');
		codes['+'] = 62;
		codes['/'] = 63;
	}

	/**
	 * base64编码
	 * <p>
	 * 原生实现
	 * </p>
	 * 
	 * @param data
	 * @return
	 */
	public static char[] encodeNative(byte[] data) {
		char[] out = new char[((data.length + 2) / 3) * 4];

		for (int i = 0, index = 0; i < data.length; i += 3, index += 4) {
			boolean quad = false;
			boolean trip = false;
			int val = (0xFF & (int) data[i]);
			val <<= 8;
			if ((i + 1) < data.length) {
				val |= (0xFF & (int) data[i + 1]);
				trip = true;
			}
			val <<= 8;
			if ((i + 2) < data.length) {
				val |= (0xFF & (int) data[i + 2]);
				quad = true;
			}
			out[index + 3] = alphabet[(quad ? (val & 0x3F) : 64)];
			val >>= 6;
			out[index + 2] = alphabet[(trip ? (val & 0x3F) : 64)];
			val >>= 6;
			out[index + 1] = alphabet[val & 0x3F];
			val >>= 6;
			out[index + 0] = alphabet[val & 0x3F];
		}
		return out;
	}

	/**
	 * base64解码
	 * <p>
	 * 原生实现
	 * </p>
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] decodeNative(char[] data) {
		int len = ((data.length + 3) / 4) * 3;
		if (data.length > 0 && data[data.length - 1] == '=')
			--len;
		if (data.length > 1 && data[data.length - 2] == '=')
			--len;
		byte[] out = new byte[len];
		int shift = 0;
		int accum = 0;
		int index = 0;
		for (int ix = 0; ix < data.length; ix++) {
			int value = codes[data[ix] & 0xFF];
			if (value >= 0) {
				accum <<= 6;
				shift += 6;
				accum |= value;
				if (shift >= 8) {
					shift -= 8;
					out[index++] = (byte) ((accum >> shift) & 0xff);
				}
			}
		}
		if (index != out.length)
			throw new Error("miscalculated data length!");
		return out;
	}

	/**
	 * 加密
	 * 
	 * @param value
	 * @return
	 */
	public static String encode(String value) {
		try {
			return encode(value.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	/**
	 * 加密
	 * 
	 * @param value
	 * @return
	 */
	@SuppressWarnings("restriction")
	public static String encode(byte[] value) {
		return new BASE64Encoder().encode(value);
	}

	/**
	 * 解密
	 * 
	 * @param value
	 * @return
	 */
	@SuppressWarnings("restriction")
	public static byte[] decode(String value) {
		if (StringUtil.isNullOrWhiteSpace(value))
			return null;
		try {
			BASE64Decoder decoder = new BASE64Decoder();
			return decoder.decodeBuffer(value);
		} catch (IOException ex) {
			return null;
		}
	}

	/**
	 * 解密
	 * 
	 * @param value
	 * @return
	 */
	public static String decode2String(String value) {
		byte[] bytes = decode(value);
		if (bytes == null)
			return "";

		try {
			return new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
}
