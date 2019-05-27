package com.futuretongfu.presenter.wlsq;

import android.content.Context;

import com.futuretongfu.bean.StoreEvaluateBean;
import com.futuretongfu.model.repository.WlsqRepository;
import com.futuretongfu.iview.StoreEvaluateIView;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.presenter.Presenter;

import java.util.List;

/**
 * @author DoneYang 2017/6/25
 */

public class StoreEvaluatePresenter extends Presenter {

    private WlsqRepository wlsqRepository;
    private StoreEvaluateIView iView;

    public StoreEvaluatePresenter(Context context, StoreEvaluateIView iView) {
        super(context);
        this.iView = iView;
        this.wlsqRepository = new WlsqRepository();
    }

    @Override
    public void onDestroy(){
        if(wlsqRepository != null)
            wlsqRepository.cancel();
    }

    public void onStoreEvaluate(int size, String shopId) {

        wlsqRepository.onStoreEvaluate(size, shopId, new BaseRepository.HttpCallListener<List<StoreEvaluateBean>>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iView != null) {
                    iView.onStoreEvaluateFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(List<StoreEvaluateBean> data) {
                if (iView != null) {
                    iView.onStoreEvaluateSuccess(data);
                }
            }
        });

    }
}
