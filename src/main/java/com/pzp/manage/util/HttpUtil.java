package com.pzp.manage.util;

import com.pzp.manage.listener.MySessionListener;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Describe:
 *
 * @author: liguodong
 * @datetime: 2018/2/12 8:38
 */
public class HttpUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * HTTP请求方式
     */
    public enum HttpRequestMethodEnum {
        POST("POST"),
        GET("GET"),
        HEAD("HEAD"),
        PUT("PUT"),
        DELETE("DELETE");

        private String method;

        HttpRequestMethodEnum(String method) {
            this.method = method;
        }

        public String getMethod() {
            return method;
        }


    }


    public static String doGet(String url){

        String responseBody = null;

        // 1. 创建HttpClient对象
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 2. 创建HttpGet对象
        HttpGet httpGet = new HttpGet(url);

        CloseableHttpResponse response = null;
        try {
            // 3. 执行GET请求
            response = httpClient.execute(httpGet);
            String statusInfo = response.getStatusLine().toString();
            LOGGER.info("请求状态信息：{}。", statusInfo);
            // 4. 获取响应实体
            HttpEntity entity = response.getEntity();
            // 5. 处理响应实体
            if (entity != null) {
                responseBody = EntityUtils.toString(entity);
                LOGGER.info("响应内容：{}...", responseBody);
            }
        } catch (IOException e) {
            LOGGER.error("HttpUtil#doGet() error, url:{}",url,e);
        } finally {
            // 6. 释放资源
            try {
                response.close();
                httpClient.close();
            } catch (IOException e) {
                LOGGER.error("HttpUtil#doGet() error, url:{}",url,e);
            }
        }
        return responseBody;
    }

    public static String doPostJson(String url,String requestBody){
        String responseBody = null;
        // 1. 创建HttpClient对象
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 2. 创建HttpPost对象
        HttpPost post = new HttpPost(url);

        // 3. 设置POST请求传递参数
        post.addHeader("Content-type","application/json; charset=utf-8");
        post.setHeader("Accept", "application/json");
        post.setEntity(new StringEntity(requestBody, Charset.forName("UTF-8")));

        // 4. 执行请求并处理响应
        try {
            CloseableHttpResponse response = httpClient.execute(post);
            String statusInfo = response.getStatusLine().toString();
            LOGGER.info("HttpUtil#doPostJson()请求状态信息：{}。", statusInfo);

            HttpEntity entity = response.getEntity();
            if (entity != null){
                responseBody = EntityUtils.toString(entity);
                LOGGER.info("响应内容：{}",responseBody);
            }
            response.close();
        } catch (IOException e) {
            LOGGER.error("HttpUtil#doPost() error, url:{}",url,e);
        } finally {
            // 释放资源
            try {
                httpClient.close();
            } catch (IOException e) {
                LOGGER.error("HttpUtil#doPostJson() error, url:{}",url,e);
            }
        }
        return responseBody;
    }


    public static String doPostForm(String url, Map<String,String> requestParam){
        String responseBody = null;
        // 1. 创建HttpClient对象
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 2. 创建HttpPost对象
        HttpPost post = new HttpPost(url);

        // 3. 设置POST请求传递参数
        List<NameValuePair> params = new ArrayList<>();
        for (Map.Entry<String, String> entry : requestParam.entrySet()) {
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params);
            post.setEntity(entity);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("HttpUtil#doPostForm() error, url:{}", url, e);
        }

        // 4. 执行请求并处理响应
        try {
            CloseableHttpResponse response = httpClient.execute(post);
            String statusInfo = response.getStatusLine().toString();
            LOGGER.info("HttpUtil#doPostForm()请求状态信息：{}。", statusInfo);
            HttpEntity entity = response.getEntity();
            if (entity != null){
                responseBody = EntityUtils.toString(entity);
                LOGGER.info("响应内容：{}...",responseBody);
            }
            response.close();
        } catch (IOException e) {
            LOGGER.error("HttpUtil#doPostForm() error, url:{}",url,e);
        } finally {
            // 释放资源
            try {
                httpClient.close();
            } catch (IOException e) {
                LOGGER.error("HttpUtil#doPostForm() error, url:{}",url,e);
            }
        }
        return responseBody;
    }


    public static void main(String[] args) {
//        doGet("http://localhost:8888/userInfo/get?name=小明");
//        doPostJson("http://localhost:8888/userInfo/addUserInfo","{\"name\":\"33dongdong3\",\"age\":333}");
        Map<String,String> requestParam = new HashMap<>();
        requestParam.put("id","11");
        requestParam.put("name","fdsgsd");
        requestParam.put("age","32");
        doPostForm("http://localhost:8888/userInfo/addUserInfoByForm",requestParam);
    }


}
