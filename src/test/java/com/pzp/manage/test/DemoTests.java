package com.pzp.manage.test;

import com.pzp.manage.bean.UserBo;
import com.pzp.manage.bean.UserVo;
import org.junit.Test;
import org.springframework.beans.BeanUtils;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.test</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/4/27 12:58 星期五
 */
public class DemoTests {

    @Test
    public void test(){
        UserVo userVo = new UserVo();
        userVo.setId(100L);
        userVo.setName("lidd");
        userVo.setAddressInfo("dsagdsa");
        userVo.setAge(10);
        UserBo userBo = new UserBo();
        BeanUtils.copyProperties(userVo,userBo);
        System.out.println(userBo.toString());
    }

}
