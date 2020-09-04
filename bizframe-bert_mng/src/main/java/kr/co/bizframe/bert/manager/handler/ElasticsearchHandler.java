package kr.co.bizframe.bert.manager.handler;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import com.google.gson.JsonObject;

import kr.co.bizframe.bert.manager.model.api.KorquadInput.ParagraphsContext;
import kr.co.bizframe.bert.manager.model.api.TrainingData;
import kr.co.bizframe.bert.manager.service.ExecutorServiceFactory;
import kr.co.bizframe.bert.manager.utils.Strings;

@Component
public class ElasticsearchHandler {

	private static Logger logger = LoggerFactory.getLogger(ElasticsearchHandler.class);

	// es host
	@Value("${es.host:192.168.10.2}")
	private String host;

	// tsd port
	@Value("${es.port:9200}")
	private int port;

	private String DEFAULT_ENDPOINT;

	private String BULK_INSERT_ENDPOINT;

	@Autowired
	private ExecutorServiceFactory executorServiceFactory;

	private ExecutorService insertContextExecutorService;

	public static final String ES_BERT_CONTEXT_INDEX = "ex-bert-contexts";
	public static final String ES_BERT_CONTEXT_TYPE = "doc";

	@Value("${dashboard.query.thread.size:6}")
	private int queryThreadsNum;

	@Value("${dashboard.query.queue.limit:30}")
	private int queryThreadsQueueLimit;

	@Value("${es.bulkinsert.number:2000}")
	public int bulkinsert_number = 2000;
	
	@Value("${bert.context.length:30}")
	public int bert_context_length = 30;

	@PostConstruct
	public void init() {
		DEFAULT_ENDPOINT = "http://" + host + ":" + port + "/";

		BULK_INSERT_ENDPOINT = DEFAULT_ENDPOINT + "/_bulk";

		RejectedExecutionHandler clearRejectionHandler = new RejectedExecutionHandler() {
			public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
				// 모두 버림
				logger.error("===clearPolicy queue clear ====" + executor.getQueue().size());
				executor.getQueue().clear();
			}
		};

		insertContextExecutorService = executorServiceFactory.getExecutorService("BERT_CONTEXT_INSERT_EXECUTOR",
				queryThreadsNum, queryThreadsQueueLimit, clearRejectionHandler);
	}

	@PreDestroy
	public void shutdown() {

		logger.info("ElasticsearchHandler successful shutdown !!");
	}

	public void bulkInsert(String requestBody, String url) {

		if (requestBody == null) {
			return;
		}

		int bodyLen = requestBody.length();
		if (bodyLen == 0) {
			return;
		}	

		insertContextExecutorService.execute(new Runnable() {
			@Override
			public void run() {
				try {
					long startT = System.currentTimeMillis();
					ResponseEntity<String> result = httpRequest(url, requestBody, HttpMethod.POST);
					logger.debug(url + " : "+result.getStatusCodeValue() + " " +result.getBody() );
				} catch (Throwable e) {
					// log something
					logger.error("bulkInsert[" + url + ", " + bodyLen + "] error " + e.getMessage());
				}
			}
		});
	}

	private HttpHeaders getHttpHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		return headers;
	}
	
	public ResponseEntity<String> httpRequest(String url, String requestBody, HttpMethod httpMethod)
			throws MalformedURLException, URISyntaxException, UnknownHostException, RestClientException {

		RequestEntity<String> request = new RequestEntity<String>(requestBody, getHttpHeaders(), httpMethod,
				new URI(url));

		return HttpClientConnectionManager.getInstance().getRestTemplate().exchange(request, String.class);
	}	
	
	public void insertContexts(List<ParagraphsContext> datas) {
		try {
			
			StringBuffer metricsb = new StringBuffer();
			
			int i = 0;
			JsonObject idxJson = new JsonObject();
			JsonObject metaJson = new JsonObject();
			metaJson.addProperty("_index", ES_BERT_CONTEXT_INDEX);
			metaJson.addProperty("_type", ES_BERT_CONTEXT_TYPE);
			idxJson.add("index", metaJson);
			long time = System.currentTimeMillis();
			for (ParagraphsContext pdata : datas) {
				String title = Strings.trim(pdata.getTitle());
				
				for (TrainingData data : pdata.getParagraphs()) {
					
					try {
						JsonObject json = new JsonObject();
						json.addProperty("timestamp", time);
						json.addProperty("title", title);
						json.addProperty("context", data.getContext());
						metricsb.append(idxJson);
						metricsb.append("\n");

						metricsb.append(json.toString());
						metricsb.append("\n");

						i++;

					} catch (Throwable e) {
						// ignore
						logger.debug("insertContexts insert error " + e.getMessage());
					}

					if (i % bulkinsert_number == 0) {
						bulkInsert(metricsb.toString(), BULK_INSERT_ENDPOINT);
						metricsb = new StringBuffer();
					}
				}
			}
			
			// 남은 insert
			bulkInsert(metricsb.toString(), BULK_INSERT_ENDPOINT);
			
		} catch (Throwable e) {
			logger.error("elasticsearch insert context error " + e.getMessage(), e);
		}
	}
	
	
	public void insertContexts(Map<String, List<String>> datas) {
		try {
			
			StringBuffer metricsb = new StringBuffer();
			
			int i = 0;
			JsonObject idxJson = new JsonObject();
			JsonObject metaJson = new JsonObject();
			metaJson.addProperty("_index", ES_BERT_CONTEXT_INDEX);
			metaJson.addProperty("_type", ES_BERT_CONTEXT_TYPE);
			idxJson.add("index", metaJson);
			long time = System.currentTimeMillis();
			for (String category : datas.keySet()) {
				for (String data : datas.get(category)) {
					
					try {
						JsonObject json = new JsonObject();
						json.addProperty("timestamp", time);
						json.addProperty("category", category);
						json.addProperty("context", data);
						metricsb.append(idxJson);
						metricsb.append("\n");

						metricsb.append(json.toString());
						metricsb.append("\n");

						i++;

					} catch (Throwable e) {
						// ignore
						logger.debug("insertContexts insert error " + e.getMessage());
					}

					if (i % bulkinsert_number == 0) {
						bulkInsert(metricsb.toString(), BULK_INSERT_ENDPOINT);
						metricsb = new StringBuffer();
					}
				}
			}
			
			// 남은 insert
			bulkInsert(metricsb.toString(), BULK_INSERT_ENDPOINT);
			
		} catch (Throwable e) {
			logger.error("elasticsearch insert context error " + e.getMessage(), e);
		}
	}
}
