package com.pzp.manage.test;

import com.pzp.manage.bean.UserInfo;
import com.pzp.manage.es.DocumentParam;
import com.pzp.manage.es.EsContext;
import com.pzp.manage.es.EsParam;
import com.pzp.manage.es.EsUtils;
import com.pzp.manage.setting.UserInfoIndexSettings;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

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
        EsUtils.createIndexLib(esParam);
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


    @Test
    public void testBulkIndexDocument(){
        EsParam esParam = new EsParam.ParamBuilder(esContext.getClient(),
                settings.getName(),settings.getType(),settings.getField())
                .setAlias(settings.getAlias())
                .setShards(3)
                .setReplicas(1)
                .build();

        UserInfo userInfo1 = new UserInfo(2,"李国冬",23);
        UserInfo userInfo2 = new UserInfo(3,"胡景涛",43);
        UserInfo userInfo3 = new UserInfo(4,"张飞",24);
        UserInfo userInfo4 = new UserInfo(5,"刘飞",26);
        UserInfo userInfo5 = new UserInfo(6,"李元芳",23);

        List<DocumentParam<UserInfo>> documentParamList = new ArrayList<>();
        DocumentParam<UserInfo> document1 = new DocumentParam<>(settings.getName(),settings.getType(),
                Integer.toString(userInfo1.getId()),userInfo1);
        DocumentParam<UserInfo> document2 = new DocumentParam<>(settings.getName(),settings.getType(),
                Integer.toString(userInfo2.getId()),userInfo2);
        DocumentParam<UserInfo> document3 = new DocumentParam<>(settings.getName(),settings.getType(),
                Integer.toString(userInfo3.getId()),userInfo3);
        DocumentParam<UserInfo> document4 = new DocumentParam<>(settings.getName(),settings.getType(),
                Integer.toString(userInfo4.getId()),userInfo4);
        DocumentParam<UserInfo> document5 = new DocumentParam<>(settings.getName(),settings.getType(),
                Integer.toString(userInfo5.getId()),userInfo5);

        documentParamList.add(document1);
        documentParamList.add(document2);
        documentParamList.add(document3);
        documentParamList.add(document4);
        documentParamList.add(document5);

        EsUtils.bulkIndexDocument(esParam.getClient(), documentParamList);
    }



}
