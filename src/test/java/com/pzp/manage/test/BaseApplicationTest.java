package com.pzp.manage.test;

import com.pzp.manage.PzpManageApplication;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.test</p>
 * <p>Title: 基础测试，避免重复启动springboot消耗资源</p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/2/2 14:02 星期五
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=PzpManageApplication.class)
@WebAppConfiguration
public abstract class BaseApplicationTest {
    protected final static Logger LOGGER  = LoggerFactory.getLogger(BaseApplicationTest.class);
}
