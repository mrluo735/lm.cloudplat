/**
 * @title MybatisConfigurer.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.pro.secureshell.cloud.provider
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年3月9日下午3:01:37
 * @version v1.0.0
 */
package lm.com.configurer.mybatis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @ClassName: MybatisConfigurer
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年3月9日 下午3:01:37
 * 
 */
@Configuration
@EnableTransactionManagement
public class MybatisConfigurer {
	private static final Logger logger = LoggerFactory.getLogger(MybatisConfigurer.class);
	
	@Value("${mybatis.mapperLocations}")
	private String mapperLocations = "";

	@Autowired
	@Qualifier("druidDataSource")
	private DataSource dataSource;
	@Autowired
	private Interceptor mybatisInterceptor;

	@Bean(name = "sqlSessionFactory")
	public SqlSessionFactory sqlSessionFactoryBean() {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		// bean.setTypeAliasesPackage("tk.mybatis.springboot.model");

		// 添加插件
		// bean.setPlugins(new Interceptor[]{pageHelper});

		try {
			// 添加XML目录
			ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
			String[] locations = mapperLocations.split(",");
			List<Resource> resources = new ArrayList<>();
			for (String location : locations) {
				Resource[] reses = null;
				try {
					reses = resolver.getResources(location);
				} catch (IOException ex) {
					continue;
				}
				resources.addAll(Arrays.asList((reses)));
			}
			bean.setMapperLocations(resources.toArray(new Resource[0]));

			// 添加拦截器
			Interceptor[] interceptors = new Interceptor[] { mybatisInterceptor };
			bean.setPlugins(interceptors);
			
			logger.info("※※※※※※※※※※ Mybatis SessionFactory创建成功！ ※※※※※※※※※※");
			return bean.getObject();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 拦截器
	 * 
	 * @return
	 */
	@Bean
	public Interceptor mybatisInterceptor() {
		return new lm.com.framework.mybatis.MybatisInterceptor();
	}

	/**
	 * 
	 * @param sqlSessionFactory
	 * @return
	 */
	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}
