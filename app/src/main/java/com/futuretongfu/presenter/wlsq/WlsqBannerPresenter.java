package com.futuretongfu.presenter.wlsq;

import android.content.Context;

import com.amap.api.services.geocoder.GeocodeSearch;
import com.futuretongfu.bean.StoreBean;
import com.futuretongfu.model.repository.WlsqRepository;
import com.futuretongfu.bean.HomeBannerBean;
import com.futuretongfu.bean.WlsqTypeBean;
import com.futuretongfu.iview.WlsqIView;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.presenter.Presenter;

import java.util.List;

/**
 * @author DoneYang 2017/6/24
 */

public class WlsqBannerPresenter extends Presenter {

    private WlsqIView wlsqIView;
    private WlsqRepository wlsqRepository;

//    private WlsqBannerSubscriber wlsqBannerSubscriber;

    public WlsqBannerPresenter(Context context, WlsqIView iview) {
        super(context);
        this.wlsqIView = iview;
        this.wlsqRepository = new WlsqRepository();
    }

    @Override
    public void onDestroy() {
        if (wlsqRepository != null)
            wlsqRepository.cancel();
    }

    /**
     * 分类
     */
    public void onWlsqType() {

        wlsqRepository.onType(new BaseRepository.HttpCallListener<List<WlsqTypeBean>>() {

            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (wlsqIView != null) {
                    wlsqIView.WlsqTypeFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(List<WlsqTypeBean> data) {
                if (wlsqIView != null) {
                    wlsqIView.WlsqTypeSuccess(data);
                }
            }
        });

    }

    /**
     * 附近商家
     */
    public void onNearbyStore(int page
//            , int size
            , String longitude, String latitude, String city) {
        wlsqRepository.onNearbyStore(page,
//                size,
                longitude, latitude, city, new BaseRepository.HttpCallListener<List<StoreBean>>() {

                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if (wlsqIView != null) {
                            wlsqIView.onNearbyStoreFail(code, msg);
                        }
                    }

                    @Override
                    public void onHttpCallSuccess(List<StoreBean> data) {
                        if (wlsqIView != null) {
                            wlsqIView.onNearbyStoreSuccess(data);
                        }
                    }
                });
    }

    /**
     * 附近商家-加载更多
     */
    public void onNearbyStoreMore(int page
//            , int size
            , String longitude, String latitude, String city) {
        wlsqRepository.onNearbyStore(page,
//                size,
                longitude, latitude, city, new BaseRepository.HttpCallListener<List<StoreBean>>() {

                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if (wlsqIView != null) {
                            wlsqIView.onNearbyStoreMoreFail(code, msg);
                        }
                    }

                    @Override
                    public void onHttpCallSuccess(List<StoreBean> data) {
                        if (wlsqIView != null) {
                            wlsqIView.onNearbyStoreMoreSuccess(data);
                        }
                    }
                });
    }

    /**
     * banner
     */
    public void onBannerList() {
        wlsqRepository.onBanner(new BaseRepository.HttpCallListener<List<HomeBannerBean>>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (wlsqIView != null) {
                    wlsqIView.onBannerListFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(List<HomeBannerBean> data) {
                if (wlsqIView != null) {
                    wlsqIView.onBannerListSuccess(data);
                }
            }
        });
    }


//    private class WlsqBannerSubscriber extends Subscriber<Void> {
//
//        @Override
//        public void onCompleted() {
//
//        }
//
//        @Override
//        public void onError(Throwable e) {
//            if (wlsqBannerSubscriber != null) {
//                wlsqBannerSubscriber.unsubscribe();
//            }
//            if (wlsqBannerView != null) {
//                wlsqBannerView.onWlsqBannerViewFail(e.getMessage());
//            }
//        }
//
//        @Override
//        public void onNext(Void aVoid) {
//            if (wlsqBannerView != null)
//                wlsqBannerView.onWlsqBannerViewSuccess();
//        }
//    }

    //城市地理编码
    public void nameGetPlace(Context context, String cityName, GeocodeSearch.OnGeocodeSearchListener listener) {
        wlsqRepository.cityGetPlace(context, cityName, listener);
    }
}
