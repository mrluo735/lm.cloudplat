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

import org.junit.Test;

import lm.com.framework.sqlmedium.FilterExpression;

/**
 * @author Administrator
 *
 */
public class QueryExpressionTest {
	@Test
	public void test1() throws NoSuchAlgorithmException, UnsupportedEncodingException, Exception {		
		String where = FilterExpression.getInstance().beginGroup().eq("id", 1111).and().eq("name", "dfsdf").and()
				.like("remark", "dfdfd").endGroup().and().gt("createon", 121323L).or().le("code", 233).and()
				.isNull("image").and().in("desc", "1,2,3").and().in("id", "SELECT id from aaa WHERE aaa.name = 'aaa'").toWhere();
		System.out.println(where);
		System.out.println(FilterExpression.getInstance().toWhere());
	}
}
