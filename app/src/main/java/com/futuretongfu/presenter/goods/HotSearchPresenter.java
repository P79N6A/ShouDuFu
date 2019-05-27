package com.futuretongfu.presenter.goods;

import android.content.Context;

import com.futuretongfu.bean.onlinegoods.GoodsBrandBean;
import com.futuretongfu.bean.onlinegoods.GoodsDataBean;
import com.futuretongfu.bean.onlinegoods.HotSearchBean;
import com.futuretongfu.iview.IOnlineHotSearchView;
import com.futuretongfu.iview.IOnlineSearchGoodsView;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.OnlineGoodsRepository;
import com.futuretongfu.presenter.Presenter;

import java.util.List;


/**
 * Created by zhanggf on 2018/5/18.
 * 搜索商品
 */

public class HotSearchPresenter extends Presenter {

    private IOnlineHotSearchView iOnlineHotSearchView;
    private OnlineGoodsRepository goodsRepository;

    public HotSearchPresenter(Context context, IOnlineHotSearchView iOnlineHotSearchView) {
        super(context);
        this.iOnlineHotSearchView = iOnlineHotSearchView;
        goodsRepository = new OnlineGoodsRepository();
    }

    @Override
    public void onDestroy() {
        if (goodsRepository != null)
            goodsRepository.cancel();
    }

    /**
     * 热搜榜
     */
    public void onHotSearchList() {
        goodsRepository.getHotSearchList(new BaseRepository.HttpCallListener<List<HotSearchBean>>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iOnlineHotSearchView != null) {
                    iOnlineHotSearchView.onHotSearchFaile( msg);
                }
            }

            @Override
            public void onHttpCallSuccess(List<HotSearchBean> data) {
                if (iOnlineHotSearchView != null) {
                    iOnlineHotSearchView.onHotSearchSuccess(data);
                }
            }
        });
    }



}
