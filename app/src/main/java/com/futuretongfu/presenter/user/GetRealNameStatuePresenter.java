package com.futuretongfu.presenter.user;

import android.content.Context;

import com.futuretongfu.iview.IGetRealNameStatueView;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.UserRepository;
import com.futuretongfu.presenter.Presenter;

/**
 *    获取实名认证状态
 * Created by ChenXiaoPeng on 2017/7/19.
 */

public class GetRealNameStatuePresenter extends Presenter {

    private IGetRealNameStatueView iGetRealNameStatueView;
    private UserRepository userRepository;

    public GetRealNameStatuePresenter(Context context, IGetRealNameStatueView iGetRealNameStatueView){
        super(context);
        this.iGetRealNameStatueView = iGetRealNameStatueView;
        this.userRepository = new UserRepository();
    }

    @Override
    public void onDestroy(){
        if(userRepository != null)
            userRepository.cancel();
    }

    //获取实名状态 和 实名信息
    public void getRealNameStatuesInfo(){
        userRepository.getRealNameInfo(
                UserManager.getInstance().getUserId() + ""
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {

                        if(iGetRealNameStatueView != null)
                            iGetRealNameStatueView.onGetRealNameStatueFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        if(iGetRealNameStatueView != null)
                            iGetRealNameStatueView.onGetRealNameStatueSuccess();

                    }
                }
        );
    }
}
