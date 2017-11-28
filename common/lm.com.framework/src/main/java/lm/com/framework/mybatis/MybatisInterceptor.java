package lm.com.framework.mybatis;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Properties;

import javax.xml.bind.PropertyException;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lm.com.framework.ReflectUtil;
import lm.com.framework.StringUtil;

/**
 * mybatis拦截器
 * 
 * @author mrluo735
 *
 */
@SuppressWarnings("all")
@Intercepts({
		@Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class }),
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class })
		//mybatis-3.4以上不支持
		//@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class }) 
})
public class MybatisInterceptor implements Interceptor {
	private static final Logger logger = LoggerFactory.getLogger(MybatisInterceptor.class);

	/**
	 * 实际执行的代理方法
	 */
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		// 对于StatementHandler其实只有两个实现类，一个是RoutingStatementHandler，另一个是抽象类BaseStatementHandler，
		// BaseStatementHandler有三个子类，分别是SimpleStatementHandler，PreparedStatementHandler和CallableStatementHandler，
		// SimpleStatementHandler是用于处理Statement的，PreparedStatementHandler是处理PreparedStatement的，而CallableStatementHandler是处理CallableStatement的。
		// Mybatis在进行Sql语句处理的时候都是建立的RoutingStatementHandler，而在RoutingStatementHandler里面拥有一个
		// StatementHandler类型的delegate属性，RoutingStatementHandler会依据Statement的不同建立对应的BaseStatementHandler，即SimpleStatementHandler、
		// PreparedStatementHandler或CallableStatementHandler，在RoutingStatementHandler里面所有StatementHandler接口方法的实现都是调用的delegate对应的方法。
		// 我们在MybatisInterceptor类上已经用@Signature标记了该Interceptor只拦截StatementHandler接口的prepare方法，又因为Mybatis只有在建立RoutingStatementHandler的时候
		// 是通过Interceptor的plugin方法进行包裹的，所以我们这里拦截到的目标对象肯定是RoutingStatementHandler对象。
		Object result = null;
		Object target = invocation.getTarget();
		if (target instanceof Executor) {
			Method method = invocation.getMethod();
			String methodName = method.getName();
			if (methodName.equals("update")) {
			} else if (methodName.equals("query")) {
			}
			/** 执行方法 */
			long start = System.currentTimeMillis();
			result = invocation.proceed();
			long end = System.currentTimeMillis();

			logger.info("方法[" + method.getName() + "]执行sql语句花费 [" + (end - start) + "] ms");
		} else if (target instanceof ParameterHandler) {
			result = invocation.proceed();
			System.out.println("ParameterHandler");
		} else if (target instanceof RoutingStatementHandler) {
			RoutingStatementHandler routingStatementHandler = (RoutingStatementHandler) target;
			BaseStatementHandler delegate = (BaseStatementHandler) ReflectUtil
					.getValueByFieldName(routingStatementHandler, "delegate");
			// 通过反射获取delegate父类BaseStatementHandler的mappedStatement属性
			MappedStatement mappedStatement = (MappedStatement) ReflectUtil.getValueByFieldName(delegate,
					"mappedStatement");

			// 拦截到的prepare方法参数是一个Connection对象
			Connection connection = (Connection) invocation.getArgs()[0];
			BoundSql boundSql = delegate.getBoundSql();
			// 获取当前要执行的Sql语句，也就是我们直接在Mapper映射语句中写的Sql语句
			String sql = boundSql.getSql();
			Object parameterObject = boundSql.getParameterObject();
			// 目标方法名
			String methodName = null == mappedStatement ? "" : mappedStatement.getId();
			result = invocation.proceed();
			logger.info(String.format("方法[%s]执行的sql语句：%s；\n参数：%s", methodName,
					sql.replaceAll("\n", "").replaceAll("\t", ""), parameterObject));
		} else if (target instanceof ResultSetHandler) {
			result = invocation.proceed();
			System.out.println("ResultSetHandler");
		}
		return result;
	}

	/**
	 * 获取代理对象，target是源对象，也就是StatementHandler
	 */
	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	/**
	 * 设置属性
	 */
	@Override
	public void setProperties(Properties p) {
		String dialect = p.getProperty("dialect");
		if (StringUtil.isNullOrWhiteSpace(dialect)) {
			try {
				throw new PropertyException("dialectName or dialect property is not found!");
			} catch (PropertyException e) {
				e.printStackTrace();
			}
		}
	}
}
