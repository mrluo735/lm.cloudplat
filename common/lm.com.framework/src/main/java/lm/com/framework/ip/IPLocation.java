/**
 * @title IPLocation.java
 * @description TODO
 * @package lm.com.framework.ip
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年10月11日下午6:52:02
 * @version v1.0
 */
package lm.com.framework.ip;

import java.io.Serializable;

/**
 * @author mrluo735
 *
 */
public class IPLocation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3068706420230919646L;
	
	private String area = "";
	private String operator = "";

	public IPLocation() {
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
		// 如果为局域网，纯真IP地址库的地区会显示CZ88.NET,这里把它去掉
		if (this.operator.trim().equalsIgnoreCase("CZ88.NET")) {
			this.operator = "本机或本网络";
		} else {
			this.operator = operator;
		}
	}

	/**
	 * 复制
	 * 
	 * @return
	 */
	public IPLocation clone() {
		IPLocation ret = new IPLocation();
		ret.area = area;
		ret.operator = operator;
		return ret;
	}
}
