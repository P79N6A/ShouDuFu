package com.futuretongfu.iview;

import com.futuretongfu.model.entity.FavoriteList;

import java.util.List;

/**
 * Created by ChenXiaoPeng on 2017/6/28.
 */

public interface IMyCollectionView {

    public void onMyCollectionDnUpdateSuccess(List<FavoriteList> datas);
    public void onMyCollectionDnUpdateFaile(String msg);

    public void onMyCollectionUpLoadSuccess(List<FavoriteList> datas);
    public void onMyCollectionUpLoadFaile(String msg);
    public void onMyCollectionUpLoadNoDatas();

    public void onMyCollectionDeleteSuccess(FavoriteList item);
    public void onMyCollectionDeleteFaile(String msg);

}
