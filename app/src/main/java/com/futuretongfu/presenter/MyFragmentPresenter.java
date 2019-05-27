package com.futuretongfu.presenter;

import android.content.Context;

import com.futuretongfu.bean.onlinegoods.PersonOrderNumBean;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.model.repository.UserRepository;
import com.futuretongfu.iview.IMyFragmentView;
import com.futuretongfu.model.repository.BaseRepository;

/**
 *   我的界面
 * Created by ChenXiaoPeng on 2017/7/15.
 */

public class MyFragmentPresenter extends Presenter{

    private IMyFragmentView iMyFragmentView;
    private UserRepository userRepository;

    public MyFragmentPresenter(Context context, IMyFragmentView iMyFragmentView){
        super(context);
        this.iMyFragmentView = iMyFragmentView;
        this.userRepository = new UserRepository();
    }

    @Override
    public void onDestroy(){
        if(userRepository != null)
            userRepository.cancel();
    }

    /**
     * 是否完成实名认证
     */
    public void getRealNameStatus() {
        userRepository.getRealNameInfo(
                UserManager.getInstance().getUserId() + ""
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if (iMyFragmentView != null)
                            iMyFragmentView.onGetRealNameStatusFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {

                        if (iMyFragmentView != null)
                            iMyFragmentView.onGetRealNameStatusSuccess();
                    }
                }
        );
    }

    /**
     *   获取个人信息
     */
    public void getUserInfoByUserId(){
        if(!UserManager.getInstance().isLogin()) {
            iMyFragmentView.onUpdateUserInfoFaile(false, "");
            return;
        }

        userRepository.getUserInfoByUserId(
                  UserManager.getInstance().getUserId()
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if (iMyFragmentView != null)
                            iMyFragmentView.onUpdateUserInfoFaile(true, msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        if (iMyFragmentView != null)
                            iMyFragmentView.onUpdateUserInfoSuccess();
                    }
                }
        );
    }


    /**
     *   获取各狀態數量
     */
    public void getPersonOrderNum(){
        if(!UserManager.getInstance().isLogin()) {
            iMyFragmentView.onUpdateUserInfoFaile(false, "");
            return;
        }
        userRepository.getPersonOrderNum(
                UserManager.getInstance().getUserId()+""
                , new BaseRepository.HttpCallListener<PersonOrderNumBean>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if (iMyFragmentView != null)
                            iMyFragmentView.onPersonOrderNumFaile(true, msg);
                    }

                    @Override
                    public void onHttpCallSuccess(PersonOrderNumBean data) {
                        if (iMyFragmentView != null)
                            iMyFragmentView.onPersonOrderNumSuccess(data);
                    }
                }
        );
    }

}
