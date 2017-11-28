package lm.com.framework.webmvc.result;

import lm.com.framework.http.EnumHttpStatus;

/**
 * JsonResult : ActionResult
 * 
 * @author mrluo735
 *
 */
public class JsonResult extends ActionResult {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8933546382771022827L;

	private boolean success = true;

	private int code = EnumHttpStatus.Two_OK.getValue();

	private String message = "";

	private Object data = null;

	// region 构造函数
	/**
	 * 重载+1 构造函数
	 */
	public JsonResult() {
		this.put("code", this.code);
		this.put("success", this.success);
	}

	/**
	 * 重载+2 构造函数
	 * 
	 * @param success
	 * @param code
	 * @param message
	 */
	public JsonResult(boolean success, int code, String message) {
		this.success = success;
		this.code = code;
		this.message = message;

		this.put("message", message);
		this.put("code", code);
		this.put("success", success);
	}

	/**
	 * 重载+3 构造函数
	 * 
	 * @param success
	 * @param code
	 * @param message
	 * @param data
	 */
	public JsonResult(boolean success, int code, String message, Object data) {
		this.success = success;
		this.code = code;
		this.message = message;
		this.data = data;

		this.put("data", data);
		this.put("message", message);
		this.put("code", code);
		this.put("success", success);
	}
	// endregion

	/**
	 * 获取成功信息
	 * 
	 * @return
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * 设置成功信息
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
		return code;
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
	 * 获取消息
	 * 
	 * @return
	 */
	public String getMessage() {
		return null == message ? "" : message;
	}

	/**
	 * 设置消息
	 * 
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 获取扩展数据
	 * 
	 * @return
	 */
	public Object getData() {
		return data;
	}

	/**
	 * 设置扩展数据
	 * 
	 * @param data
	 */
	public void setData(Object data) {
		this.data = data;
	}
}
