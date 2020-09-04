package kr.co.bizframe.bert.manager.handler;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class HttpClientConnectionManager {
	private static Logger logger = LoggerFactory.getLogger(HttpClientConnectionManager.class);

	private static final long CLOSE_IDLE_CONNECTION_WAIT_TIME_SEC = 10;

	@Value("${http.connect.timeout:10000}")
	public static int CONNECT_TIMEOUT = 10000;

	@Value("${http.connect.request.timeout:10000}")
	public static int REQUEST_TIMEOUT = 10000;

	@Value("${http.connect.read.timeout:300000}")
	public static int SOCKET_TIMEOUT = 300000;

	@Value("${http.total.connect:20}")
	public static int MAX_TOTAL_CONNECTIONS = 20;

	private RestTemplate restTemplate;

	private static HttpClientConnectionManager instance;

	private HttpClientConnectionManager() {
		restTemplate = new RestTemplate(getClientHttpRequestFactory());
	}

	public static synchronized HttpClientConnectionManager getInstance() {
		if (instance == null) {
			instance = new HttpClientConnectionManager();
		}
		return instance;
	}

	private PoolingHttpClientConnectionManager getPoolingConnectionManager() {
		SSLContextBuilder builder = new SSLContextBuilder();
		try {
			builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
		} catch (NoSuchAlgorithmException e) {
			logger.error("Pooling Connection Manager Initialisation failure because of " + e.getMessage());
		} catch (KeyStoreException e) {
			logger.error("Pooling Connection Manager Initialisation failure because of " + e.getMessage());
		}

		SSLConnectionSocketFactory sslsf = null;
		try {
			sslsf = new SSLConnectionSocketFactory(builder.build());
		} catch (KeyManagementException e) {
			logger.error("Pooling Connection Manager Initialisation failure because of " + e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			logger.error("Pooling Connection Manager Initialisation failure because of " + e.getMessage());
		}

		Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
				.register("https", sslsf)
				.register("http", new PlainConnectionSocketFactory())
				.build();

		PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager(
				socketFactoryRegistry);
		poolingConnectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
		// 해당 client는 엘라스틱만 쓴다는 가정, 한 도메인만 쓰기때문에 같게 설정
		poolingConnectionManager.setDefaultMaxPerRoute(MAX_TOTAL_CONNECTIONS);
		return poolingConnectionManager;
	}

	private CloseableHttpClient getHttpClient() {

		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectionRequestTimeout(REQUEST_TIMEOUT)
				.setConnectTimeout(CONNECT_TIMEOUT)
				.setSocketTimeout(SOCKET_TIMEOUT).build();

		CloseableHttpClient httpClient = HttpClientBuilder.create()
				.setConnectionManager(getPoolingConnectionManager())
				.setDefaultRequestConfig(requestConfig)
				.setConnectionManagerShared(true)
				//.evictIdleConnections(CLOSE_IDLE_CONNECTION_WAIT_TIME_SEC, TimeUnit.SECONDS)
				.build();

		return httpClient;
	}

	private HttpComponentsClientHttpRequestFactory getClientHttpRequestFactory() {
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setHttpClient(getHttpClient());
		return clientHttpRequestFactory;
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}
}
