package com.pzp.manage.service;

import com.pzp.manage.bean.UserInfo;
import com.pzp.manage.dao.UserInfoMapper;
import com.pzp.manage.util.UserInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Autowired
    private UserInfoMapper userInfoMapper;

    boolean flag = true;

    @Transactional(rollbackFor=Exception.class)
    public UserInfo updateUserInfoById(Integer id, UserInfo user) throws Exception {
        userInfoMapper.updateUserInfoById(id, user);
        //int temp = 1/0;
        if(flag){
            flag = !flag;
            throw new Exception();
        }
        userInfoMapper.addUserInfo(user);

//        int temp = userInfoMapper.countUserInfo();
//        List<UserInfo> userInfoList = UserInfoUtil.getUserInfo();
//        for (int i=0; i<userInfoList.size(); i++) {
//            UserInfo userInfo = userInfoList.get(i);
//            if (userInfo.getId().equals(id)) {
//                userInfoList.remove(i);
//                userInfoList.add(i, user);
//                userInfoList.remove(100);
//                return userInfo;
//            }
//        }
        return null;
    }

    public UserInfo addUserInfo(UserInfo user){
        userInfoMapper.addUserInfo(user);
        return null;
    }

}
