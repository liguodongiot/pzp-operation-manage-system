package com.pzp.manage.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.listener</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/2/5 20:59 星期一
 */
@WebListener
public class MyServletContextListener  implements ServletContextListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyServletContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOGGER.info("ServletContex初始化");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        LOGGER.info("ServletContex销毁");
    }
}
