package com.futuretongfu.bean;


import java.io.Serializable;
import java.util.List;

public class BankListBean implements Serializable {

    public String code ="";
    public String msg = "";

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

    private List<BankListItemBean> data;

    public List<BankListItemBean> getData() {
        return data;
    }

    public void setData(List<BankListItemBean> data) {
        this.data = data;
    }
}
