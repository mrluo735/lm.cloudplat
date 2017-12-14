package lm.com.framework.mq.activemq;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQMessageProducer;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.ActiveMQXAConnectionFactory;
import org.apache.activemq.AsyncCallback;
import org.apache.activemq.broker.region.Queue;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTempQueue;
import org.apache.activemq.command.ActiveMQTempTopic;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.apache.activemq.pool.XaPooledConnectionFactory;
import org.apache.activemq.util.ByteSequence;

/**
 * JmsHelper
 * 
 * @author mrluo735
 *
 */
public class JmsHelper {
	private ActiveMQConnectionFactory connectionFactory;
	private PooledConnectionFactory pooledConnectionFactory;

	private String brokerUrl = ActiveMQConnection.DEFAULT_BROKER_URL;
	private String userName = ActiveMQConnection.DEFAULT_USER;
	private String password = ActiveMQConnection.DEFAULT_PASSWORD;
	private boolean isXA = false;
	private int xaAckMode = -1;
	private boolean useCompression = true;
	private int maxThreadPoolSize = ActiveMQConnection.DEFAULT_THREAD_POOL_SIZE;

	private boolean enablePooled = true;
	private int maxConnections = 8;
	private int maximumActiveSessionPerConnection = 500;
	private int idleTimeout = 30 * 1000;
	private boolean blockIfSessionPoolIsFull = true;
	private long blockIfSessionPoolIsFullTimeout = -1L;
	private long expiryTimeout = 0l;

	/**
	 * 构造函数
	 */
	public JmsHelper() {
		this(true);
	}

	/**
	 * 构造函数
	 */
	public JmsHelper(boolean enablePooled) {
		this.enablePooled = enablePooled;
		if (this.enablePooled)
			this.createPooledConnectionFactory();
		else
			this.createConnection();
	}

	/**
	 * 创建连接工厂
	 * 
	 * @return
	 */
	private void createConnectionFactory() {
		if (!isXA)
			this.connectionFactory = new ActiveMQConnectionFactory();
		else {
			ActiveMQXAConnectionFactory xaConnectoinFactory = new ActiveMQXAConnectionFactory();
			xaConnectoinFactory.setXaAckMode(this.xaAckMode);
			this.connectionFactory = xaConnectoinFactory;
		}
		this.connectionFactory.setBrokerURL(this.brokerUrl);
		this.connectionFactory.setUserName(this.userName);
		this.connectionFactory.setPassword(this.password);
		this.connectionFactory.setUseCompression(this.useCompression);
		this.connectionFactory.setMaxThreadPoolSize(this.maxThreadPoolSize);
	}

	/**
	 * 创建连接工厂池
	 */
	private void createPooledConnectionFactory() {
		this.createConnectionFactory();
		if (!this.isXA)
			this.pooledConnectionFactory = new PooledConnectionFactory();
		else {
			this.pooledConnectionFactory = new XaPooledConnectionFactory(
					(ActiveMQXAConnectionFactory) this.connectionFactory);
			// ((XaPooledConnectionFactory)pooledConnectionFactory).setTransactionManager();
		}
		this.pooledConnectionFactory.setConnectionFactory(this.connectionFactory);

		this.pooledConnectionFactory.setMaxConnections(maxConnections);
		this.pooledConnectionFactory.setMaximumActiveSessionPerConnection(maximumActiveSessionPerConnection);
		this.pooledConnectionFactory.setIdleTimeout(idleTimeout);
		this.pooledConnectionFactory.setBlockIfSessionPoolIsFull(blockIfSessionPoolIsFull);
		this.pooledConnectionFactory.setBlockIfSessionPoolIsFullTimeout(blockIfSessionPoolIsFullTimeout);
		this.pooledConnectionFactory.setExpiryTimeout(expiryTimeout);
	}

	/**
	 * 关闭连接池
	 */
	public void closePooled() {
		if (this.enablePooled)
			this.pooledConnectionFactory.stop();
	}

