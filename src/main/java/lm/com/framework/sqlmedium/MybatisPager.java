/**
 * @title MybatisPager.java
 * @description TODO
 * @package lm.com.framework.sqlmedium
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年11月23日下午6:18:01
 * @version v1.0
 */
package lm.com.framework.sqlmedium;

import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;

import lm.com.framework.mybatis.MybatisUtil;

/**
 * mybatis分页
 * 
 * @author mrluo735
 *
 */
public class MybatisPager extends Pager {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2831788358584342174L;

	private String tableName = "";

	/**
	 * 重载+1 构造函数
	 */
	public MybatisPager() {
	}

	/**
	 * 构造函数
	 * 
	 * @param pager
	 */
	public MybatisPager(Pager pager) {
		this.setColumnPattern(pager.getColumnPattern());
		this.setWhere(pager.getWhere());
		this.setWhereMap(pager.getWhereMap());
		this.setOrderBy(pager.getOrderBy());
		this.setPageIndex(pager.getPageIndex());
		this.setPageSize(pager.getPageSize());
		this.setIsStatCount(pager.getIsStatCount());
	}

	/**
	 * 获取表名
	 * 
	 * @param sqlSessionFactory
	 * @param mapperClassName
	 * @return
	 */
	public String getTableName(final DefaultSqlSessionFactory sqlSessionFactory, String mapperClassName) {
		this.tableName = MybatisUtil.getTableName(sqlSessionFactory, mapperClassName);
		return this.tableName;
	}
}
