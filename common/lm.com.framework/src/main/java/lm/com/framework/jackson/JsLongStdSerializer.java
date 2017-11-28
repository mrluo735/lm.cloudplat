/**
 * @title JsLongSerializer.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.com.framework.jackson
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年6月16日下午3:33:13
 * @version v1.0.0
 */
package lm.com.framework.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * @ClassName: JsNumberSerializer
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年6月16日 下午3:33:13
 * 
 */
public class JsLongStdSerializer extends StdSerializer<Long> {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5026340494462848339L;

	/**
	 * @param t
	 */
	public JsLongStdSerializer() {
		super(Long.class);
	}

	/**
	 * 
	 * @param value
	 * @param gen
	 * @param provider
	 * @throws IOException
	 */
	@Override
	public void serialize(Long value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		if (value == null) {
			gen.writeObject(null);
			return;
		}
		// javascript中数字的范围是:-9007199254740992 < x < +9007199254740992
		if (value > -9007199254740992L && value < 9007199254740992L) {
			gen.writeNumber(value);
			return;
		}
		gen.writeString(value.toString());
	}
}
