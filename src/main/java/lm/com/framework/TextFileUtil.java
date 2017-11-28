/**
 * @title TextFileUtil.java
 * @description TODO
 * @package lm.com.framework
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年12月15日下午6:10:28
 * @version v1.0
 */
package lm.com.framework;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * 文本文件读写工具类
 * 
 * @author mrluo735
 *
 */
public class TextFileUtil {
	// ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 字节流读写 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// region 字节流读写
	/**
	 * 重载+1 读取文本文件
	 * 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static String read(String filePath) throws Exception {
		File file = new File(filePath);
		if (!file.isFile() || !file.exists())
			throw new Exception("找不到指定的文件");
		return read(file);
	}

	/**
	 * 重载+2 读取文本文件
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static String read(File file) throws Exception {
		StringBuilder sb = new StringBuilder();
		InputStreamReader inputStreamReader = null;
		try {
			inputStreamReader = new InputStreamReader(new FileInputStream(file), CharsetUtil.UTF8);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String line = null;
			while (null != (line = bufferedReader.readLine())) {
				sb.append(line);
			}
			return sb.toString();
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (null != inputStreamReader)
				inputStreamReader.close();
		}
	}

	/**
	 * 重载+3 读取文本文件
	 * 
	 * @param filePath
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	public static String read(String filePath, String charset) throws Exception {
		File file = new File(filePath);
		if (!file.isFile() || !file.exists())
			throw new Exception("找不到指定的文件");
		return read(file, charset);
	}

	/**
	 * 重载+4 读取文本文件
	 * 
	 * @param file
	 * @param charset
	 * @return
	 * @throws Exception
	 */
	public static String read(File file, String charset) throws Exception {
		StringBuilder sb = new StringBuilder();
		InputStreamReader inputStreamReader = null;
		try {
			inputStreamReader = new InputStreamReader(new FileInputStream(file), charset);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String line = null;
			while (null != (line = bufferedReader.readLine())) {
				sb.append(line);
			}
			return sb.toString();
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (null != inputStreamReader)
				inputStreamReader.close();
		}
	}

	/**
	 * 重载+1 写文本文件
	 * 
	 * @param filePath
	 * @param inputStream
	 * @throws Exception
	 */
	public static void write(String filePath, InputStream inputStream) throws Exception {
		StreamUtil.inputStream2File(inputStream, filePath);
	}

	/**
	 * 重载+2 写文本文件
	 * 
	 * @param filePath
	 * @param content
	 * @param append
	 * @throws Exception
	 */
	public static void write(String filePath, String content, boolean append) throws Exception {
		File file = new File(filePath);
		if (!file.isFile() || (append && !file.exists()))
			throw new Exception("找不到指定的文件");

		write(file, content, append);
	}

	/**
	 * 重载+3 写文本文件
	 * 
	 * @param file
	 * @param content
	 * @param append
	 * @throws Exception
	 */
	public static void write(File file, String content, boolean append) throws Exception {
		BufferedWriter writer = null;
		try {
			// writer = new BufferedWriter(new FileWriter(file, append));
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append)));
			writer.write(content);
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (null != writer) {
				try {
					writer.flush();
					writer.close();
				} catch (IOException ex) {
					throw ex;
				}
			}
		}
	}

	/**
	 * 重载+4 写文本文件
	 * 
	 * @param filePath
	 * @param content
	 * @param append
	 * @throws Exception
	 */
	public static void write(String filePath, String content, boolean append, String charset) throws Exception {
		File file = new File(filePath);
		if (!file.isFile() || (append && !file.exists()))
			throw new Exception("找不到指定的文件");

		write(file, content, append, charset);
	}

	/**
	 * 重载+5 写文本文件
	 * 
	 * @param file
	 * @param content
	 * @param append
	 * @throws Exception
	 */
	public static void write(File file, String content, boolean append, String charset) throws Exception {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), charset));
			writer.write(content);
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (null != writer) {
				try {
					writer.flush();
					writer.close();
				} catch (IOException ex) {
					throw ex;
				}
			}
		}
	}
	// endregion
	// ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 字节流读写 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

	// ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 简单读写 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// region 简单读写
	/**
	 * 重载+1 读取文本文件
	 * 
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static String readUseSimple(String filePath) throws Exception {
		return readUseSimple(new File(filePath));
	}

	/**
	 * 重载+2 读取文本文件
	 * 
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static String readUseSimple(File file) throws Exception {
		StringBuilder sb = new StringBuilder();
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				sb.append(line);
			}
			return sb.toString();
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (null != scanner)
				scanner.close();
		}
	}

	/**
	 * 重载+1 写文件
	 * 
	 * @param filePath
	 * @param content
	 * @throws Exception
	 */
	public static void writeUseSimple(String filePath, String content) throws Exception {
		File file = new File(filePath);
		if (!file.isFile() || !file.exists())
			throw new Exception("找不到指定的文件");
		writeUseSimple(file, content);
	}

	/**
	 * 重载+2 写文件
	 * 
	 * @param filePath
	 * @param content
	 * @param charset
	 * @throws Exception
	 */
	public static void writeUseSimple(String filePath, String content, String charset) throws Exception {
		File file = new File(filePath);
		if (!file.isFile() || !file.exists())
			throw new Exception("找不到指定的文件");
		writeUseSimple(file, content, charset);
	}

	/**
	 * 重载+3 写文件
	 * 
	 * @param filePath
	 * @param content
	 * @throws Exception
	 */
	public static void writeUseSimple(File file, String content) throws Exception {
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(file);
			printWriter.write(content);
			printWriter.flush();
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (null != printWriter)
				printWriter.close();
		}
	}

	/**
	 * 重载+4 写文件
	 * 
	 * @param filePath
	 * @param content
	 * @param charset
	 * @throws Exception
	 */
	public static void writeUseSimple(File file, String content, String charset) throws Exception {
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(file, charset);
			printWriter.write(content);
			printWriter.flush();
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (null != printWriter)
				printWriter.close();
		}
	}
	// endregion
	// ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 简单读写 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
}
