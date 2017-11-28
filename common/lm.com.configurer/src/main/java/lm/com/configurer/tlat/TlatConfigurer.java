/**
 * @title TlatConfigurer.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.pro.provider.cloud.core.configurer
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年5月3日下午3:30:03
 * @version v1.0.0
 */
package lm.com.configurer.tlat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lm.com.configurer.bootstrap.BeanDeinitionRegistryBootstrap;
import lm.com.tlat.TlatFactory;
import lm.com.tlat.TlaterScannerConfigurer;
import lm.com.tlat.dhm.tablebind.TableBindMapping;

/**
 * @ClassName: TlatConfigurer
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年5月3日 下午3:30:03
 * 
 */
@Configuration
@EnableTransactionManagement
public class TlatConfigurer {
	private static final Logger logger = LoggerFactory.getLogger(TlatConfigurer.class);

	@Value("${tlat.tlaterLocations}")
	private String tlaterLocations = "";
	@Value("${tlat.baseTlaters}")
	private String[] baseTlaters;
	@Value("${tlat.modelLocations}")
	private String[] modelLocations;

	@Autowired
	@Qualifier("druidDataSource")
	private DataSource dataSource;

	@Autowired
	@SuppressWarnings("unused")
	private TlaterScannerConfigurer tlaterScannerConfigurer;

	/**
	 * 
	 * @return
	 */
	@Bean
	public TlatFactory tlatFactory() {
		TlatFactory tlatFactory = new TlatFactory();
		tlatFactory.setDataSource(dataSource);

		// 添加XML目录
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		String[] locations = tlaterLocations.split(",");
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
		tlatFactory.setTlaterLocations(resources.toArray(new Resource[0]));

		logger.info("※※※※※※※※※※ Tlat TlatFactory创建成功！ ※※※※※※※※※※");
		return tlatFactory;
	}

	@Bean
	public TlaterScannerConfigurer tlaterScannerConfigurer(TlatFactory tlatFactory) {
		BeanDefinitionRegistry registry = BeanDeinitionRegistryBootstrap.getBeanDefinitionRegistry();
		TlaterScannerConfigurer tlaterScannerConfigurer = new TlaterScannerConfigurer();
		tlaterScannerConfigurer.setTlatFactory(tlatFactory);
		tlaterScannerConfigurer.setBaseTlaters(baseTlaters);
		tlaterScannerConfigurer.postProcessBeanDefinitionRegistry(registry);
		return tlaterScannerConfigurer;
	}

	/**
	 * 
	 * @return
	 */
	@Bean
	public TableBindMapping tableBindMapping() {
		TableBindMapping tableBindMapping = new TableBindMapping(dataSource);
		tableBindMapping.setShowSql(true);
		tableBindMapping.setDevMode(false);
		tableBindMapping.setPackagesToScan(this.modelLocations);
		tableBindMapping.start();
		logger.info("※※※※※※※※※※ Tlat DHM创建成功！ ※※※※※※※※※※");
		return tableBindMapping;
	}
}
