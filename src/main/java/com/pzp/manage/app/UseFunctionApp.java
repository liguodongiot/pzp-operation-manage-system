package com.pzp.manage.app;

import com.pzp.manage.cfg.FunctionConfig;
import com.pzp.manage.service.UseFunctionService;
import com.pzp.manage.service.chart.UnionChartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.app</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/1/19 14:37 星期五
 */
public class UseFunctionApp {

    private final static Logger LOGGER  = LoggerFactory.getLogger(UseFunctionApp.class);

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(FunctionConfig.class);
        UseFunctionService useFunctionService = context.getBean(UseFunctionService.class);
        String result = useFunctionService.sayHello("liguodong");
        LOGGER.info(result);

//        UserInfoController userInfoController = context.getBean(UserInfoController.class);
//        LOGGER.info(userInfoController.listUserInfo().toString());

        UnionChartService unionChartService = new UnionChartService();
        LOGGER.info(unionChartService.getChart("pie"));
        context.close();
    }

}
