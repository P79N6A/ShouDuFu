package com.futuretongfu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.fragment.BaseFragment;
import com.futuretongfu.ui.fragment.OfflineAttentionFragment;
import com.futuretongfu.ui.fragment.OneLineAttentionFragment;
import com.futuretongfu.view.SegmentView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zhanggf on 2018/5/3.
 */

public class MyAttentionActivity extends BaseActivity {
    private static final int MY_SHANGCHENG = 0, MY_SHANGQUAN = 1;
    @Bind(R.id.tv_edit)
    TextView tvEdit;
    @Bind(R.id.segmentview)
    SegmentView segmentview;
    private List<BaseFragment> fragments;
    private int lastFragment = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_attention;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        fragments = new ArrayList<>();
        fragments.add(new OneLineAttentionFragment());
        fragments.add(new OfflineAttentionFragment());
        //默认选中首页
        changeFragment(MY_SHANGCHENG);
        segmentview.setSegmentText("商 城",0);
        segmentview.setSegmentText("商 圈",1);
        segmentview.setSegmentTextSize(14);
        segmentview.setOnSegmentViewClickListener(new SegmentView.onSegmentViewClickListener() {
            @Override
            public void onSegmentViewClick(View v, int position) {
                if (position==0){
                    changeFragment(MY_SHANGCHENG);
                }else {
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
            transaction.add(R.id.fl_my_attention, fragments.get(tag));
        }
        transaction.show(fragments.get(tag));
        transaction.commitAllowingStateLoss();
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MyAttentionActivity.class);
        context.startActivity(intent);
    }


    @OnClick({R.id.bt_back, R.id.img_search, R.id.tv_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;
            case R.id.img_search:
                break;
            case R.id.tv_edit:
                break;
        }
    }
}
