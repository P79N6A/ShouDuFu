package com.futuretongfu.presenter;

import android.content.Context;

import com.futuretongfu.bean.HomeBannerBean;
import com.futuretongfu.iview.OverIView;
import com.futuretongfu.model.entity.HomeRecommendGoodsResult;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.HomeRepository;

import java.util.List;

public class OverPresenter extends Presenter {
    private HomeRepository homeRepository;
    OverIView overIView;

    private int page;

    public OverPresenter(Context context, OverIView overIView) {
        super(context);
        this.homeRepository = new HomeRepository();
        this.overIView = overIView;
    }

    @Override
    public void onDestroy() {
        if (homeRepository != null) homeRepository.cancel();
    }
    /**
     * 首页banner
     */
    public void onBannerList(int region) {
        homeRepository.onOverShopBanner(region, new BaseRepository.HttpCallListener<List<HomeBannerBean>>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (overIView != null) {
                    overIView.onBannerListFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(List<HomeBannerBean> data) {
                if (overIView != null) {
                    overIView.onBannerListSuccess(data);
                }
            }
        });
    }

    /**
     * 首页推荐
     */
    public void onRecomendList() {
        page = 1;
        homeRepository.onOverRecomend(page,new BaseRepository.HttpCallListener<HomeRecommendGoodsResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (overIView != null) {
                    overIView.onRecomendListFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(HomeRecommendGoodsResult data) {
                if (overIView != null) {
                    overIView.onRecomendListSuccess(data);
                }
            }
        });
    }

    /**
     * 首页推荐
     */
    public void onRecomendListUpLoad() {
        page++;
        homeRepository.onOverRecomend(page,new BaseRepository.HttpCallListener<HomeRecommendGoodsResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (overIView != null) {
                    overIView.onRecomendListDnUpdateUpLoadFaile(msg);
                }
            }

            @Override
            public void onHttpCallSuccess(HomeRecommendGoodsResult data) {
                if(overIView != null) {
                    if(data == null || data.getList().size() < 1) {
                        overIView.onRecomendListUpLoadNoDatas();
                    }
                    else{
                        overIView.onRecomendListDnUpdateUpLoadSuccess(data);
                    }
                }
            }
        });
    }





    /**
     * 1
     */
    public void onRecomendList1() {
        page = 1;
        homeRepository.onOverRecomend1(page,new BaseRepository.HttpCallListener<HomeRecommendGoodsResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (overIView != null) {
                    overIView.onRecomendListFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(HomeRecommendGoodsResult data) {
                if (overIView != null) {
                    overIView.onRecomendListSuccess(data);
                }
            }
        });
    }

    /**
     * 1
     */
    public void onRecomendListUpLoad1() {
        page++;
        homeRepository.onOverRecomend1(page,new BaseRepository.HttpCallListener<HomeRecommendGoodsResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (overIView != null) {
                    overIView.onRecomendListDnUpdateUpLoadFaile(msg);
                }
            }

            @Override
            public void onHttpCallSuccess(HomeRecommendGoodsResult data) {
                if(overIView != null) {
                    if(data == null || data.getList().size() < 1) {
                        overIView.onRecomendListUpLoadNoDatas();
                    }
                    else{
                        overIView.onRecomendListDnUpdateUpLoadSuccess(data);
                    }
                }
            }
        });
    }


    /**
     * 2
     */
    public void onRecomendList2() {
        page = 1;
        homeRepository.onOverRecomend2(page,new BaseRepository.HttpCallListener<HomeRecommendGoodsResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (overIView != null) {
                    overIView.onRecomendListFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(HomeRecommendGoodsResult data) {
                if (overIView != null) {
                    overIView.onRecomendListSuccess(data);
                }
            }
        });
    }

    /**
     * 2
     */
    public void onRecomendListUpLoad2() {
        page++;
        homeRepository.onOverRecomend2(page,new BaseRepository.HttpCallListener<HomeRecommendGoodsResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (overIView != null) {
                    overIView.onRecomendListDnUpdateUpLoadFaile(msg);
                }
            }

            @Override
            public void onHttpCallSuccess(HomeRecommendGoodsResult data) {
                if(overIView != null) {
                    if(data == null || data.getList().size() < 1) {
                        overIView.onRecomendListUpLoadNoDatas();
                    }
                    else{
                        overIView.onRecomendListDnUpdateUpLoadSuccess(data);
                    }
                }
            }
        });
    }




    /**
     * 首页推荐
     */
    public void onRecomendList3() {
        page = 1;
        homeRepository.onOverRecomend3(page,new BaseRepository.HttpCallListener<HomeRecommendGoodsResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (overIView != null) {
                    overIView.onRecomendListFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(HomeRecommendGoodsResult data) {
                if (overIView != null) {
                    overIView.onRecomendListSuccess(data);
                }
            }
        });
    }

    /**
     * 首页推荐
     */
    public void onRecomendListUpLoad3() {
        page++;
        homeRepository.onOverRecomend3(page,new BaseRepository.HttpCallListener<HomeRecommendGoodsResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (overIView != null) {
                    overIView.onRecomendListDnUpdateUpLoadFaile(msg);
                }
            }

            @Override
            public void onHttpCallSuccess(HomeRecommendGoodsResult data) {
                if(overIView != null) {
                    if(data == null || data.getList().size() < 1) {
                        overIView.onRecomendListUpLoadNoDatas();
                    }
                    else{
                        overIView.onRecomendListDnUpdateUpLoadSuccess(data);
                    }
                }
            }
        });
    }



    /**
     * 首页推荐4
     */
    public void onRecomendList4() {
        page = 1;
        homeRepository.onOverRecomend4(page,new BaseRepository.HttpCallListener<HomeRecommendGoodsResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (overIView != null) {
                    overIView.onRecomendListFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(HomeRecommendGoodsResult data) {
                if (overIView != null) {
                    overIView.onRecomendListSuccess(data);
                }
            }
        });
    }

    /**
     * 首页推荐4
     */
    public void onRecomendListUpLoad4() {
        page++;
        homeRepository.onOverRecomend4(page,new BaseRepository.HttpCallListener<HomeRecommendGoodsResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (overIView != null) {
                    overIView.onRecomendListDnUpdateUpLoadFaile(msg);
                }
            }

            @Override
            public void onHttpCallSuccess(HomeRecommendGoodsResult data) {
                if(overIView != null) {
                    if(data == null || data.getList().size() < 1) {
                        overIView.onRecomendListUpLoadNoDatas();
                    }
                    else{
                        overIView.onRecomendListDnUpdateUpLoadSuccess(data);
                    }
                }
            }
        });
    }
}
