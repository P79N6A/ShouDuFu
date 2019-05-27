package com.futuretongfu.presenter.wlsq;

import android.content.Context;

import com.futuretongfu.bean.CollectionBean;
import com.futuretongfu.bean.StoreEvaluateBean;
import com.futuretongfu.iview.StoreDetailsIView;
import com.futuretongfu.model.repository.WlsqRepository;
import com.futuretongfu.bean.StoreDetailsBean;
import com.futuretongfu.iview.AddCollectIView;
import com.futuretongfu.iview.StoreEvaluateIView;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.presenter.Presenter;

import java.util.List;

/**
 * 商家详情
 *
 * @author DoneYang 2017/6/25
 */

public class StoreDetailsPresenter extends Presenter {

    private StoreEvaluateIView iView;
    private WlsqRepository wlsqRepository;
    private StoreDetailsIView storeDetailsView;
    private AddCollectIView addCollectIView;

    public StoreDetailsPresenter(Context context, StoreDetailsIView storeDetailsView, StoreEvaluateIView iView, AddCollectIView addCollectIView) {
        super(context);
        this.iView = iView;
        this.storeDetailsView = storeDetailsView;
        this.addCollectIView = addCollectIView;
        this.wlsqRepository = new WlsqRepository();
    }

    @Override
    public void onDestroy() {
        if (wlsqRepository != null)
            wlsqRepository.cancel();
    }

    /**
     * 获取商家详情
     *
     * @param storeId
     */
    public void onStoreDetails(String userId, String storeId) {
        wlsqRepository.onStoreDetails(userId, storeId, new BaseRepository.HttpCallListener<StoreDetailsBean>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (storeDetailsView != null) {
                    storeDetailsView.onStoreDetailsFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(StoreDetailsBean data) {
                if (storeDetailsView != null) {
                    storeDetailsView.onStoreDetailsSuccess(data);
                }
            }
        });
    }

    /**
     * 添加收藏
     *
     * @param userId
     * @param storeId
     * @param terminal
     * @param type
     */
    public void onAddCollect(String userId, String storeId, String terminal, String type) {
        wlsqRepository.onAddCollect(userId, storeId, terminal, type, new BaseRepository.HttpCallListener<CollectionBean>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (addCollectIView != null) {
                    addCollectIView.onAddCollectFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(CollectionBean data) {
                if (addCollectIView != null) {
                    addCollectIView.onAddCollectSuccess(data);
                }
            }
        });
    }

    /**
     * 取消收藏
     *
     * @param userId
     * @param storeId
     */
    public void onDelCollect(String userId, String storeId) {
        wlsqRepository.onDelCollect(userId, storeId, new BaseRepository.HttpCallListener<CollectionBean>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (addCollectIView != null) {
                    addCollectIView.onAddCollectFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(CollectionBean data) {
                if (addCollectIView != null) {
                    addCollectIView.onAddCollectSuccess(data);
                }
            }
        });
    }

    //评价列表
    public void onStoreEvaluate(final int size, String shopId) {

        wlsqRepository.onStoreEvaluate(size, shopId, new BaseRepository.HttpCallListener<List<StoreEvaluateBean>>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iView != null) {
                    if (size == 1) {
                        iView.onStoreEvaluateFail(code, msg);
                    } else {
                        iView.onStoreEvaluateMoreFail(code, msg);
                    }
                }
            }

            @Override
            public void onHttpCallSuccess(List<StoreEvaluateBean> data) {
                if (iView != null) {
                    if (size == 1) {
                        iView.onStoreEvaluateSuccess(data);
                    } else {
                        iView.onStoreEvaluateMoreSuccess(data);
                    }
                }
            }
        });

    }
}
