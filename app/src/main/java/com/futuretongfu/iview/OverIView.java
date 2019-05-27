package com.futuretongfu.iview;


import com.futuretongfu.bean.HomeBannerBean;
import com.futuretongfu.model.entity.HomeRecommendGoodsResult;

import java.util.List;

public interface OverIView extends IView {

    //专区banner
    void onBannerListFail(int code, String msg);
    void onBannerListSuccess(List<HomeBannerBean> data);


    //首页专区
    void onRecomendListFail(int code, String msg);
    void onRecomendListSuccess(HomeRecommendGoodsResult data);

    public void onRecomendListDnUpdateUpLoadSuccess(HomeRecommendGoodsResult datas);
    public void onRecomendListDnUpdateUpLoadFaile(String msg);
    public void onRecomendListUpLoadNoDatas();

}
