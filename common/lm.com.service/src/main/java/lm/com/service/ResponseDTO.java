package lm.com.service;

import java.io.Serializable;

/**
 * 响应传输对象
 * 
 * @author mrluo735
 *
 */
public class ResponseDTO<T> extends ResponseBaseDTO implements Serializable {
	private static final long serialVersionUID = 3315965630264310008L;

	private T data;
	
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
		super(success, code, message);
	}

	/**
	 * 重载+3 构造函数
	 * 
	 * @param response
	 */
	public ResponseDTO(ResponseBaseDTO response) {
		super(response.isSuccess(), response.getCode(), response.getMessage());
	}

	/**
	 * 获取数据
	 * 
	 * @return
	 */
	public T getData() {
		return data;
	}

	/**
	 * 设置数据
	 * 
	 * @param data
	 */
	public void setData(T data) {
		this.data = data;
	}
}
