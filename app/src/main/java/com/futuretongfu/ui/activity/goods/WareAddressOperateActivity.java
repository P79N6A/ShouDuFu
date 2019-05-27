package com.futuretongfu.ui.activity.goods;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.futuretongfu.R;
import com.futuretongfu.model.entity.WareHorseAddressEntity;
import com.futuretongfu.iview.IWareAddressOperateView;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.goods.WareAddressOperatePresenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.utils.StringUtil;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zhanggf on 2018/5/10.
 * 添加/修改/删除收货地址
 */

public class WareAddressOperateActivity extends BaseActivity implements IWareAddressOperateView {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.bt_right)
    TextView btRight;
    @Bind(R.id.tv_name)
    EditText tvName;
    @Bind(R.id.tv_phone)
    EditText tvPhone;
    @Bind(R.id.tv_address)
    EditText tvAddress;
    @Bind(R.id.btn_delete)
    Button btnDelete;
    @Bind(R.id.tb_moren)
    ToggleButton tbMoren;
    private static final String Intent_Extra_Type = "type";
    private static final String Intent_Extra_Bean = "bean";
    private int type;//0 添加 1修改
    private WareAddressOperatePresenter presenter;
    UserManager userManager = UserManager.getInstance();
    private String userId = "";
    private String name,phone,address;
    private int isDefault=1;   //判断是否默认地址 0为默认，1为非默认
    private WareHorseAddressEntity addressBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wareaddressoperate;
    }

    @Override
    protected Presenter getPresenter() {
        return presenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        type = intent.getIntExtra(Intent_Extra_Type, 0);
        addressBean = (WareHorseAddressEntity) getIntent().getSerializableExtra(Intent_Extra_Bean);
        userId = userManager.getUserId() + "";
        btRight.setVisibility(View.VISIBLE);
        btRight.setText("保存");
        if (type==0){
            tvTitle.setText("添加地址");
            btnDelete.setVisibility(View.GONE);
        }else if (addressBean!=null){
            tvTitle.setText("修改地址");
            btnDelete.setVisibility(View.VISIBLE);
            tvName.setText(addressBean.getReceiverName());
            tvPhone.setText(addressBean.getReceiverMobile());
            tvAddress.setText(addressBean.getReceiverAddress());
            isDefault =Integer.parseInt(addressBean.getIsDefault());
            if (isDefault==0){
                tbMoren.setChecked(true);
            }else {
                tbMoren.setChecked(false);}
        }
        presenter = new WareAddressOperatePresenter(this, this);

    }

    public static void startActivity(Activity context, int type, WareHorseAddressEntity item) {
        Intent intent = new Intent(context, WareAddressOperateActivity.class);
        intent.putExtra(Intent_Extra_Type, type);
        intent.putExtra(Intent_Extra_Bean, item);
        context.startActivityForResult(intent,1000);
    }

    @OnClick({R.id.bt_back, R.id.btn_delete,R.id.bt_right,R.id.tb_moren})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;
            case R.id.bt_right://保存
                name = tvName.getText().toString();
                phone = tvPhone.getText().toString().trim();
                address = tvAddress.getText().toString();
                if (isEmpty(name, "联系人")) {
                    return;
                }
                if (isEmpty(phone, "联系电话")) {
                    return;
                }
                if (!StringUtil.isPhoneNumber(phone)) {
                    showToast("手机号码格式不正确");
                    return;
                }
                if (type==0){  //添加
                    presenter.addWareAddress(userId,name,phone,address,isDefault);
                }else {
                    presenter.updateAddress(userId,addressBean.getId(),name,phone,address,isDefault);
                }
                break;
            case R.id.btn_delete:
                presenter.deleteAddress(userId,addressBean.getId());
                break;
            case R.id.tb_moren:
                if (tbMoren.isChecked()) {
                    isDefault = 0;
                } else {
                    isDefault = 1;
                }
                break;
        }
    }

    @Override
    public void onWareAddressAddSuccess(FuturePayApiResult futurePayApiResult) {
        showToast("添加地址"+futurePayApiResult.getMsg());
        Intent intent = getIntent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onWareAddressAddFaile(String msg) {
        showToast(msg);
    }

    @Override
    public void onWareAddressUpdateSuccess(FuturePayApiResult futurePayApiResult) {
        showToast("修改地址"+futurePayApiResult.getMsg());
        Intent intent = getIntent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onWareAddressUpdateFaile(String msg) {
        showToast(msg);
    }

    @Override
    public void onWareAddressDeleteSuccess(FuturePayApiResult futurePayApiResult) {
        showToast(futurePayApiResult.getMsg());
        Intent intent = getIntent();
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onWareAddressDeleteFaile(String msg) {
        showToast(msg);
    }
}
