package com.futuretongfu.iview;

import com.futuretongfu.bean.classification.ClassOneListViewBean;

import java.util.List;


/**
 * 类:   IClassGoodsView
 * 描述:  三级分类
 */
public interface IClassGoodsView {
    //获取列表成功
    void onClassGoodsListViewSuccess(List<ClassOneListViewBean> data);

    void onClassGoodsListViewFaile(String msg);

    //最后更新时间
    void onClassLastModifyTimeFail(int code, String msg);

    void gonClassLastModifyTimeSuccess(String data);
}
