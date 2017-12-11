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

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lm.com.framework.IntegerUtil;
import lm.com.framework.JsonUtil;
import lm.com.framework.ReflectUtil;
import lm.com.framework.StringUtil;
import lm.com.framework.ThreadUtil;
import lm.com.framework.http.EnumHttpStatus;
import lm.com.framework.webmvc.SpringApplicationContext;

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
	private static final ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	/**
	 * 
	 */
	protected Map<String[], String[]> CLS_METHOD_MAP = new HashMap<String[], String[]>();

	@Autowired
	private DataSource dataSource;
	@Autowired
	protected MessageSource messageSource;

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
	 * 初始化
	 * 
	 * @param method
	 * @return
	 */
	protected void initClsMethod(String... locationPattern) {
		if (locationPattern == null)
			return;

		for (String location : locationPattern) {
			this.parseClsMethod(location);
		}
	}

	/**
	 * 初始化
	 * 
	 * @param method
	 * @return
	 */
	private void parseClsMethod(String locationPattern) {
		try {
			Resource[] resources = resourcePatternResolver.getResources(locationPattern);
			MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
			for (Resource resource : resources) {
				if (!resource.isReadable())
					continue;

				MetadataReader reader = readerFactory.getMetadataReader(resource);
				String className = reader.getClassMetadata().getClassName();
				Annotation[] annotationArray = resource.getClass().getAnnotations();
				if (annotationArray == null)
					continue;

				// 获取类的RequestMapping的value值
				String clsUrl = "";
				for (Annotation annotation : annotationArray) {
					if (annotation.annotationType().equals(RequestMapping.class)) {
						Method valueMethod = annotation.annotationType().getMethod("value");
						String[] valueArray = (String[]) valueMethod.invoke(annotation);
						clsUrl = valueArray == null ? "" : valueArray[0];
						break;
					}
				}
				Method[] methodArray = ReflectUtil.getDeclaredMethods(className);
				if (methodArray == null)
					continue;
				for (Method method : methodArray) {
					Annotation[] methodAnnoArray = method.getAnnotations();
					if (methodAnnoArray == null)
						continue;

					String methodUrl = "";
					String httpMethod = HttpMethod.GET.name();
					for (Annotation annotation : methodAnnoArray) {
						if (annotation.annotationType().equals(RequestMapping.class)) {
							Method valueMethod = annotation.annotationType().getMethod("value");
							String[] valueArray = (String[]) valueMethod.invoke(annotation);
							if (valueArray == null || valueArray.length < 1)
								continue;

							Method mthMethod = annotation.annotationType().getMethod("method");
							RequestMethod[] requestMethodArray = (RequestMethod[]) mthMethod.invoke(annotation);
							if (requestMethodArray == null || requestMethodArray.length < 1)
								continue;

							methodUrl = valueArray[0];
							httpMethod = requestMethodArray[0].name();
							break;
						} else if (annotation.annotationType().equals(GetMapping.class)) {
							Method valueMethod = annotation.annotationType().getMethod("value");
							String[] valueArray = (String[]) valueMethod.invoke(annotation);
							if (valueArray == null || valueArray.length < 1)
								continue;

							methodUrl = valueArray[0];
							httpMethod = HttpMethod.GET.name();
						} else if (annotation.annotationType().equals(PostMapping.class)) {
							Method valueMethod = annotation.annotationType().getMethod("value");
							String[] valueArray = (String[]) valueMethod.invoke(annotation);
							if (valueArray == null || valueArray.length < 1)
								continue;

							methodUrl = valueArray[0];
							httpMethod = HttpMethod.POST.name();
						} else if (annotation.annotationType().equals(PutMapping.class)) {
							Method valueMethod = annotation.annotationType().getMethod("value");
							String[] valueArray = (String[]) valueMethod.invoke(annotation);
							if (valueArray == null || valueArray.length < 1)
								continue;

							methodUrl = valueArray[0];
							httpMethod = HttpMethod.PUT.name();
						} else if (annotation.annotationType().equals(DeleteMapping.class)) {
							Method valueMethod = annotation.annotationType().getMethod("value");
							String[] valueArray = (String[]) valueMethod.invoke(annotation);
							if (valueArray == null || valueArray.length < 1)
								continue;

							methodUrl = valueArray[0];
							httpMethod = HttpMethod.DELETE.name();
						}

						if (StringUtil.isNullOrWhiteSpace(methodUrl))
							continue;

						String[] mKey = new String[] { clsUrl + methodUrl, httpMethod };
						String[] mValue = new String[] { className, method.getName() };
						CLS_METHOD_MAP.put(mKey, mValue);
					}
				}
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 获取数据库驱动名称
	 * 
	 * @return
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
	 * 执行
	 * 
	 * @param requestDTO
	 * @return
	 * @throws Exception
	 */
	public ResponseDTO execute(RequestDTO requestDTO) {
		ResponseDTO responseDTO = new ResponseDTO();
		long nanoTime = System.nanoTime();
		String clsName = "", identityJson = "", paramJson = "";
		try {
			if (null != requestDTO.getIdentity())
				identityJson = JsonUtil.toJsonUseJackson(requestDTO.getIdentity());
			paramJson = JsonUtil.toJsonUseJackson(requestDTO);

			String[] mKey = new String[] { requestDTO.getUrl(), requestDTO.getHttpMethod().name() };
			String[] mValue = CLS_METHOD_MAP.get(mKey);
			Object bean = SpringApplicationContext.getBean(mValue[0]);
			clsName = bean.toString();
			return (ResponseDTO) ReflectUtil.invokeMethod(bean, mValue[1], requestDTO);
		} catch (Exception ex) {
			logger.error("执行出错，错误原因={}", ex);
			responseDTO.setSuccess(false);
			responseDTO.setCode(ex.hashCode());
			responseDTO.setMessage(ex.getMessage());
		} finally {
			logger.info("------- 完成请求: Url={}, 花费时间(毫秒)={}, 身份信息={}, 参数={}--------",
					new Object[] { clsName + "." + requestDTO.getUrl(), (System.nanoTime() - nanoTime) / 1000000,
							identityJson, paramJson });
		}
		return responseDTO;
	}

	/**
	 * GET请求执行
	 * 
	 * @param requestDTO
	 * @return
	 */
	public ResponseDTO get(RequestDTO requestDTO) {
		requestDTO.setHttpMethod(HttpMethod.GET);
		return this.execute(requestDTO);
	}

	/**
	 * POST请求执行
	 * 
	 * @param requestDTO
	 * @return
	 */
	public ResponseDTO post(RequestDTO requestDTO) {
		requestDTO.setHttpMethod(HttpMethod.POST);
		return this.execute(requestDTO);
	}

	/**
	 * PUT请求执行
	 * 
	 * @param requestDTO
	 * @return
	 */
	public ResponseDTO put(RequestDTO requestDTO) {
		requestDTO.setHttpMethod(HttpMethod.PUT);
		return this.execute(requestDTO);
	}

	/**
	 * DELETE请求执行
	 * 
	 * @param requestDTO
	 * @return
	 */
	public ResponseDTO delete(RequestDTO requestDTO) {
		requestDTO.setHttpMethod(HttpMethod.DELETE);
		return this.execute(requestDTO);
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
		return this.messageSource.getMessage(key, null, "", locale);
	}

	/**
	 * 重载+2 获取提示信息
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
		return this.messageSource.getMessage(key, null, "", locale);
	}

	/**
	 * 重载+1 获取返回结果
	 * 
	 * @param rowCount
	 * @param locale
	 * @return
	 */
	protected ResponseDTO doReturnResult(ResponseDTO responseDTO, int rowCount, Locale locale) {
		final ResponseDTO respDTO = responseDTO;
		Integer code = EnumHttpStatus.Two_OK.getValue();
		String message = getMessage(code, locale, 4);
		if (rowCount > 0) {
			respDTO.setMessage(message);
			return respDTO;
		}
		code = IntegerUtil.toInteger(EnumHttpStatus.Four_BadRequest.getValue().toString() + Math.abs(rowCount),
				EnumHttpStatus.Four_BadRequest.getValue());
		message = getMessage(code, locale, 4);

		respDTO.setSuccess(false);
		respDTO.setCode(code);
		respDTO.setMessage(message);
		return respDTO;
	}

	/**
	 * 重载+2 获取返回结果
	 * 
	 * @param rowCount
	 * @param locale
	 * @return
	 */
	protected <T> ResponseDTO doReturnResult(ResponseDTO responseDTO, T data, Locale locale) {
		final ResponseDTO respDTO = responseDTO;
		if (data != null) {
			respDTO.setData(data);
			return respDTO;
		}

		respDTO.setSuccess(false);
		respDTO.setCode(EnumHttpStatus.Four_BadRequest.getValue());
		respDTO.setMessage(EnumHttpStatus.Four_BadRequest.getDescription());
		return respDTO;
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
