package com.futuretongfu.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.futuretongfu.R;
import com.futuretongfu.presenter.Presenter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhanggf on 2018/3/28.
 * 反馈
 */

public class FeedbackFragment extends BaseFragment {


    @Bind(R.id.et_content)
    EditText etContent;

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_feedback;
    }

    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    }


    @OnClick(R.id.btn_commit)
    public void onViewClicked() {
    }
}
