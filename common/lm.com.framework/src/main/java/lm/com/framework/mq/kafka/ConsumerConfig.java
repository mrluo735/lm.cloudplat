package lm.com.framework.mq.kafka;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import lm.com.framework.ReflectUtil;
import lm.com.framework.StringUtil;
import lm.com.framework.annotation.Value;

/**
 * 消费者配置
 * <p>
 * 基于0.9.0.x
 * </p>
 * 
 * @author mrluo735
 *
 */
public class ConsumerConfig extends BaseConfig {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8574596023944827530L;

	/**
	 * key的反序列化方式，若是没有设置，同Deserializer.class
	 */
	@Value("key.deserializer")
	private String keyDeserializer;

	/**
	 * value的反序列化方式，若是没有设置，同Deserializer.class
	 */
	@Value("value.deserializer")
	private String valueDeserializer;

	/**
	 * 每次fetch请求时，server应该返回的最小字节数。如果没有足够的数据返回，请求会等待，直到足够的数据才会返回。
	 */
	@Value("fetch.min.bytes")
	private int fetchMinBytes = 1;

	/**
	 * 用来唯一标识consumer进程所在组的字符串，如果设置同样的group id，表示这些processes都是属于同一个consumer group
	 */
	@Value("group.id")
	private String groupId;

	/**
	 * 使用Kafka的组管理设施时，心跳到达消费者协调器之间的预期时间。
	 * 心跳被用来确保消费者的会话保持活跃，并在新的消费者加入或离开该组时促进其重新平衡。
	 * 该值必须低于session.timeout.ms，但通常应该是价值不高于1 / 3套。它可以被调整，甚至低于对照正常调整的预期时间。
	 */
	@Value("heartbeat.interval.ms")
	private int heartbeatIntervalMs = 3000;

	/**
	 * 服务器将返回的每个分区的最大数据量。用于请求的最大总内存将#分区* max.partition.fetch.bytes。
	 * 这个大小必须至少与服务器允许的最大消息大小一样大，否则生产者就可以发送比用户能获取的更大的消息。
	 * 如果出现这种情况，消费者可能会试图在某个分区上获取一条大消息。
	 */
	@Value("max.partition.fetch.bytes")
	private int maxPartitionFetchBytes = 1048576;

	/**
	 * 当使用Kafka的组管理设施时，超时用于检测失败。
	 */
	@Value("session.timeout.ms")
	private int sessionTimeoutMs = 30000;

	/**
	 * 如果Kafka没有初始偏移量，或者服务器上当前的偏移量不存在（如数据已被删除），该怎么办呢？：
	 * earliest：自动将偏移量重置为最早的偏移量。
	 * latest：自动将偏移量重置为最新的偏移量。
	 * none：如果没有为消费者组找到以前的偏移量，则向消费者抛出异常。
	 * anything else：向消费者抛出异常。
	 */
	@Value("auto.offset.reset")
	private String autoOffsetReset = "latest";

	/**
	 * 如果为true，则在后台周期性地提交消费者的偏移量。
	 */
	@Value("enable.auto.commit")
	private boolean enableAutoCommit = true;

	/**
	 * 在使用组管理时，客户机将使用分区分配策略在客户实例中分配分区所有权的类名称。
	 */
	@Value("partition.assignment.strategy")
	private String partitionAssignmentStrategy = "org.apache.kafka.clients.consumer.RangeAssignor";

	/**
	 * 以毫秒为单位的频率偏移，消费者自动提交至Kafka如果enable.auto.commit设置为true。
	 */
	@Value("auto.commit.interval.ms")
	private long autoCommitIntervalMs = 5000L;

	/**
	 * 自动检查记录消耗的CRC32。这样可以确保没有对发生的消息进行在线或磁盘损坏。
	 * 此检查增加了一些开销，因此在寻求极端性能的情况下可能会被禁用。
	 */
	@Value("check.crcs")
	private boolean checkCrcs = true;

