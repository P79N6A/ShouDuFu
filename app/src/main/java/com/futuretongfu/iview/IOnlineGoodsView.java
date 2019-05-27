package com.futuretongfu.iview;

import com.futuretongfu.bean.onlinegoods.GoodsAttrValuesList;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.entity.OnlineGoodsDetailsResult;

import java.util.List;

/**
 * 类:   IOnlineGoodsView
 * 描述:  线上商品
 * 作者： zhanggf on 2018/5/22
 */
public interface IOnlineGoodsView {
    //获取商品详情成功
    void onGoodsDetailsSuccess(OnlineGoodsDetailsResult result);
    void onGoodsDetailsFaile(String msg);

    //收藏成功
    void onCollectSuccess(int type,FuturePayApiResult futurePayApiResult);
    void onCollectFail(String msg);

    //获取商品属性成功
    void onGoodsAtterInfoSuccess(int type,List<GoodsAttrValuesList> result);
    void onGoodsAtterInfoFaile(String msg);

    //获取商品属性成功
    void onGoodsAtterInfoSearchSuccess(String formatInfo,List<OnlineGoodsDetailsResult> result);
    void onGoodsAtterInfoSearchFaile(String msg);

    //添加购物车成功
    void onOnlineShoppingAddSuccess(FuturePayApiResult futurePayApiResult);
    void onOnlineShoppingAddFaile(String msg);

}
