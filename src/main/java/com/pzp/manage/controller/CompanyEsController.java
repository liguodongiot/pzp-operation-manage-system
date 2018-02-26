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
                settings.getField())
                .setAlias(settings.getAlias())
                .setReplicas(0)
                .build();
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
    @GetMapping(value = "/createIndexLib")
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
    @GetMapping(value = "/deleteIndexLib")
    public String deleteIndexLib(){
        EsUtils.deleteIndexLib(esParam);
        return "[delete index lib success]\n";
    }


    /**
     * http://localhost:8888/company/refreshIndexLib
     * @return
     */
    @GetMapping(value = "/refreshIndexLib")
    public String refreshIndexLib(){
        EsUtils.refreshIndexLib(esParam);
        LOGGER.info("刷新索引成功。。。");
        return "[refresh index lib success]\n";
    }

    /**
     * http://localhost:8888/company/updateIndicesSettings?replicas=2
     * @param replicas
     * @return
     */
    @GetMapping(value = "/updateIndicesSettings")
    public String updateIndicesSettings(Integer replicas){
        esParam.setReplicas(replicas);
        EsUtils.updateIndicesSettings(esParam);
        LOGGER.info("更新索引settings成功。。。");
        return "[update index lib settings success]\n";
    }

    /**
     * http://localhost:8888/company/getIndicesSettings
     * @return
     */
    @GetMapping(value = "/getIndicesSettings")
    public String getIndicesSettings(){
        EsUtils.getIndicesSettings(esParam);
        LOGGER.info("获取索引setting成功。。。");
        return "[get index lib settings success]\n";
    }

    /**
     * http://localhost:8888/company/getIndicesMappings
     * @return
     */
    @GetMapping(value = "/getIndicesMappings")
    public String getIndicesMappings(){
        EsUtils.getIndicesMappings(esParam);
        LOGGER.info("获取索引mappings成功。。。");
        return "[get index lib mappings success]\n";
    }




}
