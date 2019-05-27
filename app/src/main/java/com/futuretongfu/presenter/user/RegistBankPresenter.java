package com.futuretongfu.presenter.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.futuretongfu.bean.BindBankCardBean;
import com.futuretongfu.iview.IRegistBankVerView;
import com.futuretongfu.iview.IRegistRealVerView;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.repository.BankRepository;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.CommonRepository;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.activity.CameraActivity;

import java.io.File;

/**
 * 类: BankPresenter
 * 描述: 注册绑卡
 * 作者： weiang on 2017/6/25
 */
public class RegistBankPresenter extends Presenter {

    private IRegistBankVerView iBankVerView;
    private BankRepository bankRepository;
    private CommonRepository commonRepository;

    public RegistBankPresenter(Context context, IRegistBankVerView iBankView) {
        super(context);
        this.iBankVerView = iBankView;
        this.bankRepository = new BankRepository();
        commonRepository = new CommonRepository();
    }

    @Override
    public void onDestroy(){
        if(bankRepository != null) bankRepository.cancel();
        if(commonRepository != null)
            commonRepository.cancel();
    }



    public void uploadAndRecognize(String mPhotoPath) {
        if (!TextUtils.isEmpty(mPhotoPath)) {
            File file = new File(mPhotoPath);
            bankRepository.getCardNumber(file, new BaseRepository.HttpCallListener<String>() {
                @Override
                public void onHttpCallFaile(int code, String msg) {
                    if (iBankVerView != null)
                        iBankVerView.onGetCardNumberFaile(msg);
                }

                @Override
                public void onHttpCallSuccess(String data) {
                    if (iBankVerView != null)
                        iBankVerView.onGetCardNumberSuccess(data);
                }
            });
        }
    }

    public void getCardNumber(Context context) {
        File externalFile = context.getExternalFilesDir("/idcard/");

        Intent intent = new Intent(context, CameraActivity.class);
        String pathStr = externalFile.getAbsolutePath();
        String nameStr = "user.jpg";
        String typeStr = "idcardFront";
        if (!TextUtils.isEmpty(pathStr)) {
            intent.putExtra("path", pathStr);
        }
        if (!TextUtils.isEmpty(nameStr)) {
            intent.putExtra("name", nameStr);
        }
        if (!TextUtils.isEmpty(typeStr)) {
            intent.putExtra("type", typeStr);
        }
        ((Activity) context).startActivityForResult(intent, 100);
    }


    /**
     * 获取短信验证码汇付
     * @param phoneNumber 电话号码
     */
    public void getPhoneCodebindCard(String phoneNumber, String accNo,String userId) {
        commonRepository.getPhoneCodeBindCard(phoneNumber, accNo,userId, new BaseRepository.HttpCallListener<BindBankCardBean>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                iBankVerView.verificationSmsFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(BindBankCardBean data) {
                iBankVerView.verificationSmsSuccess(data);
            }
        });
    }

    /**
     * 验证实名+绑卡完善信息
     * @param userId 用户ID
     * @param accNo  银行卡账号
     */
    public void addUserTrueBank(String userId, String cardId,String userReal,String accNo, String phon, String verifyCode,
                                String innerCode,String bankName,String order_id,String provinceId,String  cityId) {
        bankRepository.addUserTrueBank(userId,cardId,userReal, accNo, phon, verifyCode, innerCode,bankName, order_id,provinceId, cityId,new BaseRepository.HttpCallListener<FuturePayApiResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iBankVerView != null)
                    iBankVerView.onaddUserTrueFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(FuturePayApiResult futurePayApiResult) {
                iBankVerView.onaddUserTrueSuccess(futurePayApiResult);
            }
        });
    }

}
