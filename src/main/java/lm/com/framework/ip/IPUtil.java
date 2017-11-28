/**
 * @title IPUtil.java
 * @description TODO
 * @package lm.com.framework.ip
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年12月23日下午4:05:42
 * @version v1.0
 */
package lm.com.framework.ip;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lm.com.framework.map.KeyValuePair;

/**
 * ip工具类
 * 
 * @author mrluo735
 *
 */
public class IPUtil {
	/**
	 * 将IPV4地址转换成十进制整数
	 * 
	 * <p>
	 * 1、通过String的indexOf方法找出IP字符串中的点"."的位置。
	 * 2、根据点的位置，使用String的substring方法把IP字符串分成4段。
	 * 3、使用Long的parseLong方法把子段转化成一个3位整数。
	 * 4、通过左移位操作（<<）给每一段的数字加权，第一段的权为2的24次方，第二段的权为2的16次方，第三段的权为2的8次方，最后一段的权为1
	 * </p>
	 * 
	 * @param strIp
	 * @return
	 */
	public static long ipv4ToLong(String strIp) {
		long[] ip = new long[4];
		// 先找到IP地址字符串中.的位置
		int position1 = strIp.indexOf(".");
		int position2 = strIp.indexOf(".", position1 + 1);
		int position3 = strIp.indexOf(".", position2 + 1);
		// 将每个.之间的字符串转换成整型
		ip[0] = Long.parseLong(strIp.substring(0, position1));
		ip[1] = Long.parseLong(strIp.substring(position1 + 1, position2));
		ip[2] = Long.parseLong(strIp.substring(position2 + 1, position3));
		ip[3] = Long.parseLong(strIp.substring(position3 + 1));
		return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
	}

	/**
	 * 将十进制整数形式转换成IPV4地址
	 * 
	 * <p>
	 * 1、将整数值进行右移位操作（>>>），右移24位，右移时高位补0，得到的数字即为第一段IP。
	 * 2、通过与操作符（&）将整数值的高8位设为0，再右移16位，得到的数字即为第二段IP。
	 * 3、通过与操作符吧整数值的高16位设为0，再右移8位，得到的数字即为第三段IP。
	 * 4、通过与操作符吧整数值的高24位设为0，得到的数字即为第四段IP。
	 * </p>
	 * 
	 * @param longIp
	 * @return
	 */
	public static String longToIpv4(long longIp) {
		StringBuffer sb = new StringBuffer("");
		// 直接右移24位
		sb.append(String.valueOf((longIp >>> 24)));
		sb.append(".");
		// 将高8位置0，然后右移16位
		sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
		sb.append(".");
		// 将高16位置0，然后右移8位
		sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
		sb.append(".");
		// 将高24位置0
		sb.append(String.valueOf((longIp & 0x000000FF)));
		return sb.toString();
	}

	/**
	 * 将IPV6地址转换成十进制整数
	 * 
	 * @param ipv6
	 * @return
	 */
	public static BigInteger ipv6ToBigInteger(String ipv6) {
		int compressIndex = ipv6.indexOf("::");
		if (compressIndex != -1) {
			String part1s = ipv6.substring(0, compressIndex);
			String part2s = ipv6.substring(compressIndex + 1);
			BigInteger part1 = ipv6ToBigInteger(part1s);
			BigInteger part2 = ipv6ToBigInteger(part2s);
			int part1hasDot = 0;
			char ch[] = part1s.toCharArray();
			for (char c : ch) {
				if (c == ':') {
					part1hasDot++;
				}
			}
			// ipv6 has most 7 dot
			return part1.shiftLeft(16 * (7 - part1hasDot)).add(part2);
		}
		String[] str = ipv6.split(":");
		BigInteger big = BigInteger.ZERO;
		for (int i = 0; i < str.length; i++) {
			// ::1
			if (str[i].isEmpty()) {
				str[i] = "0";
			}
			big = big.add(BigInteger.valueOf(Long.valueOf(str[i], 16)).shiftLeft(16 * (str.length - i - 1)));
		}
		return big;
	}

