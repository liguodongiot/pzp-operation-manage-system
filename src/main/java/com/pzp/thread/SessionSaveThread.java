package com.pzp.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.thread</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/2/6 16:35 星期二
 */
public class SessionSaveThread implements Runnable{

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionSaveThread.class);
    private String sessionId;

    public SessionSaveThread(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public void run() {
        LOGGER.info("开始保存SessionId:{}.",sessionId);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("结束保存SessionId:{}.",sessionId);
    }

}
