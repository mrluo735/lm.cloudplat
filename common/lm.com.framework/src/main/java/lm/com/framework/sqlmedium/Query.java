package lm.com.framework.sqlmedium;

import java.util.ArrayList;
import java.util.List;

import lm.com.framework.StringUtil;

public class Query {
	/**
	 * order by 关键字常量
	 */
	protected final String ORDERBY = " ORDER BY ";
	/**
	 * where 关键字常量
	 */
	protected final String WHERE = " WHERE ";

	private String orderBy = "";

	/**
	 * 获取排序
	 * 
	 * @return
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * 设置排序
	 * 
	 * @param orderby
	 */
	public void setOrderby(String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * 转成where字符串
	 * 
	 * @return
	 */
	public String toWhere() {
		List<String> list = new ArrayList<String>();
		// todo...

		if (list.size() < 1)
			return "";
		return StringUtil.join(" AND ", list);
	}
}
