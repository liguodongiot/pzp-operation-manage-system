
Filter的使用有两种配置方式:
1.注解配置Filter
```java
@WebFilter(filterName="myfilter", urlPatterns="/myHttpServlet",   
    initParams={  
            @WebInitParam(name="server", value="www.baidu.com"),  
            @WebInitParam(name="port", value="443")  
        }  
    )  
public class MyServletFilter implements Filter {}
```


2.web.xml文件中配置
```xml
<filter>  
    <filter-name>myServletFilter1</filter-name>  
    <filter-class>com.javaservlet.servlet.filter.MyServletFilter1</filter-class>  
    <init-param>  
        <param-name>server</param-name>  
        <param-value>www.baidu.com</param-value>  
    </init-param>  
    <init-param>  
        <param-name>port</param-name>  
        <param-value>443</param-value>  
    </init-param>  
</filter>  
  
<filter-mapping>  
    <filter-name>myServletFilter1</filter-name>  
    <url-pattern>/myHttpServlet</url-pattern>  
</filter-mapping> 
```

3.Java配置
```java
/**
 * 配置过滤器
 * @return
 */
@Bean
public FilterRegistrationBean someFilterRegistration() {
    FilterRegistrationBean registration = new FilterRegistrationBean();
    registration.setFilter(sessionFilter());
    registration.addUrlPatterns("/*");
    registration.addInitParameter("paramName", "paramValue");
    registration.setName("sessionFilter");
    return registration;
}

/**
 * 创建一个bean
 * @return
 */
@Bean(name = "sessionFilter")
public Filter sessionFilter() {
    return new SessionFilter();
}
```