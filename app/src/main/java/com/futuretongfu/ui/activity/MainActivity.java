package com.futuretongfu.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.appkefu.lib.interfaces.KFAPIs;
import com.appkefu.lib.service.KFXmppManager;
import com.appkefu.lib.utils.KFConstants;
import com.appkefu.lib.utils.KFLog;
import com.futuretongfu.R;
import com.futuretongfu.bean.HomeIncomeBean;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.RequestCode;
import com.futuretongfu.constants.ResultCode;
import com.futuretongfu.iview.IMainView;
import com.futuretongfu.model.eventType.FinishMainActivityEventType;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.home.MainPresenter;
import com.futuretongfu.ui.activity.user.LoginActivity;
import com.futuretongfu.ui.component.dialog.HomeIncomeDialog;
import com.futuretongfu.ui.fragment.BaseFragment;
import com.futuretongfu.ui.fragment.HomeFragment;
import com.futuretongfu.ui.fragment.PayFragment;
import com.futuretongfu.ui.fragment.PersonFragment;
import com.futuretongfu.ui.fragment.ShoppingFragment;
import com.futuretongfu.ui.fragment.WlsqFragment;
import com.futuretongfu.utils.CacheActivityUtil;
import com.futuretongfu.utils.TimerUtil;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by zhanggf on 2018/3/14.
 */
public class MainActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, IMainView {

    private static final int HOMEPAGE_TAG = 0, SHANGQUAN_TAG = 1, PAY_TAG = 2, SHOP_TAG = 3, PERSON_TAG = 4;
    @Bind(R.id.radiobutton1)
    RadioButton radiobutton1;
    @Bind(R.id.radiobutton2)
    RadioButton radiobutton2;
    @Bind(R.id.radiobutton3)
    RadioButton radiobutton3;
    @Bind(R.id.radiobutton4)
    RadioButton radiobutton4;
    @Bind(R.id.radiobutton5)
    RadioButton radiobutton5;
    private List<BaseFragment> fragments;
    private int lastFragment = -1;
    @Bind(R.id.remark_num)
    TextView remark_num;
    private WlsqFragment shangquanFragment;
    private MainPresenter mainPresenter;
    private HomeIncomeDialog homeIncomeDialog;  //对话框

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected Presenter getPresenter() {
        return mainPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        initFragment();
    }

    private void initFragment() {
        fragments = new ArrayList<>();
        registerFinishReceiver();
        registerKefuReceiver();
        mainPresenter = new MainPresenter(getContext(), this);
        fragments.add(new HomeFragment());
//        shangquanFragment = new ShangQuanFragment();
        shangquanFragment = new WlsqFragment();
        fragments.add(shangquanFragment);
//        fragments.add(new TxlFragment());
//        fragments.add(new MainFragment());
        fragments.add(new PayFragment());
        fragments.add(new ShoppingFragment());
//        fragments.add(new MyFragment());
        fragments.add(new PersonFragment());
        radiobutton1.setOnCheckedChangeListener(this);
        radiobutton2.setOnCheckedChangeListener(this);
        radiobutton3.setOnCheckedChangeListener(this);
        radiobutton4.setOnCheckedChangeListener(this);
        radiobutton5.setOnCheckedChangeListener(this);
        //默认选中首页
        changeFragment(HOMEPAGE_TAG);
        if (isLogin()) {
            String userId = UserManager.getInstance().getUserId() + "";
            mainPresenter.onShopCartNum(userId);
            mainPresenter.onHomeIncomeNum(userId);
//            mainPresenter.getUserInfoByUserId();
//            if (!TextUtils.isEmpty( UserManager.getInstance().getUserKey())&&Constants.User_Type_XK != UserManager.getInstance().getUserType()) {
//                mainPresenter.onShopCartNum(userId);
//                mainPresenter.onHomeIncomeNum(userId);
//            }
        }
    }

    /**
     * 切换界面
     *
     * @param tag fragment tag
     */
    private void changeFragment(int tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (lastFragment != -1) {
            transaction.hide(fragments.get(lastFragment));
        }
        lastFragment = tag;
        if (!fragments.get(tag).isAdded()) {
            transaction.add(R.id.fragment_main_content, fragments.get(tag));
        }
        transaction.show(fragments.get(tag));
        transaction.commitAllowingStateLoss();
    }

    private final static String Intent_Extra_NeedGuide = "needGuide";

