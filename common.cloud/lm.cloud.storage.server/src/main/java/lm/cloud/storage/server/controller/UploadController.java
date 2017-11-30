/**
 * @title StorageController.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.cloud.storage.server.controller
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年6月8日下午1:35:12
 * @version v1.0.0
 */
package lm.cloud.storage.server.controller;

import java.util.Iterator;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import lm.cloud.storage.server.model.UploadResponse;
import lm.cloud.storage.server.service.impl.CloudStorageServiceImpl;
import lm.com.framework.StreamUtil;
import lm.com.framework.WebUtil;
import lm.com.framework.webmvc.result.JsonResult;

/**
 * @ClassName: StorageController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年6月8日 下午1:35:12
 * 
 */
@RestController
public class UploadController {
	private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

	@Autowired
	private CloudStorageServiceImpl cloudStorageService;

	// @GetMapping(value = "/getToken")
	// public JsonResult getToken(HttpServletRequest request) {
	// JsonResult jsonResult = new JsonResult();
	//
	// String appKey = request.getParameter("appKey");
	// String appSecret = request.getParameter("appSecret");
	// if (appKey != this.storageConfig.appKey || appSecret !=
	// this.storageConfig.appSecret) {
	//
	// return jsonResult;
	// }
	// long now = System.currentTimeMillis();
	// String encryptStr = MD5Encrypt.encode32(this.storageConfig.appKey + ":" +
	// this.storageConfig.appSecret) + now;
	// String token = Base64Encrypt.encode(encryptStr);
	// return jsonResult;
	// }

	/**
	 * 上传
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public JsonResult upload(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		try {
			byte[] bytes = new byte[0];
			String originalName = "";
			if (request instanceof org.apache.catalina.connector.RequestFacade) {
				// 处理非multipart/form-data的上传（比如flash上传头像）
				ServletInputStream sis = request.getInputStream();
				bytes = StreamUtil.inputStream2Bytes(sis);
			} else {
				// 将当前上下文初始化给 CommonsMutipartResolver （多部分解析器）
				CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getServletContext());
				// 检查form中是否有enctype="multipart/form-data"
				if (!multipartResolver.isMultipart(request)) {
					jsonResult.put("success", false);
					jsonResult.put("code", -HttpStatus.FAILED_DEPENDENCY.value());
					jsonResult.put("message", "上传请求中不包含multipart/form-data");
					return jsonResult;
				}
				// 将request变成MultipartHttpServletRequest
				MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				// List<MultipartFile> files = multiRequest.getFiles("file");
				// 获取multiRequest 中所有的文件名
				Iterator<String> iterator = multiRequest.getFileNames();
				while (iterator.hasNext()) {
					String fileName = iterator.next();
					MultipartFile file = multiRequest.getFile(fileName);
					if (file.isEmpty())
						continue;
					bytes = file.getBytes();
					originalName = file.getOriginalFilename();
					break;
				}
			}

			String clientIp = WebUtil.getClientIP(request);
			long fileSize = bytes.length;
			// // 上传
			// file.transferTo(new File(path));
			UploadResponse uploadResponse = this.cloudStorageService.uploadFile(bytes, fileSize, originalName,
					clientIp);
			jsonResult.put("success", true);
			jsonResult.put("code", 200);
			jsonResult.put("message", "");
			jsonResult.put("data", uploadResponse);
		} catch (Exception ex) {
			jsonResult.put("success", false);
			jsonResult.put("code", -HttpStatus.FAILED_DEPENDENCY.value());
			jsonResult.put("message", ex.getMessage());
			logger.error("上传失败！失败原因：" + ex);
		}
		return jsonResult;
	}

	/**
	 * 批量上传
	 * <p>
	 * 暂时不启用
	 * </p>
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/uploadBatch", method = RequestMethod.POST)
	public JsonResult uploadBatch(HttpServletRequest request) {
		JsonResult jsonResult = new JsonResult();
		try {
			// 将request变成MultipartHttpServletRequest
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// List<MultipartFile> files = multiRequest.getFiles("file");

			// 将当前上下文初始化给 CommonsMutipartResolver （多部分解析器）
			CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getServletContext());
			// 检查form中是否有enctype="multipart/form-data"
			if (!multipartResolver.isMultipart(request)) {
				jsonResult.put("success", false);
				jsonResult.put("code", -HttpStatus.FAILED_DEPENDENCY.value());
				jsonResult.put("message", "上传请求中不包含multipart/form-data");
				return jsonResult;
			}
			// 获取multiRequest 中所有的文件名
			Iterator<String> iterator = multiRequest.getFileNames();
			while (iterator.hasNext()) {
				String fileName = iterator.next();
				// 一次遍历所有文件
				MultipartFile file = multiRequest.getFile(fileName);
				if (file.isEmpty())
					continue;
				byte[] bytes = file.getBytes();
				String originalName = file.getOriginalFilename();
				String clientIp = WebUtil.getClientIP(multiRequest);
				long fileSize = file.getSize();
				// // 上传
				// file.transferTo(new File(path));
				this.cloudStorageService.uploadFile(bytes, fileSize, originalName, clientIp);
			}
		} catch (Exception ex) {
			jsonResult.put("success", false);
			jsonResult.put("code", -HttpStatus.FAILED_DEPENDENCY.value());
			jsonResult.put("message", ex.getMessage());
			return jsonResult;
		}
		return jsonResult;
	}
}
