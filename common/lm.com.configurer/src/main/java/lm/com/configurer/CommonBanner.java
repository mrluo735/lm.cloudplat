/**
 * @title CommonBanner.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.com.configurer
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年5月19日下午4:24:29
 * @version v1.0.0
 */
package lm.com.configurer;

import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.Formatter;
import java.util.Properties;

import javax.crypto.Cipher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.core.env.Environment;

import lm.com.framework.AsciiArtUtil;
import lm.com.framework.DateTimeUtil;

/**
 * @ClassName: CommonBanner
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年5月19日 下午4:24:29
 * 
 */
public class CommonBanner implements Banner {
	private static final Logger logger = LoggerFactory.getLogger(CommonBanner.class);

	private String asciiArt = "(LM)";
	private String version = "v1.0.0";

	/**
	 * 
	 * @param asciiArt
	 */
	public void setAsciiArt(String asciiArt) {
		this.asciiArt = asciiArt;
	}

	/**
	 * 
	 * @param version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	public CommonBanner() {

	}

	public CommonBanner(String asciiArt, String version) {
		this.asciiArt = asciiArt;
		this.version = version;
	}

	@Override
	public void printBanner(final Environment environment, final Class<?> sourceClass, final PrintStream out) {
		AsciiArtUtil.printAsciiArt(out, this.asciiArt, collectEnvironmentInfo());
	}

	/**
	 * Collect environment info with details on the java and os deployment
	 * versions.
	 *
	 * @return environment info
	 */
	private String collectEnvironmentInfo() {
		final Properties properties = System.getProperties();
		try (Formatter formatter = new Formatter()) {
			formatter.format("Version: %s%n", this.version);

			formatApacheTomcatVersion(formatter);

			formatter.format("Build Date/Time: %s%n", DateTimeUtil.getNow(DateTimeUtil.DEFAULT_PATTERN));
			// formatter.format("System Temp Directory: %s%n",
			// FileUtils.getTempDirectoryPath());
			formatter.format("Java Home: %s%n", properties.get("java.home"));
			formatter.format("Java Vendor: %s%n", properties.get("java.vendor"));
			formatter.format("Java Version: %s%n", properties.get("java.version"));
			formatter.format("JCE Installed: %s%n", isJceInstalled());
			formatter.format("OS Architecture: %s%n", properties.get("os.arch"));
			formatter.format("OS Name: %s%n", properties.get("os.name"));
			formatter.format("OS Version: %s%n", properties.get("os.version"));
			return formatter.toString();
		}
	}

	private void formatApacheTomcatVersion(final Formatter formatter) {
		try {
			final Class clz = Class.forName("org.apache.catalina.util.ServerInfo");
			final Method method = clz.getMethod("getServerInfo");
			final Object version = method.invoke(null);
			formatter.format("Apache Tomcat Version: %s%n", version);
		} catch (final Exception e) {
			logger.trace(e.getMessage(), e);
		}
	}

	private boolean isJceInstalled() {
		try {
			final int maxKeyLen = Cipher.getMaxAllowedKeyLength("AES");
			return maxKeyLen == Integer.MAX_VALUE;
		} catch (final Exception e) {
			return false;
		}
	}
}
