/**
 * @title BeanDeinitionRegistryBootstrap.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.pro.provider.cloud.core
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年5月4日上午10:51:38
 * @version v1.0.0
 */
package lm.com.configurer.bootstrap;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @ClassName: PostProcesser
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年5月4日 上午10:51:38
 * 
 */
@Component
public class BeanDeinitionRegistryBootstrap implements BeanDefinitionRegistryPostProcessor {
	private static BeanDefinitionRegistry beanDefinitionRegistry;

	/**
	 * 获取BeanDefinitionRegistry
	 * 
	 * @return
	 */
	public static BeanDefinitionRegistry getBeanDefinitionRegistry() {
		return beanDefinitionRegistry;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.support.
	 * BeanDefinitionRegistryPostProcessor#postProcessBeanDefinitionRegistry(org
	 * .springframework.beans.factory.support.BeanDefinitionRegistry)
	 */
	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		// TODO Auto-generated method stub
		beanDefinitionRegistry = registry;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.config.BeanFactoryPostProcessor#
	 * postProcessBeanFactory(org.springframework.beans.factory.config.
	 * ConfigurableListableBeanFactory)
	 */
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		// TODO Auto-generated method stub

	}
}
