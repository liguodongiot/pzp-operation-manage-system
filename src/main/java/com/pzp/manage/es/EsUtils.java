package com.pzp.manage.es;

import com.alibaba.fastjson.JSONObject;
import com.carrotsearch.hppc.cursors.ObjectObjectCursor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesResponse;
import org.elasticsearch.action.admin.indices.alias.exists.AliasesExistResponse;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.get.GetFieldMappingsResponse;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.admin.indices.refresh.RefreshResponse;
import org.elasticsearch.action.admin.indices.settings.get.GetSettingsResponse;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.compress.CompressedXContent;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.DeleteByQueryRequestBuilder;
import org.elasticsearch.script.ScriptService;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.script.mustache.SearchTemplateRequestBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.NodeNotConnectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

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


    /**
     * 更新副本数量
     * @param esParam
     */
    public static void updateIndicesSettings(EsParam esParam){
        UpdateSettingsResponse response = esParam.getClient().admin().indices().prepareUpdateSettings(esParam.getName())
                .setSettings(Settings.builder()
                        .put("index.number_of_replicas", esParam.getReplicas())
                )
                .get();
        LOGGER.info("更新索引settings副本数{}。", response.isAcknowledged()?"成功":"失败");
    }

    /**
     * 获取索引设置（分片数，副本数）
     * @param esParam
     */
    public static void getIndicesSettings(EsParam esParam){
        GetSettingsResponse response = esParam.getClient().admin().indices()
                .prepareGetSettings(esParam.getName())
                .get();
        for (ObjectObjectCursor<String, Settings> cursor : response.getIndexToSettings()) {
            String index = cursor.key;
            Settings settings = cursor.value;
            Integer shards = settings.getAsInt("index.number_of_shards", null);
            Integer replicas = settings.getAsInt("index.number_of_replicas", null);
            LOGGER.info("获取索引settings。index:{}, shards:{}, replicas:{}.", index, shards, replicas);
        }
    }

    /**
     * 获取mappings信息
     * @param esParam
     */
    public static void getIndicesMappings(EsParam esParam){
        GetMappingsResponse response = esParam.getClient().admin().indices()
                .prepareGetMappings(esParam.getName())
                .get();
        ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> indexMap =  response.getMappings();
        ImmutableOpenMap<String, MappingMetaData> fieldIndexMap = indexMap.get(esParam.getName());
        MappingMetaData mappingMetaData = fieldIndexMap.get(esParam.getType());
        try {
            CompressedXContent source = mappingMetaData.source();
            LOGGER.info("Mappings info:[\n{}\n].", source.toString());
            Map<String, Object> sourceAsMap = mappingMetaData.getSourceAsMap();
            LOGGER.info("Mappings info:[\n{}\n].",JSONObject.toJSON(sourceAsMap));
        } catch (IOException e) {
            LOGGER.error("metadata to map error. EsParam:{}.", JSONObject.toJSON(esParam),e);
        }
    }

    /**
     * 热切换索引别名
     * @param client
     */
    public static void replaceIndicesAliase(TransportClient client, String removeIndex, String addIndex, String aliases){
        IndicesAliasesResponse response = client.admin().indices()
                .prepareAliases()
                .removeAlias(removeIndex, aliases)
                .addAlias(addIndex, aliases)
                .execute().actionGet();
        LOGGER.info("热切换索引别名{}。", response.isAcknowledged()?"成功":"失败");

    }

    /**
     * 移除索引别名
     * @param client
     * @param indices
     * @param aliases
     */
    public static void removeIndicesAliases(TransportClient client, String indices, String aliases){
        IndicesAliasesResponse response = client.admin().indices()
                .prepareAliases()
                .removeAlias(indices, aliases)
                .execute().actionGet();
        LOGGER.info("移除索引别名{}。", response.isAcknowledged()?"成功":"失败");
    }

    /**
     * 增加索引别名
     * @param client
     * @param indices
     * @param aliases
     */
    public static void addIndicesAliases(TransportClient client, String indices, String aliases){
        IndicesAliasesResponse response = client.admin().indices()
                .prepareAliases()
                .addAlias(indices, aliases)
                .execute().actionGet();
        LOGGER.info("增加索引别名{}。", response.isAcknowledged()?"成功":"失败");
    }


    /**
     * 判断索引是否存在
     * @param client
     * @param indices
     */
    public static void isAliasesExist(TransportClient client, String indices){
        AliasesExistResponse response = client.admin().indices()
                .prepareAliasesExist(indices)
                .execute().actionGet();
        boolean exists = response.isExists();
        LOGGER.info("索引别名{}{}。", indices,exists?"存在":"不存在");
    }

    /**
     * 获取索引别名信息
     * @param client
     * @param indices
     */
    public static void getAliases(TransportClient client, String indices){
        GetAliasesResponse response = client.admin().indices()
                .prepareGetAliases(indices)
                .execute().actionGet();
        ImmutableOpenMap<String, List<AliasMetaData>> aliases = response.getAliases();

        for (Iterator<String> it = aliases.keysIt(); it.hasNext(); ) {
            String index = it.next();
            List<AliasMetaData> aliasMetaData = aliases.get(index);
            LOGGER.info("索引别名信息:{}，Index:{}.", JSONObject.toJSON(aliasMetaData), index);
        }

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

    public static List<Map<String, Object>> queryDocumentByUids(TransportClient client,
                                                                String name,
                                                                String type,
                                                                String[] ids) {
        SearchResponse response = client.prepareSearch(name).setTypes(type)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.idsQuery().types(type).addIds(ids))
                .setFrom(0)
                .setSize(ids.length)
                .execute()
                .actionGet();

        SearchHits hits = response.getHits();
        List<Map<String, Object>> mapList = new ArrayList<>();
        if (null != hits && hits.getTotalHits() > 0) {
            for (SearchHit searchHit : hits) {
                Map<String, Object> source = searchHit.getSource();
                mapList.add(source);
            }

        } else {
            LOGGER.info("没有查询到任何内容！");
        }
        return mapList;
    }


    public static Map<String, Object> getDocumentById(TransportClient client,String index, String type, String id){
        GetResponse response = client.prepareGet(index, type, id)
                .execute()
                .actionGet();
        return response.getSource();
    }

    public static <T> T getDocumentById(TransportClient client,
                                        String index,
                                        String type,
                                        String id,
                                        Class<T> clazz){
        GetResponse response = client.prepareGet(index, type, id)
                .execute()
                .actionGet();
        String sourceAsString = response.getSourceAsString();
        return JSONObject.parseObject(sourceAsString, clazz);
    }



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
        try {
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
        } catch (Exception e){
            LOGGER.info(e.getMessage());
            LOGGER.error("es cluster error",e);
        }
        return null;
    }





    /**
     * 滚动查询
     */
    public static void pageScroll(EsSearchParam esSearchParam,
                                  QueryBuilder query,
                                  QueryBuilder postFilter,
                                  Integer size,
                                  TimeValue timeValue
                                  ){
        Date begin = new Date();
        SearchRequestBuilder searchRequestBuilder = esSearchParam.getClient()
                .prepareSearch(esSearchParam.getName())
                .setSearchType(SearchType.QUERY_THEN_FETCH);
        if(query != null){
            searchRequestBuilder.setQuery(query);
        }
        if(postFilter != null){
            searchRequestBuilder.setPostFilter(postFilter);
        }

        if(size != null){
            searchRequestBuilder.setSize(size);
        }
        if(timeValue != null){
            searchRequestBuilder.setScroll(timeValue);
        }
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

        long count = searchResponse.getHits().getTotalHits();

        int length = searchResponse.getHits().getHits().length;

        int total = 1;
        for(int i=0, sum=length; sum<count; i++){
            searchResponse = esSearchParam.getClient().prepareSearchScroll(searchResponse.getScrollId())
                    .setScroll(TimeValue.timeValueMinutes(8))
                    .execute().actionGet();
            sum += searchResponse.getHits().getHits().length;
            total += 1;
        }

        Date end = new Date();
        LOGGER.info("总耗时: "+(end.getTime()-begin.getTime()));
        LOGGER.info("平均每次耗时："+(end.getTime()-begin.getTime())/total);

    }


    public static void pageFromSize(EsSearchParam esSearchParam,
                                  QueryBuilder query,
                                  QueryBuilder postFilter,
                                  TimeValue timeValue){
        Date begin = new Date();
        SearchRequestBuilder searchRequestBuilder = esSearchParam.getClient()
                .prepareSearch(esSearchParam.getName())
                .setSearchType(SearchType.QUERY_THEN_FETCH);
        if(query != null){
            searchRequestBuilder.setQuery(query);
        }
        if(postFilter != null){
            searchRequestBuilder.setPostFilter(postFilter);
        }
        if(esSearchParam.getFrom()!= null){
            searchRequestBuilder.setFrom(esSearchParam.getFrom());
        }
        if(esSearchParam.getSize() != null){
            searchRequestBuilder.setSize(esSearchParam.getSize());
        }
        if(timeValue != null){
            searchRequestBuilder.setTimeout(timeValue);
        }
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        int length = searchResponse.getHits().getHits().length;
        LOGGER.info("length: {} .", length);

    }



}
