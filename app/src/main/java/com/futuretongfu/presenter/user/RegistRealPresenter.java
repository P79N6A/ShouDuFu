package com.futuretongfu.presenter.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.futuretongfu.iview.IRegistRealVerView;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.repository.BankRepository;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.activity.CameraActivity;

import java.io.File;

/**
 * 类: BankPresenter
 * 描述: 注册绑卡
 * 作者： weiang on 2017/6/25
 */
public class RegistRealPresenter extends Presenter {

    private IRegistRealVerView iBankVerView;
    private BankRepository bankRepository;

    public RegistRealPresenter(Context context, IRegistRealVerView iBankView) {
        super(context);
        this.iBankVerView = iBankView;
        this.bankRepository = new BankRepository();
    }

    @Override
    public void onDestroy(){
        if(bankRepository != null) bankRepository.cancel();
    }


    /**
     * 扫一扫识别身份证
     * @param context
     */
    public void checkCardNumber(Context context) {
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
            bankRepository.getCardRealNumber(file, new BaseRepository.HttpCallListener<String>() {
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

    /**
     * 开户
     * @param userId 用户ID
     */
    public void addUserOpen(String userId, String cardId,String userReal, String phon,String provinceId,String  cityId,String receiverName,
                            String receiverMobile,String receiverAddress) {
        bankRepository.addUserOPen(userId,cardId,userReal, phon,provinceId, cityId,receiverName,receiverMobile,receiverAddress,new BaseRepository.HttpCallListener<FuturePayApiResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if(iBankVerView != null) {
                    if(5555 == code){
                        iBankVerView.onaddUserOpenFaileUserExists(msg);
                    }else {
                        iBankVerView.onaddUserOpenFaile(msg);
                    }
                }
            }

            @Override
            public void onHttpCallSuccess(FuturePayApiResult futurePayApiResult) {
                iBankVerView.onaddUserOpenSuccess(futurePayApiResult);
            }
        });
    }


}
