/**
 * @title ServiceUtil.java
 * @description TODO
 * @package lm.com.framework.service
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年12月27日下午2:31:39
 * @version v1.0
 */
package lm.com.framework.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lm.com.framework.ObjectUtil;
import lm.com.framework.RMDBUtil;
import lm.com.framework.StringUtil;
import lm.com.framework.sqlmedium.FilterExpression;
import lm.com.framework.sqlmedium.Pager;
import lm.com.framework.sqlmedium.SqlKey;

/**
 * 
 * @author mrluo735
 *
 */
public class ServiceUtil {
	/**
	 * 重载+1 转成PagerMap
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, Object> toPagerMap(RequestDTO request) {
		return toPagerMap(request, null);
	}

	/**
	 * 重载+2 转成PagerMap
	 * 
	 * @param request
	 * @param driverName
	 * @return
	 */
	public static Map<String, Object> toPagerMap(RequestDTO request, String driverName) {
		Map<String, Object> pagerMap = new HashMap<String, Object>();
		pagerMap.put(Pager.PAGEINDEX, request.getInteger(Pager.PAGEINDEX, 1));
		pagerMap.put(Pager.PAGESIZE, request.getInteger(Pager.PAGESIZE, 20));
		pagerMap.put(Pager.ORDERBY, ServiceUtil.toSort(request.getSorts(), driverName));
		for (Entry<String, Object> item : request.getData().entrySet()) {
			if (Pager.COLUMNPATTEN.equalsIgnoreCase(item.getKey().toString())
					|| Pager.PAGEINDEX.equalsIgnoreCase(item.getKey().toString())
					|| Pager.PAGESIZE.equalsIgnoreCase(item.getKey().toString())
					|| Pager.ISSTATCOUNT.equalsIgnoreCase(item.getKey().toString()))
				continue;
			pagerMap.put(item.getKey().toString(), item.getValue());
		}

		if (request.containsKey(Pager.COLUMNPATTEN))
			pagerMap.put(Pager.COLUMNPATTEN, request.getString(Pager.COLUMNPATTEN));
		if (request.containsKey(Pager.ISSTATCOUNT))
			pagerMap.put(Pager.ISSTATCOUNT, request.getBoolean(Pager.ISSTATCOUNT, true));
		return pagerMap;
	}

	/**
	 * 重载+1 转成Pager对象
	 * 
	 * @param request
	 * @return
	 */
	public static Pager toPager(RequestDTO request) {
		return toPager(request, null);
	}

	/**
	 * 重载+2 转成Pager对象
	 * 
	 * @param request
	 * @param driverName
	 * @return
	 */
	public static Pager toPager(RequestDTO request, String driverName) {
		Pager pager = new Pager();
		pager.setPageIndex(request.getInteger(Pager.PAGEINDEX, 1));
		pager.setPageSize(request.getInteger(Pager.PAGESIZE, 20));
		pager.setOrderBy(ServiceUtil.toSort(request.getSorts(), driverName));
		for (Entry<String, Object> item : request.getData().entrySet()) {
			if (Pager.COLUMNPATTEN.equalsIgnoreCase(item.getKey().toString())
					|| Pager.PAGEINDEX.equalsIgnoreCase(item.getKey().toString())
					|| Pager.PAGESIZE.equalsIgnoreCase(item.getKey().toString())
					|| Pager.ISSTATCOUNT.equalsIgnoreCase(item.getKey().toString()))
				continue;
			pager.setWhereMap(item.getKey().toString(), item.getValue());
		}
		// pager.setWhere(ServiceUtil.toWhere(request.getGroups(),
		// request.getFilters(), driverName));

		if (request.containsKey(Pager.COLUMNPATTEN))
			pager.setColumnPattern(request.getString(Pager.COLUMNPATTEN));
		if (request.containsKey(Pager.ISSTATCOUNT))
			pager.setIsStatCount(request.getBoolean(Pager.ISSTATCOUNT, true));
		return pager;
	}

	/**
	 * 重载+1 转成排序字符串
	 * 
	 * @param sorts
	 * @param driverName
	 * @return
	 */
	public static String toSort(List<SortDTO> sorts) {
		return toSort(sorts, null);
	}

	/**
	 * 重载+2 转成排序字符串
	 * 
	 * @param sorts
	 * @param driverName
	 * @return
	 */
	public static String toSort(List<SortDTO> sorts, String driverName) {
		List<String> sortList = new ArrayList<>();
		boolean escapeColumn = !StringUtil.isNullOrWhiteSpace(driverName);
		for (SortDTO sort : sorts) {
			String property = sort.getProperty();
			if (escapeColumn) {
				if (property.indexOf(".") > -1) {
					String[] propertys = property.split("\\.");
					property = propertys[0].trim() + "."
							+ RMDBUtil.escapeColumn(propertys[1].trim(), driverName).trim();
				} else
					property = RMDBUtil.escapeColumn(sort.getProperty(), driverName);
			}
			if (sort.getAsc())
				sortList.add(String.format("%s %s", property, SqlKey.ASC));
			else
				sortList.add(String.format("%s %s", property, SqlKey.DESC));
		}
		return StringUtil.join(", ", sortList);
	}

