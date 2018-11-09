package com.opsramp.janus.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.opsramp.janus.core.LoadProperties;

/**
 * 
 * @author Murthy Chelankuri
 * 
 */
public class ThreadPool {

	private ExecutorService pool = null;
	private static volatile ThreadPool responsePool = null;

	private static final int NO_OF_THREADS = LoadProperties.getIntProperty("nofthreads.pool", 500);

	private ThreadPool() {
		this.pool = Executors.newFixedThreadPool(NO_OF_THREADS);
	}

	public static ThreadPool getInstance() {
		if (responsePool == null) {
			synchronized (ThreadPool.class) {
				if (responsePool == null) {
					responsePool = new ThreadPool();
				}
			}
		}
		return responsePool;
	}

	public void submit(Runnable runnalble) {
		pool.submit(runnalble);
	}
	
	public void shutDown() {
		pool.shutdown();
	}
	
	public void awaitTermination() {
		try {
			if (!pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)) {
				pool.shutdownNow();
			}
		} catch (InterruptedException ex) {
			pool.shutdownNow();
			Thread.currentThread().interrupt();
		}
	}
}
