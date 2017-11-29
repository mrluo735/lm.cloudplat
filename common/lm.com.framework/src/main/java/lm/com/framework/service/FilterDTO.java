package lm.com.framework.service;

/**
 * 过滤传输对象
 * 
 * @author mrluo735
 *
 */
public class FilterDTO implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6128456296174815834L;

	private String property;

	private String operation;

	private Object value;

	private String join = "AND";

	/**
	 * 重载+1 构造函数
	 */
	public FilterDTO() {
	}

	/**
	 * 重载+2 构造函数
	 * 
	 * @param property
	 * @param value
	 */
	public FilterDTO(String property, Object value) {
		this.property = property;
		this.value = value;
		this.operation = "eq";
	}

	/**
	 * 重载+3 构造函数
	 * 
	 * @param property
	 * @param value
	 * @param operation
	 */
	public FilterDTO(String property, String operation, Object value) {
		this.property = property;
		this.operation = operation;
		this.value = value;
	}

	/**
	 * 获取属性
	 * 
	 * @return
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * 设置属性
	 * 
	 * @param property
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * 获取操作
	 * 
	 * @return
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * 设置操作
	 * 
	 * @param operation
	 */
	public void setOperation(String operation) {
		this.operation = operation;
	}

	/**
	 * 获取值
	 * 
	 * @return
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * 设置值
	 * 
	 * @param value
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * 获取连接符
	 * 
	 * @return
	 */
	public String getJoin() {
		return join;
	}

	/**
	 * 设置连接符
	 * 
	 * @param join
	 */
	public void setJoin(String join) {
		this.join = join;
	}
}
