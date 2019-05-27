package com.futuretongfu.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.futuretongfu.bean.BankListBean;
import com.futuretongfu.bean.BankListItemBean;
import com.futuretongfu.iview.ICheckPayPwdView;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.ui.activity.user.EditPayPasswordActivity;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.PromptDialogUtils;
import com.futuretongfu.view.BankPayPopupWindow;
import com.futuretongfu.R;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.iview.IBankListView;
import com.futuretongfu.listener.OnBankItemClickListener;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.bank.BankPresenter;
import com.futuretongfu.presenter.bank.PayPasswordPresenter;
import com.futuretongfu.ui.activity.user.LoginActivity;
import com.futuretongfu.ui.adapter.MyBankRecycleAdapter;
import com.futuretongfu.utils.AppUtil;
import com.futuretongfu.utils.Util;
import com.futuretongfu.view.BankPopupWindow;
import com.futuretongfu.view.CustomLayout.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 类: MyBankActivity
 * 描述: 我的银行卡
 * 作者： weiang on 2017/6/22
 */
public class MyBankActivity extends BaseActivity implements IBankListView, ICheckPayPwdView, BankPayPopupWindow.OnBankPayListener {
    @Bind(R.id.recycler_list)
    public RecyclerView recyclerList;
    @Bind(R.id.tv_title)
    TextView titleView;
    @Bind(R.id.bt_back)
    ImageView backView;
    @Bind(R.id.bt_add)
    ImageView addView;
    @Bind(R.id.root_bank)
    LinearLayout rootView;
    @Bind(R.id.swp_bank)
    public SwipeRefreshLayout swpBank;
    @Bind(R.id.my_bank_nodata_rl)
    public RelativeLayout my_bank_nodata_rl;
    List<BankListItemBean> list;
    MyBankRecycleAdapter adapter;
    Context context;
    private BankPopupWindow bankPopup;
    private BankPayPopupWindow payPopupWindow;
    private Handler handler = new Handler();
    private int selectItemPosition = 0;
    private String cardNumber = "";
    BankPresenter bankPresenter;
    PayPasswordPresenter payPasswordPresenter;
    //    GetRealNameStatuePresenter realNameStatuePresenter;
    UserManager userManager = UserManager.getInstance();
    private String userId = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_bank;
    }

    @Override
    protected Presenter getPresenter() {
        return bankPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        context = MyBankActivity.this;
        userId = userManager.getUserId() + "";
        list = new ArrayList<>();
        bankPopup = new BankPopupWindow(this);
        payPopupWindow = new BankPayPopupWindow(this);
        bankPresenter = new BankPresenter(this, this);
        payPasswordPresenter = new PayPasswordPresenter(this, this);
        payPopupWindow.setBankPayListener(this);
        bankPopup.setOnItemClickListener(onItemClickListener);
        addView.setVisibility(View.VISIBLE);
        titleView.setText(getResources().getString(R.string.title_bank));
        initRecycle();
        initRefresh();
        initListener();
        getBankList();
    }

    @Override
    protected void onResume() {
        if (Constants.isBindCardSuccess) {
            Constants.isBindCardSuccess = false;
            list.clear();
            getBankList();
        }
        super.onResume();
    }


    private boolean idSecrecyCard = false;

    /**
     * 初始化recycle控件
     */
    private void initRecycle() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerList.setLayoutManager(linearLayoutManager);
        recyclerList.setHasFixedSize(true);
        recyclerList.addItemDecoration(new SpaceItemDecoration(20));
        adapter = new MyBankRecycleAdapter(this, list);
        recyclerList.setAdapter(adapter);
        adapter.setOnItemClickListener(new MyBankRecycleAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                selectItemPosition = position;
                bankPopup.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
                if (IntentKey.SAFETY_CARD.equals(list.get(position).getType())) {
                    idSecrecyCard = true;
                    bankPopup.hintSecrecyButton(true);
                } else {
                    idSecrecyCard = false;
                    bankPopup.hintSecrecyButton(false);
                }
                Util.setAlpha(context, 0.5f);
            }
        });
    }

    /**
     * 初始化监听器
     */

    private void initListener() {
        bankPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Util.setAlpha(context, 1.0f);
            }
        });
        payPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                payPopupWindow.setViewNull();
            }
        });
    }


    /**
     * 初始化下拉控件
     */
    private void initRefresh() {
        swpBank.setColorSchemeResources(R.color.colorPrimary);
        swpBank.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list.clear();
                getBankList();
            }
        });
    }


    private OnBankItemClickListener onItemClickListener = new OnBankItemClickListener() {
        @Override
        public void onItemClick(View view) {

        }

        @Override
        public void onItemClick(View view, View rootView) {
            switch (view.getId()) {
                case R.id.imgv_close:
                    bankPopup.dismiss();
                    break;
                case R.id.text_secrecy:
                    cardNumber = list.get(selectItemPosition).getAccNo();
                    view.setSelected(true);
                    rootView.findViewById(R.id.text_delete).setSelected(false);
                    handler.postDelayed(runnable, 100);
                    break;
                case R.id.text_delete:
                    if (idSecrecyCard) {
                        showToast("请先设置其他银行卡为安全卡");
                        return;
                    }
                    view.setSelected(true);
                    rootView.findViewById(R.id.text_secrecy).setSelected(false);
                    handler.postDelayed(deleteRunnable, 100);
                    break;
            }
        }

        @Override
        public void onItemClick(View view, int position) {

        }
    };

    @OnClick({R.id.bt_back, R.id.bt_add})
    public void onBackClick(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;
            case R.id.bt_add:
                AppUtil.getRealNameStatus(context);
                int realNameStatus = userManager.getRealNameStatus();
                if (realNameStatus == Constants.RealNameStatus_No) {
                    PromptDialogUtils.showNotRuleNamePromptDialog(this);
                } else if (realNameStatus == Constants.RealNameStatus_Yes) {
                    if (!userManager.isAnswer()) {
                        PromptDialogUtils.showNotSetQuestionPromptDialog(this);
                    } else {
                        if (!userManager.isHasPayPwd()) {
                            PromptDialogUtils.showNotSetPwdPromptDialog(this, EditPayPasswordActivity.TYPE_SET_NEW_PAY_PWD);
                        } else {
                            AddSafeCardActivity.startActivity(this);
                        }
                    }
                } else if (realNameStatus == Constants.RealNameStatus_Doing) {
                    PromptDialogUtils.showRuleNameingPromptDialog(this);
                } else if (realNameStatus == Constants.RealNameStatus_Faile) {
                    PromptDialogUtils.showRuleNameFailPromptDialog(this);
                } else if (realNameStatus == Constants.RealNameStatus_Imperfect) {
                    PromptDialogUtils.showImPerfectRuleNamePromptDialog(this);
                }
                break;
        }
    }

    Runnable deleteRunnable = new Runnable() {
        @Override
        public void run() {
            bankPopup.dismiss();
            SMSVerificationActivity.startActivity(MyBankActivity.this,2,list.get(selectItemPosition).getPhone(),list.get(selectItemPosition).getId());
//            bankPresenter.deleteBankCard(userId, list.get(selectItemPosition).getAccNo());
        }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            bankPopup.dismiss();
            payPopupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
        }
    };

    /**
     * 获取银行卡列表
     */
    private void getBankList() {
        bankPresenter.getBankList(userId);
        showProgressDialog();
    }


    @Override
    public void onBankListViewSuccess(BankListBean bankListBean) {
        hideProgressDialog();
        swpBank.setRefreshing(false);
        if (bankListBean != null && !bankListBean.getData().isEmpty()) {
            swpBank.setVisibility(View.VISIBLE);
            my_bank_nodata_rl.setVisibility(View.GONE);
            list.addAll(bankListBean.getData());
            adapter.notifyDataSetChanged();
        } else {
            swpBank.setVisibility(View.GONE);
            my_bank_nodata_rl.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void ononBankListViewFaile(String msg) {
        swpBank.setVisibility(View.GONE);
        my_bank_nodata_rl.setVisibility(View.VISIBLE);
        hideProgressDialog();
        swpBank.setRefreshing(false);
        showToast("获取数据失败：" + msg);
    }

    @Override
    public void onBankSetSuccess(FuturePayApiResult futurePayApiResult) {
        showToast("设置成功！");
        hideProgressDialog();
        list.clear();
        getBankList();
    }

    @Override
    public void onBankSetFaile(String msg) {
        showToast(msg);
        hideProgressDialog();
    }

    @Override
    public void onBankAddSuccess(FuturePayApiResult futurePayApiResult) {
        hideProgressDialog();
    }

    @Override
    public void onBankAddFaile(String msg) {
    }

    @Override
    public void onBankDeleteSuccess(FuturePayApiResult futurePayApiResult) {
        showToast("删除成功！");
        list.remove(selectItemPosition);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onBankDeleteFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
    }

    @Override
    public void onBankPayInputFinish(StringBuffer numberString) {
        Log.d(TAG, "------>:" + numberString.toString());
        if (userManager.isLogin()) {
            if (TextUtils.isEmpty(userId)) {
                IntentUtil.startActivity(context, LoginActivity.class);
                return;
            }
            payPasswordPresenter.checkPassword(userId, numberString.toString());
            showProgressDialog();
        } else {
            IntentUtil.startActivity(context, LoginActivity.class);
        }
    }

    @Override
    public void onCheckPayPwdSuccess(FuturePayApiResult futurePayApiResult) {
        hideProgressDialog();
        bankPresenter.setBanktype(userId, cardNumber);
        payPopupWindow.setViewNull();
        payPopupWindow.dismiss();
    }

    @Override
    public void onCheckPayPwdFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
        payPopupWindow.setViewNull();
        payPopupWindow.dismiss();
    }


}
