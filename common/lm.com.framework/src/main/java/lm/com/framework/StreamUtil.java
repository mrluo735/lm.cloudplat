/**
 * @title StreamUtil.java
 * @description TODO
 * @package lm.com.framework
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年12月15日下午7:04:33
 * @version v1.0
 */
package lm.com.framework;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * 输入输出流工具类
 * 
 * @author mrluo735
 *
 */
public class StreamUtil {
	/**
	 * 重载+1 文件转InputStream
	 * 
	 * @param str
	 * @return
	 * @throws FileNotFoundException
	 */
	public static InputStream toInputStream(File file) {
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException ex) {
			return null;
		}
	}

	/**
	 * 重载+2 byte[]转InputStream
	 * 
	 * @param bytes
	 * @return
	 */
	public static InputStream toInputStream(byte[] bytes) {
		InputStream inputStream = new ByteArrayInputStream(bytes);
		return inputStream;
	}

	/**
	 * 重载+3 字符串转InputStream
	 * 
	 * @param str
	 * @return
	 */
	public static InputStream toInputStream(String str) {
		ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
		return stream;
	}

	/**
	 * 重载+4 字符串转InputStream
	 * 
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static InputStream toInputStream(String str, String charset) throws UnsupportedEncodingException {
		ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes(charset));
		return stream;
	}

	/**
	 * 重载+1 InputStream转String
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static String inputStream2String(InputStream inputStream) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while (null != (line = reader.readLine())) {
				buffer.append(line);
			}
			return buffer.toString();
		} catch (IOException ex) {
			return "";
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 重载+2 InputStream转String
	 * 
	 * @param inputStream
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String inputStream2String(InputStream inputStream, String charset) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while (null != (line = reader.readLine())) {
				buffer.append(line);
			}
			return buffer.toString();
		} catch (IOException ex) {
			return "";
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * InputStream转File
	 * 
	 * @param inputStream
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	public static File inputStream2File(InputStream inputStream, String filePath) {
		File file = new File(filePath);

		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}
			return file;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			try {
				inputStream.close();
				outputStream.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * InputStream转byte[]
	 * 
	 * @param inputStream
	 * @return
	 */
	public static byte[] inputStream2Bytes(InputStream inputStream) {
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		// buff用于存放循环读取的临时数据
		byte[] buff = new byte[100];
		int rc = 0;
		try {
			while ((rc = inputStream.read(buff, 0, 100)) > 0) {
				swapStream.write(buff, 0, rc);
			}
			return swapStream.toByteArray();
		} catch (IOException ex) {
			return new byte[0];
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * byte[]转File
	 * 
	 * @param bytes
	 * @param filePath
	 * @return
	 */
	public static File bytes2File(byte[] bytes, String filePath) {
		InputStream inputStream = toInputStream(bytes);
		return inputStream2File(inputStream, filePath);
	}

	/**
	 * File转byte[]
	 * 
	 * @param file
	 * @return
	 */
	public static byte[] file2Bytes(File file) {
		InputStream inputStream = toInputStream(file);
		return inputStream2Bytes(inputStream);
	}
}
