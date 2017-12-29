package lm.com.configurer.bootstrap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lm.com.framework.webmvc.SpringApplicationContext;

/**
 * 
 * @author mrluo735
 *
 */
@Configuration
public class BootstrapConfigurer {
	/**
	 * 
	 * @return
	 */
	@Bean
	public SpringApplicationContext springApplicationContext() {
		return new SpringApplicationContext();
	}
}