	/**
	 * 时间服务器将块的读取请求回答之前，如果没有充分的数据立即满足给定要求的fetch.min.bytes最大值。
	 */
	@Value("fetch.max.wait.ms")
	private int fetchMaxWaitMs = 500;

	// region 属性
	/**
	 * 
	 * @return
	 */
	public String getKeyDeserializer() {
		return keyDeserializer;
	}

	/**
	 * 
	 * @param keyDeserializer
	 */
	public void setKeyDeserializer(String keyDeserializer) {
		this.keyDeserializer = keyDeserializer;
	}

	public String getValueDeserializer() {
		return valueDeserializer;
	}

	public void setValueDeserializer(String valueDeserializer) {
		this.valueDeserializer = valueDeserializer;
	}

	public int getFetchMinBytes() {
		return fetchMinBytes;
	}

	public void setFetchMinBytes(int fetchMinBytes) {
		this.fetchMinBytes = fetchMinBytes;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public int getHeartbeatIntervalMs() {
		return heartbeatIntervalMs;
	}

	public void setHeartbeatIntervalMs(int heartbeatIntervalMs) {
		this.heartbeatIntervalMs = heartbeatIntervalMs;
	}

	public int getMaxPartitionFetchBytes() {
		return maxPartitionFetchBytes;
	}

	public void setMaxPartitionFetchBytes(int maxPartitionFetchBytes) {
		this.maxPartitionFetchBytes = maxPartitionFetchBytes;
	}

	public int getSessionTimeoutMs() {
		return sessionTimeoutMs;
	}

	public void setSessionTimeoutMs(int sessionTimeoutMs) {
		this.sessionTimeoutMs = sessionTimeoutMs;
	}

	public String getAutoOffsetReset() {
		return autoOffsetReset;
	}

	public void setAutoOffsetReset(String autoOffsetReset) {
		this.autoOffsetReset = autoOffsetReset;
	}

	public boolean isEnableAutoCommit() {
		return enableAutoCommit;
	}

	public void setEnableAutoCommit(boolean enableAutoCommit) {
		this.enableAutoCommit = enableAutoCommit;
	}

	public String getPartitionAssignmentStrategy() {
		return partitionAssignmentStrategy;
	}

	public void setPartitionAssignmentStrategy(String partitionAssignmentStrategy) {
		this.partitionAssignmentStrategy = partitionAssignmentStrategy;
	}

	public long getAutoCommitIntervalMs() {
		return autoCommitIntervalMs;
	}

	public void setAutoCommitIntervalMs(long autoCommitIntervalMs) {
		this.autoCommitIntervalMs = autoCommitIntervalMs;
	}

	public boolean isCheckCrcs() {
		return checkCrcs;
	}

	public void setCheckCrcs(boolean checkCrcs) {
		this.checkCrcs = checkCrcs;
	}

	public int getFetchMaxWaitMs() {
		return fetchMaxWaitMs;
	}

	public void setFetchMaxWaitMs(int fetchMaxWaitMs) {
		this.fetchMaxWaitMs = fetchMaxWaitMs;
	}
	// endregion

	/**
	 * 转换成Map
	 * 
	 * @return
	 */
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] fieldArray = ReflectUtil.getDeclaredFields(this);
		if (fieldArray == null || fieldArray.length < 1)
			return map;

		for (Field field : fieldArray) {
			Value valueAnno = field.getAnnotation(Value.class);
			if (valueAnno == null || StringUtil.isNullOrWhiteSpace(valueAnno.value()))
				continue;

			Object value = ReflectUtil.getValueByFieldName(this, field.getName());
			if (value != null)
				map.put(valueAnno.value(), value);
		}
		return map;
	}

	/**
	 * 转成Properties
	 * 
	 * @return
	 */
	public Properties toProperties() {
		Properties properties = new Properties();
		Map<String, Object> map = this.toMap();
		properties.putAll(map);
		return properties;
	}
}
