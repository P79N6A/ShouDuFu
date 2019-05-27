package com.futuretongfu.presenter.user;

import android.content.Context;

import com.futuretongfu.bean.Vip_Bean;
import com.futuretongfu.bean.entity.RegistUserTypeBean;
import com.futuretongfu.iview.IMemberUpgradeView;
import com.futuretongfu.model.entity.MemberListInfo;
import com.futuretongfu.model.entity.WareHorseAddressEntity;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.MemberUpgradeReposttory;
import com.futuretongfu.model.repository.UserRepository;
import com.futuretongfu.presenter.Presenter;

import java.util.List;

/**
 * 类:    MemberUpgradePresenter
 * 描述:  会员升级
 * 作者： weiang on 2017/7/1
 */
public class MemberUpgradePresenter extends Presenter {
    private MemberUpgradeReposttory memberUpgradeReposttory;
    private UserRepository userRepository;
    private IMemberUpgradeView iMemberUpgradeView;

    public MemberUpgradePresenter(Context context, IMemberUpgradeView iMemberUpgradeView) {
        super(context);
        this.iMemberUpgradeView = iMemberUpgradeView;
        memberUpgradeReposttory = new MemberUpgradeReposttory();
        userRepository = new UserRepository();
    }

    @Override
    public void onDestroy(){
        if(userRepository != null)
            userRepository.cancel();

        if(memberUpgradeReposttory != null)
            memberUpgradeReposttory.cancel();
    }
    /*
    * 用户升级
    */
    public void getMemberVip(){
        memberUpgradeReposttory.getMemberVipBean(new BaseRepository.HttpCallListener<String>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iMemberUpgradeView != null){
                    iMemberUpgradeView.onCommitAddressFaile(msg);
                }
            }

            @Override
            public void onHttpCallSuccess(String data) {
                if (iMemberUpgradeView != null){
                    iMemberUpgradeView.onMemberVpi(data);
                }
            }
        });

    }

    /**
     * 获取用户可升级列表
     *
     * @param userId
     */
    public void getMemberList(String userId) {
        memberUpgradeReposttory.getMemberList(userId, new BaseRepository.HttpCallListener<List<MemberListInfo>>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                iMemberUpgradeView.onMemberListViewFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(List<MemberListInfo> datas) {
                if (datas == null) {
                    return;
                }
                iMemberUpgradeView.onMemberListViewSuccess(datas);
            }
        });
    }

    /**
     * 会员升级
     */
    public void memberUpgrade(String userId, String upgradeType,String password) {
        memberUpgradeReposttory.memberUpgrade(userId, upgradeType,password, new BaseRepository.HttpCallListener<Void>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                iMemberUpgradeView.onMemberUpFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(Void data) {
                iMemberUpgradeView.onMemberUpSuccess(null);
            }
        });
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
                        if (iMemberUpgradeView != null)
                            iMemberUpgradeView.onGetRealNameStatusFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {

                        if (iMemberUpgradeView != null)
                            iMemberUpgradeView.onGetRealNameStatusSuccess();
                    }
                }
        );
    }


    /**
     * 收货地址
     */
    public void getCommitAddress(String userId, String receiverName, String receiverMobile, String receiverAddress) {
        userRepository.commitaddress(userId,receiverName,receiverMobile,receiverAddress
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if (iMemberUpgradeView != null)
                            iMemberUpgradeView.onCommitAddressFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        if (iMemberUpgradeView != null)
                            iMemberUpgradeView.onCommitAddressSuccess();
                    }
                }
        );
    }

    /**
     * 获取收获地址列表
     */
    public void getWareAddressList(String userId) {
        userRepository.getAddressList(userId, new BaseRepository.HttpCallListener<WareHorseAddressEntity>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iMemberUpgradeView != null)
                    iMemberUpgradeView.onWareAddressFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(WareHorseAddressEntity data) {
                if(iMemberUpgradeView != null) {
                    iMemberUpgradeView.onWareAddressSuccess(data);
                }
            }
        });
    }


    /**
     *   注册须知
     * */
    public void getRegistUserList(){
        userRepository.getRegistUserList(new BaseRepository.HttpCallListener<List<RegistUserTypeBean>>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if(iMemberUpgradeView != null)
                    iMemberUpgradeView.onUserTypeFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(List<RegistUserTypeBean> data) {
                if(iMemberUpgradeView != null)
                    iMemberUpgradeView.onUserTypeSuccess(data);
            }
        });
    }

    /**
     * 会员升级
     */
    public void VipSjZ(String userId, String money,int flag,String rechargeChannel) {
         userRepository.VipSjZ(userId, money, flag, rechargeChannel, new BaseRepository.HttpCallListener<String>() {
             @Override
             public void onHttpCallFaile(int code, String msg) {
                 if(iMemberUpgradeView != null)
                     iMemberUpgradeView.onFirmOrderFail(msg);
             }


             @Override
             public void onHttpCallSuccess(String data) {
                 if(iMemberUpgradeView != null)
                     iMemberUpgradeView.onFirmOrderSuccess(data);
             }

         });

    }
    public void VipSjW(String userId, String money,int flag,String rechargeChannel) {
        userRepository.VipSjW(userId, money, flag, rechargeChannel, new BaseRepository.HttpCallListener<String>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if(iMemberUpgradeView != null)
                    iMemberUpgradeView.onFirmOrderFail(msg);
            }


            @Override
            public void onHttpCallSuccess(String data) {
                if(iMemberUpgradeView != null)
                    iMemberUpgradeView.onFirmOrderSuccess(data);
            }

        });

    }

}
