package com.pzp.manage.controller;

import com.pzp.manage.es.EsContext;
import com.pzp.manage.es.EsParam;
import com.pzp.manage.es.EsUtils;
import com.pzp.manage.setting.CompanyIndexSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @date 2018/2/24 16:46 星期六
 */
@RestController
@RequestMapping("/company")
public class CompanyEsController implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyEsController.class);
    @Autowired
    private CompanyIndexSettings settings;

    @Autowired
    private EsContext esContext;

    private EsParam esParam;

    @Override
    public void afterPropertiesSet() throws Exception {
        esParam = new EsParam.ParamBuilder(esContext.getClient(),
                settings.getName(),settings.getType(),
                settings.getField()).setAlias(settings.getAlias()).build();
    }

    /**
     * http://localhost:8888/company/getEsIndexInfo
     * @return
     */
    @GetMapping(value = "/getEsIndexInfo")
    public String getEsIndexInfo(){
        return settings.toString();
    }


    /**
     * http://localhost:8888/company/createIndexLib
     * curl -XGET  http://10.250.140.14:9200/alibaba?pretty
     * @return
     */
    @RequestMapping(value = "/createIndexLib")
    @ResponseBody
    public String createIndexLib(){
        EsUtils.deleteIndexLib(esParam);
        EsUtils.createIndexLib(esParam,true);
        return "[create index lib success]\n";
    }


    /**
     * http://localhost:8888/company/deleteIndexLib
     * curl -XGET  http://172.22.1.28:9200/_cat/indices
     * curl -XGET  http://172.22.1.28:9200/user_info_v1?pretty
     */
    @RequestMapping(value = "/deleteIndexLib")
    @ResponseBody
    public String deleteIndexLib(){
        EsUtils.deleteIndexLib(esParam);
        return "[delete index lib success]\n";
    }


    /**
     * http://localhost:8888/company/refreshIndexLib
     * @return
     */
    @RequestMapping(value = "/refreshIndexLib")
    @ResponseBody
    public String refreshIndexLib(){
        EsUtils.refreshIndexLib(esParam);
        LOGGER.info("刷新索引成功。。。");
        return "[refresh index lib success]\n";
    }


}
