package com.pzp.manage.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.annotation.Resource;
import javax.sql.DataSource;

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
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"com.pzp.manage.controller","com.pzp.manage.service"})
@MapperScan(basePackages = {"com.pzp.manage.mapper"})
@Configuration
//开启事务
@EnableTransactionManagement
public class PzpManageApplication  {//implements TransactionManagementConfigurer

    public static void main(String[] args) {
        SpringApplication.run(PzpManageApplication.class, args);
    }
//
//    @Resource(name="txManager")
//    private PlatformTransactionManager txManager;
//
//    @Bean(name = "txManager")
//    public PlatformTransactionManager txManager(DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }
//
//    @Override
//    public PlatformTransactionManager annotationDrivenTransactionManager() {
//        return txManager;
//    }


}
