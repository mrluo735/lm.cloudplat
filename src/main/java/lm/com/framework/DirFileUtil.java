/**
 * @title FileUtil.java
 * @description TODO
 * @package lm.com.framework
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年12月23日上午9:06:41
 * @version v1.0
 */
package lm.com.framework;

import java.io.File;

/**
 * 目录文件工具类
 * 
 * @author mrluo735
 *
 */
public class DirFileUtil {
	/**
	 * 删除某个文件夹下的所有文件夹和文件
	 *
	 * @param path
	 *            String return boolean
	 */
	public static boolean deleteFile(String path) throws Exception {
		File file = new File(path);
		if (file.isDirectory()) {
			String[] filelist = file.list();
			for (String delFile : filelist) {
				File delfile = new File(path + File.separator + delFile);
				if (delfile.isDirectory()) {
					deleteFile(path + File.separator + delFile);
				} else
					System.out.println("正在删除文件：" + delfile.getPath() + ",删除是否成功：" + delfile.delete());
			}
			System.out.println("正在删除空文件夹：" + file.getPath() + ",删除是否成功：" + file.delete());
		} else
			System.out.println("正在删除文件：" + file.getPath() + ",删除是否成功：" + file.delete());
		return true;
	}
}
