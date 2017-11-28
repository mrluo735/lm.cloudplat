/**
 * @title JsonUtilTest.java
 * @description TODO
 * @package lm.com.framework
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年12月27日下午3:17:48
 * @version v1.0
 */
package lm.com.framework;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import lm.com.framework.jackson.MyTypeReference;
import lm.com.framework.map.KeyValuePair;

/**
 * @author Administrator
 *
 */
public class JsonUtilTest {
	public static class Animal implements java.io.Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 3378489087875450714L;
		private int id;
		private String name;
		private String type;

		public Animal() {

		}

		public Animal(int id, String name, String type) {
			this.id = id;
			this.name = name;
			this.type = type;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

	}

	@Test
	public void test1() throws Exception {
		Object sso = JsonUtil.toObjectUseJackson("", Object.class);
		sso = JsonUtil.toObjectUseFastJson("");
		
		Animal oo = new Animal();
		oo.setId(1);
		oo.setName("狗");
		oo.setType("狗");

		List<Animal> list = new ArrayList<>();
		list.add(new Animal(1, "狗", "狗"));
		list.add(new Animal(2, "猫", "猫"));
		KeyValuePair<Long, List<Animal>> kvp = new KeyValuePair<>(2L, list);
		KeyValuePair<Long, List<Animal>> tt = new KeyValuePair<Long, List<Animal>>(){};
		Class<?> clazz = tt.getClass();
		System.out.println(clazz.getGenericSuperclass());
		System.out.println(new KeyValuePair<Long, List<Animal>>(){}.getClass().getGenericSuperclass());
//		Type type = clazz.getGenericSuperclass();
//		ParameterizedType pType = (ParameterizedType)type;
//		Class c = (Class) pType.getActualTypeArguments()[0];  
//		System.out.println(c);
		
//		String jjs = JsonUtil.toJsonUseJackson(list);
//		List<Animal> oo2 = JsonUtil.toObjectUseJackson(jjs, List.class);

		String json = JsonUtil.toJsonUseFastJson(kvp);
		String json2 = JsonUtil.toJsonUseJackson(kvp);
		Gson gson = new Gson();
		String json3 = gson.toJson(kvp);

		KeyValuePair<Long, List<Animal>> kvp2 = JsonUtil.toObjectUseFastJson(json, kvp.getClass());
		KeyValuePair<Long, List<Animal>> kvp3 = JsonUtil.toObjectUseJackson(json2, kvp.getClass());
		Type typeOfT = new TypeToken<KeyValuePair<Long, List<Animal>>>() {
		}.getType();
		KeyValuePair<Long, List<Animal>> kvp4 = gson.fromJson(json3, typeOfT);
		for (Animal item : kvp4.getValue()) {
			System.out.println(item.getName());
		}
		ObjectMapper mapper = new ObjectMapper();
		JavaType javaType = mapper.getTypeFactory().constructType(Long.class);
		JavaType javaType2 = mapper.getTypeFactory().constructCollectionType(List.class, Animal.class);
		JavaType jt = mapper.getTypeFactory().constructParametricType(kvp.getClass(), javaType, javaType2);
		//JavaType resultType = mapper.getTypeFactory().constructParametrizedType(kvp.getClass(), Long.class, javaType);
		
		MyTypeReference myType = new MyTypeReference();
		myType.setType(clazz.getGenericSuperclass());
		//ReflectUtil.setValueByFieldName(tr, "_type", tt.getClass().getGenericSuperclass());
		kvp3 = (KeyValuePair<Long, List<Animal>>) mapper.readValue(json2,
				myType);
		kvp3 = mapper.readValue(json2, jt);
		
		int in = 1;
		String jsonValue = JsonUtil.toJsonUseJackson(in);
		MyTypeReference myType2 = new MyTypeReference();
		myType2.setType(int.class);
		int in2 = (int)JsonUtil.toObjectUseJackson(jsonValue, myType2);
		System.out.println(json);
	}
}
