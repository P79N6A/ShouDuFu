package com.futuretongfu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;

import com.futuretongfu.R;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.fragment.BaseFragment;
import com.futuretongfu.ui.fragment.FeedbackFragment;
import com.futuretongfu.ui.fragment.HelpFragment;
import com.futuretongfu.ui.fragment.OfflineAttentionFragment;
import com.futuretongfu.ui.fragment.OneLineAttentionFragment;
import com.futuretongfu.view.SegmentView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhanggf on 2018/5/10.
 * 帮助与反馈
 */

public class HelpActivity extends BaseActivity {
    private static final int MY_SHANGCHENG = 0, MY_SHANGQUAN = 1;
    @Bind(R.id.segmentview)
    SegmentView segmentview;
    @Bind(R.id.fl_layout)
    FrameLayout flLayout;
    private List<BaseFragment> fragments;
    private int lastFragment = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_help;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        fragments = new ArrayList<>();
        fragments.add(new HelpFragment());
        fragments.add(new FeedbackFragment());
        //默认选中首页
        changeFragment(MY_SHANGCHENG);
        segmentview.setSegmentText("帮助", 0);
        segmentview.setSegmentText("反馈", 1);
        segmentview.setSegmentTextSize(14);
        segmentview.setOnSegmentViewClickListener(new SegmentView.onSegmentViewClickListener() {
            @Override
            public void onSegmentViewClick(View v, int position) {
                if (position == 0) {
                    changeFragment(MY_SHANGCHENG);
                } else {
                    changeFragment(MY_SHANGQUAN);
                }
            }
        });

    }
    /**
     * 切换界面
     * @param tag fragment tag
     */
    private void changeFragment(int tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (lastFragment != -1) {
            transaction.hide(fragments.get(lastFragment));
        }
        lastFragment = tag;
        if (!fragments.get(tag).isAdded()) {
            transaction.add(R.id.fl_layout, fragments.get(tag));
        }
        transaction.show(fragments.get(tag));
        transaction.commitAllowingStateLoss();
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, HelpActivity.class);
        context.startActivity(intent);
    }

    @OnClick(R.id.bt_back)
    public void onViewClicked() {
    }
}
