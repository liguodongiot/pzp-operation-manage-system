package com.pzp.manage.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.controller</p>
 * <p>Title: 测试HttpSessionAttributeListener</p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/2/7 10:38 星期三
 */
@RestController
@RequestMapping("/session")
public class SessionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionController.class);

    // http://localhost:8888/session/setUser?name=user&user=liguodong
    @GetMapping(value = "/setUser")
    public String setUser(HttpServletRequest request,
                            HttpServletResponse response,
                            String name, String user) {
        request.getSession().setAttribute(name, user);
        return name;
    }

    // http://localhost:8888/session/modifyUser?name=user&user=sss
    @GetMapping(value = "/modifyUser")
    public String modifyUser(HttpServletRequest request,
                             HttpServletResponse response,
                             String name, String user) {
        request.getSession().setAttribute(name, user);
        return name;
    }


    // http://localhost:8888/session/removeUser?name=user
    @GetMapping(value = "/removeUser")
    public String removeUser(HttpServletRequest request,
                                    HttpServletResponse response,
                                    String name) {
        request.getSession().removeAttribute(name);
        return name;
    }





}
