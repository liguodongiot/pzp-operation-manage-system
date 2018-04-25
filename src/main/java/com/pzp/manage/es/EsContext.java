package com.pzp.manage.es;

import com.pzp.manage.setting.EsClusterSettings;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.elasticsearch.xpack.client.PreBuiltXPackTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Describe: ES初始化相关工作
 *
 * @author: guodong.li
 * @datetime: 2017/5/24 11:14
 */

@Component
public class EsContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(EsContext.class);

    @Autowired
    private EsClusterSettings esClusterSettings;

    private TransportClient client;

    @PostConstruct
    public void  init(){
        LOGGER.info("elasticsearch is connecting...");
        //设置集群名称
        Settings settings = Settings.builder()
                .put("cluster.name", esClusterSettings.getName())
                .put("xpack.security.transport.ssl.enabled", false)
                .put("xpack.security.user", "liguodong:liguodong")
                .put("client.transport.sniff", true)//启动嗅探功能
                .put("client.transport.nodes_sampler_interval", "15s")
                .build();

        try {
            String[] hostIps = esClusterSettings.getHosts().split(",");

            for (String hostIp : hostIps) {
                this.client = new PreBuiltXPackTransportClient(settings)
                        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(hostIp), esClusterSettings.getPort()));
                /**
                 * 查看集群信息
                 */
                List<DiscoveryNode> connectedNodes = client.connectedNodes();
                if (connectedNodes.isEmpty()) {
                    LOGGER.warn("hostIp:[{}]没有连接到相应的Elasticsearch集群节点。。。", hostIp);
                    continue;
                }
                StringBuilder hosts = new StringBuilder("");
                for (DiscoveryNode node : connectedNodes) {
                    hosts.append("[")
                            .append(node.getHostAddress())
                            .append("]");
                }
                LOGGER.info("elasticsearch connect hosts:{}.", hosts);
                break;
            }
        } catch (UnknownHostException e) {
            LOGGER.error("elasticsearch connect error, connect param:{}", esClusterSettings.toString(), e);
        }
    }

    @PreDestroy
    public void preDestroy(){
        LOGGER.info("elasticsearch is closing...");
        if(client != null) {
            client.close();
        }
    }

    public TransportClient getClient() {
        return client;
    }

    public EsClusterSettings getEsClusterSettings() {
        return esClusterSettings;
    }
}
