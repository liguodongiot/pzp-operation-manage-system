package com.pzp.manage.listener;

import com.pzp.manage.filter.CommonFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.listener</p>
 * <p>Title: 监听Session的创建与销毁 </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/2/5 20:35 星期一
 */
@WebListener
public class ChatSessionListener implements HttpSessionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChatSessionListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        LOGGER.info("Session 被创建");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        LOGGER.info("Session 被销毁");
    }
}
