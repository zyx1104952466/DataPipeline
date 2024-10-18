package com.data.pipeline.utils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpClientGetExample {
    public static void main(String[] args) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 创建HttpGet对象
            HttpGet httpGet = new HttpGet("http://example.com");

            // 发送GET请求
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                // 获取响应实体
                String responseBody = EntityUtils.toString(response.getEntity());
                System.out.println("Response Status: " + response.getStatusLine().getStatusCode());
                System.out.println("Response Body: " + responseBody);
            } catch (IOException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
