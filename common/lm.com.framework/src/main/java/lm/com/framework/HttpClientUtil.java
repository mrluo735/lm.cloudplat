package lm.com.framework;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NoHttpResponseException;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HttpClient帮助类
 * 
 * @author mrluo735
 *
 */
public class HttpClientUtil {
	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	/**
	 * 默认header
	 */
	@SuppressWarnings("all")
	public static final Map<String, String> DEFAULT_HEADER = new HashMap<String, String>() {
		{
			put("Content-Type", "application/x-www-form-urlencoded");
		}
	};

	/**
	 * 获取HttpClient
	 * <p>
	 * 支持http和https方式的请求
	 * </p>
	 * 
	 * @return
	 */
	public static CloseableHttpClient createHttpClient() {
		try {
			// 自动恢复机制---->HttpRequestRetryHandler
			HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
				@Override
				public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
					if (executionCount >= 3) {
						// Do not retry if over max retry count
						return false;
					}
					if (exception instanceof NoHttpResponseException) {
						// Retry if the server dropped connection on us
						return true;
					}
					if (exception instanceof InterruptedIOException) {
						// Timeout
						return false;
					}
					if (exception instanceof UnknownHostException) {
						// Unknown host
						return false;
					}
					if (exception instanceof ConnectTimeoutException) {
						// Connection refused
						return true;
					}
					if (exception instanceof SSLException) {
						// Do not retry on SSL handshake exception
						return false;
					}
					return false;
				}
			};

			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				// 信任所有
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();

