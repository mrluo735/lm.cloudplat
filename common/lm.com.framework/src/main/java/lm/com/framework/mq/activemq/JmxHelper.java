package lm.com.framework.mq.activemq;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.activemq.broker.jmx.BrokerViewMBean;
import org.apache.activemq.broker.jmx.ProducerViewMBean;
import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.apache.activemq.broker.jmx.SubscriptionViewMBean;
import org.apache.activemq.broker.jmx.TopicViewMBean;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.web.RemoteJMXBrokerFacade;
import org.apache.activemq.web.config.SystemPropertiesConfiguration;
import org.springframework.util.CollectionUtils;

import lm.com.framework.StringUtil;

/**
 * Jmx Helper
 * 
 * @author mrluo735
 *
 */
public class JmxHelper {
	private static final String URL_FORMAT = "service:jmx:rmi:///jndi/rmi://%s:%s/jmxrmi";
	private RemoteJMXBrokerFacade brokerFacade;

	private String host;
	private int port;
	private String userName;
	private String password;

	/**
	 * 获取BrokerFacade
	 * 
	 * @return
	 */
	public RemoteJMXBrokerFacade getBrokerFacade() {
		this.brokerFacade = new RemoteJMXBrokerFacade();
		System.setProperty("webconsole.jmx.url", String.format(URL_FORMAT, this.host, this.port));
		if (!StringUtil.isNullOrWhiteSpace(this.userName)) {
			System.setProperty("webconsole.jmx.user", this.userName);
			System.setProperty("webconsole.jmx.password", this.password);
		}
		SystemPropertiesConfiguration configuration = new SystemPropertiesConfiguration();
		this.brokerFacade.setConfiguration(configuration);
		return this.brokerFacade;
	}

	/**
	 * 关闭BrokerFacade
	 */
	public void closeBrokerFacade() {
		if (this.brokerFacade != null) {
			this.brokerFacade.shutdown();
		}
	}

