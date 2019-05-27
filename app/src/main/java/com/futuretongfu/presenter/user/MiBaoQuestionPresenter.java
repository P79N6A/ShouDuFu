package com.futuretongfu.presenter.user;

import android.content.Context;

import com.futuretongfu.model.entity.SafeQuestion;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.model.repository.CommonRepository;
import com.futuretongfu.iview.IMiBaoQuestionView;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.presenter.Presenter;

import java.util.List;

/**
 * Created by ChenXiaoPeng on 2017/7/4.
 */

public class MiBaoQuestionPresenter extends Presenter {

    private IMiBaoQuestionView iMiBaoQuestionView;
    private CommonRepository commonRepository;

    public MiBaoQuestionPresenter(Context context, IMiBaoQuestionView iMiBaoQuestionView){
        super(context);
        this.iMiBaoQuestionView = iMiBaoQuestionView;
        this.commonRepository = new CommonRepository();
    }

    @Override
    public void onDestroy(){
        if(commonRepository != null)
            commonRepository.cancel();
    }

    /**
     *    获取密保问题列表
     * */
    public void queryQuestionList(){
        commonRepository.queryQuestionList(new BaseRepository.HttpCallListener<List<SafeQuestion>>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if(iMiBaoQuestionView != null)
                    iMiBaoQuestionView.onQueryQuestionListFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(List<SafeQuestion> data) {
                if(iMiBaoQuestionView != null)
                    iMiBaoQuestionView.onQueryQuestionListSuccess(data);
            }
        });
    }

    /**
     *    设置密保问题
     * */
    public void setSecurityQuestion(int questionId, String answer){
        commonRepository.setSecurityQuestion(
                UserManager.getInstance().getUserId()
                , questionId, answer
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iMiBaoQuestionView != null)
                            iMiBaoQuestionView.onSetSecurityQuestionFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        if(iMiBaoQuestionView != null)
                            iMiBaoQuestionView.onSetSecurityQuestionSuccess();
                    }
                }
        );
    }

    /**
     *    验证密保问题
     * */
    public void authSecurityQuestion(String answer){
        commonRepository.authSecurityQuestion(
                UserManager.getInstance().getUserId()
                , answer
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iMiBaoQuestionView != null)
                            iMiBaoQuestionView.onAuthSecurityQuestionFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        if(iMiBaoQuestionView != null)
                            iMiBaoQuestionView.onAuthSecurityQuestionSuccess();
                    }
                }
        );
    }

    /**
     *    修改密保问题
     * */
    public void changeSecurityQuestion(long questionId, String answer){
        commonRepository.changeSecurityQuestion(
                UserManager.getInstance().getUserId()
                , questionId, answer
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iMiBaoQuestionView != null)
                            iMiBaoQuestionView.onChangeSecurityQuestionFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        if(iMiBaoQuestionView != null)
                            iMiBaoQuestionView.onChangeSecurityQuestionSuccess();
                    }
                }
        );
    }
}
