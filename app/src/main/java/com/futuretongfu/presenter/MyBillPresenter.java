package com.futuretongfu.presenter;

import android.content.Context;

import com.futuretongfu.iview.IMyBillView;
import com.futuretongfu.model.entity.MyBillResult;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.BillRepository;

/**
 * Created by ChenXiaoPeng on 2017/7/10.
 */

public class MyBillPresenter extends Presenter{

    private IMyBillView iMyBillView;
    private BillRepository billRepository;

    private int page = 1;

    public MyBillPresenter(Context context, IMyBillView iMyBillView){
        super(context);
        this.iMyBillView = iMyBillView;
        this.billRepository = new BillRepository();
    }

    @Override
    public void onDestroy(){
        if(billRepository != null)
            billRepository.cancel();
    }

    public void billListDwRefresh(String type, String createTime){
        page = 1;
        billRepository.getBillList(
                UserManager.getInstance().getUserId()
                , page, type, createTime
                , new BaseRepository.HttpCallListener<MyBillResult>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iMyBillView != null)
                            iMyBillView.onMyBillDnRefreshFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(MyBillResult data) {
                        if(iMyBillView != null)
                            iMyBillView.onMyBillDnRefreshSuccess(data);
                    }
                }
        );
    }

    public void billListUpLoad(String type, String createTime){
        page++;
        billRepository.getBillList(
                UserManager.getInstance().getUserId()
                , page, type, createTime
                , new BaseRepository.HttpCallListener<MyBillResult>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        page--;
                        if(iMyBillView != null)
                            iMyBillView.onMyBillUpLoadFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(MyBillResult data) {
                        if(iMyBillView != null) {
                            if(null == data.getList() || data.getList().size() < 1){
                                iMyBillView.onMyBillUpLoadNoDatas();
                            }
                            else {
                                iMyBillView.onMyBillUpLoadSuccess(data.getList());
                            }
                        }
                    }
                }
        );
    }
}
