package lm.com.framework.hibernate;

import org.hibernate.EmptyInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * hibernate拦截器 : EmptyInterceptor
 * 
 * @author mrluo735
 *
 */
public class HibernateInterceptor extends EmptyInterceptor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7526198984602185480L;
	private final Logger logger = LoggerFactory.getLogger(HibernateInterceptor.class);

	@Override
	public String onPrepareStatement(String sql) {
		logger.info("执行的sql语句：{}", sql);
		return super.onPrepareStatement(sql);
	}
}
