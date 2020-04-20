package com.biyao.threadpool;

import com.biyao.util.ThreadTenantUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

/**
 * AbstractExecutorProxy
 *
 * @author: hxs
 * @create: 2020/4/13
 */
public abstract class AbstractExecutorProxy implements Executor, InitializingBean, DisposableBean {
    private static final Logger log = LoggerFactory.getLogger(AbstractExecutorProxy.class);

    // 线程池
    protected abstract Executor getExecutor();

    @Override
    public void execute(Runnable task) {
        this.getExecutor().execute(this.createWrappedRunnable(task));
    }

    /**
     * 功能描述 Callable线程执行+租户号、上下文
     *
     * @author hxs
     * @date 2020/4/13
     */
    protected <T> Callable<T> createCallable(final Callable<T> task) {
        final String currentTenant = ThreadTenantUtil.getTenant();
        return () -> {
            try {
                ThreadTenantUtil.setTenant(currentTenant);
                return task.call();
            } catch (Exception e) {
                handle(e);
                throw e;
            } finally {
                ThreadTenantUtil.remove();
            }
        };
    }

    /**
     * 功能描述 Runnable线程执行+租户号、上下文
     *
     * @author hxs
     * @date 2020/4/13
     */
    protected Runnable createWrappedRunnable(final Runnable task) {
        final String currentTenant = ThreadTenantUtil.getTenant();
        return () -> {
            try {
                ThreadTenantUtil.setTenant(currentTenant);
                task.run();
            } catch (Exception e) {
                handle(e);
                throw e;
            } finally {
                ThreadTenantUtil.remove();
            }
        };
    }

    protected void handle(Exception e) {
        log.error("Caught exception", e);
    }
}
