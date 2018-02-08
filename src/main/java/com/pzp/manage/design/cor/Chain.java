package com.pzp.manage.design.cor;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.design.cor</p>
 * <p>Title: 对request和Result封装，用来转发</p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/2/8 16:01 星期四
 */
public interface Chain {

    /**
     * 获取当前request
     */
    LeaveRequest request();

    /**
     * 转发request
     * @param request
     * @return
     */
    Result proceed(LeaveRequest request);

}
