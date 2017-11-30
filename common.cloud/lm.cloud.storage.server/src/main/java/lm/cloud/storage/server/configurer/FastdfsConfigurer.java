/**
 * @title FastdfsConfigurer.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.cloud.storage.server.configurer
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年6月13日下午3:29:12
 * @version v1.0.0
 */
package lm.cloud.storage.server.configurer;

import java.io.IOException;

import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lm.com.framework.JavaUtil;

/**
 * @ClassName: FastdfsConfigurer
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年6月13日 下午3:29:12
 * 
 */
@Configuration
public class FastdfsConfigurer {
	private static final Logger logger = LoggerFactory.getLogger(FastdfsConfigurer.class);

	/**
	 * 初始化Fastdfs
	 */
	public static void initFastdfs() {
		try {
			ClientGlobal.initByProperties("fastdfs-client.properties");
			logger.info("※※※※※※※※※※※※※※※ Fastdfs 初始化成功 ※※※※※※※※※※※※※※※" + JavaUtil.getLineSeparator()
					+ ClientGlobal.configInfo());
		} catch (IOException | MyException e) {
			logger.error("Fastdfs 初始化失败！失败原因: {}", e);
		}
	}

	/**
	 * 
	 * @return
	 */
	@Bean
	public TrackerClient trackerClient() {
		return new TrackerClient();
	}

	/**
	 * 
	 * @return
	 */
	@Bean
	public TrackerServer trackerServer() {
		try {
			TrackerServer trackerServer = this.trackerClient().getConnection();
			return trackerServer;
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 
	 * @return
	 */
	// @Bean
	// public StorageServer storageServer() {
	// }

	/**
	 * 
	 * @return
	 */
	@Bean
	public StorageClient storageClient() {
		StorageClient storageClient = new StorageClient(this.trackerServer(), null);
		return storageClient;
	}
}
