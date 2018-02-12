package com.pzp.manage.controller;

import com.pzp.manage.es.EsContext;
import com.pzp.manage.es.EsParam;
import com.pzp.manage.es.EsUtils;
import com.pzp.manage.setting.UserManageIndexSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

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
@RequestMapping("/userManage")
public class UserManageEsController implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserManageEsController.class);
    @Autowired
    private UserManageIndexSettings settings;

    @Autowired
    private EsContext esContext;

    private EsParam esParamVa;

    private EsParam esParamVb;

    /**
     * http://localhost:8888/userManage/getUserManage
     * @return
     */
    @GetMapping(value = "/getUserManage")
    public String getDbInfo(){
        return settings.toString();
    }


    /**
     * http://localhost:8888/userManage/createIndexLib?version=vb&isUseAlias=false
     * http://localhost:8888/userManage/createIndexLib?version=va&isUseAlias=true
     * @return
     */
    @GetMapping(value = "/createIndexLib")
    public String createIndexLib(
            @RequestParam(value="version", required=false) String version,
            @RequestParam(value="isUseAlias", required=false) String isUseAlias){
        if (Objects.equals(version,"vb")) {
            EsUtils.deleteIndexLib(esParamVb);
            EsUtils.createIndexLib(esParamVb, Boolean.valueOf(isUseAlias));
        } else if (Objects.equals(version,"va")) {
            EsUtils.deleteIndexLib(esParamVa);
            EsUtils.createIndexLib(esParamVa, Boolean.valueOf(isUseAlias));
        }
        return "[create index success,version="+ version + "]\n";
    }


    /**
     * http://localhost:8888/userInfo/deleteIndexLib
     * curl -XGET  http://172.22.1.28:9200/_cat/indices
     * curl -XGET  http://172.22.1.28:9200/user_info_v1?pretty
     */
    @GetMapping(value = "/deleteIndexLib")
    public String deleteIndexLib(
            @RequestParam(value="version", required=false) String version){
        if(Objects.equals(version,"vb")){
            EsUtils.deleteIndexLib(esParamVb);
        } else {
            EsUtils.deleteIndexLib(esParamVa);
        }
        return "[delete index success,version="+ version +"]\n";
    }


    /**
     * http://localhost:8888/userInfo/refreshIndexLib?version=vb
     * @return
     */
    @GetMapping(value = "/refreshIndexLib")
    public String refreshIndexLib(
            @RequestParam(value="version", required=false) String version){
        if(Objects.equals(version,"vb")){
            EsUtils.refreshIndexLib(esParamVb);
        } else {
            EsUtils.refreshIndexLib(esParamVa);
        }
        LOGGER.info("刷新索引成功。。。");
        return "[refresh index success,version="+ version +"]\n";
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        esParamVa = new EsParam.ParamBuilder(esContext.getClient(),
                settings.getNameVa(),settings.getType(),
                settings.getField()).setAlias(settings.getAlias()).build();

        esParamVb = new EsParam.ParamBuilder(esContext.getClient(),
                settings.getNameVb(),settings.getType(),
                settings.getField()).setAlias(settings.getAlias()).build();
    }
}
