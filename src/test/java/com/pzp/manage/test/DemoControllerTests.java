package com.pzp.manage.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pzp.manage.app.PzpManageApplication;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.test</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/1/18 19:36 星期四
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=PzpManageApplication.class)
@WebAppConfiguration
@Transactional
public class DemoControllerTests {

    private final static Logger LOGGER  = LoggerFactory.getLogger(DemoControllerTests.class);

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp(){
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * /demo/hello
     */
    @Test
    public void testHello() throws Exception {
        String uri = "/demo/hello";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .param("name","liguodong")
                .accept(MediaType.APPLICATION_JSON)).andReturn();
        int status = result.getResponse().getStatus();
        String content = result.getResponse().getContentAsString();
        LOGGER.info("result: [{}]...", content);
        Assert.assertEquals("错误，正确的返回值为200",200,status);
        Assert.assertEquals("错误，返回值与期望值不一致。","hello: liguodong", content);
    }


}
