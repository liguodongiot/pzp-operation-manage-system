package com.pzp.manage.setting;

import com.alibaba.fastjson.JSONObject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.setting</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/2/9 11:05 星期五
 */
@Component
@ConfigurationProperties(prefix = "es.userManage")
public class UserManageIndexSettings extends BaseIndexSettings{

    private String nameVa;
    private String nameVb;

    public String getNameVa() {
        return nameVa;
    }

    public void setNameVa(String nameVa) {
        this.nameVa = nameVa;
    }

    public String getNameVb() {
        return nameVb;
    }

    public void setNameVb(String nameVb) {
        this.nameVb = nameVb;
    }

    @Override
    public String toString() {
        return "UserManageIndexSettings:" + JSONObject.toJSONString(this);
    }
}
