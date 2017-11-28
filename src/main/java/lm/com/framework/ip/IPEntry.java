/**
 * @title IPEntry.java
 * @description TODO
 * @package lm.com.framework.ip
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年10月11日下午6:57:52
 * @version v1.0
 */
package lm.com.framework.ip;

import java.io.Serializable;

/**
 * 一条IP范围记录，不仅包括国家和区域，也包括起始IP和结束IP
 */
public class IPEntry implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2573909301881533830L;
	
	private String beginIP = "";
	private String endIP = "";
	private String area = "";
	private String operator = "";

	/**
	 * 构造函数
	 */
	public IPEntry() {
	}

	/**
	 * 获取起始IP
	 * 
	 * @return
	 */
	public String getBeginIP() {
		return beginIP;
	}

	/**
	 * 设置起始IP
	 * 
	 * @param beginIP
	 */
	public void setBeginIP(String beginIP) {
		this.beginIP = beginIP;
	}

	/**
	 * 获取结束IP
	 * 
	 * @return
	 */
	public String getEndIP() {
		return endIP;
	}

	/**
	 * 设置结束IP
	 * 
	 * @param endIP
	 */
	public void setEndIP(String endIP) {
		this.endIP = endIP;
	}

	/**
	 * 获取地区
	 * 
	 * @return
	 */
	public String getArea() {
		return area;
	}

	/**
	 * 设置地区
	 * 
	 * @param area
	 */
	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * 获取运营商
	 * 
	 * @return
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * 设置运营商
	 * 
	 * @param operator
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}
}
