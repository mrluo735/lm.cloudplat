/**
 * @title QueryExpression.java
 * @description TODO
 * @package lm.com.framework.sqlmedium
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年12月1日下午5:17:11
 * @version v1.0
 */
package lm.com.framework.sqlmedium;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import lm.com.framework.StringUtil;

/**
 * 过滤条件表达式
 * 
 * @author mrluo735
 *
 */
public class FilterExpression {
	private StringBuilder whereBuilder = new StringBuilder();

	@SuppressWarnings("serial")
	private static final List<Class<?>> numberClasses = new ArrayList<Class<?>>() {
		{
			add(boolean.class);
			add(Boolean.class);
			add(byte.class);
			add(Byte.class);
			add(short.class);
			add(Short.class);
			add(int.class);
			add(Integer.class);
			add(long.class);
			add(Long.class);
			add(float.class);
			add(Float.class);
			add(double.class);
			add(Double.class);
			add(BigInteger.class);
			add(BigDecimal.class);
			add(Number.class);
		}
	};

	/**
	 * 构造函数
	 */
	public FilterExpression() {
	}

	/**
	 * 获取实例
	 */
	public static FilterExpression getInstance() {
		return new FilterExpression();
	}

	/**
	 * like模糊查询
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	public FilterExpression like(String field, String value) {
		if (StringUtil.isNullOrWhiteSpace(field))
			return this;
		this.whereBuilder.append(StringUtil.format("{0} LIKE ''%{1}%''", field, value));
		return this;
	}

	/**
	 * like 左模糊查询
	 * <p>
	 * like '%dfd'
	 * </p>
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	public FilterExpression likeLeft(String field, String value) {
		if (StringUtil.isNullOrWhiteSpace(field))
			return this;
		this.whereBuilder.append(StringUtil.format("{0} LIKE ''%{1}''", field, value));
		return this;
	}

	/**
	 * like 右模糊查询
	 * <p>
	 * like 'dfd%'
	 * </p>
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	public FilterExpression likeRight(String field, String value) {
		if (StringUtil.isNullOrWhiteSpace(field))
			return this;
		this.whereBuilder.append(StringUtil.format("{0} LIKE ''{1}%''", field, value));
		return this;
	}

	/**
	 * 等于
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	public FilterExpression eq(String field, Object value) {
		if (StringUtil.isNullOrWhiteSpace(field))
			return this;
		if (numberClasses.contains(value.getClass()))
			this.whereBuilder.append(StringUtil.format("{0} = {1}", field, value));
		else
			this.whereBuilder.append(StringUtil.format("{0} = ''{1}''", field, value));
		return this;
	}

	/**
	 * 不等于
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	public FilterExpression ne(String field, Object value) {
		if (StringUtil.isNullOrWhiteSpace(field))
			return this;
		if (numberClasses.contains(value.getClass()))
			this.whereBuilder.append(StringUtil.format("{0} != {1}", field, value));
		else
			this.whereBuilder.append(StringUtil.format("{0} != ''{1}''", field, value));
		return this;
	}

	/**
	 * 大于
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	public FilterExpression gt(String field, Object value) {
		if (StringUtil.isNullOrWhiteSpace(field))
			return this;
		if (numberClasses.contains(value.getClass()))
			this.whereBuilder.append(StringUtil.format("{0} > {1}", field, value));
		else
			this.whereBuilder.append(StringUtil.format("{0} > ''{1}''", field, value));
		return this;
	}

	/**
	 * 大于等于
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	public FilterExpression ge(String field, Object value) {
		if (StringUtil.isNullOrWhiteSpace(field))
			return this;
		if (numberClasses.contains(value.getClass()))
			this.whereBuilder.append(StringUtil.format("{0} >= {1}", field, value));
		else
			this.whereBuilder.append(StringUtil.format("{0} >= ''{1}''", field, value));
		return this;
	}

	/**
	 * 小于
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	public FilterExpression lt(String field, Object value) {
		if (StringUtil.isNullOrWhiteSpace(field))
			return this;
		if (numberClasses.contains(value.getClass()))
			this.whereBuilder.append(StringUtil.format("{0} < {1}", field, value));
		else
			this.whereBuilder.append(StringUtil.format("{0} < ''{1}''", field, value));
		return this;
	}

	/**
	 * 小于等于
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	public FilterExpression le(String field, Object value) {
		if (StringUtil.isNullOrWhiteSpace(field))
			return this;
		if (numberClasses.contains(value.getClass()))
			this.whereBuilder.append(StringUtil.format("{0} <= {1}", field, value));
		else
			this.whereBuilder.append(StringUtil.format("{0} <= ''{1}''", field, value));
		return this;
	}

	/**
	 * is null
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	public FilterExpression isNull(String field) {
		if (StringUtil.isNullOrWhiteSpace(field))
			return this;
		this.whereBuilder.append(StringUtil.format("{0} IS NULL", field));
		return this;
	}

	/**
	 * is not null
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	public FilterExpression isNotNull(String field) {
		if (StringUtil.isNullOrWhiteSpace(field))
			return this;
		this.whereBuilder.append(StringUtil.format("{0} IS NOT NULL", field));
		return this;
	}

	/**
	 * in
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	public FilterExpression in(String field, String value) {
		if (StringUtil.isNullOrWhiteSpace(field))
			return this;
		this.whereBuilder.append(StringUtil.format("{0} IN ({1})", field, value));
		return this;
	}

	/**
	 * not in
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	public FilterExpression notIn(String field, String value) {
		if (StringUtil.isNullOrWhiteSpace(field))
			return this;
		this.whereBuilder.append(StringUtil.format("{0} NOT IN ({1})", field, value));
		return this;
	}

	/**
	 * between and
	 * 
	 * @param field
	 * @param startValue
	 * @param endValue
	 * @return
	 */
	public FilterExpression betweenAnd(String field, Object startValue, Object endValue) {
		if (StringUtil.isNullOrWhiteSpace(field))
			return this;
		if (numberClasses.contains(startValue.getClass()))
			this.whereBuilder.append(StringUtil.format("{0} BETWEEN {1} AND {2}", field, startValue, endValue));
		else
			this.whereBuilder.append(StringUtil.format("{0} BETWEEN ''{1}'' AND ''{2}''", field, startValue, endValue));
		return this;
	}

