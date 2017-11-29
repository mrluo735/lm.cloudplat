/**
 * @title ContextDTO.java
 * @description TODO
 * @package lm.com.service
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年12月28日下午3:58:59
 * @version v1.0
 */
package lm.com.framework.service;

import java.util.Map;

/**
 * 身份传输对象
 * 
 * @param <T>
 *            主键类型
 * @author mrluo735
 *
 */
public class IdentityDTO<T> implements IIdentity<T> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2217191397053795533L;

	private T userId;

	private String userName;

	private String email;

	private String mobile;

	private Map<String, Object> extra;

	/**
	 * 获取用户id
	 * 
	 * @return
	 */
	public T getUserId() {
		return this.userId;
	}

	/**
	 * 设置用户id
	 * 
	 * @param userId
	 */
	public void setUserId(T userId) {
		this.userId = userId;
	}

	/**
	 * 获取用户名
	 * 
	 * @return
	 */
	public String getUserName() {
		return this.userName;
	}

	/**
	 * 设置用户名
	 * 
	 * @param userName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * 获取邮箱
	 * 
	 * @return
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * 设置邮箱
	 * 
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 获取手机
	 * 
	 * @return
	 */
	public String getMobile() {
		return this.mobile;
	}

	/**
	 * 设置手机
	 * 
	 * @param mobile
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * 获取扩展信息
	 * 
	 * @return
	 */
	public Map<String, Object> getExtra() {
		return this.extra;
	}

	/**
	 * 设置扩展信息
	 * 
	 * @param map
	 */
	public void setExtra(Map<String, Object> extra) {
		this.extra = extra;
	}
}
