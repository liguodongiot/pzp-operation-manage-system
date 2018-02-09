package com.pzp.manage.controller;

import com.pzp.manage.es.EsContext;
import com.pzp.manage.es.EsParam;
import com.pzp.manage.es.EsUtils;
import com.pzp.manage.setting.DbSettings;
import com.pzp.manage.setting.UserInfoIndexSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.controller</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/2/9 11:08 星期五
 */
@RestController
@RequestMapping("/userInfo")
public class UserInfoEsController implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserInfoEsController.class);
    @Autowired
    private UserInfoIndexSettings userInfoIndexSettings;

    @Autowired
    private EsContext esContext;

    private EsParam esParam;

    /**
     * http://localhost:8888/userInfo/getUserInfo
     * @return
     */
    @GetMapping(value = "/getUserInfo")
    public String getDbInfo(){
        return userInfoIndexSettings.toString();
    }



    @RequestMapping(value = "/createIndexAndSetting")
    @ResponseBody
    public String createIndexAndSetting(){
        EsUtils.deleteIndexLib(esParam);
        EsUtils.createIndexLib(esParam);
        return "[create index success]\n";
    }


    /**
     * http://localhost:8888/userInfo/deleteIndexLib
     * curl -XGET  http://172.22.1.28:9200/_cat/indices
     * curl -XGET  http://172.22.1.28:9200/user_info_v1?pretty
     */
    @RequestMapping(value = "/deleteIndexLib")
    @ResponseBody
    public String deleteIndexLib(){
        EsUtils.deleteIndexLib(esParam);
        return "[delete index success]\n";
    }


    /**
     * http://localhost:8888/userInfo/refreshIndexLib
     * @return
     */
    @RequestMapping(value = "/refreshIndexLib")
    @ResponseBody
    public String refreshIndexLib(){
        EsUtils.refreshIndexLib(esParam);
        LOGGER.info("刷新索引成功。。。");
        return "[refresh index success]\n";
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        esParam = new EsParam.ParamBuilder(esContext.getClient(),
                userInfoIndexSettings.getName(),userInfoIndexSettings.getType(),
                userInfoIndexSettings.getField()).build();
    }
}
