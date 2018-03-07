package com.pzp.manage.bean.es;

import lombok.Data;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.bean</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/3/6 21:24 星期二
 */
@Data
public class Shards {

    private int total;

    private int successful;

    private int failed;


}
