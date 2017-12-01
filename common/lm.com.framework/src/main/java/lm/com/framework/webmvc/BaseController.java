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
import lm.com.framework.service.RequestDTO;
import lm.com.framework.service.SortDTO;
import lm.com.framework.sqlmedium.Pager;
import lm.com.framework.sqlmedium.SqlKey;
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

	// region 私有字段
	private static final List<String> COLPATTERN_LIST = new ArrayList<String>() {
		private static final long serialVersionUID = -3609330577976610952L;
		{
			add(Pager.COLUMNPATTEN);
			add("fields");
		}
	};
	private static final List<String> PAGEINDEX_LIST = new ArrayList<String>() {
		private static final long serialVersionUID = -3609330577976610952L;
		{
			add(Pager.PAGEINDEX);
			add("page");
		}
	};
	private static final List<String> PAGESIZE_LIST = new ArrayList<String>() {
		private static final long serialVersionUID = -3609330577976610952L;
		{
			add(Pager.PAGESIZE);
			add("per_page");
		}
	};
	private static final List<String> ISSTATCOUNT_LIST = new ArrayList<String>() {
		private static final long serialVersionUID = -3609330577976610952L;
		{
			add(Pager.ISSTATCOUNT);
			add("stat");
		}
	};
	private static final List<String> ORDERBY_LIST = new ArrayList<String>() {
		private static final long serialVersionUID = -3609330577976610952L;
		{
			add(Pager.ORDERBY);
			add("sorts");
		}
	};
	// endregion

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
			if (COLPATTERN_LIST.contains(item.getKey()))
				pager.setColumnPattern(item.getValue()[0]);
			else if (PAGEINDEX_LIST.contains(item.getKey()))
				pager.setPageIndex(IntegerUtil.toInteger(item.getValue()[0], 1));
			else if (PAGESIZE_LIST.contains(item.getKey()))
				pager.setPageSize(IntegerUtil.toInteger(item.getValue()[0], 20));
			else if (ISSTATCOUNT_LIST.contains(item.getKey()))
				pager.setIsStatCount(BooleanUtil.toBoolean(item.getValue()[0]));
			else if (ORDERBY_LIST.contains(item.getKey())) {
				String[] sorts = item.getValue()[0].split(",");
				if (null != sorts && sorts.length > 0) {
					List<String> sortList = new ArrayList<>();
					for (String sort : sorts) {
						if (sort.startsWith("-"))
							sortList.add(String.format("%s %s", StringUtil.trimStart(sort, "-"), SqlKey.DESC));
						else
							sortList.add(String.format("%s %s", StringUtil.trimStart(sort, "+"), SqlKey.ASC));
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

	/**
	 * 获取请求传输对象
	 * 
	 * @param request
	 * @return
	 */
	protected RequestDTO getRequestDTO(HttpServletRequest request) {
		RequestDTO requestDTO = new RequestDTO();
		Map<String, String[]> parameters = request.getParameterMap();
		if (null != parameters) {
			for (Entry<String, String[]> item : parameters.entrySet()) {
				if (COLPATTERN_LIST.contains(item.getKey()))
					requestDTO.put(Pager.COLUMNPATTEN, item.getValue()[0]);
				else if (PAGEINDEX_LIST.contains(item.getKey()))
					requestDTO.put(Pager.PAGEINDEX, IntegerUtil.toInteger(item.getValue()[0], 1));
				else if (PAGESIZE_LIST.contains(item.getKey()))
					requestDTO.put(Pager.PAGESIZE, IntegerUtil.toInteger(item.getValue()[0], 20));
				else if (ISSTATCOUNT_LIST.contains(item.getKey()))
					requestDTO.put(Pager.ISSTATCOUNT, BooleanUtil.toBoolean(item.getValue()[0]));
				else if (ORDERBY_LIST.contains(item.getKey())) {
					String[] sorts = item.getValue()[0].split(",");
					if (null != sorts && sorts.length > 0) {
						for (String sort : sorts) {
							if (sort.startsWith("-"))
								requestDTO.setSort(new SortDTO(StringUtil.trimStart(sort, "-"), false));
							else
								requestDTO.setSort(new SortDTO(StringUtil.trimStart(sort, "+"), true));
						}
					}
				} else {
					if (item.getValue().length > 1)
						requestDTO.put(item.getKey(), item.getValue());
					else
						requestDTO.put(item.getKey(), item.getValue()[0]);
				}
			}
		}
		return requestDTO;
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
