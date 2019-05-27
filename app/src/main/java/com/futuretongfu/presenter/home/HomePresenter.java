package com.futuretongfu.presenter.home;

import android.content.Context;

import com.futuretongfu.base.BaseSerializable;
import com.futuretongfu.bean.HomeBannerBean;
import com.futuretongfu.bean.HomeTransactionBean;
import com.futuretongfu.bean.SystemMessageBean;
import com.futuretongfu.iview.HomeIView;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.HomeRepository;
import com.futuretongfu.presenter.Presenter;

import java.util.List;

/**
 * @author DoneYang 2017/6/29
 */

public class HomePresenter extends Presenter {

    private HomeRepository homeRepository;
    private HomeIView homeIView;

    public HomePresenter(Context context, HomeIView homeIView) {
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
     * 交易列表
     *
     * @param page
     * @param userId
     */
    public void onTransactionList(int page, String userId) {
        homeRepository.onTransactionList(page, userId, new BaseRepository.HttpCallListener<List<HomeTransactionBean>>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (homeIView != null) {
                    homeIView.onTransactionFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(List<HomeTransactionBean> data) {
                if (homeIView != null) {
                    homeIView.onTransactionSuccess(data);
                }
            }
        });

    }

    /**
     * 交易列表-更多
     *
     * @param page
     * @param userId
     */
    public void onTransactionListMore(int page, String userId) {
        homeRepository.onTransactionList(page, userId, new BaseRepository.HttpCallListener<List<HomeTransactionBean>>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (homeIView != null) {
                    homeIView.onTransactionMoreFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(List<HomeTransactionBean> data) {
                if (homeIView != null) {
                    homeIView.onTransactionMoreSuccess(data);
                }
            }
        });

    }

    /**
     * 首页banner
     */
    public void onBannerList() {
        homeRepository.onHomeBanner(new BaseRepository.HttpCallListener<List<HomeBannerBean>>() {
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

    /**
     * 忽略此动态
     *
     * @param id
     */
    public void onRemoveMessage(String id) {
        homeRepository.onRemoveMessage(id, new BaseRepository.HttpCallListener<BaseSerializable>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (homeIView != null) {
                    homeIView.onRemoveMessageFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(BaseSerializable data) {
                if (homeIView != null) {
                    homeIView.onRemoveMessageSuccess(data);
                }
            }
        });
    }

    /**
     * 获取个人昨日转换
     */
    public void getIntegralConvert(){
        if(!UserManager.getInstance().isLogin()) {
            homeIView.onIntegralConvertFaile(false,"");
            return;
        }
        homeRepository.getIntegralConvert(new BaseRepository.HttpCallListener<String>(){
            @Override
            public void onHttpCallFaile(int code, String msg) {
                homeIView.onIntegralConvertFaile(false,msg);
            }

            @Override
            public void onHttpCallSuccess(String convertJifen) {
                homeIView.onIntegralConvertSuccess(convertJifen);
            }
        });

    }
}
