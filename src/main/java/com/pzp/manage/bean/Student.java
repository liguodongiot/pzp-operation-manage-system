package com.pzp.manage.bean;

import lombok.Data;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.bean</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/5/11 17:43 星期五
 */
@Data
public class Student {

    private String name;

    private String sex;

    private String age;

    private String school;

    private String className;

    public Student(String name, String sex, String age, String school, String className) {
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.school = school;
        this.className = className;
    }
}
