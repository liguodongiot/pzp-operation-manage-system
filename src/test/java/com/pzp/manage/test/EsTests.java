package com.pzp.manage.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pzp.manage.bean.Tokens;
import com.pzp.manage.bean.UserInfo;
import com.pzp.manage.es.*;
import com.pzp.manage.setting.UserInfoIndexSettings;
import com.pzp.manage.util.HttpUtil;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.test</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/2/9 16:01 星期五
 */

public class EsTests extends BaseApplicationTest{

    @Autowired
    private EsContext esContext;
    @Autowired
    private UserInfoIndexSettings settings;


    @Test
    public void testCreateIndexLib(){
        EsParam esParam = new EsParam.ParamBuilder(esContext.getClient(),
                settings.getName(),settings.getType(),settings.getField())
                .setAlias(settings.getAlias())
                .setShards(3)
                .setReplicas(1)
                .build();
        EsUtils.createIndexLib(esParam,true);
    }


    @Test
    public void testDeleteIndexLib(){
        EsParam esParam = new EsParam.ParamBuilder(esContext.getClient(),
                settings.getName(),settings.getType(),settings.getField())
                .setAlias(settings.getAlias())
                .setShards(3)
                .setReplicas(1)
                .build();
        EsUtils.deleteIndexLib(esParam);
    }

    @Test
    public void testRefreshIndexLib(){
        EsParam esParam = new EsParam.ParamBuilder(esContext.getClient(),
                settings.getName(),settings.getType(),settings.getField())
                .setAlias(settings.getAlias())
                .setShards(3)
                .setReplicas(1)
                .build();
        EsUtils.refreshIndexLib(esParam);
    }


    @Test
    public void testIndexDocument(){
        EsParam esParam = new EsParam.ParamBuilder(esContext.getClient(),
                settings.getName(),settings.getType(),settings.getField())
                .setAlias(settings.getAlias())
                .setShards(3)
                .setReplicas(1)
                .build();
        UserInfo userInfo = new UserInfo(1,"李国冬",23);
        DocumentParam<UserInfo> document = new DocumentParam<>(settings.getName(),settings.getType(),
                Integer.toString(userInfo.getId()),userInfo);
        EsUtils.indexDocument(esParam.getClient(),document);
    }


    /**
     * curl http://10.250.140.14:9200/user_info_v1/user_info/_search?pretty
     */
    @Test
    public void testBulkIndexDocument(){
        EsParam esParam = new EsParam.ParamBuilder(esContext.getClient(),
                settings.getName(),settings.getType(),settings.getField())
                .setAlias(settings.getAlias())
                .setShards(3)
                .setReplicas(1)
                .build();

        UserInfo userInfo1 = new UserInfo(2,"李国冬","滴答滴答滴滴答",23,"male");
        UserInfo userInfo2 = new UserInfo(3,"胡景涛","小酒窝长睫毛",43,"female");
        UserInfo userInfo3 = new UserInfo(4,"张飞","关公面前耍大刀",24,"female");
        UserInfo userInfo4 = new UserInfo(5,"刘飞","我到底是谁呀，我不知道",26,"female");
        UserInfo userInfo5 = new UserInfo(6,"李元芳","东方红，红太阳，希望在天上",23,"female");
        UserInfo userInfo6 = new UserInfo(7,"张飞","关公面前耍小刀",23,"female");
        UserInfo userInfo7 = new UserInfo(8,"张飞","上山打老虎",25,"male");
        UserInfo userInfo8 = new UserInfo(9,"张飞","青蜂侠爱游泳",66,"male");
        UserInfo userInfo9 = new UserInfo(10,"李元芳","东方红，红太阳，希望在天上",33,"male");
        UserInfo userInfo10 = new UserInfo(11,"李元芳","希望在天上,青青草原",23,"male");

        List<DocumentParam<UserInfo>> documentParamList = new ArrayList<>();
        DocumentParam<UserInfo> document1 = new DocumentParam<>(esParam.getName(),esParam.getType(),
                Integer.toString(userInfo1.getId()),userInfo1);
        DocumentParam<UserInfo> document2 = new DocumentParam<>(esParam.getName(),esParam.getType(),
                Integer.toString(userInfo2.getId()),userInfo2);
        DocumentParam<UserInfo> document3 = new DocumentParam<>(esParam.getName(),esParam.getType(),
                Integer.toString(userInfo3.getId()),userInfo3);
        DocumentParam<UserInfo> document4 = new DocumentParam<>(esParam.getName(),esParam.getType(),
                Integer.toString(userInfo4.getId()),userInfo4);
        DocumentParam<UserInfo> document5 = new DocumentParam<>(esParam.getName(),esParam.getType(),
                Integer.toString(userInfo5.getId()),userInfo5);
        DocumentParam<UserInfo> document6 = new DocumentParam<>(esParam.getName(),esParam.getType(),
                Integer.toString(userInfo6.getId()),userInfo6);
        DocumentParam<UserInfo> document7 = new DocumentParam<>(esParam.getName(),esParam.getType(),
                Integer.toString(userInfo7.getId()),userInfo7);
        DocumentParam<UserInfo> document8 = new DocumentParam<>(esParam.getName(),esParam.getType(),
                Integer.toString(userInfo8.getId()),userInfo8);
        DocumentParam<UserInfo> document9 = new DocumentParam<>(esParam.getName(),esParam.getType(),
                Integer.toString(userInfo9.getId()),userInfo9);
        DocumentParam<UserInfo> document10 = new DocumentParam<>(esParam.getName(),esParam.getType(),
                Integer.toString(userInfo10.getId()),userInfo10);
        documentParamList.add(document1);
        documentParamList.add(document2);
        documentParamList.add(document3);
        documentParamList.add(document4);
        documentParamList.add(document5);
        documentParamList.add(document6);
        documentParamList.add(document7);
        documentParamList.add(document8);
        documentParamList.add(document9);
        documentParamList.add(document10);

        EsUtils.bulkIndexDocument(esParam.getClient(), documentParamList);
    }

