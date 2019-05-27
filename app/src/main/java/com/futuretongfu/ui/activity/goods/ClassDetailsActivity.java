package com.futuretongfu.ui.activity.goods;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.bean.onlinegoods.HomeSortBean;
import com.futuretongfu.iview.IClassDetailsView;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.goods.ClassDetailsPresenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.adapter.FragAdapter;
import com.futuretongfu.ui.fragment.BaseFragment;
import com.futuretongfu.ui.fragment.goods.ClassDetailsFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zhanggf on 2018/5/11.
 * 分类详情
 */

public class ClassDetailsActivity extends BaseActivity implements IClassDetailsView {

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tablayout)
    TabLayout tablayout;
    @Bind(R.id.vp_aclass_details)
    ViewPager vpAclassDetails;
    private ClassDetailsPresenter presenter;
    private String classId,className;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_class_details;
    }

    @Override
    protected Presenter getPresenter() {
        return presenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        classId = getIntent().getStringExtra("classId");
        className = getIntent().getStringExtra("className");
        tvTitle.setText(className+"分类");
        presenter = new ClassDetailsPresenter(this, this);
        presenter.onSortList(classId);
    }

    private void initView(List<HomeSortBean> list) {
        List<BaseFragment> fragments = new ArrayList<>();
        String[] array = new String[list.size()];
        // List转换成数组
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i).getCategoryName();
            ClassDetailsFragment sortFragment = new ClassDetailsFragment();
            sortFragment.setType(list.get(i).getCategoryCode());
            fragments.add(sortFragment);
        }
        vpAclassDetails.setAdapter(new FragAdapter(getSupportFragmentManager(), fragments, array));
        tablayout.setupWithViewPager(vpAclassDetails);
        tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //TabLayout的切换监听
    }

    @OnClick(R.id.bt_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onSortListFail(int code, String msg) {
        showToast(msg);

    }

    @Override
    public void onSortListSuccess(List<HomeSortBean> data) {
        initView(data);
    }
}