	/**
	 * 转成where条件
	 * 
	 * @param driverName
	 * @return
	 */
	public static String toWhere(List<GroupDTO> groups, List<FilterDTO> filters, String driverName) {
		FilterExpression filterExpression = FilterExpression.getInstance();
		if (null != groups && groups.size() > 0) {
			int i = 0;
			for (GroupDTO group : groups) {
				filterExpression = filterExpression.beginGroup();

				filterExpression = parseFilter(filterExpression, group.getFilters(), driverName);

				filterExpression.endGroup();
				if (i > 0) {
					if (null == group.getJoin() || group.getJoin().trim().length() < 1
							|| group.getJoin().trim().equalsIgnoreCase("AND"))
						filterExpression = filterExpression.and();
					else
						filterExpression = filterExpression.or();
				}
				i++;
			}
			return filterExpression.toWhere();
		}
		if (null != filters && filters.size() > 0) {
			filterExpression = parseFilter(filterExpression, filters, driverName);
			return filterExpression.toWhere();
		}
		return "";
	}

	/**
	 * 解析过滤条件
	 * 
	 * @param filterExpression
	 * @param filters
	 * @param driverName
	 * @return
	 */
	private static FilterExpression parseFilter(FilterExpression filterExpression, List<FilterDTO> filters,
			String driverName) {
		int i = 0;
		boolean needJoin = false;
		for (FilterDTO filter : filters) {
			String property = filter.getProperty();
			if (property.indexOf(".") > -1) {
				String[] propertys = property.split("\\.");
				property = propertys[0].trim() + "." + RMDBUtil.escapeColumn(propertys[1].trim(), driverName).trim();
			} else
				property = RMDBUtil.escapeColumn(filter.getProperty(), driverName);

			switch (filter.getOperation().toLowerCase()) {
				case "like":
					if (!ObjectUtil.isNullOrWhiteSpace(filter.getValue())) {
						filterExpression = filterExpression.like(property, filter.getValue().toString());
						needJoin = true;
					}
					break;
				case "likeleft":
					if (!ObjectUtil.isNullOrWhiteSpace(filter.getValue())) {
						filterExpression = filterExpression.likeLeft(property, filter.getValue().toString());
						needJoin = true;
					}
					break;
				case "likeright":
					if (!ObjectUtil.isNullOrWhiteSpace(filter.getValue())) {
						filterExpression = filterExpression.likeRight(property, filter.getValue().toString());
						needJoin = true;
					}
					break;
				case "eq":
					if (!ObjectUtil.isNullOrWhiteSpace(filter.getValue())) {
						filterExpression = filterExpression.eq(property, filter.getValue());
						needJoin = true;
					}
					break;
				case "ne":
					if (!ObjectUtil.isNullOrWhiteSpace(filter.getValue())) {
						filterExpression = filterExpression.ne(property, filter.getValue());
						needJoin = true;
					}
					break;
				case "gt":
					if (!ObjectUtil.isNullOrWhiteSpace(filter.getValue())) {
						filterExpression = filterExpression.gt(property, filter.getValue());
						needJoin = true;
					}
					break;
				case "ge":
					if (!ObjectUtil.isNullOrWhiteSpace(filter.getValue())) {
						filterExpression = filterExpression.ge(property, filter.getValue());
						needJoin = true;
					}
					break;
				case "lt":
					if (!ObjectUtil.isNullOrWhiteSpace(filter.getValue())) {
						filterExpression = filterExpression.lt(property, filter.getValue());
						needJoin = true;
					}
					break;
				case "le":
					if (!ObjectUtil.isNullOrWhiteSpace(filter.getValue())) {
						filterExpression = filterExpression.le(property, filter.getValue());
						needJoin = true;
					}
					break;
				case "isnull":
					filterExpression = filterExpression.isNull(property);
					needJoin = true;
					break;
				case "isnotnull":
					filterExpression = filterExpression.isNotNull(property);
					needJoin = true;
					break;
				case "in":
					if (!ObjectUtil.isNullOrWhiteSpace(filter.getValue())) {
						filterExpression = filterExpression.in(property, filter.getValue().toString());
						needJoin = true;
					}
					break;
				case "notin":
					if (!ObjectUtil.isNullOrWhiteSpace(filter.getValue())) {
						filterExpression = filterExpression.notIn(property, filter.getValue().toString());
						needJoin = true;
					}
					break;
				case "gtreverse":
					if (!ObjectUtil.isNullOrWhiteSpace(filter.getValue())) {
						filterExpression = filterExpression.gtReverse(property, filter.getValue());
						needJoin = true;
					}
					break;
				case "gereverse":
					if (!ObjectUtil.isNullOrWhiteSpace(filter.getValue())) {
						filterExpression = filterExpression.geReverse(property, filter.getValue());
						needJoin = true;
					}
					break;
				case "ltreverse":
					if (!ObjectUtil.isNullOrWhiteSpace(filter.getValue())) {
						filterExpression = filterExpression.ltReverse(property, filter.getValue());
						needJoin = true;
					}
					break;
				case "lereverse":
					if (!ObjectUtil.isNullOrWhiteSpace(filter.getValue())) {
						filterExpression = filterExpression.leReverse(property, filter.getValue());
						needJoin = true;
					}
					break;
			}

			if (needJoin && (i < filters.size() - 1)) {// 最后一条不需要连接符
				if (null == filter.getJoin() || filter.getJoin().trim().length() < 1
						|| filter.getJoin().trim().equalsIgnoreCase("AND"))
					filterExpression = filterExpression.and();
				else
					filterExpression = filterExpression.or();
			}
			needJoin = false;
			i++;
		}
		return filterExpression;
	}
}
