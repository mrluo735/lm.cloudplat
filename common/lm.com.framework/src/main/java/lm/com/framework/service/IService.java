/**
 * @title IService.java
 * @description TODO
 * @package lm.com.service
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年12月23日上午11:21:37
 * @version v1.0
 */
package lm.com.framework.service;

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
	 * @param requestDTO
	 * @return
	 */
	public ResponseDTO execute(RequestDTO requestDTO);

	/**
	 * GET请求执行
	 * 
	 * @param requestDTO
	 * @return
	 */
	public ResponseDTO get(RequestDTO requestDTO);

	/**
	 * POST请求执行
	 * 
	 * @param requestDTO
	 * @return
	 */
	public ResponseDTO post(RequestDTO requestDTO);

	/**
	 * PUT请求执行
	 * 
	 * @param requestDTO
	 * @return
	 */
	public ResponseDTO put(RequestDTO requestDTO);

	/**
	 * DELETE请求执行
	 * 
	 * @param requestDTO
	 * @return
	 */
	public ResponseDTO delete(RequestDTO requestDTO);
}
