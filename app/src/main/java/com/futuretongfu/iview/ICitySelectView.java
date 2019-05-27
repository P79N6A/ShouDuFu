package com.futuretongfu.iview;
/*
 * Created by hhm on 2017/7/27.
 */

import com.futuretongfu.model.entity.AddressEntity;

import java.util.List;

public interface ICitySelectView {

    public void onGetCityListSuccess(List<AddressEntity> datas);
    public void onGetCityListFaile(String msg);

}
