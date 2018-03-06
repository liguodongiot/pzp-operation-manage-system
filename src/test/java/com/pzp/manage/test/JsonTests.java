package com.pzp.manage.test;

import com.pzp.manage.bean.EsResult;
import com.pzp.manage.bean.User;
import com.pzp.manage.util.JsonUtil;
import org.junit.Test;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.test</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/3/6 20:23 星期二
 */

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.cglib.beans.BeanMap;

import java.io.IOException;
import java.util.Map;

public class JsonTests {

    @Test
    public void testToUnderlineJSONString() throws JsonProcessingException {
        User user = new User("张三", "1111111");

        String json = JsonUtil.toUnderlineJSONString(user);
        System.out.println(json);

    }

    @Test
    public void testToSnakeObject() throws IOException {
        String json = "{\"user_name\":\"张三\",\"order_no\":\"1111111\"}";

        User user = JsonUtil.toSnakeObject(json, User.class);
        System.out.println(JSONObject.toJSONString(user));

    }

    @Test
    public void test(){
        String result = "{\n" +
                "  \"took\" : 1,\n" +
                "  \"timed_out\" : false,\n" +
                "  \"_shards\" : {\n" +
                "    \"total\" : 3,\n" +
                "    \"successful\" : 3,\n" +
                "    \"failed\" : 0\n" +
                "  },\n" +
                "  \"hits\" : {\n" +
                "    \"total\" : 1,\n" +
                "    \"max_score\" : 1.0754046,\n" +
                "    \"hits\" : [\n" +
                "      {\n" +
                "        \"_index\" : \"alibaba\",\n" +
                "        \"_type\" : \"employee\",\n" +
                "        \"_id\" : \"3\",\n" +
                "        \"_score\" : 1.0754046,\n" +
                "        \"_source\" : {\n" +
                "          \"user_name\" : \"詹姆斯\",\n" +
                "          \"order_no\" : \"11111\"\n" +
                "        }\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";

        EsResult esResult = JSONObject.parseObject(result, EsResult.class);
        String source = esResult.getHits().getHits().get(0).getSource();
        Map<String, Object> sourceAsMap = esResult.getHits().getHits().get(0).getSourceAsMap();
        User  user = esResult.getHits().getHits().get(0).getSourceAsBean(User.class);
        System.out.println(esResult.toString());
    }
}


