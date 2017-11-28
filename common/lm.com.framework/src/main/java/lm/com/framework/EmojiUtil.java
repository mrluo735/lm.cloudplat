/**
 * @title EmojiUtil.java
 * @description TODO
 * @package lm.com.framework
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年11月9日下午5:15:25
 * @version v1.0
 */
package lm.com.framework;

public class EmojiUtil {
	/**
	 * 检测是否有emoji字符
	 * 
	 * @param source
	 * @return
	 */
	public static boolean contains(String source) {
		if (StringUtil.isNullOrWhiteSpace(source)) {
			return false;
		}

		int len = source.length();

		for (int index = 0; index < len; index++) {
			char codePoint = source.charAt(index);

			if (is(codePoint)) {
				// do nothing，判断到了这里表明，确认有表情字符
				return true;
			}
		}

		return false;
	}

	/**
	 * 是否为emoji字符
	 * 
	 * @param codePoint
	 * @return
	 */
	public static boolean is(char codePoint) {
		return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD)
				|| ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
				|| ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
	}

	/**
	 * 过滤emoji 或者 其他非文字类型的字符
	 * 
	 * @param source
	 * @return
	 */
	public static String filter(String source) {
		if (!contains(source)) {
			return source;// 如果不包含，直接返回
		}
		// 到这里铁定包含
		StringBuilder buf = null;

		int len = source.length();

		for (int index = 0; index < len; index++) {
			char codePoint = source.charAt(index);

			if (is(codePoint)) {
				if (buf == null) {
					buf = new StringBuilder(source.length());
				}
				buf.append(codePoint);
			} else {
			}
		}

		if (buf == null) {
			return source;// 如果没有找到 emoji表情，则返回源字符串
		} else {
			if (buf.length() == len) {// 这里的意义在于尽可能少的toString，因为会重新生成字符串
				buf = null;
				return source;
			} else {
				return buf.toString();
			}
		}
	}

	/*
	 * public static void main(String[] args) { String s =
	 * "<body>😄213这是一<a>个有</a>各种内容的消息,  Hia Hia Hia !!!! xxxx@@@...*)!(@*$&@(&#!)@*)!&$!)@^%@(!&#. 😄👩👨], "
	 * ; System.out.println("有Emoji时：" + filter(s));
	 * 
	 * String expected =
	 * "<body>213这是一个有各种内容的消息,  Hia Hia Hia !!!! xxxx@@@...*)!(@*$&@(&#!)@*)!&$!)@^%@(!&#. ], "
	 * ; System.out.println("没Emoji时：" + filter(expected));
	 * 
	 * int i = 0xF0; //Unicode编码
	 * 
	 * System.out.println(i); }
	 */
}