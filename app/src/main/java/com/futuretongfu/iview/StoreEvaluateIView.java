package com.futuretongfu.iview;

import com.futuretongfu.bean.StoreEvaluateBean;

import java.util.List;

/**
 * 商家评价
 *
 * @author DoneYang 2017/7/1
 */

public interface StoreEvaluateIView extends IView {

    void onStoreEvaluateFail(int code, String msg);

    void onStoreEvaluateSuccess(List<StoreEvaluateBean> data);

    void onStoreEvaluateMoreSuccess(List<StoreEvaluateBean> data);

    void onStoreEvaluateMoreFail(int code, String msg);
}
