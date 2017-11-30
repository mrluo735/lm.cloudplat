package lm.cloud.config.server;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * 应用程序启动类
 *
 */
@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class App {
	private final Logger logger = LoggerFactory.getLogger(App.class);
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
		logger.info("Config Server 启动成功");
	}
}
