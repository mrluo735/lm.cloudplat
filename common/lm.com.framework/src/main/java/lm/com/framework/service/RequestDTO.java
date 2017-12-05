package lm.com.framework.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import lm.com.framework.EnumUtil;
import lm.com.framework.ObjectUtil;
import lm.com.framework.StringUtil;
import lm.com.framework.enumerate.IBaseEnumInt;

/**
 * 请求传输对象
 * 
 * @author mrluo735
 *
 */
public class RequestDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1380879395062010464L;

	private Map<String, Object> data = new HashMap<String, Object>();

	// private List<GroupDTO> groups = new ArrayList<GroupDTO>();
	//
	// private List<FilterDTO> filters = new ArrayList<FilterDTO>();

	private List<SortDTO> sorts = new ArrayList<SortDTO>();

	@SuppressWarnings("rawtypes")
	private IdentityDTO identity = new IdentityDTO();

	private String method;

	private String locale = "zh_CN";

	/**
	 * 重载+1 构造函数
	 */
	public RequestDTO() {
	}

	/**
	 * 重载+2 构造函数
	 * 
	 * @param method
	 * @param context
	 */
	public RequestDTO(String method) {
		this.method = method;
	}

	/**
	 * 重载+3 构造函数
	 * 
	 * @param method
	 * @param context
	 */
	public <T> RequestDTO(String method, IdentityDTO<T> identity) {
		this.method = method;
		this.identity = identity;
	}

	/**
	 * 获取data
	 * 
	 * @return
	 */
	public Map<String, Object> getData() {
		return this.data;
	}

	/**
	 * 获取组
	 * 
	 * @return
	 */
	// public List<GroupDTO> getGroups() {
	// return this.groups;
	// }

	/**
	 * 设置组
	 * 
	 * @param group
	 * @return
	 */
	// public RequestDTO setGroup(GroupDTO group) {
	// this.groups.add(group);
	// return this;
	// }

	/**
	 * 设置组
	 * 
	 * @param groups
	 * @return
	 */
	// public RequestDTO setGroups(List<GroupDTO> groups) {
	// this.groups = groups;
	// return this;
	// }

	/**
	 * 获取过滤条件
	 * 
	 * @return
	 */
	// public List<FilterDTO> getFilters() {
	// return this.filters;
	// }

	/**
	 * put 过滤条件
	 * 
	 * @param filter
	 * @return
	 */
	// public RequestDTO setFilter(FilterDTO filter) {
	// this.filters.add(filter);
	// return this;
	// }

	/**
	 * put 过滤条件
	 * 
	 * @param filters
	 * @return
	 */
	// public RequestDTO setFilters(List<FilterDTO> filters) {
	// this.filters = filters;
	// return this;
	// }

	/**
	 * 获取排序
	 * 
	 * @return
	 */
	public List<SortDTO> getSorts() {
		return this.sorts;
	}

	/**
	 * put 排序
	 * 
	 * @param sort
	 * @return
	 */
	public RequestDTO setSort(SortDTO sort) {
		this.sorts.add(sort);
		return this;
	}

	/**
	 * 设置排序
	 * 
	 * @param sorts
	 * @return
	 */
	public RequestDTO setSorts(List<SortDTO> sorts) {
		this.sorts = sorts;
		return this;
	}

	/**
	 * 获取上下文
	 * 
	 * @return
	 */
	public <T> IdentityDTO<T> getIdentity() {
		return identity;
	}

	/**
	 * 设置上下文
	 * 
	 * @param context
	 */
	public <T> void setIdentity(IdentityDTO<T> identity) {
		this.identity = identity;
	}

	/**
	 * 获取方法
	 * 
	 * @return
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * 设置方法
	 * 
	 * @param method
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * 获取方言
	 * 
	 * @return
	 */
	public Locale getLocale() {
		if (StringUtil.isNullOrWhiteSpace(this.locale) || this.locale.indexOf("_") < 0)
			return Locale.SIMPLIFIED_CHINESE;
		try {
			String[] locales = this.locale.split("_");
			return new Locale(locales[0], locales[1]);
		} catch (Exception ex) {
			return Locale.SIMPLIFIED_CHINESE;
		}
	}

	/**
	 * 设置方言
	 * 
	 * @param locale
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}

	/**
	 * 重载+1 put数据
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public RequestDTO put(String key, Object value) {
		data.put(key, value);
		return this;
	}

	/**
	 * 重载+2 put map数据
	 * 
	 * @param map
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public RequestDTO put(Map map) {
		this.data.putAll(map);
		return this;
	}

	/**
	 * 获取数据
	 * 
	 * @param key
	 * @return
	 */
	public Object get(Object key) {
		return data.get(key);
	}

	/**
	 * 获取数据
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		return (T) data.get(key);
	}

	/**
	 * 获取数据
	 * 
	 * @param key
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public <T> T get(String key, Class<T> clazz) {
		return ObjectUtil.toDataType(clazz, this.data.get(key), null);
	}

	/**
	 * 获取数据
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 * @throws Exception
	 */
	public <T> T get(String key, Class<T> clazz, T defaultValue) {
		return ObjectUtil.toDataType(clazz, this.data.get(key), defaultValue);
	}

	/**
	 * 获取布尔的值
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public boolean getBoolean(String key, boolean defaultValue) {
		return ObjectUtil.toDataType(Boolean.class, this.data.get(key), defaultValue);
	}

	/**
	 * 获取整型的值
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public byte getByte(String key, byte defaultValue) {
		return ObjectUtil.toDataType(Byte.class, this.data.get(key), defaultValue);
	}

	/**
	 * 获取整型的值
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public short getShort(String key, short defaultValue) {
		return ObjectUtil.toDataType(Short.class, this.data.get(key), defaultValue);
	}

	/**
	 * 获取整型的值
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public int getInteger(String key, int defaultValue) {
		return ObjectUtil.toDataType(Integer.class, this.data.get(key), defaultValue);
	}

	/**
	 * 获取长整型的值
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public Long getLong(String key) {
		return ObjectUtil.toDataType(Long.class, this.data.get(key), null);
	}

	/**
	 * 获取长整型的值
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public long getLong(String key, long defaultValue) {
		return ObjectUtil.toDataType(Long.class, this.data.get(key), defaultValue);
	}

	/**
	 * 获取单精度的值
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public float getFloat(String key, float defaultValue) {
		return ObjectUtil.toDataType(Float.class, this.data.get(key), defaultValue);
	}

	/**
	 * 获取双精度的值
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public double getDouble(String key, double defaultValue) {
		return ObjectUtil.toDataType(Double.class, this.data.get(key), defaultValue);
	}

	/**
	 * 获取BigInteger的值
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public BigInteger getBigInteger(String key, BigInteger defaultValue) {
		return ObjectUtil.toDataType(BigInteger.class, this.data.get(key), defaultValue);
	}

	/**
	 * 获取BigDecimal的值
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
		return ObjectUtil.toDataType(BigDecimal.class, this.data.get(key), defaultValue);
	}

	/**
	 * 获取时间类型的值
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public Time getTime(String key, Time defaultValue) {
		return ObjectUtil.toDataType(Time.class, this.data.get(key), defaultValue);
	}

	/**
	 * 获取日期类型的值
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public Date getDate(String key, Date defaultValue) {
		return ObjectUtil.toDataType(Date.class, this.data.get(key), defaultValue);
	}

	/**
	 * 获取Timestamp类型的值
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public Timestamp getDate(String key, Timestamp defaultValue) {
		return ObjectUtil.toDataType(Timestamp.class, this.data.get(key), defaultValue);
	}

	/**
	 * 获取日期类型的值
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public java.util.Date getDate(String key, java.util.Date defaultValue) {
		return ObjectUtil.toDataType(java.util.Date.class, this.data.get(key), defaultValue);
	}

	/**
	 * 获取UUID类型的值
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public UUID getUUID(String key, UUID defaultValue) {
		return ObjectUtil.toDataType(UUID.class, this.data.get(key), defaultValue);
	}

	/**
	 * 获取枚举类型的值
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public <E extends Enum<?>> E getEnum(String key, Class<E> clazz, E defaultValue) {
		return ObjectUtil.toDataType(clazz, this.data.get(key), defaultValue);
	}

	/**
	 * 获取枚举类型的值
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public <E extends IBaseEnumInt<?>> E getEnum(String key, Class<E> clazz, Integer defaultValue) {
		return EnumUtil.toEnum(clazz, this.getInteger(key, defaultValue));
	}

	/**
	 * 获取字符型的值
	 * <p>
	 * 默认值: ""
	 * </p>
	 * 
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		return ObjectUtil.toDataType(String.class, this.data.get(key), "");
	}

	/**
	 * 获取字符型的值
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getString(String key, String defaultValue) {
		return ObjectUtil.toDataType(String.class, this.data.get(key), defaultValue);
	}

	/**
	 * 清除
	 * 
	 * @return
	 */
	public RequestDTO clear() {
		this.data.clear();
		// this.groups.clear();
		// this.filters.clear();
		this.sorts.clear();
		return this;
	}

	/**
	 * 是否包含key
	 */
	public boolean containsKey(Object key) {
		return data.containsKey(key);
	}

	/**
	 * 是否包含值
	 * 
	 * @param value
	 * @return
	 */
	public boolean containsValue(Object value) {
		return data.containsValue(value);
	}

	/**
	 * key 存在，并且 value 不为 null
	 */
	public boolean notNull(Object key) {
		return data.get(key) != null;
	}

	/**
	 * key 不存在，或者 key 存在但 value 为null
	 */
	public boolean isNull(Object key) {
		return data.get(key) == null;
	}

	/**
	 * key 存在，并且 value 为 true，则返回 true
	 */
	public boolean isTrue(Object key) {
		Object value = data.get(key);
		return (value instanceof Boolean && ((Boolean) value == true));
	}

	/**
	 * key 存在，并且 value 为 false，则返回 true
	 */
	public boolean isFalse(Object key) {
		Object value = data.get(key);
		return (value instanceof Boolean && ((Boolean) value == false));
	}

	/**
	 * 移除数据
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T remove(Object key) {
		return (T) data.remove(key);
	}

	/**
	 * 
	 * @param RequestDTO
	 * @return
	 */
	public boolean equals(RequestDTO RequestDTO) {
		return RequestDTO != null && this.data.equals(RequestDTO.data);
	}
}
