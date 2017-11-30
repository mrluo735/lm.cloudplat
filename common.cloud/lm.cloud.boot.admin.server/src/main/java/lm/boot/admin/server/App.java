package lm.boot.admin.server;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

import de.codecentric.boot.admin.config.EnableAdminServer;

/**
 * 应用程序启动类
 *
 */
@SpringCloudApplication
@EnableAdminServer
public class App {
	/**
	 * 启动入口
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
