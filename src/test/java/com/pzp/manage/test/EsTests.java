package com.pzp.manage.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pzp.manage.bean.UserInfo;
import com.pzp.manage.bean.UserInfoEs;
import com.pzp.manage.bean.es.Tokens;
import com.pzp.manage.es.*;
import com.pzp.manage.setting.UserInfoIndexSettings;
import com.pzp.manage.util.HttpUtil;
import org.apache.commons.io.IOUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        EsUtils.bulkProcessorIndexDocument(esParam.getClient(), documentParamList);
        EsUtils.bulkIndexDocument(esParam.getClient(), documentParamList);
    }


    @Test
    public void testUpdate(){
        EsParam esParam = new EsParam.ParamBuilder(esContext.getClient(),
                settings.getName(),settings.getType(),settings.getField())
                .setAlias(settings.getAlias())
                .setShards(3)
                .setReplicas(1)
                .build();
        UserInfo userInfo = new UserInfo();
        userInfo.setId(4);
        userInfo.setName("国");
        userInfo.setAge(12);
        DocumentParam<UserInfo> document = new DocumentParam<>(esParam.getName(),esParam.getType(),
                Integer.toString(userInfo.getId()),userInfo);
        EsUtils.updateDocumentById(esParam.getClient(), document);
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


    @Test
    public void testSearchTemplateFile() throws IOException {

        EsSearchParam esSearchParam = new EsSearchParam.ParamBuilder(esContext.getClient(),
                settings.getName(),settings.getType())
                .setAlias(settings.getAlias())
                .build();

        Map<String, Object> templateParams = new HashMap<>();
        templateParams.put("param_name", "刘飞");

        LOGGER.info("-------------------");
        LOGGER.info(this.getClass().getResource("/").getPath());
        LOGGER.info(this.getClass().getResource("").getPath());
        File directory = new File("");// 参数为空
        String courseFile = directory.getCanonicalPath();
        LOGGER.info(courseFile);
        URL xmlpath = this.getClass().getClassLoader().getResource("");
        LOGGER.info(xmlpath.toString());
        LOGGER.info(System.getProperty("user.dir"));
        LOGGER.info(System.getProperty("java.class.path"));
        LOGGER.info("-------------------");


        //"src/main/resources/"
        File file = new File(this.getClass().getResource("/").getPath(),"/config/scripts/template_gender.json");
        LOGGER.info("File: {}",file.getAbsolutePath());

        //jar包中的文件只能通过InputStream读取，不能通过File
        InputStream inputStream = this.getClass().getResourceAsStream("/config/scripts/template_gender.json");

        String templateGender;
        try {
            //templateGender = FileUtils.readFileToString(file, "UTF-8");

            templateGender = IOUtils.toString(inputStream,"UTF-8");
        } catch (IOException e) {
            LOGGER.error("IO error. ",e);
            return;
        }
        SearchHits searchHits = EsUtils.searchTemplate(esSearchParam, templateParams, templateGender);
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
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(QueryBuilders.matchQuery("sex", "male"))
                .must(QueryBuilders.matchQuery("name", "张飞"));
        EsUtils.deleteDocumentByMatchQuery(esContext.getClient(),settings.getAlias(),
                boolQueryBuilder,true);
        Thread.sleep(3000);
    }


    @Test
    public void testGetBulkDocument(){
        String[] ids = {"6","6","5","3","5"};
        List<Map<String, Object>> mapList = EsUtils.queryDocumentByUids(esContext.getClient(), settings.getName(), settings.getType(), ids);
        System.out.println(mapList);
    }

    @Test
    public void testGetDocumentById(){
        String[] ids = {"6","6","5","3","5"};
        for (int i = 0; i < ids.length; i++) {
            Map<String, Object>  stringObjectMap = EsUtils.getDocumentById(esContext.getClient(), settings.getName(), settings.getType(), ids[i]);
            System.out.println(stringObjectMap);
        }

    }

    @Test
    public void testGetDocumentById2(){
        String[] ids = {"6","6","5","3","5"};
        for (int i = 0; i < ids.length; i++) {
            UserInfo userInfo = EsUtils.getDocumentById(esContext.getClient(), settings.getName(), settings.getType(), ids[i], UserInfo.class);
            System.out.println(userInfo);
        }

    }


    @Test
    public void testBulk(){

        UserInfoEs userInfo1 = new UserInfoEs(2,"李国冬","滴答滴答滴滴答",23,"male");
        UserInfoEs userInfo2 = new UserInfoEs(3,"胡景涛","小酒窝长睫毛",43,"female");
        UserInfoEs userInfo3 = new UserInfoEs(4,"张飞","关公面前耍大刀",24,"female");
        UserInfoEs userInfo4 = new UserInfoEs(5,"刘飞","我到底是谁呀，我不知道",26,"female");
        UserInfoEs userInfo5 = new UserInfoEs(6,"李元芳","东方红，红太阳，希望在天上",23,"female");
        UserInfoEs userInfo6 = new UserInfoEs(7,"张飞","关公面前耍小刀",23,"female");
        UserInfoEs userInfo7 = new UserInfoEs(8,"张飞","上山打老虎",25,"male");
        UserInfoEs userInfo8 = new UserInfoEs(9,"张飞","青蜂侠爱游泳",66,"male");
        UserInfoEs userInfo9 = new UserInfoEs(10,"李元芳","东方红，红太阳，希望在天上",33,"male");
        UserInfoEs userInfo10 = new UserInfoEs(11,"李元芳","希望在天上,青青草原",23,"male");
        List<UserInfoEs> userInfoList = new ArrayList<>();
        userInfoList.add(userInfo1);
        userInfoList.add(userInfo2);
        userInfoList.add(userInfo3);
        userInfoList.add(userInfo4);
        userInfoList.add(userInfo5);
        userInfoList.add(userInfo6);
        userInfoList.add(userInfo7);
        userInfoList.add(userInfo8);
        userInfoList.add(userInfo9);
        userInfoList.add(userInfo10);
        EsParam esParam = new EsParam.ParamBuilder(esContext.getClient(),
                settings.getName(),settings.getType(),settings.getField())
                .setAlias(settings.getAlias())
                .setShards(3)
                .setReplicas(1)
                .build();
        BulkIndexDocument<UserInfoEs> bulkIndexDocument = new BulkIndexDocument<>(esParam.getName(), esParam.getType(),userInfoList);
        EsUtils.bulkProcessorIndexDocument(esParam.getClient(), bulkIndexDocument);

    }


}
