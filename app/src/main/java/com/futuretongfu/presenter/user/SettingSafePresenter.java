package com.futuretongfu.presenter.user;

import android.content.Context;

import com.futuretongfu.iview.ISettingSafeView;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.UserRepository;
import com.futuretongfu.presenter.Presenter;

/**
 * Created by ChenXiaoPeng on 2017/6/27.
 */

public class SettingSafePresenter extends Presenter {

    private ISettingSafeView iSettingSafeView;
    private UserRepository userRepository;

    public SettingSafePresenter(Context context, ISettingSafeView iSettingSafeView){
        super(context);
        this.iSettingSafeView = iSettingSafeView;
        this.userRepository = new UserRepository();
    }

    @Override
    public void onDestroy(){
        if(userRepository != null)
            userRepository.cancel();
    }

    public void setAutoUpgrade(final boolean upgrade){
        userRepository.setAutoUpgrade(
                UserManager.getInstance().getUserId() + ""
                , upgrade
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iSettingSafeView != null)
                            iSettingSafeView.onSettingSafeUpgradeFaile(msg, upgrade);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        UserManager.getInstance().setUpgrad(upgrade);
                        UserManager.getInstance().save();

                        if(iSettingSafeView != null)
                            iSettingSafeView.onSettingSafeUpgradeSuccess();
                    }
                }
        );
    }
}
