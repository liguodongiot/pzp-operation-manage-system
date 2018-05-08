package com.pzp.manage.controller;


import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.elasticsearch.xpack.ml.datafeed.extractor.DataExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Date;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.controller</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/1/15 13:44 星期一
 */
@Controller
@RequestMapping("/demo")
public class DemoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoController.class);

    private static String USER_NAME = "";

    private static String location = "http://localhost:8888/word.txt";

    /**
	 * 上次更改时间
	 */
    private String lastModified;
    /**
     * 资源属性
     */
    private String eTags;

    private static CloseableHttpClient httpclient = HttpClients.createDefault();
    // http://localhost:8888/word.txt
    // http://localhost:8888/demo/hello?name=li
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    @ResponseBody
    public String hello(String name) {
        return "hello: "+name;
    }

    // http://localhost:8888/demo/putUserName?name=dddd
    @RequestMapping(value = "/putUserName", method = RequestMethod.GET)
    @ResponseBody
    public String putUserName(String name) {
        USER_NAME = name;
        return "update name: "+name;
    }
    // http://localhost:8888/demo/getUserName
    @RequestMapping(value = "/getUserName", method = RequestMethod.GET)
    @ResponseBody
    public String getUserName(HttpServletResponse response) {
        return USER_NAME;
    }
    // http://localhost:8888/demo/getHttpInfo
    @RequestMapping(value = "/getHttpInfo", method = RequestMethod.GET)
    @ResponseBody
    public Long getHttpInfo() {

        //超时设置
        RequestConfig rc = RequestConfig.custom().setConnectionRequestTimeout(10*1000)
                .setConnectTimeout(10*1000).setSocketTimeout(15*1000).build();

        HttpHead head = new HttpHead(location);
        head.setConfig(rc);
        LOGGER.info("start lastModified:[{}], eTags:[ {} ].",lastModified, eTags);
        //设置请求头
        if (lastModified != null) {
            head.setHeader("If-Modified-Since", lastModified);
        }
        if (eTags != null) {
            head.setHeader("If-None-Match", eTags);
        }

        CloseableHttpResponse response = null;
        try {

            response = httpclient.execute(head);

            //返回200 才做操作
            if(response.getStatusLine().getStatusCode()==200){
                LOGGER.info("lastModified:[{}]...",response.getLastHeader("Last-Modified"));
                LOGGER.info("eTags:[{}]...",response.getLastHeader("ETag"));
                boolean flag = ((response.getLastHeader("Last-Modified")!=null) &&
                        !response.getLastHeader("Last-Modified").getValue().equalsIgnoreCase(lastModified))
                        ||((response.getLastHeader("ETag")!=null) &&
                        !response.getLastHeader("ETag").getValue().equalsIgnoreCase(eTags));
                if (flag) {

                    // 远程词库有更新,需要重新加载词典，

                    lastModified = response.getLastHeader("Last-Modified")==null?null:response.getLastHeader("Last-Modified").getValue();
                    eTags = response.getLastHeader("ETag")==null?null:response.getLastHeader("ETag").getValue();
                    LOGGER.info("lastModified:[{}], eTags:[ {} ].",lastModified, eTags);
                }
            }else if (response.getStatusLine().getStatusCode()==304) {
                LOGGER.info("no do it.");
                //没有修改，不做操作
                //noop
            }else{
                LOGGER.info("remote_ext_dict {} return bad code {}" , location , response.getStatusLine().getStatusCode() );
            }

        } catch (Exception e) {
            LOGGER.error("remote_ext_dict {} error!",e , location);
        }finally{
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        return System.currentTimeMillis();
    }



}
