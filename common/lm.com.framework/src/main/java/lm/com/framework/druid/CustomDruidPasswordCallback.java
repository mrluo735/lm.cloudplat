package lm.com.framework.druid;

import java.util.Properties;

import com.alibaba.druid.util.DruidPasswordCallback;

import lm.com.framework.StringUtil;
import lm.com.framework.encrypt.Base64Encrypt;

/**
 * 自定义密码库密码加密
 * <p>
 * base64
 * </p>
 * 
 * @author mrluo735
 *
 */
@SuppressWarnings("serial")
public class CustomDruidPasswordCallback extends DruidPasswordCallback {
	public void setProperties(Properties properties) {
		super.setProperties(properties);
		String pwd = properties.getProperty("password");
		if (!StringUtil.isNullOrWhiteSpace(pwd)) {
			try {
				// 这里的password是将jdbc.properties配置得到的密码进行解密之后的值
				// 所以这里的代码是将密码进行解密
				// TODO 将pwd进行解密;
				String password = Base64Encrypt.decode2String(pwd);
				setPassword(password.toCharArray());
			} catch (Exception e) {
				setPassword(pwd.toCharArray());
			}
		}
	}
}
