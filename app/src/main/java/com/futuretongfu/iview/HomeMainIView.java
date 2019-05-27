package com.futuretongfu.iview;

import com.futuretongfu.base.BaseSerializable;
import com.futuretongfu.bean.HomeBannerBean;
import com.futuretongfu.bean.onlinegoods.GoodsDataBean;
import com.futuretongfu.bean.onlinegoods.HomeNoticeTipBean;
import com.futuretongfu.bean.onlinegoods.HomeSortBean;
import com.futuretongfu.bean.onlinegoods.RecommendDataBean;
import com.futuretongfu.model.entity.HomeRecommendGoodsResult;

import java.util.List;

/**
 * 交易列表
 *
 * @author DoneYang 2017/6/29
 */

public interface HomeMainIView extends IView {

    //首页banner
    void onBannerListFail(int code, String msg);
    void onBannerListSuccess(List<HomeBannerBean> data);

    //首页分类
    void onSortListFail(int code, String msg);
    void onSortListSuccess(List<HomeSortBean> data);

    //小贴士
    void onNoticeTipListFail(int code, String msg);
    void onNoticeTipListSuccess(List<HomeNoticeTipBean> data);

    //首页推荐
    void onRecomendListFail(int code, String msg);
    void onRecomendListSuccess(HomeRecommendGoodsResult data);

    public void onRecomendListDnUpdateUpLoadSuccess(HomeRecommendGoodsResult datas);
    public void onRecomendListDnUpdateUpLoadFaile(String msg);
    public void onRecomendListUpLoadNoDatas();

    //消息
    void onSystemMessageNumFail(int code, String msg);
    void onSystemMessageNumSuccess(BaseSerializable data);
    void onBaseUrlFail(int code, String msg);
    void onBaseUrlSuccess(String data);

}
