package com.futuretongfu.presenter;

import android.content.Context;
import android.util.Log;

import com.futuretongfu.iview.IMyBillView;
import com.futuretongfu.model.entity.MyBillEntity;
import com.futuretongfu.model.entity.MyBillResult;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.iview.IBillDetailView;
import com.futuretongfu.model.entity.BillDetail;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.BillRepository;

import java.util.List;

/**
 * Created by ChenXiaoPeng on 2017/7/12.
 */

public class BillDetailPresenter extends Presenter implements IMyBillView {

    private IBillDetailView iBillDetailView;
    private BillRepository billRepository;

    public BillDetailPresenter(Context context, IBillDetailView iBillDetailView){
        super(context);
        this.iBillDetailView = iBillDetailView;
        this.billRepository = new BillRepository();
    }

    @Override
    public void onDestroy(){
        if(billRepository != null)
            billRepository.cancel();
    }

    /**
     *    账单详情
     */
    public void getBillDetail(String businessNo, String businessType){
        billRepository.getBillDetail(
                UserManager.getInstance().getUserId()
                , businessNo, businessType
                , new BaseRepository.HttpCallListener<BillDetail>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iBillDetailView != null)
                            iBillDetailView.onBillDetailFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(BillDetail data) {
                        if(iBillDetailView != null)
                            iBillDetailView.onBillDetailSuccess(data);
                    }
                }
        );
    }

    /**
     *    账单详情
     */
    public void getJFDetail(String businessNo, String businessType){
        billRepository.getJFDetail(
                UserManager.getInstance().getUserId()
                , businessNo, businessType
                , new BaseRepository.HttpCallListener<BillDetail>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iBillDetailView != null)
                            iBillDetailView.onBillDetailFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(BillDetail data) {
                        if(iBillDetailView != null)
                            iBillDetailView.onBillDetailSuccess(data);
                    }
                }
        );
    }


    @Override
    public void onMyBillDnRefreshSuccess(MyBillResult datas) {

    }

    @Override
    public void onMyBillDnRefreshFaile(String msg) {

    }

    @Override
    public void onMyBillUpLoadSuccess(List<MyBillEntity> datas) {

    }

    @Override
    public void onMyBillUpLoadFaile(String msg) {

    }

    @Override
    public void onMyBillUpLoadNoDatas() {

    }
}
