/**
 * @title NetUtil.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.com.framework
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年4月26日上午10:10:11
 * @version v1.0.0
 */
package lm.com.framework;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 网络工具类
 * 
 * @ClassName: NetUtil
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年4月26日 上午10:10:11
 * 
 */
public class NetUtil {
	/**
	 * 获取主机名
	 * 
	 * @return
	 */
	public static String getHostName() {
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			// 主机名
			String hostName = inetAddress.getHostName();
			return hostName;
		} catch (UnknownHostException ex) {

		}
		return "";
	}

	/**
	 * 获取本地ip
	 * 
	 * @return
	 */
	public static String getLocalIP() {
		try {
			InetAddress inetAddress = InetAddress.getLocalHost();
			return inetAddress.getHostAddress();
		} catch (UnknownHostException ex) {

		}
		return "";
	}

	/**
	 * 获取本地IP列表
	 * <p>
	 * 针对多网卡情况）
	 * </p>
	 * 
	 * @return
	 */
	public static List<String> getLocalIPList() {
		List<String> ipList = new ArrayList<String>();
		try {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			NetworkInterface networkInterface;
			Enumeration<InetAddress> inetAddresses;
			InetAddress inetAddress;
			String ip;
			while (networkInterfaces.hasMoreElements()) {
				networkInterface = networkInterfaces.nextElement();
				inetAddresses = networkInterface.getInetAddresses();
				while (inetAddresses.hasMoreElements()) {
					inetAddress = inetAddresses.nextElement();
					if (inetAddress != null && (inetAddress instanceof Inet4Address || inetAddress instanceof Inet6Address)) {
						ip = inetAddress.getHostAddress();
						ipList.add(ip);
					}
				}
			}
		} catch (SocketException ex) {
		}
		return ipList;
	}

	/**
	 * 是否为IPv4地址
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isIPv4Address(final String input) {
		Pattern IPV4_PATTERN = Pattern
				.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");
		return IPV4_PATTERN.matcher(input).matches();
	}

	/**
	 * 是否为IPv6地址
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isIPv6Address(final String input) {
		return isIPv6StdAddress(input) || isIPv6HexCompressedAddress(input);
	}

	/**
	 * 是否为IPv6标准地址
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isIPv6StdAddress(final String input) {
		Pattern IPV6_STD_PATTERN = Pattern.compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");
		return IPV6_STD_PATTERN.matcher(input).matches();
	}

	/**
	 * 是否为IPv6压缩地址
	 * 
	 * @param input
	 * @return
	 */
	public static boolean isIPv6HexCompressedAddress(final String input) {
		Pattern IPV6_HEX_COMPRESSED_PATTERN = Pattern.compile(
				"^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");
		return IPV6_HEX_COMPRESSED_PATTERN.matcher(input).matches();
	}
}
