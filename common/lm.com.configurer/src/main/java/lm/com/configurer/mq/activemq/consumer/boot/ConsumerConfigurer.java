package lm.com.configurer.mq.activemq.consumer.boot;

import javax.jms.ConnectionFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

/**
 * 消费端配置
 * 
 * @author mrluo735
 *
 */
@EnableJms
@Configuration
public class ConsumerConfigurer {
	/**
	 * Queue
	 * 
	 * @param activeMQConnectionFactory
	 * @return
	 */
	@Bean
	public JmsListenerContainerFactory<?> jmsListenerContainerQueue(ConnectionFactory activeMQConnectionFactory) {
		DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
		bean.setConnectionFactory(activeMQConnectionFactory);
		return bean;
	}

	/**
	 * Topic
	 * 
	 * @param activeMQConnectionFactory
	 * @return
	 */
	@Bean
	public JmsListenerContainerFactory<?> jmsListenerContainerTopic(ConnectionFactory activeMQConnectionFactory) {
		DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
		bean.setPubSubDomain(true);
		bean.setConnectionFactory(activeMQConnectionFactory);
		return bean;
	}
}
