package com.futuretongfu.iview;

import com.futuretongfu.bean.onlinegoods.GoodsBrandBean;
import com.futuretongfu.bean.onlinegoods.GoodsDataBean;
import com.futuretongfu.bean.onlinegoods.GoodsSearchDataBean;
import com.futuretongfu.bean.onlinegoods.GoodsSearchTermBean;
import com.futuretongfu.bean.onlinegoods.HomeSortBean;

import java.util.List;

/**
 * Created by zhanggf on 2018/5/18.
 */

public interface IOnlineSearchGoodsView {
    //获取品牌列表成功
    void onSearchBrandListViewSuccess(List<GoodsBrandBean> data);

    void onSearchBrandListViewFaile(String msg);

    //获取搜索条件列表成功
    void onSearchTermListViewSuccess(List<GoodsSearchTermBean> data);

    void onSearchTermListViewFaile(String msg);
    //搜索商品分类
    void onSearchGoodsListViewDnUpdateSuccess(List<GoodsSearchDataBean> data);
    void onSearchGoodsListViewDnUpdateFaile(String msg);

    public void onSearchGoodsListDnUpdateUpLoadSuccess(List<GoodsSearchDataBean> datas);
    public void onSearchGoodsListDnUpdateUpLoadFaile(String msg);
    public void onSearchGoodsListUpLoadNoDatas();

}
