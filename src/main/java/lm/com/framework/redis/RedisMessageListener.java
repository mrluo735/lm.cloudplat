/**
 * @title RedisMessageListener.java
 * @description TODO
 * @package lm.com.framework.redis
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年12月7日下午6:09:54
 * @version v1.0
 */
package lm.com.framework.redis;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * redis消息监听器
 * <p>
 * 实现基于redis的订阅发布
 * </p>
 * 
 * @author mrluo735
 *
 */
public class RedisMessageListener implements MessageListener {
	private static final Logger logger = LoggerFactory.getLogger(RedisMessageListener.class);

	private RedisTemplate<Serializable, Serializable> redisTemplate;

	/**
	 * 设置redisTemplate
	 * 
	 * @param redisTemplate
	 */
	public void setRedisTemplate(RedisTemplate<Serializable, Serializable> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	/**
	 * 接收消息
	 */
	public void onMessage(Message message, byte[] pattern) {
		byte[] body = message.getBody();// 请使用valueSerializer
		byte[] channel = message.getChannel();

		// key必须为StringSerializer。和redisTemplate.convertAndSend对应
		String msgContent = (String) redisTemplate.getValueSerializer().deserialize(body);
		String topic = (String) redisTemplate.getStringSerializer().deserialize(channel);
		
		logger.info(topic + ":" + msgContent);
		
	}
}