	/**
	 * not between and
	 * 
	 * @param field
	 * @param startValue
	 * @param endValue
	 * @return
	 */
	public FilterExpression notBetweenAnd(String field, Object startValue, Object endValue) {
		if (StringUtil.isNullOrWhiteSpace(field))
			return this;
		if (numberClasses.contains(startValue.getClass()))
			this.whereBuilder.append(StringUtil.format("{0} NOT BETWEEN {1} AND {2}", field, startValue, endValue));
		else
			this.whereBuilder
					.append(StringUtil.format("{0} NOT BETWEEN ''{1}'' AND ''{2}''", field, startValue, endValue));
		return this;
	}

	/**
	 * exists
	 * 
	 * @param expression
	 * @return
	 */
	public FilterExpression exists(String expression) {
		if (StringUtil.isNullOrWhiteSpace(expression))
			return this;
		this.whereBuilder.append(StringUtil.format("EXISTS({0})", expression));
		return this;
	}

	/**
	 * not exists
	 * 
	 * @param expression
	 * @return
	 */
	public FilterExpression notExists(String expression) {
		if (StringUtil.isNullOrWhiteSpace(expression))
			return this;
		this.whereBuilder.append(StringUtil.format("NOT EXISTS({0})", expression));
		return this;
	}

	// region ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 特殊表达式 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	/**
	 * value > field
	 * <p>
	 * 例如：123.00 > price
	 * </p>
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	public FilterExpression gtReverse(String field, Object value) {
		if (StringUtil.isNullOrWhiteSpace(field))
			return this;

		if (numberClasses.contains(value.getClass()))
			this.whereBuilder.append(StringUtil.format("{0} > {1}", value, field));
		else
			this.whereBuilder.append(StringUtil.format("''{0}'' > {1}", value, field));
		return this;
	}
	
	/**
	 * value >= field
	 * <p>
	 * 例如：123.00 >= price
	 * </p>
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	public FilterExpression geReverse(String field, Object value) {
		if (StringUtil.isNullOrWhiteSpace(field))
			return this;

		if (numberClasses.contains(value.getClass()))
			this.whereBuilder.append(StringUtil.format("{0} >= {1}", value, field));
		else
			this.whereBuilder.append(StringUtil.format("''{0}'' >= {1}", value, field));
		return this;
	}

	/**
	 * value < field
	 * <p>
	 * 例如：123.00 < price
	 * </p>
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	public FilterExpression ltReverse(String field, Object value) {
		if (StringUtil.isNullOrWhiteSpace(field))
			return this;

		if (numberClasses.contains(value.getClass()))
			this.whereBuilder.append(StringUtil.format("{0} < {1}", value, field));
		else
			this.whereBuilder.append(StringUtil.format("''{0}'' < {1}", value, field));
		return this;
	}
	
	/**
	 * value <= field
	 * <p>
	 * 例如：123.00 <= price
	 * </p>
	 * 
	 * @param field
	 * @param value
	 * @return
	 */
	public FilterExpression leReverse(String field, Object value) {
		if (StringUtil.isNullOrWhiteSpace(field))
			return this;

		if (numberClasses.contains(value.getClass()))
			this.whereBuilder.append(StringUtil.format("{0} <= {1}", value, field));
		else
			this.whereBuilder.append(StringUtil.format("''{0}'' <= {1}", value, field));
		return this;
	}

