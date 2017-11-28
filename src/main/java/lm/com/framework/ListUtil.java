package lm.com.framework;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * List 工具类
 * 
 * @author mrluo735
 *
 */
public class ListUtil {
	/**
	 * 判断是否为空.
	 */
	public static boolean isEmpty(List<?> list) {
		return (list == null) || list.isEmpty();
	}

	/**
	 * 判断是否不为空.
	 */
	public static boolean isNotEmpty(List<?> list) {
		return (list != null) && !(list.isEmpty());
	}
	
	/**
	 * 排序
	 * 
	 * @param list
	 * @param property
	 * @param asc
	 */
	@SuppressWarnings({ "all" })
	public static <T> void sort(List<T> list, final String fieldName, final boolean asc) {
		Collections.sort(list, new Comparator() {

			public int compare(Object a, Object b) {
				int ret = 0;
				try {
					Field field = ReflectUtil.getFieldByFieldName(a, fieldName);
					Class<?> type = field.getType();
					
					String getter = "get".concat(StringUtil.capitalize(fieldName));
					if(type == Boolean.class || type == boolean.class)
						getter = "is".concat(StringUtil.capitalize(fieldName));
					
					Method m1 = ((T) a).getClass().getMethod(getter, null);
					Method m2 = ((T) b).getClass().getMethod(getter, null);
					String pValue1 = m1.invoke(((T) a), null).toString();
					String pValue2 = m2.invoke(((T) b), null).toString();
					if (asc)// 正序
						ret = pValue1.compareTo(pValue2);
					else// 倒序
						ret = pValue2.compareTo(pValue1);

					if (type == Byte.class || type == Short.class || type == Integer.class || type == Long.class
							|| type == Float.class || type == Double.class || type == byte.class || type == short.class
							|| type == int.class || type == long.class || type == float.class || type == double.class) {
						Double dValue1 = Double.valueOf(pValue1);
						Double dValue2 = Double.valueOf(pValue2);
						if (asc)// 正序
							ret = dValue1.compareTo(dValue2);
						else// 倒序
							ret = dValue2.compareTo(dValue1);
					}

				} catch (NoSuchMethodException ne) {
					System.out.println(ne);
				} catch (IllegalAccessException ie) {
					System.out.println(ie);
				} catch (InvocationTargetException it) {
					System.out.println(it);
				}
				return ret;
			}
		});
	}
}
