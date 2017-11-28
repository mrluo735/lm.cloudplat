/**
 * @title IService.java
 * @description TODO
 * @package lm.com.service
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年12月23日上午11:21:37
 * @version v1.0
 */
package lm.com.service;

/**
 * 所有服务接口基类
 * 
 * @author mrluo735
 *
 */
public interface IService {
	/**
	 * 执行
	 * 
	 * @param request
	 * @return
	 */
	public ResponseDTO<Object> execute(RequestDTO request);
}
