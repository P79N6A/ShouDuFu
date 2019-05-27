package com.futuretongfu.bean.onlinegoods;


import java.util.List;

/**
 * Created by zhanggf on 2018/5/18.
 * 订单
 */

public class OrderOnlineResult {
    private int total;
    private List<OrderOnlineBean> list;

    public List<OrderOnlineBean> getList() {
        return list;
    }

    public void setList(List<OrderOnlineBean> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
