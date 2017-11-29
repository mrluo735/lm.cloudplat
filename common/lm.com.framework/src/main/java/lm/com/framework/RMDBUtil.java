/**
 * @title SqlUtil.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.com.framework
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年6月19日下午6:23:35
 * @version v1.0.0
 */
package lm.com.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lm.com.framework.enumerate.EnumRMDBType;

/**
 * 关系型数据库工具类
 * 
 * @ClassName: RMDBUtil
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年6月19日 下午6:23:35
 * 
 */
public class RMDBUtil {
	/**
	 * 关键字转义
	 * 
	 * @param key
	 * @param rmdbType
	 * @return
	 */
	public static String escapeKey(String key, EnumRMDBType rmdbType) {
		switch (rmdbType) {
			case Access:
			case SqlServer:
				return String.format("[%s]", key);
			case Mysql:
				return String.format("`%s`", key);
			case Oracle:
			case Postgresql:
				return String.format("\"%s\"", key);
			case Db2:
			case Sybase:
				return String.format("[%s]", key);
			default:
				return key;
		}
	}

	/**
	 * 转义列名
	 * 
	 * @param column
	 * @param driver
	 * @return
	 */
	public static String escapeColumn(String column, String driver) {
		EnumRMDBType rdbType = getRMDBType(driver);
		switch (rdbType) {
			case Access:
			case SqlServer:
				return String.format("[%s]", StringUtil.trim(column, "[]"));
			case Mysql:
				return String.format("`%s`", StringUtil.trim(column, "`"));
			case Oracle:
			case Postgresql:
				return String.format("\"%s\"", StringUtil.trim(column, "\""));
			case Db2:
			case Sybase:
				return String.format("[%s]", StringUtil.trim(column, "[]"));
			default:
				return column;
		}
	}

	/**
	 * ORDER BY的sql子句转行转义
	 * 
	 * @param orderBy
	 * @param driver
	 * @return
	 */
	public static String escapeSqlOrderBy(String orderBy, String driver) {
		if (StringUtil.isNullOrWhiteSpace(orderBy))
			return "";
		String[] array = orderBy.split(",");
		if (null == array || array.length < 1)
			return "";

		List<String> list = new ArrayList<>();
		EnumRMDBType rdbType = getRMDBType(driver);
		for (String item : array) {
			String[] columnOrderBys = item.split(" ");
			if (null == columnOrderBys || columnOrderBys.length < 2)
				continue;
			String columnStr = columnOrderBys[0];
			String column = escapeKey(columnStr, rdbType);
			if (columnStr.indexOf(".") > -1) {
				String[] columns = columnStr.split("\\.");
				column = columns[0] + "." + escapeKey(columns[1], rdbType);
			}
			list.add(String.format("%s %s", column.trim(), columnOrderBys[1].trim().toUpperCase()));
		}
		return StringUtil.join(", ", list);
	}

	/**
	 * 重载+1 根据连接驱动获取关系型数据库类型
	 * 
	 * @param driver
	 * @param defaultValue
	 * @return
	 */
	public static String getRMDBType(String driver, String defaultValue) {
		EnumRMDBType rmdbType = getRMDBType(driver);
		if (rmdbType == null)
			return defaultValue;
		return rmdbType.getName();
	}

	/**
	 * 重载+2 根据连接驱动获取关系型数据库类型
	 * 
	 * @param driver
	 * @return
	 */
	public static EnumRMDBType getRMDBType(String driver) {
		// mysql-driverClass：com.mysql.jdbc.Driver
		// Sql
		// Server2000-driverClass：com.microsoft.jdbc.sqlserver.SQLServerDriver
		// Sql
		// Server2005-driverClass：com.microsoft.sqlserver.jdbc.SQLServerDriver
		// oracle-driverClass：oracle.jdbc.driver.OracleDriver
		// db2-driverClass：com.ibm.db2.jcc.DB2Driver
		// PostgreSQL-driverClass：org.postgresql.Driver
		// sybase-driverClass：com.sybase.jdbc.SybDriver

		for (EnumRMDBType type : EnumRMDBType.values()) {
			int index = driver.toLowerCase().indexOf(type.toString().toLowerCase());
			if (index > -1)
				return type;
		}
		return null;
	}

	/**
	 * 过滤sql关键字
	 * 
	 * @param value
	 * @return
	 */
	public static String filterSqlKey(String value) {
		String sqlKey = "and +|or +|exec +|execute +|insert +|select +|delete +|update +|alter|create +|drop +|count|\\*|chr|char|asc|mid|substring|master|truncate +|declare +|xp_cmdshell|restore +|backup +|net +user|net +localgroup +administrators";
		Pattern sqlPattern = Pattern.compile(sqlKey, Pattern.CASE_INSENSITIVE);
		Matcher matcher = sqlPattern.matcher(value);
		return matcher.replaceAll("");
	}
}
