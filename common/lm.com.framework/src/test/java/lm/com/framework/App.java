/**
 * @title App.java
 * @description TODO
 * @package lm.com.framework
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年11月9日下午2:26:44
 * @version v1.0
 */
package lm.com.framework;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.junit.Test;

import lm.com.framework.sqlmedium.FilterExpression;

/**
 * @author Administrator
 *
 */
public class App {
	@Test
	public void test1() throws NoSuchAlgorithmException, UnsupportedEncodingException, Exception {		
		String result = HttpClientUtil.post("http://localhost:1101/oauth/token", null,
				"client_id=hby201612080000001&client_secret=b5c21378373a44a1a5ffa28f0ae6a5fa&grant_type=authorization_code&code=CW5wGy&redirect_uri=http://localhost:1100/test/getcode", null);
		System.out.println(result);
		System.out.println(DateTimeUtil.toString(DateTimeUtil.getFirstInMonth(new Date()), "yyyy-MM-dd HH:mm:ss"));
		System.out.println(DateTimeUtil.toString(DateTimeUtil.getLastInMonth(new Date()), "yyyy-MM-dd HH:mm:ss"));
		System.out.println(DateTimeUtil.getDaysInMonth(2016, 12));
		
		// ObjectId objectId = ObjectId.newObjectId();
		// System.out.println(objectId.toString());
		// System.out.println(MoneyChineseUtil.formatCapitalization(1003256.22));
		// System.out.println(MoneyChineseUtil.formatNumber(1003256, 4));
		float dd = 5454878.232323f;
		String ss = StringUtil.format("a = {0}, b = {1}, c={2}, d = {3}, e = {4}", "asd", 803164546773102592L, dd,
				new Date(), null);
		System.out.println(ss);

		System.out.println(DateTimeUtil.toLong("1970-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
		System.out.println(new Date(-28800000L));
		System.out.println(new Date(0L));

		System.out.println(DateTimeUtil.toLong("1753-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
		System.out.println(new Date(-6847833600000L));

		System.out.println(
				DateTimeUtil.toString(DateTimeUtil.addDays(DateTimeUtil.getNow(), 100), DateTimeUtil.DEFAULT_PATTERN));
		System.out.println(
				DateTimeUtil.toString(DateTimeUtil.addMonths(DateTimeUtil.getNow(), 1), DateTimeUtil.DEFAULT_PATTERN));

		String where = FilterExpression.getInstance().beginGroup().eq("id", 1111).and().eq("name", "dfsdf").and()
				.like("remark", "dfdfd").endGroup().and().gt("createon", 121323L).or().le("code", 233).and()
				.isNull("image").and().in("desc", "1,2,3").toWhere();
		System.out.println(where);
		System.out.println(FilterExpression.getInstance().toWhere());
	}
}
