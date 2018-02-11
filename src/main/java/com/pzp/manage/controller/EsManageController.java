package com.pzp.manage.controller;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.controller</p>
 * <p>Title: ES请求 </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/1/15 13:44 星期一
 */
@Controller
@RequestMapping("/esManage")
public class EsManageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EsManageController.class);

    @Value("${es.token}")
    private String esToken;

    /**
     * http://localhost:8888/esManage/index
     * @return
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView index() {
        return new ModelAndView("esManage");
    }


    /**
     * http://localhost:8888/esManage/handleReq
     * @param reqUrl
     * @param reqMethod
     * @param reqContent
     * @param token
     * @return
     */
    @RequestMapping(value = "/handleReq")
    @ResponseBody
    public Map<String, Object> handleReq(String reqUrl,
                            String reqMethod,
                            String reqContent,
                            String token) {
        Map<String,Object> result = new HashMap<>(1);
        result.put("data",reqUrl);
        return result;
    }


}
