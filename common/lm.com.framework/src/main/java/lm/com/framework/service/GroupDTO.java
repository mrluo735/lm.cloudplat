/**
 * @title Groups.java
 * @description TODO
 * @package lm.com.service
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年12月26日下午6:13:24
 * @version v1.0
 */
package lm.com.framework.service;

import java.util.ArrayList;
import java.util.List;

/**
 * 组传输对象
 * 
 * @author mrluo735
 *
 */
public class GroupDTO implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 473809257285902336L;

	private Integer order = 1;

	private String join = "AND";

	private List<FilterDTO> filters = new ArrayList<>();

	/**
	 * 获取排序
	 * 
	 * @return
	 */
	public Integer getOrder() {
		return order;
	}

	/**
	 * 设置排序
	 * 
	 * @param order
	 */
	public void setOrder(Integer order) {
		this.order = order;
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

	/**
	 * 获取过滤集合
	 * 
	 * @return
	 */
	public List<FilterDTO> getFilters() {
		return filters;
	}

	/**
	 * 设置过滤集合
	 * 
	 * @param filters
	 */
	public void setFilters(List<FilterDTO> filters) {
		this.filters = filters;
	}
}
