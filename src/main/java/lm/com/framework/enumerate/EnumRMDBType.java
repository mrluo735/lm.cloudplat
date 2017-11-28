package lm.com.framework.enumerate;

import java.util.HashMap;
import java.util.Map;

/**
 * 关系型数据库枚举
 * 
 * @author mrluo735
 *
 */
public enum EnumRMDBType implements IBaseEnumInt<EnumRMDBType> {
	/**
	 * Microsoft Access
	 */
	Access(1, "Microsoft Access"),
	/**
	 * Microsoft SqlServer
	 */
	SqlServer(2, "Microsoft SqlServer"),
	/**
	 * Oracle Mysql
	 */
	Mysql(3, "Oracle Mysql"),
	/**
	 * Oracle
	 */
	Oracle(4, "Oracle"),
	/**
	 * Postgresql
	 */
	Postgresql(5, "Postgresql"),
	/**
	 * Db2
	 */
	Db2(6, "Db2"),
	/**
	 * Sybase
	 */
	Sybase(7, "Sybase");

	private String name;
	private Integer value;
	private String description;

	static Map<String, EnumRMDBType> enumNameMap = new HashMap<String, EnumRMDBType>();
	static Map<Integer, EnumRMDBType> enumValueMap = new HashMap<Integer, EnumRMDBType>();
	static {
		for (EnumRMDBType type : EnumRMDBType.values()) {
			enumNameMap.put(type.toString(), type);
			enumValueMap.put(type.getValue(), type);
		}
	}

	private EnumRMDBType(Integer value, String description) {
		this.value = value;
		this.description = description;
	}

	public static EnumRMDBType getEnum(String name) {
		return enumNameMap.get(name);
	}

	public static EnumRMDBType getEnum(Integer value) {
		return enumValueMap.get(value);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
