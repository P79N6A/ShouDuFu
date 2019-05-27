package com.futuretongfu.presenter.goods;

import android.content.Context;

import com.futuretongfu.bean.StoreOnLineDetailsBean;
import com.futuretongfu.bean.classification.ClassOneListViewBean;
import com.futuretongfu.iview.IClassGoodsView;
import com.futuretongfu.iview.IStoreIndexView;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.ClassGoodsRepository;
import com.futuretongfu.model.repository.StoreIndexRepository;
import com.futuretongfu.presenter.Presenter;

import java.util.List;


/**
 * Created by zhanggf on 2018/5/10.
 * 店铺主页--线上
 */

public class StoreIndexPresenter extends Presenter {

    private IStoreIndexView iStoreIndexView;
    private StoreIndexRepository storeIndexRepository;

    public StoreIndexPresenter(Context context, IStoreIndexView iStoreIndexView) {
        super(context);
        this.iStoreIndexView = iStoreIndexView;
        storeIndexRepository = new StoreIndexRepository();
    }

    @Override
    public void onDestroy() {
        if (storeIndexRepository != null)
            storeIndexRepository.cancel();
    }

    /**
     * 获取分类列表
     */
    public void getStoreDetailsList(String userId, String id) {
        storeIndexRepository.getStoreDetails(userId,id, new BaseRepository.HttpCallListener<StoreOnLineDetailsBean>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iStoreIndexView != null)
                    iStoreIndexView.onStoreDetailsFail(code,msg);
            }

            @Override
            public void onHttpCallSuccess(StoreOnLineDetailsBean data) {
                if(iStoreIndexView != null) {
                    iStoreIndexView.onStoreDetailsSuccess(data);
                }
            }
        });
    }

    /**
     * 关注店铺
     */
    public void onStoreAddFavorite(String userId, String id,String type) {
        storeIndexRepository.onStoreAddFavorite(userId, id,type, new BaseRepository.HttpCallListener<FuturePayApiResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iStoreIndexView != null)
                    iStoreIndexView.onCollectFail(msg);
            }

            @Override
            public void onHttpCallSuccess(FuturePayApiResult futurePayApiResult) {
                iStoreIndexView.onCollectSuccess(1,futurePayApiResult);
            }
        });

    }

    /**
     * 取消关注
     */
    public void onStoreCancelFavorite(String userId, String id) {
        storeIndexRepository.onStoreCancelFavorite(userId, id, new BaseRepository.HttpCallListener<FuturePayApiResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iStoreIndexView != null)
                    iStoreIndexView.onCollectFail(msg);
            }

            @Override
            public void onHttpCallSuccess(FuturePayApiResult futurePayApiResult) {
                    iStoreIndexView.onCollectSuccess(2,futurePayApiResult);
            }
        });
    }


}
