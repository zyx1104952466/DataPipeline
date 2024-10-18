package com.data.pipeline.utils;

import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpClientExample {
    public static void main(String[] args) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        // 开启一个连接
    }
}
