/**
 * @title AopConfigurer.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.pro.aop
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年5月2日下午5:23:51
 * @version v1.0.0
 */
package lm.com.aop;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.AspectJAfterAdvice;
import org.springframework.aop.aspectj.AspectJAfterReturningAdvice;
import org.springframework.aop.aspectj.AspectJAfterThrowingAdvice;
import org.springframework.aop.aspectj.AspectJAroundAdvice;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.AspectJMethodBeforeAdvice;
import org.springframework.aop.aspectj.SimpleAspectInstanceFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import lm.com.framework.ReflectUtil;

/**
 * @ClassName: AopConfigurer
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年5月2日 下午5:23:51
 * 
 */
@Configuration
public class AopConfigurer {
	private static final Logger logger = LoggerFactory.getLogger(AopConfigurer.class);
	private static final PathMatchingResourcePatternResolver PATH_RESOURCE_RESOLVER = new PathMatchingResourcePatternResolver();

	@Value("${lm.aop.aspect.pointcut.expression}")
	private String expression = "";
	@Value("${lm.aop.method.description.pattern}")
	private String pattern = "classpath:mdmap/*.properties";

	/**
	 * 方法描述
	 * 
	 * @return
	 */
	@Bean
	public Map<String, String> methodDescription() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			Resource[] resources = PATH_RESOURCE_RESOLVER.getResources(pattern);
			if (resources == null || resources.length < 1)
				return map;

			Properties properties = new Properties();
			for (Resource resource : resources) {
				if (!resource.isReadable())
					continue;
				properties.clear();
				properties.load(resource.getInputStream());
				for (String key : properties.stringPropertyNames()) {
					map.put(key, properties.getProperty(key, ""));
				}
			}
		} catch (Exception ex) {
			logger.error("加载方法描述出错！错误原因: {}", ex);
		}
		return map;
	}

	/**
	 * 定义切点,设置拦截规则
	 * 
	 * @return
	 */
	@Bean
	public AspectJExpressionPointcut pointcut() {
		AspectJExpressionPointcut aspectJExpPointcut = new AspectJExpressionPointcut();
		aspectJExpPointcut.setExpression(this.expression);
		return aspectJExpPointcut;
	}

	/**
	 * 前置通知
	 * 
	 * @return
	 */
	@Bean
	public DefaultPointcutAdvisor beforeAdvisor() {
		Method method = ReflectUtil.getMethod(GlobalAopAspect.class, "before", JoinPoint.class);
		SimpleAspectInstanceFactory saif = new SimpleAspectInstanceFactory(method.getDeclaringClass());
		AspectJMethodBeforeAdvice beforeAdvice = new AspectJMethodBeforeAdvice(method, this.pointcut(), saif);

		DefaultPointcutAdvisor defaultPointcutAdvisor = new DefaultPointcutAdvisor();
		defaultPointcutAdvisor.setPointcut(this.pointcut());
		defaultPointcutAdvisor.setAdvice(beforeAdvice);
		return defaultPointcutAdvisor;
	}

	/**
	 * 返回后通知
	 * 
	 * @return
	 */
	@Bean
	public DefaultPointcutAdvisor afterReturningAdvisor() {
		Method method = ReflectUtil.getMethod(GlobalAopAspect.class, "afterReturning", JoinPoint.class);
		SimpleAspectInstanceFactory saif = new SimpleAspectInstanceFactory(method.getDeclaringClass());
		AspectJAfterReturningAdvice afterReturningAdvice = new AspectJAfterReturningAdvice(method, this.pointcut(),
				saif);
		afterReturningAdvice.setReturningName("returnValue");

		DefaultPointcutAdvisor defaultPointcutAdvisor = new DefaultPointcutAdvisor();
		defaultPointcutAdvisor.setPointcut(this.pointcut());
		defaultPointcutAdvisor.setAdvice(afterReturningAdvice);
		return defaultPointcutAdvisor;
	}

	/**
	 * 抛出异常后通知
	 * 
	 * @return
	 */
	@Bean
	public DefaultPointcutAdvisor afterThrowingAdvisor() {
		Method method = ReflectUtil.getMethod(GlobalAopAspect.class, "afterThrowing", JoinPoint.class);
		SimpleAspectInstanceFactory saif = new SimpleAspectInstanceFactory(method.getDeclaringClass());
		AspectJAfterThrowingAdvice afterThrowingAdvice = new AspectJAfterThrowingAdvice(method, this.pointcut(), saif);
		afterThrowingAdvice.setThrowingName("exception");

		DefaultPointcutAdvisor defaultPointcutAdvisor = new DefaultPointcutAdvisor();
		defaultPointcutAdvisor.setPointcut(this.pointcut());
		defaultPointcutAdvisor.setAdvice(afterThrowingAdvice);
		return defaultPointcutAdvisor;
	}

	/**
	 * 后置通知
	 * 
	 * @return
	 */
	@Bean
	public DefaultPointcutAdvisor afterAdvisor() {
		Method method = ReflectUtil.getMethod(GlobalAopAspect.class, "after", JoinPoint.class);
		SimpleAspectInstanceFactory saif = new SimpleAspectInstanceFactory(method.getDeclaringClass());
		AspectJAfterAdvice afterAdvice = new AspectJAfterAdvice(method, this.pointcut(), saif);

		DefaultPointcutAdvisor defaultPointcutAdvisor = new DefaultPointcutAdvisor();
		defaultPointcutAdvisor.setPointcut(this.pointcut());
		defaultPointcutAdvisor.setAdvice(afterAdvice);
		return defaultPointcutAdvisor;
	}

	/**
	 * 环绕通知
	 * 
	 * @return
	 */
	@Bean
	public DefaultPointcutAdvisor aroundAdvisor() {
		// AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
		Method method = ReflectUtil.getMethod(GlobalAopAspect.class, "around", ProceedingJoinPoint.class);
		SimpleAspectInstanceFactory saif = new SimpleAspectInstanceFactory(method.getDeclaringClass());
		AspectJAroundAdvice aroundAdvice = new AspectJAroundAdvice(method, this.pointcut(), saif);

		DefaultPointcutAdvisor defaultPointcutAdvisor = new DefaultPointcutAdvisor();
		defaultPointcutAdvisor.setPointcut(this.pointcut());
		defaultPointcutAdvisor.setAdvice(aroundAdvice);
		return defaultPointcutAdvisor;
	}
}
