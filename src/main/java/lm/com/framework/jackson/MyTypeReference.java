/**
 * @title MyTypeReference.java
 * @description TODO
 * @package lm.com.framework
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年12月28日上午8:34:08
 * @version v1.0
 */
package lm.com.framework.jackson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * MyTypeReference
 * 
 * @author mrluo735
 *
 */
public class MyTypeReference extends TypeReference<Object> {
	private Type _type;

	public MyTypeReference() {
		Type superClass = getClass().getGenericSuperclass();
		if (superClass instanceof Class<?>) { // sanity check, should never
												// happen
			throw new IllegalArgumentException(
					"Internal error: TypeReference constructed without actual type information");
		}
		/*
		 * 22-Dec-2008, tatu: Not sure if this case is safe -- I suspect it is
		 * possible to make it fail? But let's deal with specific case when we
		 * know an actual use case, and thereby suitable workarounds for valid
		 * case(s) and/or error to throw on invalid one(s).
		 */
		this._type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
	}

	@Override
	public Type getType() {
		return this._type;
	}

	public void setType(Type type) {
		this._type = type;
	}
}
