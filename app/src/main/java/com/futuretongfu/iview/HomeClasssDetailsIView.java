package com.futuretongfu.iview;

import com.futuretongfu.base.BaseSerializable;
import com.futuretongfu.bean.HomeBannerBean;
import com.futuretongfu.bean.onlinegoods.HomeSortBean;
import com.futuretongfu.model.entity.HomeRecommendGoodsResult;

import java.util.List;

/**
 * 交易列表
 *
 * @author DoneYang 2017/6/29
 */

public interface HomeClasssDetailsIView extends IView {

    void onRecomendListFail(int code, String msg);
    void onRecomendListSuccess(HomeRecommendGoodsResult data);

    void onRecomendListDnUpdateUpLoadSuccess(HomeRecommendGoodsResult datas);
    void onRecomendListDnUpdateUpLoadFaile(String msg);
    void onRecomendListUpLoadNoDatas();

    //首页banner
    void onBannerListFail(int code, String msg);
    void onBannerListSuccess(List<HomeBannerBean> data);
}
