/**
 * @title CloudStorageServiceImpl.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.cloud.storage.server.service.impl
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年6月8日下午8:15:45
 * @version v1.0.0
 */
package lm.cloud.storage.server.service.impl;

import java.awt.Image;
import java.io.File;
import java.util.UUID;

import javax.activation.MimetypesFileTypeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lm.cloud.storage.server.model.UploadResponse;
import lm.com.framework.MAryUtil;
import lm.com.framework.NetUtil;
import lm.com.framework.StreamUtil;
import lm.com.framework.StringUtil;
import lm.com.framework.encrypt.Base64Encrypt;
import lm.com.framework.encrypt.Crc32Encrypt;
import lm.com.framework.encrypt.MD5Encrypt;
import lm.com.framework.id.IdWorker;
import lm.com.framework.image.ImageUtil;
import lm.com.framework.io.FileUtil;
import lm.com.framework.ip.IPUtil;

/**
 * @ClassName: CloudStorageServiceImpl
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年6月8日 下午8:15:45
 * 
 */
@Service
public class CloudStorageServiceImpl {
	@Value("${lm.cloud.storage.drive}")
	private String storageDrive;
	@Value("${lm.cloud.storage.res.domain}")
	private String resDomain;
	@Value("${lm.cloud.storage.res.bucketName}")
	private String bucketName;

	@Autowired
	private IdWorker idWorker;

	/**
	 * 上传文件
	 * 
	 * @param bytes
	 * @param fileSize
	 * @param originalName
	 * @param clientIp
	 */
	public UploadResponse uploadFile(byte[] bytes, long fileSize, String originalName, String clientIp) {
		UploadResponse uploadResponse = new UploadResponse();
		try {
			// 获取文件的MD5值
			String fileMd5 = MD5Encrypt.encode(bytes);
			String firstDir = fileMd5.substring(0, 2);
			String secondDir = fileMd5.substring(2, 4);
			// 构建文件存放的目录
			String dirPath = String.format("%s/%s/%s/%s", this.storageDrive, this.bucketName, firstDir, secondDir);
			FileUtil.createDir(dirPath);

			// 获取文件的CRC32
			long crc32 = Crc32Encrypt.encode(bytes);

			String fileNameWithoutExtension = UUID.randomUUID().toString();
			long uploadOn = System.currentTimeMillis();
			String fileHash = this.getFileHash(clientIp, uploadOn, fileSize, crc32);
			String mimetype = "image/jpeg";
			String extension = "jpg";
			if (!StringUtil.isNullOrWhiteSpace(originalName)) {
				mimetype = new MimetypesFileTypeMap().getContentType(originalName);
				extension = FileUtil.getExtension(originalName).toLowerCase();
			}
			String fileName = String.format("%s.%s", fileNameWithoutExtension, extension);
			String filePath = String.format("%s/%s", dirPath, fileName);
			File file = StreamUtil.bytes2File(bytes, filePath);

			Image image = ImageUtil.isImage(file);
			int imageWidth = 0, imageHeight = 0;
			if (image != null) {
				imageWidth = image.getWidth(null);
				imageHeight = image.getHeight(null);
				// 销毁image
				image = null;
			}

			String url = String.format("%s/%s/%s/%s/%s", this.resDomain, this.bucketName, firstDir, secondDir,
					fileName);
			uploadResponse.setId(this.idWorker.nextId());
			uploadResponse.setOriginalName(originalName);
			uploadResponse.setName(fileName);
			uploadResponse.setNameWithoutExtension(fileNameWithoutExtension);
			uploadResponse.setExtension(extension);
			uploadResponse.setMimetype(mimetype);
			uploadResponse.setDomain(this.resDomain);
			uploadResponse.setUrl(url);
			uploadResponse.setUrlWithoutDomain(url.replace(this.resDomain, ""));
			uploadResponse.setSize(fileSize);
			uploadResponse.setWidth(imageWidth);
			uploadResponse.setHeight(imageHeight);
			// uploadResponse.setDuration(duration);
			uploadResponse.setHash(fileHash);
			uploadResponse.setStorageType(1);
			uploadResponse.setBucketName(this.bucketName);
			uploadResponse.setUploadOn(uploadOn);
			return uploadResponse;
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 上传图片
	 * 
	 * @param bytes
	 * @param fileSize
	 * @param originalName
	 * @param clientIp
	 */
	public UploadResponse uploadImage(byte[] bytes, long fileSize, String originalName, String clientIp) {
		UploadResponse uploadResponse = this.uploadFile(bytes, fileSize, originalName, clientIp);
		// 增加水印
		// String filePath = String.format("%s%s", this.storageDrive,
		// uploadResponse.getUrlWithoutDomain());
		// File file = new File(filePath);
		// if (file.exists()) {
		// ImageWaterMarkUtil.addWaterMark(srcImgPath, destImgPath, text,
		// fontName, fontStyle, fontSize, fontColor, position, alpha, degree);
		// }
		return uploadResponse;
	}

	/**
	 * 获取文件hash
	 * <p>
	 * 亦可用作文件名 <br />
	 * base64(to64hex(toLong(ip))) + $ + base64(to64hex(toLong(now))) + $ +
	 * base64(to64hex(fileSize)) + $ + base64(to64hex(crc32))
	 * <p>
	 * 
	 * @param clientIp
	 * @param uploadOn
	 * @param fileSize
	 * @param crc32
	 * @return
	 */
	private String getFileHash(String clientIp, long uploadOn, long fileSize, long crc32) {
		long ipLong = 0L;
		if (NetUtil.isIPv4Address(clientIp))
			ipLong = IPUtil.ipv4ToLong(clientIp);
		else
			ipLong = IPUtil.ipv6ToBigInteger(clientIp).longValue();
		String ip64hex = MAryUtil.tenTo64hex(ipLong);
		String ipBase64 = Base64Encrypt.encode(ip64hex);

		long now = uploadOn / 1000;
		String now64hex = MAryUtil.tenTo64hex(now);
		String nowBase64 = Base64Encrypt.encode(now64hex);

		String fileSize64hex = MAryUtil.tenTo64hex(fileSize);
		String fileSizeBase64 = Base64Encrypt.encode(fileSize64hex);

		String crc64hex = MAryUtil.tenTo64hex(crc32);
		String crcBase64 = Base64Encrypt.encode(crc64hex);

		String fileHash = String.format("%s$%s$%s$%s", ipBase64, nowBase64, fileSizeBase64, crcBase64);
		return fileHash;
	}
}
