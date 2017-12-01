/**
 * @title RedisConfigurer.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.mixplat.ui.core
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年3月21日下午4:03:38
 * @version v1.0.0
 */
package lm.com.configurer.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

/**
 * @ClassName: RedisConfigurer
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年3月21日 下午4:03:38
 * 
 */
@Configuration
public class RedisConfigurer {
	@Value("${redis.pool.maxTotal:200}")
	private int maxTotal;
	@Value("${redis.pool.maxIdle:50}")
	private int maxIdle;
	@Value("${redis.pool.minIdle:10}")
	private int minIdle;
	@Value("${redis.pool.maxWaitMillis:20000}")
	private long maxWaitMillis;
	@Value("${redis.pool.testOnBorrow:true}")
	private boolean testOnBorrow;
	@Value("${redis.pool.testOnReturn:true}")
	private boolean testOnReturn;

	@Value("${redis.hostName:127.0.0.1}")
	private String hostName;
	@Value("${redis.port:6379}")
	private int port;
	@Value("${redis.password}")
	private String password = "";
	@Value("${redis.timeout:100000}")
	private int timeout;
	@Value("${redis.database:0}")
	private int database;
	@Value("${redis.cache.expiration:86400}")
	private int expiration;

	/**
	 * jedis连接池
	 * 
	 * @return
	 */
	@Bean
	public JedisPoolConfig jedisPoolConfig() {
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxTotal(maxTotal);
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setMinIdle(minIdle);
		jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
		jedisPoolConfig.setTestOnBorrow(testOnBorrow);
		jedisPoolConfig.setTestOnReturn(testOnReturn);
		return jedisPoolConfig;
	}

	/**
	 * jedis连接工厂
	 * 
	 * @return
	 */
	@Bean
	public JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
		jedisConnectionFactory.setPoolConfig(this.jedisPoolConfig());
		jedisConnectionFactory.setUsePool(true);
		jedisConnectionFactory.setHostName(hostName);
		jedisConnectionFactory.setPort(port);
		jedisConnectionFactory.setPassword(password);
		jedisConnectionFactory.setTimeout(timeout);
		jedisConnectionFactory.setDatabase(database);
		return jedisConnectionFactory;
	}

	/**
	 * redisTemplate
	 * 
	 * @return
	 */
	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
		redisTemplate.setConnectionFactory(this.jedisConnectionFactory());
		redisTemplate.setKeySerializer(this.stringRedisSerializer());
		redisTemplate.setValueSerializer(this.jdkSerializationRedisSerializer());
		redisTemplate.setHashKeySerializer(this.stringRedisSerializer());
		redisTemplate.setHashValueSerializer(this.jdkSerializationRedisSerializer());
		return redisTemplate;
	}

	@Bean
	public StringRedisSerializer stringRedisSerializer() {
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		return stringRedisSerializer;
	}

	@Bean
	public JdkSerializationRedisSerializer jdkSerializationRedisSerializer() {
		JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
		return jdkSerializationRedisSerializer;
	}

	/**
	 * 
	 * @return
	 */
	@Bean
	public RedisCacheManager redisCacheManager() {
		RedisCacheManager cacheManager = new RedisCacheManager(this.redisTemplate());
		cacheManager.setDefaultExpiration(this.expiration);
		return cacheManager;
	}
}
