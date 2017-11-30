package lm.cloud.zuul.server;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import lm.cloud.zuul.server.filter.PasswordGrantZuulFilter;
import lm.cloud.zuul.server.filter.CsrfHeaderFilter;

/**
 * 应用程序启动类
 *
 */
@SpringCloudApplication
@EnableZuulProxy
@EnableOAuth2Sso
public class App extends WebSecurityConfigurerAdapter {
	private final Logger logger = LoggerFactory.getLogger(App.class);

	@Value("${zuul.security.ignoreUrls}")
	private String[] ignoreUrls;
	
	/**
	 * 启动入口
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		new SpringApplicationBuilder(App.class).web(true).run(args);
	}

	// @Bean
	// public AccessFilter accessFilter() {
	// return new AccessFilter();
	// }

	/**
	 * Password授权Filter
	 * 
	 * @return
	 */
	@Bean
	public PasswordGrantZuulFilter passwordGrantZuulFilter() {
		return new PasswordGrantZuulFilter();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// web.ignoring().antMatchers("/**/*.css", "/**/*.js", "/favicon.ico");
		//web.ignoring().antMatchers("/token", "/secureshell/**");
		web.ignoring().antMatchers(ignoreUrls);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http.authorizeRequests()
				.antMatchers("/", "/**/*.html").permitAll()
				.anyRequest().authenticated()
			.and()
				.logout().logoutUrl("/logout").permitAll()
			.and()
				.csrf().csrfTokenRepository(csrfTokenRepository())
			.and()
				.addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class);
		
//		http.csrf().disable();
//		http.headers().disable();
		// http.httpBasic().disable();
		// @formatter:on
	}

	private CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-XSRF-TOKEN");
		return repository;
	}

	@PostConstruct
	public void construct() {
		logger.info("Zuul Api-Gateway 启动成功");
	}
	
	@PreDestroy
    public void destory() {
		logger.info("Zuul Api-Gateway 销毁成功");
    }
}
