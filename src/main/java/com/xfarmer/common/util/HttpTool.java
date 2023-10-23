package com.xfarmer.common.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * @author 东岳
 */
public class HttpTool {

    private HttpTool() {

    }

    private static String encoding = "UTF-8";


    private static String question = "?";

    private static String and = "&";

    private static Charset getDefaultCharset() {
        return StandardCharsets.UTF_8;
    }

    private static HttpClient client = null;


    private static HttpClient getClient() {
        synchronized (HttpTool.class) {
            if (client == null) {
                client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1)
                        .followRedirects(HttpClient.Redirect.NORMAL)
                        .connectTimeout(Duration.ofSeconds(6)).executor(newFixedThreadPool(80)).build();
            }
        }
        return client;
    }


    /**
     * get请求
     *
     * @param url   url链接
     * @param param 请求参数
     * @return
     */
    public static String doGet(String url, Map<String, String> param) {
        return doGet(url, param, new HashMap<>());
    }


    /**
     * get请求
     *
     * @param url   url链接
     * @param param 请求参数
     * @return
     */
    public static String doGet(String url, Map<String, String> param, Map<String, String> head) {

        HttpRequest request = null;
        HttpResponse<String> response = null;
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder().timeout(Duration.ofSeconds(10)).uri(URI.create(url + getParam(param)))
                    .GET();
            buildHeader(head, builder);
            request = builder.build();

            response = getClient().send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (InterruptedException | IOException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
            return "";
        }
    }

    static String jsonContentType = "application/json";

    /**
     * post请求
     *
     * @param url
     * @param body
     * @return
     */
    public static String doPostJson(String url, String body) {
        Map<String, String> header = new HashMap<>();
        header.put("Content-type", jsonContentType);
        return doPost(url, header, body);
    }


    /**
     * post请求
     *
     * @param url
     * @param body
     * @return
     */
    public static String doPostJson(String url, Map<String, String> header, String body) {
        if (header == null) {
            header = new HashMap<>();
        }
        header.put("Content-type", jsonContentType);
        return doPost(url, header, body);
    }

    /**
     * post请求
     *
     * @param url
     * @param header
     * @param body
     * @return
     */
    public static String doPut(String url, Map<String, String> header, String body) {
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(body, getDefaultCharset());
        var builder = HttpRequest.newBuilder().timeout(Duration.ofSeconds(10)).uri(URI.create(url)).PUT(bodyPublisher);
        buildHeader(header, builder);
        return execute(builder, getDefaultCharset());
    }

    /**
     * post请求
     *
     * @param url
     * @param header
     * @param body
     * @return
     */
    public static String doPost(String url, Map<String, String> header, String body) {
        HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofString(body, getDefaultCharset());
        var builder = HttpRequest.newBuilder().timeout(Duration.ofSeconds(10)).uri(URI.create(url)).POST(bodyPublisher);
        buildHeader(header, builder);
        return execute(builder, getDefaultCharset());
    }


    private static void buildHeader(Map<String, String> header, HttpRequest.Builder builder) {
        builder.setHeader("Content-Type", jsonContentType);
        if (header != null && header.size() > 0) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                builder.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    private static String execute(HttpRequest.Builder builder, Charset charset) {
        var request = builder.build();
        try {
            HttpResponse<String> response = getClient().send(request, HttpResponse.BodyHandlers.ofString(charset));
            String result = null;
            if (response.statusCode() == 200) {
                result = response.body();
            } else {
                result = "";
            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 将map参数拼接成url参数
     *
     * @param param url参数
     * @return ?param1=xxx&param2=xxx
     */
    public static String getParam(Map<String, String> param) throws UnsupportedEncodingException {
        if (param == null || param.isEmpty()) {
            return "";
        }
        StringBuilder paramStringBuffer = new StringBuilder();
        Set<Map.Entry<String, String>> entries = param.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            if (StringUtil.isNotNull(entry.getValue())) {
                paramStringBuffer
                        .append(entry.getKey())
                        .append("=")
                        .append(URLDecoder.decode(entry.getValue(), encoding))
                        .append("&");
            }
        }
        String paramStr = paramStringBuffer.toString();
        if (!paramStr.startsWith(question)) {
            paramStr = "?" + paramStr;
        }
        if (paramStr.endsWith(and)) {
            paramStr = StringUtil.substringBeforeLast(paramStr, "&");
        }
        return paramStr;
    }


}
