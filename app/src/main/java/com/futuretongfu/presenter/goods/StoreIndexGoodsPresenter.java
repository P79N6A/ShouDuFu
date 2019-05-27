package com.futuretongfu.presenter.goods;

import android.content.Context;

import com.futuretongfu.bean.StoreOnLineDetailsBean;
import com.futuretongfu.bean.onlinegoods.StoreIndexGoodsResult;
import com.futuretongfu.iview.IStoreIndexGoodsView;
import com.futuretongfu.iview.IStoreIndexView;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.StoreIndexRepository;
import com.futuretongfu.presenter.Presenter;


/**
 * Created by zhanggf on 2018/5/10.
 * 店铺主页--线上
 */

public class StoreIndexGoodsPresenter extends Presenter {

    private IStoreIndexGoodsView iStoreIndexView;
    private StoreIndexRepository storeIndexRepository;
    private int page;

    public StoreIndexGoodsPresenter(Context context, IStoreIndexGoodsView iStoreIndexView) {
        super(context);
        this.iStoreIndexView = iStoreIndexView;
        storeIndexRepository = new StoreIndexRepository();
    }

    @Override
    public void onDestroy() {
        if (storeIndexRepository != null)
            storeIndexRepository.cancel();
    }

    public void getStoreGoodsList(String storeId, int type) {
        page = 1;
        storeIndexRepository.getStoreGoodsListGoods(storeId,type,page, new BaseRepository.HttpCallListener<StoreIndexGoodsResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iStoreIndexView != null)
                    iStoreIndexView.onStoreGoodsFail(code,msg);
            }

            @Override
            public void onHttpCallSuccess(StoreIndexGoodsResult data) {
                if(iStoreIndexView != null) {
                    iStoreIndexView.onStoreGoodsSuccess(data);
                }
            }
        });
    }

    public void getStoreGoodsUpLoadList(String storeId, int type) {
        page ++;
        storeIndexRepository.getStoreGoodsListGoods(storeId,type,page, new BaseRepository.HttpCallListener<StoreIndexGoodsResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iStoreIndexView != null)
                    iStoreIndexView.onStoreGoodsUpLoadFail(code,msg);
            }

            @Override
            public void onHttpCallSuccess(StoreIndexGoodsResult data) {
                if(iStoreIndexView != null) {
                    if(data == null || data.getList().size()< 1) {
                        iStoreIndexView.onStoreGoodsUpLoadNoDatas();
                    }
                    else{
                        iStoreIndexView.onStoreGoodsUpLoadSuccess(data);
                    }
                }
            }
        });
    }

}
