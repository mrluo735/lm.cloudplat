package lm.com.framework.video;

import java.io.Serializable;

public class VideoInfo implements Serializable {

	private static final long serialVersionUID = -6220973207083491817L;

	/**
	 * 视频标题
	 */
	private String title = "";
	/**
	 * 视频缩略图
	 */
	private String thumbnail = "";
	/**
	 * 视频描述
	 */
	private String description = "";
	/**
	 * 视频时长
	 */
	private String duration = "";
	/**
	 * 视频来源
	 */
	private String source = "";
	/**
	 * 视频页面地址
	 */
	private String pageUrl = "";
	/**
	 * 视频FLASH地址
	 */
	private String flashUrl = "";
	/**
	 * 视频HTML代码
	 */
	private String htmlCode = "";

	/**
	 * 获取标题
	 * 
	 * @return
	 */
	public String getTitle() {
		if (null == title) {
			return "";
		} else {
			return title;
		}
	}

	/**
	 * 设置标题
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 获取缩略图
	 * 
	 * @return
	 */
	public String getThumbnail() {
		return thumbnail == null ? "" : thumbnail;
	}

	/**
	 * 设置缩略图
	 * 
	 * @param thumbnail
	 */
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	/**
	 * 获取描述
	 * 
	 * @return
	 */
	public String getDescription() {
		return description == null ? "" : description;
	}

	/**
	 * 设置描述
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 获取时长
	 * 
	 * @return
	 */
	public String getDuration() {
		return duration == null ? "" : duration;
	}

	/**
	 * 设置时长
	 * 
	 * @param duration
	 */
	public void setDuration(String duration) {
		this.duration = duration;
	}

	/**
	 * 获取来源
	 * 
	 * @return
	 */
	public String getSource() {
		return source == null ? "" : source;
	}

	/**
	 * 设置来源
	 * 
	 * @param source
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * 获取页面url
	 * 
	 * @return
	 */
	public String getPageUrl() {
		return pageUrl == null ? "" : pageUrl;
	}

	/**
	 * 设置页面url
	 * 
	 * @param pageUrl
	 */
	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	/**
	 * 获取视频url
	 * 
	 * @return
	 */
	public String getFlashUrl() {
		return flashUrl == null ? "" : flashUrl;
	}

	/**
	 * 设置视频url
	 * 
	 * @param flashUrl
	 */
	public void setFlashUrl(String flashUrl) {
		this.flashUrl = flashUrl;
	}

	/**
	 * 获取视频HTML代码
	 * 
	 * @return
	 */
	public String getHtmlCode() {
		return htmlCode == null ? "" : htmlCode;
	}

	/**
	 * 设置视频HTML代码
	 * 
	 * @param htmlCode
	 */
	public void setHtmlCode(String htmlCode) {
		this.htmlCode = htmlCode;
	}
}
