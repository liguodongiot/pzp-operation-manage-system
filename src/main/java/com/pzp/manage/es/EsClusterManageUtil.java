package com.pzp.manage.es;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.health.ClusterHealthStatus;
import org.elasticsearch.cluster.health.ClusterIndexHealth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.es</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/4/25 16:00 星期三
 */
public class EsClusterManageUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(EsClusterManageUtil.class);

   public static ClusterHealthResponse getClusterHealth(TransportClient client){
       ClusterHealthResponse healths = null;
       try {
           healths = client.admin().cluster().prepareHealth().get();
           String clusterName = healths.getClusterName();
           int numberOfDataNodes = healths.getNumberOfDataNodes();
           int numberOfNodes = healths.getNumberOfNodes();
           LOGGER.info("clusterName: {} , numberOfDataNodes: {} , numberOfNodes: {} .",clusterName, numberOfDataNodes, numberOfNodes);
           for (ClusterIndexHealth health : healths.getIndices().values()) {
               String index = health.getIndex();
               int numberOfShards = health.getNumberOfShards();
               int numberOfReplicas = health.getNumberOfReplicas();
               ClusterHealthStatus status = health.getStatus();
               LOGGER.info("index: {} ,numberOfShards: {} ,numberOfReplicas: {} ,status {} .",index, numberOfShards, numberOfReplicas, status);
           }
       } catch (Exception e) {
            LOGGER.error("es cluster error",e);
       }
       return healths;
   }



}
