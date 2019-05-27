package com.futuretongfu.presenter.home;

import android.content.Context;

import com.futuretongfu.bean.TransferListBean;
import com.futuretongfu.iview.TransferContactIVew;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.HomeRepository;
import com.futuretongfu.presenter.Presenter;

import java.util.List;

/**
 * 转账-最近联系人
 *
 * @author DoneYang 2017/7/4
 */

public class TransferContactPresenter extends Presenter {

    private HomeRepository homeRepository;
    private TransferContactIVew transferContactIVew;

    public TransferContactPresenter(Context context, TransferContactIVew transferContactIVew) {
        super(context);
        this.transferContactIVew = transferContactIVew;
        this.homeRepository = new HomeRepository();
    }

    @Override
    public void onDestroy(){
        if(homeRepository != null) homeRepository.cancel();
    }

    /**
     * 转账-最近联系人
     *
     * @param userId
     */
    public void onTransferContract(String userId) {
        homeRepository.onTransferContact(userId, new BaseRepository.HttpCallListener<List<TransferListBean>>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (transferContactIVew != null) {
                    transferContactIVew.onTransferContactFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(List<TransferListBean> data) {
                if (transferContactIVew != null) {
                    transferContactIVew.onTransferContactSuccess(data);
                }
            }
        });
    }
}
