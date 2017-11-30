package lm.cloud.oauth.server.oauth2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import lm.com.framework.StringUtil;

/**
 * 
 * @ClassName: CustomUserDetailsService
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年5月10日 上午11:47:03
 *
 */
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Map<String, Object> oAuthUser = jdbcTemplate
				.queryForMap("SELECT * FROM oauth_user WHERE loginName = '" + username + "'");
		if (oAuthUser == null || oAuthUser.get("loginName") == null)
			throw new UsernameNotFoundException("Not found any user for username[" + username + "]");

		Collection<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
		String privileges = (String) oAuthUser.get("privileges");
		if (!StringUtil.isNullOrWhiteSpace(privileges)) {
			for (String privilege : privileges.split(",")) {
				GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(String.format("ROLE_%s", privilege));
				authorities.add(grantedAuthority);
			}
		}
		return new User(oAuthUser.get("loginName").toString(), oAuthUser.get("password").toString(), authorities);
	}
}
