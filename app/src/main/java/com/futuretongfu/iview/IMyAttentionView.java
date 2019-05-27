package com.futuretongfu.iview;

import com.futuretongfu.bean.onlinegoods.OnlineFavoriteBean;
import com.futuretongfu.model.entity.FavoriteList;

import java.util.List;

/**
 * Created by ChenXiaoPeng on 2017/6/28.
 */

public interface IMyAttentionView {

    public void onMyAttentionDnUpdateSuccess(List<OnlineFavoriteBean> datas);
    public void onMyAttentionDnUpdateFaile(String msg);

    public void onMyAttentionUpLoadSuccess(List<OnlineFavoriteBean> datas);
    public void onMyAttentionUpLoadFaile(String msg);
    public void onMyAttentionUpLoadNoDatas();

    public void onMyAttentionDeleteSuccess(OnlineFavoriteBean item);
    public void onMyAttentionDeleteFaile(String msg);

}
