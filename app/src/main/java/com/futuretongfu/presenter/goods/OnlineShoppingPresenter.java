package com.futuretongfu.presenter.goods;

import android.content.Context;

import com.futuretongfu.bean.onlinegoods.ShoppingGroupBean;
import com.futuretongfu.iview.IOnlineShoppingView;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.OnlineShoppingRepository;
import com.futuretongfu.presenter.Presenter;

import java.util.List;


/**
 * 线上商城--购物车
 * @author zhanggf 2018/05/24
 */

public class OnlineShoppingPresenter extends Presenter {

    private IOnlineShoppingView iOnlineShoppingView;
    private OnlineShoppingRepository shoppingRepository;

    public OnlineShoppingPresenter(Context context, IOnlineShoppingView iOnlineShoppingView) {
        super(context);
        this.iOnlineShoppingView = iOnlineShoppingView;
        shoppingRepository = new OnlineShoppingRepository();
    }

    @Override
    public void onDestroy() {
        if (shoppingRepository != null)
            shoppingRepository.cancel();
    }

    /**
     * 获取购物车列表
     */
    public void getOnlineShoppingList(String userId) {
        shoppingRepository.getOnlineShoppingList(userId, new BaseRepository.HttpCallListener<List<ShoppingGroupBean>>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iOnlineShoppingView != null)
                    iOnlineShoppingView.onOnlineShoppingListFail(msg);
            }

            @Override
            public void onHttpCallSuccess(List<ShoppingGroupBean> data) {
                if(iOnlineShoppingView != null) {
                    iOnlineShoppingView.onOnlineShoppingListSuccess(data);
                }
            }
        });
    }
    
    /**
     * 删除购物车
     */
    public void deleteOnlineShopping(String userId, String id) {
        shoppingRepository.ShoppingGoodDelete(userId, id, new BaseRepository.HttpCallListener<FuturePayApiResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iOnlineShoppingView != null)
                    iOnlineShoppingView.onOnlineShoppingDeleteFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(FuturePayApiResult futurePayApiResult) {
                iOnlineShoppingView.onOnlineShoppingDeleteSuccess(futurePayApiResult);
            }
        });

    }
    /**
     * 修改购物车商品数量
     */
    public void updateOnlineShopping(final int type, final int count, final int groupPosition, final int childPosition, String userId, String id, int num) {
        shoppingRepository.ShoppingGoodUpdate(userId,id, num,
                new BaseRepository.HttpCallListener<FuturePayApiResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iOnlineShoppingView != null)
                    iOnlineShoppingView.onOnlineShoppingUpdateFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(FuturePayApiResult futurePayApiResult) {
                iOnlineShoppingView.onOnlineShoppingUpdateSuccess(type,count,groupPosition,childPosition,futurePayApiResult);
            }
        });
    }

}
