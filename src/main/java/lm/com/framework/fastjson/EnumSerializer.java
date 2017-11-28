/**
 * @title EnumSerializer.java
 * @description TODO
 * @package lm.com.framework.fastjson
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年12月21日上午9:03:21
 * @version v1.0
 */
package lm.com.framework.fastjson;

import java.io.IOException;
import java.lang.reflect.Type;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

import lm.com.framework.enumerate.IBaseEnumInt;

/**
 * 枚举序列化
 * 
 * @author mrluo735
 *
 */
public class EnumSerializer<E extends IBaseEnumInt<?>> implements ObjectSerializer {
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
		E e = (E) object;
		SerializeWriter out = serializer.getWriter();
		if (object == null) {
			serializer.getWriter().writeNull();
			return;
		}
		out.write(e.getValue());
	}
}
