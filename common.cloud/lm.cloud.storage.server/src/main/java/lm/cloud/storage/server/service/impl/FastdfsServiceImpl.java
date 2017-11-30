/**
 * @title FastdfsServiceImpl.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.cloud.storage.server.service.impl
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年6月13日下午3:17:16
 * @version v1.0.0
 */
package lm.cloud.storage.server.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.csource.common.NameValuePair;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.StorageClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lm.cloud.storage.server.model.UploadResponse;
import lm.com.framework.StringUtil;
import lm.com.framework.io.FileUtil;

/**
 * @ClassName: FastdfsServiceImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年6月13日 下午3:17:16
 * 
 */
@Service
public class FastdfsServiceImpl {
	private static final Logger logger = LoggerFactory.getLogger(FastdfsServiceImpl.class);

	@Value("${lm.cloud.storage.res.domain}")
	private String resDomain;

	@Autowired
	private StorageClient storageClient;

	/**
	 * 上传文件
	 * 
	 * @param data
	 * @param extension
	 *            后缀名
	 * @param metaData
	 */
	public UploadResponse upload(byte[] data, String extension, Map<String, String> metaData) {
		try {
			List<NameValuePair> nvpList = new ArrayList<NameValuePair>();
			if (null != metaData) {
				for (String key : metaData.keySet()) {
					nvpList.add(new NameValuePair(key, metaData.get(key)));
				}
			}
			String[] fileIds = this.storageClient.upload_file(data, StringUtil.trimStart(extension, "."),
					nvpList.toArray(new NameValuePair[0]));
			String group = fileIds[0];
			String relativeUrl = fileIds[1]; // M00/00/00/wKgEoFgq5y6APpyOAABOhiLIwdw754.jpg
			FileInfo fileInfo = storageClient.get_file_info(group, relativeUrl);
			NameValuePair[] nvps = storageClient.get_metadata(group, relativeUrl);

			UploadResponse uploadResponse = new UploadResponse();
			uploadResponse.setOriginalName("");
			uploadResponse.setName(FileUtil.getFileName(relativeUrl));
			uploadResponse.setNameWithoutExtension(FileUtil.getFileNameWithoutExtension(relativeUrl));
			uploadResponse.setExtension(FileUtil.getExtension(relativeUrl));
			uploadResponse.setUrl(String.format("%s/%s/%s", resDomain, group, relativeUrl));
			uploadResponse.setUrlWithoutDomain(relativeUrl);
			uploadResponse.setSize(fileInfo.getFileSize());
			uploadResponse.setStorageType(2);
			uploadResponse.setBucketName(group);
			uploadResponse.setUploadOn(System.currentTimeMillis());
			if (null != nvps) {
				for (NameValuePair nvp : nvps) {
					uploadResponse.getExtra().put(nvp.getName(), nvp.getValue());
				}
			}
			uploadResponse.setUploadOn(fileInfo.getCreateTimestamp().getTime());
			return uploadResponse;
		} catch (Exception ex) {
			logger.error("上传文件异常！错误原因：{}", ex.getMessage());
			return null;
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param group
	 * @param relativePath
	 * @return
	 */
	public boolean deleteFile(String group, String relativePath) {
		try {
			int result = this.storageClient.delete_file(group, relativePath);
			return 0 == result ? true : false;
		} catch (Exception ex) {
			logger.error("删除文件异常！错误原因：{}", ex.getMessage());
			return false;
		}
	}
}
