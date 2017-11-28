package lm.com.framework.video;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * 视频网址视频信息提取工具类
 * <p>
 * 支持以下网址：优酷、土豆、酷6、乐视、腾讯、搜狐、凤凰视频、音悦台MV<br />
 * 链接地址不在支持的列表中时返回原链接地址以及链接的页面标题
 * </p>
 * 
 * @author mrluo735
 */
public class VideoUtil {
	/**
	 * 优酷
	 */
	private final static String VIDEO_DOMAIN_YOUKU = "v.youku.com";

	/**
	 * 土豆
	 */
	private final static String VIDEO_DOMAIN_TUDOU = "www.tudou.com";

	/**
	 * 酷6
	 */
	private final static String VIDEO_DOMAIN_KU6 = "v.ku6.com";

	/**
	 * 乐视
	 */
	private final static String VIDEO_DOMAIN_LETV = "www.letv.com";

	/**
	 * 腾讯
	 */
	private final static String VIDEO_DOMAIN_QQ = "v.qq.com";

	/**
	 * 搜狐
	 */
	private final static String VIDEO_DOMAIN_SOHU = "tv.sohu.com";

	/**
	 * 凤凰
	 */
	private final static String VIDEO_DOMAIN_IFENG = "v.ifeng.com";

	/**
	 * 网易
	 */
	private final static String VIDEO_DOMAIN_NETEASY = "v.163.com/video";

	/**
	 * 音悦台MV
	 */
	private final static String VIDEO_DOMAIN_YINYUETAI = "v.yinyuetai.com/video";

