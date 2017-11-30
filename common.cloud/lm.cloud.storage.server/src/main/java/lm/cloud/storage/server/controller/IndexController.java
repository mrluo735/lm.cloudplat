/**
 * @title IndexController.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.cloud.storage.server.controller
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年6月13日下午3:47:03
 * @version v1.0.0
 */
package lm.cloud.storage.server.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @ClassName: IndexController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年6月13日 下午3:47:03
 * 
 */
@Controller
public class IndexController {
	/**
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/")
	@ResponseBody
	public String index(HttpServletRequest request) {
		return "LM 云存储";
	}
}
