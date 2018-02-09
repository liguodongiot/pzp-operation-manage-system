package com.pzp.manage.es;

/**
 * Describe:
 * author: guodong.li
 * datetime: 2017/6/22 17:20
 */
public class DocumentParam<T> {

    private String index;
    private String type;
    private String id;
    private T source;

    public DocumentParam(String index, String type, String id, T source) {
        this.index = index;
        this.type = type;
        this.id = id;
        this.source = source;
    }


    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public T getSource() {
        return source;
    }

    public void setSource(T source) {
        this.source = source;
    }
}
