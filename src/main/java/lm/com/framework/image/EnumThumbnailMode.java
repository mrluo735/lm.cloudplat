/**
 * @title EnumThumbnailMode.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.com.framework.image
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年6月9日下午4:13:30
 * @version v1.0.0
 */
package lm.com.framework.image;

import java.util.HashMap;
import java.util.Map;

import lm.com.framework.enumerate.IBaseEnumInt;

/**
 * 缩略方式枚举
 * 
 * @ClassName: EnumThumbnailMode
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年6月9日 下午4:13:30
 * 
 */
public enum EnumThumbnailMode implements IBaseEnumInt<EnumThumbnailMode> {
	/**
	 * 指定宽高缩放（不变形，可能填白）
	 */
	WidthAndHeight(1, "指定宽高缩放"),
	/**
	 * 指定宽，高按比例（不变形，可能填白）
	 */
	AccordingWidth(2, "指定宽，高按比例"),
	/**
	 * 指定高，宽按比例（不变形，可能填白）
	 */
	AccordingHeight(3, "指定高，宽按比例"),
	/**
	 * 指定宽高裁减（不变形，可能填白）
	 */
	CutImage(4, "指定宽高裁减 "),
	/**
	 * 宽高自适应（不变形，可能填白）
	 */
	SelfAdapting(5, "宽高自适应"),
	/**
	 * 等宽高，内部自适应（可能变形）
	 */
	FixWidthHeightInnerSacle(6, "等宽高，内部自适应");

	private String name;
	private Integer value;
	private String description;

	static Map<String, EnumThumbnailMode> enumNameMap = new HashMap<String, EnumThumbnailMode>();
	static Map<Integer, EnumThumbnailMode> enumValueMap = new HashMap<Integer, EnumThumbnailMode>();
	static {
		for (EnumThumbnailMode type : EnumThumbnailMode.values()) {
			enumNameMap.put(type.toString(), type);
			enumValueMap.put(type.getValue(), type);
		}
	}

	private EnumThumbnailMode(Integer value, String description) {
		this.value = value;
		this.description = description;
	}

	public static EnumThumbnailMode getEnum(String name) {
		return enumNameMap.get(name);
	}

	public static EnumThumbnailMode getEnum(Integer value) {
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
