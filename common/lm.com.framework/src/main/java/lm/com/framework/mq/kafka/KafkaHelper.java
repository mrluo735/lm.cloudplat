package lm.com.framework.mq.kafka;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kafka帮助类
 * 
 * @author mrluo735
 *
 */
public class KafkaHelper {
	private static final Logger logger = LoggerFactory.getLogger(KafkaHelper.class);

	private ProducerConfig producerConfig;
	private ConsumerConfig consumerConfig;

	/**
	 * 连接服务器
	 * 
	 * @return
	 */
	public boolean connect() {
		KafkaProducer<String, String> producer = null;
		try {
			producerConfig.setKeySerializer(StringSerializer.class.getName());
			producerConfig.setValueSerializer(StringSerializer.class.getName());
			producer = new KafkaProducer<String, String>(producerConfig.toMap());
			List<PartitionInfo> list = producer.partitionsFor(BaseConfig.TOPIC_OFFSET_CONSUMER);
			return list != null ? true : false;
		} catch (Exception ex) {
			logger.error("Kafka连接失败！错误原因: {}", ex);
		} finally {
			if (producer != null)
				producer.close();
		}
		return false;
	}

	/**
	 * 重载+1 同步发送消息
	 * 
	 * @param topicName
	 * @param key
	 * @param value
	 * @return
	 */
	public long sendSync(String topicName, String value) {
		this.producerConfig.setKeySerializer(StringSerializer.class.getName());
		this.producerConfig.setValueSerializer(StringSerializer.class.getName());
		return this.<String, String>send(topicName, null, value, null);
	}

	/**
	 * 重载+2 同步发送消息
	 * 
	 * @param topicName
	 * @param key
	 * @param value
	 * @return
	 */
	public long sendSync(String topicName, byte[] value) {
		this.producerConfig.setKeySerializer(ByteArraySerializer.class.getName());
		this.producerConfig.setValueSerializer(ByteArraySerializer.class.getName());
		return this.<byte[], byte[]>send(topicName, null, value, null);
	}

	/**
	 * 重载+1 异步发送消息
	 * 
	 * @param topicName
	 * @param key
	 * @param value
	 * @return
	 */
	public long sendAsync(String topicName, String value, Callback callback) {
		this.producerConfig.setKeySerializer(StringSerializer.class.getName());
		this.producerConfig.setValueSerializer(StringSerializer.class.getName());
		return this.<String, String>send(topicName, null, value, callback);
	}

	/**
	 * 重载+2 异步发送消息
	 * 
	 * @param topicName
	 * @param key
	 * @param value
	 * @return
	 */
	public long sendAsync(String topicName, byte[] value, Callback callback) {
		this.producerConfig.setKeySerializer(ByteArraySerializer.class.getName());
		this.producerConfig.setValueSerializer(ByteArraySerializer.class.getName());
		return this.<byte[], byte[]>send(topicName, null, value, callback);
	}

	/**
	 * 发送消息
	 * 
	 * @param topicName
	 * @param key
	 * @param value
	 * @param callback
	 * @return
	 */
	public <K, V> long send(String topicName, K key, V value, Callback callback) {
		KafkaProducer<K, V> producer = null;
		Future<RecordMetadata> futrue = null;
		try {
			producer = new KafkaProducer<K, V>(producerConfig.toMap());
			ProducerRecord<K, V> record = new ProducerRecord<K, V>(topicName, key, value);
			if (callback != null)
				futrue = producer.send(record, callback);
			else
				futrue = producer.send(record);
			return futrue.get() == null ? 0L : futrue.get().offset();
		} catch (Exception ex) {
			logger.error("消息发送失败！错误原因: {}", ex);
		} finally {
			if (producer != null)
				producer.close();
		}
		return 0L;
	}

	/**
	 * 接收消息
	 * 
	 * @param topicName
	 * @param key
	 * @param value
	 * @return
	 */
	public ConsumerRecords<String, String> receiveString(String topicName) {
		consumerConfig.setKeyDeserializer(StringDeserializer.class.getName());
		consumerConfig.setValueDeserializer(StringDeserializer.class.getName());
		return this.receive(Arrays.asList(topicName));
	}

	/**
	 * 接收消息
	 * 
	 * @param topicName
	 * @param key
	 * @param value
	 * @return
	 */
	public ConsumerRecords<byte[], byte[]> receiveBytes(String topicName) {
		consumerConfig.setKeyDeserializer(ByteArrayDeserializer.class.getName());
		consumerConfig.setValueDeserializer(ByteArrayDeserializer.class.getName());
		return this.receive(Arrays.asList(topicName));
	}

	/**
	 * 接收消息
	 * 
	 * @param topicNames
	 * @param key
	 * @param value
	 * @return
	 */
	public <K, V> ConsumerRecords<K, V> receive(List<String> topicNames) {
		KafkaConsumer<K, V> consumer = null;
		try {
			consumer = new KafkaConsumer<K, V>(consumerConfig.toMap());
			consumer.subscribe(topicNames);
			ConsumerRecords<K, V> records = consumer.poll(consumerConfig.getSessionTimeoutMs());
			return records;
		} catch (Exception ex) {
			logger.error("消息发送失败！错误原因: {}", ex);
		} finally {
			if (consumer != null)
				consumer.close();
		}
		return null;
	}
}
