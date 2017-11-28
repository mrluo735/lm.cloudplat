/**
 * @title ElasticSearchConfigurer.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.search.cloud.server
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年6月19日上午10:54:00
 * @version v1.0.0
 */
package lm.com.configurer.elasticsearch;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: ElasticSearchConfigurer
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年6月19日 上午10:54:00
 * 
 */
@Configuration
public class ElasticSearchConfigurer {
	private static final Logger logger = LoggerFactory.getLogger(ElasticSearchConfigurer.class);

	@Value("${lm.elasticsearch.cluster.name}")
	public String clusterName;

	@Value("${lm.elasticsearch.server.host}")
	public String serverHost;

	@Value("${lm.elasticsearch.server.port:9300}")
	public int serverPort;

	/**
	 * 
	 * @return
	 */
	@Bean
	@SuppressWarnings("resource")
	public TransportClient transportClient() {
		try {
			// 设置集群名称
			Settings settings = Settings.builder().put("cluster.name", this.clusterName).build();
			// 创建client
			TransportClient transportClient = new PreBuiltTransportClient(settings).addTransportAddress(
					new InetSocketTransportAddress(InetAddress.getByName(this.serverHost), this.serverPort));
			logger.info("※※※※※※※※※※ Elastic Search连接成功！ ※※※※※※※※※※");
			return transportClient;
		} catch (UnknownHostException ex) {
			logger.info("Elastic Search连接失败！");
			throw new RuntimeException(ex);
		}
	}
}
