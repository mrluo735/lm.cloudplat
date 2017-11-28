/**
 * @title IPTest.java
 * @description TODO
 * @package lm.com.framework
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年10月11日下午7:19:28
 * @version v1.0
 */
package lm.com.framework;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import lm.com.framework.ip.IPLocation;
import lm.com.framework.ip.IPSeeker;

/**
 * @author Administrator
 *
 */
public class IPTest {
	@Test
	public void test1() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		IPSeeker seeker = new IPSeeker("qqwry.dat", "D:/ip");

		IPLocation ipLocation = seeker.getIPLocation("49.114.16.0"); // 浙江省杭州市183.131.62.95
		System.out.println(String.format("地区：%s,%s", ipLocation.getArea(), ipLocation.getOperator()));
		Map<String, String> map = this.getArea(ipLocation.getArea(), ipLocation.getOperator());
		System.out.println("国家=" + map.get("country") + "， 省份=" + map.get("province") + "， 城市=" + map.get("city"));

		IPLocation ipLocation2 = seeker.getIPLocation("048.255.255.255"); // 美国
		System.out.println(String.format("地区：%s,%s", ipLocation2.getArea(), ipLocation2.getOperator()));
		map = this.getArea(ipLocation2.getArea(), ipLocation2.getOperator());
		System.out.println("国家=" + map.get("country") + "， 省份=" + map.get("province") + "， 城市=" + map.get("city"));

		IPLocation ipLocation3 = seeker.getIPLocation("61.244.148.166"); // 香港
		System.out.println(String.format("地区：%s,%s", ipLocation3.getArea(), ipLocation3.getOperator()));
		map = this.getArea(ipLocation3.getArea(), ipLocation3.getOperator());
		System.out.println("国家=" + map.get("country") + "， 省份=" + map.get("province") + "， 城市=" + map.get("city"));
	}

	private Map<String, String> getArea(String country, String area) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("country", "");
		map.put("province", "");
		map.put("city", "");

		final String[] provinces = new String[] { "北京市", "天津市", "河北省", "山西省", "内蒙古", "辽宁省", "吉林省", "黑龙江省", "上海市", "江苏省",
				"浙江省", "安徽省", "福建省", "江西省", "山东省", "河南省", "湖北省", "湖南省", "广东省", "广西", "海南省", "重庆市", "四川省", "贵州省", "云南省",
				"西藏", "陕西省", "甘肃省", "青海省", "宁夏", "新疆", "香港", "澳门", "台湾省" };
		final String[] levels = new String[] { "地区", "州", "市" };
		if (null == country || country.length() < 1)
			return map;

		boolean isMatch = false;
		for (String p : provinces) {
			if (country.indexOf(p) > -1) {
				map.put("country", "中国");
				map.put("province", p);
				if ("北京市".equals(p) || "上海市".equals(p) || "天津市".equals(p) || "重庆市".equals(p) || "香港".equals(p)
						|| "澳门".equals(p))
					map.put("city", p);
				else {
					String city = country.replace(p, "");
					if (city.indexOf("地区") > -1) {
						int index = city.indexOf("地区");
						map.put("city", city.substring(0, index + 2));
					} else if (city.indexOf("市") > -1) {
						int index = city.indexOf("市");
						map.put("city", city.substring(0, index + 1));
					} else if (city.indexOf("州") > -1) {
						int index = city.indexOf("州");
						map.put("city", city.substring(0, index + 1));
					}else {
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
}
