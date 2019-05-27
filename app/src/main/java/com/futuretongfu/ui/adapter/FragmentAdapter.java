package com.futuretongfu.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.futuretongfu.R;

import java.util.List;

/**
 * Fragment切换adapter
 */
public class FragmentAdapter {

    private List<Fragment> fragments;
    private FragmentActivity fragmentActivity; // Fragment所属的Activity

    private int fragmentContentId; // Activity中所要被替换的区域的id

    private int currentTab; // 当前Tab页面索引

    private int index=0;

    public FragmentAdapter(FragmentActivity fragmentActivity,
                           List<Fragment> fragments, int fragmentContentId) {
        this.fragmentActivity = fragmentActivity;
        this.fragments = fragments;
        this.fragmentContentId = fragmentContentId;

        // 默认显示第一页
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager()
                .beginTransaction();

        ft.add(fragmentContentId, fragments.get(index));
        ft.commit();
    }

    public FragmentAdapter(FragmentActivity fragmentActivity,
                           List<Fragment> fragments, int fragmentContentId, int index) {
        this.fragmentActivity = fragmentActivity;
        this.fragments = fragments;
        this.fragmentContentId = fragmentContentId;
        this.index = index;

        // 默认显示第一页
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager()
                .beginTransaction();

        ft.add(fragmentContentId, fragments.get(index));
        ft.commit();
    }

    public void onChange(int index) {
        Fragment fragment = fragments.get(index);
        FragmentTransaction ft = obtainFragmentTransaction(index);

        getCurrentFragment().onPause(); // 暂停当前tab
        getCurrentFragment().onStop(); // 暂停当前tab

        if (fragment.isAdded()) {
            fragment.onStart(); // 启动目标tab的onStart()
            fragment.onResume(); // 启动目标tab的onResume()
        } else {
            ft.add(fragmentContentId, fragment);
        }
        showTab(index); // 显示目标tab
        ft.commit();

        currentTab = index;
    }

    /**
     * 切换tab
     *
     * @param idx 当前tab
     */
    private void showTab(int idx) {
        for (int i = 0; i < fragments.size(); i++) {
            Fragment fragment = fragments.get(i);
            FragmentTransaction ft = obtainFragmentTransaction(idx);
            if (idx == i) {
                ft.show(fragment);
            } else {
                ft.hide(fragment);
            }
            ft.commit();
        }
        currentTab = idx; // 更新目标tab为当前tab
    }

    /**
     * 获取一个带动画的FragmentTransaction
     *
     * @param index 动画
     * @return 动画
     */
    private FragmentTransaction obtainFragmentTransaction(int index) {
        FragmentTransaction ft = fragmentActivity.getSupportFragmentManager()
                .beginTransaction();
        // 设置切换动画
        if (index > currentTab) {
            ft.setCustomAnimations(R.anim.slide_in_from_right,
                    R.anim.slide_out_to_left);
        } else {
            ft.setCustomAnimations(R.anim.slide_in_from_left,
                    R.anim.slide_out_to_right);
        }
        return ft;
    }

    public Fragment getCurrentFragment() {
        return fragments.get(currentTab);
    }

}
