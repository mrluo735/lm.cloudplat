package lm.cloud.oauth.server;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.SessionAttributes;

import lm.com.configurer.ConfigurerConstant;

/**
 * 应用程序启动类
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableResourceServer
@EnableAuthorizationServer
@SessionAttributes("authorizationRequest")
@ComponentScan(basePackages = { ConfigurerConstant.DataSource.DRUID, ConfigurerConstant.Orm.JDBC })
@ComponentScan(basePackageClasses = App.class)
public class App {
	private static final Logger logger = LoggerFactory.getLogger(App.class);

	/**
	 * 启动入口
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@PostConstruct
	public void ApplicationInitial() {
		logger.info("lm.cloud.oauth.server 启动成功");
	}

	@PreDestroy
	public void destory() {
		logger.info("lm.cloud.oauth.server 销毁成功");
	}
}
