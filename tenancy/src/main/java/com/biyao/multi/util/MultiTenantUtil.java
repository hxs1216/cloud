package com.biyao.multi.util;

import com.biyao.error.CommonException;
import com.biyao.error.ErrorCode;
import com.biyao.multi.TenantDataSourceProvider;
import com.biyao.threadpool.ThreadPoolProxyFactory;
import com.biyao.util.ThreadTenantUtil;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.concurrent.*;
import java.util.function.Supplier;

/**
 * 功能描述 多租户工具类
 *
 * @author hxs
 * @date 2020/5/14
 */
public class MultiTenantUtil {

    private static final Logger log = LoggerFactory.getLogger(MultiTenantUtil.class);

    /**
     * 调用者租户信息
     */
    private static ThreadLocal<String> callerTenant = new ThreadLocal<>();

    /**
     * 多租户工具类执行线程池
     */
    private static ExecutorService pool = ThreadPoolProxyFactory.createThreadPoolExecutor(
            5,
            200,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(2048),
            new ThreadFactoryBuilder().setNameFormat("multi-pool-%d").build(),
            new ThreadPoolExecutor.AbortPolicy());


    /**
     * 在指定租户下执行方法并返回结果
     *
     * @param executeTenantCode 指定租户
     * @param supplier          执行方法
     * @param <T>               返回类型
     * @return 执行结果
     */
    public static <T> T executeWithReturn(final String executeTenantCode, final Supplier<T> supplier) {
        log.debug("executeWithReturn:{},{}", executeTenantCode, supplier);
        checkNeedCrossLibrary(executeTenantCode);
        return execute(executeTenantCode, Boolean.TRUE, supplier);
    }

    /**
     * 租户编码不能为空
     */
    private static void checkNeedCrossLibrary(final String executeTenantCode) {
        Assert.hasText(executeTenantCode, "租户编码不能为空");
    }

    /**
     * 在指定租户下执行方法无返回结果
     *
     * @param executeTenantCode 指定租户
     * @param supplier          执行方法
     * @param <T>               返回类型
     */
    public static <T> void executeWithoutReturn(final String executeTenantCode, final Supplier<T> supplier) {
        log.debug("executeWithoutReturn:{},{}", executeTenantCode, supplier);
        checkNeedCrossLibrary(executeTenantCode);
        execute(executeTenantCode, Boolean.FALSE, supplier);
    }

    /**
     * 在指定租户下异步执行方法
     *
     * @param executeTenantCode 执行租户编码
     * @param needReturn        是否需要返回
     * @param supplier          执行方法
     * @param <T>               返回类型
     * @return 执行结果
     */
    private static <T> T execute(final String executeTenantCode, final boolean needReturn, final Supplier<T> supplier) {
        String currentTenantCode = ThreadTenantUtil.getTenant();
        log.debug("currentTenant:{} execute another tenant:{} of {}", currentTenantCode, executeTenantCode, supplier);
        try {
            if (needReturn) {
                // 带返回值执行异步执行
                Future future = pool.submit((Callable) () -> execute(currentTenantCode, executeTenantCode, supplier));
                return (T) future.get();
            } else {
                // 异步执行方法
                pool.execute(() -> execute(currentTenantCode, executeTenantCode, supplier));
            }
            return null;
        } catch (Exception e) {
            log.error("async execute failed. tenantCode:{},caller:{}", executeTenantCode, getCallerTenant(), e);
            throw new CommonException(ErrorCode.proc_failed, " " + e);
        }
    }


    /**
     * 在默认租户下执行
     *
     * @param supplier 执行方法
     * @param <T>      返回类型
     * @return 执行结果
     */
    public static <T> T executeInDefault(final Supplier<T> supplier) {
        log.debug("executeInDefault:{}", supplier);
        return execute(TenantDataSourceProvider.DEFAULT_KEY, Boolean.TRUE, supplier);
    }

    /**
     * 当前租户执行
     */
    private static <T> T execute(final String currentTenantCode, final String executeTenantCode, final Supplier<T> supplier) {
        try {
            //设置执行租户
            ThreadTenantUtil.setTenant(executeTenantCode);
            setCallerTenant(currentTenantCode);
            return supplier.get();
        } finally {
            remove();
            ThreadTenantUtil.remove();
        }
    }

    /**
     * 设置租户
     */
    private static void setCallerTenant(String tenantCode) {
        if (null != tenantCode) {
            callerTenant.set(tenantCode);
        }
    }

    /**
     * 获取当前线程对应的调用租户
     */
    public static String getCallerTenant() {
        return callerTenant.get();
    }

    /**
     * 删除线程变量
     */
    private static void remove() {
        callerTenant.remove();
    }
}