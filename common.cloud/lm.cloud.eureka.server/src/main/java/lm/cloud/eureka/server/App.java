package lm.cloud.eureka.server;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 应用程序启动类
 *
 */
@EnableEurekaServer
@SpringBootApplication
public class App {
	private final Logger logger = LoggerFactory.getLogger(App.class);

	/**
	 * 启动入口
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new SpringApplicationBuilder(App.class).web(true).run(args);
	}

	@PostConstruct
	public void ApplicationInitial() {
		logger.info("Eureka Server 启动成功");
	}
}
