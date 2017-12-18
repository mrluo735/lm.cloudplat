package lm.com.framework.mq.activemq;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.QueueBrowser;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQMessageConsumer;
import org.apache.activemq.ActiveMQMessageProducer;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.ActiveMQXAConnectionFactory;
import org.apache.activemq.AsyncCallback;
import org.apache.activemq.BlobMessage;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.activemq.command.ActiveMQMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTempQueue;
import org.apache.activemq.command.ActiveMQTempTopic;
import org.apache.activemq.command.ActiveMQTopic;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.apache.activemq.pool.XaPooledConnectionFactory;

import lm.com.framework.StreamUtil;

/**
 * JmsHelper
 * 
 * @author mrluo735
 *
 */
public class JmsHelper {
	private ActiveMQConnectionFactory connectionFactory;
	private PooledConnectionFactory pooledConnectionFactory;
	private ActiveMQConnection connection;

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

	// region 属性
	/**
	 * 获取BrokerUrl
	 * 
	 * @return
	 */
	public String getBrokerUrl() {
		return brokerUrl;
	}

	public void setBrokerUrl(String brokerUrl) {
		this.brokerUrl = brokerUrl;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isXA() {
		return isXA;
	}

	public void setXA(boolean isXA) {
		this.isXA = isXA;
	}

	public int getXaAckMode() {
		return xaAckMode;
	}

	public void setXaAckMode(int xaAckMode) {
		this.xaAckMode = xaAckMode;
	}

	public boolean isUseCompression() {
		return useCompression;
	}

	public void setUseCompression(boolean useCompression) {
		this.useCompression = useCompression;
	}

	public int getMaxThreadPoolSize() {
		return maxThreadPoolSize;
	}

	public void setMaxThreadPoolSize(int maxThreadPoolSize) {
		this.maxThreadPoolSize = maxThreadPoolSize;
	}

	public boolean isEnablePooled() {
		return enablePooled;
	}

	public void setEnablePooled(boolean enablePooled) {
		this.enablePooled = enablePooled;
	}

	public int getMaxConnections() {
		return maxConnections;
	}

	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}

	public int getMaximumActiveSessionPerConnection() {
		return maximumActiveSessionPerConnection;
	}

	public void setMaximumActiveSessionPerConnection(int maximumActiveSessionPerConnection) {
		this.maximumActiveSessionPerConnection = maximumActiveSessionPerConnection;
	}

	public int getIdleTimeout() {
		return idleTimeout;
	}

	public void setIdleTimeout(int idleTimeout) {
		this.idleTimeout = idleTimeout;
	}

	public boolean isBlockIfSessionPoolIsFull() {
		return blockIfSessionPoolIsFull;
	}

	public void setBlockIfSessionPoolIsFull(boolean blockIfSessionPoolIsFull) {
		this.blockIfSessionPoolIsFull = blockIfSessionPoolIsFull;
	}

	public long getBlockIfSessionPoolIsFullTimeout() {
		return blockIfSessionPoolIsFullTimeout;
	}

	public void setBlockIfSessionPoolIsFullTimeout(long blockIfSessionPoolIsFullTimeout) {
		this.blockIfSessionPoolIsFullTimeout = blockIfSessionPoolIsFullTimeout;
	}

	public long getExpiryTimeout() {
		return expiryTimeout;
	}

	public void setExpiryTimeout(long expiryTimeout) {
		this.expiryTimeout = expiryTimeout;
	}
	// endregion

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
			this.createConnectionFactory();
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
			if (this.connection != null)
				return this.connection;

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
	 * 重载+1 关闭连接
	 */
	public void closeConnection() {
		this.closeConnection(this.connection);
	}

