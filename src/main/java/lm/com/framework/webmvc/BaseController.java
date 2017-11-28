package lm.com.framework.webmvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import lm.com.framework.BooleanUtil;
import lm.com.framework.IntegerUtil;
import lm.com.framework.StringUtil;
import lm.com.framework.sqlmedium.Pager;
import lm.com.framework.webmvc.result.FailureJsonResult;
import lm.com.framework.webmvc.result.JsonResult;
import lm.com.framework.webmvc.result.SuccessJsonResult;

/**
 * 控制器基类
 * 
 * @author mrluo735
 *
 */
@Controller
public class BaseController {
	/**
	 * 日志
	 */
	protected final Logger logger = LoggerFactory.getLogger(getLogClass());
	// protected final Logger logger = LoggerFactory.getLogger(getLogClass());

	protected Class<? extends BaseController> getLogClass() {
		return getClass();
	}

	/**
	 * 从request参数中获取Pager
	 * 
	 * @param request
	 * @return
	 */
	protected Pager getPager(HttpServletRequest request) {
		Pager pager = new Pager();
		Map<String, String[]> parameters = request.getParameterMap();
		if (null == parameters)
			return pager;

		for (Entry<String, String[]> item : parameters.entrySet()) {
			if ("columnPattern".equalsIgnoreCase(item.getKey()) || "fields".equalsIgnoreCase(item.getKey()))
				pager.setColumnPattern(item.getValue()[0]);
			else if ("pageIndex".equalsIgnoreCase(item.getKey()) || "page".equalsIgnoreCase(item.getKey()))
				pager.setPageIndex(IntegerUtil.toInteger(item.getValue()[0], 1));
			else if ("pageSize".equalsIgnoreCase(item.getKey()) || "per_page".equalsIgnoreCase(item.getKey()))
				pager.setPageSize(IntegerUtil.toInteger(item.getValue()[0], 20));
			else if ("isStatCount".equalsIgnoreCase(item.getKey()) || "stat".equalsIgnoreCase(item.getKey()))
				pager.setIsStatCount(BooleanUtil.toBoolean(item.getValue()[0]));
			else if ("orderBy".equalsIgnoreCase(item.getKey()) || "sorts".equalsIgnoreCase(item.getKey())) {
				String[] sorts = item.getValue()[0].split(",");
				if (null != sorts && sorts.length > 0) {
					List<String> sortList = new ArrayList<>();
					for (String sort : sorts) {
						if (sort.startsWith("-"))
							sortList.add(String.format("%s DESC", StringUtil.trimStart(sort, "-")));
						else
							sortList.add(String.format("%s ASC", StringUtil.trimStart(sort, "+")));
					}
					pager.setOrderBy(StringUtil.join(", ", sortList));
				}
			} else {
				// 其他全部当成where条件处理
				pager.setWhereMap(item.getKey(), item.getValue()[0]);
			}
		}
		return pager;
	}

	// region JsonResult
	/**
	 * JsonResult
	 * 
	 * @param success
	 * @param code
	 * @param message
	 * @return
	 */
	protected JsonResult toJsonResult(boolean success, int code, String message) {
		JsonResult jsonResult = new JsonResult(success, code, message);
		return jsonResult;
	}

	/**
	 * JsonResult
	 * 
	 * @param success
	 * @param code
	 * @param message
	 * @param data
	 * @return
	 */
	protected JsonResult toJsonResult(boolean success, int code, String message, Object data) {
		JsonResult jsonResult = new JsonResult(success, code, message, data);
		return jsonResult;
	}

	/**
	 * 成功JsonResult
	 * 
	 * @param message
	 * @return
	 */
	protected SuccessJsonResult toSuccessJsonResult(String message) {
		return new SuccessJsonResult(message);
	}

	/**
	 * 成功JsonResult
	 * 
	 * @param message
	 * @return
	 */
	protected SuccessJsonResult toSuccessJsonResult(Object data) {
		return new SuccessJsonResult(data);
	}

	/**
	 * 成功JsonResult
	 * 
	 * @param message
	 * @param data
	 * @return
	 */
	protected SuccessJsonResult toSuccessJsonResult(String message, Object data) {
		return new SuccessJsonResult(message, data);
	}

	/**
	 * 失败JsonResult
	 * 
	 * @param code
	 * @param message
	 * @return
	 */
	protected FailureJsonResult toFailureJsonResult(int code, String message) {
		return new FailureJsonResult(code, message);
	}

	/**
	 * 失败JsonResult
	 * 
	 * @param code
	 * @param message
	 * @return
	 */
	protected FailureJsonResult toFailureJsonResult(int code, Object data) {
		return new FailureJsonResult(code, data);
	}

	/**
	 * 失败JsonResult
	 * 
	 * @param code
	 * @param message
	 * @param data
	 * @return
	 */
	protected FailureJsonResult toFailureJsonResult(int code, String message, Object data) {
		return new FailureJsonResult(code, message, data);
	}
	// endregion
}
