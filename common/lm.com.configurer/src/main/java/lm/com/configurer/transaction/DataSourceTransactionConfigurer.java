/**
 * @title DataSourceTransactionConfigurer.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.com.framework.boot.configurer.transaction
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年5月13日下午5:32:21
 * @version v1.0.0
 */
package lm.com.configurer.transaction;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

/**
 * @ClassName: DataSourceTransactionConfigurer
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年5月13日 下午5:32:21
 * 
 */
@Configuration
public class DataSourceTransactionConfigurer implements TransactionManagementConfigurer {
	@Autowired
	@Qualifier("druidDataSource")
	private DataSource dataSource;

	/**
	 * 支持注解事务
	 */
	@Bean
	@Override
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		return new DataSourceTransactionManager(dataSource);
	}
}
