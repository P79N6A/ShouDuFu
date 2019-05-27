package com.futuretongfu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.widget.TextView;

import com.futuretongfu.iview.IRecommendHeadView;
import com.futuretongfu.model.entity.RecommendHeadInfo;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.user.RecommendPresenter;
import com.futuretongfu.ui.adapter.FragAdapter;
import com.futuretongfu.ui.component.ScrollViewPager;
import com.futuretongfu.ui.fragment.BaseFragment;
import com.futuretongfu.ui.fragment.recommend.RecommendFragmentCK;
import com.futuretongfu.ui.fragment.recommend.RecommendFragmentCT;
import com.futuretongfu.ui.fragment.recommend.RecommendFragmentCY;
import com.futuretongfu.ui.fragment.recommend.RecommendFragmentTK;
import com.futuretongfu.ui.fragment.recommend.RecommendFragmentXC;
import com.futuretongfu.utils.StringUtil;
import com.futuretongfu.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 我的推荐
 *
 * @author ChenXiaoPeng
 */
public class MyRecommendActivity extends BaseActivity implements ViewPager.OnPageChangeListener, IRecommendHeadView {

    @Bind(R.id.tv_title)
    public TextView textTitle;
    @Bind(R.id.text_rmb)
    public TextView rmbView;
    @Bind(R.id.text_points)
    public TextView pointsView;
    @Bind(R.id.tabs_recommend)
    public TabLayout tabsRecommend;
    @Bind(R.id.pager_recommend)
    public ScrollViewPager pagerRecommend;
    @Bind(R.id.scroll)
    public NestedScrollView scroll;

    private static FragAdapter fragAdapter;
    private RecommendPresenter recommendPresenter;
//    public static String[] titleStr = new String[]{"享客(0)", "拓客(0)", "创客(0)", "创投(0)", "联合代理(0)"};
    public static String[] titleStr = new String[]{"免费(0)","拓客(0)", "创客(0)", "创业(0)","创投(0)"};


    /***********************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_recommend;
    }

    @Override
    protected Presenter getPresenter() {
        return recommendPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        textTitle.setText("我的推荐");
        recommendPresenter = new RecommendPresenter(this, this);
        List<BaseFragment> fragments = new ArrayList<>();
        RecommendFragmentXC recommendFragmentXC = new RecommendFragmentXC();
        RecommendFragmentTK recommendFragment1 = new RecommendFragmentTK();
        RecommendFragmentCK recommendFragment2 = new RecommendFragmentCK();
        RecommendFragmentCY recommendFragment3 = new RecommendFragmentCY();
        RecommendFragmentCT recommendFragment4 = new RecommendFragmentCT();
//        RecommendFragmentLH recommendFragment4 = new RecommendFragmentLH();
        fragments.add(recommendFragmentXC);
        fragments.add(recommendFragment1);
        fragments.add(recommendFragment2);
        fragments.add(recommendFragment3);
        fragments.add(recommendFragment4);
        fragAdapter = new FragAdapter(getSupportFragmentManager(), fragments, titleStr);
        recommendPresenter.getRecommentHead(UserManager.getInstance().getUserId() + "");
        showProgressDialog();
        pagerRecommend.setAdapter(fragAdapter);
        tabsRecommend.setupWithViewPager(pagerRecommend);
//        tabsRecommend.setTabMode(TabLayout.MODE_SCROLLABLE);
        pagerRecommend.addOnPageChangeListener(this);
    }


    /**
     * 设置
     *
     * @param index
     * @param title
     */
    public  void setTitlesForIndex(int index, String title) {
        titleStr[index] = title;

    }


    //返回按键
    @Override
    public void onBackPressed() {
        boolean success = getSupportFragmentManager().popBackStackImmediate();
        if (!success)
            super.onBackPressed();
    }

    /***********************************************************************/
    @OnClick(R.id.bt_back)
    public void onClickBack() {
        finish();
    }

    /***********************************************************************/
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /***********************************************************************/

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MyRecommendActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onRecommendHeadSuccess(RecommendHeadInfo recommendHeadInfo) {
        hideProgressDialog();
        rmbView.setText(StringUtil.fmtMicrometer(Double.parseDouble(recommendHeadInfo.getCash())));
        pointsView.setText(StringUtil.fmtMicrometer(Double.parseDouble(recommendHeadInfo.getJifen())));
        if(!TextUtils.isEmpty(recommendHeadInfo.getHarker())){
            setTitlesForIndex(0,"免费("+recommendHeadInfo.getHarker()+")");
        }
        if(!TextUtils.isEmpty(recommendHeadInfo.getComMember())){
            setTitlesForIndex(1,"拓客("+recommendHeadInfo.getComMember()+")");
        }
        if(!TextUtils.isEmpty(recommendHeadInfo.getMaker())){
            setTitlesForIndex(2,"创客("+recommendHeadInfo.getMaker()+")");
        }
        if(!TextUtils.isEmpty(recommendHeadInfo.getMaker())){
            setTitlesForIndex(3,"创业("+recommendHeadInfo.getCymaker()+")");
        }
        if(!TextUtils.isEmpty(recommendHeadInfo.getVenture())){
            setTitlesForIndex(4,"创投("+recommendHeadInfo.getVenture()+")");
        }
//        if(!TextUtils.isEmpty(recommendHeadInfo.getAgent())){
//            setTitlesForIndex(4,"联合代理("+recommendHeadInfo.getAgent()+")");
//        }
        fragAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRecommendHeadFaile(String msg) {
        hideProgressDialog();
        //showToast(msg);
    }
}
