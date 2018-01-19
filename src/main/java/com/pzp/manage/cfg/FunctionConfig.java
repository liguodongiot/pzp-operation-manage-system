package com.pzp.manage.cfg;

import com.pzp.manage.service.FunctionService;
import com.pzp.manage.service.UseFunctionService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.cfg</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/1/19 14:29 星期五
 */
@Configuration
@ComponentScan(basePackages = {"com.pzp.manage.service.chart"})
public class FunctionConfig {

    @Bean
    public FunctionService functionService(){
        return  new FunctionService();
    }

    @Bean
    public UseFunctionService useFunctionService(){
        UseFunctionService useFunctionService = new UseFunctionService();
        useFunctionService.setFunctionService(functionService());
        return useFunctionService;
    }



}
