/**
 * @title MoneyChineseUtil.java
 * @description TODO
 * @package lm.com.framework
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年11月9日下午5:15:25
 * @version v1.0
 */
package lm.com.framework;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 中国金钱工具类
 * 
 * @author mrluo735
 *
 */
public class MoneyChineseUtil {
	private static final String NUMBERS[] = { "\u96F6", "\u58F9", "\u8D30", "\u53C1", "\u8086", "\u4F0D", "\u9646",
			"\u67D2", "\u634C", "\u7396" };
	private static final String UNITS[] = { "\u5143", "\u62FE", "\u4F70", "\u4EDF", "\u4E07", "\u62FE", "\u4F70",
			"\u4EDF", "\u4EBF", "\u62FE", "\u4F70", "\u4EDF", "\u4E07", "\u62FE", "\u4F70", "\u4EDF" };
	//private static final long MAX_NUMBER = 0x2386f26fc10000L;

	private MoneyChineseUtil() {
	}

	public static String formatCapitalization(double amount) {
		String amtstr = formatNumber(amount, 2);
		String intPart = amtstr.substring(0, amtstr.indexOf("."));
		String fPart = amtstr.substring(amtstr.indexOf(".") + 1);
		StringBuffer buffer = new StringBuffer();
		//int num = 0;
		int length = intPart.length();
		int partsize = length / 4;
		if (length % 4 != 0)
			partsize++;
		String parts[] = new String[partsize];
		int start = 0;
		int end = 4;
		if (Long.parseLong(intPart) >= 0x2386f26fc10000L)
			throw new IllegalArgumentException("\u91D1\u989D\u8D85\u51FA\u8303\u56F4");
		if (Long.parseLong(intPart) == 0L) {
			buffer.append(NUMBERS[0]);
		} else {
			for (int i = 0; i < parts.length; i++) {
				start = length - (parts.length - i) * 4;
				end = start + 4;
				if (start < 0)
					start = 0;
				parts[i] = intPart.substring(start, end);
				buffer.append(formatFour(parts[i]));
				if (i < parts.length - 1)
					if ("0000".equals(parts[i])) {
						if (!NUMBERS[0].equals(buffer.substring(buffer.length() - 1))) {
							if (parts.length - i == 3)
								buffer.append(UNITS[8]);
							buffer.append(NUMBERS[0]);
						}
					} else {
						buffer.append(UNITS[(parts.length - 1 - i) * 4]);
					}
			}

		}
		if (NUMBERS[0].equals(buffer.substring(buffer.length() - 1)))
			buffer.delete(buffer.length() - 1, buffer.length());
		buffer.append(UNITS[0]);
		if ("00".equals(fPart)) {
			buffer.append("\u6574");
		} else {
			int fen = Integer.parseInt(fPart);
			if (fen >= 10) {
				buffer.append(NUMBERS[fen / 10]);
				buffer.append("\u89D2");
			} else {
				buffer.append("\u96F6");
			}
			if (fen % 10 != 0) {
				buffer.append(NUMBERS[fen % 10]);
				buffer.append("\u5206");
			}
		}
		return buffer.toString().replaceAll("\u96F6\u96F6", "\u96F6");
	}

	public static String formatNumber(double amount, int digits) {
		NumberFormat nf = new DecimalFormat();
		nf.setMaximumFractionDigits(digits);
		nf.setMinimumFractionDigits(digits);
		long lamt = (long) amount;
		String fractionDigits = nf.format(amount - (double) lamt);
		if (fractionDigits.charAt(0) == '1')
			lamt++;
		String result = lamt + fractionDigits.substring(fractionDigits.indexOf("."));
		return result;
	}

	private static String formatFour(String number) {
		int num = 0;
		int length = number.length();
		StringBuffer ret = new StringBuffer();
		for (int i = 0; i < number.length(); i++) {
			num = Integer.parseInt(number.substring(i, i + 1));
			if (num == 0) {
				if (ret.length() == 0 || !NUMBERS[0].equals(ret.substring(ret.length() - 1)))
					if (i < length - 1 && Integer.parseInt(number.substring(i + 1)) != 0)
						ret.append(NUMBERS[num]);
					else
						return ret.toString();
			} else {
				ret.append(NUMBERS[num]);
				if (i != length - 1)
					ret.append(UNITS[length - 1 - i]);
			}
		}

		return ret.toString();
	}
}
