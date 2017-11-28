/**
 * @title RestTemplateConfigurer.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.pro.secureshell.consumer.cloud
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年3月16日下午3:48:42
 * @version v1.0.0
 */
package lm.com.configurer.rest;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName: RestTemplateConfigurer
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年3月16日 下午3:48:42
 * 
 */
@Configuration
public class RestTemplateConfigurer {
	/**
	 * 
	 * @return
	 */
	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
