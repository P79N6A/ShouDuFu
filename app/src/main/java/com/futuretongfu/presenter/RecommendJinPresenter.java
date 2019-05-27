package com.futuretongfu.presenter;

import android.content.Context;

import com.futuretongfu.bean.RecommendBean;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.model.repository.AccountRepository;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.UserRepository;
import com.futuretongfu.view.IRecommendView;

public class RecommendJinPresenter extends Presenter {



    public final static int Recommend_ZC = 0;//充值
    public final static int Recommend_TX = 1;//提现

    private AccountRepository accountRepository;
    private UserRepository userRepository;
    private IRecommendView mRecommendView;


    public RecommendJinPresenter(Context context, IRecommendView mRecommendView) {
        super(context);
        this.mRecommendView=mRecommendView;
        this.accountRepository = new AccountRepository();
        this.userRepository = new UserRepository();
    }

    @Override
    public void onDestroy() {
        if(accountRepository != null)
            accountRepository.cancel();

        if(userRepository != null)
            userRepository.cancel();
    }

    /**
     *   推荐奖
     * */
    public void recommend(){
        accountRepository.getrecommend(
                UserManager.getInstance().getUserId() + ""
                , new BaseRepository.HttpCallListener<RecommendBean.DataBean>() {

                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(mRecommendView != null)
                            mRecommendView.onRecommendFaile(msg);
                    }
                    @Override
                    public void onHttpCallSuccess(RecommendBean.DataBean data) {
                        if(mRecommendView != null)
                            mRecommendView.onRecommendSuccess(data);
                    }

                }
        );
    }
    /**
     * 是否完成实名认证
     */
    public void getRealNameStatus(final int operation) {
        userRepository.getRealNameInfo(
                UserManager.getInstance().getUserId() + ""
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if (mRecommendView != null)
                            mRecommendView.onGetRealNameStatusFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {

                        if (mRecommendView != null)
                            mRecommendView.onGetRealNameStatusSuccess(operation);

                    }
                }
        );
    }
}
