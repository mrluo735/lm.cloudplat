/**
 * @title UnixTimeUtil.java
 * @description TODO
 * @package lm.com.framework
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年11月29日下午6:49:50
 * @version v1.0
 */
package lm.com.framework;

import java.util.Date;

/**
 * unix时间工具类
 * 
 * @author mrluo735
 *
 */
public class UnixTimeUtil {
	/**
	 * 最小时间
	 * <p>
	 * 1970-01-01 00:00:00
	 * </p>
	 */
	public static final Date MIN = new Date(-28800000L);

	/**
	 * 最小时间
	 * <p>
	 * 1970-01-01 00:00:00
	 * </p>
	 */
	public static final String MIN_STRING = "1970-01-01 00:00:00";

	/**
	 * 最小时间
	 * <p>
	 * 1970-01-01 00:00:00
	 * </p>
	 */
	public static final long MIN_LONG = -28800000L;
}
