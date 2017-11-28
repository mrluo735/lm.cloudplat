package lm.com.framework.webmvc.result;

import lm.com.framework.http.EnumHttpStatus;

/**
 * 成功JsonResult : JsonResult
 * 
 * @author mrluo735
 *
 */
public class SuccessJsonResult extends JsonResult {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6635351070011993077L;

	/**
	 * 重载+1 构造函数
	 */
	public SuccessJsonResult() {
		super();
	}

	/**
	 * 重载+2 构造函数
	 * 
	 * @param message
	 */
	public SuccessJsonResult(String message) {
		super(true, EnumHttpStatus.Two_OK.getValue(), message);
	}

	/**
	 * 重载+3 构造函数
	 * 
	 * @param message
	 */
	public SuccessJsonResult(Object data) {
		super(true, EnumHttpStatus.Two_OK.getValue(), EnumHttpStatus.Two_OK.getDescription(), data);
	}

	/**
	 * 重载+4 构造函数
	 * 
	 * @param message
	 * @param data
	 */
	public SuccessJsonResult(String message, Object data) {
		super(true, EnumHttpStatus.Two_OK.getValue(), message, data);
	}
}
