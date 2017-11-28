/**
 * @title ResponseBaseDTO.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.com.service
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年3月21日上午11:26:21
 * @version v1.0.0
 */
package lm.com.service;

import java.io.Serializable;

import lm.com.framework.http.EnumHttpStatus;

/**
 * @ClassName: ResponseBaseDTO
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年3月21日 上午11:26:21
 * 
 */
public class ResponseBaseDTO implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 103772970716964745L;

	/**
	 * 
	 */
	private boolean success = true;

	private int code = EnumHttpStatus.Two_OK.getValue();

	private String message = EnumHttpStatus.Two_OK.getDescription();

	/**
	 * 重载+1 构造函数
	 */
	public ResponseBaseDTO() {
	}

	/**
	 * 重载+2 构造函数
	 * 
	 * @param success
	 * @param code
	 * @param message
	 */
	public ResponseBaseDTO(boolean success, int code, String message) {
		this.success = success;
		this.code = code;
		this.message = message;
	}

	/**
	 * 获取是否成功
	 * 
	 * @return
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * 设置是否成功
	 * 
	 * @param success
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * 获取代码
	 * 
	 * @return
	 */
	public int getCode() {
		return this.code;
	}

	/**
	 * 设置代码
	 * 
	 * @param code
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * 获取提示信息
	 * 
	 * @return
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * 设置提示信息
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