	/**
	 * 将十进制整数形式转换成IPV6地址
	 * 
	 * @param bigInteger
	 * @return
	 */
	public static String bigIntegerToIpv6(BigInteger bigInteger) {
		String str = "";
		BigInteger ff = BigInteger.valueOf(0xffff);
		for (int i = 0; i < 8; i++) {
			str = bigInteger.and(ff).toString(16) + ":" + str;

			bigInteger = bigInteger.shiftRight(16);
		}
		// the last :
		str = str.substring(0, str.length() - 1);

		return str.replaceFirst("(^|:)(0+(:|$)){2,8}", "::");
	}

	/**
	 * 解析QQ纯真ip
	 * 
	 * @param country
	 * @return
	 */
	public static Map<String, String> parseQQWry(String country) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("country", "");
		map.put("countryCode", "");
		map.put("province", "");
		map.put("provinceCode", "");
		map.put("city", "");
		map.put("cityCode", "");
		map.put("county", "");
		map.put("countyCode", "");

		final List<KeyValuePair<String, String>> provinces = new ArrayList<>();
		provinces.add(new KeyValuePair<String, String>("110000", "北京市"));
		provinces.add(new KeyValuePair<String, String>("120000", "天津市"));
		provinces.add(new KeyValuePair<String, String>("130000", "河北省"));
		provinces.add(new KeyValuePair<String, String>("140000", "山西省"));
		provinces.add(new KeyValuePair<String, String>("150000", "内蒙古"));
		provinces.add(new KeyValuePair<String, String>("210000", "辽宁省"));
		provinces.add(new KeyValuePair<String, String>("220000", "吉林省"));
		provinces.add(new KeyValuePair<String, String>("230000", "黑龙江省"));
		provinces.add(new KeyValuePair<String, String>("310000", "上海市"));
		provinces.add(new KeyValuePair<String, String>("320000", "江苏省"));
		provinces.add(new KeyValuePair<String, String>("330000", "浙江省"));
		provinces.add(new KeyValuePair<String, String>("340000", "安徽省"));
		provinces.add(new KeyValuePair<String, String>("350000", "福建省"));
		provinces.add(new KeyValuePair<String, String>("360000", "江西省"));
		provinces.add(new KeyValuePair<String, String>("370000", "山东省"));
		provinces.add(new KeyValuePair<String, String>("410000", "河南省"));
		provinces.add(new KeyValuePair<String, String>("420000", "湖北省"));
		provinces.add(new KeyValuePair<String, String>("430000", "湖南省"));
		provinces.add(new KeyValuePair<String, String>("440000", "广东省"));
		provinces.add(new KeyValuePair<String, String>("450000", "广西"));
		provinces.add(new KeyValuePair<String, String>("460000", "海南省"));
		provinces.add(new KeyValuePair<String, String>("500000", "重庆市"));
		provinces.add(new KeyValuePair<String, String>("510000", "四川省"));
		provinces.add(new KeyValuePair<String, String>("520000", "贵州省"));
		provinces.add(new KeyValuePair<String, String>("530000", "云南省"));
		provinces.add(new KeyValuePair<String, String>("540000", "西藏"));
		provinces.add(new KeyValuePair<String, String>("610000", "陕西省"));
		provinces.add(new KeyValuePair<String, String>("620000", "甘肃省"));
		provinces.add(new KeyValuePair<String, String>("630000", "青海省"));
		provinces.add(new KeyValuePair<String, String>("640000", "宁夏"));
		provinces.add(new KeyValuePair<String, String>("650000", "新疆"));
		provinces.add(new KeyValuePair<String, String>("710000", "台湾省"));
		provinces.add(new KeyValuePair<String, String>("810000", "香港"));
		provinces.add(new KeyValuePair<String, String>("820000", "澳门"));

		// final String[] levels = new String[] { "地区", "州", "市" };
		if (null == country || country.length() < 1)
			return map;

