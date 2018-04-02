package com.pzp.manage.controller;

import com.alibaba.fastjson.JSONObject;
import com.pzp.manage.es.EsContext;
import com.pzp.manage.es.EsSearchParam;
import com.pzp.manage.es.EsUtils;
import com.pzp.manage.setting.UserInfoIndexSettings;
import org.apache.commons.io.FileUtils;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.controller</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/4/2 12:52 星期一
 */
@RestController
@RequestMapping("/search")
public class EsSearchController {

    protected final static Logger LOGGER  = LoggerFactory.getLogger(EsSearchController.class);

    @Autowired
    private EsContext esContext;
    @Autowired
    private UserInfoIndexSettings settings;

    /**
     * http://localhost:8888/search/script
     * @return
     */
    @RequestMapping("/script")
    public String templateScript(){
        EsSearchParam esSearchParam = new EsSearchParam.ParamBuilder(esContext.getClient(),
                settings.getName(),settings.getType())
                .setAlias(settings.getAlias())
                .build();

        Map<String, Object> templateParams = new HashMap<>();
        templateParams.put("param_name", "刘飞");

        File file = new File("src/main/resources/","template_gender.mustache");
        LOGGER.info("File: {}",file.getAbsolutePath());

        String templateGender;
        try {
            templateGender = FileUtils.readFileToString(file, "UTF-8");
        } catch (IOException e) {
            LOGGER.error("IO error. ",e);
            return "";
        }
        SearchHits searchHits = EsUtils.searchTemplate(esSearchParam, templateParams, templateGender);
        long total = searchHits.getTotalHits();
        SearchHit[]  searchHitArr = searchHits.getHits();
        LOGGER.info("total:{}", total);
        LOGGER.info("searchHitArr:{}", JSONObject.toJSON(searchHitArr));
        return JSONObject.toJSONString(searchHitArr);
    }


}
