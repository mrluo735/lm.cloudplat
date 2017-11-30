/**
 * @title WorkaroundRestTemplateCustomizer.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.cloud.oauth
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年5月12日下午2:50:56
 * @version v1.0.0
 */
package lm.cloud.zuul.server;

import java.util.ArrayList;

import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoRestTemplateCustomizer;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;

/**
 * @ClassName: WorkaroundRestTemplateCustomizer
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年5月12日 下午2:50:56
 * 
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class WorkaroundRestTemplateCustomizer implements UserInfoRestTemplateCustomizer {
	/**
	 * 
	 */
	@Override
	public void customize(OAuth2RestTemplate template) {
		template.setInterceptors(new ArrayList<>(template.getInterceptors()));
	}
}
