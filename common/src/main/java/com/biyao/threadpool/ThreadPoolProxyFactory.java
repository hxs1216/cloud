package com.biyao.threadpool;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Queue;
import java.util.concurrent.*;

/**
 * 租户线程工具类
 *
 * @author: hxs
 * @create: 2020/4/13
 */
public final class ThreadPoolProxyFactory {

    // 线程池队列
    private static final Queue<Executor> executorList = new ConcurrentLinkedDeque<>();

    private ThreadPoolProxyFactory() {
    }

    /**
     * 功能描述 关闭某个线程池
     * 1、当线程池关闭后，继续提交新任务会抛出异常
     * 2、线程池虽然关闭，但是队列中的任务任然继续执行，除非用shutdownNow()
     * 3、不会 interrupt 运行中线程，除非用shutdownNow()
     *
     * @return void
     * @author hxs
     * @date 2020/4/13
     */
    public static void shutdown(ExecutorServiceProxy executorServiceProxy) {
        executorServiceProxy.shutdown();
        executorList.remove(executorServiceProxy);
    }

    /**
     * 功能描述 关闭线程池组
     *
     * @return void
     * @author hxs
     * @date 2020/4/13
     */
    public static synchronized void shutdownAll() {
        Executor executor;
        do {
            executor = executorList.poll();
            if (executor != null) {
                if (executor instanceof DisposableBean) {
                    DisposableBean bean = (DisposableBean) executor;
                    try {
                        bean.destroy();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (executor instanceof ExecutorService) {
                    ExecutorService executorService = (ExecutorService) executor;
                    executorService.shutdown();
                }
            }
        } while (executor != null);
    }

    /**
     * 功能描述 Executors.newCachedThreadPool()
     *
     * @return java.util.concurrent.ExecutorService
     * @author hxs
     * @date 2020/4/13
     */
    public static ExecutorService createThreadPoolExecutor() {
        return createThreadPoolExecutor(
                5,
                200,
                0L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(2048),
                new ThreadFactoryBuilder().setNameFormat("thread-pool-%d").build(),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }

    /**
     * 功能描述 jdk创建的线程
     *
     * @param corePoolSize    核心线程大小
     * @param maximumPoolSize 最大线程大小
     * @param keepAliveTime   存活时间
     * @param unit            单位
     * @param workQueue       队列
     * @param threadFactory
     * @param handler         拒绝处理
     * @return java.util.concurrent.ExecutorService
     * @author hxs
     * @date 2020/4/13
     */
    public static ExecutorService createThreadPoolExecutor(int corePoolSize,
                                                           int maximumPoolSize,
                                                           long keepAliveTime,
                                                           TimeUnit unit,
                                                           BlockingQueue<Runnable> workQueue,
                                                           ThreadFactory threadFactory,
                                                           RejectedExecutionHandler handler) {
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                unit,
                workQueue,
                threadFactory,
                handler
        );
        ExecutorService result = new ExecutorServiceProxy(executorService);
        executorList.add(result);
        return result;
    }

    /**
     * 功能描述 spring创建的线程池
     *
     * @param corePoolSize     核心线程大小
     * @param maxPoolSize      最大线程大小
     * @param queueCapacity    队列容量
     * @param threadNamePrefix 线程前缀
     * @return org.springframework.core.task.AsyncTaskExecutor
     * @author hxs
     * @date 2020/4/13
     */
    public static AsyncTaskExecutor createAsyncTaskExecutor(int corePoolSize,
                                                            int maxPoolSize,
                                                            int queueCapacity,
                                                            String threadNamePrefix) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadNamePrefix);
        AsyncTaskExecutor result = new AsyncTaskExecutorProxy(executor);
        executorList.add(result);
        return result;
    }

    public static void main(String[] args) {
        ExecutorService threadPoolExecutor = ThreadPoolProxyFactory.createThreadPoolExecutor();
        for(int i=1;i<3000;i++){
            threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName());
                }
            });
        }
    }
}
