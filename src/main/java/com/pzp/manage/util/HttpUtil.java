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

    public static void doGet(String url){

        // 1. 创建HttpClient对象
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 2. 创建HttpGet对象
        HttpGet httpGet = new HttpGet(url);

        CloseableHttpResponse response = null;
        try {
            // 3. 执行GET请求
            response = httpClient.execute(httpGet);
            LOGGER.info(response.getStatusLine().toString());
            // 4. 获取响应实体
            HttpEntity entity = response.getEntity();
            // 5. 处理响应实体
            if (entity != null) {
                LOGGER.info("长度：" + entity.getContentLength());
                LOGGER.info("内容：" + EntityUtils.toString(entity));
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

    }


    public static void doPostJson(String url,String requestBody){
        // 1. 创建HttpClient对象
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 2. 创建HttpPost对象
        HttpPost post = new HttpPost(url);

        // 3. 设置POST请求传递参数
        post.addHeader("Content-type","application/json; charset=utf-8");
        post.setHeader("Accept", "application/json");
        post.setEntity(new StringEntity(requestBody, Charset.forName("UTF-8")));

//        // 3. 设置POST请求传递参数
//        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        params.add(new BasicNameValuePair("username", "test"));
//        params.add(new BasicNameValuePair("password", "12356"));
//        try {
//            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params);
//            post.setEntity(entity);
//        } catch (UnsupportedEncodingException e) {
//            LOGGER.error("HttpUtil#doPost() error, url:{}",url,e);
//        }

        // 4. 执行请求并处理响应
        try {
            CloseableHttpResponse response = httpClient.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                LOGGER.warn("状态码为{}。。。", statusCode);
            }
            HttpEntity entity = response.getEntity();
            if (entity != null){
                System.out.println("响应内容：");
                System.out.println(EntityUtils.toString(entity));
            }
            response.close();
        } catch (IOException e) {
            LOGGER.error("HttpUtil#doPost() error, url:{}",url,e);
        } finally {
            // 释放资源
            try {
                httpClient.close();
            } catch (IOException e) {
                LOGGER.error("HttpUtil#doPost() error, url:{}",url,e);
            }
        }
    }


    public static void doPostForm(String url, Map<String,String> requestParam){
        // 1. 创建HttpClient对象
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        // 2. 创建HttpPost对象
        HttpPost post = new HttpPost(url);

        // 3. 设置POST请求传递参数
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", "test"));
        params.add(new BasicNameValuePair("password", "12356"));
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params);
            post.setEntity(entity);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("HttpUtil#doPost() error, url:{}",url,e);
        }

        // 4. 执行请求并处理响应
        try {
            CloseableHttpResponse response = httpClient.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                LOGGER.warn("状态码为{}。。。", statusCode);
            }
            HttpEntity entity = response.getEntity();
            if (entity != null){
                System.out.println("响应内容：");
                System.out.println(EntityUtils.toString(entity));
            }
            response.close();
        } catch (IOException e) {
            LOGGER.error("HttpUtil#doPost() error, url:{}",url,e);
        } finally {
            // 释放资源
            try {
                httpClient.close();
            } catch (IOException e) {
                LOGGER.error("HttpUtil#doPost() error, url:{}",url,e);
            }
        }
    }


    public static void main(String[] args) {
        doGet("");
        doPostJson("","");
        doPostForm("",null);
    }


}
