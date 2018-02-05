package com.pzp.manage.bean;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.bean</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/2/5 15:15 星期一
 */
public class ChatContext implements Serializable {

    private static final long serialVersionUID = -1281905252452715041L;

    private Deque<UserInfo> userInfoDeque = new ArrayDeque<>(20);

    public Deque<UserInfo> getUserInfoDeque() {
        return userInfoDeque;
    }

    public void setUserInfoDeque(Deque<UserInfo> userInfoDeque) {
        this.userInfoDeque = userInfoDeque;
    }
}
