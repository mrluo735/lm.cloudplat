/**
 * @title HibernateBean.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.pro.secureshell.cloud.provider
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年3月9日上午11:41:22
 * @version v1.0.0
 */
package lm.com.configurer.hibernate;

import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.Interceptor;
import org.hibernate.SessionFactory;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lm.com.framework.hibernate.HibernateInterceptor;
import lm.com.framework.hibernate.HibernateLogEventListener;

/**
 * @ClassName: HibernateBean
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年3月9日 上午11:41:22
 * 
 */
@Configuration
@EnableTransactionManagement
public class HibernateConfigurer {
	private static final Logger logger = LoggerFactory.getLogger(HibernateConfigurer.class);
	
	@Value("${hibernate.packagesToScan}")
	private String packagesToScan = "";
	@Value("${hibernate.dialect}")
	private String dialect = "org.hibernate.dialect.MySQL5Dialect";
	@Value("${javax.persistence.validation.mode}")
	private String validationMode = "none";
	@Value("${hibernate.hbm2ddl.auto}")
	private String hbm2ddlAuto = "update";
	@Value("${hibernate.show_sql}")
	private String show_sql = "true";
	@Value("${hibernate.current_session_context_class}")
	private String current_session_context_class = "thread";
	@Value("${hibernate.format_sql}")
	private String format_sql = "true";
	@Value("${hibernate.use_sql_comments}")
	private String use_sql_comments = "true";
	@Value("${hibernate.max_fetch_depth}")
	private String max_fetch_depth = "3";
	@Value("${hibernate.jdbc.batch_size}")
	private String jdbc_batch_size = "20";
	@Value("${hibernate.jdbc.fetch_size}")
	private String jdbc_fetch_size = "20";
	@Value("${hibernate.cache.use_second_level_cache}")
	private String use_second_level_cache = "false";
	@Value("${hibernate.connection.release_mode}")
	private String connection_release_mode = "on_close";

	@Autowired
	@Qualifier("druidDataSource")
	private DataSource dataSource;
	@Autowired
	private Interceptor hibernateInterceptor;
	@Autowired
	private HibernateLogEventListener hibernateLogEventListener;

	@Bean("sessionFactory")
	public LocalSessionFactoryBean LocalSessionFactoryBean() {
		LocalSessionFactoryBean localSessionFactoryBean = new LocalSessionFactoryBean();
		localSessionFactoryBean.setDataSource(dataSource);
		localSessionFactoryBean.setPackagesToScan(packagesToScan.split(","));
		localSessionFactoryBean.setEntityInterceptor(hibernateInterceptor);

		Properties properties = new Properties();
		// hibernate 方言
		properties.setProperty("hibernate.dialect", dialect);
		properties.setProperty("javax.persistence.validation.mode", validationMode);
		// 必要时在数据库新建所有表格
		properties.setProperty("hibernate.hbm2ddl.auto", hbm2ddlAuto);
		properties.setProperty("hibernate.show_sql", show_sql);
		// 配置current session的上下文环境，方便我们调用sessionFactory获取当前线程统一个session对象
		properties.setProperty("hibernate.current_session_context_class", current_session_context_class);
		// 用更漂亮的格式显示sql语句
		properties.setProperty("hibernate.format_sql", format_sql);
		properties.setProperty("hibernate.use_sql_comments", use_sql_comments);
		properties.setProperty("hibernate.max_fetch_depth", max_fetch_depth);
		properties.setProperty("hibernate.jdbc.batch_size", jdbc_batch_size);
		properties.setProperty("hibernate.jdbc.fetch_size", jdbc_fetch_size);
		properties.setProperty("hibernate.cache.use_second_level_cache", use_second_level_cache);
		properties.setProperty("hibernate.connection.release_mode", connection_release_mode);
		localSessionFactoryBean.setHibernateProperties(properties);
		
		logger.info("※※※※※※※※※※ Hibernate SessionFactory创建成功！ ※※※※※※※※※※");
		return localSessionFactoryBean;
	}

	/**
	 * hibernate拦截器
	 */
	@Bean
	public Interceptor hibernateInterceptor() {
		HibernateInterceptor hibernateInterceptor = new lm.com.framework.hibernate.HibernateInterceptor();
		return hibernateInterceptor;
	}

	/**
	 * hibernate日志监听器
	 */
	@Bean
	public HibernateLogEventListener hibernateLogEventListener() {
		HibernateLogEventListener hibernateLogEventListener = new lm.com.framework.hibernate.HibernateLogEventListener();
		return hibernateLogEventListener;
	}

	/**
	 * hibernateTransactionManager事务
	 * 
	 * @return
	 * @throws SQLException
	 */
	@Bean
	public HibernateTransactionManager hibernateTransactionManager(SessionFactory sessionFactory) throws SQLException {
		HibernateTransactionManager hibernateTransactionManager = new HibernateTransactionManager();
		hibernateTransactionManager.setSessionFactory(sessionFactory);
		return hibernateTransactionManager;
	}

	/**
	 * 注册监听器
	 * 
	 * @param sessionFactory
	 */
	@Bean
	public EventListenerRegistry registerListeners(SessionFactory sessionFactory) {
		EventListenerRegistry registry = ((SessionFactoryImpl) sessionFactory).getServiceRegistry()
				.getService(EventListenerRegistry.class);
		registry.getEventListenerGroup(EventType.LOAD).appendListener(hibernateLogEventListener);
		registry.getEventListenerGroup(EventType.PRE_INSERT).appendListener(hibernateLogEventListener);
		registry.getEventListenerGroup(EventType.POST_INSERT).appendListener(hibernateLogEventListener);
		registry.getEventListenerGroup(EventType.PRE_UPDATE).appendListener(hibernateLogEventListener);
		registry.getEventListenerGroup(EventType.POST_UPDATE).appendListener(hibernateLogEventListener);
		registry.getEventListenerGroup(EventType.PRE_DELETE).appendListener(hibernateLogEventListener);
		registry.getEventListenerGroup(EventType.POST_DELETE).appendListener(hibernateLogEventListener);
		return registry;
	}

	/**
	 * 支持注解事务
	 */
	// @Bean
	// @Override
	// public PlatformTransactionManager annotationDrivenTransactionManager() {
	// return new DataSourceTransactionManager(dataSource);
	// }
}
