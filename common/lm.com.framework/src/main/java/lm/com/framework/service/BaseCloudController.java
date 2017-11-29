/**
 * @title BaseCloudController.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.com.service
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年3月14日上午11:07:29
 * @version v1.0.0
 */
package lm.com.framework.service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Locale;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import lm.com.framework.IntegerUtil;
import lm.com.framework.ThreadUtil;
import lm.com.framework.http.EnumHttpStatus;

/**
 * @ClassName: BaseCloudController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年3月14日 上午11:07:29
 * 
 */
@RestController
public class BaseCloudController {
	/**
	 * 日志
	 */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private DataSource dataSource;

	@Autowired
	private MessageSource messageSource;

	/**
	 * 获取数据库驱动名称
	 * 
	 * @return
	 * @throws SQLException
	 */
	protected String getDriverName() {
		Connection connection = null;
		try {
			connection = this.dataSource.getConnection();
			DatabaseMetaData databaseMetaData = connection.getMetaData();
			return databaseMetaData.getDriverName();
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				if (null != connection && !connection.isClosed())
					connection.close();
			} catch (SQLException ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	/**
	 * 重载+1 获取提示信息
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
	 * 重载+2 获取提示信息
	 * 
	 * @param code
	 * @param locale
	 * @return
	 */
	protected String getMessage(String code, Locale locale) {
		String className = ThreadUtil.getCurrentClassName(3);
		String methodName = ThreadUtil.getCurrentMethodName(3);
		String key = String.format("%s.%s.%s", className, methodName, code);
		return this.messageSource.getMessage(key, null, locale);
	}

	/**
	 * 重载+3 获取提示信息
	 * 
	 * @param code
	 * @param locale
	 * @param level
	 * @return
	 */
	protected String getMessage(int code, Locale locale, int level) {
		String className = ThreadUtil.getCurrentClassName(level);
		String methodName = ThreadUtil.getCurrentMethodName(level);
		String key = String.format("%s.%s.%s", className, methodName, code);
		return this.messageSource.getMessage(key, null, locale);
	}

	/**
	 * 重载+1 获取返回结果
	 * 
	 * @param rowCount
	 * @param locale
	 * @return
	 */
	protected void doReturnResult(ResponseDTO responseDTO, int rowCount, Locale locale) {
		Integer code = EnumHttpStatus.Two_OK.getValue();
		String message = getMessage(code, locale, 4);
		if (rowCount > 0) {
			responseDTO.setMessage(message);
			return;
		}
		code = IntegerUtil.toInteger(EnumHttpStatus.Four_BadRequest.getValue().toString() + Math.abs(rowCount),
				EnumHttpStatus.Four_BadRequest.getValue());
		message = getMessage(code, locale, 4);

		responseDTO.setSuccess(false);
		responseDTO.setCode(code);
		responseDTO.setMessage(message);
	}

	/**
	 * 重载+2 获取返回结果
	 * 
	 * @param rowCount
	 * @param locale
	 * @return
	 */
	protected <T> void doReturnResult(ResponseDTO responseDTO, T data, Locale locale) {
		if (data != null) {
			responseDTO.setData(data);
			return;
		}

		responseDTO.setSuccess(false);
		responseDTO.setCode(EnumHttpStatus.Four_BadRequest.getValue());
		responseDTO.setMessage(EnumHttpStatus.Four_BadRequest.getDescription());
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
}
