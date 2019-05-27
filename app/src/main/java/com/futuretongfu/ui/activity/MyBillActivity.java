package com.futuretongfu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.futuretongfu.R;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.iview.IMyBillView;
import com.futuretongfu.model.entity.MyBillEntity;
import com.futuretongfu.model.entity.MyBillResult;
import com.futuretongfu.presenter.MyBillPresenter;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.adapter.MyBillAdapter;
import com.futuretongfu.ui.component.dialog.BillScreenDialog;
import com.futuretongfu.ui.component.dialog.DateDialog;
import com.futuretongfu.utils.Logger.Logger;

import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 我的账单
 *
 * @author ChenXiaoPeng
 */
public class MyBillActivity extends BaseActivity implements IMyBillView, BaseQuickAdapter.RequestLoadMoreListener {

    private final static String Intent_Extra_Type = "type";

    @Bind(R.id.tv_title)
    public TextView textTitle;
    @Bind(R.id.bt_right)
    public TextView textScreen;

    @Bind(R.id.text_time)
    public TextView textTime;

    @Bind(R.id.swp_list)
    public SwipeRefreshLayout swpList;
    @Bind(R.id.recycler_list)
    public RecyclerView recyclerList;

    private MyBillPresenter myBillPresenter;
    private MyBillAdapter myBillAdapter;

    private int curScreenType = BillScreenDialog.Type_PaymentBalance;
    private int selectYear;
    private int selectMonth;

    private String type = Constants.Bill_Type_Cash;
    private String createTime;

    /***********************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_bill;
    }

    @Override
    protected Presenter getPresenter() {
        return myBillPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        textTitle.setText("我的账单");
        textScreen.setVisibility(View.VISIBLE);
        textScreen.setText("筛选");
        Intent intent = getIntent();
        type = intent.getStringExtra(Intent_Extra_Type);

        if (TextUtils.isEmpty(type))
            type = Constants.Bill_Type_Cash;

        if (Constants.Bill_Type_Cash.equals(type)) {
            curScreenType = BillScreenDialog.Type_PaymentBalance;
        } else {
            curScreenType = BillScreenDialog.Type_FSort;
        }

        myBillPresenter = new MyBillPresenter(this, this);

        swpList.setColorSchemeResources(R.color.colorPrimary);
        swpList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                myBillPresenter.billListDwRefresh(type, createTime);
            }
        });

        recyclerList.setLayoutManager(new LinearLayoutManager(this));

        myBillAdapter = new MyBillAdapter(this, curScreenType);
        myBillAdapter.setOnLoadMoreListener(this, recyclerList);
        myBillAdapter.disableLoadMoreIfNotFullPage(recyclerList);
        myBillAdapter.setEmptyView(R.layout.layout_recycler_empty_view);

        recyclerList.setAdapter(myBillAdapter);

        Calendar today = Calendar.getInstance();
        selectYear = today.get(Calendar.YEAR);
        selectMonth = today.get(Calendar.MONTH) + 1;
        createTime = "" + selectYear + selectMonth;

        textTime.setText(selectYear + "年" + selectMonth + "月");

        myBillPresenter.billListDwRefresh(type, createTime);

    }

    @Override
    public void onResume() {
        super.onResume();
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

    //筛选
    @OnClick(R.id.bt_right)
    public void onClickScreen() {
        BillScreenDialog dlg = new BillScreenDialog(this, new BillScreenDialog.BillScreenDialogListener() {

            @Override
            public void onBillScreenDialogSelecMall() {
                curScreenType = BillScreenDialog.Type_Mall;
                type = Constants.Bill_Type_mall;
                swpList.setRefreshing(true);
                myBillPresenter.billListDwRefresh(type, createTime);
                myBillAdapter.putType(BillScreenDialog.Type_Mall);
            }

            @Override
            public void onBillScreenDialogPaymentBalance() {
                curScreenType = BillScreenDialog.Type_PaymentBalance;
                type = Constants.Bill_Type_Cash;
                swpList.setRefreshing(true);
                myBillPresenter.billListDwRefresh(type, createTime);
                myBillAdapter.putType(BillScreenDialog.Type_PaymentBalance);
            }

            @Override
            public void onBillScreenDialogFSort() {
                curScreenType = BillScreenDialog.Type_FSort;
                type = Constants.Bill_Type_Jifen;
                myBillAdapter.putType(BillScreenDialog.Type_FSort);
                swpList.setRefreshing(true);
                myBillPresenter.billListDwRefresh(type, createTime);
            }
        });
        dlg.setType(curScreenType);
        dlg.show();
    }

    //时间
    @OnClick(R.id.imgv_time)
    public void onClickTime() {
        DateDialog dlg = new DateDialog(getContext(), selectYear, selectMonth, new DateDialog.DateDialogListener() {
            public void onDateDialogConfirm(int year, int month) {
                Logger.d("DateDialog", "year = " + year + ", month = " + month);

                selectYear = year;
                selectMonth = month;

                textTime.setText(year + "年" + month + "月");
                createTime = "" + selectYear + selectMonth;

                swpList.setRefreshing(true);
                myBillPresenter.billListDwRefresh(type, createTime);
            }
        });

        dlg.show();
    }

    /***********************************************************************/
    @Override
    public void onLoadMoreRequested() {
        Logger.i(TAG, "loadMore=true");
        myBillPresenter.billListUpLoad(type, createTime);
    }

    /***********************************************************************/
    @Override
    public void onMyBillDnRefreshSuccess(MyBillResult datas) {
        swpList.setRefreshing(false);
        myBillAdapter.setNewData(datas.getList());
    }

    @Override
    public void onMyBillDnRefreshFaile(String msg) {
        if (swpList != null) {
            swpList.setRefreshing(false);
        }
        showToast(msg);
    }

    @Override
    public void onMyBillUpLoadSuccess(List<MyBillEntity> datas) {
        myBillAdapter.loadMoreComplete();
        myBillAdapter.addData(datas);
    }

    @Override
    public void onMyBillUpLoadFaile(String msg) {
        myBillAdapter.loadMoreFail();
        showToast(msg);
    }

    @Override
    public void onMyBillUpLoadNoDatas() {
        myBillAdapter.loadMoreEnd();
    }

    /***********************************************************************/
    public static void startActivity(Context context, String type) {
        Intent intent = new Intent(context, MyBillActivity.class);
        intent.putExtra(Intent_Extra_Type, type);
        context.startActivity(intent);
    }

}
