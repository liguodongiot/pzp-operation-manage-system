package com.pzp.manage.bean;

import java.io.Serializable;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.bean</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/3/6 21:05 星期二
 */
public class User implements Serializable {

    private static final long serialVersionUID = -329066647199569031L;

    private String userName;

    private String orderNo;

    public User() {
    }

    public User(String userName, String orderNo) {
        this.userName = userName;
        this.orderNo = orderNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
