package com.futuretongfu.iview;

import com.futuretongfu.bean.HomeBannerBean;
import com.futuretongfu.bean.StoreBean;
import com.futuretongfu.bean.WlsqTypeBean;

import java.util.List;

/**
 * @author DoneYang 2017/6/24
 */

public interface WlsqIView extends IView {

    //行业type
    void WlsqTypeSuccess(List<WlsqTypeBean> data);

    void WlsqTypeFail(int code, String msg);

    //附近商家
    void onNearbyStoreSuccess(List<StoreBean> data);

    void onNearbyStoreFail(int code, String msg);

    //附近商家-加载更多
    void onNearbyStoreMoreSuccess(List<StoreBean> data);

    void onNearbyStoreMoreFail(int code, String msg);

    //banner
    void onBannerListFail(int code, String msg);

    void onBannerListSuccess(List<HomeBannerBean> data);


}
