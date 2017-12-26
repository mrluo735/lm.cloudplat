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
 * @ClassName: EnumClassType
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年5月2日 上午11:37:39
 * 
 */
public enum EnumClassType implements IBaseEnumInt<EnumClassType> {
	/**
	 * 普通类
	 */
	Common(1, "普通类"),
	/**
	 * 控制器
	 */
	Controller(2, "控制器"),
	/**
	 * Rest控制器
	 */
	RestController(3, "Rest控制器");

	private String name;
	private Integer value;
	private String description;

	static Map<String, EnumClassType> enumNameMap = new HashMap<String, EnumClassType>();
	static Map<Integer, EnumClassType> enumValueMap = new HashMap<Integer, EnumClassType>();
	static {
		for (EnumClassType type : EnumClassType.values()) {
			enumNameMap.put(type.toString(), type);
			enumValueMap.put(type.getValue(), type);
		}
	}

	private EnumClassType(Integer value, String description) {
		this.value = value;
		this.description = description;
	}

	public static EnumClassType getEnum(String name) {
		return enumNameMap.get(name);
	}

	public static EnumClassType getEnum(Integer value) {
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
