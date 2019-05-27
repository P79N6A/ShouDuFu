package com.futuretongfu.presenter.goods;

import android.content.Context;

import com.futuretongfu.bean.onlinegoods.GoodsCommentDataBean;
import com.futuretongfu.iview.IOnlineGoodsCommentView;
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

public class GoodCommentPresenter extends Presenter {

    private IOnlineGoodsCommentView iOnlineGoodsView;
    private OnlineGoodsRepository goodsRepository;
    private int page;

    public GoodCommentPresenter(Context context, IOnlineGoodsCommentView iOnlineGoodsView) {
        super(context);
        this.iOnlineGoodsView = iOnlineGoodsView;
        goodsRepository = new OnlineGoodsRepository();
    }

    @Override
    public void onDestroy() {
        if (goodsRepository != null)
            goodsRepository.cancel();
    }

    /**
     * 获取商品列表
     */
    public void getGoodCommentList(String skuId) {
        page = 1;
        goodsRepository.getGoodsCommentList(skuId, page,new BaseRepository.HttpCallListener<List<GoodsCommentDataBean>>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iOnlineGoodsView != null)
                    iOnlineGoodsView.onGoodsCommentDnUpdateFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(List<GoodsCommentDataBean> data) {
                if(iOnlineGoodsView != null) {
                    iOnlineGoodsView.onGoodsCommentDnUpdateSuccess(data);
                }
            }
        });
    }

    /**
     * 获取商品列表
     */
    public void getGoodCommentUpLoadList(String skuId) {
        page ++;
        goodsRepository.getGoodsCommentList(skuId, page,new BaseRepository.HttpCallListener<List<GoodsCommentDataBean>>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iOnlineGoodsView != null)
                    iOnlineGoodsView.onGoodsCommentUpLoadFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(List<GoodsCommentDataBean> data) {
                if(iOnlineGoodsView != null) {
                    if(data == null || data.size() < 1) {
                        iOnlineGoodsView.onGoodsCommentUpLoadNoDatas();
                    }
                    else{
                        iOnlineGoodsView.onGoodsCommentUpLoadSuccess(data);
                    }
                }
            }
        });
    }


}
