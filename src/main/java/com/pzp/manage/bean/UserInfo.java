package com.pzp.manage.bean;

import com.alibaba.fastjson.JSONObject;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.bean</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/1/15 19:24 星期一
 */
public class UserInfo {

    private Integer id;
    private String name;
    private String motto;
    private Integer age;
    private String sex;

    public UserInfo() {
    }

    public UserInfo(Integer id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public UserInfo(Integer id, String name, String motto, Integer age) {
        this.id = id;
        this.name = name;
        this.motto = motto;
        this.age = age;
    }

    public UserInfo(Integer id, String name, String motto, Integer age, String sex) {
        this.id = id;
        this.name = name;
        this.motto = motto;
        this.age = age;
        this.sex = sex;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
