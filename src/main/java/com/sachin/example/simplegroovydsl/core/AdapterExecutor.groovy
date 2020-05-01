package com.sachin.example.simplegroovydsl.core

import com.sachin.example.simplegroovydsl.exception.AdapterExecException
import groovy.util.logging.Slf4j
import okhttp3.OkHttpClient
import org.springframework.scheduling.config.CronTask
import org.springframework.scheduling.config.FixedDelayTask
import org.springframework.scheduling.config.ScheduledTask
import org.springframework.scheduling.config.ScheduledTaskRegistrar

import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

@Slf4j
class AdapterExecutor {

    private static final AtomicInteger threadCount = new AtomicInteger(0)
    private static final ScheduledTaskRegistrar scheduler = new ScheduledTaskRegistrar()
    private static final Map<String, ScheduledTask> scheduledFutures = new ConcurrentHashMap<>()

    private final AdapterDefinition definition
    private final String name

    private def global = [:]
    private Map<String, Closure> api
    private Map<String, AdapterRequest> resolvedApi = [:]

    private final OkHttpClient httpClient

    static {
        scheduler.scheduler = Executors.newScheduledThreadPool(4, { r ->
            def t = new Thread(r, "Adapter-Scheduler-${threadCount.getAndIncrement()}")
            t.setDaemon(true)
            t
        })
    }


    AdapterExecutor(AdapterDefinition definition, String name) {
        this.name = name
        this.definition = definition
        cancelScheduledTask(name)
        // 执行闭包，给定义的global和api赋值、解析
        def delegate = this
        if (this.definition.givenCall) {
            this.definition.givenCall.delegate = delegate
            this.definition.givenCall()
        }
        api.each { n, c ->
            def request = new AdapterRequest()
            resolvedApi[n] = c.rehydrate(delegate, delegate, request)()
        }
        if (global != null) {
            global = Collections.unmodifiableMap(global)
        }
        resolvedApi = Collections.unmodifiableMap(resolvedApi)
        // 初始化OkHttpClient
        def builder = new OkHttpClient.Builder()
        def trustManager = getX509TrustManager()
        SSLContext sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, [trustManager] as TrustManager[], new SecureRandom())
        builder.sslSocketFactory(sslContext.getSocketFactory(), trustManager)
        builder.connectTimeout(global?.httpConnectTimeout ?: 10000, TimeUnit.MILLISECONDS)
        builder.writeTimeout(global?.httpReadTimeout ?: 10000, TimeUnit.MILLISECONDS)
        builder.readTimeout(global?.httpWriteTimeout ?: 10000, TimeUnit.MILLISECONDS)
        httpClient = builder.build()
        // 发布时首次生成定时任务
        def scheduleContext = new AdapterContext(name, global, resolvedApi, httpClient)
        if (this.definition.scheduleCall) {
            this.definition.scheduleCall.delegate = scheduleContext
            def task
            def command = {
                while (true) {
                    try {
                        log.info("开始执行[{}]定时任务.", name)
                        this.definition.scheduleCall()
                        log.info("执行[{}]定时任务成功.", name)
                        break
                    } catch (Exception e) {
                        log.info("执行[{}]定时任务失败, 3s后重新执行.", name, e)
                        Thread.sleep(3000L)
                    }
                }
            }
            try {
                if (this.definition.cronExp) {
                    task = scheduler.scheduleCronTask(new CronTask(command, this.definition.cronExp))
                    log.info("设置[{}](cron)定时任务成功.", name)
                } else {
                    task = scheduler.scheduleFixedDelayTask(new FixedDelayTask(command, this.definition.rate, 0))
                    log.info("设置[{}](delay)定时任务成功.", name)
                }
                scheduledFutures.put(name, task)
            } catch (Exception e) {
                log.error("设置[{}]定时任务失败.", name, e)
            }
        }
    }

    Object doAdapter(Map<String, Object> input) {
        def context = new AdapterContext(name, global, resolvedApi, httpClient, input)
        def result
        try {
            result = this.definition.executeCall.rehydrate(context, this, this.definition)()
        } catch (Exception e) {
            throw new AdapterExecException(this.name, e)
        }
        if (result != null) {
            return result
        } else {
            throw new AdapterExecException(this.name, "执行结果不能返回null值")
        }
    }

    static void cancelScheduledTask(String name) {
        if (scheduledFutures.remove(name)?.cancel()) {
            log.info("停止[{}]定时任务成功.", name)
        }
    }


    private static X509TrustManager getX509TrustManager() {
        return new X509TrustManager() {

            @Override
            void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0]
            }
        }
    }
}
