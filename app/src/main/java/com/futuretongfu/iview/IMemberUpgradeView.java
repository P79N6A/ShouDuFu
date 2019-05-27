package com.futuretongfu.iview;

import com.futuretongfu.bean.Vip_Bean;
import com.futuretongfu.bean.entity.RegistUserTypeBean;
import com.futuretongfu.model.entity.MemberListInfo;
import com.futuretongfu.model.entity.WareHorseAddressEntity;

import java.util.List;

/**
 * 类:   IMemberUpgradeView
 * 描述:  会员升级view
 * 作者： weiang on 2017/7/1
 */
public interface IMemberUpgradeView {

    //获取可升级会员列表失败
    public void onMemberListViewFaile(String msg);

    //升级成为会员
    public void onMemberVpi(String data);

    //获取可升级会员列表成功
    public void onMemberListViewSuccess(List<MemberListInfo> memberListInfo);

    //升级失败
    public void onMemberUpFaile(String msg);

    //升级成功
    public void onMemberUpSuccess(String msg);

    //还未实名认证
    public void onGetRealNameStatusFaile(String msg);
    //已经实名认证
    public void onGetRealNameStatusSuccess();

    //提交收货地址
    public void onCommitAddressFaile(String msg);
    public void onCommitAddressSuccess();
    //获取收货地址列表成功
    public void onWareAddressSuccess(WareHorseAddressEntity datas);
    //获取收货地址列表失败
    public void onWareAddressFaile(String msg);

    public void onUserTypeSuccess(List<RegistUserTypeBean> data);
    public void onUserTypeFaile(String msg);


    void onFirmOrderFail(String msg);
    void onFirmOrderSuccess(String result);


    //支付
    void onPaymentFail(int code, String msg);
    void onPaymentSuccess(String data);

}
