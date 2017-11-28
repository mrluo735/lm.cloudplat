/**
 * @title SpellUtil.java
 * @description TODO
 * @package lm.com.framework
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年11月9日下午5:01:07
 * @version v1.0
 */
package lm.com.framework;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 拼音工具类
 * 
 * @author mrluo735
 *
 */
public class SpellUtil {
	private final static Logger logger = LoggerFactory.getLogger(SpellUtil.class);

	/**
	 * 获取全拼
	 * 
	 * @param src
	 * @return
	 */
	public static String getSpell(String src) {
		char[] t1 = null;
		t1 = src.toCharArray();
		String[] t2 = new String[t1.length];
		HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();
		t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		t3.setVCharType(HanyuPinyinVCharType.WITH_V);
		String t4 = "";
		int t0 = t1.length;
		try {
			for (int i = 0; i < t0; i++) {
				char c = t1[i];
				// 如果字符为空
				if (String.valueOf(c).trim().length() != 0) {
					// 判断是否为汉字字符
					if (java.lang.Character.toString(t1[i]).matches("[\\u4E00-\\u9FA5]+")) {
						t2 = PinyinHelper.toHanyuPinyinStringArray(c, t3);
						if (null != t2) {
							t4 += t2[0];
						}
					} else {
						t4 += java.lang.Character.toString(t1[i]);
					}
				}
			}
			return t4;
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			logger.error(e.getMessage());
		}
		return t4;
	}

	/**
	 * 获取中文首字母
	 * 
	 * @param str
	 * @return
	 */
	public static String getHeadChar(String str) {
		String convert = "";
		for (int j = 0; j < str.length(); j++) {
			char word = str.charAt(j);
			String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
			if (pinyinArray != null) {
				convert += pinyinArray[0].charAt(0);
			} else {
				convert += word;
			}
		}
		return convert.toUpperCase();
	}

	/**
	 * 获取第一个首字母
	 * 
	 * @param str
	 * @return
	 */
	public static String getFirstHeadChar(String str) {
		String convert = "";
		char word = str.charAt(0);
		String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
		if (pinyinArray != null) {
			convert += pinyinArray[0].charAt(0);
		} else {
			convert += word;
		}
		return convert.toUpperCase();
	}
}
