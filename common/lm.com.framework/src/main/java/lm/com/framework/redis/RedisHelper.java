/**
 * @title RedisHelper.java
 * @description TODO
 * @package lm.com.framework.redis
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年11月4日上午9:05:41
 * @version v1.0
 */
package lm.com.framework.redis;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * @author mrluo735
 *
 */
@Component
@SuppressWarnings("all")
public class RedisHelper {
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	/**
	 * 存
	 * 
	 * @param key
	 * @param t
	 * @param seconds
	 */
	public <T extends Serializable> void set(final String key, final T t, final long seconds) {
		redisTemplate.execute(new RedisCallback<Object>() {
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer redisSerializer = redisTemplate.getKeySerializer();
				byte[] keyBytes = redisSerializer.serialize(key);
				RedisSerializer valueSerializer = redisTemplate.getValueSerializer();
				connection.set(keyBytes, valueSerializer.serialize(t));
				if (seconds > 0)
					connection.expire(key.getBytes(), seconds);
				return null;
			}
		});
	}

	/**
	 * 取
	 * 
	 * @param key
	 * @return
	 */
	public <T extends Serializable> T get(final String key) {
		return (T) redisTemplate.execute(new RedisCallback<T>() {
			public T doInRedis(RedisConnection connection) throws DataAccessException {
				if (connection.exists(key.getBytes())) {
					RedisSerializer redisSerializer = redisTemplate.getKeySerializer();
					byte[] keyBytes = redisSerializer.serialize(key);
					byte[] value = connection.get(keyBytes);
					return (T) redisTemplate.getValueSerializer().deserialize(value);
				}
				return null;
			}
		});
	}

	/**
	 * 删除
	 * 
	 * @param redisTemplate
	 * @param key
	 * @return
	 */
	public void delete(final String key) {
		redisTemplate.delete(key);
	}

	/**
	 * 删除
	 * 
	 * @param keys
	 * @return
	 */
	public void delete(final String... keys) {
		redisTemplate.delete(CollectionUtils.arrayToList(keys));
	}

	/**
	 * 存hash
	 * 
	 * @param key
	 * @param t
	 */
	public <T extends Serializable> void putForHash(final String key, final T t) {
		redisTemplate.opsForHash().put(key, key, t);
	}

	/**
	 * 取hash
	 * 
	 * @param key
	 * @return
	 */
	public <T extends Serializable> T getForHash(final String key) {
		return (T) redisTemplate.opsForHash().get(key, key);
	}

	/**
	 * 删除hash
	 * 
	 * @param key
	 */
	public void deleteForHash(final String key) {
		redisTemplate.opsForHash().delete(key, key);
	}
}
