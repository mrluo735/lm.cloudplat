/**
 * @title EnumDeserializer.java
 * @description TODO
 * @package lm.com.framework.fastjson
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年12月21日上午9:03:00
 * @version v1.0
 */
package lm.com.framework.fastjson;

import java.lang.reflect.Type;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;

import lm.com.framework.ReflectUtil;
import lm.com.framework.enumerate.IBaseEnumInt;

/**
 * 枚举反序列化
 * 
 * @author mrluo735
 *
 */
public class EnumDeserializer<E extends IBaseEnumInt<?>> implements ObjectDeserializer {
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public E deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
		JSONLexer lexer = parser.getLexer();
		int value = lexer.intValue();

		Class<E> clazz = (Class<E>) ReflectUtil.getFieldClass(fieldName, fieldName.toString());// TODO待调试修改
		E[] enums = clazz.getEnumConstants();
		for (E e : enums) {
			if (e.getValue().equals(value)) {
				return e;
			}
		}
		return null;
	}

	public int getFastMatchToken() {
		// TODO Auto-generated method stub
		return 0;
	}
}
