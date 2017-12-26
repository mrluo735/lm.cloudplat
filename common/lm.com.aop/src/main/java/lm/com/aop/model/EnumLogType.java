/**
 * @title EnumClassType.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.pro.aop
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年5月2日上午11:37:39
 * @version v1.0.0
 */
package lm.com.aop.model;

import java.util.HashMap;
import java.util.Map;

import lm.com.framework.enumerate.IBaseEnumInt;

/**
 * @ClassName: EnumLogType
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年5月2日 上午11:37:39
 * 
 */
public enum EnumLogType implements IBaseEnumInt<EnumLogType> {
	/**
	 * 执行开销日志
	 */
	Run(1, "执行开销日志"),
	/**
	 * 行为操作日志
	 */
	Action(2, "行为操作日志"),
	/**
	 * 错误日志
	 */
	Error(3, "错误日志"),
	/**
	 * 上传日志
	 */
	Upload(4, "上传日志");

	private String name;
	private Integer value;
	private String description;

	static Map<String, EnumLogType> enumNameMap = new HashMap<String, EnumLogType>();
	static Map<Integer, EnumLogType> enumValueMap = new HashMap<Integer, EnumLogType>();
	static {
		for (EnumLogType type : EnumLogType.values()) {
			enumNameMap.put(type.toString(), type);
			enumValueMap.put(type.getValue(), type);
		}
	}

	private EnumLogType(Integer value, String description) {
		this.name = name();
		this.value = value;
		this.description = description;
	}

	public static EnumLogType getEnum(String name) {
		return enumNameMap.get(name);
	}

	public static EnumLogType getEnum(Integer value) {
		return enumValueMap.get(value);
	}

	/**
	 * 获取名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取值
	 */
	public Integer getValue() {
		return value;
	}

	/**
	 * 设置值
	 */
	public void setValue(Integer value) {
		this.value = value;
	}

	/**
	 * 获取描述
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 设置描述
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
