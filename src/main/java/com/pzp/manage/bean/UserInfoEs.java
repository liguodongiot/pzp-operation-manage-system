package com.pzp.manage.bean;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

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
@Data
public class UserInfoEs extends BaseIntegerEs implements Serializable{

    private String name;

    private String motto;

    private Integer age;

    private String sex;

    public UserInfoEs(Integer id, String name, String motto, Integer age, String sex) {
        super(id);
        this.name = name;
        this.motto = motto;
        this.age = age;
        this.sex = sex;
    }
}
