package com.futuretongfu.presenter;
/*
 * Created by hhm on 2017/7/27.
 */

import android.content.Context;

import com.futuretongfu.iview.ICitySelectView;
import com.futuretongfu.model.repository.CommonRepository;
import com.futuretongfu.model.entity.AddressEntity;
import com.futuretongfu.model.repository.BaseRepository;

import java.util.List;

public class CitySelectPresenter extends Presenter {
    private ICitySelectView iCitySelectView;
    private CommonRepository commonRepository;

    public CitySelectPresenter(Context context,ICitySelectView iCitySelectView) {
        super(context);
        this.iCitySelectView = iCitySelectView;
        this.commonRepository = new CommonRepository();
    }

    @Override
    public void onDestroy() {
        if (commonRepository!=null) {
            commonRepository.cancel();
        }
    }

    /**
     *    查询所有城市
     * */
    public void getCityList(int provinceCode){
        commonRepository.getCityList(provinceCode, new BaseRepository.HttpCallListener<List<AddressEntity>>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if(iCitySelectView != null)
                    iCitySelectView.onGetCityListFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(List<AddressEntity> data) {
                if(iCitySelectView != null)
                    iCitySelectView.onGetCityListSuccess(data);
            }
        });
    }
}
