package com.futuretongfu.iview;

import com.futuretongfu.bean.onlinegoods.GoodsCommentDataBean;

import java.util.List;

/**
 * 类:   IOnlineGoodsView
 * 描述:  线上商品
 * 作者： zhanggf on 2018/5/22
 */
public interface IOnlineGoodsCommentView {
    //获取商品评论成功

    public void onGoodsCommentDnUpdateSuccess(List<GoodsCommentDataBean> datas);
    public void onGoodsCommentDnUpdateFaile(String msg);

    public void onGoodsCommentUpLoadSuccess(List<GoodsCommentDataBean> datas);
    public void onGoodsCommentUpLoadFaile(String msg);
    public void onGoodsCommentUpLoadNoDatas();


}
