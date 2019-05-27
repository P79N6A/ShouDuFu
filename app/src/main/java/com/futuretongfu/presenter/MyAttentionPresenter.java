package com.futuretongfu.presenter;

import android.content.Context;

import com.futuretongfu.bean.CollectionBean;
import com.futuretongfu.bean.onlinegoods.OnlineFavoriteBean;
import com.futuretongfu.iview.IMyAttentionView;
import com.futuretongfu.iview.IMyCollectionView;
import com.futuretongfu.model.entity.FavoriteList;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.StoreIndexRepository;
import com.futuretongfu.model.repository.WlsqRepository;

import java.util.List;


/**
 * Created by zhanggf on 2018/3/28.
 * 关注店铺
 */

public class MyAttentionPresenter extends Presenter{

    private IMyAttentionView iMyAttentionView;
    private StoreIndexRepository storeIndexRepository;
    private int page = 1;
    private long userID;

    public MyAttentionPresenter(Context context, IMyAttentionView iMyAttentionView){
        super(context);
        this.iMyAttentionView = iMyAttentionView;
        this.storeIndexRepository = new StoreIndexRepository();
        userID = UserManager.getInstance().getUserId();
    }

    @Override
    public void onDestroy(){
        if(storeIndexRepository != null)
            storeIndexRepository.cancel();
    }

    //下拉刷新
    public void favoriteListDnUpdate(String type){
        page = 1;
        storeIndexRepository.favoriteList(
                  userID
                , page,type
                , new BaseRepository.HttpCallListener<List<OnlineFavoriteBean>>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iMyAttentionView != null)
                            iMyAttentionView.onMyAttentionDnUpdateFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(List<OnlineFavoriteBean> data) {
                        if(iMyAttentionView != null)
                            iMyAttentionView.onMyAttentionDnUpdateSuccess(data);
                    }
                }

        );
    }

    //上拉加载
    public void favoriteListUpLoad(String type){
        page++;
        storeIndexRepository.favoriteList(
                  userID
                , page,type
                , new BaseRepository.HttpCallListener<List<OnlineFavoriteBean>>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iMyAttentionView != null)
                            iMyAttentionView.onMyAttentionUpLoadFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(List<OnlineFavoriteBean> data) {
                        if(iMyAttentionView != null) {
                            if(data == null || data.size() < 1) {
                                iMyAttentionView.onMyAttentionUpLoadNoDatas();
                            }
                            else{
                                iMyAttentionView.onMyAttentionUpLoadSuccess(data);
                            }
                        }
                    }
                }

        );
    }

    /**
     * 取消收藏
     */
    public void onDelCollect(final OnlineFavoriteBean item){
        storeIndexRepository.onStoreCancelFavorite2(
                UserManager.getInstance().getUserId() + ""
                , item.getTargetId() + ""
                , new BaseRepository.HttpCallListener<CollectionBean>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iMyAttentionView != null)
                            iMyAttentionView.onMyAttentionDeleteFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(CollectionBean data) {
                        if(iMyAttentionView != null)
                            iMyAttentionView.onMyAttentionDeleteSuccess(item);
                    }
                }
        );
    }
}
