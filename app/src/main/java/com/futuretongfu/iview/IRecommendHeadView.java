package com.futuretongfu.iview;


import com.futuretongfu.model.entity.RecommendHeadInfo;

/**
 * 类:  IRecommendView
 * 描述:  我的推荐View
 * 作者： weiang on 2017/6/29
 */
public interface IRecommendHeadView extends IView {

    //获取推荐奖励信息成功
    public void onRecommendHeadSuccess(RecommendHeadInfo recommendHeadInfo);

    //获取推荐奖励信息失败
    public void onRecommendHeadFaile(String msg);

}
