/**
 * @title OAuthController.java
 * @description TODO
 * @package lm.openapi.ui.controller
 * @author mrluo735
 * @since JDK1.7
 * @date 
 * @version v1.0
 */
package lm.cloud.oauth.server.controller;

import java.util.Collections;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.BadClientCredentialsException;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.common.exceptions.UnsupportedGrantTypeException;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.OAuth2RequestValidator;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestValidator;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * @author mrluo735
 *
 */
@Controller
@RequestMapping("/oauth")
public class OAuthController implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(OAuthController.class);
	@Autowired
	private ClientDetailsService clientDetailsService;
	@Autowired
	private AuthorizationServerTokenServices tokenServices;
	@Autowired
	private AuthorizationCodeServices authorizationCodeServices;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private OAuth2ClientContext clientContext;

	private OAuth2RequestFactory oAuth2RequestFactory;

	//private AuthenticationDetailsSource<HttpServletRequest, ?> authenticationDetailsSource = new WebAuthenticationDetailsSource();
	private OAuth2RequestValidator oAuth2RequestValidator = new DefaultOAuth2RequestValidator();
	private WebResponseExceptionTranslator providerExceptionHandler = new DefaultWebResponseExceptionTranslator();

	/**
	 * RESTful API 方式获取accessToken
	 * 
	 * <p>
	 * <table class="table table-bordered">
	 * <thead>
	 * <tr>
	 * <th>参数名</th>
	 * <th>参数值</th>
	 * <th>必须?</th>
	 * <th>备注</th>
	 * </tr>
	 * </thead> <tbody>
	 * <tr>
	 * <td>grant_type</td>
	 * <td>{grant_type}</td>
	 * <td>是</td>
	 * <td>authorization_code,password,implicit,refresh_token,client_credentials</td>
	 * </tr>
	 * <tr>
	 * <td>scope</td>
	 * <td>{scope}</td>
	 * <td>是</td>
	 * <td>read or write</td>
	 * </tr>
	 * <tr>
	 * <td>client_id</td>
	 * <td>{client_id}</td>
	 * <td>是</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>client_secret</td>
	 * <td>{client_secret}</td>
	 * <td>是</td>
	 * <td></td>
	 * </tr>
	 * <tr>
	 * <td>username</td>
	 * <td>{username}</td>
	 * <td>否</td>
	 * <td>grant_type=password时必须有</td>
	 * </tr>
	 * <tr>
	 * <td>password</td>
	 * <td>{password}</td>
	 * <td>否</td>
	 * <td>grant_type=password时必须有</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * 请求Body示例:
	 * <p>
	 * <code>{"client_id":"test1234","client_secret":"test1234","grant_type":"password","scope":"read","username":"mobile","password":"mobile"}</code>
	 * </p>
	 * 或
	 * <p>
	 * <code>{"client_id":"test1234","client_secret":"test1234","grant_type":"password","scope":"read"}</code>
	 * </p>
	 * 响应: 正常 [200]
	 * {"access_token":"e2996930-8398-44fd-8de5-7d1b1624ced7","token_type":"bearer","refresh_token":"2b2de701-53e7-4b57-8301-e4a06ee49698","expires_in":43008,"scope":"read"}
	 * 异常 [401] {"error":"invalid_grant","error_description":"Bad credentials"}
	 * </p>
	 * 
	 * @param parameters
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/access_token", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public OAuth2AccessToken accessToken(HttpServletRequest request, @RequestBody Map<String, String> parameters) {
//		String userName = parameters.get("username");
//		String password = parameters.get("password");
		String clientId = parameters.get("client_id");
		// 登录
//		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(userName, password);
//		authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
//		Authentication authentication = this.getAuthenticationManager().authenticate(authRequest);
//		logger.info("登录信息:{}", authentication);
		
		ClientDetails authenticatedClient = clientDetailsService.loadClientByClientId(clientId);
		TokenRequest tokenRequest = oAuth2RequestFactory.createTokenRequest(parameters, authenticatedClient);

		if (clientId != null && !"".equals(clientId)) {
			// Only validate the client details if a client authenticated during
			// this
			// request.
			if (!clientId.equals(tokenRequest.getClientId())) {
				// double check to make sure that the client ID in the token
				// request is the same as that in the
				// authenticated client
				throw new InvalidClientException("Given client ID does not match authenticated client");
			}
		}

		if (authenticatedClient != null) {
			oAuth2RequestValidator.validateScope(tokenRequest, authenticatedClient);
		}

		final String grantType = tokenRequest.getGrantType();
		if (grantType == null || grantType.length() < 1) {
			throw new InvalidRequestException("Missing grant type");
		}
		if ("implicit".equals(grantType)) {
			throw new InvalidGrantException("Implicit grant type not supported from token endpoint");
		}

		if (isAuthCodeRequest(parameters)) {
			// The scope was requested or determined during the authorization
			// step
			if (!tokenRequest.getScope().isEmpty()) {
				logger.debug("Clearing scope of incoming token request");
				tokenRequest.setScope(Collections.<String>emptySet());
			}
		}

		if (isRefreshTokenRequest(parameters)) {
			// A refresh token has its own default scopes, so we should ignore
			// any added by the factory here.
			tokenRequest.setScope(OAuth2Utils.parseParameterList(parameters.get(OAuth2Utils.SCOPE)));
		}

		OAuth2AccessToken accessToken = getTokenGranter(grantType).grant(grantType, tokenRequest);
		if (accessToken == null) {
			throw new UnsupportedGrantTypeException("Unsupported grant type: " + grantType);
		}
		clientContext.setAccessToken(accessToken);
		return accessToken;
	}

	private boolean isAuthCodeRequest(Map<String, String> parameters) {
		return "authorization_code".equals(parameters.get("grant_type")) && parameters.get("code") != null;
	}

	private boolean isRefreshTokenRequest(Map<String, String> parameters) {
		return "refresh_token".equals(parameters.get("grant_type")) && parameters.get("refresh_token") != null;
	}

	private AuthenticationManager getAuthenticationManager() {
		return this.authenticationManager;
	}

	private WebResponseExceptionTranslator getExceptionTranslator() {
		return providerExceptionHandler;
	}

	private TokenGranter getTokenGranter(String grantType) {
		if ("authorization_code".equals(grantType)) {
			return new AuthorizationCodeTokenGranter(tokenServices, authorizationCodeServices, clientDetailsService,
					this.oAuth2RequestFactory);
		} else if ("password".equals(grantType)) {
			return new ResourceOwnerPasswordTokenGranter(getAuthenticationManager(), tokenServices,
					clientDetailsService, this.oAuth2RequestFactory);
		} else if ("refresh_token".equals(grantType)) {
			return new RefreshTokenGranter(tokenServices, clientDetailsService, this.oAuth2RequestFactory);
		} else if ("client_credentials".equals(grantType)) {
			return new ClientCredentialsTokenGranter(tokenServices, clientDetailsService, this.oAuth2RequestFactory);
		} else if ("implicit".equals(grantType)) {
			return new ImplicitTokenGranter(tokenServices, clientDetailsService, this.oAuth2RequestFactory);
		} else {
			throw new UnsupportedGrantTypeException("Unsupport grant_type: " + grantType);
		}
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<OAuth2Exception> handleException(Exception e) throws Exception {
		logger.info("Handling error: " + e.getClass().getSimpleName() + ", " + e.getMessage());
		return getExceptionTranslator().translate(e);
	}

	@ExceptionHandler(ClientRegistrationException.class)
	public ResponseEntity<OAuth2Exception> handleClientRegistrationException(Exception e) throws Exception {
		logger.info("Handling error: " + e.getClass().getSimpleName() + ", " + e.getMessage());
		return getExceptionTranslator().translate(new BadClientCredentialsException());
	}

	@ExceptionHandler(OAuth2Exception.class)
	public ResponseEntity<OAuth2Exception> handleException(OAuth2Exception e) throws Exception {
		logger.info("Handling error: " + e.getClass().getSimpleName() + ", " + e.getMessage());
		return getExceptionTranslator().translate(e);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.state(clientDetailsService != null, "ClientDetailsService must be provided");
		Assert.state(authenticationManager != null, "AuthenticationManager must be provided");

		oAuth2RequestFactory = new DefaultOAuth2RequestFactory(clientDetailsService);
	}

	// @Override
	// public void setApplicationContext(ApplicationContext applicationContext)
	// throws BeansException {
	// if (this.authenticationManager == null) {
	// this.authenticationManager = (AuthenticationManager)
	// applicationContext.getBean("authenticationManager");
	// }
	// }
}
