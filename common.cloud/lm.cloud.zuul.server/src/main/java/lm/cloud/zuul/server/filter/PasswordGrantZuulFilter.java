/**
 * @title PasswordGrantZuulFilter.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.cloud.oauth
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年5月11日下午7:37:56
 * @version v1.0.0
 */
package lm.cloud.zuul.server.filter;

import java.nio.charset.Charset;
import java.util.Collections;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * @ClassName: PasswordGrantZuulFilter
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年5月11日 下午7:37:56
 * 
 */
public class PasswordGrantZuulFilter extends ZuulFilter {
	private static final Logger logger = LoggerFactory.getLogger(PasswordGrantZuulFilter.class);
	@Value("${security.oauth2.client.accessTokenUri}")
	private String accessTokenUri;

	@Value("${security.oauth2.client.clientId}")
	private String clientId;

	@Value("${security.oauth2.client.clientSecret}")
	private String clientSecret;

	@Autowired
	private OAuth2ClientContext clientContext;

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public boolean shouldFilter() {
		RequestContext ctx = RequestContext.getCurrentContext();
		String header = ctx.getRequest().getHeader(HttpHeaders.AUTHORIZATION);

		return header != null && header.toLowerCase().startsWith("basic");
	}

	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		if (clientContext.getAccessToken() == null) {

			String header = ctx.getRequest().getHeader(HttpHeaders.AUTHORIZATION);

			String base64Credentials = header.substring("Basic".length()).trim();
			String credentials = new String(Base64.decodeBase64(base64Credentials), Charset.forName("UTF-8"));
			final String[] values = credentials.split(":", 2);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.put(HttpHeaders.AUTHORIZATION, Collections.singletonList(
					"Basic " + Base64.encodeBase64String((clientId + ":" + clientSecret).getBytes())));
			
			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
			map.add("grant_type", "password");
			map.add("username", values[0]);
			map.add("password", values[1]);

			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
			ResponseEntity<OAuth2AccessToken> response = new RestTemplate().postForEntity(accessTokenUri, request,
					OAuth2AccessToken.class);
			clientContext.setAccessToken(response.getBody());
		}

		ctx.addZuulRequestHeader(HttpHeaders.AUTHORIZATION,
				OAuth2AccessToken.BEARER_TYPE + " " + clientContext.getAccessToken().getValue());
		logger.info("access_token={}", clientContext.getAccessToken().getValue());
		return null;
	}
}
