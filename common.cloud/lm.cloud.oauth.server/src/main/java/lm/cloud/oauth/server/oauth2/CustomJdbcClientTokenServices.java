/**
 * @title CustomJdbcClientTokenServices.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.cloud.oauth.server.oauth2
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年5月8日下午2:39:22
 * @version v1.0.0
 */
package lm.cloud.oauth.server.oauth2;

import javax.sql.DataSource;

import org.springframework.security.oauth2.client.token.JdbcClientTokenServices;

/**
 * 实现对表oauth_client_token的操作
 * 
 * @ClassName: CustomJdbcClientTokenServices
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年5月8日 下午2:39:22
 * 
 */
public class CustomJdbcClientTokenServices extends JdbcClientTokenServices {
	/**
	 * 构造函数
	 * 
	 * @param dataSource
	 */
	public CustomJdbcClientTokenServices(DataSource dataSource) {
		super(dataSource);
	}
}
