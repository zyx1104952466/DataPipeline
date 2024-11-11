package com.data.pipeline.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 示例类，用于发送 POST 请求。
 */
public class HttpRequestExample {

    private static final String POST_URL = "http://ysusernew.ysepay.com:18020/manager/feeMethod.do?method=list";
//    private static final String POST_PARAMS = "usercode1=Y156851154099854&trantype1=321&fristFlag=1"; // 应该根据实际情况更改
    private static final String POST_PARAMS = "usercode1=Y156851154099854&trantype1=321&fristFlag=1"; // 应该根据实际情况更改

    /**
     * 发送 POST 请求并打印响应内容。
     *
     * @return 响应内容字符串
     */
    public static String sendPostRequest() {
        try {
            return HttpSender.sendPostRequest(POST_URL, POST_PARAMS);
        } catch (IOException e) {
            // Log the error
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * 发送 GET 请求并打印响应内容。
     *
     * @return 响应内容字符串
     */
    public static String sendGetRequest() {
        try {
            String GET_URL = "http://ysusernew.ysepay.com:16080/boss_manager/urmtminf.do?method=detail&mercId=%s";
            GET_URL = String.format(GET_URL, "826121650460011");
            return HttpSender.sendGetRequest(GET_URL, POST_PARAMS);
        } catch (IOException e) {
            // Log the error
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * 发送 POST 请求并打印响应内容。
     *
     * @return 响应内容字符串
     */
    public static String sendPostRequest(Map<String, String> params) {
        try {
            String param = String.format("usercode1=%s&trantype1=%s&fristFlag=1", params.get("usercode1"), params.get("trantype1"));
            String response = HttpSender.sendPostRequest(POST_URL, param);
            return parseHtmlResponse(response);
        } catch (IOException e) {
            // Log the error
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * 解析 HTML 响应。
     *
     * @param htmlResponse HTML 响应内容
     */
    private static String parseHtmlResponse(String htmlResponse) {
        Document doc = Jsoup.parse(htmlResponse);

        // 示例：获取所有的表格
        Elements tables = doc.select("table");
        Element element = tables.get(2).select("thead").get(0);
        Elements ths = element.select("th");
        Element elementBody = tables.get(2).select("tbody").get(0);
        Elements tbodyths = elementBody.select("td");
        if (tbodyths.size() < 7) {
            return null;
        }
        String code = extractCode(tbodyths.get(6).text());
        return code;
    }

    public static String extractCode(String text) {
        String regex = "\\(([^\\)]+)\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group(1); // 提取括号内的内容
        }
        return null; // 没有找到匹配项
    }

    public static void main(String[] args) {
//        parseHtmlResponse(sendPostRequest());
        parseHtmlResponse(sendGetRequest());
    }
}
