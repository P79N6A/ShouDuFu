package com.futuretongfu.presenter;

import android.content.Context;

import com.futuretongfu.bean.CollectionBean;
import com.futuretongfu.iview.IMyCollectionView;
import com.futuretongfu.model.entity.FavoriteList;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.WlsqRepository;

import java.util.List;

/**
 * Created by ChenXiaoPeng on 2017/6/28.
 */

public class MyCollectionPresenter extends Presenter{

    private IMyCollectionView iMyCollectionView;
    private WlsqRepository wlsqRepository;
    private int page = 1;
    private long userID;

    public MyCollectionPresenter(Context context, IMyCollectionView iMyCollectionView){
        super(context);
        this.iMyCollectionView = iMyCollectionView;
        this.wlsqRepository = new WlsqRepository();
        userID = UserManager.getInstance().getUserId();
    }

    @Override
    public void onDestroy(){
        if(wlsqRepository != null)
            wlsqRepository.cancel();
    }

    //下拉刷新
    public void favoriteListDnUpdate(){
        page = 1;
        wlsqRepository.favoriteList(
                  userID
                , page
                , new BaseRepository.HttpCallListener<List<FavoriteList>>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iMyCollectionView != null)
                            iMyCollectionView.onMyCollectionDnUpdateFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(List<FavoriteList> data) {
                        if(iMyCollectionView != null)
                            iMyCollectionView.onMyCollectionDnUpdateSuccess(data);
                    }
                }

        );
    }

    //上拉加载
    public void favoriteListUpLoad(){
        page++;
        wlsqRepository.favoriteList(
                  userID
                , page
                , new BaseRepository.HttpCallListener<List<FavoriteList>>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iMyCollectionView != null)
                            iMyCollectionView.onMyCollectionUpLoadFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(List<FavoriteList> data) {
                        if(iMyCollectionView != null) {
                            if(data == null || data.size() < 1) {
                                iMyCollectionView.onMyCollectionUpLoadNoDatas();
                            }
                            else{
                                iMyCollectionView.onMyCollectionUpLoadSuccess(data);
                            }
                        }
                    }
                }

        );
    }

    /**
     * 取消收藏
     */
    public void onDelCollect(final FavoriteList item){
        wlsqRepository.onDelCollect(
                UserManager.getInstance().getUserId() + ""
                , item.getTargetId() + ""
                , new BaseRepository.HttpCallListener<CollectionBean>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iMyCollectionView != null)
                            iMyCollectionView.onMyCollectionDeleteFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(CollectionBean data) {
                        if(iMyCollectionView != null)
                            iMyCollectionView.onMyCollectionDeleteSuccess(item);
                    }
                }
        );
    }
}
