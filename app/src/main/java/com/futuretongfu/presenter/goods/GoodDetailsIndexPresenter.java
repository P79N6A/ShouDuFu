package com.futuretongfu.presenter.goods;

import android.content.Context;

import com.futuretongfu.bean.onlinegoods.GoodsAttrValuesList;
import com.futuretongfu.iview.IOnlineGoodsIndexView;
import com.futuretongfu.iview.IOnlineGoodsView;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.entity.OnlineGoodsDetailsResult;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.OnlineGoodsRepository;
import com.futuretongfu.model.repository.StoreIndexRepository;
import com.futuretongfu.presenter.Presenter;

import java.util.List;


/**
 * Created by zhanggf on 2018/5/10.
 * 商品详情--线上
 */

public class GoodDetailsIndexPresenter extends Presenter {

    private IOnlineGoodsIndexView iOnlineGoodsView;
    private OnlineGoodsRepository goodsRepository;
    private StoreIndexRepository storeIndexRepository;

    public GoodDetailsIndexPresenter(Context context, IOnlineGoodsIndexView iOnlineGoodsView) {
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
    public void getGoodsDetailsList(String userId, String id) {
        goodsRepository.getGoodsDetailsList(userId, id, new BaseRepository.HttpCallListener<OnlineGoodsDetailsResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iOnlineGoodsView != null)
                    iOnlineGoodsView.onGoodsDetailsFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(OnlineGoodsDetailsResult data) {
                if (iOnlineGoodsView != null) {
                    iOnlineGoodsView.onGoodsDetailsSuccess(data);
                }
            }
        });
    }

}
