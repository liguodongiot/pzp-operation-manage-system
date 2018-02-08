package com.pzp.manage.design.cor;

/**
 * <p>Project: bigdata-frames</p>
 * <p>Package: com.lgd.base.design.cor</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/2/8 14:10 星期四
 */
public class Manager implements Ratify {

    @Override
    public Result deal(Chain chain) {
        LeaveRequest request = chain.request();
        System.out.println("Manager=====>request:" + request.toString());
        if (request.getDays() > 3) {
            // 构建新的Request
            LeaveRequest newRequest = new LeaveRequest.RequestBuilder(request)
                    .setManagerInfo(request.getName() + "每月的KPI考核还不错，可以批准")
                    .build();
            return chain.proceed(newRequest);

        }
        return new Result(true, "Manager：早点把事情办完，项目离不开你");
    }

}
