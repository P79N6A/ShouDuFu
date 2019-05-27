package com.futuretongfu.ui.activity.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.model.eventType.UploadVoucheEventType;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.adapter.FragAdapter;
import com.futuretongfu.ui.component.dialog.DateDialog;
import com.futuretongfu.ui.fragment.BaseFragment;
import com.futuretongfu.ui.fragment.UploadVoucheFragment;
import com.futuretongfu.utils.DateUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 消费增值列表
 *
 * @author zhanggf
 */
public class UploadVocucheListActivity extends BaseActivity  {
    @Bind(R.id.tv_title)
    public TextView textTitle;
    @Bind(R.id.bt_add)
    public ImageView bt_add;
    @Bind(R.id.tabs_order)
    public TabLayout tabOrder;
    @Bind(R.id.pagers_order)
    public ViewPager pagerOrder;
    @Bind(R.id.text_time)
    public TextView textTime;
    private int selectYear;
    private int selectMonth;
    private String createTime;
    /***********************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_upload_vocuchelist;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        textTitle.setText("上传明细");
        Calendar today = Calendar.getInstance();
        selectYear = today.get(Calendar.YEAR);
        selectMonth = today.get(Calendar.MONTH) + 1;
        String str = String.format("%02d", selectMonth);
        createTime = "" + selectYear + str;
        textTime.setText(selectYear + "年" + str + "月");
        List<BaseFragment> fragments = new ArrayList<>();
        UploadVoucheFragment order0 = new UploadVoucheFragment();
        UploadVoucheFragment order1 = new UploadVoucheFragment();
        UploadVoucheFragment order2 = new UploadVoucheFragment();
        UploadVoucheFragment order3 = new UploadVoucheFragment();
        UploadVoucheFragment order4 = new UploadVoucheFragment();
        order0.setOrderConsumerStatus(Constants.OrderCheckStatus_Examine_All,createTime);//全部
        order1.setOrderConsumerStatus(Constants.OrderCheckStatus_Examine_Doing,createTime);//待审核
        order2.setOrderConsumerStatus(Constants.OrderCheckStatus_Examine_Success,createTime);//审核通过待付款
        order3.setOrderConsumerStatus(Constants.OrderCheckStatus_Examine_Faile,createTime);//审核失败
        order4.setOrderConsumerStatus(Constants.OrderCheckStatus_Examine_Finish,createTime);//已完成
        fragments.add(order0);
        fragments.add(order1);
        fragments.add(order2);
        fragments.add(order3);
        fragments.add(order4);
        pagerOrder.setAdapter(new FragAdapter(getSupportFragmentManager(), fragments, new String[]{ "全部","待审核", "待付款", "审核拒绝", "已完成"}));
        tabOrder.setupWithViewPager(pagerOrder);
        pagerOrder.setOffscreenPageLimit(2);
//        pagerOrder.setCurrentItem(type);
    }


    //返回按键
    @Override
    public void onBackPressed() {
        boolean success = getSupportFragmentManager().popBackStackImmediate();
        if (!success)
            super.onBackPressed();
    }

    /***********************************************************************/

    public static void startActivity(Context context, int type) {
        Intent intent = new Intent(context, UploadVocucheListActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @OnClick({R.id.bt_back, R.id.bt_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;
            case R.id.bt_add:
                UploadVoucheActivity.startActivity(context,false);
                break;
        }
    }

    //时间
    @OnClick(R.id.imgv_time)
    public void onClickTime() {
        DateDialog dlg = new DateDialog(getContext(), selectYear, selectMonth, new DateDialog.DateDialogListener() {
            public void onDateDialogConfirm(int year, int month) {
                selectYear = year;
                selectMonth = month;
                String str = String.format("%02d", selectMonth);
                createTime = "" + selectYear + str;
                textTime.setText(year + "年" +  str+ "月");
                EventBus.getDefault().post(createTime);
            }
        });
        dlg.show();
    }

}
