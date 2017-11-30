/**
 * @title CustomJdbcTokenStore.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.cloud.oauth.server.oauth2
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年5月8日下午2:00:07
 * @version v1.0.0
 */
package lm.cloud.oauth.server.oauth2;

import javax.sql.DataSource;

import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

/**
 * 实现对表oauth_access_token,oauth_refresh_token的操作
 * 
 * @ClassName: CustomJdbcTokenStore
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年5月8日 下午2:00:07
 * 
 */
public class CustomJdbcTokenStore extends JdbcTokenStore {
	/**
	 * 构造函数
	 * 
	 * @param dataSource
	 */
	public CustomJdbcTokenStore(DataSource dataSource) {
		super(dataSource);
	}
}