    @Test
    public void testSearchTemplate(){

        EsSearchParam esSearchParam = new EsSearchParam.ParamBuilder(esContext.getClient(),
                settings.getName(),settings.getType())
                .setAlias(settings.getAlias())
                .build();

        Map<String, Object> templateParams = new HashMap<>();
        templateParams.put("param_name", "张飞");

//        String templateScript = "{\n" +
//                "        \"query\" : {\n" +
//                "            \"match\" : {\n" +
//                "                \"name\" : \"{{param_name}}\"\n" +
//                "            }\n" +
//                "        }\n" +
//                "}";

        String templateScript = "{\n" +
                "\t\"query\" : {\n" +
                "\t\t\"match\" : {\n" +
                "\t\t\t\"name\" : \"{{param_name}}\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "    \"from\": 0, \n" +
                "    \"size\": 2\n" +
                "}";


        SearchHits searchHits = EsUtils.searchTemplate(esSearchParam, templateParams, templateScript);
        long total = searchHits.getTotalHits();
        SearchHit[]  searchHitArr = searchHits.getHits();
        LOGGER.info("total:{}", total);
        LOGGER.info("searchHitArr:{}", JSONObject.toJSON(searchHitArr));
    }

    ////////////////////////////////
    @Test
    public void testAnalyze(){
        String url = "http://10.250.140.14:9200/_analyze?pretty";
        String json = "{\n" +
                "  \"analyzer\": \"ik_max_word\",\n" +
                "  \"text\": \"中华人民共和国国歌\"\n" +
                "}";
        List<String> list = new ArrayList<>();
        String result = HttpUtil.doPostJson(url, json);
        JSONObject parse = JSONObject.parseObject(result);
        JSONArray tokenArr = parse.getJSONArray("tokens");
        for (int i = 0; i < tokenArr.size(); i++) {
            JSONObject jsonObject = tokenArr.getJSONObject(i);
            String token = jsonObject.get("token").toString();
            list.add(token);
        }

        LOGGER.info("List:{}" ,list.toString());
    }

    @Test
    public void parseToken(){
        String url = "http://10.250.140.14:9200/_analyze?pretty";
        String json = "{\n" +
                "  \"analyzer\": \"ik_max_word\",\n" +
                "  \"text\": \"中华人民共和国国歌\"\n" +
                "}";

        String result = HttpUtil.doPostJson(url, json);
        Tokens tokens = JSONObject.parseObject(result,Tokens.class);

        LOGGER.info("List:{}" ,tokens.toString());
    }


    ////////////////////////////////
    @Test
    public void testMatch(){
        String url = "http://10.250.140.14:9200/alibaba_alias/employee/_search?pretty";
        String json = "{\n" +
                "    \"query\" : {\n" +
                "        \"match\" : {\n" +
                "            \"last_name\" : \"哈登\"\n" +
                "        }\n" +
                "    }\n" +
                "}";
        List<String> list = new ArrayList<>();
        String result = HttpUtil.doPostJson(url, json);
        JSONObject parse = JSONObject.parseObject(result);
        JSONArray tokenArr = parse.getJSONArray("tokens");
        for (int i = 0; i < tokenArr.size(); i++) {
            JSONObject jsonObject = tokenArr.getJSONObject(i);
            String token = jsonObject.get("token").toString();
            list.add(token);
        }

        LOGGER.info("List:{}" ,list.toString());
    }


    @Test
    public void testDeleteDocument(){
        EsUtils.deleteDocument(esContext.getClient(),settings.getAlias(),settings.getType(),"2");
    }

    @Test
    public void testDeleteDocumentByMatchQuery() throws InterruptedException {
        EsUtils.deleteDocumentByMatchQuery(esContext.getClient(),settings.getAlias(),"name","张飞",true);
        Thread.sleep(3000);
    }


}
