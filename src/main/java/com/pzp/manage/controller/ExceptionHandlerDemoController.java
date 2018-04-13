package com.pzp.manage.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.controller</p>
 * <p>Title: </p>
 * <p>Description:  @ExceptionHandler </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/4/13 17:28 星期五
 */
@RestController
@RequestMapping("/exception")
public class ExceptionHandlerDemoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DemoController.class);

    // http://localhost:8888/exception/hello?name=li
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    @ResponseBody
    public String hello(String name) {
        int num = 1/0;
        return "hello: "+name+num;
    }

    // http://localhost:8888/exception/hello2?name=li
    @RequestMapping(value = "/hello2", method = RequestMethod.GET)
    @ResponseBody
    public String hello2(String name) {
        Map<String,String> map = new HashMap<>();
        try{
            String aaa = map.get("aaa").toString();
        } catch (NullPointerException e){
            LOGGER.error("error",e);
        }
        return "hello: "+name;
    }


}
