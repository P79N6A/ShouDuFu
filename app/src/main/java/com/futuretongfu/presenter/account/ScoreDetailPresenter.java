package com.futuretongfu.presenter.account;

import android.content.Context;

import com.futuretongfu.iview.IScoreDetailView;
import com.futuretongfu.model.entity.Score;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.model.repository.AccountRepository;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.presenter.Presenter;

/**
 *    我——积分详情  Presenter
 * Created by ChenXiaoPeng on 2017/6/29.
 */

public class ScoreDetailPresenter extends Presenter {

    private IScoreDetailView iScoreDetailView;
    private AccountRepository accountRepository;

    public ScoreDetailPresenter(Context context, IScoreDetailView iScoreDetailView){
        super(context);
        this.iScoreDetailView = iScoreDetailView;
        this.accountRepository = new AccountRepository();
    }

    @Override
    public void onDestroy(){
        if(accountRepository != null)  accountRepository.cancel();
    }

    /**
     *   积分
     * */
    public void score(){
        accountRepository.score(
                UserManager.getInstance().getUserId() + ""
                , new BaseRepository.HttpCallListener<Score>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iScoreDetailView != null)
                            iScoreDetailView.onScoreDetailFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Score data) {
                        if(iScoreDetailView != null)
                            iScoreDetailView.onScoreDetailSuccess(data);
                    }
                }
        );
    }
}
