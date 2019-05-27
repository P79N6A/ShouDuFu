package com.futuretongfu.ui.activity.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.RequestCode;
import com.futuretongfu.iview.IScoreDetailView;
import com.futuretongfu.model.entity.Score;
import com.futuretongfu.model.manager.SysDataManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.account.ScoreDetailPresenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.activity.MyBillActivity;
import com.futuretongfu.ui.adapter.FragAdapter;
import com.futuretongfu.ui.component.ScrollViewPager;
import com.futuretongfu.ui.fragment.BaseFragment;
import com.futuretongfu.ui.fragment.TongbaiDetailsFragment;
import com.futuretongfu.ui.fragment.TongbaiExchangeFragment;
import com.futuretongfu.utils.StringUtil;
import com.futuretongfu.view.SegmentView2;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 通贝管理---备份
 *
 * @author ChenXiaoPeng
 */
public class ScoreDetailActivity2 extends BaseActivity implements IScoreDetailView, ViewPager.OnPageChangeListener {

    @Bind(R.id.segmentview)
    SegmentView2 segmentview;
    @Bind(R.id.imgv_eye)
    public ImageView imgvEye;

    @Bind(R.id.text_amount)
    public TextView textAmount;
    @Bind(R.id.text_inter_avlbal)
    public TextView textInterAvlbal;
    @Bind(R.id.tool_bar)
    Toolbar toolBar;
    @Bind(R.id.tabs_recommend)
    TabLayout tabsRecommend;
    @Bind(R.id.app_bar)
    AppBarLayout appBar;
    @Bind(R.id.pager_recommend)
    ScrollViewPager pagerRecommend;
    @Bind(R.id.scroll)
    NestedScrollView scroll;

    private boolean isEyeOpen = true;
    private Score score;
    private ScoreDetailPresenter scoreDetailPresenter;
    private final static String Intent_Extra_Type = "type";
    private static FragAdapter fragAdapter;
    public static String[] titleStr = new String[]{"通贝明细", "通贝兑换"};

    /***********************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_score_detail2;
    }

    @Override
    protected Presenter getPresenter() {
        return scoreDetailPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        segmentview.setSegmentText("商城通贝",0);
        segmentview.setSegmentText("商圈通贝",1);
        segmentview.setSegmentTextSize(14);
        segmentview.setOnSegmentViewClickListener(new SegmentView2.onSegmentViewClickListener() {
            @Override
            public void onSegmentViewClick(View v, int position) {
                if (position==0){
                    showToast("商城通贝");
//                    changeFragment(MY_SHANGCHENG);
                }else {
                    showToast("商圈通贝");
//                    changeFragment(MY_SHANGQUAN);
                }
            }
        });
        initView();
        scoreDetailPresenter = new ScoreDetailPresenter(this, this);

        isEyeOpen = SysDataManager.getInstance().isEyeScoreDetail();
        updateView(isEyeOpen);
    }

    private void initView() {
        List<BaseFragment> fragments = new ArrayList<>();
        TongbaiDetailsFragment Fragment1 = new TongbaiDetailsFragment();
        TongbaiExchangeFragment Fragment2 = new TongbaiExchangeFragment();
        fragments.add(Fragment1);
        fragments.add(Fragment2);
        fragAdapter = new FragAdapter(getSupportFragmentManager(), fragments, titleStr);
//        showProgressDialog();
        pagerRecommend.setAdapter(fragAdapter);
        tabsRecommend.setupWithViewPager(pagerRecommend);
        pagerRecommend.addOnPageChangeListener(this);
    }

    //返回按键
    @Override
    public void onBackPressed() {
        boolean success = getSupportFragmentManager().popBackStackImmediate();
        if (!success)
            super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();

        scoreDetailPresenter.score();
    }



    /***********************************************************************/
    @Override
    public void onScoreDetailSuccess(Score data) {
        this.score = data;
        updateView();
    }

    @Override
    public void onScoreDetailFaile(String msg) {
        showToast(msg);
    }

    /***********************************************************************/

    private void updateView(boolean isEyeOpen) {
        if (isEyeOpen) {
            imgvEye.setImageDrawable(getResources().getDrawable(R.mipmap.icon_visible));
        } else {
            imgvEye.setImageDrawable(getResources().getDrawable(R.mipmap.icon_invisible));
        }

        updateView();
    }

    private void updateView() {
        if (isEyeOpen) {
            if (null == score) {
                textAmount.setText("0.00");
                textInterAvlbal.setText("0.00");
                return;
            }
            textAmount.setText(StringUtil.fmtMicrometer(score.getAvlBal()));
            textInterAvlbal.setText(StringUtil.fmtMicrometer(score.getInterAvlbal()));
        } else {
            textAmount.setText("******");
            textInterAvlbal.setText("***");
        }
    }

    public static void startActivity(Context context, int type) {
        Intent intent = new Intent(context, ScoreDetailActivity2.class);
        intent.putExtra(Intent_Extra_Type, type);
        ((Activity) context).startActivityForResult(intent, RequestCode.REQUEST_CODE_JLTYJ);
    }


    @OnClick({R.id.bt_back, R.id.text_amount, R.id.btn_bill,R.id.imgv_eye})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;
            case R.id.text_amount:
                break;
            case R.id.imgv_eye:
                this.isEyeOpen = !isEyeOpen;
                SysDataManager.getInstance().setEyeScoreDetail(isEyeOpen);
                SysDataManager.getInstance().save();

                updateView(isEyeOpen);
                break;
            case R.id.btn_bill:
                MyBillActivity.startActivity(this, Constants.Bill_Type_Jifen);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ScoreDetailActivity2.class);
        ((Activity) context).startActivityForResult(intent, RequestCode.REQUEST_CODE_JLTYJ);
    }
}
