/**
 * @title CustomJdbcAuthorizationCodeServices.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.cloud.oauth.server.oauth2
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年5月8日下午2:03:44
 * @version v1.0.0
 */
package lm.cloud.oauth.server.oauth2;

import javax.sql.DataSource;

import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;

/**
 * 实现对表oauth_code的操作
 * 
 * @ClassName: CustomJdbcAuthorizationCodeServices
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年5月8日 下午2:03:44
 * 
 */
public class CustomJdbcAuthorizationCodeServices extends JdbcAuthorizationCodeServices {
	/**
	 * 构造函数
	 * 
	 * @param dataSource
	 */
	public CustomJdbcAuthorizationCodeServices(DataSource dataSource) {
		super(dataSource);
	}
}
