package com.futuretongfu.presenter.goods;

import android.content.Context;

import com.futuretongfu.bean.onlinegoods.GoodsBrandBean;
import com.futuretongfu.bean.onlinegoods.GoodsDataBean;
import com.futuretongfu.bean.onlinegoods.GoodsSearchDataBean;
import com.futuretongfu.bean.onlinegoods.GoodsSearchTermBean;
import com.futuretongfu.iview.IOnlineSearchGoodsView;
import com.futuretongfu.model.entity.FavoriteList;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.OnlineGoodsRepository;
import com.futuretongfu.presenter.Presenter;

import java.util.List;


/**
 * Created by zhanggf on 2018/5/18.
 * 搜索商品
 */

public class SearchGoodsPresenter extends Presenter {

    private IOnlineSearchGoodsView iOnlineSearchGoodsView;
    private OnlineGoodsRepository goodsRepository;
    private int page = 1;
    public SearchGoodsPresenter(Context context, IOnlineSearchGoodsView iOnlineSearchGoodsView) {
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
     * 获取品牌
     */
    public void onSearchBrandList(String categoryId) {
        goodsRepository.getSearchBrandList(categoryId,new BaseRepository.HttpCallListener<List<GoodsBrandBean>>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iOnlineSearchGoodsView != null) {
                    iOnlineSearchGoodsView.onSearchBrandListViewFaile( msg);
                }
            }

            @Override
            public void onHttpCallSuccess(List<GoodsBrandBean> data) {
                if (iOnlineSearchGoodsView != null) {
                    iOnlineSearchGoodsView.onSearchBrandListViewSuccess(data);
                }
            }
        });
    }

    /**
     * 获取搜索条件
     */
    public void onSearchTermList() {
        goodsRepository.getSearchTermList(new BaseRepository.HttpCallListener<List<GoodsSearchTermBean>>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iOnlineSearchGoodsView != null) {
                    iOnlineSearchGoodsView.onSearchTermListViewFaile( msg);
                }
            }

            @Override
            public void onHttpCallSuccess(List<GoodsSearchTermBean> data) {
                if (iOnlineSearchGoodsView != null) {
                    iOnlineSearchGoodsView.onSearchTermListViewSuccess(data);
                }
            }
        });
    }


    /**
     *搜索商品列表
     * 下拉刷新
     */
    public void onSearchGoodsList(String proName,int mark,String lowestPrice,String highestPrice,String brandId,
                                  List<GoodsSearchTermBean> termBeans,String categoryId) {
        page = 1;
        goodsRepository.getSearchGoodsList(proName,mark,lowestPrice,highestPrice,brandId,termBeans,categoryId,page,new BaseRepository.HttpCallListener<List<GoodsSearchDataBean>>() {
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
    public void onSearchGoodsListUpLoad(String proName,int mark,String lowestPrice,String highestPrice,String brandId,
                                   List<GoodsSearchTermBean> termBeans,String categoryId){
        page++;
        goodsRepository.getSearchGoodsList(
                proName,mark,lowestPrice,highestPrice,brandId,termBeans,categoryId,page
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
