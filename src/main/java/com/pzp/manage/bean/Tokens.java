package com.pzp.manage.bean;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

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
public class Tokens {

    private List<Token> tokens;

    public Tokens(){
    }

    public Tokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
