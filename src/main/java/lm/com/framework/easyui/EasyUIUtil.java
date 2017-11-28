package lm.com.framework.easyui;

import java.util.ArrayList;
import java.util.List;

import lm.com.framework.StringUtil;

/**
 * EasyUI 帮助类
 * 
 * @author Administrator
 *
 */
public final class EasyUIUtil {
	/**
	 * 排序转换成字符串
	 * 
	 * @param sort
	 * @param order
	 * @return
	 */
	public static String toSortString(String sort, String order) {
		sort = StringUtil.toNonNull(sort);
		order = StringUtil.toNonNull(order);
		String[] sorts = sort.split(",");
		String[] orders = order.split(",");
		if (null == sorts || null == orders || sorts.length != orders.length)
			return "";

		List<String> list = new ArrayList<String>();
		for (int i = 0; i < sorts.length; i++) {
			list.add(String.format("%s %s", sorts[i], orders[i].toUpperCase()));
		}
		return StringUtil.join(",", list);
	}
}
