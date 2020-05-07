package com.biyao.constants;

import com.biyao.threadpool.ThreadPoolProxyFactory;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/*
 * 公共参数
 *
 * @author hxs
 * @date 2020/5/7
 */
public class Constants {

    public static final String TENANT_CODE_KEY = "tenant_code";
    public static final String PROFILE_MONGO = "mongo";
    public static final String PROFILE_MYSQL = "mysql";


    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_TEST = "test";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";
    public static final String SPRING_PROFILE_SWAGGER = "swagger";

    /**
     * 定义异步执行部分接口调用需要的线程池对象
     */
    public static ExecutorService pool = ThreadPoolProxyFactory.createThreadPoolExecutor(
            5,
            200,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1024),
            new ThreadFactoryBuilder().setNameFormat("thread-pool-%d").build(),
            new ThreadPoolExecutor.AbortPolicy());
}