	/**
	 * = all(...)
	 * 
	 * @param field
	 * @param expression
	 * @return
	 */
	public FilterExpression eqAll(String field, String expression) {
		if (StringUtil.isNullOrWhiteSpace(field) || StringUtil.isNullOrWhiteSpace(expression))
			return this;

		this.whereBuilder.append(StringUtil.format("{0} = ALL({1})", field, expression));
		return this;
	}

	/**
	 * = any(...)
	 * 
	 * @param field
	 * @param expression
	 * @return
	 */
	public FilterExpression eqAny(String field, String expression) {
		if (StringUtil.isNullOrWhiteSpace(field) || StringUtil.isNullOrWhiteSpace(expression))
			return this;

		this.whereBuilder.append(StringUtil.format("{0} = ANY({1})", field, expression));
		return this;
	}

	/**
	 * = some(...)
	 * 
	 * @param field
	 * @param expression
	 * @return
	 */
	public FilterExpression eqSome(String field, String expression) {
		if (StringUtil.isNullOrWhiteSpace(field) || StringUtil.isNullOrWhiteSpace(expression))
			return this;

		this.whereBuilder.append(StringUtil.format("{0} = SOME({1})", field, expression));
		return this;
	}

	/**
	 * != all(...)
	 * 
	 * @param field
	 * @param expression
	 * @return
	 */
	public FilterExpression neAll(String field, String expression) {
		if (StringUtil.isNullOrWhiteSpace(field) || StringUtil.isNullOrWhiteSpace(expression))
			return this;

		this.whereBuilder.append(StringUtil.format("{0} != ALL({1})", field, expression));
		return this;
	}

	/**
	 * != any(...)
	 * 
	 * @param field
	 * @param expression
	 * @return
	 */
	public FilterExpression neAny(String field, String expression) {
		if (StringUtil.isNullOrWhiteSpace(field) || StringUtil.isNullOrWhiteSpace(expression))
			return this;

		this.whereBuilder.append(StringUtil.format("{0} != ANY({1})", field, expression));
		return this;
	}

	/**
	 * != some(...)
	 * 
	 * @param field
	 * @param expression
	 * @return
	 */
	public FilterExpression neSome(String field, String expression) {
		if (StringUtil.isNullOrWhiteSpace(field) || StringUtil.isNullOrWhiteSpace(expression))
			return this;

		this.whereBuilder.append(StringUtil.format("{0} != SOME({1})", field, expression));
		return this;
	}

	/**
	 * > all(...)
	 * 
	 * @param field
	 * @param expression
	 * @return
	 */
	public FilterExpression gtAll(String field, String expression) {
		if (StringUtil.isNullOrWhiteSpace(field) || StringUtil.isNullOrWhiteSpace(expression))
			return this;

		this.whereBuilder.append(StringUtil.format("{0} > ALL({1})", field, expression));
		return this;
	}

	/**
	 * != any(...)
	 * 
	 * @param field
	 * @param expression
	 * @return
	 */
	public FilterExpression gtAny(String field, String expression) {
		if (StringUtil.isNullOrWhiteSpace(field) || StringUtil.isNullOrWhiteSpace(expression))
			return this;

		this.whereBuilder.append(StringUtil.format("{0} > ANY({1})", field, expression));
		return this;
	}

	/**
	 * > some(...)
	 * 
	 * @param field
	 * @param expression
	 * @return
	 */
	public FilterExpression gtSome(String field, String expression) {
		if (StringUtil.isNullOrWhiteSpace(field) || StringUtil.isNullOrWhiteSpace(expression))
			return this;

		this.whereBuilder.append(StringUtil.format("{0} > SOME({1})", field, expression));
		return this;
	}

