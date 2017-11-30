/**
 * @title OAuthSecurityConfigurer.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.cloud.oauth.server.configurer
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年5月8日下午2:50:08
 * @version v1.0.0
 */
package lm.cloud.oauth.server.configurer;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

import lm.cloud.oauth.server.oauth2.CustomJdbcAuthorizationCodeServices;
import lm.cloud.oauth.server.oauth2.CustomJdbcClientDetailsService;
import lm.cloud.oauth.server.oauth2.CustomJdbcTokenStore;

/**
 * @ClassName: OAuthSecurityConfigurer
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年5月8日 下午2:50:08
 * 
 */
@Configuration
@EnableAuthorizationServer
public class OAuthSecurityConfigurer extends AuthorizationServerConfigurerAdapter {
	@Value("${lm.cloud.oauth.access-token-validity:43200}")
	private int accessTokenValidity;
	@Value("${lm.cloud.oauth.refresh-token-validity:2592000}")
	private int refreshTokenValidity;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private DataSource dataSource;

	@Bean
	public TokenStore tokenStore() {
		return new CustomJdbcTokenStore(dataSource);
	}

	@Bean
	public ClientDetailsService clientDetailsService() {
		return new CustomJdbcClientDetailsService(dataSource);
	}

	@Bean
	public AuthorizationCodeServices authorizationCodeServices() {
		return new CustomJdbcAuthorizationCodeServices(dataSource);
	}

	@Bean
	@Primary
	public AuthorizationServerTokenServices tokenServices() {
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(this.tokenStore());
		defaultTokenServices.setSupportRefreshToken(true);
		defaultTokenServices.setClientDetailsService(this.clientDetailsService());
//		defaultTokenServices.setTokenEnhancer(tokenEnhancer);
		defaultTokenServices.setAccessTokenValiditySeconds(this.accessTokenValidity); // 默认12小时(int) TimeUnit.DAYS.toSeconds(30)
		defaultTokenServices.setRefreshTokenValiditySeconds(this.refreshTokenValidity);	// 默认30天(int) TimeUnit.DAYS.toSeconds(30)
		return defaultTokenServices;
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()");		//公开/oauth/token的接口
		security.checkTokenAccess("isAuthenticated()");

		security.allowFormAuthenticationForClients();
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.withClientDetails(clientDetailsService());

		// @formatter:off
//		clients.inMemory()
//			.withClient("ui1")
//				.secret("ui1-secret")
//				.authorities("ROLE_TRUSTED_CLIENT")
//				.authorizedGrantTypes("authorization_code", "refresh_token")
//				.scopes("ui1.read")
//				.autoApprove(true)
//			.and()
//			.withClient("ui2")
//				.secret("ui2-secret")
//				.authorities("ROLE_TRUSTED_CLIENT")
//				.authorizedGrantTypes("authorization_code", "refresh_token")
//				.scopes("ui2.read", "ui2.write")
//				.autoApprove(true)
//			.and()
//			.withClient("mobile-app")
//				.authorities("ROLE_CLIENT")
//				.authorizedGrantTypes("implicit", "refresh_token")
//				.scopes("read")
//				.autoApprove(true)
//			.and()
//			.withClient("customer-integration-system")
//				.secret("1234567890")
//				.authorities("ROLE_CLIENT")
//				.authorizedGrantTypes("client_credentials")
//				.scopes("read")
//				.autoApprove(true);
		// @formatter:on
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authenticationManager(this.authenticationManager).tokenStore(tokenStore());
		endpoints.setClientDetailsService(clientDetailsService());
		endpoints.tokenServices(this.tokenServices());
		endpoints.authorizationCodeServices(authorizationCodeServices());
	}
}
