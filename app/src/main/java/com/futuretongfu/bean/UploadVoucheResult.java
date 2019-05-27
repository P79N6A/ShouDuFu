package com.futuretongfu.bean;



import java.util.List;

/**
 * Created by zhanggf on 2018/5/18.
 * 订单
 */

public class UploadVoucheResult {
    private int total;
    private List<UploadVoucheBean> list;

    public List<UploadVoucheBean> getList() {
        return list;
    }

    public void setList(List<UploadVoucheBean> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
