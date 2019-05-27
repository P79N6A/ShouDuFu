package com.futuretongfu.iview;

import com.futuretongfu.base.BaseSerializable;

/**
 * 商家详情
 *
 * @author DoneYang 2017/6/27
 */

public interface StoreDetailsIView extends IView {

    void onStoreDetailsFail(int code, String msg);

    void onStoreDetailsSuccess(BaseSerializable data);

    void onStoreBindFail(int code, String msg);

    void onStoreBindSuccess(String data,String data1);

}
