package com.futuretongfu.model.entity;

import java.io.Serializable;

/**
 * 类: FuturePayApiResult
 * 描述: 请求返回数据基础模型
 * 作者： weiang on 2017/6/25
 */
public class FuturePayApiResult<T> implements Serializable {

    private String code = "";
    private String msg = "";
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }



}
