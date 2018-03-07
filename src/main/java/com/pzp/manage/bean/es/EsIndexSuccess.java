package com.pzp.manage.bean.es;

import lombok.Data;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.bean.es</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/3/7 11:42 星期三
 */
@Data
public class EsIndexSuccess {

/**********************************************
    {
        "acknowledged" : true,
            "shards_acknowledged" : true
    }

**********************************************/

    private boolean acknowledged;

    private boolean shardsAcknowledged;


}
