/**
 * @title RandomUtil.java
 * @description TODO
 * @package lm.com.framework
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年11月9日下午5:15:25
 * @version v1.0
 */
package lm.com.framework;

import java.util.Random;

/**
 * 随机数工具类
 * 
 * @author mrluo735
 *
 */
public class RandomUtil {
	/**
	 * <p>
	 * Random object used by random method. This has to be not local to the
	 * random method so as to not return the same value in the same millisecond.
	 * </p>
	 */
	private static final Random RANDOM = new Random();

	/**
	 * 构造函数
	 */
	public RandomUtil() {
		super();
	}

	// Random
	// -----------------------------------------------------------------------
	/**
	 * 创建一个指定长度的随机字符串
	 * 
	 * @param count
	 * @return
	 */
	public static String random(final int count) {
		return random(count, false, false);
	}

	/**
	 * 创建一个指定长度的随机ascii码对应字符串
	 */
	public static String randomAscii(final int count) {
		return random(count, 32, 127, false, false);
	}

	/**
	 * 创建一个指定长度的随机字母字符串
	 */
	public static String randomAlphabetic(final int count) {
		return random(count, true, false);
	}

	/**
	 * 创建一个指定长度的随机字母数字字符串
	 */
	public static String randomAlphanumeric(final int count) {
		return random(count, true, true);
	}

	/**
	 * 创建一个指定长度的随机数字字符串
	 */
	public static String randomNumeric(final int count) {
		return random(count, false, true);
	}

	/**
	 * 创建一个指定长度的随机字符串
	 * 
	 * @param count
	 * @param letters
	 *            是否只有字母
	 * @param numbers
	 *            是否只有数字
	 * @return
	 */
	public static String random(final int count, final boolean letters, final boolean numbers) {
		return random(count, 0, 0, letters, numbers);
	}

	/**
	 * 创建一个指定长度的随机字符串
	 * 
	 * @param count
	 * @param start
	 * @param end
	 * @param letters
	 * @param numbers
	 * @return
	 */
	public static String random(final int count, final int start, final int end, final boolean letters,
			final boolean numbers) {
		return random(count, start, end, letters, numbers, null, RANDOM);
	}

	/**
	 * 创建一个指定长度的随机字符串
	 * 
	 * @param count
	 * @param start
	 * @param end
	 * @param letters
	 * @param numbers
	 * @param chars
	 * @return
	 */
	public static String random(final int count, final int start, final int end, final boolean letters,
			final boolean numbers, final char... chars) {
		return random(count, start, end, letters, numbers, chars, RANDOM);
	}

	/**
	 * 创建一个指定长度的随机字符串
	 * 
	 * @param count
	 * @param start
	 * @param end
	 * @param letters
	 * @param numbers
	 * @param chars
	 * @param random
	 * @return
	 */
	public static String random(int count, int start, int end, final boolean letters, final boolean numbers,
			final char[] chars, final Random random) {
		if (count == 0) {
			return "";
		} else if (count < 0) {
			throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
		}
		if (chars != null && chars.length == 0) {
			throw new IllegalArgumentException("The chars array must not be empty");
		}

		if (start == 0 && end == 0) {
			if (chars != null) {
				end = chars.length;
			} else {
				if (!letters && !numbers) {
					end = Integer.MAX_VALUE;
				} else {
					end = 'z' + 1;
					start = ' ';
				}
			}
		} else {
			if (end <= start) {
				throw new IllegalArgumentException(
						"Parameter end (" + end + ") must be greater than start (" + start + ")");
			}
		}

		final char[] buffer = new char[count];
		final int gap = end - start;

		while (count-- != 0) {
			char ch;
			if (chars == null) {
				ch = (char) (random.nextInt(gap) + start);
			} else {
				ch = chars[random.nextInt(gap) + start];
			}
			if (letters && Character.isLetter(ch) || numbers && Character.isDigit(ch) || !letters && !numbers) {
				if (ch >= 56320 && ch <= 57343) {
					if (count == 0) {
						count++;
					} else {
						// low surrogate, insert high surrogate after putting it
						// in
						buffer[count] = ch;
						count--;
						buffer[count] = (char) (55296 + random.nextInt(128));
					}
				} else if (ch >= 55296 && ch <= 56191) {
					if (count == 0) {
						count++;
					} else {
						// high surrogate, insert low surrogate before putting
						// it in
						buffer[count] = (char) (56320 + random.nextInt(128));
						count--;
						buffer[count] = ch;
					}
				} else if (ch >= 56192 && ch <= 56319) {
					// private high surrogate, no effing clue, so skip it
					count++;
				} else {
					buffer[count] = ch;
				}
			} else {
				count++;
			}
		}
		return new String(buffer);
	}

	/**
	 * 从指定字符串里创建一个指定长度的随机字符串
	 * 
	 * @param count
	 * @param chars
	 * @return
	 */
	public static String random(final int count, final String chars) {
		if (chars == null) {
			return random(count, 0, 0, false, false, null, RANDOM);
		}
		return random(count, chars.toCharArray());
	}

	/**
	 * 从指定字符串数组里创建一个指定长度的随机字符串
	 * 
	 * @param count
	 * @param chars
	 * @return
	 */
	public static String random(final int count, final char... chars) {
		if (chars == null) {
			return random(count, 0, 0, false, false, null, RANDOM);
		}
		return random(count, 0, chars.length, false, false, chars, RANDOM);
	}
}
