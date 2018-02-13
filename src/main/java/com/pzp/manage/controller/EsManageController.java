package com.pzp.manage.controller;

import com.pzp.manage.util.HttpUtil;
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
import java.util.Objects;

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
    @RequestMapping(value = "/handleReq", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> handleReq(String reqUrl,
                            String reqMethod,
                            String reqContent,
                            String token) {
        String responseBody = null;
        Map<String,Object> result = new HashMap<>(1);
        if(!Objects.equals(esToken,token)){
            result.put("data","认证失败！");
            return result;
        }
        if(Objects.equals(HttpUtil.HttpRequestMethodEnum.POST.getMethod(), reqMethod)){
            responseBody = HttpUtil.doPostJson(reqUrl, reqContent);
        } else if(Objects.equals(HttpUtil.HttpRequestMethodEnum.GET.getMethod(), reqMethod)) {
            responseBody = HttpUtil.doGet(reqUrl);
        } else if(Objects.equals(HttpUtil.HttpRequestMethodEnum.PUT.getMethod(), reqMethod)){
            responseBody = HttpUtil.doPutJson(reqUrl, reqContent);
        } else if(Objects.equals(HttpUtil.HttpRequestMethodEnum.DELETE.getMethod(), reqMethod)){
            responseBody = HttpUtil.doDelete(reqUrl);
        }
        result.put("data",responseBody);
        return result;
    }


}
