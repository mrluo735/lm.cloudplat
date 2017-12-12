package lm.com.configurer.id.snowflake;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lm.com.framework.id.IdWorker;

/**
 * 
 * @author mrluo735
 *
 */
@Configuration
public class IdConfigurer {
	@Value("${lm.id.workerId.machineId:2}")
	private long machineId;
	@Value("${lm.id.workerId.centerId:2}")
	private long centerId;

	/**
	 * IdWorker
	 * 
	 * @return
	 */
	@Bean
	public IdWorker idWorker() {
		IdWorker idWorker = new IdWorker(this.centerId, this.centerId);
		return idWorker;
	}
}
