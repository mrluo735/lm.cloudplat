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

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import lm.com.framework.enumerate.IBaseEnumInt;

/**
 * 枚举序列化
 * 
 * @author mrluo735
 *
 */
public class EnumSerializer<E extends IBaseEnumInt<?>> extends JsonSerializer<E> {
	@Override
	public void serialize(final E enumValue, final JsonGenerator gen, final SerializerProvider serializer)
			throws IOException, JsonProcessingException {
		gen.writeNumber(enumValue.getValue());
	}
}
