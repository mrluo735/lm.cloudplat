package lm.com.configurer.dubbo.consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.RegistryConfig;

@Configuration
public class DubboConfigurer {
	@Value("${dubbo.application.name}")
	private String dubboApplicationName;
	@Value("${dubbo.application.organization}")
	private String dubboApplicationOrganzation;
	@Value("${dubbo.application.owner}")
	private String dubboApplicationOwner;
	@Value("${dubbo.application.version}")
	private String dubboApplicationVersion;

	@Value("${dubbo.group.prefix}")
	protected String dubboGroupPrefix = "";

	@Value("${dubbo.registry.default.id}")
	private String dubboRegistryDefaultId;
	@Value("${dubbo.registry.default.protocol}")
	private String dubboRegistryDefaultProtocol;
	@Value("${dubbo.registry.default.address}")
	private String dubboRegistryDefaultAddress;

	/**
	 * 由&lt;dubbo:application/>转换过来
	 */
	@Bean
	public ApplicationConfig applicationConfig() {
		ApplicationConfig applicationConfig = new ApplicationConfig();
		// applicationConfig.setLogger("slf4j");
		applicationConfig.setName(this.dubboApplicationName);
		applicationConfig.setOrganization(this.dubboApplicationOrganzation);
		applicationConfig.setOwner(this.dubboApplicationOwner);
		applicationConfig.setVersion(this.dubboApplicationVersion);
		return applicationConfig;
	}

	/**
	 * 与&lt;dubbo:registry/>相当
	 */
	@Bean
	public RegistryConfig registryDefault() {
		RegistryConfig registryConfig = new RegistryConfig();
		registryConfig.setId(this.dubboRegistryDefaultId);
		registryConfig.setProtocol(this.dubboRegistryDefaultProtocol);
		registryConfig.setAddress(this.dubboRegistryDefaultAddress);
		return registryConfig;
	}

	/**
	 * 
	 * @return
	 */
	//	@Bean
	//	public ReferenceBean<IService> backOpenService() {
	//		ReferenceBean<IService> reference = new ReferenceBean<>();
	//		reference.setId("backOpenService");
	//		reference.setInterface(IService.class);
	//		reference.setRegistry(super.registryDefault());
	//		reference.setGroup(dubboGroupPrefix + "Back");
	//		reference.setCheck(false);
	//		// reference.setTimeout(5000);
	//		// reference.setRetries(3);
	//		// reference.setVersion("myversion");
	//		return reference;
	//	}
}
