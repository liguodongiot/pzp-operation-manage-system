package com.pzp.manage.service;

import com.pzp.manage.bean.UserInfo;
import com.pzp.manage.util.UserInfoUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.service</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/1/16 18:12 星期二
 */
@Service
public class UserInfoService {

    //@Transactional(transactionManager = "txManager",readOnly = false, propagation = Propagation.REQUIRED, rollbackFor=Exception.class)
    public UserInfo updateUserInfoById(Integer id, UserInfo user){
        List<UserInfo> userInfoList = UserInfoUtil.getUserInfo();
        for (int i=0; i<userInfoList.size(); i++) {
            UserInfo userInfo = userInfoList.get(i);
            if (userInfo.getId().equals(id)) {
                userInfoList.remove(i);
                userInfoList.add(i, user);
                //userInfoList.remove(100);
                return userInfo;
            }
        }
        return null;
    }


}
