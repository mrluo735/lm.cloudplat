/**
 * @title StorageConfigurer.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.cloud.storage.server.configurer
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年6月14日下午6:36:24
 * @version v1.0.0
 */
package lm.cloud.storage.server.configurer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lm.com.framework.id.IdWorker;

/**
 * @ClassName: StorageConfigurer
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年6月14日 下午6:36:24
 * 
 */
@Configuration
public class StorageConfigurer {
	/**
	 * id生成器
	 * 
	 * @return
	 */
	@Bean
	public IdWorker idWorker() {
		IdWorker idWorker = new IdWorker(1, 1);
		return idWorker;
	}
}
