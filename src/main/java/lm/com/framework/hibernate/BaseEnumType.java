package lm.com.framework.hibernate;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;
import org.springframework.util.ObjectUtils;

/**
 * 持久化枚举类型的用户自定义hibernate映射类型
 * 
 * 使用此类型来进行映射的枚举类，必须实现{@link lm.com.framework.hibernate.IPersistEnum} 接口
 * 
 * @author mrluo735
 *
 */
public class BaseEnumType implements UserType, ParameterizedType {
	private Method returnEnum;

	private Method getPersistedValue;

	private Class<Enum<?>> enumClass;

	private Object enumObject;

	/**
	 * This method uses the parameter values passed during enum mapping
	 * definition and sets corresponding properties defined
	 */
	@SuppressWarnings("unchecked")
	public void setParameterValues(Properties parameters) {
		if (parameters != null) {
			try {
				enumClass = (Class<Enum<?>>) Class.forName(parameters.get("enumClass").toString());
				enumObject = enumClass.getEnumConstants()[0];
				getPersistedValue = enumClass.getMethod("getPersistedValue");
				returnEnum = enumClass.getMethod("returnEnum", new Class[] { String.class });
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method maps the database mapping
	 */
	public int[] sqlTypes() {
		return new int[] { Types.VARCHAR };
	}

	/**
	 * This method maps the class for which user type is created
	 */
	public Class<?> returnedClass() {
		return enumClass;
	}

	public boolean equals(Object x, Object y) throws HibernateException {
		return ObjectUtils.nullSafeEquals(x, y);
	}

	/**
	 * Fetch the hash code
	 */
	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}

	/**
	 * Recreate the enum from the resultset
	 */
	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
			throws HibernateException, SQLException {
		try {
			if (rs.wasNull())
				return null;

			// String value = rs.getString(names[0]);
			Integer value = rs.getInt(names[0]);
			Object returnVal = returnEnum.invoke(enumObject, new Object[] { value.toString() });
			return returnVal;
		} catch (IllegalArgumentException e) {
			System.out.println("异常！BaseEnumType.nullSafeGet.IllegalArgumentException:" + e);
		} catch (IllegalAccessException e) {
			System.out.println("异常！BaseEnumType.nullSafeGet.IllegalAccessException:" + e);
		} catch (InvocationTargetException e) {
			System.out.println("异常！BaseEnumType.nullSafeGet.InvocationTargetException:" + e);
		} catch (Exception e) {
			System.out.println("异常！BaseEnumType.nullSafeGet.Exception:" + e);
		}
		return null;
	}

	/**
	 * Fetch the data from enum and set it in prepared statement
	 */
	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session)
			throws HibernateException, SQLException {
		try {
			if (value == null) {
				st.setObject(index, null);
			} else {
				/*
				 * String prepStmtVal =
				 * getPersistedValue.invoke(value).toString();
				 * st.setString(index, prepStmtVal);
				 */
				Integer prepStmtVal = Integer.parseInt(getPersistedValue.invoke(value).toString());
				st.setInt(index, prepStmtVal);
			}
		} catch (IllegalArgumentException e) {
			System.out.println("异常！BaseEnumType.nullSafeSet.IllegalArgumentException:" + e);
		} catch (IllegalAccessException e) {
			System.out.println("异常！BaseEnumType.nullSafeSet.IllegalAccessException:" + e);
		} catch (InvocationTargetException e) {
			System.out.println("异常！BaseEnumType.nullSafeSet.InvocationTargetException:" + e);
		} catch (Exception e) {
			System.out.println("异常！BaseEnumType.nullSafeSet.Exception:" + e);
		}
	}

	/**
	 * Deep copy method
	 */
	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	public boolean isMutable() {
		return false;
	}

	public Serializable disassemble(Object value) throws HibernateException {
		Object deepCopy = deepCopy(value);

		if (!(deepCopy instanceof Serializable))
			return (Serializable) deepCopy;

		return null;
	}

	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return deepCopy(cached);
	}

	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		return deepCopy(original);
	}
}
