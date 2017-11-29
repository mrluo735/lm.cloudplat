package lm.com.framework.service;

import java.io.Serializable;

import lm.com.framework.http.EnumHttpStatus;

/**
 * 响应传输对象
 * 
 * @author mrluo735
 *
 */
public class ResponseDTO implements Serializable {
	private static final long serialVersionUID = 3315965630264310008L;

	/**
	 * 
	 */
	private boolean success = true;

	private int code = EnumHttpStatus.Two_OK.getValue();

	private String message = EnumHttpStatus.Two_OK.getDescription();

	private Object data;

	/**
	 * 重载+1 构造函数
	 */
	public ResponseDTO() {
	}

	/**
	 * 重载+2 构造函数
	 * 
	 * @param success
	 * @param code
	 * @param message
	 */
	public ResponseDTO(boolean success, int code, String message) {
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

	/**
	 * 获取数据
	 * 
	 * @return
	 */
	public Object getData() {
		return data;
	}

	/**
	 * 设置数据
	 * 
	 * @param data
	 */
	public void setData(Object data) {
		this.data = data;
	}
}
