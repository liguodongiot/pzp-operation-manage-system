package com.pzp.manage.dao;

import com.pzp.manage.bean.UserInfo;
import org.apache.ibatis.annotations.Param;

/**
 * Describe:
 *
 * @author: liguodong
 * @datetime: 2018/1/16 22:18
 */
public interface UserInfoMapper {

    int countUserInfo();

    int updateUserInfoById(@Param("id") Integer id,
                           @Param("userInfo") UserInfo userInfo);

    int addUserInfo(UserInfo userInfo);

}
