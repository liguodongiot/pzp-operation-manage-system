package com.pzp.manage.service.chart;

import org.springframework.stereotype.Service;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.service.chart</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/1/19 15:02 星期五
 */
@Service
public class UnionChartService {

    public String getChart(String name){
        return "chart is "+name;
    }


}
