package com.futuretongfu.presenter.user;

import android.content.Context;

import com.futuretongfu.iview.IEditPhoneView;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.model.repository.UserRepository;
import com.futuretongfu.iview.IEditPersonalDataView;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.presenter.Presenter;

/**
 * 编辑个人资料
 * Created by ChenXiaoPeng on 2017/6/26.
 */

public class EditPersonalDataPresenter extends Presenter {

    private IEditPersonalDataView iEditPersonalDataView;
    private IEditPhoneView iEditPhoneView;
    private UserRepository userRepository;

    public EditPersonalDataPresenter(Context context, IEditPersonalDataView iEditPersonalDataView) {
        super(context);
        this.iEditPersonalDataView = iEditPersonalDataView;
        this.userRepository = new UserRepository();
    }

    public EditPersonalDataPresenter(Context context, IEditPhoneView iEditPhoneView) {
        super(context);
        this.iEditPhoneView = iEditPhoneView;
        this.userRepository = new UserRepository();
    }


    @Override
    public void onDestroy() {
        if (userRepository != null)
            userRepository.cancel();
    }


    /**
     * 修改手机号
     */
    public void changePhoneNumber(final String phone, final String yzm) {
        userRepository.changePhoneNumber(phone, yzm, new BaseRepository.HttpCallListener<Void>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                  if(iEditPhoneView!=null){
                      iEditPhoneView.onEditPhoneFaile(msg);
                  }
            }

            @Override
            public void onHttpCallSuccess(Void data) {
                if(iEditPhoneView!=null){
                    iEditPhoneView.onEditPhoneSuccess();
                }
            }
        });
    }


    public void setNickName(final String nickName) {
        userRepository.setNickName(
                UserManager.getInstance().getUserId() + ""
                , nickName
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if (iEditPersonalDataView != null)
                            iEditPersonalDataView.onEditPersonalDataEditNameFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        UserManager.getInstance().setNickName(nickName);
                        UserManager.getInstance().save();

                        if (iEditPersonalDataView != null)
                            iEditPersonalDataView.onEditPersonalDataEditNameSuccess();
                    }
                }
        );
    }

    /**
     * 设置/修改邮箱
     */
    public void setEmail(final String email) {
        userRepository.setEmail(
                UserManager.getInstance().getUserId() + ""
                , email
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if (iEditPersonalDataView != null)
                            iEditPersonalDataView.onEditPersonalDataEditEmailFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        UserManager.getInstance().setEmail(email);
                        UserManager.getInstance().save();

                        if (iEditPersonalDataView != null)
                            iEditPersonalDataView.onEditPersonalDataEditEmailSuccess();
                    }
                }
        );
    }

    /**
     * 设置性别
     */
    public void setGender(final int gender) {
        userRepository.setGender(
                UserManager.getInstance().getUserId() + ""
                , gender
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if (iEditPersonalDataView != null)
                            iEditPersonalDataView.onEditPersonalDataEditGenderFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        UserManager.getInstance().setGender(gender);
                        UserManager.getInstance().save();

                        if (iEditPersonalDataView != null)
                            iEditPersonalDataView.onEditPersonalDataEditGenderSuccess();
                    }
                }
        );
    }
}
