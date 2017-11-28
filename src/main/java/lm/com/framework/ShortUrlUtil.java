/**
 * @title ShortUrlUtil.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.com.framework
 * @author mrluo735
 * @since JDK1.8
 * @date 2017年6月26日下午5:12:06
 * @version v1.0.0
 */
package lm.com.framework;

import java.util.regex.Pattern;

import lm.com.framework.encrypt.MD5Encrypt;

/**
 * @ClassName: ShortUrlUtil
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年6月26日 下午5:12:06
 * 
 */
public class ShortUrlUtil {
	/**
	 * 转换成短链接
	 * <p>
	 * 1.将"原始链接（长链接）+ key(自定义字符串,防止算法泄漏)"MD5加密<br />
	 * 2.把加密字符按照 8 位一组 16 进制与 0x3FFFFFFF 进行位与运算，把得到的值与 0x0000003D 进行位与运算，取得字符数组 chars 索引，把取得的字符相加，每次循环按位右移 5
	 * 位，把字符串存入对应索引的输出数组(4组6位字符串)<br />
	 * 3.从4组6位字符串的数组中取出任意一个，作为短链
	 * 
	 * @param key
	 *            加密盐值
	 * @param url
	 *            待转换url
	 * @return
	 */
	public static String[] convert(String key, String url) {
		// 可以自定义生成 MD5 加密字符传前的混合 KEY
		// 要使用生成 URL 的字符
		String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p",
				"q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A",
				"B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
				"W", "X", "Y", "Z" };

		// 对url进行 MD5 加密
		String hex = MD5Encrypt.encode32(String.format("%s%s", url, key));
		String[] resUrl = new String[4];
		for (int i = 0; i < 4; i++) {
			// 把加密字符按照 8 位一组 16 进制与 0x3FFFFFFF 进行位与运算
			String sTempSubString = hex.substring(i * 8, i * 8 + 8);
			// 这里需要使用 long 型来转换，因为 Inteper .parseInt() 只能处理 31 位 ,
			// 首位为符号位 , 如果不用long ，则会越界
			long lHexLong = 0x3FFFFFFF & Long.parseLong(sTempSubString, 16);
			String outChars = "";
			for (int j = 0; j < 6; j++) {
				// 把得到的值与 0x0000003D 进行位与运算，取得字符数组 chars 索引
				long index = 0x0000003D & lHexLong;
				// 把取得的字符相加
				outChars += chars[(int) index];
				// 每次循环按位右移 5 位
				lHexLong = lHexLong >> 5;
			}

			// 把字符串存入对应索引的输出数组
			resUrl[i] = outChars;
		}
		return resUrl;
	}

	/**
	 * 解析短地址
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isShortUrl(String url) {
		String shortCode = parseShortUrl(url);
		return StringUtil.isNullOrWhiteSpace(shortCode) ? false : true;
	}

	/**
	 * 解析短地址
	 * 
	 * @param url
	 * @return
	 */
	public static String parseShortUrl(String url) {
		// 示例：http://www.lmplat.com/efa3fo
		int index = url.lastIndexOf("/");
		if (index < 0)
			return StringUtil.Empty;

		String shortCode = url.substring(index + 1);
		if (StringUtil.isNullOrWhiteSpace(shortCode))
			return StringUtil.Empty;

		Pattern pattern = Pattern.compile("([0-9a-zA-Z]{6})", Pattern.CASE_INSENSITIVE);
		if (!pattern.matcher(shortCode).matches())
			return StringUtil.Empty;
		return shortCode;
	}
}
