/**
 * @title WebContainerUtil.java
 * @description TODO
 * @package lm.com.framework
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年12月23日上午8:54:48
 * @version v1.0
 */
package lm.com.framework;

/**
 * Web容器工具类
 * 
 * @author mrluo735
 *
 */
public class WebContainerUtil {
	private static WebContainerUtil _instance = new WebContainerUtil();
	private String _serverId;
	private Boolean _geronimo;
	private Boolean _glassfish;
	private Boolean _jBoss;
	private Boolean _jetty;
	private Boolean _jonas;
	private Boolean _oc4j;
	private Boolean _resin;
	private Boolean _tomcat;
	private Boolean _webLogic;
	private Boolean _webSphere;

	public static final String GERONIMO_ID = "geronimo";
	public static final String GLASSFISH_ID = "glassfish";
	public static final String JBOSS_ID = "jboss";
	public static final String JETTY_ID = "jetty";
	public static final String JONAS_ID = "jonas";
	public static final String OC4J_ID = "oc4j";
	public static final String RESIN_ID = "resin";
	public static final String TOMCAT_ID = "tomcat";
	public static final String WEBLOGIC_ID = "weblogic";
	public static final String WEBSPHERE_ID = "websphere";

	/**
	 * 获取容器
	 * 
	 * @return
	 */
	public static String getServerId() {
		WebContainerUtil sd = _instance;
		if (sd._serverId == null) {
			if (isGeronimo()) {
				sd._serverId = "geronimo";
			} else if (isGlassfish()) {
				sd._serverId = "glassfish";
			} else if (isJBoss()) {
				sd._serverId = "jboss";
			} else if (isJonas()) {
				sd._serverId = "jonas";
			} else if (isOC4J()) {
				sd._serverId = "oc4j";
			} else if (isResin()) {
				sd._serverId = "resin";
			} else if (isWeblogic()) {
				sd._serverId = "weblogic";
			} else if (isWebsphere()) {
				sd._serverId = "websphere";
			}
			if (isJetty()) {
				if (sd._serverId == null) {
					sd._serverId = "jetty";
				} else {
					sd._serverId += "-jetty";
				}
			} else if (isTomcat()) {
				if (sd._serverId == null) {
					sd._serverId = "tomcat";
				} else {
					sd._serverId += "-tomcat";
				}
			}
			if (sd._serverId == null) {
				throw new RuntimeException("Server is not supported");
			}
		}
		return sd._serverId;
	}

	/**
	 * 
	 * @return
	 */
	public static boolean isGeronimo() {
		WebContainerUtil sd = _instance;
		if (sd._geronimo == null) {
			sd._geronimo = _detect("/org/apache/geronimo/system/main/Daemon.class");
		}
		return sd._geronimo.booleanValue();
	}

	/**
	 * 
	 * @return
	 */
	public static boolean isGlassfish() {
		WebContainerUtil sd = _instance;
		if (sd._glassfish == null) {
			String value = System.getProperty("com.sun.aas.instanceRoot");
			if (value != null) {
				sd._glassfish = Boolean.TRUE;
			} else {
				sd._glassfish = Boolean.FALSE;
			}
		}
		return sd._glassfish.booleanValue();
	}

	/**
	 * 
	 * @return
	 */
	public static boolean isJBoss() {
		WebContainerUtil sd = _instance;
		if (sd._jBoss == null) {
			sd._jBoss = _detect("/org/jboss/Main.class");
		}
		return sd._jBoss.booleanValue();
	}

	/**
	 * 
	 * @return
	 */
	public static boolean isJetty() {
		WebContainerUtil sd = _instance;
		if (sd._jetty == null) {
			sd._jetty = _detect("/org/mortbay/jetty/Server.class");
		}
		return sd._jetty.booleanValue();
	}

	/**
	 * 
	 * @return
	 */
	public static boolean isJonas() {
		WebContainerUtil sd = _instance;
		if (sd._jonas == null) {
			sd._jonas = _detect("/org/objectweb/jonas/server/Server.class");
		}
		return sd._jonas.booleanValue();
	}

	/**
	 * 
	 * @return
	 */
	public static boolean isOC4J() {
		WebContainerUtil sd = _instance;
		if (sd._oc4j == null) {
			sd._oc4j = _detect("oracle.oc4j.util.ClassUtils");
		}
		return sd._oc4j.booleanValue();
	}

	/**
	 * 
	 * @return
	 */
	public static boolean isResin() {
		WebContainerUtil sd = _instance;
		if (sd._resin == null) {
			sd._resin = _detect("/com/caucho/server/resin/Resin.class");
		}
		return sd._resin.booleanValue();
	}

	public static boolean isSupportsComet() {
		return false;
	}

	/**
	 * 
	 * @return
	 */
	public static boolean isTomcat() {
		WebContainerUtil sd = _instance;
		if (sd._tomcat == null) {
			sd._tomcat = _detect("/org/apache/catalina/startup/Bootstrap.class");
		}
		if (sd._tomcat == null) {
			sd._tomcat = _detect("/org/apache/catalina/startup/Embedded.class");
		}
		return sd._tomcat.booleanValue();
	}

	/**
	 * 
	 * @return
	 */
	public static boolean isWeblogic() {
		WebContainerUtil sd = _instance;
		if (sd._webLogic == null) {
			sd._webLogic = _detect("/weblogic/Server.class");
		}
		return sd._webLogic.booleanValue();
	}

	/**
	 * 
	 * @return
	 */
	public static boolean isWebsphere() {
		WebContainerUtil sd = _instance;
		if (sd._webSphere == null) {
			sd._webSphere = _detect("/com/ibm/websphere/product/VersionInfo.class");
		}
		return sd._webSphere.booleanValue();
	}

	/**
	 * 
	 * @param className
	 * @return
	 */
	private static Boolean _detect(String className) {
		try {
			ClassLoader.getSystemClassLoader().loadClass(className);
			return Boolean.TRUE;
		} catch (ClassNotFoundException cnfe) {
			WebContainerUtil sd = _instance;

			Class<?> c = sd.getClass();
			if (c.getResource(className) != null) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
}
