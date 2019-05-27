package com.futuretongfu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.umeng.analytics.MobclickAgent;
import com.futuretongfu.R;
import com.futuretongfu.WeiLaiFuApplication;
import com.futuretongfu.base.BaseSerializable;
import com.futuretongfu.bean.entity.Tranfer;
import com.futuretongfu.iview.IUserLoginOffline;
import com.futuretongfu.iview.IView;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.utils.AppUtil;
import com.futuretongfu.utils.CacheActivityUtil;
import com.futuretongfu.utils.TimerUtil;
import com.futuretongfu.utils.To;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import cc.cloudist.acplibrary.ACProgressPie;
import xiaofei.library.hermeseventbus.HermesEventBus;

import static com.futuretongfu.WeiLaiFuApplication.statusBarHeight;

/*
 * Created by ChenXiaoPeng on 2017/6/8.
 */

public abstract class BaseActivity extends RxBaseActivity implements IView, IUserLoginOffline {

    protected String TAG = getClass().getSimpleName();
    protected Context context;
    //protected Presenter presenter;
    protected ACProgressFlower progressDialog;
    private ACProgressPie progressDialog2;//有进度

//    //是否获取转账信息
//    public boolean mTranferInfo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        context = this;
        ButterKnife.bind(this);
        //禁止横屏
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initStatusBar(getWindow().getDecorView());
        HermesEventBus.getDefault().register(this);
        CacheActivityUtil.addActivity(this);
        init(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        WeiLaiFuApplication.initUserLoginOffline(this);//注册 单点登录 监控器
//        if (presenter == null && getPresenter() != null) {
//            presenter = getPresenter();
//        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        //WeiLaiFuApplication.relaseUserLoginOffline();//释放 单点登录 监控器
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        CacheActivityUtil.removeActivity(this);
        HermesEventBus.getDefault().unregister(this);

        if (getPresenter() != null) {
            getPresenter().onDestroy();
        }

        super.onDestroy();


    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_in_from_right,
                R.anim.slide_out_to_left);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_from_left,
                R.anim.slide_out_to_right);
    }

    @Override
    public void onUserLoginOffline() {
        AppUtil.openLoginOfflineDialog(this);
    }

    public void initStatusBar(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final View view = v.findViewById(R.id.title_bar);
            if (view != null) {
                view.getLayoutParams().height = (int) (statusBarHeight + getResources().getDimension(R.dimen.title_bar_height));
                view.setPadding(view.getPaddingLeft(), view.getPaddingTop() + statusBarHeight, view.getPaddingRight(), view.getPaddingBottom());
            }
        }
    }

    public boolean isEmpty(String var, String msg) {
        if (TextUtils.isEmpty(var)) {
            showToast(msg + "不能为空");
            return true;
        }
        return false;
    }

    public boolean isEmpty(String var, int msgID) {
        String msg = getResourcesString(msgID);
        return isEmpty(var, msg);
    }

    public String getResourcesString(int id) {
        return getResources().getString(id);
    }

    public void showProgressDialog(String msg) {
        progressDialog = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text(msg)
                .fadeColor(Color.DKGRAY).build();

        progressDialog.show();
    }

    public void showProgressDialog(int msgID) {
        showProgressDialog(getResourcesString(msgID));
    }

    public void showProgressDialog() {
        progressDialog = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .fadeColor(Color.DKGRAY).build();

        progressDialog.show();
    }

    public void hideProgressDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    public void showProgressDialog2() {
        progressDialog2 = new ACProgressPie.Builder(this)
                .ringColor(Color.WHITE)
                .pieColor(Color.WHITE)
                .updateType(ACProgressConstant.PIE_MANUAL_UPDATE)
                .build();
        progressDialog2.show();

    }

    public void setProgressDialog2(float percentage) {
        if (progressDialog2 != null)
            progressDialog2.setPiePercentage(percentage);
    }

    public void hideProgressDialog2() {
        if (progressDialog2 != null)
            progressDialog2.dismiss();
    }

    /**
     * 转账
     *
     * @param tranfer
     */
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void TranferInfo(Tranfer tranfer) {

    }

    @Override
    public void onSuccess(BaseSerializable data) {
    }

    @Override
    public void onFail(int code, String msg) {

    }

    public void showToast(String msg) {
        To.s(msg);
    }

    public void showToast(int msgID) {
        To.s(msgID);
    }

    public Context getContext() {
        return context;
    }

    public void delayFinish(int time) {
        TimerUtil.startTimer(time, new TimerUtil.TimerCallBackListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onEnd() {
                finish();
            }
        });
    }

    protected abstract int getLayoutId();

    protected abstract Presenter getPresenter();

    protected abstract void init(Bundle savedInstanceState);

}
