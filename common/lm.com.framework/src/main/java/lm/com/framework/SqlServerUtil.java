/**
 * @title SqlServerUtil.java
 * @description TODO
 * @package lm.com.framework
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年11月29日下午7:00:40
 * @version v1.0
 */
package lm.com.framework;

import java.util.Date;

/**
 * SqlServer工具类
 * 
 * @author mrluo735
 *
 */
public class SqlServerUtil {
	/**
	 * 最小值:1753-01-01 00:00:00
	 */
	public static final Date DATETIME_MIN = new Date(-6847833600000L);

	/**
	 * 最小值:1753-01-01 00:00:00
	 */
	public static final String DATETIME_MIN_STRING = "1753-01-01 00:00:00";

	/**
	 * 最小值:-6847833600000L
	 */
	public static final long DATETIME_MIN_LONG = -6847833600000L;
}
