package lm.com.framework.mq.activemq;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQXAConnectionFactory;
import org.apache.activemq.jms.pool.PooledConnectionFactory;

/**
 * JmsHelper
 * 
 * @author mrluo735
 *
 */
public class JmsHelper {
	private PooledConnectionFactory pooledConnectionFactory;
	
	private String brokerUrl = ActiveMQConnection.DEFAULT_BROKER_URL;
	private String userName = ActiveMQConnection.DEFAULT_USER;
	private String password = ActiveMQConnection.DEFAULT_PASSWORD;
	private boolean isXA = false;
	private boolean useCompression = true;

	private void createConnectionFactory() {
		ActiveMQConnectionFactory connectionFactory = null;
		if (!isXA)
			connectionFactory = new ActiveMQConnectionFactory();
		else
			connectionFactory = new ActiveMQXAConnectionFactory();
		connectionFactory.setBrokerURL(this.brokerUrl);
		connectionFactory.setUserName(this.userName);
		connectionFactory.setPassword(this.password);
		connectionFactory.setUseCompression(this.useCompression);
		
		pooledConnectionFactory = new PooledConnectionFactory();
	}
}
