package lm.com.configurer.mq.activemq.producer.boot;

import javax.jms.ConnectionFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsMessagingTemplate;

/**
 * 生产者配置
 * 
 * @author mrluo735
 *
 */
@Configuration
public class ProducerConfigurer {
	/**
	 * 
	 * @param connectionFactory
	 * @return
	 */
	@Bean
	public JmsMessagingTemplate jmsMessagingTemplate(ConnectionFactory connectionFactory) {
		return new JmsMessagingTemplate(connectionFactory);
	}
}
