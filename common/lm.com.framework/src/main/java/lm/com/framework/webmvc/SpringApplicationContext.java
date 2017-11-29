package lm.com.framework.webmvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

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
	 * 
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringApplicationContext.applicationContext = applicationContext;
	}
}
