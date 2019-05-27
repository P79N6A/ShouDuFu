package com.futuretongfu.presenter.home;

import android.content.Context;

import com.futuretongfu.bean.HomeBannerBean;
import com.futuretongfu.bean.SystemMessageBean;
import com.futuretongfu.bean.onlinegoods.HomeNoticeTipBean;
import com.futuretongfu.bean.onlinegoods.HomeSortBean;
import com.futuretongfu.iview.HomeMainIView;
import com.futuretongfu.model.entity.HomeRecommendGoodsResult;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.HomeRepository;
import com.futuretongfu.presenter.Presenter;

import java.util.List;

/**
 * @author DoneYang 2017/6/29
 */

public class HomeMainPresenter extends Presenter {

    private HomeRepository homeRepository;
    private HomeMainIView homeIView;
    private int page;

    public HomeMainPresenter(Context context, HomeMainIView homeIView) {
        super(context);
        this.homeRepository = new HomeRepository();
        this.homeIView = homeIView;
    }

    @Override
    public void onDestroy(){
        if(homeRepository != null) homeRepository.cancel();
    }

    /**
     * 系统消息数量
     *
     * @param userId
     */
    public void onSystemMessageNum(String userId) {
        homeRepository.onSystemMessageNum(userId, new BaseRepository.HttpCallListener<SystemMessageBean>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (homeIView != null) {
                    homeIView.onSystemMessageNumFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(SystemMessageBean data) {
                if (homeIView != null) {
                    homeIView.onSystemMessageNumSuccess(data);
                }
            }
        });
    }


    /**
     * 首页banner
     */
    public void onBannerList(int region) {
        homeRepository.onHomeShopBanner(region,new BaseRepository.HttpCallListener<List<HomeBannerBean>>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (homeIView != null) {
                    homeIView.onBannerListFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(List<HomeBannerBean> data) {
                if (homeIView != null) {
                    homeIView.onBannerListSuccess(data);
                }
            }
        });
    }


    public void onBaseUrl() {
        homeRepository.onBaseUrl(new BaseRepository.HttpCallListener<String>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (homeIView != null) {
                    homeIView.onBaseUrlFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(String data) {
                if (homeIView != null) {
                    homeIView.onBaseUrlSuccess(data);
                }
            }
        });
    }


    /**
     * 首页分类
     */
    public void onSortList(String parentId) {
        homeRepository.onHomeSort(parentId,new BaseRepository.HttpCallListener<List<HomeSortBean>>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (homeIView != null) {
                    homeIView.onSortListFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(List<HomeSortBean> data) {
                if (homeIView != null) {
                    homeIView.onSortListSuccess(data);
                }
            }
        });
    }

    /**
     * 公告
     */
    public void onNoticeTipList() {
        homeRepository.onNoticeTipList(new BaseRepository.HttpCallListener<List<HomeNoticeTipBean>>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (homeIView != null) {
                    homeIView.onNoticeTipListFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(List<HomeNoticeTipBean> data) {
                if (homeIView != null) {
                    homeIView.onNoticeTipListSuccess(data);
                }
            }
        });
    }


    /**
     * 首页推荐
     */
    public void onRecomendList() {
        page = 1;
        homeRepository.onHomeRecomend(page,new BaseRepository.HttpCallListener<HomeRecommendGoodsResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (homeIView != null) {
                    homeIView.onRecomendListFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(HomeRecommendGoodsResult data) {
                if (homeIView != null) {
                    homeIView.onRecomendListSuccess(data);
                }
            }
        });
    }

    /**
     * 首页推荐
     */
    public void onRecomendListUpLoad() {
        page++;
        homeRepository.onHomeRecomend(page,new BaseRepository.HttpCallListener<HomeRecommendGoodsResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (homeIView != null) {
                    homeIView.onRecomendListDnUpdateUpLoadFaile(msg);
                }
            }

            @Override
            public void onHttpCallSuccess(HomeRecommendGoodsResult data) {
                if(homeIView != null) {
                    if(data == null || data.getList().size() < 1) {
                        homeIView.onRecomendListUpLoadNoDatas();
                    }
                    else{
                        homeIView.onRecomendListDnUpdateUpLoadSuccess(data);
                    }
                }
            }
        });
    }


}
