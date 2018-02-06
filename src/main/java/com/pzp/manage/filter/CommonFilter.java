package com.pzp.manage.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


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

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

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
