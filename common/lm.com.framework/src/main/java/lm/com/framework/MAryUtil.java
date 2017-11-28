/**
 * @title MAryUtil.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.com.framework
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年6月8日下午5:36:49
 * @version v1.0.0
 */
package lm.com.framework;

import java.util.Stack;

/**
 * 进制转换工具类
 * <p>
 * 32(含)进制以下
 * </p>
 * 
 * @ClassName: MAryUtil
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年6月8日 下午5:36:49
 * 
 */
public class MAryUtil {
	private final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
			'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'R', 'S', 'T', 'U', 'W', 'X', 'Y', 'Z' };
	private final static char[] h62Digis = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
			.toCharArray();
	private final static char[] h64Digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
			'V', 'W', 'X', 'Y', 'Z', '+', '/' };

	/**
	 * 重载+1 将十进制的数字转换为指定进制的字符串。
	 * 
	 * @param i
	 *            十进制的数字。
	 * @param system
	 *            指定的进制，常见的2/8/16/32。
	 * @return 转换后的字符串。
	 */
	public static String ten2Any(int i, int system) {
		long num = 0;
		if (i < 0) {
			num = ((long) 2 * 0x7fffffff) + i + 2;
		} else {
			num = i;
		}
		char[] buf = new char[32];
		int charPos = 32;
		while ((num / system) > 0) {
			buf[--charPos] = digits[(int) (num % system)];
			num /= system;
		}
		buf[--charPos] = digits[(int) (num % system)];
		return new String(buf, charPos, (32 - charPos));
	}

	/**
	 * 重载+2 将十进制的数字转换为指定进制的字符串。
	 * 
	 * @param i
	 *            十进制的数字。
	 * @param system
	 *            指定的进制，常见的2/8/16。
	 * @return 转换后的字符串。
	 */
	public static String ten2Any(long i, int system) {
		long num = 0;
		if (i < 0) {
			num = ((long) 2 * 0x7fffffff) + i + 2;
		} else {
			num = i;
		}
		char[] buf = new char[32];
		int charPos = 32;
		while ((num / system) > 0) {
			buf[--charPos] = digits[(int) (num % system)];
			num /= system;
		}
		buf[--charPos] = digits[(int) (num % system)];
		return new String(buf, charPos, (32 - charPos));
	}

	/**
	 * 将其它进制的数字（字符串形式）转换为十进制的数字。
	 * 
	 * @param s
	 *            其它进制的数字（字符串形式）
	 * @param system
	 *            指定的进制，常见的2/8/16/32。
	 * @return 转换后的数字。
	 */
	public static int any2Ten(String s, int system) {
		char[] buf = new char[s.length()];
		s.getChars(0, s.length(), buf, 0);
		long num = 0;
		for (int i = 0; i < buf.length; i++) {
			for (int j = 0; j < digits.length; j++) {
				if (digits[j] == buf[i]) {
					num += j * Math.pow(system, buf.length - i - 1);
					break;
				}
			}
		}
		return (int) num;
	}

	/**
	 * 将10进制转化为62进制
	 * 
	 * @param number
	 * @return
	 */
	public static String tenTo62hex(long number) {
		Long rest = number;
		Stack<Character> stack = new Stack<Character>();
		StringBuilder result = new StringBuilder(0);
		while (rest != 0) {
			stack.add(h62Digis[new Long((rest - (rest / 62) * 62)).intValue()]);
			rest = rest / 62;
		}
		for (; !stack.isEmpty();) {
			result.append(stack.pop());
		}
		int result_length = result.length();
		StringBuilder temp0 = new StringBuilder();
		for (int i = 0; i < 0 - result_length; i++) {
			temp0.append('0');
		}

		return temp0.toString() + result.toString();

	}

	/**
	 * 将62进制转换成10进制数
	 * 
	 * @param str
	 *            62进制字符串
	 * @return
	 */
	public static long hex62ToTen(String s) {
		Long dst = 0L;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			for (int j = 0; j < h62Digis.length; j++) {
				if (c == h62Digis[j]) {
					dst = (dst * 62) + j;
					break;
				}
			}
		}
		String str = String.format("%08d", dst);
		return LongUtil.toLong(str);
	}

	/**
	 * 把10进制的数字转换成64进制
	 * 
	 * @param number
	 * @return
	 */
	public static String tenTo64hex(long number) {
		char[] buf = new char[64];
		int charPos = 64;
		int radix = 1 << 6;
		long mask = radix - 1;
		do {
			buf[--charPos] = h64Digits[(int) (number & mask)];
			number >>>= 6;
		} while (number != 0);
		return new String(buf, charPos, (64 - charPos));
	}

	/**
	 * 把64进制的字符串转换成10进制
	 * 
	 * @param hex64
	 * @return
	 */
	public static long hex64ToTen(String hex64) {
		long result = 0;
		for (int i = hex64.length() - 1; i >= 0; i--) {
			for (int j = 0; j < h64Digits.length; j++) {
				if (hex64.charAt(i) == h64Digits[j]) {
					result += ((long) j) << 6 * (hex64.length() - 1 - i);
				}
			}
		}
		return result;
	}
}
