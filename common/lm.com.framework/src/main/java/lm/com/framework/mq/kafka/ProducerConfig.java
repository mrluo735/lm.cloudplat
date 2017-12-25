package lm.com.framework.mq.kafka;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import lm.com.framework.ReflectUtil;
import lm.com.framework.StringUtil;
import lm.com.framework.annotation.Value;

/**
 * 生产者配置
 * <p>
 * 基于0.9.0.x
 * </p>
 * 
 * @author mrluo735
 *
 */
public class ProducerConfig extends BaseConfig {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2366906572892394440L;

	/**
	 * key的序列化方式，若是没有设置，同Serializer.class
	 */
	@Value("key.serializer")
	private String keySerializer;

	/**
	 * value的序列化方式，若是没有设置，同Serializer.class
	 */
	@Value("value.serializer")
	private String valueSerializer;

	/**
	 * producer需要server接收到数据之后发出的确认接收的信号，此项配置就是指procuder需要多少个这样的确认信号。
	 * 此配置实际上代表了数据备份的可用性。以下设置为常用选项：
	 * （1）acks=0：设置为0表示producer不需要等待任何确认收到的信息。副本将立即加到socket
	 * buffer并认为已经发送。没有任何保障可以保证此种情况下server已经成功接收数据，
	 * 同时重试配置不会发生作用（因为客户端不知道是否失败）回馈的offset会总是设置为-1；
	 * （2）acks=1：这意味着至少要等待leader已经成功将数据写入本地log，但是并没有等待所有follower是否成功写入。
	 * 这种情况下，如果follower没有成功备份数据，而此时leader又挂掉，则消息会丢失。
	 * （3）acks=all：这意味着leader需要等待所有备份都成功写入日志，这种策略会保证只要有一个备份存活就不会丢失数据。这是最强的保证。
	 */
	@Value("acks")
	private String acks = "1";

	/**
	 * producer可以用来缓存数据的内存大小。如果数据产生速度大于向broker发送的速度，producer会阻塞或者抛出异常，
	 * 以“block.on.buffer.full”来表明。这项设置将和producer能够使用的总内存相关，但并不是一个硬性的限制，
	 * 因为不是producer使用的所有内存都是用于缓存。一些额外的内存会用于压缩（如果引入压缩机制），同样还有一些用于维护请求。
	 */
	@Value("buffer.memory")
	private long bufferMemory = 33554432;

	/**
	 * producer用于压缩数据的压缩类型。默认是无压缩。正确的选项值是none、gzip、snappy。
	 * 压缩最好用于批量处理，批量处理消息越多，压缩性能越好
	 */
	@Value("compression.type")
	private String compressionType = "none";

	/**
	 * 设置大于0的值将使客户端重新发送任何数据，一旦这些数据发送失败。
	 * 注意，这些重试与客户端接收到发送错误时的重试没有什么不同。
	 * 允许重试将潜在的改变数据的顺序，如果这两个消息记录都是发送到同一个partition，
	 * 则第一个消息失败第二个发送成功，则第二条消息会比第一条消息出现要早。
	 */
	@Value("retries")
	private int retries = 0;

	/**
	 * producer将试图批处理消息记录，以减少请求次数。这将改善client与server之间的性能。
	 * 这项配置控制默认的批量处理消息字节数。不会试图处理大于这个字节数的消息字节数。
	 * 发送到brokers的请求将包含多个批量处理，其中会包含对每个partition的一个请求。
	 * 较小的批量处理数值比较少用，并且可能降低吞吐量（0则会仅用批量处理）。
	 * 较大的批量处理数值将会浪费更多内存空间，这样就需要分配特定批量处理数值的内存大小。
	 */
	@Value("batch.size")
	private int batchSize = 16384;

	/**
	 * producer组将会汇总任何在请求与发送之间到达的消息记录一个单独批量的请求。
	 * 通常来说，这只有在记录产生速度大于发送速度的时候才能发生。
	 * 然而，在某些条件下，客户端将希望降低请求的数量，甚至降低到中等负载一下。
	 * 这项设置将通过增加小的延迟来完成–即，不是立即发送一条记录，
	 * producer将会等待给定的延迟时间以允许其他消息记录发送，这些消息记录可以批量处理。
	 * 这可以认为是TCP种Nagle的算法类似。这项设置设定了批量处理的更高的延迟边界：
	 * 一旦我们获得某个partition的batch.size，他将会立即发送而不顾这项设置，
	 * 然而如果我们获得消息字节数比这项设置要小的多，我们需要“linger”特定的时间以获取更多的消息。 
	 * 这个设置默认为0，即没有延迟。设定linger.ms=5，例如，将会减少请求数目，但是同时会增加5ms的延迟。
	 */
	@Value("linger.ms")
	private long lingerMs = 0L;

