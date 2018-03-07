package com.pzp.manage.bean.es;

import lombok.Data;

import java.util.List;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.test</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/3/6 20:47 星期二
 */
@Data
public class EsHits {

    public long total;

    private float maxScore;

    List<EsHit> hits;



}