	// region 获取视频信息
	/**
	 * 获取视频信息
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static VideoInfo getVideoInfo(String url) {
		VideoInfo video = null;

		if (url.indexOf(VIDEO_DOMAIN_YOUKU) != -1) {
			video = getYouKu(url);
		} else if (url.indexOf(VIDEO_DOMAIN_TUDOU) != -1) {
			video = getTudou(url);
		} else if (url.indexOf(VIDEO_DOMAIN_KU6) != -1) {
			video = getKu6(url);
		} else if (url.indexOf(VIDEO_DOMAIN_LETV) != -1) {
			video = getLetv(url);
		} else if (url.indexOf(VIDEO_DOMAIN_QQ) != -1) {
			video = getQQ(url);
		} else if (url.indexOf(VIDEO_DOMAIN_SOHU) != -1) {
			video = getSohu(url);
		} else if (url.indexOf(VIDEO_DOMAIN_IFENG) != -1) {
			video = getIfeng(url);
		} else if (url.indexOf(VIDEO_DOMAIN_NETEASY) != -1) {
			video = getNetEase(url);
		} else if (url.indexOf(VIDEO_DOMAIN_YINYUETAI) != -1) {
			video = getYinYueTai(url);
		} else {
			// 链接地址不在支持的列表中时返回原链接地址以及链接的页面标题
			// Document doc = getURLContent(url);
			// video = new Video();
			// video.setTitle(doc.title());
			// video.setPageUrl(url);
		}

		return video;
	}
	// endregion

	/**
	 * 获取优酷视频
	 * 
	 * @param url
	 *            视频URL
	 */
	public static VideoInfo getYouKu(String url) {
		try {
			Document doc = getURLContent(url);

			String title = doc.select("meta[name=irTitle]").attr("content");

			String pic = getElementAttrById(doc, "s_baidu1", "href");
			int local = pic.indexOf("pic=");
			pic = pic.substring(local + 4);

			String flash = getElementAttrById(doc, "link2", "value");

			String htmlCode = getElementAttrById(doc, "link3", "value");

			String duration = "";

			String description = doc.select("meta[name=description]").attr("content");

			VideoInfo video = new VideoInfo();
			video.setTitle(title);
			video.setThumbnail(pic);
			video.setFlashUrl(flash);
			video.setDuration(duration);
			video.setSource("优酷视频");
			video.setPageUrl(url);
			video.setDescription(description);
			video.setHtmlCode(htmlCode);
			return video;
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 获取土豆视频
	 * 
	 * @param url
	 *            视频URL
	 */
	public static VideoInfo getTudou(String url) {
		try {
			Document doc = getURLContent(url);
			String content = doc.html();
			int beginLocal = content.indexOf("var pageConfig = {");
			int endLocal = content.indexOf("};");
			content = content.substring(beginLocal, endLocal).trim();

			String title = doc.select("meta[name=irTitle]").attr("content");

			String flash = getScriptVarByName("lcode", content);
			flash = "http://www.tudou.com/a/" + flash + "/v.swf";

			String pic = getScriptVarByName(",pic", content);

			String description = doc.select("meta[name=description]").attr("content");

			String duration = getScriptVarByName("time", content);

			VideoInfo video = new VideoInfo();
			video.setTitle(title);
			video.setThumbnail(pic);
			video.setFlashUrl(flash);
			video.setDuration(duration);
			video.setSource("土豆视频");
			video.setPageUrl(url);
			video.setDescription(description);
			video.setHtmlCode(getHtmlCode(flash));
			return video;
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 获取酷6视频
	 * 
	 * @param url
	 *            视频URL
	 */
	public static VideoInfo getKu6(String url) {
		try {
			Document doc = getURLContent(url);

			String content = doc.html();
			int beginLocal = content.indexOf("VideoInfo:{");
			int endLocal = content.indexOf(", ObjectInfo");
			content = content.substring(beginLocal + 10, endLocal).trim();

			String title = doc.select("meta[name=title]").attr("content");

			String flash = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf(".html"));
			flash = "http://player.ku6.com/refer/" + flash + "/v.swf";

			String pic = getScriptVarByName("cover", content);
			pic = pic.substring(1, pic.length() - 1);

			String duration = getScriptVarByName("time", content);
			duration = duration.substring(1, duration.length() - 1) + "";

			/**
			 * 视频简介
			 */
			String description = doc.select("meta[name=description]").attr("content");

			VideoInfo video = new VideoInfo();
			video.setTitle(title);
			video.setThumbnail(pic);
			video.setFlashUrl(flash);
			video.setDuration(formatSecond(Integer.parseInt(duration)));
			video.setSource("酷6视频");
			video.setPageUrl(url);
			video.setDescription(description);
			video.setHtmlCode(getHtmlCode(flash));
			return video;
		} catch (Exception ex) {
			return null;
		}

	}

	/**
	 * 获取乐视视频
	 * 
	 * @param url
	 *            视频URL
	 */
	public static VideoInfo getLetv(String url) {
		try {
			Document doc = getURLContent(url);

			String content = doc.html();
			int beginLocal = content.indexOf("var __INFO__ = {");
			int endLocal = content.indexOf("playlist:");
			content = content.substring(beginLocal + 15, endLocal).trim();

			String title = doc.select("meta[name=irTitle]").attr("content");

			String pic = getScriptVarByName("videoPic", content);
			pic = pic.substring(0, pic.length() - 1);

			String flash = getScriptVarByName("vid", content);
			flash = "http://img1.c0.letv.com/ptv/player/swfPlayer.swf?id=" + flash + "&autoplay=0";

			String duration = getScriptVarByName("duration", content);

			String description = doc.select("meta[name=description]").attr("content");

			VideoInfo video = new VideoInfo();
			video.setTitle(title);
			video.setThumbnail(pic);
			video.setFlashUrl(flash);
			video.setDuration(duration);
			video.setSource("乐视视频");
			video.setPageUrl(url);
			video.setDescription(description);
			video.setHtmlCode(getHtmlCode(flash));
			return video;
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 获取腾讯视频
	 * 
	 * @param url
	 *            视频URL
	 */
	public static VideoInfo getQQ(String url) {
		try {
			Document doc = getURLContent(url);

			String title = doc.select("meta[itemprop=name]").attr("content");

			String pic = doc.select("meta[itemprop=image]").attr("content");

			String flash = doc.select("meta[itemprop=url]").attr("content");
			flash = flash.substring(flash.lastIndexOf("/") + 1, flash.lastIndexOf(".html"));
			flash = "http://static.video.qq.com/TPout.swf?vid=" + flash + "&auto=0";

			String description = doc.select("meta[name=description]").attr("content");

			VideoInfo video = new VideoInfo();
			video.setTitle(title);
			video.setThumbnail(pic);
			video.setFlashUrl(flash);
			video.setSource("腾讯视频");
			video.setPageUrl(url);
			video.setDescription(description);
			video.setHtmlCode(getHtmlCode(flash));
			return video;
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 获取搜狐视频
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static VideoInfo getSohu(String url) {
		try {
			Document doc = getURLContent(url);

			String title = doc.title().split("-")[0].trim();

			String description = doc.select("meta[name=description]").attr("content");

			String thumbnail = doc.select("meta[property=og:image]").attr("content");

			String flash = doc.select("meta[property=og:videosrc]").attr("content");

			VideoInfo video = new VideoInfo();
			video.setTitle(title);
			video.setThumbnail(thumbnail);
			video.setFlashUrl(flash);
			video.setSource("搜狐视频");
			video.setPageUrl(url);
			video.setDescription(description);
			video.setHtmlCode(getHtmlCode(flash));
			return video;
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 获取凤凰视频
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static VideoInfo getIfeng(String url) {
		try {
			Document doc = getURLContent(url);

			String title = doc.select("meta[property=og:title]").attr("content");

			String description = doc.select("meta[property=og:description]").attr("content");

			String videoId = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf(".shtml"));
			String flash = "http://v.ifeng.com/include/exterior.swf?guid=" + videoId + "&AutoPlay=false";

			String thumbnail = doc.select("meta[property=og:image]").attr("content");

			/**
			 * 视频时长
			 */
			// String duration = doc.getElementById("duration").html();

			VideoInfo video = new VideoInfo();
			video.setTitle(title);
			video.setThumbnail(thumbnail);
			video.setFlashUrl(flash);
			// video.setDuration(duration);
			video.setSource("凤凰视频");
			video.setPageUrl(url);
			video.setDescription(description);
			video.setHtmlCode(getHtmlCode(flash));
			return video;
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 获取网易视频
	 * 
	 * @param url
	 * @return
	 */
	public static VideoInfo getNetEase(String url) {
		try {
			Document doc = getURLContent(url);

			String content = doc.html();
			content = content.substring(content.indexOf("topicid"));
			content = content.substring(0, content.indexOf("</script>"));
			content = content.replaceAll("\"", "").replaceAll("\n", "").trim();
			String contents[] = content.split(";");

			String title = getScriptVarByName("title=", contents);
			String thumbnail = getScriptVarByName("imgpath=", contents);
			String description = "";

			/**
			 * 视频地址
			 */
			String topicid = getScriptVarByName("topicid=", contents);
			String vid = getScriptVarByName("vid=", contents);
			String flash = "http://img1.cache.netease.com/flvplayer081128/~false~" + topicid + "_" + vid + "~"
					+ thumbnail.substring(7, thumbnail.length() - 4) + "~.swf";

			VideoInfo video = new VideoInfo();
			video.setTitle(title);
			video.setThumbnail(thumbnail);
			video.setFlashUrl(flash);
			video.setSource("网易视频");
			video.setPageUrl(url);
			video.setDescription(description);
			video.setHtmlCode(getHtmlCode(flash));
			return video;
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * 获取音悦台MV
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static VideoInfo getYinYueTai(String url) {
		try {
			Document doc = getURLContent(url);

			String title = doc.select("meta[property=og:title]").attr("content");

			String description = doc.select("meta[property=og:description]").attr("content");

			String thumbnail = doc.select("meta[property=og:image]").attr("content");

			String flash = doc.select("meta[property=og:videosrc]").attr("content");

			VideoInfo video = new VideoInfo();
			video.setTitle(title);
			video.setThumbnail(thumbnail);
			video.setFlashUrl(flash);
			video.setSource("音悦台MV");
			video.setPageUrl(url);
			video.setDescription(description);
			video.setHtmlCode(getHtmlCode(flash));
			return video;
		} catch (Exception ex) {
			return null;
		}
	}

	public static String formatSecond(int second) {
		String html = "0";
		if (second != 0) {
			String format;
			Object[] array;
			Integer hours = (int) (second / (60 * 60));
			Integer minutes = (int) (second / 60 - hours * 60);
			Integer seconds = (int) (second - minutes * 60 - hours * 60 * 60);
			if (hours > 0) {
				format = "%1$,d:%2$,d:%3$,d";
				array = new Object[] { hours, minutes, seconds };
			} else if (minutes > 0) {
				format = "00:%1$,d:%2$,d";
				array = new Object[] { minutes, seconds };
			} else {
				format = "00:00:%1$,d";
				array = new Object[] { seconds };
			}
			html = String.format(format, array);
		}

		return html;

	}

	/**
	 * 获取script某个变量的值
	 * 
	 * @param name
	 *            变量名称
	 * @return 返回获取的值
	 */
	private static String getScriptVarByName(String name, String content) {
		String script = content;
		int begin = script.indexOf(name);
		script = script.substring(begin + name.length() + 2);
		int end = script.indexOf(",");
		script = script.substring(0, end);
		String result = script.replaceAll("'", "");
		return result.trim();
	}

	private static String getScriptVarByName(String name, String contents[]) {
		String text = "";
		for (String s : contents) {
			if (s.contains(name)) {
				text = s.substring(s.indexOf(name) + name.length());
				break;
			}
		}
		return text;
	}

	/**
	 * 根据HTML的ID键及属于名，获取属于值
	 * 
	 * @param id
	 *            HTML的ID键
	 * @param attrName
	 *            属于名
	 * @return 返回属性值
	 */
	private static String getElementAttrById(Document doc, String id, String attrName) throws Exception {
		Element et = doc.getElementById(id);
		String attrValue = et.attr(attrName);
		return attrValue;
	}

	/**
	 * 根据FLASH地址生成页面代码
	 * 
	 * @param flashUrl
	 * @return
	 * @throws Exception
	 */
	private static String getHtmlCode(String flashUrl) throws Exception {
		return "<embed src=\"" + flashUrl
				+ "\" allowFullScreen=\"true\" quality=\"high\" width=\"480\" height=\"400\" align=\"middle\" allowScriptAccess=\"always\" type=\"application/x-shockwave-flash\"></embed>";
	}

	/**
	 * 获取网页的内容
	 */
	private static Document getURLContent(String url) throws Exception {
		Document doc = Jsoup.connect(url).data("query", "Java").userAgent("Mozilla").cookie("auth", "token")
				.timeout(5000).get();
		return doc;
	}
}