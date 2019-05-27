package com.futuretongfu.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.futuretongfu.base.BaseSerializable;
import com.futuretongfu.bean.entity.StoreInfo;
import com.futuretongfu.iview.IView;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.utils.To;
import com.futuretongfu.bean.entity.Tranfer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import xiaofei.library.hermeseventbus.HermesEventBus;

/**
 * Created by ChenXiaoPeng on 2017/6/8.
 */

public abstract class BaseFragment extends RxBaseFragment implements IView {

    protected Presenter presenter;

    protected String TAG = getClass().getSimpleName();

    private boolean isRegester = false;
    protected ACProgressFlower progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), null);
        ButterKnife.bind(this, view);
        getBaseActivity().initStatusBar(view);
        init(inflater, container, savedInstanceState);
        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isRegester) {
            isRegester = true;
            HermesEventBus.getDefault().register(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter == null && getPresenter() != null) {
            presenter = getPresenter();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        HermesEventBus.getDefault().unregister(this);
        isRegester = false;
    }

    @Override
    public void onDestroy() {

        if (getPresenter() != null) {
            getPresenter().onDestroy();
        }

        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    /**
     * 转账
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void TranferInfo(Tranfer tranfer) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStoreInfo(StoreInfo storeInfo) {

    }

    @Override
    public void onSuccess(BaseSerializable data) {

    }

    @Override
    public void onFail(int code, String msg) {

    }

    public void showToast(String msg) {
        if (!"null".equals(msg)) {
            To.s(msg);
        }
    }

    public void showToast(int msgID) {
        To.s(getResourcesString(msgID));
    }

    public String getResourcesString(int id) {
        return getResources().getString(id);
    }

    public void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new ACProgressFlower.Builder(getContext())
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(Color.WHITE)
                    .text(msg)
                    .fadeColor(Color.DKGRAY).build();
        }

        progressDialog.show();
    }


    public void showProgressDialog(int msgID) {
        showProgressDialog(getResourcesString(msgID));
    }

    public void showProgressDialog() {
        progressDialog = new ACProgressFlower.Builder(getContext())
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .fadeColor(Color.DKGRAY).build();

        progressDialog.show();
    }

    public void hideProgressDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void startActivity(Intent intent) {
        getActivity().startActivity(intent);
    }

    protected abstract Presenter getPresenter();

    protected abstract int getLayoutId();

    protected abstract void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

}
