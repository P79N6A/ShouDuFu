package com.futuretongfu.iview;


import com.futuretongfu.model.entity.RecommendListInfo;

/**
 * 类:  IRecommendView
 * 描述:  我的推荐View
 * 作者： weiang on 2017/6/29
 */
public interface IRecommendListView extends IView {

    //获取推荐列表信息成功
    public void onRecommendListSuccess(RecommendListInfo dataBean);

    //获取推荐列表信息失败
    public void onRecommendListFaile(String msg);

}
