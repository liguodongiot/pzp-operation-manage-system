package com.pzp.manage.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Properties;


/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.filter</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/2/5 20:36 星期一
 */
@WebFilter(filterName="commonFilter",urlPatterns="/*")
public class CommonFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonFilter.class);

    @Autowired
    private Environment environment;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String[] activeProfiles = environment.getActiveProfiles();
        String[] defaultProfiles = environment.getDefaultProfiles();
        String property = environment.getProperty("spring.profiles.active");
        //-Dspring.profiles.active=company
        String profiles = System.getProperty("spring.profiles.active");
        LOGGER.info("");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String uri=((HttpServletRequest) request).getRequestURI();
        LOGGER.info("URI is {} .", uri);
        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {
    }
}
