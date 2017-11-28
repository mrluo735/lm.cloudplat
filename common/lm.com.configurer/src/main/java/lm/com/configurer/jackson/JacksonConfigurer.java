/**
 * @title JacksonConfigurer.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.pro.secureshell.cloud.provider
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年3月16日下午5:45:30
 * @version v1.0.0
 */
package lm.com.configurer.jackson;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import lm.com.framework.StringUtil;

/**
 * @ClassName: JacksonConfigurer
 * @Description: Jackson配置
 * @author mrluo735
 * @date 2017年3月16日 下午5:45:30
 * 
 */
@Configuration
public class JacksonConfigurer {
	@Value("${jackson.date-formate:yyyy-mm-dd HH:mm:ss}")
	private String dateFormat;

	@Bean
	public Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder(
			List<Jackson2ObjectMapperBuilderCustomizer> customizers) {
		Jackson2ObjectMapperBuilder builder = configureObjectMapper();
		customize(builder, customizers);
		return builder;
	}

	private void customize(Jackson2ObjectMapperBuilder builder,
			List<Jackson2ObjectMapperBuilderCustomizer> customizers) {
		for (Jackson2ObjectMapperBuilderCustomizer customizer : customizers) {
			customizer.customize(builder);
		}
	}

	private Jackson2ObjectMapperBuilder configureObjectMapper() {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
		// List<String> activeProfiles = asList(env.getActiveProfiles());
		// if (activeProfiles.contains(SPRING_PROFILE_DEVELOPMENT)) {
		// builder.featuresToEnable(SerializationFeature.INDENT_OUTPUT);
		// }
		if (!StringUtil.isNullOrWhiteSpace(this.dateFormat))
			builder.dateFormat(new SimpleDateFormat(this.dateFormat));
		return builder;
	}
}
