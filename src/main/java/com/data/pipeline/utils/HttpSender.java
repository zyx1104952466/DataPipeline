package com.data.pipeline.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 通用的 HTTP 请求发送工具类。
 */
public class HttpSender {

    private static final String COOKIE = "JSESSIONID=FDE39B9BCBD12D40AF66ECCE652826E1; hikefa_ticket=\"xbfu1hfKNGmzRAYHKDGwuGyDIkEhr8QyNXXQ4IOOrZImWWBB0zChN4QiGWLe6V01F8DvBdd27dkgLK6rFLctoO1Dc8m0CTo22VgqxJBLypXzfV7ZAKMDnwj9vJkoyD9CTfbcp0juXPH4vqU+y4rQWwS6mRo9VEf9m3H61kEb494hn8s3LOjUkhudfIiksRP7GVh4BZKyboPmzKm0U4MCox/X+fra4QqrXObF9eobrRycNWthQqUYlwftXblfGSW9mBSWnY0jJr4ssHnYxIwvDw==\"";

    /**
     * 创建一个新的 HTTP 连接。
     *
     * @param urlString URL 地址字符串
     * @param method    HTTP 方法类型（GET/POST）
     * @return 新创建的 HttpURLConnection 对象
     * @throws IOException 如果打开连接失败
     */
    private static HttpURLConnection createConnection(String urlString, String method) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        return connection;
    }

    /**
     * 设置 HTTP 请求头属性。
     *
     * @param connection HTTP 连接对象
     * @param params     请求体参数
     */
    private static void setRequestProperties(HttpURLConnection connection, String params) {
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
//        connection.setRequestProperty("Content-Length", Integer.toString(params.getBytes().length));
        connection.setRequestProperty("Cookie", COOKIE);
    }

    /**
     * 写入请求体数据。
     *
     * @param connection   HTTP 连接对象
     * @param requestBody  请求体内容
     * @throws IOException 如果写入数据失败
     */
    private static void writeRequestBody(HttpURLConnection connection, String requestBody) throws IOException {
        connection.getOutputStream().write(requestBody.getBytes());
        connection.getOutputStream().flush();
        connection.getOutputStream().close();
    }

    /**
     * 读取 HTTP 响应内容。
     *
     * @param connection HTTP 连接对象
     * @return 响应内容字符串
     * @throws IOException 如果读取数据失败
     */
    private static String readResponse(HttpURLConnection connection) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            return content.toString();
        }
    }

    /**
     * 发送 POST 请求。
     *
     * @param url     请求 URL
     * @param body    请求体内容
     * @return 响应内容字符串
     * @throws IOException 如果发送请求失败
     */
    public static String sendPostRequest(String url, String body) throws IOException {
        HttpURLConnection connection = null;
        try {
            connection = createConnection(url, "POST");
            setRequestProperties(connection, body);
            writeRequestBody(connection, body);
            return readResponse(connection);
        } catch (IOException e) {
            throw e;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * 发送 GET 请求。
     *
     * @param url     请求 URL
     * @param body    请求体内容
     * @return 响应内容字符串
     * @throws IOException 如果发送请求失败
     */
    public static String sendGetRequest(String url, String body) throws IOException {
        HttpURLConnection connection = null;
        try {
            connection = createConnection(url, "GET");
            setRequestProperties(connection, body);
            return readResponse(connection);
        } catch (IOException e) {
            throw e;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
