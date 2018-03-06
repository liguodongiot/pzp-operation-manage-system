package com.pzp.manage.test;

import com.alibaba.fastjson.JSONObject;
import com.pzp.manage.bean.UserInfo;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.test</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/2/2 10:28 星期五
 */
public class UserInfoTests {

    private final static Logger LOGGER  = LoggerFactory.getLogger(UserInfoTests.class);

    @Test
    public void testBeanToJson(){
        UserInfo userInfo = new UserInfo();
        userInfo.setId(12);
        userInfo.setName("dsaf");
        userInfo.setAge(32);

        Object result = JSONObject.toJSON(userInfo);
        LOGGER.info(result.toString());

    }

    @Test
    public void test(){
        List<UserInfo> userInfoList = null;
        if(Objects.nonNull(userInfoList)) {
            for (UserInfo userInfo : userInfoList) {
                LOGGER.info(userInfo.toString());
            }
        }



    }


}
