package com.futuretongfu.ui.live_video;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.futuretongfu.R;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.fragment.BaseFragment;

public class Live_Fragment extends BaseFragment {

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.live_fragment;
    }

    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    }
}