package com.futuretongfu.presenter.goods;

import android.content.Context;

import com.futuretongfu.iview.IOnlineGoodsIndexView;
import com.futuretongfu.iview.IOnlineGoodsSpecialIndexView;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.entity.OnlineGoodsDetailsResult;
import com.futuretongfu.model.entity.OnlineGoodsSpecialDetailsResult;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.OnlineGoodsRepository;
import com.futuretongfu.model.repository.StoreIndexRepository;
import com.futuretongfu.presenter.Presenter;


/**
 * Created by zhanggf on 2018/5/10.
 * 商品详情--线上
 */

public class GoodDetailsSpecialIndexPresenter extends Presenter {

    private IOnlineGoodsSpecialIndexView iOnlineGoodsView;
    private OnlineGoodsRepository goodsRepository;
    private StoreIndexRepository storeIndexRepository;

    public GoodDetailsSpecialIndexPresenter(Context context, IOnlineGoodsSpecialIndexView iOnlineGoodsView) {
        super(context);
        this.iOnlineGoodsView = iOnlineGoodsView;
        goodsRepository = new OnlineGoodsRepository();
        storeIndexRepository = new StoreIndexRepository();
    }

    @Override
    public void onDestroy() {
        if (goodsRepository != null)
            goodsRepository.cancel();
        if (storeIndexRepository != null)
            storeIndexRepository.cancel();
    }

    /**
     * 获取商品列表
     */
    public void getGoodsSpecialDetailsList(String userId, String id) {
        goodsRepository.getGoodsSpecialDetailsList(userId, id, new BaseRepository.HttpCallListener<OnlineGoodsSpecialDetailsResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iOnlineGoodsView != null)
                    iOnlineGoodsView.onGoodsDetailsSpecialFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(OnlineGoodsSpecialDetailsResult data) {
                if (iOnlineGoodsView != null) {
                    iOnlineGoodsView.onGoodsDetailsSpecialSuccess(data);
                }
            }
        });
    }
    /**
     * 关注商品
     */
    public void onGoodsAddFavorite(String userId, String id,String type) {
        storeIndexRepository.onStoreAddFavorite(userId, id,type, new BaseRepository.HttpCallListener<FuturePayApiResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iOnlineGoodsView != null)
                    iOnlineGoodsView.onCollectFail(msg);
            }

            @Override
            public void onHttpCallSuccess(FuturePayApiResult futurePayApiResult) {
                iOnlineGoodsView.onCollectSuccess(1,futurePayApiResult);
            }
        });

    }

    /**
     * 取消关注
     */
    public void onnGoodsCancelFavorite(String userId, String id) {
        storeIndexRepository.onStoreCancelFavorite(userId, id, new BaseRepository.HttpCallListener<FuturePayApiResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iOnlineGoodsView != null)
                    iOnlineGoodsView.onCollectFail(msg);
            }

            @Override
            public void onHttpCallSuccess(FuturePayApiResult futurePayApiResult) {
                iOnlineGoodsView.onCollectSuccess(2,futurePayApiResult);
            }
        });
    }



}
