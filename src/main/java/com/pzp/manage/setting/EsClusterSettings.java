package com.pzp.manage.setting;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.setting</p>
 * <p>Title: ES集群配置</p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/2/9 11:31 星期五
 */
@Component
@ConfigurationProperties(prefix = "es.cluster")
public class EsClusterSettings {

    private String hosts;
    private Integer port;
    private String name;

    public String getHosts() {
        return hosts;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "EsClusterSettings{" +
                "hosts='" + hosts + '\'' +
                ", port='" + port + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
