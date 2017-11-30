/**
 * @title StorageConfig.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.cloud.storage.server.configurer
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年6月14日上午11:33:16
 * @version v1.0.0
 */
package lm.cloud.storage.server.configurer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: StorageConfig
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年6月14日 上午11:33:16
 * 
 */
@Configuration
public class StorageConfig {
	/**
	 * 
	 */
	@Value("${lm.cloud.storage.appKey}")
	public String appKey;
	/**
	 * 
	 */
	@Value("${lm.cloud.storage.appSecret}")
	public String appSecret;
	/**
	 * 
	 */
	@Value("${lm.cloud.storage.token.validity:3600000}")
	public int tokenValidity;
	/**
	 * 存储磁盘
	 */
	@Value("${lm.cloud.storage.drive}")
	public String storageDrive;
	/**
	 * res域名
	 */
	@Value("${lm.cloud.storage.res.domain}")
	public String resDomain;
	/**
	 * 空间名
	 */
	@Value("${lm.cloud.storage.res.bucketName}")
	public String bucketName;
}
