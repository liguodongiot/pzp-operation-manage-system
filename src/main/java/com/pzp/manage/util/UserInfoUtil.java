package com.pzp.manage.util;

import com.pzp.manage.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.util</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/1/15 19:27 星期一
 */
public class UserInfoUtil {

    private static List<UserInfo> userInfoList = new ArrayList<UserInfo>();


   static {
        userInfoList.add(new UserInfo(1,"小明", 13));
        userInfoList.add(new UserInfo(2,"小kk", 15));
        userInfoList.add(new UserInfo(3,"小yy", 14));
        userInfoList.add(new UserInfo(4,"小xx", 13));
    }

    public static List<UserInfo> getUserInfo(){
        return userInfoList;
    }


}
