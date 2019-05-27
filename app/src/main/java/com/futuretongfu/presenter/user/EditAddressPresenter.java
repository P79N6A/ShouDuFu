package com.futuretongfu.presenter.user;

import android.content.Context;

import com.futuretongfu.iview.IEditAddressView;
import com.futuretongfu.model.entity.AddressEntity;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.CommonRepository;
import com.futuretongfu.model.repository.UserRepository;
import com.futuretongfu.presenter.Presenter;

import java.util.List;

/**
 *   修改地区
 * Created by ChenXiaoPeng on 2017/6/27.
 */

public class EditAddressPresenter extends Presenter {

    private IEditAddressView iEditAddressView;
    private UserRepository userRepository;
    private CommonRepository commonRepository;

    public EditAddressPresenter(Context context, IEditAddressView iEditAddressView){
        super(context);
        this.iEditAddressView = iEditAddressView;
        this.userRepository = new UserRepository();
        this.commonRepository = new CommonRepository();
    }

    @Override
    public void onDestroy(){
        if(userRepository != null)
            userRepository.cancel();

        if(commonRepository != null)
            commonRepository.cancel();
    }

    /**
     *    设置和修改地区
     * */
    public void setRegion(final String province, final String city, final String district){

        userRepository.setRegion(
                UserManager.getInstance().getUserId() + ""
                , province, city, district
                , new BaseRepository.HttpCallListener<Void>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if(iEditAddressView != null)
                            iEditAddressView.onEditAddressFaile(msg);
                    }

                    @Override
                    public void onHttpCallSuccess(Void data) {
                        String address;
                        if(province.equals(city)){
                            address = province + district;
                        }
                        else{
                            address = province + city + district;
                        }

                        UserManager.getInstance().setRegion(address);
                        UserManager.getInstance().save();

                        if(iEditAddressView != null)
                            iEditAddressView.onEditAddressSuccess();
                    }
                }
        );
    }

    /**
     *    查询省份列表
     * */
    public void getProvinceList(){
        commonRepository.getProvinceList(new BaseRepository.HttpCallListener<List<AddressEntity>>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if(iEditAddressView != null)
                    iEditAddressView.onGetProvinceListFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(List<AddressEntity> data) {
                if(iEditAddressView != null)
                    iEditAddressView.onGetProvinceListSuccess(data);
            }
        });
    }

    /**
     *    查询城市列表
     * */
    public void getCityList(int provinceCode){
        commonRepository.getCityList(provinceCode, new BaseRepository.HttpCallListener<List<AddressEntity>>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if(iEditAddressView != null)
                    iEditAddressView.onGetCityListFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(List<AddressEntity> data) {
                if(iEditAddressView != null)
                    iEditAddressView.onGetCityListSuccess(data);
            }
        });
    }

    /**
     *    查询地区列表
     * */
    public void getDistrictList(int cityCode){
        commonRepository.getDistrictList(cityCode, new BaseRepository.HttpCallListener<List<AddressEntity>>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if(iEditAddressView != null)
                    iEditAddressView.onGetDistrictListFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(List<AddressEntity> data) {
                if(iEditAddressView != null)
                    iEditAddressView.onGetDistrictListSuccess(data);
            }
        });
    }
}