	/**
	 * >= all(...)
	 * 
	 * @param field
	 * @param expression
	 * @return
	 */
	public FilterExpression geAll(String field, String expression) {
		if (StringUtil.isNullOrWhiteSpace(field) || StringUtil.isNullOrWhiteSpace(expression))
			return this;

		this.whereBuilder.append(StringUtil.format("{0} >= ALL({1})", field, expression));
		return this;
	}

	/**
	 * >= any(...)
	 * 
	 * @param field
	 * @param expression
	 * @return
	 */
	public FilterExpression geAny(String field, String expression) {
		if (StringUtil.isNullOrWhiteSpace(field) || StringUtil.isNullOrWhiteSpace(expression))
			return this;

		this.whereBuilder.append(StringUtil.format("{0} >= ANY({1})", field, expression));
		return this;
	}

	/**
	 * >= some(...)
	 * 
	 * @param field
	 * @param expression
	 * @return
	 */
	public FilterExpression geSome(String field, String expression) {
		if (StringUtil.isNullOrWhiteSpace(field) || StringUtil.isNullOrWhiteSpace(expression))
			return this;

		this.whereBuilder.append(StringUtil.format("{0} >= SOME({1})", field, expression));
		return this;
	}

	/**
	 * < all(...)
	 * 
	 * @param field
	 * @param expression
	 * @return
	 */
	public FilterExpression ltAll(String field, String expression) {
		if (StringUtil.isNullOrWhiteSpace(field) || StringUtil.isNullOrWhiteSpace(expression))
			return this;

		this.whereBuilder.append(StringUtil.format("{0} < ALL({1})", field, expression));
		return this;
	}

	/**
	 * < any(...)
	 * 
	 * @param field
	 * @param expression
	 * @return
	 */
	public FilterExpression ltAny(String field, String expression) {
		if (StringUtil.isNullOrWhiteSpace(field) || StringUtil.isNullOrWhiteSpace(expression))
			return this;

		this.whereBuilder.append(StringUtil.format("{0} < ANY({1})", field, expression));
		return this;
	}

	/**
	 * < some(...)
	 * 
	 * @param field
	 * @param expression
	 * @return
	 */
	public FilterExpression ltSome(String field, String expression) {
		if (StringUtil.isNullOrWhiteSpace(field) || StringUtil.isNullOrWhiteSpace(expression))
			return this;

		this.whereBuilder.append(StringUtil.format("{0} < SOME({1})", field, expression));
		return this;
	}

	/**
	 * <= all(...)
	 * 
	 * @param field
	 * @param expression
	 * @return
	 */
	public FilterExpression leAll(String field, String expression) {
		if (StringUtil.isNullOrWhiteSpace(field) || StringUtil.isNullOrWhiteSpace(expression))
			return this;

		this.whereBuilder.append(StringUtil.format("{0} <= ALL({1})", field, expression));
		return this;
	}

	/**
	 * <= any(...)
	 * 
	 * @param field
	 * @param expression
	 * @return
	 */
	public FilterExpression leAny(String field, String expression) {
		if (StringUtil.isNullOrWhiteSpace(field) || StringUtil.isNullOrWhiteSpace(expression))
			return this;

		this.whereBuilder.append(StringUtil.format("{0} <= ANY({1})", field, expression));
		return this;
	}

	/**
	 * <= some(...)
	 * 
	 * @param field
	 * @param expression
	 * @return
	 */
	public FilterExpression leSome(String field, String expression) {
		if (StringUtil.isNullOrWhiteSpace(field) || StringUtil.isNullOrWhiteSpace(expression))
			return this;

		this.whereBuilder.append(StringUtil.format("{0} <= SOME({1})", field, expression));
		return this;
	}
	// endregion ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 特殊表达式 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

	/**
	 * and连接
	 * 
	 * @return
	 */
	public FilterExpression and() {
		this.whereBuilder.append(" AND ");
		return this;
	}

	/**
	 * or连接
	 * 
	 * @return
	 */
	public FilterExpression or() {
		this.whereBuilder.append(" OR ");
		return this;
	}

	/**
	 * 组开始
	 * 
	 * @return
	 */
	public FilterExpression beginGroup() {
		this.whereBuilder.append("(");
		return this;
	}

	/**
	 * 组结束
	 * 
	 * @return
	 */
	public FilterExpression endGroup() {
		this.whereBuilder.append(")");
		return this;
	}

	/**
	 * 转成条件
	 * 
	 * @return
	 */
	public String toWhere() {
		String where = this.whereBuilder.toString().trim();
		return StringUtil.trimEnd(where, "ANDOR").trim();
	}

