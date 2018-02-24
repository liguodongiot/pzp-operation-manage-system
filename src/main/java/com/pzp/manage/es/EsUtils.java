package com.pzp.manage.es;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.admin.indices.refresh.RefreshResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.DeleteByQueryRequestBuilder;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequestBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.es</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/2/9 11:31 星期五
 */
public final class EsUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(EsUtils.class);

    private static final Integer BULK_SIZE = 2000;


    public static void createIndexLib(EsParam esParam, Boolean isUseAlias){
        LOGGER.info("EsParam:{}...", JSONObject.toJSONString(esParam));
        IndicesAdminClient indicesAdminClient = esParam.getClient().admin().indices();

        CreateIndexRequestBuilder createIndexRequestBuilder = indicesAdminClient.prepareCreate(esParam.getName());
        //是否创建索引别名
        if(isUseAlias && StringUtils.isNotBlank(esParam.getAlias())){
            String aliasName = String.format("{\"%s\":{}}",esParam.getAlias());
            LOGGER.info("aliasName:{}...", aliasName);
            createIndexRequestBuilder.setAliases(aliasName);
        }

        createIndexRequestBuilder.setSettings(Settings.builder()
                .put("index.number_of_shards", esParam.getShards())
                .put("index.number_of_replicas", esParam.getReplicas()));

        createIndexRequestBuilder.addMapping(esParam.getType(),esParam.getField(), XContentType.JSON);

        CreateIndexResponse response = createIndexRequestBuilder.get();

        TransportAddress transportAddress = response.remoteAddress();
        LOGGER.info("createIndexLib: host:{},address:{},port:{},indexName:{} is created...",
                transportAddress.getHost(),
                transportAddress.getAddress(),
                transportAddress.getPort(),
                esParam.getName());
    }


    public static void deleteIndexLib(EsParam esParam) {
        if(isExistsIndexLib(esParam)){
            DeleteIndexResponse deleteResponse = esParam.getClient().admin().indices().prepareDelete(esParam.getName())
                    .setTimeout(TimeValue.timeValueSeconds(1))
                    .get();
            TransportAddress transportAddress = deleteResponse.remoteAddress();
            LOGGER.info("deleteIndexLib: host:{},address:{},port:{},indexName:{} is deleted...",
                    transportAddress.getHost(),
                    transportAddress.getAddress(),
                    transportAddress.getPort(),
                    esParam.getName());
        }
    }

    public static boolean isExistsIndexLib(EsParam esParam) {
        IndicesExistsResponse response = esParam.getClient().admin().indices().prepareExists(esParam.getName()).get();
        return response.isExists();
    }

    public static void refreshIndexLib(EsParam esParam){
        RefreshResponse response = esParam.getClient().admin().indices()
                .prepareRefresh(esParam.getName())
                .get();
        LOGGER.info("refreshIndex: [{}],totalShards:[{}],successfulShards:[{}],failedShards:[{}].",
                esParam.getName(),
                response.getTotalShards(),
                response.getSuccessfulShards(),
                response.getFailedShards());
    }


    public static <T> void indexDocument(TransportClient client,DocumentParam<T> document){
        ObjectMapper objectMapper = new ObjectMapper();

        String source = null;
        try {
            source = objectMapper.writeValueAsString(document.getSource());
        } catch (JsonProcessingException e) {
            LOGGER.error("解析JSON异常，文档：{}... ", JSONObject.toJSONString(document), e);
        }

        IndexResponse response = null;
        try {
            response = client.prepareIndex(document.getIndex(),
                document.getType(), document.getId())
                .setSource(source, XContentType.JSON)
                .execute()
                .actionGet();

            LOGGER.info("创建索引：{}，文档ID：[{}]结束。。。", response.getId(), response.getIndex());
        } catch (Exception e) {
            LOGGER.error("索引文档到ES索引库出错,索引：{},类型：{},文档ID：{}。。。",
                    document.getIndex(), document.getType(), document.getId(), e);
        }

    }


    /**
     * 批量索引
     * @param client
     * @param documentList
     */
    public static <T> void bulkIndexDocument(TransportClient client, List<DocumentParam<T>> documentList){
        ObjectMapper objectMapper = new ObjectMapper();

        if (documentList == null || documentList.isEmpty()) {
        	return;
        }

        BulkRequestBuilder bulkRequest = client.prepareBulk();
        BulkResponse bulkItemResponses = null;

        for (int i = 0; i < documentList.size(); i++) {
            DocumentParam<T> document = documentList.get(i);
            String source = null;
            try {
                source = objectMapper.writeValueAsString(document.getSource());
            } catch (JsonProcessingException e) {
                LOGGER.error("解析JSON异常... ", e);
            }
            bulkRequest.add(client.prepareIndex(document.getIndex(), document.getType(), document.getId())
                    .setSource(source, XContentType.JSON)
            );
            if( (i+1) % BULK_SIZE == 0){
                bulkItemResponses = bulkRequest.execute().actionGet();
                if(bulkItemResponses !=null && bulkItemResponses.hasFailures()){
                    String errorInfo = bulkItemResponses.buildFailureMessage();
                    LOGGER.error("索引文档到ES索引库出错：{}...", errorInfo);
                }
            }
        }

        bulkItemResponses = bulkRequest.execute().actionGet();
        if(bulkItemResponses !=null && bulkItemResponses.hasFailures()){
            String errorInfo = bulkItemResponses.buildFailureMessage();
            LOGGER.error("索引文档到ES索引库出错：{}...", errorInfo);
        }
    }


