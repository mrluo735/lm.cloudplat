/**
 * @title IContext.java
 * @description TODO
 * @package lm.com.service
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年12月23日上午11:50:23
 * @version v1.0
 */
package lm.com.service;

import java.util.Map;

/**
 * 上下文接口
 * 
 * @author mrluo735
 *
 */
public interface IContext<T> extends java.io.Serializable {
	/**
	 * 获取用户id
	 * 
	 * @return
	 */
	public T getUserId();

	/**
	 * 设置用户id
	 * 
	 * @param userId
	 */
	public void setUserId(T userId);

	/**
	 * 获取用户名
	 * 
	 * @return
	 */
	public String getUserName();

	/**
	 * 设置用户名
	 * 
	 * @param userName
	 */
	public void setUserName(String userName);

	/**
	 * 获取邮箱
	 * 
	 * @return
	 */
	public String getEmail();

	/**
	 * 设置邮箱
	 * 
	 * @param email
	 */
	public void setEmail(String email);

	/**
	 * 获取手机
	 * 
	 * @return
	 */
	public String getMobile();

	/**
	 * 设置手机
	 * 
	 * @param mobile
	 */
	public void setMobile(String mobile);

	/**
	 * 获取扩展信息
	 * 
	 * @return
	 */
	public Map<String, Object> getExtra();

	/**
	 * 设置扩展信息
	 * 
	 * @param extra
	 */
	public void setExtra(Map<String, Object> extra);
}
