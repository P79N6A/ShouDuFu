package com.futuretongfu.iview;

import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.entity.OnlineGoodsDetailsResult;


/**
 * 类:   IOnlineGoodsView
 * 描述:  线上商品
 * 作者： zhanggf on 2018/5/22
 */
public interface IOnlineGoodsIndexView {
    //获取商品详情成功
    void onGoodsDetailsSuccess(OnlineGoodsDetailsResult result);
    void onGoodsDetailsFaile(String msg);

}
