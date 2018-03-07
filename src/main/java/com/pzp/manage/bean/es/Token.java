package com.pzp.manage.bean.es;

import com.alibaba.fastjson.JSONObject;

/**
 * <p>Project: pzp-operation-manage-system</p>
 * <p>Package: com.pzp.manage.bean</p>
 * <p>Title: </p>
 * <p>Description: </p>
 *
 * @author guodong.li
 * @version 1.0.0
 * @date 2018/3/6 19:31 星期二
 */
public class Token {

    private String token;

    private Integer startOffset;

    private Integer endOffset;

    private String type;

    private Integer position;

    public Token(){
    }


    public Token(String token, Integer startOffset, Integer endOffset, String type, Integer position) {
        this.token = token;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.type = type;
        this.position = position;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getStartOffset() {
        return startOffset;
    }

    public void setStartOffset(Integer startOffset) {
        this.startOffset = startOffset;
    }

    public Integer getEndOffset() {
        return endOffset;
    }

    public void setEndOffset(Integer endOffset) {
        this.endOffset = endOffset;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
