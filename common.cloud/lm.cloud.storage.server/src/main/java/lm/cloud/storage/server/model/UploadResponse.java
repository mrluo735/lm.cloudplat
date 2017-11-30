/**
 * @title UploadResponse.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.cloud.storage.server.model
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年6月9日上午11:56:22
 * @version v1.0.0
 */
package lm.cloud.storage.server.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: UploadResponse
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年6月9日 上午11:56:22
 * 
 */
public class UploadResponse implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5085387794968296736L;

	/**
	 * 文件唯一id
	 */
	private Long id;

	/**
	 * 文件原名称
	 */
	private String originalName = "";

	/**
	 * 文件名称
	 */
	private String name = "";

	/**
	 * 文件名称无扩展名
	 */
	private String nameWithoutExtension = "";

	/**
	 * 扩展名
	 */
	private String extension = "";

	/**
	 * Mime类型
	 */
	private String mimetype = "";

	/**
	 * 域名
	 */
	private String domain = "";

	/**
	 * url
	 */
	private String url = "";

	/**
	 * url无域名
	 */
	private String urlWithoutDomain = "";

	/**
	 * 文件大小
	 */
	private long size = 0L;

	/**
	 * 宽度
	 */
	private int width = 0;

	/**
	 * 高度
	 */
	private int height = 0;

	/**
	 * 持续时间
	 */
	private BigDecimal duration = BigDecimal.ZERO;

	/**
	 * 文件hash
	 */
	private String hash = "";

	/**
	 * 存储类型(1:自定义;2:Fastdfs;3七牛)
	 */
	private int storageType;

	/**
	 * 空间名
	 */
	private String bucketName = "";

	/**
	 * 上传时间
	 */
	private long uploadOn = 0L;

	/**
	 * 补充信息
	 */
	private Map<String, String> extra = new HashMap<String, String>();

	/**
	 * 
	 * @return
	 */
	public Long getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the originalName
	 */
	public String getOriginalName() {
		return originalName;
	}

	/**
	 * @param originalName
	 *            the originalName to set
	 */
	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the nameWithoutExtension
	 */
	public String getNameWithoutExtension() {
		return nameWithoutExtension;
	}

	/**
	 * @param nameWithoutExtension
	 *            the nameWithoutExtension to set
	 */
	public void setNameWithoutExtension(String nameWithoutExtension) {
		this.nameWithoutExtension = nameWithoutExtension;
	}

	/**
	 * @return the extension
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * @param extension
	 *            the extension to set
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}

	/**
	 * @return the mimetype
	 */
	public String getMimetype() {
		return mimetype;
	}

	/**
	 * @param mimetype
	 *            the mimetype to set
	 */
	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	/**
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * @param domain
	 *            the domain to set
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the urlWithoutDomain
	 */
	public String getUrlWithoutDomain() {
		return urlWithoutDomain;
	}

	/**
	 * @param urlWithoutDomain
	 *            the urlWithoutDomain to set
	 */
	public void setUrlWithoutDomain(String urlWithoutDomain) {
		this.urlWithoutDomain = urlWithoutDomain;
	}

	/**
	 * @return the size
	 */
	public long getSize() {
		return size;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(long size) {
		this.size = size;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return the duration
	 */
	public BigDecimal getDuration() {
		return duration;
	}

	/**
	 * @param duration
	 *            the duration to set
	 */
	public void setDuration(BigDecimal duration) {
		this.duration = duration;
	}

	/**
	 * @return the hash
	 */
	public String getHash() {
		return hash;
	}

	/**
	 * @param hash
	 *            the hash to set
	 */
	public void setHash(String hash) {
		this.hash = hash;
	}

	/**
	 * 
	 * @return
	 */
	public int getStorageType() {
		return storageType;
	}

	/**
	 * 
	 * @param storageType
	 *            the storageType to set
	 */
	public void setStorageType(int storageType) {
		this.storageType = storageType;
	}

	/**
	 * @return the bucketName
	 */
	public String getBucketName() {
		return bucketName;
	}

	/**
	 * @param bucketName
	 *            the bucketName to set
	 */
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	/**
	 * @return the uploadOn
	 */
	public long getUploadOn() {
		return uploadOn;
	}

	/**
	 * @param uploadOn
	 *            the uploadOn to set
	 */
	public void setUploadOn(long uploadOn) {
		this.uploadOn = uploadOn;
	}

	/**
	 * @return the extra
	 */
	public Map<String, String> getExtra() {
		return extra;
	}

	/**
	 * @param extra
	 *            the extra to set
	 */
	public void setExtra(Map<String, String> extra) {
		this.extra = extra;
	}
}
