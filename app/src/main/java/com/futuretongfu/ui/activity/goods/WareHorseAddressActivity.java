package com.futuretongfu.ui.activity.goods;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.model.entity.WareHorseAddressEntity;
import com.futuretongfu.iview.IWareAddressView;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.goods.WareAddressPresenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.adapter.WareHorseAddressAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zhanggf on 2018/5/10.
 * 收货地址
 */

public class WareHorseAddressActivity extends BaseActivity implements IWareAddressView,
        WareHorseAddressAdapter.OnItemClickListener {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.recycler_list)
    RecyclerView recyclerList;
    @Bind(R.id.swp_bank)
    SwipeRefreshLayout swpList;
    @Bind(R.id.my_address_nodata_rl)
    RelativeLayout my_address_nodata_rl;
    private List<WareHorseAddressEntity> list = new ArrayList<>();
    private WareHorseAddressAdapter addressAdapter;
    private WareAddressPresenter presenter;
    UserManager userManager = UserManager.getInstance();
    private String userId = "";
    private boolean isClick = false;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_warehorseaddress;
    }

    @Override
    protected Presenter getPresenter() {
        return presenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        tvTitle.setText("收货地址");
        userId = userManager.getUserId() + "";
        presenter = new WareAddressPresenter(this, this);
        isClick = getIntent().getBooleanExtra("isClick",false);
        showProgressDialog();
        getAddressList();
        initRecycle();

    }

    private void initRecycle() {
        addressAdapter = new WareHorseAddressAdapter(context, this, list);
        addressAdapter.setIsClick(isClick);
        recyclerList.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerList.setAdapter(addressAdapter);
        swpList.setColorSchemeResources(R.color.colorPrimary);
        swpList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list.clear();
                getAddressList();
            }
        });
    }

    private void getAddressList() {
        presenter.getWareAddressList(userId);
    }

    @OnClick({R.id.bt_back, R.id.btn_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;
            case R.id.btn_add:
                WareAddressOperateActivity.startActivity(this, 0, null);
                break;
        }
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, WareHorseAddressActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onWareAddressSuccess(List<WareHorseAddressEntity> datas) {
        hideProgressDialog();
        swpList.setRefreshing(false);
        if (datas != null && datas.size() > 0) {
            swpList.setVisibility(View.VISIBLE);
            my_address_nodata_rl.setVisibility(View.GONE);
            list.addAll(datas);
            addressAdapter.notifyDataSetChanged();
        } else {
            swpList.setVisibility(View.GONE);
            my_address_nodata_rl.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onWareAddressFaile(String msg) {
        swpList.setVisibility(View.GONE);
        my_address_nodata_rl.setVisibility(View.VISIBLE);
        hideProgressDialog();
        swpList.setRefreshing(false);
        showToast("获取数据失败：" + msg);
    }

    //设置默认
    @Override
    public void onWareAddressSetSuccess(FuturePayApiResult futurePayApiResult) {
        hideProgressDialog();
        showToast("设置" + futurePayApiResult.getMsg());
        list.clear();
        getAddressList();
    }

    @Override
    public void onWareAddressSetFaile(String msg) {
        showToast(msg);
    }

    //删除成功
    @Override
    public void onWareAddressDeleteSuccess(FuturePayApiResult futurePayApiResult) {
        hideProgressDialog();
        showToast("删除" + futurePayApiResult.getMsg());
        list.clear();
        getAddressList();
//        list.remove(selectItemPosition);
//        addressAdapter.notifyDataSetChanged();
//        addressAdapter.deleteItem(item);
    }

    @Override
    public void onWareAddressDeleteFaile(String msg) {
        showToast(msg);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1000:
                if (resultCode == Activity.RESULT_OK) {
                    if (list != null && list.size() > 0)
                        list.clear();
                    getAddressList();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onMyAddressAdapterClick(WareHorseAddressEntity item) {
        if (!isClick){
            WareAddressOperateActivity.startActivity(this, 1, item);
        }else {
            Intent intent = getIntent();
            Bundle bundle = new Bundle();
            bundle.putString("getId", item.getId());
            bundle.putString("getAddress", item.getReceiverAddress());
            bundle.putString("getName",item.getReceiverName());
            bundle.putString("getPhone",item.getReceiverMobile());
            intent.putExtras(bundle);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onMyAddressAdapterEditClick(WareHorseAddressEntity item) {
        WareAddressOperateActivity.startActivity(this, 1, item);
    }

    @Override
    public void onMyAddressAdapterDelete(WareHorseAddressEntity item) {
        presenter.deleteAddress(userId, item.getId());
    }

    @Override
    public void onMyAddressAdapterSet(WareHorseAddressEntity item) {
        presenter.setBanktype(userId, item.getId());
    }
}
