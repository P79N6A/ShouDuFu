package com.futuretongfu.iview;

import com.futuretongfu.model.entity.AddressEntity;

import java.util.List;

/**
 * Created by ChenXiaoPeng on 2017/6/27.
 */

public interface IEditAddressView {

    public void onEditAddressSuccess();
    public void onEditAddressFaile(String msg);

    public void onGetProvinceListSuccess(List<AddressEntity> datas);
    public void onGetProvinceListFaile(String msg);

    public void onGetCityListSuccess(List<AddressEntity> datas);
    public void onGetCityListFaile(String msg);

    public void onGetDistrictListSuccess(List<AddressEntity> datas);
    public void onGetDistrictListFaile(String msg);

}
