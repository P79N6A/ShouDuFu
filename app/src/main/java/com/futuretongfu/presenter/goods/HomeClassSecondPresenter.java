package com.futuretongfu.presenter.goods;

import android.content.Context;

import com.futuretongfu.bean.HomeBannerBean;
import com.futuretongfu.bean.onlinegoods.GoodsAttrValuesList;
import com.futuretongfu.iview.HomeClasssDetailsIView;
import com.futuretongfu.iview.HomeMainIView;
import com.futuretongfu.iview.IOnlineGoodsView;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.entity.HomeRecommendGoodsResult;
import com.futuretongfu.model.entity.OnlineGoodsDetailsResult;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.HomeRepository;
import com.futuretongfu.model.repository.OnlineGoodsRepository;
import com.futuretongfu.model.repository.StoreIndexRepository;
import com.futuretongfu.presenter.Presenter;

import java.util.List;


/**
 * Created by zhanggf on 2018/5/10.
 * 首页分类二级--线上
 */

public class HomeClassSecondPresenter extends Presenter {

    private HomeClasssDetailsIView iOnlineGoodsView;
    private HomeRepository homeRepository;
    private int page;


    public HomeClassSecondPresenter(Context context, HomeClasssDetailsIView iOnlineGoodsView) {
        super(context);
        this.iOnlineGoodsView = iOnlineGoodsView;
        homeRepository = new HomeRepository();
    }

    @Override
    public void onDestroy() {
        if (homeRepository != null)
            homeRepository.cancel();
    }


    /**
     * 首页分类二级
     */
    public void ononClassDetailsList(String flbh2) {
        page = 1;
        homeRepository.onClassDetails(flbh2,page,new BaseRepository.HttpCallListener<HomeRecommendGoodsResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iOnlineGoodsView != null) {
                    iOnlineGoodsView.onRecomendListFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(HomeRecommendGoodsResult data) {
                if (iOnlineGoodsView != null) {
                    iOnlineGoodsView.onRecomendListSuccess(data);
                }
            }
        });
    }

    /**
     * 首页分类二级
     */
    public void onClassDetailsUpLoad(String flbh2) {
        page++;
        homeRepository.onClassDetails(flbh2,page,new BaseRepository.HttpCallListener<HomeRecommendGoodsResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iOnlineGoodsView != null) {
                    iOnlineGoodsView.onRecomendListDnUpdateUpLoadFaile(msg);
                }
            }

            @Override
            public void onHttpCallSuccess(HomeRecommendGoodsResult data) {
                if(iOnlineGoodsView != null) {
                    if(data == null || data.getList().size() < 1) {
                        iOnlineGoodsView.onRecomendListUpLoadNoDatas();
                    }
                    else{
                        iOnlineGoodsView.onRecomendListDnUpdateUpLoadSuccess(data);
                    }
                }
            }
        });
    }

    /**
     *banner
     */
    public void onBannerList(int region) {
        homeRepository.onHomeShopBanner(region,new BaseRepository.HttpCallListener<List<HomeBannerBean>>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iOnlineGoodsView != null) {
                    iOnlineGoodsView.onBannerListFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(List<HomeBannerBean> data) {
                if (iOnlineGoodsView != null) {
                    iOnlineGoodsView.onBannerListSuccess(data);
                }
            }
        });
    }



}
