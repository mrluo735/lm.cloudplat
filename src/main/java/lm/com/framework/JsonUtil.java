package lm.com.framework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Json序列化帮助类
 * 
 * @author mrluo735
 *
 */
public final class JsonUtil {
	private static final ObjectMapper objectMapper = new ObjectMapper();
	private static final Gson gson = new Gson();
	static {
		// 允许键没有引号
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
	}

	// region ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ Jackson ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	/**
	 * 对象转成json字符串
	 * 
	 * @param object
	 * @return
	 */
	public static String toJsonUseJackson(Object object) {
		try {
			objectMapper.disable(SerializationFeature.WRITE_ENUMS_USING_INDEX);
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException ex) {
			return "";
		}
	}

	/**
	 * 对象转成json字符串
	 * 
	 * @param object
	 * @param isOrdinal
	 * @return
	 */
	public static String toJsonUseJackson(Object object, boolean isOrdinal) {
		try {
			objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_INDEX, isOrdinal);
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException ex) {
			return "";
		}
	}

	/**
	 * json串转成泛型对象
	 * 
	 * @param text
	 * @param clazz
	 * @return
	 */
	public static <T> T toObjectUseJackson(String text, Class<T> clazz) {
		try {
			return objectMapper.readValue(text, clazz);
		} catch (JsonParseException e) {
		} catch (JsonMappingException e) {
		} catch (IOException e) {
		}
		return null;
	}

	/**
	 * json串转成泛型对象
	 * 
	 * @param text
	 * @param clazz
	 * @return
	 */
	public static <T> List<T> toListUseJackson(String text, Class<T> clazz) {
		JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, clazz);
		try {
			return objectMapper.readValue(text, javaType);
		} catch (JsonParseException e) {
		} catch (JsonMappingException e) {
		} catch (IOException e) {
		}
		return null;
	}

	/**
	 * json串转成泛型对象
	 * 
	 * @param text
	 * @param valueTypeRef
	 * @return
	 */
	public static <T> T toObjectUseJackson(String text, TypeReference<T> valueTypeRef) {
		try {
			return objectMapper.readValue(text, valueTypeRef);
		} catch (JsonParseException e) {
		} catch (JsonMappingException e) {
		} catch (IOException e) {
		}
		return null;
	}
	// endregion ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ Jackson ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

	// region ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ Fastjson ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	/**
	 * 对象转成json字符串
	 * 
	 * @param object
	 * @return
	 */
	public static String toJsonUseFastJson(Object object) {
		try {
			return JSON.toJSONString(object);
		} catch (Exception ex) {
			return "";
		}
	}

	/**
	 * json串转成对象
	 * 
	 * @param text
	 * @return
	 */
	public static Object toObjectUseFastJson(String text) {
		return JSON.parse(text);
	}

	/**
	 * json串转成泛型对象
	 * 
	 * @param text
	 * @param clazz
	 * @return
	 */
	public static <T> T toObjectUseFastJson(String text, Class<T> clazz) {
		return JSON.parseObject(text, clazz);
	}
	// endregion ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ Fastjson ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

	// region ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ Gson ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	/**
	 * 对象转成json字符串
	 * 
	 * @param object
	 * @return
	 */
	public static String toJsonUseGson(Object object) {
		try {
			return gson.toJson(object);
		} catch (Exception ex) {
			return "";
		}
	}

	/**
	 * json字符串转成对象
	 * 
	 * @param text
	 * @param clazz
	 * @return
	 */
	public static <T> T toObjectUseGson(String text, Class<T> clazz) {
		return gson.fromJson(text, clazz);
	}

	/**
	 * json字符串转成对象
	 * 
	 * @param text
	 * @param typeToken
	 * @return
	 */
	public static <T> T toObjectUseGson(String text, TypeToken<T> typeToken) {
		return gson.fromJson(text, typeToken.getType());
	}
	// endregion ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ Gson ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
}
