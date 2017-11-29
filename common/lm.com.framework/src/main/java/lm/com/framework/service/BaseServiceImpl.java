/**
 * @title BaseSerivce.java
 * @description TODO
 * @package lm.com.service
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年12月23日下午2:07:45
 * @version v1.0
 */
package lm.com.framework.service;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Locale;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpStatus;

import lm.com.framework.JsonUtil;
import lm.com.framework.ReflectUtil;
import lm.com.framework.ThreadUtil;

/**
 * 所有服务接口抽象实现类
 * 
 * @author mrluo735
 *
 */
public abstract class BaseServiceImpl implements IService {
	/**
	 * 日志
	 */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private DataSource dataSource;

	@Autowired
	protected ReloadableResourceBundleMessageSource messageSource;

	/**
	 * 获取数据库驱动名称
	 * 
	 * @return
	 * @throws SQLException
	 */
	protected String getDriverName() throws SQLException {
		DatabaseMetaData databaseMetaData = this.dataSource.getConnection().getMetaData();
		return databaseMetaData.getDriverName();
	}

	/**
	 * 执行
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public ResponseDTO execute(RequestDTO request) {
		ResponseDTO resultDTO = new ResponseDTO();
		long nanoTime = System.nanoTime();
		String contextJson = "", paramJson = "";
		try {
			if (null != request.getContext())
				contextJson = JsonUtil.toJsonUseJackson(request.getContext());
			paramJson = JsonUtil.toJsonUseJackson(request);

			return (ResponseDTO) ReflectUtil.invokeMethod(getService(), request.getMethod(), request);
		} catch (Exception ex) {
			logger.error("执行出错，错误原因={}", ex);
			resultDTO.setSuccess(false);
			resultDTO.setCode(ex.hashCode());
			resultDTO.setMessage(ex.getMessage());
		} finally {
			logger.info("------- 完成请求: Method={}, 花费时间(毫秒)={}, 登录信息={}, 参数={}--------",
					new Object[] { getService().getClass().getSimpleName() + "." + request.getMethod(),
							(System.nanoTime() - nanoTime) / 1000000, contextJson, paramJson });
		}
		return resultDTO;
	}

	/**
	 * 获取提示信息
	 * 
	 * @param code
	 * @param locale
	 * @return
	 */
	protected String getMessage(int code, Locale locale) {
		String className = ThreadUtil.getCurrentClassName(3);
		String methodName = ThreadUtil.getCurrentMethodName(3);
		String key = String.format("%s.%s.%s", className, methodName, code);
		return this.messageSource.getMessage(key, null, locale);
	}

	/**
	 * 异常结果
	 * 
	 * @param locale
	 * @return
	 */
	protected ResponseDTO exception(Locale locale) {
		ResponseDTO result = new ResponseDTO();
		result.setSuccess(false);
		result.setCode(HttpStatus.EXPECTATION_FAILED.value());
		result.setMessage(
				messageSource.getMessage(String.valueOf(HttpStatus.EXPECTATION_FAILED.value()), null, locale));
		return result;
	}

	/**
	 * 重载+1 获取服务
	 * 
	 * @return
	 */
	@Deprecated
	public BaseServiceImpl getService() {
		try {
			return getClass().newInstance();
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}
		return null;
	}

	/**
	 * 重载+2 获取服务
	 * 
	 * @param method
	 * @return
	 */
	public String getService(String method) {
		throw new RuntimeException("调用前子类必须实现此方法!");
	}
}
