package com.futuretongfu.iview;

import com.futuretongfu.model.entity.FuturePayApiResult;

/**
 * Created by zhanggf on 2018/6/2.
 */

public interface IGoodsRefundView {
    public void onGoodsRefundAddSuccess(FuturePayApiResult futurePayApiResult);
    public void onGoodsRefundAddFaile(String msg);
    public void onOrderConsumerCommentPercentage(float percentage);

}
