package com.pzp.manage;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.app</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/1/15 13:45 星期一
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.pzp.manage.controller","com.pzp.manage.service",
        "com.pzp.manage.setting",
        "com.pzp.manage.es","com.pzp.manage.exception"})
@MapperScan(basePackages = {"com.pzp.manage.dao"})
@Configuration
//@ServletComponentScan("com.pzp.manage")
@ServletComponentScan
public class PzpManageApplication  {

    public static void main(String[] args) {
        SpringApplication.run(PzpManageApplication.class, args);
    }

}
