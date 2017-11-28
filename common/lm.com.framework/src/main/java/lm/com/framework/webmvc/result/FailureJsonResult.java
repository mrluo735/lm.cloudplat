package lm.com.framework.webmvc.result;

/**
 * 失败JsonReulst : JsonResult
 * 
 * @author mrluo735
 *
 */
public class FailureJsonResult extends JsonResult {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6865572456915592945L;

	/**
	 * 重载+1 构造函数
	 */
	public FailureJsonResult() {
		super();
	}

	/**
	 * 重载+2 构造函数
	 * 
	 * @param code
	 * @param message
	 */
	public FailureJsonResult(int code, String message) {
		super(false, code, message);
	}

	/**
	 * 重载+3 构造函数
	 * 
	 * @param code
	 * @param message
	 */
	public FailureJsonResult(int code, Object data) {
		super(false, code, "", data);
	}

	/**
	 * 重载+4 构造函数
	 * 
	 * @param code
	 * @param message
	 * @param data
	 */
	public FailureJsonResult(int code, String message, Object data) {
		super(false, code, message, data);
	}
}
