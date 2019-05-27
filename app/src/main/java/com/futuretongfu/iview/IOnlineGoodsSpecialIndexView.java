package com.futuretongfu.iview;

import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.entity.OnlineGoodsSpecialDetailsResult;


/**
 * 类:   IOnlineGoodsView
 * 描述:  线上商品
 * 作者： zhanggf on 2018/5/22
 */
public interface IOnlineGoodsSpecialIndexView {
    //获取商品详情成功
    void onGoodsDetailsSpecialSuccess(OnlineGoodsSpecialDetailsResult result);
    void onGoodsDetailsSpecialFaile(String msg);
    //收藏成功
    void onCollectSuccess(int type, FuturePayApiResult futurePayApiResult);
    void onCollectFail(String msg);


}
