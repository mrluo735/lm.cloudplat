/**
 * @title ConfigurerConstant.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.com.configurer
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年5月13日下午5:37:46
 * @version v1.0.0
 */
package lm.com.configurer;

/**
 * 配置常量
 * 
 * @ClassName: ConfigurerConstant
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年5月13日 下午5:37:46
 * 
 */
public class ConfigurerConstant {
	/**
	 * id生成器
	 * 
	 * @author mrluo735
	 *
	 */
	public static class Id {
		/**
		 * Snowflake Id
		 */
		public static final String SNOWFLAKE = "lm.com.configurer.id.snowflake";
	}

	/**
	 * 引导组件
	 * 
	 * @ClassName: Bootstrap
	 * @Description: TODO(这里用一句话描述这个类的作用)
	 * @author mrluo735
	 * @date 2017年5月13日 下午6:10:05
	 *
	 */
	public static class Bootstrap {
		/**
		 * 所有的引导组件
		 */
		public static final String ALL = "lm.com.configurer.bootstrap";
	}

	// region 缓存
	/**
	 * 缓存
	 * 
	 * @ClassName: Cache
	 * @Description: TODO(这里用一句话描述这个类的作用)
	 * @author mrluo735
	 * @date 2017年6月27日 下午7:53:41
	 *
	 */
	public static class Cache {
		/**
		 * redis
		 */
		public static final String REDIS = "lm.com.configurer.redis";
	}
	// endregion

	// region 数据源
	/**
	 * 数据源
	 * 
	 * @ClassName: DataSource
	 * @Description: TODO(这里用一句话描述这个类的作用)
	 * @author mrluo735
	 * @date 2017年5月13日 下午5:51:46
	 *
	 */
	public static class DataSource {
		/**
		 * commons.dbcp
		 */
		public static final String DBCP = "lm.com.configurer.dbcp";

		/**
		 * druid
		 */
		public static final String DRUID = "lm.com.configurer.druid";
	}
	// endregion

	// region ORM
	/**
	 * Orm
	 * 
	 * @ClassName: Orm
	 * @Description: TODO(这里用一句话描述这个类的作用)
	 * @author mrluo735
	 * @date 2017年5月13日 下午5:56:19
	 *
	 */
	public static class Orm {
		/**
		 * Hibernate
		 */
		public static final String HIBERNATE = "lm.com.configurer.hibernate";

		/**
		 * Mybatis
		 */
		public static final String MYBATIS = "lm.com.configurer.mybatis";

		/**
		 * Tlat
		 */
		public static final String TLAT = "lm.com.configurer.tlat";

		/**
		 * Jdbc
		 */
		public static final String JDBC = "lm.com.configurer.jdbc";

		/**
		 * Transaction
		 */
		public static final String TRANSACTION = "lm.com.configurer.transaction";
	}
	// endregion

	// region Json
	/**
	 * Json
	 * 
	 * @ClassName: Json
	 * @Description: TODO(这里用一句话描述这个类的作用)
	 * @author mrluo735
	 * @date 2017年5月13日 下午6:04:49
	 *
	 */
	public static class Json {
		public static final String JACKSON = "lm.com.configurer.jackson";
	}
	// endregion

	/**
	 * Dubbo
	 * 
	 * @author mrluo735
	 *
	 */
	public static class Dubbo {
		/**
		 * 生产端
		 */
		public static final String PRODUCER = "lm.com.configurer.dubbo.producer";
		/**
		 * 消费端
		 */
		public static final String CONSUMER = "lm.com.configurer.dubbo.consumer";
	}

	// region Feign
	/**
	 * Feign
	 * 
	 * @ClassName: Feign
	 * @Description: TODO(这里用一句话描述这个类的作用)
	 * @author mrluo735
	 * @date 2017年5月13日 下午6:04:49
	 *
	 */
	public static class Feign {
		public static final String FEIGN = "lm.com.configurer.feign";
	}
	// endregion

	// region Rest
	/**
	 * Rest
	 * 
	 * @ClassName: Rest
	 * @Description: TODO(这里用一句话描述这个类的作用)
	 * @author mrluo735
	 * @date 2017年5月13日 下午6:04:49
	 *
	 */
	public static class Rest {
		public static final String REST = "lm.com.configurer.rest";
	}
	// endregion

	/**
	 * MQ
	 *
	 */
	public static class MQ {
		/**
		 * ActiveMQ
		 * 
		 * @author mrluo735
		 *
		 */
		public static class ActiveMQ {
			/**
			 * 生产端
			 */
			public static final String PRODUCER_BOOT = "lm.com.configurer.mq.activemq.producer.boot";

			/**
			 * 消费端
			 */
			public static final String CONSUMER_BOOT = "lm.com.configurer.mq.activemq.consumer.boot";
		}

		/**
		 * Kafka
		 * 
		 * @author mrluo735
		 *
		 */
		public static class Kafka {
			/**
			 * 生产端
			 */
			public static final String PRODUCER_BOOT = "lm.com.configurer.mq.kafka.producer.boot";
			/**
			 * 消费端
			 */
			public static final String CONSUMER_BOOT = "lm.com.configurer.mq.kafka.consumer.boot";
		}
	}

	/**
	 * ElasticSearch
	 * 
	 * @ClassName: ElasticSearch
	 * @Description: TODO(这里用一句话描述这个类的作用)
	 * @author mrluo735
	 * @date 2017年6月27日 下午4:52:19
	 *
	 */
	public static class ElasticSearch {
		public static final String ELASTICSEARCH = "lm.com.configurer.elasticsearch";
	}
}
