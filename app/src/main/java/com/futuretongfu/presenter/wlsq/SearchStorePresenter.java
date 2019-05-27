package com.futuretongfu.presenter.wlsq;

import android.content.Context;

import com.futuretongfu.bean.StoreBean;
import com.futuretongfu.model.repository.WlsqRepository;
import com.futuretongfu.iview.SearchStoreIView;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.presenter.Presenter;

import java.util.List;

/**
 * 商家搜索
 *
 * @author DoneYang 2017/6/25
 */

public class SearchStorePresenter extends Presenter {

    private WlsqRepository wlsqRepository;
    private SearchStoreIView searchView;

    public SearchStorePresenter(Context context, SearchStoreIView searchView) {
        super(context);
        this.searchView = searchView;
        this.wlsqRepository = new WlsqRepository();
    }

    @Override
    public void onDestroy() {
        if (wlsqRepository != null)
            wlsqRepository.cancel();
    }

    public void onSearchStore(final int page, String storeName, String longitude, String latitude, String city
            , String industryId) {
           wlsqRepository.onSearchStore(page, storeName, longitude, latitude, city, industryId, new BaseRepository.HttpCallListener<List<StoreBean>>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (searchView != null) {
                    if ("1".equals(page + "")) {
                        searchView.onSearchStoreFail(code, msg);
                    } else {
                        searchView.onSearchStoreMoreFail(code, msg);
                    }
                }
            }

            @Override
            public void onHttpCallSuccess(List<StoreBean> data) {
                if (searchView != null) {
                    if ("1".equals(page + "")) {
                        searchView.onSearchStoreSuccess(data);
                    } else {
                        searchView.onSearchStoreMoreSuccess(data);
                    }
                }
            }
        });
    }
}
