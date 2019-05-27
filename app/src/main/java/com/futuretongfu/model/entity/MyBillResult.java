package com.futuretongfu.model.entity;

import java.util.List;

/**
 * Created by ChenXiaoPeng on 2017/7/12.
 */

public class MyBillResult {

    private int totalNum;//当前月份订单总数
    private List<MyBillEntity> list;

    public MyBillResult(){

    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public List<MyBillEntity> getList() {
        return list;
    }

    public void setList(List<MyBillEntity> list) {
        this.list = list;
    }
}
