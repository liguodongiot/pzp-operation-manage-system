package com.pzp.manage.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.bean</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/4/27 12:53 星期五
 */
@Data
public class UserBo {
    private Long id;

    private String name;

    @JsonProperty("address_info")
    private String addressInfo;

    private String sex;
}