    public static void startActivity(Context context, boolean isNeedGuide) {
        CacheActivityUtil.finishActivity();
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(Intent_Extra_NeedGuide, isNeedGuide);
        context.startActivity(intent);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            switch (compoundButton.getId()) {
                case R.id.radiobutton1:
                    radiobutton1.setChecked(true);
                    radiobutton2.setChecked(false);
                    radiobutton3.setChecked(false);
                    radiobutton4.setChecked(false);
                    radiobutton5.setChecked(false);
                    changeFragment(HOMEPAGE_TAG);
                    break;
                case R.id.radiobutton2:
                    radiobutton1.setChecked(false);
                    radiobutton2.setChecked(true);
                    radiobutton3.setChecked(false);
                    radiobutton4.setChecked(false);
                    radiobutton5.setChecked(false);
                    changeFragment(SHANGQUAN_TAG);
                    break;
                case R.id.radiobutton3:
                    radiobutton1.setChecked(false);
                    radiobutton2.setChecked(false);
                    radiobutton3.setChecked(true);
                    radiobutton4.setChecked(false);
                    radiobutton5.setChecked(false);
                    changeFragment(PAY_TAG);
                    break;
                case R.id.radiobutton4:
                    radiobutton1.setChecked(false);
                    radiobutton2.setChecked(false);
                    radiobutton3.setChecked(false);
                    radiobutton4.setChecked(true);
                    radiobutton5.setChecked(false);
                    if (!UserManager.getInstance().isLogin()) {
                        LoginActivity.startActivity(getContext(), RequestCode.REQUEST_CODE_LOGIN_NORMAL);
                        return;
                    }

                    changeFragment(SHOP_TAG);
                    break;
                case R.id.radiobutton5:
                    radiobutton1.setChecked(false);
                    radiobutton2.setChecked(false);
                    radiobutton3.setChecked(false);
                    radiobutton4.setChecked(false);
                    radiobutton5.setChecked(true);
                    changeFragment(PERSON_TAG);
                    break;
            }
        }
    }

    /**
     * @param isShow 是否显示红点
     */
    public void showShopCartNum(boolean isShow) {
        if (remark_num != null) {
            if (isShow) {
                remark_num.setVisibility(View.VISIBLE);
            } else {
                remark_num.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE && resultCode == Constants.RESULT_CODE) {
            shangquanFragment.refreshData(data.getStringExtra("cityName"));
        }
        //友盟分享
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        /* 登录成功，显示“我的”界面 */
        if (requestCode == RequestCode.REQUEST_CODE_LOGIN_MY && resultCode == RESULT_OK) {
            changeFragment(PERSON_TAG);
        }
        /* 退出，显示“首页”界面 */
        else if (requestCode == RequestCode.REQUEST_CODE_SET && resultCode == ResultCode.RESULT_CODE__SET_QUIT) {
            changeFragment(HOMEPAGE_TAG);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    RefreshReceive refreshReceive;

    public void registerFinishReceiver() {
        refreshReceive = new RefreshReceive();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(Constants.REQUEST_CODE_SHOPPING);
        filter.addAction(Constants.REQUEST_CODE_SHOPCARTNUM);
        filter.addAction(Constants.REQUEST_CODE_UPGRADE);
        filter.addAction(Constants.REQUEST_CODE_UPGRADE_SUCCESS);
        registerReceiver(refreshReceive, filter);
    }

    public void registerKefuReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        //监听网络连接变化情况
        intentFilter.addAction(KFConstants.ACTION_XMPP_CONNECTION_CHANGED);
        //监听消息
        intentFilter.addAction(KFConstants.ACTION_XMPP_MESSAGE_RECEIVED);
        //工作组在线状态
        intentFilter.addAction(KFConstants.ACTION_XMPP_WORKGROUP_ONLINESTATUS);
        registerReceiver(mXmppreceiver, intentFilter);
    }

    @Override
    public void onshopCartFail(int code, String msg) {
        showToast(msg);
    }

    @Override
    public void onshopCartSuccess(String data) {
        if (!TextUtils.isEmpty(data) && !data.equals("0")) {
            showShopCartNum(true);
            remark_num.setText(data);
        } else {
            showShopCartNum(false);
        }
    }

    @Override
    public void onHomeIncomeFail(int code, String msg) {
        showToast(msg);
    }

    @Override
    public void onnHomeIncomeSuccess(HomeIncomeBean data) {
        if (!TextUtils.isEmpty(data.getMoneyByDay()) && !data.getMoneyByDay().equals("0")) {
            homeIncomeDialog = new HomeIncomeDialog(this, data, new HomeIncomeDialog.HomeIncomeDialogListener() {
                @Override
                public void onHomeIncomeDialog() {
                    MyBillActivity.startActivity(getContext(), Constants.Bill_Type_Cash);
                }
            });
            homeIncomeDialog.show();
        }
    }

    @Override
    public void onUpdateUserInfoSuccess() {
        if (!TextUtils.isEmpty(UserManager.getInstance().getUserKey()) && Constants.User_Type_XK == UserManager.getInstance().getUserType()) {
//            PromptDialogUtils.showUpgradePromptDialog(this);
        }
    }

    @Override
    public void onUpdateUserInfoFaile(boolean showToast, String msg) {
        showToast(msg);
    }



    public class RefreshReceive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(Constants.REQUEST_CODE_SHOPPING)) {
                radiobutton4.setChecked(true);
            } else if (intent.getAction().equalsIgnoreCase(Constants.REQUEST_CODE_SHOPCARTNUM)) {
                if (isLogin()) {
                    String userId = UserManager.getInstance().getUserId() + "";
                    mainPresenter.onShopCartNum(userId);
                }
            } else if (intent.getAction().equalsIgnoreCase(Constants.REQUEST_CODE_UPGRADE)) {
                mainPresenter.getUserInfoByUserId();
            } else if (intent.getAction().equalsIgnoreCase(Constants.REQUEST_CODE_UPGRADE_SUCCESS)) {
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                TimerUtil.startTimer(400, new TimerUtil.TimerCallBackListener2() {
                    @Override
                    public void onEnd() {
                        finish();
                        startActivity(getIntent());
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
        unregisterReceiver(refreshReceive);
        unregisterReceiver(mXmppreceiver);
    }

    /*************************************************************/
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(FinishMainActivityEventType event) {
        delayFinish(200);
    }

    /**
     * 是否登录
     */
    public boolean isLogin() {
        if (!UserManager.getInstance().isLogin()) {
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        KFAPIs.visitorLogin(this);
    }

    // 监听：连接状态、即时通讯消息、客服在线状态
    private BroadcastReceiver mXmppreceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 监听：连接状态
            if (action.equals(KFConstants.ACTION_XMPP_CONNECTION_CHANGED))// 监听链接状态
            {
                updateStatus(intent.getIntExtra("new_state", 0));
            }
            // 监听：即时通讯消息
            else if (action.equals(KFConstants.ACTION_XMPP_MESSAGE_RECEIVED))// 监听消息
            {
                //消息内容
                String body = intent.getStringExtra("body");
                //消息来自于
                String from = intent.getStringExtra("from");
                KFLog.d("消息来自于:" + from + " 消息内容:" + body);
                KFLog.d("未读消息数目：" + KFAPIs.getUnreadMessageCount(from, getContext()));
            }
            // 客服工作组在线状态
            else if (action.equals(KFConstants.ACTION_XMPP_WORKGROUP_ONLINESTATUS)) {
                String fromWorkgroupName = intent.getStringExtra("from");
                String onlineStatus = intent.getStringExtra("onlinestatus");
                KFLog.d("客服工作组:" + fromWorkgroupName + " 在线状态:" + onlineStatus);// online：在线；offline:
                // 离线
                // 截获到客服工作组wgdemo的在线状态
            }

        }
    };

    // 根据监听到的连接变化情况更新界面显示
    private void updateStatus(int status) {

        switch (status) {
            case KFXmppManager.CONNECTED:
                // 查询客服工作组在线状态，返回结果在BroadcastReceiver中返回
                KFAPIs.checkKeFuIsOnlineAsync("shoudufupingtaikefugongzuozu", this);
                break;
            case KFXmppManager.DISCONNECTED:
//                showToast("微客服4(Demo)(未连接)");
                break;
            case KFXmppManager.CONNECTING:
//                showToast("微客服4(Demo)(登录中...");
                break;
            case KFXmppManager.DISCONNECTING:

//                showToast("微客服4(Demo)(登出中...)");
                break;
            case KFXmppManager.WAITING_TO_CONNECT:
            case KFXmppManager.WAITING_FOR_NETWORK:
//                showToast("微客服4(Demo)(等待中...)");
                break;
            default:
                throw new IllegalStateException();
        }
    }

}
