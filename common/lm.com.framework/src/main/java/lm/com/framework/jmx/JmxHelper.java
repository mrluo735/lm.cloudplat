package lm.com.framework.jmx;

import java.io.IOException;
import java.lang.management.ClassLoadingMXBean;
import java.lang.management.RuntimeMXBean;
import java.util.HashMap;
import java.util.Map;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.activemq.broker.jmx.BrokerViewMBean;
import org.apache.activemq.broker.jmx.HealthViewMBean;
import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.apache.activemq.broker.jmx.TopicViewMBean;

import com.sun.management.OperatingSystemMXBean;
import com.sun.management.ThreadMXBean;

import lm.com.framework.StringUtil;

/**
 * 
 * @author mrluo735
 *
 */
public class JmxHelper {
	private static final String URL_FORMAT = "service:jmx:rmi:///jndi/rmi://%s:%s/jmxrmi";
	private static final String CLASSLOADING_OBJECT = "java.lang:type=ClassLoading";
	private static final String OPERATINGSYSTEM_OBJECT = "java.lang:type=OperatingSystem";
	private static final String RUNTIME_OBJECT = "java.lang:type=Runtime";
	private static final String THREADING_OBJECT = "java.lang:type=Threading";

	/**
	 * ActiveMQ
	 */
	private static final String ACTIVEMQ_BROKER_FORMAT = "org.apache.activemq:type=Broker,brokerName=%s";
	private static final String ACTIVEMQ_HEALTH_FORMAT = "org.apache.activemq:type=Broker,brokerName=%s,service=Health";
	private static final String ACTIVEMQ_QUEUE_FORMAT = "org.apache.activemq:type=Broker,brokerName=%s,destinationType=Queue,destinationName=%s";
	private static final String ACTIVEMQ_TOPIC_FORMAT = "org.apache.activemq:type=Broker,brokerName=%s,destinationType=Topic,destinationName=%s";

	private JMXConnector connector;
	private String host;
	private int port;
	private String userName;
	private String password;

	// region 属性
	/**
	 * 
	 * @return
	 */
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
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
	// endregion

	/**
	 * 
	 * @return
	 */
	public JMXConnector getConnector() {
		try {
			JMXServiceURL serivceUrl = new JMXServiceURL(String.format(URL_FORMAT, host, port));
			Map<String, Object> environment = new HashMap<String, Object>();
			if (!StringUtil.isNullOrWhiteSpace(userName)) {
				String[] credentials = new String[] { this.userName, this.password };
				environment.put("jmx.remote.credentials", credentials);
			}
			this.connector = JMXConnectorFactory.connect(serivceUrl, environment);
		} catch (Exception e) {
		}
		return this.connector;
	}

