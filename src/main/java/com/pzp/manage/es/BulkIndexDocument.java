package com.pzp.manage.es;

import com.pzp.manage.bean.BaseObjectEs;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.es</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/4/28 20:58 星期六
 */
@Data
@AllArgsConstructor
public class BulkIndexDocument<T extends BaseObjectEs> {

    private String index;
    private String type;
    private List<T> source;

}
