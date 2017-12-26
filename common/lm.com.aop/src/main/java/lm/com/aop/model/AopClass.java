/**
 * @title AopClass.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.pro.aop
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年5月2日上午11:34:03
 * @version v1.0.0
 */
package lm.com.aop.model;

import java.io.Serializable;

/**
 * @ClassName: AopClass
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年5月2日 上午11:34:03
 * 
 */
public class AopClass implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 532730544830479526L;

	/**
	 * 类类型
	 */
	private EnumClassType classType = EnumClassType.Common;
	
	private Class<?> type;

	/**
	 * 包名
	 */
	private String packageName = "";

	/**
	 * 类名
	 */
	private String className = "";

	/**
	 * 类描述
	 */
	private String classDescription = "";

	/**
	 * 方法全名
	 */
	private String methodFullName = "";

	/**
	 * 方法名
	 */
	private String methodName = "";

	/**
	 * 方法参数
	 */
	private String methodParameter = "";

	/**
	 * 方法描述
	 */
	private String methodDescription = "";

	/**
	 * 获取类类型
	 * 
	 * @return the classType
	 */
	public EnumClassType getClassType() {
		return classType;
	}

	/**
	 * 设置类类型
	 * 
	 * @param classType
	 *            the classType to set
	 */
	public void setClassType(EnumClassType classType) {
		this.classType = classType;
	}

	/**
	 * @return the type
	 */
	public Class<?> getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(Class<?> type) {
		this.type = type;
	}

	/**
	 * 获取包名
	 * 
	 * @return the packageName
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * 设置包名
	 * 
	 * @param packageName
	 *            the packageName to set
	 */
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * 获取类名
	 * 
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * 设置类名
	 * 
	 * @param className
	 *            the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * 获取类描述
	 * 
	 * @return the classDescription
	 */
	public String getClassDescription() {
		return classDescription;
	}

	/**
	 * 设置类描述
	 * 
	 * @param classDescription
	 *            the classDescription to set
	 */
	public void setClassDescription(String classDescription) {
		this.classDescription = classDescription;
	}

	/**
	 * 获取方法全名
	 * 
	 * @return the methodFullName
	 */
	public String getMethodFullName() {
		return methodFullName;
	}

	/**
	 * 设置方法全名
	 * 
	 * @param methodFullName
	 *            the methodFullName to set
	 */
	public void setMethodFullName(String methodFullName) {
		this.methodFullName = methodFullName;
	}

	/**
	 * 获取方法名
	 * 
	 * @return the methodName
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * 设置方法名
	 * 
	 * @param methodName
	 *            the methodName to set
	 */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	/**
	 * 获取方法参数
	 * 
	 * @return the methodParameter
	 */
	public String getMethodParameter() {
		return methodParameter;
	}

	/**
	 * 设置方法参数
	 * 
	 * @param methodParameter
	 *            the methodParameter to set
	 */
	public void setMethodParameter(String methodParameter) {
		this.methodParameter = methodParameter;
	}

	/**
	 * 获取方法描述
	 * 
	 * @return the methodDescription
	 */
	public String getMethodDescription() {
		return methodDescription;
	}

	/**
	 * 设置方法描述
	 * 
	 * @param methodDescription
	 *            the methodDescription to set
	 */
	public void setMethodDescription(String methodDescription) {
		this.methodDescription = methodDescription;
	}
}