	/**
	 * 关闭Connector
	 */
	public void closeConnector() {
		if (this.connector != null) {
			try {
				this.connector.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * 获取ClassLoading
	 * 
	 * @return
	 */
	public Map<String, Object> getClassLoading() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			this.getConnector();
			MBeanServerConnection connection = this.connector.getMBeanServerConnection();
			ObjectName objectName = new ObjectName(CLASSLOADING_OBJECT);
			ClassLoadingMXBean clBean = MBeanServerInvocationHandler.newProxyInstance(connection, objectName,
					ClassLoadingMXBean.class, true);
			map.put("loadedClassCount", clBean.getLoadedClassCount());
			map.put("objectName", clBean.getObjectName().toString());
			map.put("totalLoadedClassCount", clBean.getTotalLoadedClassCount());
			map.put("unloadedClassCount", clBean.getUnloadedClassCount());
			map.put("verbose", clBean.isVerbose());
		} catch (Exception e) {
		} finally {
			this.closeConnector();
		}
		return map;
	}

	/**
	 * 获取OperatingSystem
	 * 
	 * @return
	 */
	@SuppressWarnings("restriction")
	public Map<String, Object> getOperatingSystem() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			this.getConnector();
			MBeanServerConnection connection = this.connector.getMBeanServerConnection();
			ObjectName objectName = new ObjectName(OPERATINGSYSTEM_OBJECT);
			OperatingSystemMXBean osBean = MBeanServerInvocationHandler.newProxyInstance(connection, objectName,
					OperatingSystemMXBean.class, true);
			map.put("arch", osBean.getArch());
			map.put("availableProcessors", osBean.getAvailableProcessors());
			map.put("committedVirtualMemorySize", osBean.getCommittedVirtualMemorySize());
			map.put("freePhysicalMemorySize", osBean.getFreePhysicalMemorySize());
			map.put("freeSwapSpaceSize", osBean.getFreeSwapSpaceSize());
			map.put("name", osBean.getName());
			map.put("objectName", osBean.getObjectName().toString());
			map.put("processCpuLoad", osBean.getProcessCpuLoad());
			map.put("processCpuTime", osBean.getProcessCpuTime());
			map.put("systemCpuLoad", osBean.getSystemCpuLoad());
			map.put("systemLoadAverage", osBean.getSystemLoadAverage());
			map.put("totalPhysicalMemorySize", osBean.getTotalPhysicalMemorySize());
			map.put("totalSwapSpaceSize", osBean.getTotalSwapSpaceSize());
			map.put("version", osBean.getVersion());
		} catch (Exception e) {
		} finally {
			this.closeConnector();
		}
		return map;
	}

	/**
	 * 获取Runtime
	 * 
	 * @return
	 */
	public Map<String, Object> getRuntime() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			this.getConnector();
			MBeanServerConnection connection = this.connector.getMBeanServerConnection();
			ObjectName objectName = new ObjectName(RUNTIME_OBJECT);
			RuntimeMXBean rtBean = MBeanServerInvocationHandler.newProxyInstance(connection, objectName,
					RuntimeMXBean.class, true);
			map.put("bootClassPath", rtBean.getBootClassPath());
			map.put("bootClassPathSupported", rtBean.isBootClassPathSupported());
			map.put("classPath", rtBean.getClassPath());
			map.put("inputArguments", rtBean.getInputArguments());
			map.put("libraryPath", rtBean.getLibraryPath());
			map.put("managementSpecVersion", rtBean.getManagementSpecVersion());
			map.put("objectName", rtBean.getObjectName().toString());
			map.put("name", rtBean.getName());
			map.put("specName", rtBean.getSpecName());
			map.put("specVendor", rtBean.getSpecVendor());
			map.put("specVersion", rtBean.getSpecVersion());
			map.put("startTime", rtBean.getStartTime());
			map.put("systemProperties", rtBean.getSystemProperties());
			map.put("uptime", rtBean.getUptime());
			map.put("vmName", rtBean.getVmName());
			map.put("vmVendor", rtBean.getVmVendor());
			map.put("vmVersion", rtBean.getVmVersion());
		} catch (Exception e) {
		} finally {
			this.closeConnector();
		}
		return map;
	}

	/**
	 * 获取Threading
	 * 
	 * @return
	 */
	@SuppressWarnings("restriction")
	public Map<String, Object> getThreading() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			this.getConnector();
			MBeanServerConnection connection = this.connector.getMBeanServerConnection();
			ObjectName objectName = new ObjectName(THREADING_OBJECT);
			ThreadMXBean rtBean = MBeanServerInvocationHandler.newProxyInstance(connection, objectName,
					ThreadMXBean.class, true);
			map.put("allThreadIds", rtBean.getAllThreadIds());
			map.put("currentThreadCpuTime", rtBean.getCurrentThreadCpuTime());
			map.put("currentThreadCpuTimeSupported", rtBean.isCurrentThreadCpuTimeSupported());
			map.put("currentThreadUserTime", rtBean.getCurrentThreadUserTime());
			map.put("daemonThreadCount", rtBean.getDaemonThreadCount());
			map.put("objectMonitorUsageSupported", rtBean.isObjectMonitorUsageSupported());
			map.put("objectName", rtBean.getObjectName().toString());
			map.put("peakThreadCount", rtBean.getPeakThreadCount());
			map.put("synchronizerUsageSupported", rtBean.isSynchronizerUsageSupported());
			map.put("threadAllocatedMemoryEnabled", rtBean.isThreadAllocatedMemoryEnabled());
			map.put("threadAllocatedMemorySupported", rtBean.isThreadAllocatedMemorySupported());
			map.put("threadContentionMonitoringEnabled", rtBean.isThreadContentionMonitoringEnabled());
			map.put("threadContentionMonitoringSupported", rtBean.isThreadContentionMonitoringSupported());
			map.put("threadCount", rtBean.getThreadCount());
			map.put("threadCpuTimeEnabled", rtBean.isThreadCpuTimeEnabled());
			map.put("threadCpuTimeSupported", rtBean.isThreadCpuTimeSupported());
			map.put("totalStartedThreadCount", rtBean.getTotalStartedThreadCount());
		} catch (Exception e) {
		} finally {
			this.closeConnector();
		}
		return map;
	}

	// region 获取ActiveMQ Broker信息
	/**
	 * 获取ActiveMQ Broker信息
	 * 
	 * @param brokerName
	 * @return
	 */
	public Map<String, Object> getActiveMQBroker(String brokerName) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			this.getConnector();
			MBeanServerConnection connection = this.connector.getMBeanServerConnection();
			ObjectName objectName = new ObjectName(String.format(ACTIVEMQ_BROKER_FORMAT, brokerName));
			BrokerViewMBean bvBean = MBeanServerInvocationHandler.newProxyInstance(connection, objectName,
					BrokerViewMBean.class, true);
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
			this.closeConnector();
		}
		return map;
	}
	// endregion

	/**
	 * 获取ActiveMQ健康状况
	 * 
	 * @param brokerName
	 * 
	 * @return
	 */
	public Map<String, Object> getActiveMQHealth(String brokerName) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			this.getConnector();
			MBeanServerConnection connection = this.connector.getMBeanServerConnection();
			ObjectName objectName = new ObjectName(String.format(ACTIVEMQ_HEALTH_FORMAT, brokerName));
			HealthViewMBean hvBean = MBeanServerInvocationHandler.newProxyInstance(connection, objectName,
					HealthViewMBean.class, true);
			map.put("currentStatus", hvBean.getCurrentStatus());
		} catch (Exception e) {
		} finally {
			this.closeConnector();
		}
		return map;
	}

	// region 获取ActiveMQ Queue信息
	/**
	 * 获取ActiveMQ Queue信息
	 * 
	 * @param brokerName
	 * @param queueName
	 * @return
	 */
	public Map<String, Object> getActiveMQQueue(String brokerName, String queueName) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			this.getConnector();
			MBeanServerConnection connection = this.connector.getMBeanServerConnection();
			ObjectName objectName = new ObjectName(String.format(ACTIVEMQ_QUEUE_FORMAT, brokerName, queueName));
			QueueViewMBean qBean = MBeanServerInvocationHandler.newProxyInstance(connection, objectName,
					QueueViewMBean.class, true);
			map.put("alwaysRetroactive", qBean.isAlwaysRetroactive());
			map.put("averageBlockedTime", qBean.getAverageBlockedTime());
			map.put("averageEnqueueTime", qBean.getAverageEnqueueTime());
			map.put("averageMessageSize", qBean.getAverageMessageSize());
			map.put("blockedProducerWarningInterval", qBean.getBlockedProducerWarningInterval());
			map.put("blockedSends", qBean.getBlockedSends());
			map.put("cacheEnabled", qBean.isCacheEnabled());
			map.put("consumerCount", qBean.getConsumerCount());
			map.put("cursorFull", qBean.isCursorFull());
			map.put("cursorMemoryUsage", qBean.getCursorMemoryUsage());
			map.put("cursorPercentUsage", qBean.getCursorPercentUsage());
			map.put("dlq", qBean.isDLQ());
			map.put("dequeueCount", qBean.getDequeueCount());
			map.put("dispatchCount", qBean.getDispatchCount());
			map.put("enqueueCount", qBean.getEnqueueCount());
			map.put("expiredCount", qBean.getExpiredCount());
			map.put("forwardCount", qBean.getForwardCount());
			map.put("inFlightCount", qBean.getInFlightCount());
			map.put("maxAuditDepth", qBean.getMaxAuditDepth());
			map.put("maxEnqueueTime", qBean.getMaxEnqueueTime());
			map.put("maxMessageSize", qBean.getMaxMessageSize());
			map.put("maxPageSize", qBean.getMaxPageSize());
			map.put("maxProducersToAudit", qBean.getMaxProducersToAudit());
			map.put("memoryLimit", qBean.getMemoryLimit());
			map.put("memoryPercentUsage", qBean.getMemoryPercentUsage());
			map.put("memoryUsageByteCount", qBean.getMemoryUsageByteCount());
			map.put("memoryUsagePortion", qBean.getMemoryUsagePortion());
			map.put("messageGroupType", qBean.getMessageGroupType());
			map.put("messageGroups", qBean.getMessageGroups());
			map.put("minEnqueueTime", qBean.getMinEnqueueTime());
			map.put("minMessageSize", qBean.getMinMessageSize());
			map.put("name", qBean.getName());
			map.put("options", qBean.getOptions());
			map.put("paused", qBean.isPaused());
			map.put("prioritizedMessages", qBean.isPrioritizedMessages());
			map.put("producerCount", qBean.getProducerCount());
			map.put("producerFlowControl", qBean.isProducerFlowControl());
			map.put("queueSize", qBean.getQueueSize());
			map.put("slowConsumerStrategy", qBean.getSlowConsumerStrategy());
			map.put("storeMessageSize", qBean.getStoreMessageSize());
			map.put("subscriptions", qBean.getSubscriptions());
			map.put("totalBlockedTime", qBean.getTotalBlockedTime());
			map.put("useCache", qBean.isUseCache());
		} catch (Exception e) {
		} finally {
			this.closeConnector();
		}
		return map;
	}
	// endregion

	// region 获取ActiveMQ Topic信息
	/**
	 * 获取ActiveMQ Topic信息
	 * 
	 * @param brokerName
	 * @param topicName
	 * @return
	 */
	public Map<String, Object> getActiveMQTopic(String brokerName, String topicName) {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			this.getConnector();
			MBeanServerConnection connection = this.connector.getMBeanServerConnection();
			ObjectName objectName = new ObjectName(String.format(ACTIVEMQ_TOPIC_FORMAT, brokerName, topicName));
			TopicViewMBean qBean = MBeanServerInvocationHandler.newProxyInstance(connection, objectName,
					TopicViewMBean.class, true);
			map.put("alwaysRetroactive", qBean.isAlwaysRetroactive());
			map.put("averageBlockedTime", qBean.getAverageBlockedTime());
			map.put("averageEnqueueTime", qBean.getAverageEnqueueTime());
			map.put("averageMessageSize", qBean.getAverageMessageSize());
			map.put("blockedProducerWarningInterval", qBean.getBlockedProducerWarningInterval());
			map.put("blockedSends", qBean.getBlockedSends());
			map.put("consumerCount", qBean.getConsumerCount());
			map.put("dlq", qBean.isDLQ());
			map.put("dequeueCount", qBean.getDequeueCount());
			map.put("dispatchCount", qBean.getDispatchCount());
			map.put("enqueueCount", qBean.getEnqueueCount());
			map.put("expiredCount", qBean.getExpiredCount());
			map.put("forwardCount", qBean.getForwardCount());
			map.put("inFlightCount", qBean.getInFlightCount());
			map.put("maxAuditDepth", qBean.getMaxAuditDepth());
			map.put("maxEnqueueTime", qBean.getMaxEnqueueTime());
			map.put("maxMessageSize", qBean.getMaxMessageSize());
			map.put("maxPageSize", qBean.getMaxPageSize());
			map.put("maxProducersToAudit", qBean.getMaxProducersToAudit());
			map.put("memoryLimit", qBean.getMemoryLimit());
			map.put("memoryPercentUsage", qBean.getMemoryPercentUsage());
			map.put("memoryUsageByteCount", qBean.getMemoryUsageByteCount());
			map.put("memoryUsagePortion", qBean.getMemoryUsagePortion());
			map.put("minEnqueueTime", qBean.getMinEnqueueTime());
			map.put("minMessageSize", qBean.getMinMessageSize());
			map.put("name", qBean.getName());
			map.put("options", qBean.getOptions());
			map.put("prioritizedMessages", qBean.isPrioritizedMessages());
			map.put("producerCount", qBean.getProducerCount());
			map.put("producerFlowControl", qBean.isProducerFlowControl());
			map.put("queueSize", qBean.getQueueSize());
			map.put("slowConsumerStrategy", qBean.getSlowConsumerStrategy());
			map.put("storeMessageSize", qBean.getStoreMessageSize());
			map.put("subscriptions", qBean.getSubscriptions());
			map.put("totalBlockedTime", qBean.getTotalBlockedTime());
			map.put("useCache", qBean.isUseCache());
		} catch (Exception e) {
		} finally {
			this.closeConnector();
		}
		return map;
	}
	// endregion
}
