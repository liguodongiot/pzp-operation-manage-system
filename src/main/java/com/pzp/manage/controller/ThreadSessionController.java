package com.pzp.manage.controller;

import com.pzp.thread.SessionSaveThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.controller</p>
 * <p>Title: 测试监听器多线程</p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/2/6 16:21 星期二
 */
@Controller
@RequestMapping("/thread")
public class ThreadSessionController implements InitializingBean{

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadSessionController.class);

    private ExecutorService newFixedThreadPool;


    // http://localhost:8888/thread/index?name=li
    @RequestMapping(value = "/index")
    public ModelAndView index(HttpServletRequest request,
                              HttpServletResponse response,
                              String name) {
        String sessionId = request.getSession().getId();
        LOGGER.info("session: {}.",sessionId);
        newFixedThreadPool.execute(new SessionSaveThread(sessionId));

        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("name",name);
        return modelAndView;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        //可用核数
        int cpuNums = Runtime.getRuntime().availableProcessors();
        LOGGER.info("可用核数：{}。",cpuNums);
        newFixedThreadPool = Executors.newFixedThreadPool(cpuNums);
    }

}
