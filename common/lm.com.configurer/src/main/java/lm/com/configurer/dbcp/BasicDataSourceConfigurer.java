/**
 * @title BasicDataSourceConfigurer.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.pro.secureshell.cloud.provider
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年3月10日上午11:05:16
 * @version v1.0.0
 */
package lm.com.configurer.dbcp;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 基础数据源
 * 
 * @ClassName: BasicDataSourceConfigurer
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年3月10日 上午11:05:16
 * 
 */
@Configuration
public class BasicDataSourceConfigurer {
	@Value("${jdbc.driver}")
	private String driver = "";
	@Value("${jdbc.url}")
	private String url = "";
	@Value("${jdbc.username}")
	private String username = "";
	@Value("${jdbc.password}")
	private String password = "";
	@Value("${jdbc.initialSize}")
	private int initialSize = 0;
	@Value("${jdbc.maxActive}")
	private int maxActive = 100;
	@Value("${jdbc.maxIdle}")
	private int maxIdle = 20;
	@Value("${jdbc.minIdle}")
	private int minIdle = 1;
	@Value("${jdbc.maxWait}")
	private long maxWait = 60;

	/**
	 * 
	 * @return
	 */
	@Bean(name = "basicDataSource", destroyMethod = "close")
	public DataSource basicDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(driver);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		// 初始化连接大小
		dataSource.setInitialSize(initialSize);
		// 连接池最大数量
		dataSource.setMaxActive(maxActive);
		// 连接池最大空闲
		dataSource.setMaxIdle(maxIdle);
		// 连接池最小空闲
		dataSource.setMinIdle(minIdle);
		// 获取连接最大等待时间
		dataSource.setMaxWait(maxWait);
		return dataSource;
	}
}
