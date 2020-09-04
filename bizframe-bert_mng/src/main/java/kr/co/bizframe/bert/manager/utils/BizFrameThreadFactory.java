package kr.co.bizframe.bert.manager.utils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class BizFrameThreadFactory implements ThreadFactory {
	private final String name;
	private final boolean daemon;
	private final AtomicInteger threadNumber = new AtomicInteger(1);

	public BizFrameThreadFactory(String name) {
		this(name, false);
	}

	public BizFrameThreadFactory(String name, boolean daemon) {
		this.name = name;
		this.daemon = daemon;
	}

	public Thread newThread(Runnable r) {
		int num = this.threadNumber.getAndIncrement();
		String threadName = this.name + "_" + num;
		Thread t = new Thread(r, threadName);
		if (this.daemon) {
			t.setDaemon(true);
		}
		return t;
	}

}