//    /**
//     * 根据UID（type#id）删除多个文档
//     * @param client
//     * @param name
//     * @param type
//     * @param id
//     */
//    public static void deleteDocumentByUids(TransportClient client, String name,String type, String[] id) {
//        SearchResponse response = client.prepareSearch(name)
//                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
//                .setQuery(QueryBuilders.idsQuery().types(type).addIds(id))
//                .setFrom(0)
//                .setSize(id.length)
//                .execute()
//                .actionGet();
//
//        SearchHits hits = response.getHits();
//
//        if (null != hits && hits.getTotalHits() > 0) {
//            for (SearchHit searchHit : hits) {
//                String indexName = searchHit.getIndex();
//                String typeName = searchHit.getType();
//                String docId = searchHit.getId();
//                DeleteResponse deleteResponse = client.prepareDelete(indexName,typeName,docId).get();
//                if(deleteResponse==null || deleteResponse.getShardInfo().getFailed() > 0){
//                    LOGGER.error("删除ES索引数据有误，result:{}。。。", docId);
//                }
//            }
//        } else {
//            LOGGER.info("没有查询到任何内容！");
//        }
//
//    }


    /**
     * 根据id删除文档
     * @param client
     * @param index
     * @param type
     * @param id
     */
    public static void deleteDocument(TransportClient client, String index, String type, String id){
        DeleteResponse deleteResponse = client.prepareDelete(index, type, id).get();
        if(deleteResponse==null || deleteResponse.getShardInfo().getFailed() > 0){
            LOGGER.error("删除ES索引数据有误，index:{}, type:{}, id{}。", index, type, id);
        }
        LOGGER.info("删除ES索引数据分片信息：shardInfo:{}", deleteResponse.getShardInfo());
    }


    /**
     * 根据查询删除文档
     * @param client
     * @param index
     * @param name
     * @param text
     */
    public static void deleteDocumentByMatchQuery(TransportClient client,
                                             final String index,
                                             final String name,
                                             final Object text,
                                             boolean asynchronously){
        DeleteByQueryRequestBuilder deleteByQueryRequestBuilder =  DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
                .filter(QueryBuilders.matchQuery(name, text))
                .source(index);
        if(asynchronously){
            //异步
            deleteByQueryRequestBuilder.execute(new ActionListener<BulkByScrollResponse>() {
                @Override
                public void onResponse(BulkByScrollResponse response) {
                    long deleted = response.getDeleted();
                    LOGGER.info("通过查询删除文档数：{}。", deleted);
                }
                @Override
                public void onFailure(Exception e) {
                    LOGGER.error("通过查询删除文档异常： index：{},  name：{}, text：{}。", index, name, text, e);
                }
            });
        } else {
            //同步
            BulkByScrollResponse response = deleteByQueryRequestBuilder.get();
            long deleted = response.getDeleted();
            LOGGER.info("通过查询删除文档数：{}。", deleted);
        }
    }


    public static SearchHits searchTemplate(EsSearchParam esSearchParam, Map<String, Object> templateParams,String templateScript){

        SearchRequest searchRequest = new SearchRequest();
        if(esSearchParam.getAlias()!=null) {
            searchRequest.indices(esSearchParam.getAlias());
        } else {
            searchRequest.indices(esSearchParam.getName());
        }
        searchRequest.types(esSearchParam.getType())
                .searchType(SearchType.DFS_QUERY_THEN_FETCH);

        SearchResponse searchResponse = new SearchTemplateRequestBuilder(esSearchParam.getClient())
                .setScript(templateScript)
                .setScriptType(ScriptType.INLINE)
                .setScriptParams(templateParams)
                .setRequest(searchRequest)
                .get()
                .getResponse();
        SearchHits searchHits = searchResponse.getHits();
        LOGGER.info("searchHits:{}.", searchHits);

        return searchHits;
    }


}
