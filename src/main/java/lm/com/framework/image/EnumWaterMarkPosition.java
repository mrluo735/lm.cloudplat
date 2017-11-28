/**
 * @title EnumWaterMarkMode.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.com.framework.image
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年6月12日上午11:05:20
 * @version v1.0.0
 */
package lm.com.framework.image;

import java.util.HashMap;
import java.util.Map;

import lm.com.framework.enumerate.IBaseEnumInt;

/**
 * 水印位置枚举
 * 
 * @ClassName: EnumWaterMarkMode
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年6月12日 上午11:05:20
 * 
 */
public enum EnumWaterMarkPosition implements IBaseEnumInt<EnumWaterMarkPosition> {
	/**
	 * 左上
	 */
	LeftTop(1, "左上"),
	/**
	 * 左中
	 */
	LeftMiddle(2, "左中"),
	/**
	 * 左下
	 */
	LeftBottom(3, "左下"),
	/**
	 * 中上
	 */
	CenterTop(4, "中上"),
	/**
	 * 中中
	 */
	CenterMiddle(5, "中中"),
	/**
	 * 中下
	 */
	CenterBottom(6, "中下"),
	/**
	 * 右上
	 */
	RightTop(7, "右上"),
	/**
	 * 右中
	 */
	RightMiddle(8, "右中"),
	/**
	 * 右下
	 */
	RightBottom(9, "右下");

	private String name;
	private Integer value;
	private String description;

	static Map<String, EnumWaterMarkPosition> enumNameMap = new HashMap<String, EnumWaterMarkPosition>();
	static Map<Integer, EnumWaterMarkPosition> enumValueMap = new HashMap<Integer, EnumWaterMarkPosition>();
	static {
		for (EnumWaterMarkPosition type : EnumWaterMarkPosition.values()) {
			enumNameMap.put(type.toString(), type);
			enumValueMap.put(type.getValue(), type);
		}
	}

	private EnumWaterMarkPosition(Integer value, String description) {
		this.value = value;
		this.description = description;
	}

	public static EnumWaterMarkPosition getEnum(String name) {
		return enumNameMap.get(name);
	}

	public static EnumWaterMarkPosition getEnum(Integer value) {
		return enumValueMap.get(value);
	}

	/**
	 * 获取名称
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * 设置名称
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取值
	 */
	@Override
	public Integer getValue() {
		return this.value;
	}

	/**
	 * 设置值
	 */
	@Override
	public void setValue(Integer value) {
		this.value = value;
	}

	/**
	 * 获取描述
	 */
	@Override
	public String getDescription() {
		return this.description;
	}

	/**
	 * 设置描述
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}
}
