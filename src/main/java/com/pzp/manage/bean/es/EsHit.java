package com.pzp.manage.bean.es;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.elasticsearch.common.text.Text;

import java.util.Map;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.bean</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/3/6 20:48 星期二
 */

@Data
public class EsHit {

    private String index;

    private String id;

    private String type;

    private Float score;

    private String source;

    public Map<String, Object> getSourceAsMap(){
        return JSONObject.parseObject(source);
    }

    public <T> T getSourceAsBean(Class<T> clazz){
        return JSONObject.parseObject(source, clazz);
    }


}
