package com.pzp.manage.bean.es;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.bean.es</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/3/7 14:18 星期三
 */
@Data
public class EsErrorInfo {

    private List<RootCause> rootCause;

    private String type;

    private String reason;

    private String indexUuid;

    private String index;
/**********************************************
    ## JSONField的作用对象:
    1. Field
    2. Setter 和 Getter方法
    注：FastJson在进行操作时，是根据getter和setter的方法进行的，并不是依据Field进行。

    1、作用在Field
    JSONField作用在Field时，其name不仅定义了输入key的名称，同时也定义了输出的名称。

    2、作用在setter和getter方法上
    顾名思义，当作用在setter方法上时，就相当于根据 name 到 json中寻找对应的值，并调用该setter对象赋值。
    当作用在getter上时，在bean转换为json时，其key值为name定义的值。
    @link https://www.cnblogs.com/softidea/p/5681928.html
**********************************************/
    @JSONField(name="resource.type")
    private String resourceType;

    @JSONField(name="resource.id")
    private String resourceId;

}
