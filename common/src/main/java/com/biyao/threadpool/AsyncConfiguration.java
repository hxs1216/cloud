package com.biyao.threadpool;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * spring的线程池
 *
 * @author: hxs
 * @create: 2020/4/13
 */
@Configuration
@EnableAsync
@EnableScheduling
@ConditionalOnProperty(prefix = "jhipster.async", name = {"core-pool-size", "max-pool-size", "queue-capacity"})
public class AsyncConfiguration implements AsyncConfigurer {

//    private final Logger log = LoggerFactory.getLogger(AsyncConfiguration.class);
//
//    @Value("${eureka.instance.appname:}")
//    private String APP_NAME;
//    @Value("${jhipster.async.core-pool-size:2}")
//    private Integer corePoolSize;
//    @Value("${jhipster.async.max-pool-size:50}")
//    private Integer maxPoolSize;
//    @Value("${jhipster.async.queue-capacity:10000}")
//    private Integer queueCapacity;
//
//    public static final String THREAD_NAME_PREFIX = "Common-Executor-";
//
//    @Override
//    @Bean(name = "taskExecutor", destroyMethod = "destroy")
//    @Primary
//    public Executor getAsyncExecutor() {
//        log.debug("Creating Async Task Executor");
//        return ThreadPoolProxyFactory.createAsyncTaskExecutor(
//                corePoolSize,
//                maxPoolSize,
//                queueCapacity,
//                StringUtils.isBlank(APP_NAME) ? THREAD_NAME_PREFIX : APP_NAME + "-" + THREAD_NAME_PREFIX
//        );
//    }
//
//    @Override
//    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
//        return new SimpleAsyncUncaughtExceptionHandler();
//    }
//
//    @EventListener(ContextClosedEvent.class)
//    public void onApplicationEvent(ContextClosedEvent event) {
//        ThreadPoolProxyFactory.shutdownAll();
//    }
}