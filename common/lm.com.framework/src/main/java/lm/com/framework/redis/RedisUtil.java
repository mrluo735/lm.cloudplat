/**
 * @title RedisUtil.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.com.framework.redis
 * @author mrluo735
 * @since JDK1.8
 * @date 2017年6月29日下午2:25:00
 * @version v1.0.0
 */
package lm.com.framework.redis;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.CollectionUtils;

/**
 * @ClassName: RedisUtil
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年6月29日 下午2:25:00
 * 
 */
public class RedisUtil {
	/**
	 * 重载+1 设置缓存值
	 * 
	 * @param redisTemplate
	 * @param key
	 * @param value
	 */
	@SuppressWarnings(value = { "rawtypes", "unchecked" })
	public static <K extends Serializable, V> void set(final RedisTemplate<K, V> redisTemplate, final K key,
			final V value) {
		redisTemplate.execute(new RedisCallback<Object>() {
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer redisSerializer = redisTemplate.getKeySerializer();
				byte[] keyBytes = redisSerializer.serialize(key);
				RedisSerializer valueSerializer = redisTemplate.getValueSerializer();
				connection.set(keyBytes, valueSerializer.serialize(value));
				return null;
			}
		});
	}

	/**
	 * 重载+2 设置缓存值
	 * 
	 * @param redisTemplate
	 * @param key
	 * @param value
	 * @param seconds
	 *            过期时间（单位：秒）
	 */
	@SuppressWarnings(value = { "rawtypes", "unchecked" })
	public static <K extends Serializable, V> void set(final RedisTemplate<K, V> redisTemplate, final K key,
			final V value, final long seconds) {
		redisTemplate.execute(new RedisCallback<Object>() {
			public Object doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer redisSerializer = redisTemplate.getKeySerializer();
				byte[] keyBytes = redisSerializer.serialize(key);
				RedisSerializer valueSerializer = redisTemplate.getValueSerializer();
				connection.set(keyBytes, valueSerializer.serialize(value));
				if (seconds > 0)
					connection.expire(keyBytes, seconds);
				return null;
			}
		});
	}

	/**
	 * 获取缓存值
	 * 
	 * @param redisTemplate
	 * @param key
	 * @return
	 */
	@SuppressWarnings(value = { "rawtypes", "unchecked" })
	public static <K extends Serializable, V> V get(final RedisTemplate<K, V> redisTemplate, final K key) {
		return redisTemplate.execute(new RedisCallback<V>() {
			public V doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer redisSerializer = redisTemplate.getKeySerializer();
				byte[] keyBytes = redisSerializer.serialize(key);
				if (connection.exists(keyBytes)) {
					byte[] value = connection.get(keyBytes);
					return (V) redisTemplate.getValueSerializer().deserialize(value);
				}
				return null;
			}
		});
	}

	/**
	 * 重载+1 删除缓存
	 * 
	 * @param redisTemplate
	 * @param key
	 * @return
	 */
	public static <K extends Serializable, V> void delete(final RedisTemplate<K, V> redisTemplate, final K key) {
		redisTemplate.delete(key);
	}

	/**
	 * 重载+2 删除缓存
	 * 
	 * @param redisTemplate
	 * @param keys
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <K extends Serializable, V> void delete(final RedisTemplate<K, V> redisTemplate, final K... keys) {
		redisTemplate.delete(CollectionUtils.arrayToList(keys));
	}

	/**
	 * 判断键是否存在
	 * 
	 * @param redisTemplate
	 * @param key
	 * @return
	 */
	@SuppressWarnings(value = { "rawtypes", "unchecked" })
	public static <K extends Serializable, V> boolean exists(final RedisTemplate<K, V> redisTemplate, final K key) {
		return redisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer redisSerializer = redisTemplate.getKeySerializer();
				byte[] keyBytes = redisSerializer.serialize(key);
				return connection.exists(keyBytes);
			}
		});
	}

	/**
	 * 设置过期
	 * 
	 * @param redisTemplate
	 * @param key
	 * @param timeout
	 *            过期时间
	 * @param timeUnit
	 *            指定timeout的单位
	 * @return
	 */
	public static <K extends Serializable, V> boolean expire(final RedisTemplate<K, V> redisTemplate, final K key,
			long timeout, TimeUnit timeUnit) {
		return redisTemplate.expire(key, timeout, timeUnit);
	}

	/**
	 * 设置过期
	 * 
	 * @param redisTemplate
	 * @param key
	 * @param date
	 *            过期时间
	 * @return
	 */
	public static <K extends Serializable, V> boolean expireAt(final RedisTemplate<K, V> redisTemplate, final K key,
			Date date) {
		return redisTemplate.expireAt(key, date);
	}

	/**
	 * 存hash
	 * 
	 * @param redisTemplate
	 * @param key
	 * @param hashKey
	 * @param value
	 */
	public static <K extends Serializable, V> void putForHash(final RedisTemplate<K, V> redisTemplate, final K key,
			Object hashKey, final V value) {
		redisTemplate.opsForHash().put(key, hashKey, value);
	}

	/**
	 * 取hash
	 * 
	 * @param key
	 * @param hashKey
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <K extends Serializable, V> V getForHash(final RedisTemplate<K, V> redisTemplate, final K key,
			Object hashKey) {
		return (V) redisTemplate.opsForHash().get(key, hashKey);
	}

	/**
	 * 删除hash
	 * 
	 * @param key
	 * @param hashKey
	 */
	public static <K extends Serializable, V> void deleteForHash(final RedisTemplate<K, V> redisTemplate, final K key,
			Object hashKey) {
		redisTemplate.opsForHash().delete(key, hashKey);
	}
}