	/**
	 * 创建连接
	 * 
	 * @return
	 */
	public ActiveMQConnection createConnection() {
		try {
			ActiveMQConnection connection = null;
			if (this.enablePooled) {
				connection = (ActiveMQConnection) this.pooledConnectionFactory.createConnection();
			} else {
				this.createConnectionFactory();
				connection = (ActiveMQConnection) this.connectionFactory.createConnection();
			}
			connection.start();
			return connection;
		} catch (JMSException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 关闭连接
	 * 
	 * @param connection
	 */
	public void closeConnection(ActiveMQConnection connection) {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (JMSException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 重载+1 创建目标
	 * 
	 * @param name
	 * @param type
	 * @return
	 */
	public ActiveMQDestination createDestination(String name, byte type) {
		return this.createDestination(name, type, false, ActiveMQSession.AUTO_ACKNOWLEDGE);
	}

	/**
	 * 重载+2 创建目标
	 * 
	 * @param name
	 * @param type
	 * @param transacted
	 * @param acknowledgeMode
	 * @return
	 */
	public ActiveMQDestination createDestination(String name, byte type, boolean transacted, int acknowledgeMode) {
		ActiveMQConnection connection = null;
		ActiveMQSession session = null;
		ActiveMQDestination destination = null;
		try {
			connection = this.createConnection();
			session = (ActiveMQSession) connection.createSession(transacted, acknowledgeMode);
			if (type == ActiveMQDestination.QUEUE_TYPE)
				destination = (ActiveMQDestination) session.createQueue(name);
			else if (type == ActiveMQDestination.TOPIC_TYPE)
				destination = (ActiveMQDestination) session.createTopic(name);
			else if (type == ActiveMQDestination.TEMP_QUEUE_TYPE)
				destination = (ActiveMQDestination) session.createTemporaryQueue();
			else if (type == ActiveMQDestination.TEMP_TOPIC_TYPE)
				destination = (ActiveMQDestination) session.createTemporaryTopic();
			else
				return null;

			return destination;
		} catch (JMSException ex) {
			throw new RuntimeException(ex);
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (JMSException e) {
				}
			}
			this.closeConnection(connection);
		}
	}

	/**
	 * 同步发送消息
	 * 
	 * @param name
	 * @param type
	 * @param content
	 * @param transacted
	 * @param acknowledgeMode
	 * @param deliveryMode
	 * @param priority
	 * @param timeToLive
	 */
	public void sendSync(String name, byte type, String content) {
		try {
			this.sendSync(name, type, content.getBytes("utf-8"), false, ActiveMQSession.AUTO_ACKNOWLEDGE, 0, 0, 0L);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 同步发送消息
	 * 
	 * @param name
	 * @param type
	 * @param content
	 * @param transacted
	 * @param acknowledgeMode
	 * @param deliveryMode
	 * @param priority
	 * @param timeToLive
	 */
	public void sendSync(String name, byte type, byte[] content, boolean transacted, int acknowledgeMode,
			int deliveryMode, int priority, long timeToLive) {
		ActiveMQConnection connection = null;
		ActiveMQSession session = null;
		ActiveMQMessageProducer producer = null;
		try {
			connection = this.createConnection();
			session = (ActiveMQSession) connection.createSession(transacted, acknowledgeMode);
			if (type == ActiveMQDestination.QUEUE_TYPE)
				producer = (ActiveMQMessageProducer) session.createProducer(new ActiveMQQueue(name));
			else if (type == ActiveMQDestination.TOPIC_TYPE)
				producer = (ActiveMQMessageProducer) session.createProducer(new ActiveMQTopic(name));
			else if (type == ActiveMQDestination.TEMP_QUEUE_TYPE)
				producer = (ActiveMQMessageProducer) session.createProducer(new ActiveMQTempQueue(name));
			else if (type == ActiveMQDestination.TEMP_TOPIC_TYPE)
				producer = (ActiveMQMessageProducer) session.createProducer(new ActiveMQTempTopic(name));
			else
				return;

			ActiveMQMessage message = new ActiveMQMessage();
			message.setContent(new ByteSequence(content));
			producer.send(message, deliveryMode, priority, timeToLive);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			if (producer != null) {
				try {
					producer.close();
				} catch (JMSException e) {
				}
			}
			if (session != null) {
				try {
					session.close();
				} catch (JMSException e) {
				}
			}
			this.closeConnection(connection);
		}
	}

	/**
	 * 异步发送消息
	 * 
	 * @param name
	 * @param type
	 * @param content
	 * @param transacted
	 * @param acknowledgeMode
	 * @param onComplete
	 */
	public void sendAsync(String name, byte type, byte[] content, boolean transacted, int acknowledgeMode,
			AsyncCallback onComplete) {
		ActiveMQConnection connection = null;
		ActiveMQSession session = null;
		ActiveMQMessageProducer producer = null;
		try {
			connection = this.createConnection();
			session = (ActiveMQSession) connection.createSession(transacted, acknowledgeMode);
			if (type == ActiveMQDestination.QUEUE_TYPE)
				producer = (ActiveMQMessageProducer) session.createProducer(new ActiveMQQueue(name));
			else if (type == ActiveMQDestination.TOPIC_TYPE)
				producer = (ActiveMQMessageProducer) session.createProducer(new ActiveMQTopic(name));
			else if (type == ActiveMQDestination.TEMP_QUEUE_TYPE)
				producer = (ActiveMQMessageProducer) session.createProducer(new ActiveMQTempQueue(name));
			else if (type == ActiveMQDestination.TEMP_TOPIC_TYPE)
				producer = (ActiveMQMessageProducer) session.createProducer(new ActiveMQTempTopic(name));
			else
				return;

			ActiveMQMessage message = new ActiveMQMessage();
			message.setContent(new ByteSequence(content));
			producer.send(message, onComplete);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			if (producer != null) {
				try {
					producer.close();
				} catch (JMSException e) {
				}
			}
			if (session != null) {
				try {
					session.close();
				} catch (JMSException e) {
				}
			}
			this.closeConnection(connection);
		}
	}
}
