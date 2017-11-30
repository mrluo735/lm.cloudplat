package lm.cloud.storage.server;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;

import lm.cloud.storage.server.configurer.FastdfsConfigurer;

/**
 * 应用程序启动类
 *
 */
@SpringBootApplication
public class App {

	/**
	 * 启动入口
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		FastdfsConfigurer.initFastdfs();
		SpringApplication.run(App.class, args);
	}

	/**
	 * 和Spring Boot自带的MultiPart产生冲突
	 * 
	 * @return
	 */
//	 @Bean
//	 public MultipartResolver multipartResolver() {
//		 CommonsMultipartResolver bean = new CommonsMultipartResolver();
//		 bean.setDefaultEncoding("UTF-8");
//		 bean.setMaxInMemorySize(268435456); // 256MB
//		 bean.setMaxUploadSize(268435456);
//		 return bean;
//	 }

	/**
	 * 
	 * @return
	 */
	@Bean
	public MultipartConfigElement multipartConfigElement() {
		MultipartConfigFactory factory = new MultipartConfigFactory();
		//// 设置文件大小限制 ,超了，页面会抛出异常信息，这时候就需要进行异常信息的处理了;
		factory.setMaxFileSize("256MB"); // KB,MB
		/// 设置总上传数据总大小
		factory.setMaxRequestSize("1024MB");
		// Sets the directory location wherefiles will be stored.
		// factory.setLocation("路径地址");
		return factory.createMultipartConfig();

	}
}
