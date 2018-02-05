package com.pzp.manage.test;

import com.alibaba.fastjson.JSONObject;
import com.pzp.manage.bean.ChatContext;
import com.pzp.manage.bean.UserInfo;
import org.junit.Test;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.test</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/2/5 15:18 星期一
 */
public class ChatContextTests {

    @Test
    public void testJsonObject(){
        ChatContext chatContext = new ChatContext();
        UserInfo userInfo = new UserInfo();
        userInfo.setName("李国冬");
        Deque<UserInfo> userInfoDeque = new ArrayDeque<>(20);
        userInfoDeque.addLast(userInfo);
        chatContext.setUserInfoDeque(userInfoDeque);

        Object obj = JSONObject.toJSON(chatContext);
        System.out.println(obj);
        ChatContext chatContext2 = JSONObject.parseObject(obj.toString(),ChatContext.class);
        System.out.println(JSONObject.toJSON(chatContext2));

    }

}
