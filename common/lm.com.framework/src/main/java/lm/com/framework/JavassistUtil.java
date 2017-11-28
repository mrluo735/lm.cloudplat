/**
 * @title JavassistUtil.java
 * @description TODO
 * @package lm.com.framework
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年12月29日下午5:37:56
 * @version v1.0
 */
package lm.com.framework;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.StringMemberValue;

/**
 * @author mrluo735
 *
 */
public class JavassistUtil {
	/**
	 * 修改类的注解
	 * 
	 * @param className
	 * @param fieldName
	 * @param annotationType
	 * @param annotationProperty
	 * @param annotationValue
	 * @throws NotFoundException
	 */
	public static void updateClassAnnotation(String className, String annotationType, String annotationProperty,
			String annotationValue) throws NotFoundException {
		ClassPool classPool = ClassPool.getDefault();
		// 获取要修改的类的所有信息
		CtClass ctClass = classPool.get(className);
		ClassFile classFile = ctClass.getClassFile();
		// 获取注解属性
		AnnotationsAttribute attribute = (AnnotationsAttribute) classFile.getAttribute(AnnotationsAttribute.visibleTag);
		// 获取注解
		Annotation annotation = attribute.getAnnotation(annotationType);
		// 获取注解的值
		String oldValue = ((StringMemberValue) annotation.getMemberValue(annotationProperty)).getValue();
		System.out.println("" + oldValue);
		// 修改名称为annotationProperty的注解
		ConstPool cp = classFile.getConstPool();
		annotation.addMemberValue(annotationProperty, new StringMemberValue(annotationValue, cp));
		attribute.setAnnotation(annotation);
	}

	/**
	 * 修改属性的注解
	 * 
	 * @param className
	 * @param fieldName
	 * @param annotationType
	 * @param annotationProperty
	 * @param annotationValue
	 * @throws NotFoundException
	 */
	public static void updateFieldAnnotation(String className, String fieldName, String annotationType,
			String annotationProperty, String annotationValue) throws NotFoundException {
		ClassPool classPool = ClassPool.getDefault();
		// 获取要修改的类的所有信息
		CtClass ctClass = classPool.get(className);
		// 获取类里的em属性
		CtField cfField = ctClass.getField(fieldName);
		// 获取属性信息
		FieldInfo fieldInfo = cfField.getFieldInfo();
		// 获取注解属性
		AnnotationsAttribute attribute = (AnnotationsAttribute) fieldInfo.getAttribute(AnnotationsAttribute.visibleTag);
		// 获取注解
		Annotation annotation = attribute.getAnnotation(annotationType);
		// 获取注解的值
		String oldValue = ((StringMemberValue) annotation.getMemberValue(annotationProperty)).getValue();
		System.out.println("" + oldValue);
		// 修改名称为annotationProperty的注解
		ConstPool cp = fieldInfo.getConstPool();
		annotation.addMemberValue(annotationProperty, new StringMemberValue(annotationValue, cp));
		attribute.setAnnotation(annotation);
	}

	/**
	 * 修改方法的注解
	 * 
	 * @param className
	 * @param methodName
	 * @param annotationType
	 * @param annotationProperty
	 * @param annotationValue
	 * @throws NotFoundException
	 */
	public static void updateMethodAnnotation(String className, String methodName, Class<?>[] methodParamTypes,
			String annotationType, String annotationProperty, String annotationValue) throws NotFoundException {
		ClassPool classPool = ClassPool.getDefault();
		// 获取要修改的类的所有信息
		CtClass ctClass = classPool.get(className);
		// 获取类里的所有方法
		CtMethod ctMethod = null;
		CtMethod[] ctMethods = ctClass.getDeclaredMethods();
		for (CtMethod method : ctMethods) {
			if (methodName.equals(method.getName()) && (null == methodParamTypes
					|| equalCtParameterType(methodParamTypes, method.getParameterTypes()))) {
				ctMethod = method;
			}
		}
		if (null == ctMethod)
			return;
		// 获取注解属性
		AnnotationsAttribute attribute = (AnnotationsAttribute) ctMethod.getMethodInfo()
				.getAttribute(AnnotationsAttribute.visibleTag);
		// 获取注解
		Annotation annotation = attribute.getAnnotation(annotationType);
		// 获取注解的值
		String oldValue = ((StringMemberValue) annotation.getMemberValue(annotationProperty)).getValue();
		System.out.println("" + oldValue);
		// 修改名称为annotationProperty的注解
		ConstPool cp = ctMethod.getMethodInfo().getConstPool();
		annotation.addMemberValue(annotationProperty, new StringMemberValue(annotationValue, cp));
		attribute.setAnnotation(annotation);
	}

	/**
	 * 比较参数类型是否相同
	 * 
	 * @param parameterTypes
	 * @param ctParameterTypes
	 * @return
	 */
	private static boolean equalCtParameterType(Class<?>[] parameterTypes, CtClass[] ctParameterTypes) {
		if (parameterTypes.length != ctParameterTypes.length)
			return false;
		int i = 0;
		for (Class<?> parameterType : parameterTypes) {
			if (parameterType != ctParameterTypes[i].getClass())
				return false;
			i++;
		}
		return true;
	}
}
