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

import java.sql.Timestamp;
import java.util.Date;

/**
 * MySql工具类
 * 
 * @author mrluo735
 *
 */
public class MySqlUtil {
	/**
	 * unix时间最小值:1970-01-01 08:00:00
	 */
	public static final Date UNIXTIME_MIN = new Date(0L);
	
	/**
	 * unix时间最小值:1970-01-01 08:00:00
	 */
	public static final Timestamp UNIXTIME_MIN_TS = new Timestamp(0L);

	/**
	 * unix时间最小值:1970-01-01 08:00:00
	 */
	public static final String UNIXTIME_MIN_STRING = "1970-01-01 08:00:00";

	/**
	 * unix时间最小时间:0L
	 */
	public static final long UNIXTIME_MIN_LONG = 0L;
}
