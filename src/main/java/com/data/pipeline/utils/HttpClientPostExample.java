package com.data.pipeline.utils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpClientPostExample {
    public static void main(String[] args) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // 创建HttpPost对象
            HttpPost httpPost = new HttpPost("http://example.com/api/resource");

            // 设置请求头
            httpPost.setHeader("Content-type", "application/json");

            // 设置请求体
            StringEntity entity = new StringEntity("{\"key\":\"value\"}");
            httpPost.setEntity(entity);

            // 发送POST请求
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                // 获取响应实体
                String responseBody = EntityUtils.toString(response.getEntity());
                System.out.println("Response Status: " + response.getStatusLine().getStatusCode());
                System.out.println("Response Body: " + responseBody);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
