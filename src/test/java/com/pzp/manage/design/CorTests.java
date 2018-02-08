package com.pzp.manage.design;

import com.pzp.manage.design.cor.ChainOfResponsibilityClient;
import com.pzp.manage.design.cor.CustomInterceptor;
import com.pzp.manage.design.cor.LeaveRequest;
import com.pzp.manage.design.cor.Result;
import org.junit.Test;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.design</p>
 * <p>Title: 职责链  请假</p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/2/8 16:20 星期四
 */
public class CorTests {


    @Test
    public void test1(){
        LeaveRequest request = new LeaveRequest.RequestBuilder("张三","事假", 5).build();
        ChainOfResponsibilityClient client = new ChainOfResponsibilityClient();
        Result result = client.execute(request);

        System.out.println("结果：" + result.toString());
    }


    @Test
    public void test2(){
        LeaveRequest request = new LeaveRequest.RequestBuilder("张三","事假", 5).build();
        ChainOfResponsibilityClient client = new ChainOfResponsibilityClient();
        client.addRatifys(new CustomInterceptor());

        Result result = client.execute(request);

        System.out.println("结果：" + result.toString());
    }

}
