package com.futuretongfu.iview;

import com.futuretongfu.bean.onlinegoods.StoreIndexGoodsResult;

/**
 * 添加收藏
 *
 * @author DoneYang 2017/6/28
 */

public interface IStoreIndexGoodsView extends IView {

    void onStoreGoodsFail(int code, String msg);
    void onStoreGoodsSuccess(StoreIndexGoodsResult data);

    void onStoreGoodsUpLoadFail(int code, String msg);
    void onStoreGoodsUpLoadSuccess(StoreIndexGoodsResult data);

    void onStoreGoodsUpLoadNoDatas();
}
