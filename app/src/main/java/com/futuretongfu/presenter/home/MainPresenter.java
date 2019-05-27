package com.futuretongfu.presenter.home;

import android.content.Context;

import com.futuretongfu.bean.HomeBannerBean;
import com.futuretongfu.bean.HomeIncomeBean;
import com.futuretongfu.bean.SystemMessageBean;
import com.futuretongfu.bean.onlinegoods.HomeNoticeTipBean;
import com.futuretongfu.bean.onlinegoods.HomeSortBean;
import com.futuretongfu.iview.HomeMainIView;
import com.futuretongfu.iview.IMainView;
import com.futuretongfu.model.entity.HomeRecommendGoodsResult;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.HomeRepository;
import com.futuretongfu.model.repository.UserRepository;
import com.futuretongfu.presenter.Presenter;

import java.util.List;

/**
 * @author DoneYang 2017/6/29
 */

public class MainPresenter extends Presenter {

    private HomeRepository homeRepository;
    private UserRepository userRepository;
    private IMainView homeIView;

    public MainPresenter(Context context, IMainView homeIView) {
        super(context);
        this.homeRepository = new HomeRepository();
        this.userRepository = new UserRepository();
        this.homeIView = homeIView;
    }

    @Override
    public void onDestroy(){
        if(homeRepository != null) homeRepository.cancel();
        if(userRepository != null) userRepository.cancel();
    }


    /**
     * 购物车数量
     * @param userId
     */
    public void onShopCartNum(String userId) {
        homeRepository.onShopCartNum(userId, new BaseRepository.HttpCallListener<String>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (homeIView != null) {
                    homeIView.onshopCartFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(String data) {
                if (homeIView != null) {
                    homeIView.onshopCartSuccess(data);
                }
            }
        });
    }

    /**
     * 首页收益
     * @param userId
     */
    public void onHomeIncomeNum(String userId) {
        homeRepository.onHomeIncomeNum(userId, new BaseRepository.HttpCallListener<HomeIncomeBean>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (homeIView != null) {
                    homeIView.onHomeIncomeFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(HomeIncomeBean data) {
                if (homeIView != null) {
                    homeIView.onnHomeIncomeSuccess(data);
                }
            }
        });
    }

    /**
     *   获取个人信息
     */
    public void getUserInfoByUserId(){
        if(!UserManager.getInstance().isLogin()) {
            homeIView.onUpdateUserInfoFaile(false, "");
            return;
        }

        userRepository.getUserInfoByUserId(
                UserManager.getInstance().getUserId()
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if (homeIView != null)
                            homeIView.onUpdateUserInfoFaile(true, msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        if (homeIView != null)
                            homeIView.onUpdateUserInfoSuccess();
                    }
                }
        );
    }


}
