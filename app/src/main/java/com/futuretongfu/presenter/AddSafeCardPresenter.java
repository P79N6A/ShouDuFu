package com.futuretongfu.presenter;
/*
 * Created by hhm on 2017/7/26.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.futuretongfu.iview.IBankVerView;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.model.repository.BankRepository;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.ui.activity.CameraActivity;

import org.json.JSONObject;

import java.io.File;

public class AddSafeCardPresenter extends Presenter {

    private IBankVerView iBankVerView;
    private Context context;
    private BankRepository bankRepository;

    public AddSafeCardPresenter(Context context, IBankVerView iBankVerView) {
        super(context);
        this.iBankVerView = iBankVerView;
        bankRepository = new BankRepository();
    }

    @Override
    public void onDestroy() {

        if(bankRepository != null)
            bankRepository.cancel();
    }

    /**
     * 校验银行卡
     * bankNo:银行卡号
     * idNo：身份证号
     * phone：手机号
     */
    public void verBankCard(String userId, String bankNo, String phone) {
        bankRepository.verBankCard(userId, bankNo, phone, new BaseRepository.HttpCallListener<JSONObject>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iBankVerView != null)
                    iBankVerView.onBankVerFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(JSONObject json) {
                String innnerCode = json.optString(Constants.Response_bank_code);
                String bankName = json.optString(Constants.Response_bank_description);
                if(!TextUtils.isEmpty(innnerCode) && !TextUtils.isEmpty(bankName)){
                    iBankVerView.onBankVerSuccess(innnerCode,bankName);
                }else{
                    iBankVerView.onBankVerSuccess("","");
                }
            }
        });
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
}
