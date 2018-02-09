package com.pzp.manage.es;


import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.client.transport.TransportClient;

import java.io.Serializable;

/**
 * Describe:
 * author: guodong.li
 * datetime: 2017/6/21 20:50
 */
public class EsParam implements Serializable {

    private static final long serialVersionUID = 3806157555158678120L;

    /** ES通讯客户端 */
    private TransportClient client;
    /** 索引库 */
    private String name;
    /** 索引库别名 */
    private String alias;
    /** 类型 */
    private String type;
    /** 字段 */
    private String field;
    /** 分片数 */
    private Integer shards;
    /** 副本 */
    private Integer replicas;

    public TransportClient getClient() {
        return client;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    public String getType() {
        return type;
    }

    public String getField() {
        return field;
    }

    public Integer getShards() {
        return shards;
    }

    public Integer getReplicas() {
        return replicas;
    }


    public static class ParamBuilder {

        private TransportClient client;
        private String name;
        private String type;
        private String field;

        private String alias;
        private Integer shards = 3;
        private Integer replicas = 1;

        public ParamBuilder setAlias(String alias) {
            this.alias = alias;
            return this;
        }
        public ParamBuilder setShards(Integer shards) {
            this.shards = shards;
            return this;
        }
        public ParamBuilder setReplicas(Integer replicas) {
            this.replicas = replicas;
            return this;
        }

        public ParamBuilder(TransportClient client,
                            String name,String type, String field){
            this.client = client;
            this.name = name;
            this.type = type;
            this.field = field;
        }

        public EsParam build() {
            return new EsParam(this);
        }
    }

    private EsParam(ParamBuilder builder){
        this.client = builder.client;
        this.name = builder.name;
        this.type = builder.type;
        this.field = builder.field;
        this.alias = StringUtils.isBlank(builder.alias)?builder.name+"_alias":builder.alias;
        this.replicas = builder.replicas;
        this.shards = builder.shards;
    }

}