	/**
	 * 控制block的时长,当buffer空间不够或者metadata丢失时产生block
	 */
	@Value("max.block.ms")
	private long maxBlockMs = 60000L;

	/**
	 * 请求的最大字节数。这也是对最大记录尺寸的有效覆盖。
	 * 注意：server具有自己对消息记录尺寸的覆盖，这些尺寸和这个设置不同。
	 * 此项设置将会限制producer每次批量发送请求的数目，以防发出巨量的请求。
	 */
	@Value("max.request.size")
	private int maxRequestSize = 1048576;

	/**
	 * 分区类
	 */
	@Value("partitioner.class")
	private String partitionerClass = "org.apache.kafka.clients.producer.internals.DefaultPartitioner";

	/**
	 * 此配置选项控制server等待来自followers的确认的最大时间。
	 * 如果确认的请求数目在此时间内没有实现，则会返回一个错误。这个超时限制是以server端度量的，没有包含请求的网络延迟
	 */
	@Value("timeout.ms")
	private int timeoutMs = 30000;

	/**
	 * 当我们的内存缓冲区耗尽时，我们必须停止接受新的记录（块）或抛出错误。
	 * 默认情况下，此设置是正确的，我们会阻止，但在某些情况下，阻塞是不可取的，最好立即给出一个错误。
	 * 设置为false将完成：生产者将如果recrord发送和缓冲空间全扔bufferexhaustedexception。
	 */
	@Value("block.on.buffer.full")
	private boolean blockOnBufferFull = false;

	/**
	 * kafka可以在一个connection中发送多个请求，叫作一个flight,这样可以减少开销，
	 * 但是如果产生错误，可能会造成数据的发送顺序改变,默认是5 (修改）
	 */
	@Value("max.in.flight.requests.per.connection")
	private int maxInFlightRequestsPerConnection = 5;

	/**
	 * 是指我们所获取的一些元素据的第一个时间数据。元素据包含：topic，host，partitions。
	 * 此项配置是指当等待元素据fetch成功完成所需要的时间，否则会跑出异常给客户端
	 */
	@Value("metadata.fetch.timeout.ms")
	private long metadataFetchTimeoutMs = 60000L;

	// region 属性
	/**
	 * 
	 * @return
	 */
	public String getKeySerializer() {
		return keySerializer;
	}

	/**
	 * 
	 * @param keySerializer
	 */
	public void setKeySerializer(String keySerializer) {
		this.keySerializer = keySerializer;
	}

	public String getValueSerializer() {
		return valueSerializer;
	}

	public void setValueSerializer(String valueSerializer) {
		this.valueSerializer = valueSerializer;
	}

	public String getAcks() {
		return acks;
	}

	public void setAcks(String acks) {
		this.acks = acks;
	}

	public long getBufferMemory() {
		return bufferMemory;
	}

	public void setBufferMemory(long bufferMemory) {
		this.bufferMemory = bufferMemory;
	}

	public String getCompressionType() {
		return compressionType;
	}

	public void setCompressionType(String compressionType) {
		this.compressionType = compressionType;
	}

	public int getRetries() {
		return retries;
	}

	public void setRetries(int retries) {
		this.retries = retries;
	}

	public int getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}

	public long getLingerMs() {
		return lingerMs;
	}

	public void setLingerMs(long lingerMs) {
		this.lingerMs = lingerMs;
	}

	public long getMaxBlockMs() {
		return maxBlockMs;
	}

	public void setMaxBlockMs(long maxBlockMs) {
		this.maxBlockMs = maxBlockMs;
	}

	public int getMaxRequestSize() {
		return maxRequestSize;
	}

	public void setMaxRequestSize(int maxRequestSize) {
		this.maxRequestSize = maxRequestSize;
	}

	public String getPartitionerClass() {
		return partitionerClass;
	}

	public void setPartitionerClass(String partitionerClass) {
		this.partitionerClass = partitionerClass;
	}

	public int getTimeoutMs() {
		return timeoutMs;
	}

	public void setTimeoutMs(int timeoutMs) {
		this.timeoutMs = timeoutMs;
	}

	public boolean isBlockOnBufferFull() {
		return blockOnBufferFull;
	}

	public void setBlockOnBufferFull(boolean blockOnBufferFull) {
		this.blockOnBufferFull = blockOnBufferFull;
	}

	public int getMaxInFlightRequestsPerConnection() {
		return maxInFlightRequestsPerConnection;
	}

	public void setMaxInFlightRequestsPerConnection(int maxInFlightRequestsPerConnection) {
		this.maxInFlightRequestsPerConnection = maxInFlightRequestsPerConnection;
	}

	public long getMetadataFetchTimeoutMs() {
		return metadataFetchTimeoutMs;
	}

	public void setMetadataFetchTimeoutMs(long metadataFetchTimeoutMs) {
		this.metadataFetchTimeoutMs = metadataFetchTimeoutMs;
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
