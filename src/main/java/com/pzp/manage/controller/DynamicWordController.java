package com.pzp.manage.controller;

import com.alibaba.fastjson.JSONObject;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.library.DicLibrary;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.controller</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/5/8 19:22 星期二
 */
@Controller
@RequestMapping("/dynamic")
public class DynamicWordController {


    // http://localhost:8888/dynamic/token
    @RequestMapping(value = "/token", method = RequestMethod.GET)
    @ResponseBody
    public String token() {
        Result parse = ToAnalysis.parse("李国玲是哪里人");
        List<Term> terms = parse.getTerms();
        return JSONObject.toJSONString(terms);
    }

    // http://localhost:8888/dynamic/add
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    @ResponseBody
    public Long add() {
//        MyStaticValue.isNumRecognition = true;
//        MyStaticValue.isQuantifierRecognition = false;
//        DicLibrary.insert(DicLibrary.DEFAULT, "蛇药片", "n", 1000);
        DicLibrary.insert(DicLibrary.DEFAULT,"李国玲是");
        return System.currentTimeMillis();
    }

    // http://localhost:8888/dynamic/del
    @RequestMapping(value = "/del", method = RequestMethod.GET)
    @ResponseBody
    public Long del() {
        DicLibrary.delete(DicLibrary.DEFAULT,"李国玲是");
        return System.currentTimeMillis();
    }

}
