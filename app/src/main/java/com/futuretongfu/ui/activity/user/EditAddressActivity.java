package com.futuretongfu.ui.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.futuretongfu.iview.IEditAddressView;
import com.futuretongfu.model.entity.AddressEntity;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.user.EditAddressPresenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.adapter.EditAddressAdapter;
import com.futuretongfu.R;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 编辑地址
 *
 * @author ChenXiaoPeng
 */
public class EditAddressActivity extends BaseActivity implements
        EditAddressAdapter.EditAddressAdapterListener
        , IEditAddressView {

    public static final int Type_Province = 0;//省份
    public static final int Type_City = 1;//城市
    public static final int Type_Region = 2;//地区

    @Bind(R.id.tv_title)
    public TextView textTitle;
    @Bind(R.id.activity_edit_address_all_tv)
    public TextView tvAll;
    @Bind(R.id.recycler_list)
    public RecyclerView recyclerList;

    private int type = Type_Province;
    private String province;
    private String city;
    private EditAddressAdapter editAddressAdapter;

    private EditAddressPresenter editAddressPresenter;

    /***********************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_address;
    }


    @Override
    protected Presenter getPresenter() {
        return editAddressPresenter;
    }


    @Override
    protected void init(Bundle savedInstanceState) {
        editAddressPresenter = new EditAddressPresenter(this, this);
        setTypeProvince();
    }

    //返回按键
    @Override
    public void onBackPressed() {
        boolean success = getSupportFragmentManager().popBackStackImmediate();
        if (!success)
            super.onBackPressed();
    }

    /***********************************************************************/
    @OnClick(R.id.bt_back)
    public void onClickBack() {
        finish();
    }

    /***********************************************************************/
    @Override
    public void onEditAddressAdapter2City(String province, int provinceCode) {
        this.province = province;
        editAddressPresenter.getCityList(provinceCode);
    }

    @Override
    public void onEditAddressAdapter2Region(String city, int cityCode) {
        this.city = city;
        editAddressPresenter.getDistrictList(cityCode);
    }

    @Override
    public void onEditAddressAdapterFinish(String district) {
        editAddressPresenter.setRegion(province, city, district);
    }

    /***********************************************************************/
    @Override
    public void onEditAddressSuccess() {
        finish();
    }

    @Override
    public void onEditAddressFaile(String msg) {
        showToast(msg);
    }

    @Override
    public void onGetProvinceListSuccess(List<AddressEntity> datas) {
        editAddressAdapter.setDatas(type, datas);
    }

    @Override
    public void onGetProvinceListFaile(String msg) {
        showToast(msg);
    }

    @Override
    public void onGetCityListSuccess(List<AddressEntity> datas) {
        setTypeCity(datas);
    }

    @Override
    public void onGetCityListFaile(String msg) {
        showToast(msg);
    }

    @Override
    public void onGetDistrictListSuccess(List<AddressEntity> datas) {
        setTypeRegion(datas);
    }

    @Override
    public void onGetDistrictListFaile(String msg) {
        showToast(msg);
    }

    /***********************************************************************/

    private void setTypeProvince() {
        textTitle.setText("省份");
        tvAll.setText("全部");

        type = Type_Province;
        editAddressAdapter = new EditAddressAdapter(this, type, this);
        recyclerList.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerList.setAdapter(editAddressAdapter);

        editAddressPresenter.getProvinceList();
    }

    private void setTypeCity(List<AddressEntity> datas) {
        textTitle.setText("城市");
        tvAll.setText(province);

        type = Type_City;
        editAddressAdapter.setDatas(type, datas);
    }

    private void setTypeRegion(List<AddressEntity> datas) {
        textTitle.setText("地区");
        tvAll.setText(city);

        type = Type_Region;
        editAddressAdapter.setDatas(type, datas);

    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, EditAddressActivity.class);
        context.startActivity(intent);
    }
}
