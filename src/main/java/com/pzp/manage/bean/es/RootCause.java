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
 * @date 2018/3/7 14:19 星期三
 */
@Data
public class RootCause {

    private String type;

    private String reason;

    private String indexUuid;

    private String index;


}