			HostnameVerifier hostNameVerifier = SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
			SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext,
					hostNameVerifier);
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", PlainConnectionSocketFactory.getSocketFactory())
					.register("https", sslConnectionSocketFactory).build();
			// 初始化连接管理器
			PoolingHttpClientConnectionManager pcm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
			// Increase max total connection to 200
			pcm.setMaxTotal(200);
			// Increase default max connection per route to 20
			pcm.setDefaultMaxPerRoute(20);
			// Increase max connections for localhost:80 to 50
			// HttpHost localhost = new HttpHost("locahost", 80);
			// pcm.setMaxPerRoute(new HttpRoute(localhost), 50);
			return HttpClients.custom().setConnectionManager(pcm).setRetryHandler(retryHandler).build();
			// return
			// HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
		} catch (KeyManagementException ex) {
			logger.error("获取HttpClient出错了", ex);
		} catch (NoSuchAlgorithmException ex) {
			logger.error("获取HttpClient出错了", ex);
		} catch (KeyStoreException ex) {
			logger.error("获取HttpClient出错了", ex);
		}

		return HttpClients.createDefault();
	}

	/**
	 * get 请求
	 * 
	 * @param url
	 * @param headers
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String get(String url, Map<String, String> headers) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(url);
		if (null != headers)
			for (Entry<String, String> item : headers.entrySet()) {
				httpGet.addHeader(item.getKey(), item.getValue());
			}
		CloseableHttpResponse response = httpClient.execute(httpGet);
		try {
			StatusLine statusLine = response.getStatusLine();
			logger.info("响应代码：{},响应原因：{}", statusLine.getStatusCode(), statusLine.getReasonPhrase());
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity httpEntity = response.getEntity();
				String result = EntityUtils.toString(httpEntity, "UTF-8");
				EntityUtils.consume(httpEntity);
				return result;
			}
			return "";
		} finally {
			response.close();
		}
	}

	/**
	 * 重载+1 post 请求
	 * 
	 * @param url
	 * @param requestBody
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String post(String url, String requestBody) throws ClientProtocolException, IOException {
		return post(url, null, requestBody, EnumPostEntityType.BASICHTTPENTITY);
	}

	/**
	 * 重载+2 post 请求
	 * 
	 * @param url
	 * @param requestBody
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String post(String url, Map<String, String> headers, String requestBody)
			throws ClientProtocolException, IOException {
		return post(url, headers, requestBody, EnumPostEntityType.BASICHTTPENTITY);
	}

	/**
	 * 重载+3 post 请求
	 * 
	 * @param url
	 * @param headers
	 * @param requestBody
	 * @param entityType
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String post(String url, Map<String, String> headers, String requestBody,
			EnumPostEntityType entityType) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		if (null == headers)
			headers = DEFAULT_HEADER;
		else
			for (Entry<String, String> item : headers.entrySet()) {
				httpPost.addHeader(item.getKey(), item.getValue());
			}

		if (null == entityType)
			entityType = EnumPostEntityType.BASICHTTPENTITY;
		switch (entityType) {
		case BASICHTTPENTITY:
			BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
			basicHttpEntity.setContent(new ByteArrayInputStream(requestBody.getBytes("UTF-8")));
			basicHttpEntity.setContentLength(requestBody.getBytes("UTF-8").length);
			httpPost.setEntity(basicHttpEntity);
			break;
		case STRINGENTITY:
			StringEntity stringEntity = new StringEntity(requestBody);
			httpPost.setEntity(stringEntity);
			break;
		case BYTEARRAYENTITY:
			ByteArrayEntity byteArrayEntity = new ByteArrayEntity(requestBody.getBytes("UTF-8"));
			httpPost.setEntity(byteArrayEntity);
			break;
		case FILEENTITY:
			FileEntity fileEntity = new FileEntity(new File(""));
			httpPost.setEntity(fileEntity);
			break;
		case HTTPENTITYWRAPPER:
			// HttpEntityWrapper httpEntityWrapper = new
			// HttpEntityWrapper(wrappedEntity);
			break;
		case BUFFEREDHTTPENTITY:
			// BufferedHttpEntity bufferedHttpEntity = new
			// BufferedHttpEntity(entity);
			break;
		default:
			basicHttpEntity = new BasicHttpEntity();
			basicHttpEntity.setContent(new ByteArrayInputStream(requestBody.getBytes("UTF-8")));
			basicHttpEntity.setContentLength(requestBody.getBytes("UTF-8").length);
			httpPost.setEntity(basicHttpEntity);
			break;
		}

		CloseableHttpResponse response = httpClient.execute(httpPost);
		try {
			StatusLine statusLine = response.getStatusLine();
			logger.info("响应代码：{},响应原因：{}", statusLine.getStatusCode(), statusLine.getReasonPhrase());
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity httpEntity = response.getEntity();
				String result = EntityUtils.toString(httpEntity, "UTF-8");
				EntityUtils.consume(httpEntity);
				return result;
			}
			return "";
		} finally {
			response.close();
		}
	}

	/**
	 * put 请求
	 * 
	 * @param url
	 * @param headers
	 * @param requestBody
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String put(String url, Map<String, String> headers, String requestBody)
			throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPut httpPut = new HttpPut(url);
		for (Entry<String, String> item : headers.entrySet()) {
			httpPut.addHeader(item.getKey(), item.getValue());
		}

		BasicHttpEntity requestEntity = new BasicHttpEntity();
		requestEntity.setContent(new ByteArrayInputStream(requestBody.getBytes("UTF-8")));
		requestEntity.setContentLength(requestBody.getBytes("UTF-8").length);
		httpPut.setEntity(requestEntity);

		CloseableHttpResponse response = httpClient.execute(httpPut);
		try {
			StatusLine statusLine = response.getStatusLine();
			logger.info("响应代码：{},响应原因：{}", statusLine.getStatusCode(), statusLine.getReasonPhrase());
			HttpEntity httpEntity = response.getEntity();
			String result = EntityUtils.toString(httpEntity, "UTF-8");
			EntityUtils.consume(httpEntity);
			return result;
		} finally {
			response.close();
		}
	}

	/**
	 * delete 请求
	 * 
	 * @param url
	 * @param headers
	 * @param requestBody
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String delete(String url, Map<String, String> headers, String requestBody)
			throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpDelete httpDelete = new HttpDelete(url);
		for (Entry<String, String> item : headers.entrySet()) {
			httpDelete.addHeader(item.getKey(), item.getValue());
		}

		CloseableHttpResponse response = httpClient.execute(httpDelete);
		try {
			StatusLine statusLine = response.getStatusLine();
			logger.info("响应代码：{},响应原因：{}", statusLine.getStatusCode(), statusLine.getReasonPhrase());
			HttpEntity httpEntity = response.getEntity();
			String result = EntityUtils.toString(httpEntity, "UTF-8");
			EntityUtils.consume(httpEntity);
			return result;
		} finally {
			response.close();
		}
	}

	/**
	 * post EntityType枚举
	 * 
	 * @author mrluo735
	 *
	 */
	public enum EnumPostEntityType {
		/**
		 * BasicHttpEntity
		 */
		BASICHTTPENTITY,
		/**
		 * StringEntity
		 */
		STRINGENTITY,
		/**
		 * ByteArrayEntity
		 */
		BYTEARRAYENTITY,
		/**
		 * FileEntity
		 */
		FILEENTITY,
		/**
		 * HttpEntityWrapper
		 */
		HTTPENTITYWRAPPER,
		/**
		 * BufferedHttpEntity
		 */
		BUFFEREDHTTPENTITY
	}
}
