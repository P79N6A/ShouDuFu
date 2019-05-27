package com.futuretongfu.iview;

import com.futuretongfu.bean.onlinegoods.HomeSortBean;

import java.util.List;


/**
 * 类:   IClassGoodsView
 * 描述:  三级分类
 */
public interface IClassDetailsView {
    //首页分类
    void onSortListFail(int code, String msg);

    void onSortListSuccess(List<HomeSortBean> data);

}
