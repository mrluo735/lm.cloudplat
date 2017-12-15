package lm.com.framework.jmx;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import lm.com.framework.StringUtil;

/**
 * 
 * @author mrluo735
 *
 */
public class JmxHelper {
	private static final String URL_FORMAT = "service:jmx:rmi:///jndi/rmi://%s:%s/jmxrmi";
	private static final String JMI_OBJECT = "JMImplementation:type=MBeanServerDelegate";
	private static final String OPERATINGSYSTEM_OBJECT = "java.lang:type=OperatingSystem";
	private JMXConnector connector;
	private String host;
	private int port;
	private String userName;
	private String password;

	/**
	 * 
	 * @return
	 */
	public JMXConnector getConnector() {
		try {
			JMXServiceURL serivceUrl = new JMXServiceURL(String.format(URL_FORMAT, host, port));
			Map<String, Object> environment = new HashMap<String, Object>();
			if (!StringUtil.isNullOrWhiteSpace(userName)) {
				String[] credentials = new String[] { this.userName, this.password };
				environment.put("jmx.remote.credentials", credentials);
			}
			this.connector = JMXConnectorFactory.connect(serivceUrl, environment);
		} catch (Exception e) {
		}
		return this.connector;
	}

	/**
	 * 关闭Connector
	 */
	public void closeConnector() {
		if (this.connector != null) {
			try {
				this.connector.close();
			} catch (IOException e) {
			}
		}
	}

	public void OperatingSystem() {
		try {
			ObjectName objectName = new ObjectName(OPERATINGSYSTEM_OBJECT);
			MBeanServerConnection serverConnection = this.connector.getMBeanServerConnection();
		} catch (Exception e) {
		}
	}
}
