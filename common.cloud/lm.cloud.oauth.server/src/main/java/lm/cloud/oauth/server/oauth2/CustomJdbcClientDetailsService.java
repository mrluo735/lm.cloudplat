/**
 * @title CustomJdbcClientDetailsService.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.cloud.oauth.server.oauth2
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年5月8日下午2:06:03
 * @version v1.0.0
 */
package lm.cloud.oauth.server.oauth2;

import javax.sql.DataSource;

import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

/**
 * 实现对表oauth_client_details的操作
 * 
 * @ClassName: CustomJdbcClientDetailsService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年5月8日 下午2:06:03
 * 
 */
public class CustomJdbcClientDetailsService extends JdbcClientDetailsService {
	/**
	 * 构造函数
	 * 
	 * @param dataSource
	 */
	public CustomJdbcClientDetailsService(DataSource dataSource) {
		super(dataSource);
	}
}
