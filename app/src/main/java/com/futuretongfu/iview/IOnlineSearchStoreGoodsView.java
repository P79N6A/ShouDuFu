package com.futuretongfu.iview;

import com.futuretongfu.bean.onlinegoods.GoodsSearchDataBean;

import java.util.List;

/**
 * Created by zhanggf on 2018/5/18.
 */

public interface IOnlineSearchStoreGoodsView {
    //搜索商品分类
    void onSearchGoodsListViewDnUpdateSuccess(List<GoodsSearchDataBean> data);
    void onSearchGoodsListViewDnUpdateFaile(String msg);

    public void onSearchGoodsListDnUpdateUpLoadSuccess(List<GoodsSearchDataBean> datas);
    public void onSearchGoodsListDnUpdateUpLoadFaile(String msg);
    public void onSearchGoodsListUpLoadNoDatas();

}
