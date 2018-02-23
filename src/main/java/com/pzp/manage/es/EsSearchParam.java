package com.pzp.manage.es;


import org.elasticsearch.client.transport.TransportClient;

import java.io.Serializable;

/**
 * Describe: Es搜索参数
 * author: guodong.li
 * datetime: 2017/6/21 20:50
 */
public class EsSearchParam implements Serializable {

    private static final long serialVersionUID = 3806157555158678120L;

    /** ES通讯客户端 */
    private TransportClient client;
    /** 索引库 */
    private String name;
    /** 索引库别名 */
    private String alias;
    /** 类型 */
    private String type;
    /** 开始 */
    private Integer from;
    /** 结束 */
    private Integer size;

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

    public Integer getFrom() {
        return from;
    }

    public Integer getSize() {
        return size;
    }

    public static class ParamBuilder {

        private TransportClient client;
        private String name;
        private String type;

        private String alias;
        private Integer from = 0;
        private Integer size = 0;

        public ParamBuilder setAlias(String alias) {
            this.alias = alias;
            return this;
        }

        public ParamBuilder setFrom(Integer from) {
            this.from = from;
            return this;
        }

        public ParamBuilder setSize(Integer size) {
            this.size = size;
            return this;
        }

        public ParamBuilder(TransportClient client,
                            String name, String type){
            this.client = client;
            this.name = name;
            this.type = type;
        }

        public EsSearchParam build() {
            return new EsSearchParam(this);
        }
    }

    private EsSearchParam(ParamBuilder builder){
        this.client = builder.client;
        this.name = builder.name;
        this.type = builder.type;
        this.alias = builder.alias;
        this.from = builder.from;
        this.size = builder.size;
    }

}
