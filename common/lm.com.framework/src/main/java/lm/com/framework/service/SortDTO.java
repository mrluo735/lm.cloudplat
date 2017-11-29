package lm.com.framework.service;

/**
 * 排序传输对象
 * 
 * @author mrluo735
 *
 */
public class SortDTO implements java.io.Serializable {
	private static final long serialVersionUID = -1313717882887630404L;

	/**
	 * 属性
	 */
	private String property;

	/**
	 * 升序
	 */
	private boolean asc;

	public SortDTO() {
	}

	public SortDTO(String property, boolean asc) {
		this.property = property;
		this.asc = asc;
	}

	/**
	 * 获取排序属性
	 * 
	 * @return
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * 设置排序属性
	 * 
	 * @param property
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * 获取是否升序
	 * 
	 * @return
	 */
	public boolean getAsc() {
		return this.asc;
	}

	/**
	 * 设置是否升序
	 * 
	 * @param asc
	 */
	public void setAsc(boolean asc) {
		this.asc = asc;
	}
}
