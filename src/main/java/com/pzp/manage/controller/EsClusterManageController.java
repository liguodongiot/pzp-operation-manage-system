package com.pzp.manage.controller;

import com.alibaba.fastjson.JSONObject;
import com.pzp.manage.es.EsClusterManageUtil;
import com.pzp.manage.es.EsContext;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.controller</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/4/25 16:21 星期三
 */
@RestController
@RequestMapping("/es")
public class EsClusterManageController {


    protected final static Logger LOGGER  = LoggerFactory.getLogger(EsSearchController.class);

    @Autowired
    private EsContext esContext;

    /**
     * http://localhost:8888/es/clusterHealth
     * @return
     */
    @RequestMapping("/clusterHealth")
    public String clusterHealth(){
        ClusterHealthResponse clusterHealth = EsClusterManageUtil.getClusterHealth(esContext.getClient());
        return JSONObject.toJSONString(clusterHealth);
    }


}
