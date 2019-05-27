package com.futuretongfu.presenter.goods;

import android.content.Context;
import android.util.Log;

import com.futuretongfu.bean.PaySetMoneyBean;
import com.futuretongfu.bean.ShoppingGoodBeans;
import com.futuretongfu.bean.classification.ClassOneListViewBean;
import com.futuretongfu.bean.onlinegoods.HomeSortBean;
import com.futuretongfu.bean.onlinegoods.OnlinePayBean;
import com.futuretongfu.iview.IClassDetailsView;
import com.futuretongfu.iview.IFirmOrderView;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.entity.WareHorseAddressEntity;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.ClassGoodsRepository;
import com.futuretongfu.model.repository.HomeRepository;
import com.futuretongfu.model.repository.OnlineShoppingRepository;
import com.futuretongfu.model.repository.OrderOnlineRepository;
import com.futuretongfu.presenter.Presenter;

import java.util.List;


/**
 * Created by zhanggf on 2018/5/28.
 * 确认订单
 */

public class FirmOrderPresenter extends Presenter {

    private IFirmOrderView iFirmOrderView;
    private OnlineShoppingRepository shoppingRepository;
    private OrderOnlineRepository orderConsumerRepository;

    public FirmOrderPresenter(Context context, IFirmOrderView iFirmOrderView) {
        super(context);
        this.iFirmOrderView = iFirmOrderView;
        shoppingRepository = new OnlineShoppingRepository();
        this.orderConsumerRepository = new OrderOnlineRepository();
    }

    @Override
    public void onDestroy() {
        if (shoppingRepository != null)
            shoppingRepository.cancel();
        if (orderConsumerRepository != null)
            orderConsumerRepository.cancel();
    }

    /**
     * 确认订单
     */
    public void ShopFirmOrder(String userId, String onlineStoreId,String real_cash,String format,String addressId,String amount,
                                     String price,String totalPrice,double totalMoney,double realMoney,String leaveMessage,
                                     String skuId) {
        shoppingRepository.ShoppingFirmOrder(userId, onlineStoreId,real_cash ,format,addressId,amount,price,
                totalPrice,totalMoney,realMoney,leaveMessage,skuId,new BaseRepository.HttpCallListener<String>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iFirmOrderView != null)
                    iFirmOrderView.onFirmOrderFail(msg);
            }

            @Override
            public void onHttpCallSuccess(String futurePayApiResult) {
                iFirmOrderView.onFirmOrderSuccess(futurePayApiResult);
            }
        });

    }
    /**
     * 确认订单
     */
    public void ShopFirm(int type,String userId, String onlineStoreId, String totalMoney, String bussMoney, String platMoney, String alipayMoney,
                         String terminal, String leaveMessage, String logisticsFee, String realMoney,
                         String realCash, String addressId, List<ShoppingGoodBeans.SkuListBean> skuList) {
        shoppingRepository.ShoppingFirm(type,userId, onlineStoreId,totalMoney ,bussMoney,platMoney,alipayMoney,
                terminal,leaveMessage,logisticsFee,realMoney,realCash,addressId,skuList,new BaseRepository.HttpCallListener<String>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if (iFirmOrderView != null)
                            iFirmOrderView.onFirmOrderFail(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(String futurePayApiResult) {
                       iFirmOrderView.onFirmOrderSuccess(futurePayApiResult);
                   }
                });

    }

    /**
     * 获取收获地址列表
     */
    public void getWareAddressList(String userId) {
        shoppingRepository.getAddressList(userId, new BaseRepository.HttpCallListener<List<WareHorseAddressEntity>>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iFirmOrderView != null)
                    iFirmOrderView.onWareAddressFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(List<WareHorseAddressEntity> data) {
                if(iFirmOrderView != null) {
                    iFirmOrderView.onWareAddressSuccess(data);
                }
            }
        });
    }

    /**
     * 支付
     */
    public void onPayMent(int type,String userId, String storeId, String onlineOrderNo) {
        orderConsumerRepository.onAlipayPay(type,userId, storeId, onlineOrderNo,new BaseRepository.HttpCallListener<String>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iFirmOrderView != null) {
                    iFirmOrderView.onPaymentFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(String data) {
                Log.e("支付成功",data);

                if (iFirmOrderView != null) {
                    iFirmOrderView.onPaymentSuccess(data);
                }
            }
        });
    }

    /**
     * 支付余额
     */
    public void onPaymentSetBalance(String userId, String onlineOrderNo, String payPwd) {
        orderConsumerRepository.onPaymentSetBalance(userId, onlineOrderNo, payPwd,new BaseRepository.HttpCallListener<PaySetMoneyBean>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iFirmOrderView != null) {
                    iFirmOrderView.onPaymentSetBalanceFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(PaySetMoneyBean data) {
                if (iFirmOrderView != null) {
                    iFirmOrderView.onPaymentSetBalanceSuccess(data);
                }
            }
        });
    }


}
