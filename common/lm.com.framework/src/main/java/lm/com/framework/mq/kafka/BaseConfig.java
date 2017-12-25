package lm.com.framework.mq.kafka;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import lm.com.framework.annotation.Value;

/**
 * 配置基类
 * <p>
 * 基于0.9.0.x
 * </p>
 * 
 * @author mrluo735
 *
 */
public class BaseConfig implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1458117470508129251L;
	
	/**
	 * 
	 */
	public static final String TOPIC_OFFSET_CONSUMER = "__offset_consumer";

	/**
	 * 用于建立与kafka集群连接的host/port组。数据将会在所有servers上均衡加载， 不管哪些server是指定用于bootstrapping。 这个列表仅仅影响初始化的hosts（用于发现全部的servers）。
	 * 这个列表格式：host1:port1,host2:port2,…因为这些server仅仅是用于初始化的连接，以发现集群所有成员关系（可能会动态的变化），
	 * 这个列表不需要包含所有的servers（你可能想要不止一个server，尽管这样，可能某个server宕机了）。 如果没有server在这个列表出现，则发送数据会一直失败，直到列表可用。
	 */
	@Value("bootstrap.servers")
	private String bootstrapServers;

	/**
	 * 关闭连接空闲时间
	 */
	@Value("connections.max.idle.ms")
	private long connectionsMaxIdleMs = 540000L;

	/**
	 * socket的接收缓存空间大小,当阅读数据时使用
	 */
	@Value("receive.buffer.bytes")
	private int receiveBufferBytes = 32768;

	/**
	 * 客户端将等待请求的响应的最大时间,如果在这个时间内没有收到响应，客户端将重发请求;超过重试次数将抛异常
	 */
	@Value("request.timeout.ms")
	private int requestTimeoutMs = 30000;

	/**
	 * 安全通信协议。有效值为：PLAINTEXT，SSL，SASL_PLAINTEXT，SASL_SSL。
	 */
	@Value("security.protocol")
	private String securityProtocol = "PLAINTEXT";

	/**
	 * 发送数据时的缓存空间大小
	 */
	@Value("send.buffer.bytes")
	private int sendBufferBytes = 131072;

	/**
	 * 当向server发出请求时，这个字符串会发送给server。目的是能够追踪请求源头，以此来允许ip/port许可列表之外的一些应用可以发送信息。
	 * 这项应用可以设置任意字符串，因为没有任何功能性的目的，除了记录和跟踪
	 */
	@Value("client.id")
	private String clientId;

	/**
	 * 以微秒为单位的时间，是在我们强制更新metadata的时间间隔。即使我们没有看到任何partition leadership改变。
	 */
	@Value("metadata.max.age.ms")
	private long metadataMaxAgeMs = 300000L;

	/**
	 * 类的列表，用于衡量指标。实现MetricReporter接口，将允许增加一些类，这些类在新的衡量指标产生时就会改变。
	 * JmxReporter总会包含用于注册JMX统计
	 */
	@Value("metric.reporters")
	private String metricReporters;

	/**
	 * 用于维护metrics的样本数
	 */
	@Value("metrics.num.samples")
	private int metricsNumSamples = 2;

	/**
	 * metrics系统维护可配置的样本数量，在一个可修正的window size。
	 * 这项配置配置了窗口大小，例如。我们可能在30s的期间维护两个样本。当一个窗口推出后，我们会擦除并重写最老的窗口
	 */
	@Value("metrics.sample.window.ms")
	private long metricsSampleWindowMs = 30000L;

	/**
	 * 连接失败时，当我们重新连接时的等待时间。这避免了客户端反复重连
	 */
	@Value("reconnect.backoff.ms")
	private long reconnectBackoffMs = 50L;

	/**
	 * 在试图重试失败的produce请求之前的等待时间。避免陷入发送-失败的死循环中
	 */
	@Value("retry.backoff.ms")
	private long retryBackoffMs = 100L;

	// region ↓↓↓↓↓↓↓↓↓↓ ssl Begin ↓↓↓↓↓↓↓↓↓↓
	/**
	 * 密钥存储文件中私钥的密码。这是客户端可选的。
	 */
	@Value("ssl.key.password")
	private String sslKeyPassword;

	/**
	 * 密钥存储文件的位置。这是客户端可选的，可以用于客户端的双向身份验证。
	 */
	@Value("ssl.keystore.location")
	private String sslKeystoreLocation;

	/**
	 * 对于密钥存储文件的存储密码。如果ssl.keystore.location配置这是可选的客户端，只需要。
	 */
	@Value("ssl.keystore.password")
	private String sslKeystorePassword;

	/**
	 * 信任存储文件的位置。
	 */
	@Value("ssl.truststore.location")
	private String sslTruststoreLocation;

	/**
	 * 信任存储文件的密码。
	 */
	@Value("ssl.truststore.password")
	private String sslTruststorePassword;

	/**
	 * 启用SSL连接的协议列表。
	 */
	@Value("ssl.enabled.protocols")
	private List<String> sslEnabledProtocols = Arrays.asList("TLSv1.2", "TLSv1.1", "TLSv1");

	/**
	 * 密钥存储文件的文件格式。这是客户端可选的。
	 */
	@Value("ssl.keystore.type")
	private String sslKeystoreType = "JKS";

	/**
	 * 使用SSL协议产生的SSLContext。默认设置是TLS，大多数情况下都可以。
	 * 允许的值在最近的jvm的TLS，TLSv1.1和tlsv1.2。SSL SSL版本2，以及可能在年长的JVM的支持，但他们的用法是气馁，由于已知的安全漏洞。
	 */
	@Value("ssl.protocol")
	private String sslProtocol = "TLS";

	/**
	 * 用于SSL连接的安全提供程序的名称。默认值是JVM的默认安全提供程序。
	 */
	@Value("ssl.provider")
	private String sslProvider;

	/**
	 * 信任存储文件的文件格式。
	 */
	@Value("ssl.truststore.type")
	private String sslTruststoreType = "JKS";

	/**
	 * 密码套件列表。这是一个命名的认证、加密、MAC和密钥交换算法的组合，用于使用TLS或SSL网络协议协商网络连接的安全设置。
	 */
	@Value("ssl.cipher.suites")
	private String sslCipherSuites;

	/**
	 * 端点识别算法验证使用服务器证书服务器主机名。
	 */
	@Value("ssl.endpoint.identification.algorithm")
	private String sslEndpointIdentificationAlgorithm;

	/**
	 * 密钥管理器工厂用于SSL连接的算法。默认值是密钥管理器厂算法配置java虚拟机。
	 */
	@Value("ssl.keymanager.algorithm")
	private String sslKeymanagerAlgorithm = "SunX509";

	/**
	 * 信任管理器工厂用于SSL连接的算法。默认值为信托经理厂算法配置java虚拟机。
	 */
	@Value("ssl.trustmanager.algorithm")
	private String sslTrustmanagerAlgorithm = "PKIX";
	// endregion ↑↑↑↑↑↑↑↑↑↑ ssl End ↑↑↑↑↑↑↑↑↑↑

	// region ↓↓↓↓↓↓↓↓↓↓ sasl Begin ↓↓↓↓↓↓↓↓↓↓
	/**
	 * Kafka运行的Kerberos主体名称。这可以被定义在Kafka的JAAS配置或Kafka的配置。
	 */
	@Value("sasl.kerberos.service.name")
	private String saslKerberosServiceName;

	/**
	 * Kerberos kinit命令路径。
	 */
	@Value("sasl.kerberos.kinit.cmd")
	private String saslKerberosKinitCmd = "/usr/bin/kinit";

	/**
	 * 在刷新尝试之间登录线程休眠时间。
	 */
	@Value("sasl.kerberos.min.time.before.relogin")
	private long saslKerberosMinTimeBeforeRelogin = 60000L;

	/**
	 * 随机抖动添加到更新时间的百分比。
	 */
	@Value("sasl.kerberos.ticket.renew.jitter")
	private double saslKerberosTicketRenewJitter = 0.05D;

	/**
	 * 登录线程将休眠，直到从最后刷新到票证到期的指定窗口时间因子到达，此时它将尝试续订该票证。
	 */
	@Value("sasl.kerberos.ticket.renew.window.factor")
	private double saslKerberosTicketRenewWindowFactor = 0.8D;
	// endregion ↑↑↑↑↑↑↑↑↑↑ sasl End ↑↑↑↑↑↑↑↑↑↑

	/**
	 * 
	 * @return
	 */
	public String getBootstrapServers() {
		return bootstrapServers;
	}

	/**
	 * 
	 * @param bootstrapServers
	 */
	public void setBootstrapServers(String bootstrapServers) {
		this.bootstrapServers = bootstrapServers;
	}

	public long getConnectionsMaxIdleMs() {
		return connectionsMaxIdleMs;
	}

	public void setConnectionsMaxIdleMs(long connectionsMaxIdleMs) {
		this.connectionsMaxIdleMs = connectionsMaxIdleMs;
	}

	public int getReceiveBufferBytes() {
		return receiveBufferBytes;
	}

	public void setReceiveBufferBytes(int receiveBufferBytes) {
		this.receiveBufferBytes = receiveBufferBytes;
	}

	public int getRequestTimeoutMs() {
		return requestTimeoutMs;
	}

	public void setRequestTimeoutMs(int requestTimeoutMs) {
		this.requestTimeoutMs = requestTimeoutMs;
	}

	public String getSecurityProtocol() {
		return securityProtocol;
	}

	public void setSecurityProtocol(String securityProtocol) {
		this.securityProtocol = securityProtocol;
	}

	public int getSendBufferBytes() {
		return sendBufferBytes;
	}

	public void setSendBufferBytes(int sendBufferBytes) {
		this.sendBufferBytes = sendBufferBytes;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public long getMetadataMaxAgeMs() {
		return metadataMaxAgeMs;
	}

	public void setMetadataMaxAgeMs(long metadataMaxAgeMs) {
		this.metadataMaxAgeMs = metadataMaxAgeMs;
	}

	public String getMetricReporters() {
		return metricReporters;
	}

	public void setMetricReporters(String metricReporters) {
		this.metricReporters = metricReporters;
	}

	public int getMetricsNumSamples() {
		return metricsNumSamples;
	}

	public void setMetricsNumSamples(int metricsNumSamples) {
		this.metricsNumSamples = metricsNumSamples;
	}

	public long getMetricsSampleWindowMs() {
		return metricsSampleWindowMs;
	}

	public void setMetricsSampleWindowMs(long metricsSampleWindowMs) {
		this.metricsSampleWindowMs = metricsSampleWindowMs;
	}

	public long getReconnectBackoffMs() {
		return reconnectBackoffMs;
	}

	public void setReconnectBackoffMs(long reconnectBackoffMs) {
		this.reconnectBackoffMs = reconnectBackoffMs;
	}

	public long getRetryBackoffMs() {
		return retryBackoffMs;
	}

	public void setRetryBackoffMs(long retryBackoffMs) {
		this.retryBackoffMs = retryBackoffMs;
	}

	public String getSslKeyPassword() {
		return sslKeyPassword;
	}

	public void setSslKeyPassword(String sslKeyPassword) {
		this.sslKeyPassword = sslKeyPassword;
	}

	public String getSslKeystoreLocation() {
		return sslKeystoreLocation;
	}

	public void setSslKeystoreLocation(String sslKeystoreLocation) {
		this.sslKeystoreLocation = sslKeystoreLocation;
	}

	public String getSslKeystorePassword() {
		return sslKeystorePassword;
	}

	public void setSslKeystorePassword(String sslKeystorePassword) {
		this.sslKeystorePassword = sslKeystorePassword;
	}

	public String getSslTruststoreLocation() {
		return sslTruststoreLocation;
	}

	public void setSslTruststoreLocation(String sslTruststoreLocation) {
		this.sslTruststoreLocation = sslTruststoreLocation;
	}

	public String getSslTruststorePassword() {
		return sslTruststorePassword;
	}

	public void setSslTruststorePassword(String sslTruststorePassword) {
		this.sslTruststorePassword = sslTruststorePassword;
	}

	public List<String> getSslEnabledProtocols() {
		return sslEnabledProtocols;
	}

	public void setSslEnabledProtocols(List<String> sslEnabledProtocols) {
		this.sslEnabledProtocols = sslEnabledProtocols;
	}

	public String getSslKeystoreType() {
		return sslKeystoreType;
	}

	public void setSslKeystoreType(String sslKeystoreType) {
		this.sslKeystoreType = sslKeystoreType;
	}

	public String getSslProtocol() {
		return sslProtocol;
	}

	public void setSslProtocol(String sslProtocol) {
		this.sslProtocol = sslProtocol;
	}

	public String getSslProvider() {
		return sslProvider;
	}

	public void setSslProvider(String sslProvider) {
		this.sslProvider = sslProvider;
	}

	public String getSslTruststoreType() {
		return sslTruststoreType;
	}

	public void setSslTruststoreType(String sslTruststoreType) {
		this.sslTruststoreType = sslTruststoreType;
	}

	public String getSslCipherSuites() {
		return sslCipherSuites;
	}

	public void setSslCipherSuites(String sslCipherSuites) {
		this.sslCipherSuites = sslCipherSuites;
	}

	public String getSslEndpointIdentificationAlgorithm() {
		return sslEndpointIdentificationAlgorithm;
	}

	public void setSslEndpointIdentificationAlgorithm(String sslEndpointIdentificationAlgorithm) {
		this.sslEndpointIdentificationAlgorithm = sslEndpointIdentificationAlgorithm;
	}

	public String getSslKeymanagerAlgorithm() {
		return sslKeymanagerAlgorithm;
	}

	public void setSslKeymanagerAlgorithm(String sslKeymanagerAlgorithm) {
		this.sslKeymanagerAlgorithm = sslKeymanagerAlgorithm;
	}

	public String getSslTrustmanagerAlgorithm() {
		return sslTrustmanagerAlgorithm;
	}

	public void setSslTrustmanagerAlgorithm(String sslTrustmanagerAlgorithm) {
		this.sslTrustmanagerAlgorithm = sslTrustmanagerAlgorithm;
	}

	public String getSaslKerberosServiceName() {
		return saslKerberosServiceName;
	}

	public void setSaslKerberosServiceName(String saslKerberosServiceName) {
		this.saslKerberosServiceName = saslKerberosServiceName;
	}

	public String getSaslKerberosKinitCmd() {
		return saslKerberosKinitCmd;
	}

	public void setSaslKerberosKinitCmd(String saslKerberosKinitCmd) {
		this.saslKerberosKinitCmd = saslKerberosKinitCmd;
	}

	public long getSaslKerberosMinTimeBeforeRelogin() {
		return saslKerberosMinTimeBeforeRelogin;
	}

	public void setSaslKerberosMinTimeBeforeRelogin(long saslKerberosMinTimeBeforeRelogin) {
		this.saslKerberosMinTimeBeforeRelogin = saslKerberosMinTimeBeforeRelogin;
	}

	public double getSaslKerberosTicketRenewJitter() {
		return saslKerberosTicketRenewJitter;
	}

	public void setSaslKerberosTicketRenewJitter(double saslKerberosTicketRenewJitter) {
		this.saslKerberosTicketRenewJitter = saslKerberosTicketRenewJitter;
	}

	public double getSaslKerberosTicketRenewWindowFactor() {
		return saslKerberosTicketRenewWindowFactor;
	}

	public void setSaslKerberosTicketRenewWindowFactor(double saslKerberosTicketRenewWindowFactor) {
		this.saslKerberosTicketRenewWindowFactor = saslKerberosTicketRenewWindowFactor;
	}
}