	// public FilterExpression addExpression(String field,
	// EnumRelationalOperator
	// relateionOperator, Object value) {
	// if (StringUtil.isNullOrWhiteSpace(field))
	// return this;
	// List<Class<?>> classes = new ArrayList<>();
	// classes.add(boolean.class);
	// classes.add(Boolean.class);
	// classes.add(byte.class);
	// classes.add(Byte.class);
	// classes.add(short.class);
	// classes.add(Short.class);
	// classes.add(int.class);
	// classes.add(Integer.class);
	// classes.add(long.class);
	// classes.add(Long.class);
	// classes.add(float.class);
	// classes.add(Float.class);
	// classes.add(double.class);
	// classes.add(Double.class);
	// classes.add(BigInteger.class);
	// classes.add(BigDecimal.class);
	// classes.add(Number.class);
	// // List<Class<?>> classes2 = new ArrayList<Class<?>>() {
	// // {
	// // add(byte.class);
	// // add(Byte.class);
	// // add(short.class);
	// // add(Short.class);
	// // add(int.class);
	// // add(Integer.class);
	// // }
	// // };
	// switch (relateionOperator) {
	// case LIKE:
	// this.whereList.add(StringUtil.format("{0} LIKE '%{1}%'", field));
	// break;
	// case LIKER:
	// this.whereList.add(StringUtil.format("{0} LIKE '{1}%'", field));
	// break;
	// case LIKEL:
	// this.whereList.add(StringUtil.format("{0} LIKE '%{1}'", field));
	// break;
	// case EQ:
	// case NE:
	// case GT:
	// case GE:
	// case LT:
	// case LE:
	// String description =
	// EnumUtil.getDescription(EnumRelationalOperator.class,
	// relateionOperator.name);
	// if (classes.contains(value.getClass())) {
	// this.whereList.add(StringUtil.format("{0} {1} {2}", field, description,
	// value));
	// } else {
	// this.whereList.add(StringUtil.format("{0} {1} '{2}'", field, description,
	// value));
	// }
	// break;
	// default:
	// break;
	// }
	// return this;
	// }

	// region 关系运算符
	// /**
	// * 关系运算符
	// *
	// * @author mrluo735
	// *
	// */
	// public enum EnumRelationalOperator implements
	// IBaseEnumInt<EnumRelationalOperator> {
	// /**
	// * 模糊匹配
	// */
	// LIKE(10, "LIKE"),
	// /**
	// * 右模糊匹配
	// */
	// LIKER(11, "LIKER"),
	// /**
	// * 左模糊匹配
	// */
	// LIKEL(12, "LIKEL"),
	// /**
	// * 等于
	// */
	// EQ(20, "="),
	// /**
	// * 不等于
	// */
	// NE(21, "!="),
	// /**
	// * 大于
	// */
	// GT(30, ">"),
	// /**
	// * 大于等于
	// */
	// GE(31, ">="),
	// /**
	// * 小于
	// */
	// LT(40, "<"),
	// /**
	// * 小于等于
	// */
	// LE(41, "<=");
	//
	// private String name;
	// private Integer value;
	// private String description;
	//
	// static Map<String, EnumRelationalOperator> enumNameMap = new
	// HashMap<String, EnumRelationalOperator>();
	// static Map<Integer, EnumRelationalOperator> enumValueMap = new
	// HashMap<Integer, EnumRelationalOperator>();
	// static {
	// for (EnumRelationalOperator type : EnumRelationalOperator.values()) {
	// enumNameMap.put(type.toString(), type);
	// enumValueMap.put(type.getValue(), type);
	// }
	// }
	//
	// private EnumRelationalOperator(Integer value, String description) {
	// this.value = value;
	// this.description = description;
	// }
	//
	// public static EnumRelationalOperator getEnum(String name) {
	// return enumNameMap.get(name);
	// }
	//
	// public static EnumRelationalOperator getEnum(Integer value) {
	// return enumValueMap.get(value);
	// }
	//
	// public String getName() {
	// return name;
	// }
	//
	// public void setName(String name) {
	// this.name = name;
	// }
	//
	// public Integer getValue() {
	// return value;
	// }
	//
	// public void setValue(Integer value) {
	// this.value = value;
	// }
	//
	// public String getDescription() {
	// return description;
	// }
	//
	// public void setDescription(String description) {
	// this.description = description;
	// }
	// }
	// endregion
}
