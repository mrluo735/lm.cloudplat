/**
 * @title FeignConfigurer.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.pro.secureshell.consumer.cloud
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年3月16日下午5:05:42
 * @version v1.0.0
 */
package lm.com.configurer.feign;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.netflix.feign.support.ResponseEntityDecoder;
import org.springframework.cloud.netflix.feign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import feign.codec.Decoder;
import lm.com.framework.jackson.JsLongSerializer;

/**
 * @ClassName: FeignConfigurer
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年3月16日 下午5:05:42
 * 
 */
@Configuration
public class FeignConfigurer {
	@Value("${lm.cloud.oauth.userName}")
	private String userName;
	@Value("${lm.cloud.oauth.password}")
	private String password;
	@Value("${jackson.date-format}")
	private String jacksonDateFormat;

	@Bean
	public Decoder feignDecoder() {
		final HttpMessageConverter<?> jacksonConverter = new MappingJackson2HttpMessageConverter(customObjectMapper());
		ObjectFactory<HttpMessageConverters> objectFactory = new ObjectFactory<HttpMessageConverters>() {
			@Override
			public HttpMessageConverters getObject() throws BeansException {
				return new HttpMessageConverters(jacksonConverter);
			}
		};
		return new ResponseEntityDecoder(new SpringDecoder(objectFactory));
	}

	private ObjectMapper customObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setDateFormat(new SimpleDateFormat(this.jacksonDateFormat));
		SimpleModule simpleModule = new SimpleModule();
		simpleModule.addSerializer(Long.class, new JsLongSerializer());
		objectMapper.registerModule(simpleModule);
		return objectMapper;
	}

	@Bean
	public RequestInterceptor requestInterceptor() {
		return new RequestInterceptor() {
			@Override
			public void apply(RequestTemplate template) {
				String clientCredentials = String.format("%s:%s", userName, password);
				String base64 = Base64.encodeBase64String(clientCredentials.getBytes(Charset.forName("UTF-8")));
				template.header("Authorization", "Basic " + base64);
			}
		};
	}

	// @Bean
	// public RequestInterceptor
	// oauth2FeignRequestInterceptor(OAuth2ClientContext oauth2ClientContext){
	// return new OAuth2FeignRequestInterceptor(oauth2ClientContext,
	// resource());
	// }

	// @Bean
	// protected OAuth2ProtectedResourceDetails resource() {
	// AuthorizationCodeResourceDetails resource = new
	// AuthorizationCodeResourceDetails();
	// resource.setAccessTokenUri(tokenUrl);
	// resource.setUserAuthorizationUri(authorizeUrl);
	// resource.setClientId(clientId);
	// // TODO: Remove this harcode
	// resource.setClientSecret("secret");
	// return resource;
	// }
}
