package kr.co.bizframe.bert.manager.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import kr.co.bizframe.bert.manager.utils.Strings;
import kr.co.bizframe.bert.manager.utils.BizFrameThreadFactory;

@Component
public class ExecutorServiceFactory {

	public static final String DEFAULT_EXECUTOR_SERVICE_NAME = "BWM";

	private static Logger logger = LoggerFactory.getLogger(ExecutorServiceFactory.class);

	private Map<String, ExecutorService> executorServices = new HashMap<String, ExecutorService>();
	private Map<String, ScheduledExecutorService> scheduledExecutorServices = new HashMap<String, ScheduledExecutorService>();

	@Value("${executor.service.thread.size:0}")
	private int nThreads;

	@Value("${executor.service.queue.limit:0}")
	private int serviceQueueLimit;

	@Value("${executor.service.shutdown.now:false}")
	private boolean executorShutdownNow;

	@PostConstruct
	public void init() {
		if (nThreads == 0) {
			nThreads = Runtime.getRuntime().availableProcessors();
		}

		if (serviceQueueLimit == 0) {
			serviceQueueLimit = nThreads * 10;
		}
	}

	public ExecutorService getExecutorService() {
		return getExecutorService(null);
	}

	public ExecutorService getExecutorService(String name) {
		if (name == null) {
			name = DEFAULT_EXECUTOR_SERVICE_NAME;
		}

		return getExecutorService(name, nThreads);
	}

	public ExecutorService getExecutorService(String name0, int nThreads) {
		return getExecutorService(name0, nThreads, serviceQueueLimit);
	}

	public ExecutorService getExecutorService(String name0, int nThreads, int queueLimit) {
		return getExecutorService(name0, nThreads, queueLimit, null);
	}

	public ExecutorService getExecutorService(String name0, int nThreads, int queueLimit,
			RejectedExecutionHandler rejectionHandler) {
		final String name = Strings.trim(name0);

		ExecutorService executorService;
		synchronized (executorServices) {
			if (executorServices.containsKey(name)) {
				executorService = executorServices.get(name);
			} else {
				ThreadFactory threadFactory = new BizFrameThreadFactory(name);
				if (queueLimit > 0) {
				} else {
					queueLimit = serviceQueueLimit;
				}
				final BlockingQueue<Runnable> bq = new ArrayBlockingQueue<Runnable>(queueLimit);
				executorService = new ThreadPoolExecutor(nThreads, nThreads + 1, 60L, TimeUnit.SECONDS, bq,
						threadFactory);
				if (rejectionHandler == null) {
					rejectionHandler = new RejectedExecutionHandler() {
						public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
							try {
								long startT = 0L;
								if (logger.isDebugEnabled()) {
									startT = System.currentTimeMillis();
								}

								executor.getQueue().put(r);

								if (logger.isDebugEnabled()) {
									logger.debug("[" + name + "] RejectedExecutionHandler, queue 대기 시간 = "
											+ (System.currentTimeMillis() - startT) + " ms");
								}

							} catch (Throwable e) {
								logger.error("[" + name + "] Fixed Pool Size creat fail", e);
							}
						}
					};
				}
				((ThreadPoolExecutor) executorService).setRejectedExecutionHandler(rejectionHandler);

			}
			executorServices.put(name, executorService);
		}

		return executorService;
	}

	public ScheduledExecutorService getScheduledExecutorService(String name) {
		name = Strings.trim(name);
		if (name == null) {
			name = DEFAULT_EXECUTOR_SERVICE_NAME;
		}
		return getScheduledExecutorService(name, 0);
	}

	public ScheduledExecutorService getScheduledExecutorService(String name0, int nThreads0) {
		String name = Strings.trim(name0);

		ScheduledExecutorService scheduledExecutorService;
		synchronized (scheduledExecutorServices) {
			if (scheduledExecutorServices.containsKey(name)) {
				scheduledExecutorService = scheduledExecutorServices.get(name);
			} else {
				ThreadFactory threadFactory = new BizFrameThreadFactory(name);
				if (nThreads0 <= 0) {
					if (logger.isDebugEnabled()) {
						logger.debug("[" + name + "] Single Scheduled Thread Pool created");
					}
					scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(threadFactory);
				} else {
					if (logger.isDebugEnabled()) {
						logger.debug("[" + name + "] Scheduled Thread Pool created : poolSize[" + nThreads0 + "]");
					}
					scheduledExecutorService = Executors.newScheduledThreadPool(nThreads0, threadFactory);
				}

				scheduledExecutorServices.put(name, scheduledExecutorService);
			}
		}

		return scheduledExecutorService;
	}

	@PreDestroy
	public void shutdown() {
		synchronized (executorServices) {
			for (Entry<String, ExecutorService> entry : executorServices.entrySet()) {
				if (executorShutdownNow) {
					entry.getValue().shutdownNow();
				} else {
					entry.getValue().shutdown();
				}
			}
			executorServices.clear();
		}

		synchronized (scheduledExecutorServices) {
			for (Entry<String, ScheduledExecutorService> entry : scheduledExecutorServices.entrySet()) {
				if (executorShutdownNow) {
					entry.getValue().shutdownNow();
				} else {
					entry.getValue().shutdown();
				}
			}
			scheduledExecutorServices.clear();
		}
	}

}
