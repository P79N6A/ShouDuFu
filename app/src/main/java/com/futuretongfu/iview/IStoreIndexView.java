package com.futuretongfu.iview;

import com.futuretongfu.bean.StoreOnLineDetailsBean;
import com.futuretongfu.model.entity.FuturePayApiResult;

/**
 * 添加收藏
 *
 * @author DoneYang 2017/6/28
 */

public interface IStoreIndexView extends IView {

    void onStoreDetailsFail(int code, String msg);

    void onStoreDetailsSuccess(StoreOnLineDetailsBean data);

    void onCollectFail(String msg);

    void onCollectSuccess(int type,FuturePayApiResult futurePayApiResult);
}
