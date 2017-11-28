/**
 * @title JdbcConfigurer.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.cloud.oauth.server.configurer
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年5月10日上午11:59:49
 * @version v1.0.0
 */
package lm.com.configurer.jdbc;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import lm.com.framework.jdbc.JdbcHelper;

/**
 * @ClassName: JdbcConfigurer
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年5月10日 上午11:59:49
 * 
 */
@Configuration
public class JdbcConfigurer {
	@Autowired
	@Qualifier("druidDataSource")
	private DataSource dataSource;

	/**
	 * JdbcTemplate
	 * 
	 * @return
	 */
	@Bean
	public JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource);
	}

	/**
	 * JdbcHelper
	 * 
	 * @return
	 */
	@Bean
	public JdbcHelper jdbcHelper() {
		return new JdbcHelper(dataSource);
	}
}
