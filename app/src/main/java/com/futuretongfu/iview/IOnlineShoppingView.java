package com.futuretongfu.iview;

import com.futuretongfu.bean.onlinegoods.ShoppingGroupBean;
import com.futuretongfu.model.entity.FuturePayApiResult;

import java.util.List;

/**
 * 线上商城--购物车
 * @author zhanggf 2018/05/24
 */

public interface IOnlineShoppingView extends IView {

    //获取购物车列表
    void onOnlineShoppingListSuccess(List<ShoppingGroupBean> data);
    void onOnlineShoppingListFail(String msg);

    //修改购物车成功
    public void onOnlineShoppingUpdateSuccess(int type,int count,int groupPosition,int childPosition,FuturePayApiResult futurePayApiResult);
    public void onOnlineShoppingUpdateFaile(String msg);

    //删除购物车成功
    public void onOnlineShoppingDeleteSuccess(FuturePayApiResult futurePayApiResult);
    public void onOnlineShoppingDeleteFaile(String msg);

}
