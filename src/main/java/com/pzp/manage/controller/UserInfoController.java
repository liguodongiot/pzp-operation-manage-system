package com.pzp.manage.controller;

import com.pzp.manage.bean.UserInfo;
import com.pzp.manage.util.UserInfoUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.controller</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/1/15 13:44 星期一
 */
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {

    /**
     * http://localhost:8080/userInfo/list
     * @return
     */
    @GetMapping(value = "/list")
    public List<UserInfo> listUserInfo() {
        return UserInfoUtil.getUserInfo();
    }

    /**
     * http://localhost:8080/userInfo/get?name=小明
     * @param name
     * @return
     */
    @GetMapping(value = "/get")
    public UserInfo getUserInfo(@RequestParam("name") String name) {
        List<UserInfo> userInfoList = UserInfoUtil.getUserInfo();
        for (UserInfo userInfo : userInfoList) {
            if (userInfo.getName().equals(name)) {
                return userInfo;
            }
        }
        return null;
    }


    /**
     * http://localhost:8080/userInfo/getById/1
     * @param id
     * @return
     */
    @GetMapping(value = "/getById/{id}")
    public UserInfo getUserInfoById(@PathVariable("id") Integer id) {
        List<UserInfo> userInfoList = UserInfoUtil.getUserInfo();
        for (UserInfo userInfo : userInfoList) {
            if (userInfo.getId().equals(id)) {
                return userInfo;
            }
        }
        return null;
    }








}
