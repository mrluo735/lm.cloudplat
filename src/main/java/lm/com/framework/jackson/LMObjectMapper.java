/**
 * @title LMObjectMapper.java
 * @description TODO
 * @package lm.com.framework.jackson
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年11月24日下午5:46:27
 * @version v1.0
 */
package lm.com.framework.jackson;

import java.io.IOException;
import java.text.SimpleDateFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * @author mrluo735
 *
 */
public class LMObjectMapper extends ObjectMapper {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8791291097243382919L;

	public LMObjectMapper() {
		super();
		// 设置null转换""
		getSerializerProvider().setNullValueSerializer(new NullSerializer());
		// 设置日期转换yyyy-MM-dd HH:mm:ss
		setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
	}

	// null的JSON序列
	private class NullSerializer extends JsonSerializer<Object> {
		public void serialize(Object value, JsonGenerator jgen, SerializerProvider provider)
				throws IOException, JsonProcessingException {
			jgen.writeString("");
		}
	}
}
