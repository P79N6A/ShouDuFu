package com.futuretongfu.iview;

import com.futuretongfu.bean.StoreBean;

import java.util.List;

/**
 * @author DoneYang 2017/7/1
 */

public interface SearchStoreIView extends IView {
    void onSearchStoreFail(int code,String msg);
    void onSearchStoreSuccess(List<StoreBean> data);

    //加载更多
    void onSearchStoreMoreSuccess(List<StoreBean> data);
    void onSearchStoreMoreFail(int code, String msg);
}
