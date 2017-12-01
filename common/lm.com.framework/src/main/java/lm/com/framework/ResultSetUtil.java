/**
 * @title ResultSetUtil.java
 * @description TODO
 * @package lm.com.framework
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年12月16日下午7:08:35
 * @version v1.0
 */
package lm.com.framework;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;

import lm.com.framework.enumerate.IBaseEnumInt;

/**
 * ResultSet工具类
 * 
 * @author mrluo735
 *
 */
@SuppressWarnings("all")
public class ResultSetUtil {
	/**
	 * 转成bean
	 * 
	 * @param rs
	 * @param clazz
	 * @return
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static <T extends Object> T toBean(ResultSet rs, Class<T> clazz)
			throws SQLException, InstantiationException, IllegalAccessException {
		T bean = (T) clazz.newInstance();
		Field[] fields = ReflectUtil.getDeclaredFields(bean);
		while (rs.next()) {
			toBean(rs, bean, fields);
			break;
		}
		return bean;
	}

	/**
	 * 转成bean
	 * 
	 * @param rs
	 * @param clazz
	 * @return
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static <T extends Object> List<T> toBeans(ResultSet rs, Class<T> clazz)
			throws SQLException, InstantiationException, IllegalAccessException {
		List<T> list = new ArrayList<>();

		T bean = clazz.newInstance();
		Field[] fields = ReflectUtil.getDeclaredFields(bean);
		while (rs.next()) {
			bean = clazz.newInstance();
			toBean(rs, bean, fields);
			list.add(bean);
		}
		return list;
	}

	/**
	 * 转成bean
	 * 
	 * @param rs
	 * @param bean
	 * @param fields
	 * @throws SQLException
	 */
	private static <T extends Object> void toBean(ResultSet rs, T bean, Field[] fields) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			String columnName = rsmd.getColumnLabel(i);
			if (StringUtil.isNullOrWhiteSpace(columnName))
				columnName = rsmd.getColumnName(i);
			Object columnValue = rs.getObject(i);
			for (Field field : fields) {
				if (field.getName().equalsIgnoreCase(columnName)) {
					setValue(bean, field, columnValue);
				}
				// 带@Column注解的覆盖之前的值
				if (field.isAnnotationPresent(Column.class)) {
					Column column = field.getAnnotation(Column.class);
					if (column.name().equalsIgnoreCase(columnName)) {
						setValue(bean, field, columnValue);
						break;
					}
				}
			}
		}
	}

	/**
	 * 处理枚举
	 * 
	 * @param bean
	 * @param field
	 * @param columnValue
	 * @return
	 */
	private static <T extends Object> void setValue(T bean, Field field, Object columnValue) {
		if (field.getType().isEnum()) {
			Object value;
			Class clazz = field.getType();
			Class<?>[] interfaces = field.getType().getInterfaces();
			for (Class<?> interf : interfaces) {
				if (interf.equals(IBaseEnumInt.class)) {
					value = EnumUtil.toEnum(clazz, (Integer) columnValue);
					ReflectUtil.setValueByFieldName(bean, field.getName(), value);
					return;
				}
			}
			value = Enum.valueOf(clazz, columnValue.toString());
			ReflectUtil.setValueByFieldName(bean, field.getName(), value);
			return;
		}
		ReflectUtil.setValueByFieldName(bean, field.getName(), columnValue);
	}
}
