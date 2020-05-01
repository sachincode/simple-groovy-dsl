package com.sachin.example.simplegroovydsl.core

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializerFeature
import com.sachin.example.simplegroovydsl.Constants
import com.sachin.example.simplegroovydsl.exception.AdapterExecException
import groovy.util.logging.Slf4j
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.slf4j.MDC


@Slf4j
class AdapterContext {

    // Adapter name
    private final String name

    // 全局变量
    private final Map global

    // 入参
    private Map input

    // 请求api时的请求体
    def body

    // 请求api时的响应体，code为http code，body为响应string
    def response = [:]

    // 局部变量，限制于当前一次dsl请求执行内
    def ctx = [:]

    // 解析出的api定义
    final Map<String, AdapterRequest> api

    final OkHttpClient client

    static Map<String, AdapterExecutor> executors


    static {
        String.metaClass.asJSON = { -> return JSON.parseObject(delegate as String) }
        Map.metaClass.toJSONString() { -> return toJSONString(delegate) }
    }


    public AdapterContext(String name, Map global, Map<String, AdapterRequest> api, OkHttpClient client) {
        this.name = name
        this.global = global
        this.api = api
        this.client = client
    }

    public AdapterContext(String name, Map global, Map<String, AdapterRequest> api, OkHttpClient client, Map input) {
        this.name = name
        this.global = global
        this.api = api
        this.client = client
        this.input = input
    }

    def fail() {
        throw new AdapterExecException(name)
    }

    def fail(String message) {
        throw new AdapterExecException(name, message)
    }

    /**
     * 调用其他adapter
     * @param adapterName适配器名称
     * @return 该adapter的执行结果
     */
    def require(String adapterName) {
        require(adapterName, input)
    }

    /**
     * 调用其他adapter
     * @param adapterName适配器名称
     * @param 指定入参 /上下文，调用的adapter都从input中取参数
     * @return 该adapter的执行结果
     */
    def require(String adapterName, Map<String, Object> customInput) {
        MDC.put(Constants.MDC_CUR_ADAPTER_ID, adapterName)
        executors.get(adapterName).doAdapter(customInput)
    }

    /**
     *
     * @param apiKey api中定义的名称
     */
    def callApi(String apiKey) {
        AdapterRequest req = api[apiKey]
        if (req == null) {
            fail('不存在此api的定义：' + apiKey)
        }
        // 定义动态url优先级高于静态url
        def url = req._urlClosure != null ? req._urlClosure.rehydrate(this, this, this)() : req._url
        // url 不限定于http协议，可以自定义协议并在此解析为可供httpclient调用的http即可
        log.info("[{}] [{}]--执行http-->", name, apiKey, url)
        def urlBuilder = HttpUrl.parse(url).newBuilder()
        def builder = new Request.Builder()
        def header = req._headerClosure != null ? req._headerClosure.rehydrate(this, this, this)() : req._header
        header?.each { k, v ->
            builder.header(k, v)
        }

        if ('GET'.equalsIgnoreCase(req._method)) {
            if (body) {
                if (body instanceof Map) {
                    body.each { k, v -> urlBuilder.addQueryParameter(k as String, v as String) }
                } else if (body instanceof String || body instanceof GString) {
                    urlBuilder.query(body as String)
                }
            }
            builder.url(urlBuilder.build()).get()
        } else {
            builder.url(urlBuilder.build())
            def contentType = header['Content-Type'] as String
            def requestBody
            if (contentType.contains("form") && body instanceof Map) {
                def form = new FormBody.Builder()
                body.each { k, v -> form.add(k as String, v as String) }
                requestBody = form.build()
            } else {
                requestBody = RequestBody.create(MediaType.parse(contentType ? contentType : 'application/json'),
                        body instanceof String || body instanceof GString ? body.bytes : JSON.toJSONBytes(body, SerializerFeature.DisableCircularReferenceDetect))
            }
            builder.method(req._method, requestBody)
        }

        // 假定trackId
        def trackId = MDC.get(Constants.MDC_TRACKING_ID)
        if (trackId) {
            builder.addHeader(Constants.TRACKING_ID_HEADER, trackId)
        }

        def request = builder.build()
        def resp = null
        try {
            long start = System.currentTimeMillis()
            if (req._trace) {
                log.info("[{}] 发送请求[{}], request=[{}].", name, apiKey, request)
            }
            resp = client.newCall(request).execute()
            response.code = resp.code()
            response.body = resp.body().string()
            long useTime = System.currentTimeMillis() - start
            if (req._trace) {
                log.info("[{}] 收到响应[{}], time={}, code=[{}], body={}.", name, apiKey, useTime, response.code, response.body)
            } else {
                log.info("[{}] 收到响应[{}], time={}, code=[{}].", name, apiKey, useTime, response.code)
            }
            return response
        } catch (Exception e) {
            throw new AdapterExecException(name, '调用API失败：' + apiKey, e)
        } finally {
            resp?.close()
        }
    }


    private static String toJSONString(def o) {
        return JSON.toJSONString(o, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteMapNullValue)
    }

}
