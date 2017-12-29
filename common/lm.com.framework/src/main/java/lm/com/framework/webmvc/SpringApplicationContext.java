package lm.com.framework.webmvc;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import lm.com.framework.map.MapUtil;

/**
 * spring应用程序上下文
 * 
 * @author mrluo735
 *
 */
public class SpringApplicationContext implements ApplicationContextAware {
	private static final Logger logger = LoggerFactory.getLogger(SpringApplicationContext.class);
	private static ApplicationContext applicationContext;

	/**
	 * 获取bean
	 * 
	 * @param name
	 * @return
	 */
	public static Object getBean(String name) {
		logger.info("name:" + name + "对象:" + applicationContext);
		return applicationContext.getBean(name);
	}

	/**
	 * 获取bean
	 * 
	 * @param clazz
	 * @return
	 */
	public static <T> T getBean(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}

	/**
	 * 重载+1 注册Bean
	 * 
	 * @param clazz
	 * @param beanName
	 * @param properties
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T registBeanAsT(Class<T> clazz, String beanName) {
		return (T) registBean(clazz, beanName, null);
	}

	/**
	 * 重载+2 注册Bean
	 * 
	 * @param clazz
	 * @param beanName
	 * @param properties
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T registBeanAsT(Class<T> clazz, String beanName, Map<String, Object> properties) {
		return (T) registBean(clazz, beanName, properties);
	}

	/**
	 * 重载+1 注册Bean
	 * 
	 * @param clazz
	 * @param beanName
	 * @param properties
	 * @return
	 */
	public static Object registBean(Class<?> clazz, String beanName) {
		return registBean(clazz, beanName, null);
	}

	/**
	 * 重载+2 注册Bean
	 * 
	 * @param clazz
	 * @param beanName
	 * @param properties
	 * @return
	 */
	public static Object registBean(Class<?> clazz, String beanName, Map<String, Object> properties) {
		ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;

		// 获取bean工厂并转换为DefaultListableBeanFactory  
		DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext
				.getBeanFactory();

		// 通过BeanDefinitionBuilder创建bean定义  
		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
		// 设置属性
		if (MapUtil.isNotEmpty(properties)) {
			for (Map.Entry<String, Object> item : properties.entrySet()) {
				beanDefinitionBuilder.addPropertyValue(item.getKey(), item.getValue());
			}
		}
		// 注册bean  
		defaultListableBeanFactory.registerBeanDefinition(beanName, beanDefinitionBuilder.getRawBeanDefinition());
		return defaultListableBeanFactory.getBean(beanName);
	}

	/**
	 * 
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringApplicationContext.applicationContext = applicationContext;
	}
}
