package lm.com.configurer.mq.kafka.producer.boot;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@EnableKafka
@Configuration
public class ProducerConfigurer {
	@Value("${kafka.producer.servers}")
	private String servers;
	@Value("${kafka.producer.retries}")
	private int retries;
	@Value("${kafka.producer.batch.size}")
	private int batchSize;
	@Value("${kafka.producer.linger}")
	private int linger;
	@Value("${kafka.producer.buffer.memory}")
	private int bufferMemory;

	/**
	 * 生产者配置
	 * 
	 * @return
	 */
	private Map<String, Object> producerConfig() {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
		props.put(ProducerConfig.RETRIES_CONFIG, retries);
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
		props.put(ProducerConfig.LINGER_MS_CONFIG, linger);
		props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
		return props;
	}

	/**
	 * 生产者工厂
	 * 
	 * @return
	 */
	private ProducerFactory<byte[], byte[]> producerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfig());
	}

	/**
	 * KafkaTemplate
	 * 
	 * @return
	 */
	@Bean
	public KafkaTemplate<byte[], byte[]> kafkaTemplate() {
		return new KafkaTemplate<byte[], byte[]>(producerFactory());
	}
}