	// region 获取ActiveMQ Broker信息
	/**
	 * 获取ActiveMQ Broker信息
	 * 
	 * @return
	 */
	public Map<String, Object> getBroker() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			this.getBrokerFacade();
			BrokerViewMBean bvBean = this.brokerFacade.getBrokerAdmin();
			map.put("averageMessageSize", bvBean.getAverageMessageSize());
			map.put("brokerId", bvBean.getBrokerId());
			map.put("brokerName", bvBean.getBrokerName());
			map.put("brokerVersion", bvBean.getBrokerVersion());
			map.put("currentConnectionsCount", bvBean.getCurrentConnectionsCount());
			map.put("dataDirectory", bvBean.getDataDirectory());
			map.put("durableTopicSubscribers", bvBean.getDurableTopicSubscribers());
			map.put("dynamicDestinationProducers", bvBean.getDynamicDestinationProducers());
			map.put("inactiveDurableTopicSubscribers", bvBean.getInactiveDurableTopicSubscribers());
			map.put("jmsJobScheduler", bvBean.getJMSJobScheduler());
			map.put("jobSchedulerStoreLimit", bvBean.getJobSchedulerStoreLimit());
			map.put("jobSchedulerStorePercentUsage", bvBean.getJobSchedulerStorePercentUsage());
			map.put("maxMessageSize", bvBean.getMaxMessageSize());
			map.put("memoryLimit", bvBean.getMemoryLimit());
			map.put("memoryPercentUsage", bvBean.getMemoryPercentUsage());
			map.put("minMessageSize", bvBean.getMinMessageSize());
			map.put("persistent", bvBean.isPersistent());
			map.put("queueProducers", bvBean.getQueueProducers());
			map.put("queueSubscribers", bvBean.getQueueSubscribers());
			map.put("queues", bvBean.getQueues());
			map.put("slave", bvBean.isSlave());
			map.put("statisticsEnabled", bvBean.isStatisticsEnabled());
			map.put("storeLimit", bvBean.getStoreLimit());
			map.put("storePercentUsage", bvBean.getStorePercentUsage());
			map.put("tempLimit", bvBean.getTempLimit());
			map.put("tempPercentUsage", bvBean.getTempPercentUsage());
			map.put("temporaryQueueProducers", bvBean.getTemporaryQueueProducers());
			map.put("temporaryQueueSubscribers", bvBean.getTemporaryQueueSubscribers());
			map.put("temporaryQueues", bvBean.getTemporaryQueues());
			map.put("temporaryTopicProducers", bvBean.getTemporaryTopicProducers());
			map.put("temporaryTopicSubscribers", bvBean.getTemporaryTopicSubscribers());
			map.put("temporaryTopics", bvBean.getTemporaryTopics());
			map.put("topicProducers", bvBean.getTopicProducers());
			map.put("topicSubscribers", bvBean.getTopicSubscribers());
			map.put("topics", bvBean.getTopics());
			map.put("totalConnectionsCount", bvBean.getTotalConnectionsCount());
			map.put("totalConsumerCount", bvBean.getTotalConsumerCount());
			map.put("totalDequeueCount", bvBean.getTotalDequeueCount());
			map.put("totalEnqueueCount", bvBean.getTotalEnqueueCount());
			map.put("totalMessageCount", bvBean.getTotalMessageCount());
			map.put("totalProducerCount", bvBean.getTotalProducerCount());
			map.put("transportConnectors", bvBean.getTransportConnectors());
			map.put("uptime", bvBean.getUptime());
			map.put("uptimeMillis", bvBean.getUptimeMillis());
			map.put("vmURL", bvBean.getVMURL());
		} catch (Exception e) {
		} finally {
			this.closeBrokerFacade();
		}
		return map;
	}
	// endregion

	// region 获取生产者信息
	/**
	 * 获取生产者信息
	 * 
	 * @param name
	 * @param type
	 * @return
	 */
	public List<Map<String, Object>> getProducers(String name, byte type) {
		List<Map<String, Object>> list = new ArrayList<>();
		try {
			this.getBrokerFacade();
			Collection<ProducerViewMBean> producerList;
			if (type == ActiveMQDestination.QUEUE_TYPE || type == ActiveMQDestination.TEMP_QUEUE_TYPE)
				producerList = this.brokerFacade.getQueueProducers(name);
			else if (type == ActiveMQDestination.TOPIC_TYPE || type == ActiveMQDestination.TEMP_TOPIC_TYPE)
				producerList = this.brokerFacade.getTopicProducers(name);
			else
				return list;
			if (CollectionUtils.isEmpty(producerList))
				return list;

			for (ProducerViewMBean item : producerList) {
				Map<String, Object> map = new HashMap<>();
				map.put("clientId", item.getClientId());
				map.put("connectionId", item.getConnectionId());
				map.put("sessionId", item.getSessionId());
				map.put("producerId", item.getProducerId());
				map.put("destinationName", item.getDestinationName());
				map.put("destinationQueue", item.isDestinationQueue());
				map.put("destinationTopic", item.isDestinationTopic());
				map.put("destinationTemporary", item.isDestinationTemporary());
				map.put("producerWindowSize", item.getProducerWindowSize());
				map.put("dispatchAsync", item.isDispatchAsync());
				map.put("userName", item.getUserName());
				map.put("producerBlocked", item.isProducerBlocked());
				map.put("totalTimeBlocked", item.getTotalTimeBlocked());
				map.put("percentageBlocked", item.getPercentageBlocked());
				list.add(map);
			}
		} catch (Exception e) {
		} finally {
			this.closeBrokerFacade();
		}
		return list;
	}
	// endregion

	// region 获取消费者/订阅者信息
	/**
	 * 获取消费者/订阅者信息
	 * 
	 * @param name
	 * @param type
	 * @return
	 */
	public List<Map<String, Object>> getSubscriptions(String name, byte type) {
		List<Map<String, Object>> list = new ArrayList<>();
		try {
			this.getBrokerFacade();
			Collection<SubscriptionViewMBean> subscribeList;
			if (type == ActiveMQDestination.QUEUE_TYPE || type == ActiveMQDestination.TEMP_QUEUE_TYPE)
				subscribeList = this.brokerFacade.getQueueConsumers(name);
			else if (type == ActiveMQDestination.TOPIC_TYPE || type == ActiveMQDestination.TEMP_TOPIC_TYPE)
				subscribeList = this.brokerFacade.getTopicSubscribers(name);
			else
				return list;
			if (CollectionUtils.isEmpty(subscribeList))
				return list;

			for (SubscriptionViewMBean item : subscribeList) {
				Map<String, Object> map = new HashMap<>();
				map.put("clientId", item.getClientId());
				map.put("connectionId", item.getConnectionId());
				map.put("sessionId", item.getSessionId());
				map.put("subscriptionId", item.getSubscriptionId());
				map.put("selector", item.getSelector());
				map.put("destinationName", item.getDestinationName());
				map.put("destinationQueue", item.isDestinationQueue());
				map.put("destinationTopic", item.isDestinationTopic());
				map.put("destinationTemporary", item.isDestinationTemporary());
				map.put("active", item.isActive());
				map.put("network", item.isNetwork());
				map.put("pendingQueueSize", item.getPendingQueueSize());
				map.put("dispatchedQueueSize", item.getDispatchedQueueSize());
				map.put("messageCountAwaitingAcknowledge", item.getMessageCountAwaitingAcknowledge());
				map.put("dispatchedCounter", item.getDispatchedCounter());
				map.put("enqueueCounter", item.getEnqueueCounter());
				map.put("dequeueCounter", item.getDequeueCounter());
				map.put("prefetchSize", item.getPrefetchSize());
				map.put("retroactive", item.isRetroactive());
				map.put("exclusive", item.isExclusive());
				map.put("durable", item.isDurable());
				map.put("noLocal", item.isNoLocal());
				map.put("dispatchAsync", item.isDispatchAsync());
				map.put("maximumPendingMessageLimit", item.getMaximumPendingMessageLimit());
				map.put("priority", item.getPriority());
				map.put("subscriptionName", item.getSubscriptionName());
				map.put("slowConsumer", item.isSlowConsumer());
				map.put("userName", item.getUserName());
				map.put("consumedCount", item.getConsumedCount());
				list.add(map);
			}
		} catch (Exception e) {
		} finally {
			this.closeBrokerFacade();
		}
		return list;
	}
	// endregion

	// region 获取Queue信息
	/**
	 * 获取Queue信息
	 * 
	 * @return
	 */
	public Collection<QueueViewMBean> getQueues() {
		try {
			this.getBrokerFacade();
			return this.brokerFacade.getQueues();
		} catch (Exception e) {
		}
		return null;
	}
	// endregion

	// region 获取Topic信息
	/**
	 * 获取Topic信息
	 * 
	 * @return
	 */
	public Collection<TopicViewMBean> getTopics() {
		try {
			this.getBrokerFacade();
			return this.brokerFacade.getTopics();
		} catch (Exception e) {
		}
		return null;
	}
	// endregion

	// region 清除Queue信息
	/**
	 * 清除Queue信息
	 * 
	 * @return
	 */
	public void purgeQueue(String name) {
		try {
			this.getBrokerFacade();
			this.brokerFacade.purgeQueue(new ActiveMQQueue(name));
		} catch (Exception e) {
		}
	}
	// endregion

	// region 删除目标信息
	/**
	 * 删除目标信息
	 * 
	 * @return
	 */
	public void removeDestination(String name, byte type) {
		try {
			this.getBrokerFacade();
			if (type == ActiveMQDestination.QUEUE_TYPE || type == ActiveMQDestination.TEMP_QUEUE_TYPE)
				this.brokerFacade.getBrokerAdmin().removeQueue(name);
			else if (type == ActiveMQDestination.TOPIC_TYPE || type == ActiveMQDestination.TEMP_TOPIC_TYPE)
				this.brokerFacade.getBrokerAdmin().removeTopic(name);
		} catch (Exception e) {
		}
	}
	// endregion
}
