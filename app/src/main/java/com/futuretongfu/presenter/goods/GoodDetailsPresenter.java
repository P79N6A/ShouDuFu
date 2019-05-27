package com.futuretongfu.presenter.goods;

import android.content.Context;

import com.futuretongfu.bean.onlinegoods.GoodsAttrValuesList;
import com.futuretongfu.iview.IOnlineGoodsView;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.entity.OnlineGoodsDetailsResult;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.OnlineGoodsRepository;
import com.futuretongfu.model.repository.StoreIndexRepository;
import com.futuretongfu.presenter.Presenter;

import java.util.List;


/**
 * Created by zhanggf on 2018/5/10.
 * 商品详情--线上
 */

public class GoodDetailsPresenter extends Presenter {

    private IOnlineGoodsView iOnlineGoodsView;
    private OnlineGoodsRepository goodsRepository;
    private StoreIndexRepository storeIndexRepository;

    public GoodDetailsPresenter(Context context, IOnlineGoodsView iOnlineGoodsView) {
        super(context);
        this.iOnlineGoodsView = iOnlineGoodsView;
        goodsRepository = new OnlineGoodsRepository();
        storeIndexRepository = new StoreIndexRepository();
    }

    @Override
    public void onDestroy() {
        if (goodsRepository != null)
            goodsRepository.cancel();
        if (storeIndexRepository != null)
            storeIndexRepository.cancel();
    }

    /**
     * 获取商品列表
     */
    public void getGoodsDetailsList(String userId, String id) {
        goodsRepository.getGoodsDetailsList(userId, id, new BaseRepository.HttpCallListener<OnlineGoodsDetailsResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iOnlineGoodsView != null)
                    iOnlineGoodsView.onGoodsDetailsFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(OnlineGoodsDetailsResult data) {
                if (iOnlineGoodsView != null) {
                    iOnlineGoodsView.onGoodsDetailsSuccess(data);
                }
            }
        });
    }

    /**
     * 获取商品列表
     */
    public void getGoodsAttrInfoList(final int type, String spuId, String storeId) {
        goodsRepository.getGoodsAttrInfo(spuId, storeId, new BaseRepository.HttpCallListener<List<GoodsAttrValuesList>>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iOnlineGoodsView != null)
                    iOnlineGoodsView.onGoodsAtterInfoFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(List<GoodsAttrValuesList> data) {
                if (iOnlineGoodsView != null) {
                    iOnlineGoodsView.onGoodsAtterInfoSuccess(type,data);
                }
            }
        });
    }


    /**
     * 根据选择属性查找sku信息接口
     */
    public void getGoodsAttrSearchGoodsList(final String formatInfo, String spuId, String storeId, String attributeId, String attributeValueId) {
        goodsRepository.getGoodsAttrSearchGoodsInfo(spuId, storeId, attributeId, attributeValueId,
        new BaseRepository.HttpCallListener<List<OnlineGoodsDetailsResult>>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iOnlineGoodsView != null)
                    iOnlineGoodsView.onGoodsAtterInfoSearchFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(List<OnlineGoodsDetailsResult> data) {
                if (iOnlineGoodsView != null) {
                    iOnlineGoodsView.onGoodsAtterInfoSearchSuccess(formatInfo,data);
                }
            }
        });
    }


    /**
     * 关注商品
     */
    public void onGoodsAddFavorite(String userId, String id,String type) {
        storeIndexRepository.onStoreAddFavorite(userId, id,type, new BaseRepository.HttpCallListener<FuturePayApiResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iOnlineGoodsView != null)
                    iOnlineGoodsView.onCollectFail(msg);
            }

            @Override
            public void onHttpCallSuccess(FuturePayApiResult futurePayApiResult) {
                iOnlineGoodsView.onCollectSuccess(1,futurePayApiResult);
            }
        });

    }

    /**
     * 取消关注
     */
    public void onnGoodsCancelFavorite(String userId, String id) {
        storeIndexRepository.onStoreCancelFavorite(userId, id, new BaseRepository.HttpCallListener<FuturePayApiResult>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iOnlineGoodsView != null)
                    iOnlineGoodsView.onCollectFail(msg);
            }

            @Override
            public void onHttpCallSuccess(FuturePayApiResult futurePayApiResult) {
                iOnlineGoodsView.onCollectSuccess(2,futurePayApiResult);
            }
        });
    }

    /**
     * 添加购物车
     */
    public void addOnlineShopping(String userId,String id, int num,String format,String StoreId,String mony) {
        goodsRepository.ShoppingGoodAdd(userId,id,num,format,StoreId,mony,
                new BaseRepository.HttpCallListener<FuturePayApiResult>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if (iOnlineGoodsView != null)
                            iOnlineGoodsView.onOnlineShoppingAddFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(FuturePayApiResult data) {
                        if(iOnlineGoodsView != null) {
                            iOnlineGoodsView.onOnlineShoppingAddSuccess(data);
                        }
                    }
                });
    }



}
