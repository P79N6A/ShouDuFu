package com.futuretongfu.iview;

import com.futuretongfu.bean.onlinegoods.HotSearchBean;

import java.util.List;

/**
 * Created by zhanggf on 2018/5/18.
 */

public interface IOnlineHotSearchView {

    //搜索商品分类
    void onHotSearchSuccess(List<HotSearchBean> data);

    void onHotSearchFaile(String msg);
}
