package com.pzp.manage.controller;

import com.pzp.manage.setting.DbSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
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
 * @date 2018/1/19 16:18 星期五
 */
@RestController
@RequestMapping("/db")
public class DbController {

    @Autowired
    private DbSettings dbSettings;

    @Value("${mybatis.mapperLocations}")
    private String mapperLocations;

    /**
     * http://localhost:8888/db/getBaseInfo
     * @return
     */
    @GetMapping(value = "/getBaseInfo")
    public String getDbInfo(){
        return dbSettings.toString();
    }

    /**
     * http://localhost:8888/db/getMybatisLocation
     * @return
     */
    @GetMapping(value = "/getMybatisLocation")
    public String getMybatisLocation(){
        return mapperLocations;
    }

}
