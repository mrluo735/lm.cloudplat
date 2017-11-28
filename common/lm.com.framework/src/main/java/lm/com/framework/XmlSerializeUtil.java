package lm.com.framework;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAnyElement;

/**
 * xml序列化帮助类
 * @author mrluo735
 *
 */
public class XmlSerializeUtil {
	/**
	 * 响应类和对应的格式化类
	 */
	private static ConcurrentMap<String, JAXBContext> classJaxbContextMap = new ConcurrentHashMap<String, JAXBContext>();
	
	/**
	 * Xml->Java Object.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T fromXml(String xml, Class<T> clazz) {
		try {
			StringReader reader = new StringReader(xml);
			return (T) createUnmarshaller(clazz).unmarshal(reader);
		} catch (JAXBException e) {
			throw new RuntimeException();
		}
	}
	
	/**
	 * Java Object->Xml with encoding.
	 */
	public static String toXml(Object instance, String encoding) {
		return toXml(instance, instance.getClass(), encoding);
	}

	/**
	 * Java Object->Xml with encoding.
	 */
	public static String toXml(Object root, Class<?> clazz, String encoding) {
		try {
			StringWriter writer = new StringWriter();
			createMarshaller(clazz, encoding).marshal(root, writer);
			return writer.toString();
		} catch (JAXBException e) {
			throw new RuntimeException();
		}
	}
	
	/**
	 * 创建Marshaller并设定encoding(可为null). 线程不安全，需要每次创建或pooling。
	 */
	public static Marshaller createMarshaller(Class<?> clazz, String encoding) {
		try {
			JAXBContext jaxbContext = getJaxbContext(clazz);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
			marshaller.setProperty(Marshaller.JAXB_ENCODING, encoding);

			return marshaller;
		} catch (JAXBException e) {
			throw new RuntimeException();
		}
	}
	
	/**
	 * 创建UnMarshaller.
	 * 线程不安全，需要每次创建或pooling。
	 */
	public static Unmarshaller createUnmarshaller(Class<?> clazz) {
		try {
			JAXBContext jaxbContext = getJaxbContext(clazz);
			return jaxbContext.createUnmarshaller();
		} catch (JAXBException e) {
			throw new RuntimeException();
		}
	}

	protected static JAXBContext getJaxbContext(Class<?> clazz) {
		JAXBContext jaxbContext = classJaxbContextMap.get(clazz.getSimpleName());
		if (jaxbContext == null) {
			try {
				jaxbContext = JAXBContext.newInstance(clazz, CollectionWrapper.class);
				classJaxbContextMap.putIfAbsent(clazz.getSimpleName(), jaxbContext);
			} catch (JAXBException ex) {
				throw new RuntimeException("Could not instantiate JAXBContext for class [" + clazz + "]: "
						+ ex.getMessage(), ex);
			}
		}
		return jaxbContext;
	}
	
	/**
	 * 封装Root Element 是 Collection的情况.
	 */
	public static class CollectionWrapper {
		@XmlAnyElement
		protected Collection<?> collection;
	}
}
