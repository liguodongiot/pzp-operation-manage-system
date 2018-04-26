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
 * @date 2018/4/26 11:26 星期四
 */
@Data
public class EsNodeInfo {

    private String ip;

    private Integer tcpPort;

    private Integer httpPort;
}