	/**
	 * 重载+2 关闭连接
	 * 
	 * @param connection
	 */
	public void closeConnection(ActiveMQConnection connection) {
		try {
			if (this.connection != null) {
				this.connection.close();
				this.connection = null;
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
	public boolean createDestination(String name, byte type) {
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
	public boolean createDestination(String name, byte type, boolean transacted, int acknowledgeMode) {
		ActiveMQSession session = null;
		ActiveMQDestination destination = null;
		ActiveMQMessageProducer producer = null;
		try {
			this.createConnection();
			session = (ActiveMQSession) this.connection.createSession(transacted, acknowledgeMode);
			destination = this.getDestination(name, type);
			if (destination == null)
				return false;
			producer = (ActiveMQMessageProducer) session.createProducer(new ActiveMQQueue(name));
			return true;
		} catch (JMSException ex) {
			return false;
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
			this.closeConnection();
		}
	}

	/**
	 * 重载+1 同步发送消息
	 * 
	 * @param name
	 * @param type
	 * @param content
	 */
	public void sendSync(String name, byte type, String content) {
		try {
			this.send(name, type, content, false, ActiveMQSession.AUTO_ACKNOWLEDGE, 0, 0, 0L, null);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 重载+2 同步发送消息
	 * 
	 * @param name
	 * @param type
	 * @param content
	 */
	public void sendSync(String name, byte type, byte[] content) {
		try {
			this.send(name, type, content, false, ActiveMQSession.AUTO_ACKNOWLEDGE, 0, 0, 0L, null);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 重载+1 异步发送消息
	 * 
	 * @param name
	 * @param type
	 * @param content
	 * @param onComplete
	 */
	public void sendAsync(String name, byte type, String content, AsyncCallback onComplete) {
		try {
			this.send(name, type, content, false, ActiveMQSession.AUTO_ACKNOWLEDGE, 0, 0, 0L, onComplete);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 重载+2 异步发送消息
	 * 
	 * @param name
	 * @param type
	 * @param content
	 * @param onComplete
	 */
	public void sendAsync(String name, byte type, byte[] content, AsyncCallback onComplete) {
		try {
			this.send(name, type, content, false, ActiveMQSession.AUTO_ACKNOWLEDGE, 0, 0, 0L, onComplete);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 发送消息
	 * 
	 * @param <T>
	 * @param name
	 * @param type
	 * @param content
	 * @param transacted
	 * @param acknowledgeMode
	 * @param deliveryMode
	 * @param priority
	 * @param timeToLive
	 * @param onComplete
	 */
	public <T> void send(String name, byte type, T content, boolean transacted, int acknowledgeMode, int deliveryMode,
			int priority, long timeToLive, AsyncCallback onComplete) {
		ActiveMQSession session = null;
		ActiveMQMessageProducer producer = null;
		try {
			this.createConnection();
			Destination destination = this.getDestination(name, type);
			if (destination == null)
				throw new RuntimeException("destination is null!");

			session = (ActiveMQSession) this.connection.createSession(transacted, acknowledgeMode);
			producer = (ActiveMQMessageProducer) session.createProducer(destination);

			Message message = null;
			if (content instanceof byte[]) { // CommandTypes.ACTIVEMQ_BYTES_MESSAGE
				BytesMessage bytesMessage = session.createBytesMessage();
				bytesMessage.writeBytes((byte[]) content);
				message = bytesMessage;
			} else if (content instanceof Map) { // CommandTypes.ACTIVEMQ_MAP_MESSAGE
				ActiveMQMapMessage mapMessage = (ActiveMQMapMessage) session.createMapMessage();
				mapMessage.getContentMap().putAll((Map<String, Object>) content);
				message = mapMessage;
			} else if (content instanceof Serializable) { // CommandTypes.ACTIVEMQ_OBJECT_MESSAGE
				ObjectMessage objectMessage = session.createObjectMessage((Serializable) content);
				message = objectMessage;
			} else if (content instanceof InputStream) { // CommandTypes.ACTIVEMQ_STREAM_MESSAGE
				StreamMessage streamMessage = session.createStreamMessage();
				streamMessage.writeBytes(StreamUtil.inputStream2Bytes((InputStream) content));
				message = streamMessage;
			} else if (content instanceof String) {
				TextMessage textMessage = session.createTextMessage((String) content);
				message = textMessage;
			} else if (content instanceof InputStream) { // CommandTypes.ACTIVEMQ_BLOB_MESSAGE
				BlobMessage blobMessage = session.createBlobMessage((InputStream) content);
				message = blobMessage;
			} else
				return;
			if (onComplete == null)
				producer.send(message, deliveryMode, priority, timeToLive);
			else
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
			this.closeConnection();
		}
	}

	/**
	 * 重载+1 同步消费消息
	 * 
	 * @param name
	 * @param type
	 * @param contentType
	 * @param transacted
	 * @param acknowledgeMode
	 */
	public String consumeSync(String name, byte type) {
		return this.consumeSync(name, type, TextMessage.class, false, ActiveMQSession.AUTO_ACKNOWLEDGE).toString();
	}

	/**
	 * 重载+2 同步消费消息
	 * 
	 * @param name
	 * @param type
	 * @param contentType
	 * @param transacted
	 * @param acknowledgeMode
	 */
	public <T> Object consumeSync(String name, byte type, Class<T> contentType, boolean transacted,
			int acknowledgeMode) {
		ActiveMQSession session = null;
		ActiveMQMessageConsumer consumer = null;
		try {
			this.createConnection();
			session = (ActiveMQSession) this.connection.createSession(transacted, acknowledgeMode);
			Destination destination = this.getDestination(name, type);
			if (destination == null)
				throw new RuntimeException("destination is null!");

			consumer = (ActiveMQMessageConsumer) session.createConsumer(destination);
			ActiveMQMessage message = (ActiveMQMessage) consumer.receive(6000);
			return message.getContent();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			if (consumer != null) {
				try {
					consumer.close();
				} catch (JMSException e) {
				}
			}
			if (session != null) {
				try {
					session.close();
				} catch (JMSException e) {
				}
			}
			this.closeConnection();
		}
	}

	/**
	 * 异步消费消息
	 * 
	 * @param name
	 * @param type
	 * @param transacted
	 * @param acknowledgeMode
	 * @param listener
	 */
	public ActiveMQMessageConsumer consumeAsync(String name, byte type, boolean transacted, int acknowledgeMode,
			MessageListener listener) {
		ActiveMQSession session = null;
		ActiveMQMessageConsumer consumer = null;
		try {
			this.createConnection();
			session = (ActiveMQSession) this.connection.createSession(transacted, acknowledgeMode);
			Destination destination = this.getDestination(name, type);
			if (destination == null)
				throw new RuntimeException("destination is null!");

			consumer = (ActiveMQMessageConsumer) session.createConsumer(destination);
			consumer.setMessageListener(listener);
			// consumer.commit();
			return consumer;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
		}
	}

	/**
	 * 浏览消息内容
	 * 
	 * @param name
	 */
	public List<Object> brower(String name) {
		List<Object> list = new ArrayList<Object>();

		ActiveMQSession session = null;
		QueueBrowser browser = null;
		try {
			this.createConnection();
			session = (ActiveMQSession) this.connection.createSession(false, ActiveMQSession.AUTO_ACKNOWLEDGE);
			browser = session.createBrowser(new ActiveMQQueue(name));
			while (browser.getEnumeration().hasMoreElements()) {
				Object object = browser.getEnumeration().nextElement();
				list.add(object);
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			if (browser != null) {
				try {
					browser.close();
				} catch (JMSException e) {
				}
			}
			if (session != null) {
				try {
					session.close();
				} catch (JMSException e) {
				}
			}
			this.closeConnection();
		}
		return list;
	}

	/**
	 * 获取目标
	 * 
	 * @param name
	 * @param type
	 * @return
	 */
	private ActiveMQDestination getDestination(String name, byte type) {
		switch (type) {
			case ActiveMQDestination.QUEUE_TYPE:
				return new ActiveMQQueue(name);
			case ActiveMQDestination.TOPIC_TYPE:
				return new ActiveMQTopic(name);
			case ActiveMQDestination.TEMP_QUEUE_TYPE:
				return new ActiveMQTempQueue(name);
			case ActiveMQDestination.TEMP_TOPIC_TYPE:
				return new ActiveMQTempTopic(name);
			default:
				return null;
		}
	}
}
