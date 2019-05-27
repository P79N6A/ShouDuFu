package com.futuretongfu.iview;

import com.futuretongfu.bean.StoreBean;

import java.util.List;

/**
 * @author DoneYang 2017/6/25
 */

public interface NearbyStoreView extends IView {

    void onNearbyStoreSuccess(List<StoreBean> data);

    void onNearbyStoreFail(int code, String msg);

    void onNearbyStoreMoreSuccess(List<StoreBean> data);

    void onNearbyStoreMoreFail(int code, String msg);


}
