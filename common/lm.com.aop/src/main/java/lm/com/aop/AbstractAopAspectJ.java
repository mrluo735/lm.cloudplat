package lm.com.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Aop切面抽象类
 * 
 * @author mrluo735
 *
 */
public abstract class AbstractAopAspectJ {
	/**
	 * 日志TOPIC前缀
	 */
	public static final String LOG_SYSTEM_PREFIX = "LOG_SYSTEM_";

	/**
	 * 前置通知
	 * 
	 * @param joinPoint
	 */
	public abstract void before(JoinPoint joinPoint);

	/**
	 * 返回后通知
	 * 
	 * @param joinPoint
	 */
	public abstract void afterReturning(JoinPoint joinPoint);

	/**
	 * 抛出异常后通知
	 * 
	 * @param joinPoint
	 */
	public abstract void afterThrowing(JoinPoint joinPoint);

	/**
	 * 后置通知
	 * 
	 * @param joinPoint
	 */
	public abstract void after(JoinPoint joinPoint);

	/**
	 * 环绕通知
	 * 
	 * @param proceedingJoinPoint
	 * @return
	 */
	public abstract Object around(ProceedingJoinPoint proceedingJoinPoint);
}
