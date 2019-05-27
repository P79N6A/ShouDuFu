package com.futuretongfu.presenter.goods;

import android.content.Context;

import com.futuretongfu.bean.onlinegoods.GoodsBrandBean;
import com.futuretongfu.bean.onlinegoods.GoodsSearchDataBean;
import com.futuretongfu.bean.onlinegoods.GoodsSearchTermBean;
import com.futuretongfu.iview.IOnlineSearchGoodsView;
import com.futuretongfu.iview.IOnlineSearchStoreGoodsView;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.OnlineGoodsRepository;
import com.futuretongfu.presenter.Presenter;

import java.util.List;


/**
 * Created by zhanggf on 2018/5/18.
 * 搜索店铺内商品
 */

public class SearchStoreGoodsPresenter extends Presenter {

    private IOnlineSearchStoreGoodsView iOnlineSearchGoodsView;
    private OnlineGoodsRepository goodsRepository;
    private int page = 1;
    public SearchStoreGoodsPresenter(Context context, IOnlineSearchStoreGoodsView iOnlineSearchGoodsView) {
        super(context);
        this.iOnlineSearchGoodsView = iOnlineSearchGoodsView;
        goodsRepository = new OnlineGoodsRepository();
    }

    @Override
    public void onDestroy() {
        if (goodsRepository != null)
            goodsRepository.cancel();
    }


    /**
     *搜索商品列表
     * 下拉刷新
     */
    public void onSearchGoodsList(String proName,String onlineStoreId) {
        page = 1;
        goodsRepository.getSearchStoreGoodsList(proName,onlineStoreId,page,new BaseRepository.HttpCallListener<List<GoodsSearchDataBean>>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iOnlineSearchGoodsView != null) {
                    iOnlineSearchGoodsView.onSearchGoodsListViewDnUpdateFaile(msg);
                }
            }

            @Override
            public void onHttpCallSuccess(List<GoodsSearchDataBean> data) {
                if (iOnlineSearchGoodsView != null) {
                    iOnlineSearchGoodsView.onSearchGoodsListViewDnUpdateSuccess(data);
                }
            }
        });
    }

    //上拉加载
    public void onSearchGoodsListUpLoad(String proName,String onlineStoreId){
        page++;
        goodsRepository.getSearchStoreGoodsList(
                proName,onlineStoreId,page
                , new BaseRepository.HttpCallListener<List<GoodsSearchDataBean>>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iOnlineSearchGoodsView != null)
                            iOnlineSearchGoodsView.onSearchGoodsListDnUpdateUpLoadFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(List<GoodsSearchDataBean> data) {
                        if(iOnlineSearchGoodsView != null) {
                            if(data == null || data.size() < 1) {
                                iOnlineSearchGoodsView.onSearchGoodsListUpLoadNoDatas();
                            }
                            else{
                                iOnlineSearchGoodsView.onSearchGoodsListDnUpdateUpLoadSuccess(data);
                            }
                        }
                    }
                }

        );
    }


}
