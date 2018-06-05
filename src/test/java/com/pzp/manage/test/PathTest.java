package com.pzp.manage.test;

import org.junit.Test;

/**
 * <p>Project: pzp-all</p>
 * <p>Package: com.pzp.manage.test</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/5/30
 */

public class PathTest {

    @Test
    public void test(){
        //这个方法可以获取当前运行程序的完整路径,绝对的路径
        System.out.println(System.getProperty("java.class.path"));
        System.out.println(System.getProperty("user.dir"));
        System.out.println(this.getClass().getResource("/").getPath());
    }


}
