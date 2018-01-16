package com.pzp.manage.controller;

import com.pzp.manage.bean.UserInfo;
import com.pzp.manage.service.UserInfoService;
import com.pzp.manage.util.UserInfoUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.beans.Transient;
import java.util.List;
import java.util.Map;

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

    @Autowired
    UserInfoService userInfoService;

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
     * 查询
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


    /**
     * 删除
     * http://localhost:8080/userInfo/delById/1
     * @param id
     * @return
     */
    @DeleteMapping(value = "/delById/{id}")
    public UserInfo delUserInfoById(@PathVariable("id") Integer id) {
        List<UserInfo> userInfoList = UserInfoUtil.getUserInfo();
        for (UserInfo userInfo : userInfoList) {
            if (userInfo.getId().equals(id)) {
                userInfoList.remove(userInfo);
                return userInfo;
            }
        }
        return null;
    }


    /**
     * 更新
     * http://localhost:8080/userInfo/updateById/1
     * @param id
     * @param map
     * @return
     */
    @PutMapping(value = "/updateById/{id}")
    public UserInfo updateUserInfoById(@PathVariable Integer id, @RequestBody Map<String,Object> map) {
        UserInfo user = new UserInfo();
        BeanMap beanMap = BeanMap.create(user);
        beanMap.putAll(map);
        return userInfoService.updateUserInfoById(id, user);
    }



    /**
     * 新增
     * http://localhost:8080/userInfo/addUserInfo
     * @param user
     * @return
     */
    @PostMapping(value = "/addUserInfo")
    public UserInfo addUserInfo(@RequestBody UserInfo user) {
        List<UserInfo> userInfoList = UserInfoUtil.getUserInfo();
        userInfoList.add(user);
        return user;
    }



}
