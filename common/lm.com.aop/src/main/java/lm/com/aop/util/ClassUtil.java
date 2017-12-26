/**
 * @title AspectjUtil.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.pro.aop
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年5月2日上午10:41:54
 * @version v1.0.0
 */
package lm.com.aop.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import lm.com.aop.model.AopClass;
import lm.com.aop.model.EnumClassType;
import lm.com.framework.JsonUtil;
import lm.com.framework.ReflectUtil;

/**
 * Aspectj工具类
 * 
 * @ClassName: AspectjUtil
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年5月2日 上午10:41:54
 * 
 */
public class ClassUtil {
	private static final Logger logger = LoggerFactory.getLogger(ClassUtil.class);

	/**
	 * 获取拦截主体信息
	 * 
	 * @param joinPoint
	 * @return
	 */
	public static AopClass getBody(JoinPoint joinPoint) {
		AopClass aopClass = new AopClass();
		try {
			Signature signature = joinPoint.getSignature();
			if (null != signature) {
				Class<?> clazz = signature.getDeclaringType();
				EnumClassType classType = getClassType(clazz);
				String packageName = clazz.getPackage().getName();
				String className = clazz.getSimpleName();
				String classDescription = getClassDescription(clazz);
				String methodFullName = signature.getDeclaringTypeName().concat(".").concat(signature.getName());
				String methodName = signature.getName();
				String methodParameter = getMethodParameter(joinPoint, signature);
				String methodDescription = getMethodDescription(clazz, methodName, classType);

				aopClass.setType(ReflectUtil.getTopSuperClass(clazz));
				aopClass.setClassType(classType);
				aopClass.setPackageName(packageName);
				aopClass.setClassName(className);
				aopClass.setClassDescription(classDescription);
				aopClass.setMethodFullName(methodFullName);
				aopClass.setMethodName(methodName);
				aopClass.setMethodParameter(methodParameter);
				aopClass.setMethodDescription(methodDescription);
			}
		} catch (Exception ex) {
			logger.error("AspectjUtil.getBody解析出错！错误原因：{}", ex.getMessage());
		}
		return aopClass;
	}

	/**
	 * 获取类类型
	 * 
	 * @param clazz
	 * @return
	 */
	public static EnumClassType getClassType(Class<?> clazz) {
		// 获取类上所有注解
		Annotation[] annotations = clazz.getDeclaredAnnotations();
		if (null == annotations || annotations.length < 1) {
			return EnumClassType.Common;
		}

		for (Annotation item : annotations) {
			if (item.annotationType().equals(RestController.class))
				return EnumClassType.RestController;
			else if (item.annotationType().equals(Controller.class))
				return EnumClassType.Controller;
		}
		return EnumClassType.Common;
	}

	/**
	 * 获取类描述
	 * 
	 * @param clazz
	 * @return
	 */
	public static String getClassDescription(Class<?> clazz) {
		try {
			// 获取类上所有注解
			Annotation[] annotations = clazz.getDeclaredAnnotations();
			if (null == annotations || annotations.length < 1) {
				return "";
			}

			for (Annotation item : annotations) {
				if (!item.annotationType().equals(Description.class))
					continue;

				Method valueMethod = item.annotationType().getMethod("value");
				if (null == valueMethod)
					return "";

				Object methodDesc = valueMethod.invoke(item);
				return null == methodDesc ? "" : methodDesc.toString();
			}
		} catch (NoSuchMethodException ex) {
		} catch (IllegalAccessException ex) {
		} catch (IllegalArgumentException ex) {
		} catch (InvocationTargetException ex) {
		} catch (Exception ex) {
		}
		return "";
	}

	/**
	 * 获取方法参数
	 * 
	 * @param joinPoint
	 * @param signature
	 * @return
	 */
	public static String getMethodParameter(JoinPoint joinPoint, Signature signature) {
		CodeSignature codeSignature = (CodeSignature) signature;
		if (null == codeSignature)
			return "";

		String[] parameterNames = codeSignature.getParameterNames();
		if (null == parameterNames || parameterNames.length < 1)
			return "";

		Class<?>[] parameterTypes = codeSignature.getParameterTypes();
		Object[] parameterValues = joinPoint.getArgs();
		StringBuilder sb = new StringBuilder();
		sb.append("参数名称 = ".concat(JsonUtil.toJsonUseFastJson(parameterNames)).concat("\n"));
		sb.append("参数类型 = ".concat(JsonUtil.toJsonUseFastJson(parameterTypes)).concat("\n"));
		sb.append("参数值 = ".concat(JsonUtil.toJsonUseFastJson(parameterValues)).concat("\n"));
		return sb.toString();
	}

	/**
	 * 获取方法描述
	 * 
	 * @param clazz
	 * @param methodName
	 * @param classType
	 * @return
	 */
	public static String getMethodDescription(Class<?> clazz, String methodName, EnumClassType classType) {
		// 普通类里的方法不获取描述
		if (classType == EnumClassType.Common)
			return "";

		try {
			// 获取方法上的注解
			Method[] methods = clazz.getMethods();
			if (null == methods || methods.length < 1)
				return "";

			for (Method method : methods) {
				if (!method.getName().equals(methodName))
					continue;
				Annotation[] annotations = method.getAnnotations();
				for (Annotation item : annotations) {
					if (!item.annotationType().equals(Description.class))
						continue;
					Method valueMethod;
					valueMethod = item.annotationType().getMethod("value");

					if (null == valueMethod)
						return "";

					Object methodDesc = valueMethod.invoke(item);
					return null == methodDesc ? "" : methodDesc.toString();
				}
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException ex) {
		}
		return "";
	}

	/**
	 * 获取ServletRequest
	 * 
	 * @param joinPoint
	 * @return
	 */
	public static HttpServletRequest getServletRequest(JoinPoint joinPoint) {
		Signature signature = joinPoint.getSignature();
		if (signature == null || !(signature instanceof CodeSignature))
			return null;

		CodeSignature codeSignature = (CodeSignature) signature;

		String[] parameterNames = codeSignature.getParameterNames();
		if (null == parameterNames || parameterNames.length < 1)
			return null;

		// Class<?>[] parameterTypes = codeSignature.getParameterTypes();
		Object[] parameterValues = joinPoint.getArgs();
		for (Object parameterValue : parameterValues) {
			if (parameterValue instanceof HttpServletRequest) {
				HttpServletRequest request = (HttpServletRequest) parameterValue;
				return request;
			}
		}
		return null;
	}
}
