package lm.com.framework.io;

import java.io.File;

import lm.com.framework.StringUtil;

/**
 * File 工具类
 * 
 * @ClassName: FileUtil
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年6月9日 上午10:09:47
 *
 */
public class FileUtil {
	/**
	 * 创建目录
	 * 
	 * @param dirPath
	 * @return
	 */
	public static boolean createDir(String dirPath) {
		if (StringUtil.isNullOrWhiteSpace(dirPath))
			return false;

		File dir = new File(dirPath);
		if (dir.exists())
			return true;

		// 结尾是否以"/"or"\"结束
		if (!dirPath.endsWith(File.separator))
			dirPath = dirPath + File.separator;
		return dir.mkdirs();
	}

	/**
	 * 获取文件名
	 * 
	 * @param path
	 * @return
	 */
	public static String getFileName(String path) {
		if (path == null) {
			return null;
		}
		path = path.replace("\\", "/");
		int separatorIndex = path.lastIndexOf("/");
		return (separatorIndex != -1 ? path.substring(separatorIndex + 1) : path);
	}

	/**
	 * 获取文件名
	 * <p>
	 * 不含扩展名
	 * <p>
	 * 
	 * @param path
	 * @return
	 */
	public static String getFileNameWithoutExtension(String path) {
		String fileName = getFileName(path);
		String extension = getExtension(path);
		if (null == fileName)
			return null;
		return fileName.replace("." + extension, "");
	}

	/**
	 * 取得文件路径的后缀
	 * <p>
	 * 不包含.
	 * </p>
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getExtension(String fileName) {
		if (StringUtil.isNullOrWhiteSpace(fileName))
			return "";

		fileName = fileName.replace('\\', '/');
		fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
		int index = fileName.lastIndexOf(".");

		if (index >= 0)
			return fileName.substring(index + 1);

		return "";
	}
}
