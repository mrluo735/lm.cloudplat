/**
 * @title UserController.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.cloud.oauth.server.controller
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年5月10日下午1:32:24
 * @version v1.0.0
 */
package lm.cloud.oauth.server.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName: UserController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年5月10日 下午1:32:24
 * 
 */
@RestController
public class UserController {
	/**
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping("/user")
	public Principal user(Principal user) {
		return user;
	}
}
