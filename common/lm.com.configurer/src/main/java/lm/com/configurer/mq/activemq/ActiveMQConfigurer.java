package lm.com.configurer.mq.activemq;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ActiveMQ配置
 * 
 * @author mrluo735
 *
 */
@Configuration
public class ActiveMQConfigurer {
	/**
	 * Queue
	 * 
	 * @return
	 */
	@Bean
	public ActiveMQQueue queue() {
		return new ActiveMQQueue("queue");
	}

	/**
	 * Topic
	 * 
	 * @return
	 */
	@Bean
	public ActiveMQTopic topic() {
		return new ActiveMQTopic("topic");
	}
}
