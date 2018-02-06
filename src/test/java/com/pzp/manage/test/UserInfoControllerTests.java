package com.pzp.manage.test;

import com.alibaba.fastjson.JSONObject;
import com.pzp.manage.PzpManageApplication;
import com.pzp.manage.bean.UserInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.test</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/2/2 13:46 星期五
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=PzpManageApplication.class)
@WebAppConfiguration
public class UserInfoControllerTests {

    private final static Logger LOGGER  = LoggerFactory.getLogger(UserInfoControllerTests.class);

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * POST请求
     */
    @Test
    public void testAddUserInfo() throws Exception {
        UserInfo askInfo = new UserInfo();
        askInfo.setAge(18);
        askInfo.setName("开头语");
        askInfo.setId(324);

        String askInfoJson = JSONObject.toJSONString(askInfo);
        LOGGER.info(askInfoJson);
        String uri = "/userInfo/addUserInfo";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .content(askInfoJson)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();
        LOGGER.info("result: [{}]...", content);
        Assert.assertEquals("错误，正确的返回值为200",200,status);

    }

}
