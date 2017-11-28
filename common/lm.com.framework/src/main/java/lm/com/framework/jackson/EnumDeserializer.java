/**
 * @title TypeSerializer.java
 * @description TODO
 * @package lm.pro.schedule.model.enumerate
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年11月24日上午11:46:51
 * @version v1.0
 */
package lm.com.framework.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import lm.com.framework.IntegerUtil;
import lm.com.framework.ReflectUtil;
import lm.com.framework.enumerate.IBaseEnumInt;

/**
 * 枚举反序列化
 * 
 * @author mrluo735
 *
 */
public class EnumDeserializer<E extends IBaseEnumInt<?>> extends JsonDeserializer<E> {
	/**
	 * 反序列化
	 */
	@Override
	@SuppressWarnings("unchecked")
	public E deserialize(final JsonParser p, final DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		Integer value = IntegerUtil.tryParse(p.getText(), 0);
		Object obj = p.getCurrentValue();
		String fieldName = p.getCurrentName();
		Class<E> clazz = (Class<E>) ReflectUtil.getFieldClass(obj, fieldName);
		E[] enums = clazz.getEnumConstants();
		for (E e : enums) {
			if (e.getValue().equals(value)) {
				return e;
			}
		}

		// ObjectMapper mapper = new ObjectMapper();
		// SimpleModule module = new SimpleModule();
		// module.addDeserializer(Item.class, new EnumDeserializer());
		// mapper.registerModule(module);
		return null;
	}
}
