package com.pzp.manage.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.bean</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/4/28 21:09 星期六
 */
@Data
public class BaseIntegerEs {
    private Integer id;

    public BaseIntegerEs(Integer id) {
        this.id = id;
    }
}
