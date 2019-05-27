package com.futuretongfu.model.entity;

import com.futuretongfu.bean.onlinegoods.GoodsDataBean;
import com.futuretongfu.bean.onlinegoods.HomeSortBean;

import java.util.List;

/**
 * Created by ChenXiaoPeng on 2017/7/5.
 */

public class HomeRecommendGoodsResult {

    private List<GoodsDataBean> list;
    private int total;

    public List<GoodsDataBean> getList() {
        return list;
    }

    public void setList(List<GoodsDataBean> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
