package lm.com.framework.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import lm.com.framework.EnumUtil;
import lm.com.framework.ObjectUtil;
import lm.com.framework.enumerate.IBaseEnumInt;
import lm.com.framework.http.EnumHttpStatus;

/**
 * 响应传输对象
 * 
 * @author mrluo735
 *
 */
public class ResponseDTO implements Serializable {
	private static final long serialVersionUID = 3315965630264310008L;

	/**
	 * 
	 */
	private boolean success = true;

	private int code = EnumHttpStatus.Two_OK.getValue();

	private String message = EnumHttpStatus.Two_OK.getDescription();

	private Object data;

	/**
	 * 重载+1 构造函数
	 */
	public ResponseDTO() {
	}

	/**
	 * 重载+2 构造函数
	 * 
	 * @param success
	 * @param code
	 * @param message
	 */
	public ResponseDTO(boolean success, int code, String message) {
		this.success = success;
		this.code = code;
		this.message = message;
	}

	/**
	 * 获取是否成功
	 * 
	 * @return
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * 设置是否成功
	 * 
	 * @param success
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * 获取代码
	 * 
	 * @return
	 */
	public int getCode() {
		return this.code;
	}

	/**
	 * 设置代码
	 * 
	 * @param code
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * 获取提示信息
	 * 
	 * @return
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * 设置提示信息
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 设置数据
	 * 
	 * @param data
	 */
	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * 获取数据
	 * 
	 * @return
	 */
	public Object getData() {
		return data;
	}

	/**
	 * 获取数据
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getDataT() {
		return (T) this.data;
	}

	/**
	 * 获取数据
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getList() {
		return (List<T>) this.data;
	}

	/**
	 * 获取数据
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getMap() {
		return (Map<String, Object>) this.data;
	}

	/**
	 * 获取数据
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getListMap() {
		return (List<Map<String, Object>>) this.data;
	}

	/**
	 * 获取数据
	 * 
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public <T> T get(Class<T> clazz) {
		return ObjectUtil.toDataType(clazz, this.data, null);
	}

	/**
	 * 获取数据
	 * 
	 * @param defaultValue
	 * @return
	 * @throws Exception
	 */
	public <T> T get(Class<T> clazz, T defaultValue) {
		return ObjectUtil.toDataType(clazz, this.data, defaultValue);
	}

	/**
	 * 获取布尔的值
	 * 
	 * @param defaultValue
	 * @return
	 */
	public boolean getBoolean(boolean defaultValue) {
		return ObjectUtil.toDataType(Boolean.class, this.data, defaultValue);
	}

	/**
	 * 获取整型的值
	 * 
	 * @param defaultValue
	 * @return
	 */
	public byte getByte(byte defaultValue) {
		return ObjectUtil.toDataType(Byte.class, this.data, defaultValue);
	}

	/**
	 * 获取整型的值
	 * 
	 * @param defaultValue
	 * @return
	 */
	public short getShort(short defaultValue) {
		return ObjectUtil.toDataType(Short.class, this.data, defaultValue);
	}

	/**
	 * 获取整型的值
	 * 
	 * @param defaultValue
	 * @return
	 */
	public int getInteger(int defaultValue) {
		return ObjectUtil.toDataType(Integer.class, this.data, defaultValue);
	}

	/**
	 * 获取长整型的值
	 * 
	 * @param defaultValue
	 * @return
	 */
	public Long getLong() {
		return ObjectUtil.toDataType(Long.class, this.data, null);
	}

	/**
	 * 获取长整型的值
	 * 
	 * @param defaultValue
	 * @return
	 */
	public long getLong(long defaultValue) {
		return ObjectUtil.toDataType(Long.class, this.data, defaultValue);
	}

	/**
	 * 获取单精度的值
	 * 
	 * @param defaultValue
	 * @return
	 */
	public float getFloat(float defaultValue) {
		return ObjectUtil.toDataType(Float.class, this.data, defaultValue);
	}

	/**
	 * 获取双精度的值
	 * 
	 * @param defaultValue
	 * @return
	 */
	public double getDouble(double defaultValue) {
		return ObjectUtil.toDataType(Double.class, this.data, defaultValue);
	}

	/**
	 * 获取BigInteger的值
	 * 
	 * @param defaultValue
	 * @return
	 */
	public BigInteger getBigInteger(BigInteger defaultValue) {
		return ObjectUtil.toDataType(BigInteger.class, this.data, defaultValue);
	}

	/**
	 * 获取BigDecimal的值
	 * 
	 * @param defaultValue
	 * @return
	 */
	public BigDecimal getBigDecimal(BigDecimal defaultValue) {
		return ObjectUtil.toDataType(BigDecimal.class, this.data, defaultValue);
	}

	/**
	 * 获取时间类型的值
	 * 
	 * @param defaultValue
	 * @return
	 */
	public Time getTime(Time defaultValue) {
		return ObjectUtil.toDataType(Time.class, this.data, defaultValue);
	}

	/**
	 * 获取日期类型的值
	 * 
	 * @param defaultValue
	 * @return
	 */
	public Date getDate(Date defaultValue) {
		return ObjectUtil.toDataType(Date.class, this.data, defaultValue);
	}

	/**
	 * 获取Timestamp类型的值
	 * 
	 * @param defaultValue
	 * @return
	 */
	public Timestamp getDate(Timestamp defaultValue) {
		return ObjectUtil.toDataType(Timestamp.class, this.data, defaultValue);
	}

	/**
	 * 获取日期类型的值
	 * 
	 * @param defaultValue
	 * @return
	 */
	public java.util.Date getDate(java.util.Date defaultValue) {
		return ObjectUtil.toDataType(java.util.Date.class, this.data, defaultValue);
	}

	/**
	 * 获取UUID类型的值
	 * 
	 * @param defaultValue
	 * @return
	 */
	public UUID getUUID(UUID defaultValue) {
		return ObjectUtil.toDataType(UUID.class, this.data, defaultValue);
	}

	/**
	 * 获取枚举类型的值
	 * 
	 * @param defaultValue
	 * @return
	 */
	public <E extends Enum<?>> E getEnum(Class<E> clazz, E defaultValue) {
		return ObjectUtil.toDataType(clazz, this.data, defaultValue);
	}

	/**
	 * 获取枚举类型的值
	 * 
	 * @param defaultValue
	 * @return
	 */
	public <E extends IBaseEnumInt<?>> E getEnum(Class<E> clazz, Integer defaultValue) {
		return EnumUtil.toEnum(clazz, this.getInteger(defaultValue));
	}

	/**
	 * 获取字符型的值
	 * <p>
	 * 默认值: ""
	 * </p>
	 * 
	 * @return
	 */
	public String getString() {
		return ObjectUtil.toDataType(String.class, this.data, "");
	}

	/**
	 * 获取字符型的值
	 * 
	 * @param defaultValue
	 * @return
	 */
	public String getString(String defaultValue) {
		return ObjectUtil.toDataType(String.class, this.data, defaultValue);
	}
}