		boolean isMatch = false;
		for (KeyValuePair<String, String> kvp : provinces) {
			if (country.indexOf(kvp.getValue()) > -1) {
				map.put("country", "中国");
				map.put("countryCode", "086");

				if ("北京市".equals(kvp.getValue()) || "上海市".equals(kvp.getValue()) || "天津市".equals(kvp.getValue())
						|| "重庆市".equals(kvp.getValue())) {
					map.put("province", kvp.getValue());
					map.put("provinceCode", kvp.getKey());
					map.put("city", kvp.getValue());
					map.put("cityCode", kvp.getKey());
					map.put("county", parseCounty(country.replace(kvp.getValue(), "")));
				} else if ("香港".equals(kvp.getValue()) || "澳门".equals(kvp.getValue())) {
					map.put("province", kvp.getValue() + "特别行政区");
					map.put("provinceCode", kvp.getKey());
					map.put("provinceCode", kvp.getKey());
					map.put("city", kvp.getValue());
					map.put("cityCode", kvp.getKey());
				} else if ("内蒙古".equals(kvp.getValue())) {
					map.put("province", "内蒙古自治区");
					map.put("provinceCode", kvp.getKey());

					String cityStr = country.replace(kvp.getValue(), "");
					if (cityStr.indexOf("兴安盟") > -1) {
						map.put("city", "兴安盟");
						map.put("county", parseCounty(cityStr).replace("兴安盟", ""));
					} else if (cityStr.indexOf("锡林郭勒盟") > -1) {
						map.put("city", "锡林郭勒盟");
						map.put("county", parseCounty(cityStr).replace("锡林郭勒盟", ""));
					} else if (cityStr.indexOf("") > -1) {
						map.put("city", "阿拉善盟");
						map.put("county", parseCounty(cityStr).replace("阿拉善盟", ""));
					}
				} else if ("广西".equals(kvp.getValue())) {
					map.put("province", "广西壮族自治区");
					map.put("provinceCode", kvp.getKey());
				} else if ("西藏".equals(kvp.getValue())) {
					map.put("province", "西藏自治区");
					map.put("provinceCode", kvp.getKey());
				} else if ("宁夏".equals(kvp.getValue())) {
					map.put("province", "宁夏回族自治区");
					map.put("provinceCode", kvp.getKey());
				} else if ("新疆".equals(kvp.getValue())) {
					map.put("province", "新疆维吾尔自治区");
					map.put("provinceCode", kvp.getKey());
				} else { // 其他省份
					map.put("province", kvp.getValue());
					map.put("provinceCode", kvp.getKey());

					String cityStr = country.replace(kvp.getValue(), "");
					if (cityStr.indexOf("地区") > -1) {
						int index = cityStr.indexOf("地区");
						String city = cityStr.substring(0, index + 2);
						map.put("city", city);
						map.put("county", parseCounty(cityStr.replace(city, "")));
					} else if (cityStr.indexOf("市") > -1) {
						int index = cityStr.indexOf("市");
						String city = cityStr.substring(0, index + 1);
						map.put("city", city);
						map.put("county", parseCounty(cityStr.replace(city, "")));
					} else if (cityStr.indexOf("州") > -1) {
						int index = cityStr.indexOf("州");
						String city = cityStr.substring(0, index + 1);
						map.put("city", city);
						map.put("county", parseCounty(cityStr.replace(city, "")));
					} else {
						map.put("city", "");
					}
				}
				isMatch = true;
				break;
			}
		}
		if (!isMatch)
			map.put("country", country);

		return map;
	}

	private static String parseCounty(String str) {
		if (str.indexOf("区") > -1) {
			int index = str.indexOf("区");
			return str.substring(0, index + 1);
		} else if (str.indexOf("县") > -1) {
			int index = str.indexOf("县");
			return str.substring(0, index + 1);
		} else if (str.indexOf("市") > -1) {
			int index = str.indexOf("市");
			return str.substring(0, index + 1);
		} else if (str.indexOf("旗") > -1) {
			int index = str.indexOf("旗");
			return str.substring(0, index + 1);
		} else
			return "";
	}
}
