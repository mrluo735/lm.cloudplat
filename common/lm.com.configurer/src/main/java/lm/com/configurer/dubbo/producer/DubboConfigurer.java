package lm.com.configurer.dubbo.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.alibaba.dubbo.config.spring.AnnotationBean;

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

	@Value("${dubbo.annotation.package}")
	private String dubboAnnotationPackage;

	@Value("${dubbo.registry.default.id}")
	private String dubboRegistryDefaultId;
	@Value("${dubbo.registry.default.protocol}")
	private String dubboRegistryDefaultProtocol;
	@Value("${dubbo.registry.default.address}")
	private String dubboRegistryDefaultAddress;

	@Value("${dubbo.provider.default.id}")
	private String dubboProviderDefaultId;
	@Value("${dubbo.provider.default.registry}")
	private String dubboProviderDefaultRegistry;
	@Value("${dubbo.provider.default.protocol}")
	private String dubboProviderDefaultProtocol;
	@Value("${dubbo.provider.default.delay:-1}")
	private int dubboProviderDefaultDelay;
	@Value("${dubbo.provider.default.timeout:6000}")
	private int dubboProviderDefaultTimeout;
	@Value("${dubbo.provider.default.retries:0}")
	private int dubboProviderDefaultRetries;
	@Value("${dubbo.provider.default.loadbalance}")
	private String dubboProviderDefaultLoadbalance;
	@Value("${dubbo.provider.default.cluster}")
	private String dubboProviderDefaultCluster;

	@Value("${dubbo.protocol.dubbo.netty.hessian2.id}")
	private String dubboProtocolDubboNettyHessian2Id;
	@Value("${dubbo.protocol.dubbo.netty.hessian2.name}")
	private String dubboProtocolDubboNettyHessian2Name;
	@Value("${dubbo.protocol.dubbo.netty.hessian2.server}")
	private String dubboProtocolDubboNettyHessian2Server;
	@Value("${dubbo.protocol.dubbo.netty.hessian2.serialization}")
	private String dubboProtocolDubboNettyHessian2Serialization;
	@Value("${dubbo.protocol.dubbo.netty.hessian2.host}")
	private String dubboProtocolDubboNettyHessian2Host;
	@Value("${dubbo.protocol.dubbo.netty.hessian2.port}")
	private int dubboProtocolDubboNettyHessian2Port;
	@Value("${dubbo.protocol.dubbo.netty.hessian2.heartbeat}")
	private int dubboProtocolDubboNettyHessian2Heartbeat;
	@Value("${dubbo.protocol.dubbo.netty.hessian2.threadpool}")
	private String dubboProtocolDubboNettyHessian2Threadpool;
	@Value("${dubbo.protocol.dubbo.netty.hessian2.threads}")
	private int dubboProtocolDubboNettyHessian2Threads;

	@Value("${dubbo.protocol.dubbo.netty.kryo.id}")
	private String dubboProtocolDubboNettyKryoId;
	@Value("${dubbo.protocol.dubbo.netty.kryo.name}")
	private String dubboProtocolDubboNettyKryoName;
	@Value("${dubbo.protocol.dubbo.netty.kryo.server}")
	private String dubboProtocolDubboNettyKryoServer;
	@Value("${dubbo.protocol.dubbo.netty.kryo.serialization}")
	private String dubboProtocolDubboNettyKryoSerialization;
	@Value("${dubbo.protocol.dubbo.netty.kryo.host}")
	private String dubboProtocolDubboNettyKryoHost;
	@Value("${dubbo.protocol.dubbo.netty.kryo.port}")
	private int dubboProtocolDubboNettyKryoPort;
	@Value("${dubbo.protocol.dubbo.netty.kryo.heartbeat}")
	private int dubboProtocolDubboNettyKryoHeartbeat;
	@Value("${dubbo.protocol.dubbo.netty.kryo.threadpool}")
	private String dubboProtocolDubboNettyKryoThreadpool;
	@Value("${dubbo.protocol.dubbo.netty.kryo.threads}")
	private int dubboProtocolDubboNettyKryoThreads;

	@Value("${dubbo.protocol.rmi.netty.hessian2.id}")
	private String dubboProtocolRmiNettyHessian2Id;
	@Value("${dubbo.protocol.rmi.netty.hessian2.name}")
	private String dubboProtocolRmiNettyHessian2Name;
	@Value("${dubbo.protocol.rmi.netty.hessian2.server}")
	private String dubboProtocolRmiNettyHessian2Server;
	@Value("${dubbo.protocol.rmi.netty.hessian2.serialization}")
	private String dubboProtocolRmiNettyHessian2Serialization;
	@Value("${dubbo.protocol.rmi.netty.hessian2.host}")
	private String dubboProtocolRmiNettyHessian2Host;
	@Value("${dubbo.protocol.rmi.netty.hessian2.port}")
	private int dubboProtocolRmiNettyHessian2Port;
	@Value("${dubbo.protocol.rmi.netty.hessian2.heartbeat}")
	private int dubboProtocolRmiNettyHessian2Heartbeat;
	@Value("${dubbo.protocol.rmi.netty.hessian2.threadpool}")
	private String dubboProtocolRmiNettyHessian2Threadpool;
	@Value("${dubbo.protocol.rmi.netty.hessian2.threads}")
	private int dubboProtocolRmiNettyHessian2Threads;

	@Value("${dubbo.protocol.rest.tomcat.json.id}")
	private String dubboProtocolRestTomcatJsonId;
	@Value("${dubbo.protocol.rest.tomcat.json.name}")
	private String dubboProtocolRestTomcatJsonName;
	@Value("${dubbo.protocol.rest.tomcat.json.server}")
	private String dubboProtocolRestTomcatJsonServer;
	@Value("${dubbo.protocol.rest.tomcat.json.serialization}")
	private String dubboProtocolRestTomcatJsonSerialization;
	@Value("${dubbo.protocol.rest.tomcat.json.host}")
	private String dubboProtocolRestTomcatJsonHost;
	@Value("${dubbo.protocol.rest.tomcat.json.port}")
	private int dubboProtocolRestTomcatJsonPort;
	@Value("${dubbo.protocol.rest.tomcat.json.heartbeat}")
	private int dubboProtocolRestTomcatJsonHeartbeat;
	@Value("${dubbo.protocol.rest.tomcat.json.threadpool}")
	private String dubboProtocolRestTomcatJsonThreadpool;
	@Value("${dubbo.protocol.rest.tomcat.json.threads}")
	private int dubboProtocolRestTomcatJsonThreads;
	@Value("${dubbo.protocol.rest.tomcat.json.keepalive:true}")
	private boolean dubboProtocolRestTomcatJsonKeepAlive;

	@Value("${dubbo.protocol.rest.jetty.json.id}")
	private String dubboProtocolRestJettyJsonId;
	@Value("${dubbo.protocol.rest.jetty.json.name}")
	private String dubboProtocolRestJettyJsonName;
	@Value("${dubbo.protocol.rest.jetty.json.server}")
	private String dubboProtocolRestJettyJsonServer;
	@Value("${dubbo.protocol.rest.jetty.json.serialization}")
	private String dubboProtocolRestJettyJsonSerialization;
	@Value("${dubbo.protocol.rest.jetty.json.host}")
	private String dubboProtocolRestJettyJsonHost;
	@Value("${dubbo.protocol.rest.jetty.json.port}")
	private int dubboProtocolRestJettyJsonPort;
	@Value("${dubbo.protocol.rest.jetty.json.heartbeat}")
	private int dubboProtocolRestJettyJsonHeartbeat;
	@Value("${dubbo.protocol.rest.jetty.json.threadpool}")
	private String dubboProtocolRestJettyJsonThreadpool;
	@Value("${dubbo.protocol.rest.jetty.json.threads}")
	private int dubboProtocolRestJettyJsonThreads;
	@Value("${dubbo.protocol.rest.jetty.json.keepalive:true}")
	private boolean dubboProtocolRestJettyJsonKeepAlive;

	@Value("${dubbo.protocol.rest.netty.json.id}")
	private String dubboProtocolRestNettyJsonId;
	@Value("${dubbo.protocol.rest.netty.json.name}")
	private String dubboProtocolRestNettyJsonName;
	@Value("${dubbo.protocol.rest.netty.json.server}")
	private String dubboProtocolRestNettyJsonServer;
	@Value("${dubbo.protocol.rest.netty.json.serialization}")
	private String dubboProtocolRestNettyJsonSerialization;
	@Value("${dubbo.protocol.rest.netty.json.host}")
	private String dubboProtocolRestNettyJsonHost;
	@Value("${dubbo.protocol.rest.netty.json.port}")
	private int dubboProtocolRestNettyJsonPort;
	@Value("${dubbo.protocol.rest.netty.json.heartbeat}")
	private int dubboProtocolRestNettyJsonHeartbeat;
	@Value("${dubbo.protocol.rest.netty.json.threadpool}")
	private String dubboProtocolRestNettyJsonThreadpool;
	@Value("${dubbo.protocol.rest.netty.json.threads}")
	private int dubboProtocolRestNettyJsonThreads;
	@Value("${dubbo.protocol.rest.netty.json.keepalive:true}")
	private boolean dubboProtocolRestNettyJsonKeepAlive;

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
	 *
	 * 与&lt;dubbo:annotation/>相当.提供方扫描带有@com.alibaba.dubbo.config.annotation.Service的注解类
	 */
	@Bean
	public static AnnotationBean annotationBean(@Value("${dubbo.annotation.package}") String packages) {
		AnnotationBean annotationBean = new AnnotationBean();
		// 多个包可使用英文逗号隔开
		annotationBean.setPackage(packages);
		return annotationBean;
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
	 * 与&lt;dubbo:provider/>相当
	 *
	 * @return
	 */
	@Bean
	public ProviderConfig providerDefault() {
		ProviderConfig providerConfig = new ProviderConfig();
		providerConfig.setId(this.dubboProviderDefaultId);
		if (this.dubboProviderDefaultRegistry.equalsIgnoreCase("registryDefault"))
			providerConfig.setRegistry(this.registryDefault());

		providerConfig.setDelay(this.dubboProviderDefaultDelay);
		providerConfig.setTimeout(this.dubboProviderDefaultTimeout);
		providerConfig.setRetries(this.dubboProviderDefaultRetries);
		providerConfig.setLoadbalance(this.dubboProviderDefaultLoadbalance);
		providerConfig.setCluster(this.dubboProviderDefaultCluster);

		if (this.dubboProviderDefaultRegistry.equalsIgnoreCase("protocolDNH"))
			providerConfig.setProtocol(this.protocolDNH());
		else if (this.dubboProviderDefaultRegistry.equalsIgnoreCase("protocolDNK"))
			providerConfig.setProtocol(this.protocolDNK());
		else if (this.dubboProviderDefaultRegistry.equalsIgnoreCase("protocolRNH"))
			providerConfig.setProtocol(this.protocolRNH());
		else if (this.dubboProviderDefaultRegistry.equalsIgnoreCase("protocolRTJ"))
			providerConfig.setProtocol(this.protocolRTJ());
		else if (this.dubboProviderDefaultRegistry.equalsIgnoreCase("protocolRJJ"))
			providerConfig.setProtocol(this.protocolRJJ());
		else if (this.dubboProviderDefaultRegistry.equalsIgnoreCase("protocolRNJ"))
			providerConfig.setProtocol(this.protocolRNJ());
		return providerConfig;
	}

	/**
	 * 与&lt;dubbo:protocol/>相当
	 * <p>
	 * dubbo协议，netty传输，hessian2序列化
	 * </p>
	 *
	 * @return
	 */
	@Bean
	public ProtocolConfig protocolDNH() {
		ProtocolConfig protocolConfig = new ProtocolConfig();
		protocolConfig.setId(this.dubboProtocolDubboNettyHessian2Id);
		protocolConfig.setName(this.dubboProtocolDubboNettyHessian2Name);
		protocolConfig.setServer(this.dubboProtocolDubboNettyHessian2Server);
		protocolConfig.setSerialization(this.dubboProtocolDubboNettyHessian2Serialization);
		protocolConfig.setHost(this.dubboProtocolDubboNettyHessian2Host);
		protocolConfig.setPort(this.dubboProtocolDubboNettyHessian2Port);
		protocolConfig.setHeartbeat(this.dubboProtocolDubboNettyHessian2Heartbeat);
		protocolConfig.setThreadpool(this.dubboProtocolDubboNettyHessian2Threadpool);
		protocolConfig.setThreads(this.dubboProtocolDubboNettyHessian2Threads);
		return protocolConfig;
	}

	/**
	 * 与&lt;dubbo:protocol/>相当
	 * <p>
	 * dubbo协议，netty传输，kryo序列化
	 * </p>
	 *
	 * @return
	 */
	@Bean
	public ProtocolConfig protocolDNK() {
		ProtocolConfig protocolConfig = new ProtocolConfig();
		protocolConfig.setId(this.dubboProtocolDubboNettyKryoId);
		protocolConfig.setName(this.dubboProtocolDubboNettyKryoName);
		protocolConfig.setServer(this.dubboProtocolDubboNettyKryoServer);
		protocolConfig.setSerialization(this.dubboProtocolDubboNettyKryoSerialization);
		protocolConfig.setHost(this.dubboProtocolDubboNettyKryoHost);
		protocolConfig.setPort(this.dubboProtocolDubboNettyKryoPort);
		protocolConfig.setHeartbeat(this.dubboProtocolDubboNettyKryoHeartbeat);
		protocolConfig.setThreadpool(this.dubboProtocolDubboNettyKryoThreadpool);
		protocolConfig.setThreads(this.dubboProtocolDubboNettyKryoThreads);
		return protocolConfig;
	}

	/**
	 * 与&lt;dubbo:protocol/>相当
	 * <p>
	 * rmi协议，netty传输，hessian2序列化
	 * </p>
	 *
	 * @return
	 */
	@Bean
	public ProtocolConfig protocolRNH() {
		ProtocolConfig protocolConfig = new ProtocolConfig();
		protocolConfig.setId(this.dubboProtocolRmiNettyHessian2Id);
		protocolConfig.setName(this.dubboProtocolRmiNettyHessian2Name);
		protocolConfig.setServer(this.dubboProtocolRmiNettyHessian2Server);
		protocolConfig.setSerialization(this.dubboProtocolRmiNettyHessian2Serialization);
		protocolConfig.setHost(this.dubboProtocolRmiNettyHessian2Host);
		protocolConfig.setPort(this.dubboProtocolRmiNettyHessian2Port);
		protocolConfig.setHeartbeat(this.dubboProtocolRmiNettyHessian2Heartbeat);
		protocolConfig.setThreadpool(this.dubboProtocolRmiNettyHessian2Threadpool);
		protocolConfig.setThreads(this.dubboProtocolRmiNettyHessian2Threads);
		return protocolConfig;
	}

	/**
	 * 与&lt;dubbo:protocol/>相当
	 * <p>
	 * rest协议，tomcat传输，json序列化
	 * </p>
	 *
	 * @return
	 */
	@Bean
	public ProtocolConfig protocolRTJ() {
		ProtocolConfig protocolConfig = new ProtocolConfig();
		protocolConfig.setId(this.dubboProtocolRestTomcatJsonId);
		protocolConfig.setName(this.dubboProtocolRestTomcatJsonName);
		protocolConfig.setServer(this.dubboProtocolRestTomcatJsonServer);
		protocolConfig.setSerialization(this.dubboProtocolRestTomcatJsonSerialization);
		protocolConfig.setHost(this.dubboProtocolRestTomcatJsonHost);
		protocolConfig.setPort(this.dubboProtocolRestTomcatJsonPort);
		protocolConfig.setHeartbeat(this.dubboProtocolRestTomcatJsonHeartbeat);
		protocolConfig.setThreadpool(this.dubboProtocolRestTomcatJsonThreadpool);
		protocolConfig.setThreads(this.dubboProtocolRestTomcatJsonThreads);
		// protocolConfig.setKeepAlive(this.dubboProtocolRestTomcatJsonKeepAlive);
		return protocolConfig;
	}

	/**
	 * 与&lt;dubbo:protocol/>相当
	 * <p>
	 * rest协议，jetty传输，json序列化
	 * </p>
	 *
	 * @return
	 */
	@Bean
	public ProtocolConfig protocolRJJ() {
		ProtocolConfig protocolConfig = new ProtocolConfig();
		protocolConfig.setId(this.dubboProtocolRestJettyJsonId);
		protocolConfig.setName(this.dubboProtocolRestJettyJsonName);
		protocolConfig.setServer(this.dubboProtocolRestJettyJsonServer);
		protocolConfig.setSerialization(this.dubboProtocolRestJettyJsonSerialization);
		protocolConfig.setHost(this.dubboProtocolRestJettyJsonHost);
		protocolConfig.setPort(this.dubboProtocolRestJettyJsonPort);
		protocolConfig.setHeartbeat(this.dubboProtocolRestJettyJsonHeartbeat);
		protocolConfig.setThreadpool(this.dubboProtocolRestJettyJsonThreadpool);
		protocolConfig.setThreads(this.dubboProtocolRestJettyJsonThreads);
		// protocolConfig.setKeepAlive(this.dubboProtocolRestJettyJsonKeepAlive);
		return protocolConfig;
	}

	/**
	 * 与&lt;dubbo:protocol/>相当
	 * <p>
	 * rest协议，netty传输，json序列化
	 * </p>
	 *
	 * @return
	 */
	@Bean
	public ProtocolConfig protocolRNJ() {
		ProtocolConfig protocolConfig = new ProtocolConfig();
		protocolConfig.setId(this.dubboProtocolRestNettyJsonId);
		protocolConfig.setName(this.dubboProtocolRestNettyJsonName);
		protocolConfig.setServer(this.dubboProtocolRestNettyJsonServer);
		protocolConfig.setSerialization(this.dubboProtocolRestNettyJsonSerialization);
		protocolConfig.setHost(this.dubboProtocolRestNettyJsonHost);
		protocolConfig.setPort(this.dubboProtocolRestNettyJsonPort);
		protocolConfig.setHeartbeat(this.dubboProtocolRestNettyJsonHeartbeat);
		protocolConfig.setThreadpool(this.dubboProtocolRestNettyJsonThreadpool);
		protocolConfig.setThreads(this.dubboProtocolRestNettyJsonThreads);
		// protocolConfig.setKeepAlive(this.dubboProtocolRestNettyJsonKeepAlive);
		return protocolConfig;
	}

	/**
	 * 与&lt;dubbo:service/>相当
	 *
	 * @return
	 */
	public <T> ServiceConfig<T> serviceConfig() {
		ServiceConfig<T> serviceConfig = new ServiceConfig<>();
		// serviceConfig.setInterface(interfaceClass);
		// serviceConfig.setRef(ref);
		// serviceConfig.setProtocol(protocol);
		// serviceConfig.setProvider(provider);
		return serviceConfig;
	}

	//	@Bean
	//	public ServiceBean<BackOpenServiceImpl> serviceBack() {
	//		ServiceBean<BackOpenServiceImpl> serviceBean = new ServiceBean<BackOpenServiceImpl>();
	//		this.backOpenService.setService(this.backMixService);
	//		serviceBean.setInterface(IService.class);
	//		serviceBean.setRef(this.backOpenService);
	//		serviceBean.setGroup(dubboGroupPrefix + "Back");
	//		// serviceBean.setTimeout(5000);
	//		// serviceBean.setRetries(3);
	//		// serviceBean.setProxy("javassist");
	//		// serviceBean.setVersion("myversion");
	//		return serviceBean;
	//	}
}
