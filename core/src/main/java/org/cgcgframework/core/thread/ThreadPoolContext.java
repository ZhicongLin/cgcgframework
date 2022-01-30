package org.cgcgframework.core.thread;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池相关配置
 *
 * @author zhicong.lin
 */
public class ThreadPoolContext {

    private static LinkedBlockingQueue<Runnable> linkedBlockingQueue = new LinkedBlockingQueue<>();
    private static ThreadPoolExecutor.AbortPolicy abortPolicy = new ThreadPoolExecutor.AbortPolicy();
    private static ThreadPoolExecutor threadPoolExecutor = ThreadPoolManager.createExecutor(linkedBlockingQueue, abortPolicy);

    public static LinkedBlockingQueue<Runnable> getLinkedBlockingQueue() {
        return linkedBlockingQueue;
    }

    public static void setLinkedBlockingQueue(LinkedBlockingQueue<Runnable> linkedBlockingQueue) {
        ThreadPoolContext.linkedBlockingQueue = linkedBlockingQueue;
    }

    public static ThreadPoolExecutor.AbortPolicy getAbortPolicy() {
        return abortPolicy;
    }

    public static void setAbortPolicy(ThreadPoolExecutor.AbortPolicy abortPolicy) {
        ThreadPoolContext.abortPolicy = abortPolicy;
    }

    public static ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    public static void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor) {
        ThreadPoolContext.threadPoolExecutor = threadPoolExecutor;
    }
}
