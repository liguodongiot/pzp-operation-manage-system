package com.pzp.manage.setting;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.bean</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/1/19 15:06 星期五
 */

public class BaseIndexSettings {

    private String name;
    private String alias;
    private String type;
    private String field;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return "BaseIndexSettings{" +
                "name='" + name + '\'' +
                ", alias='" + alias + '\'' +
                ", type='" + type + '\'' +
                ", field='" + field + '\'' +
                '}';
    }
}
