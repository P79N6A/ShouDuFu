package com.futuretongfu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.iview.IBillDetailView;
import com.futuretongfu.model.entity.BillDetail;
import com.futuretongfu.model.entity.MyBillEntity;
import com.futuretongfu.presenter.BillDetailPresenter;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.activity.goods.OrderOnlineDetailsActivity;
import com.futuretongfu.ui.activity.order.OrderConsumerDetailActivity;
import com.futuretongfu.ui.component.dialog.BillScreenDialog;
import com.futuretongfu.utils.DateUtil;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.StringUtil;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 账单详情
 *
 * @author ChenXiaoPeng
 * ss
 */
public class BillDetailActivity extends BaseActivity implements IBillDetailView {

    private final static String Intent_Extra_BusinessNo = "businessNo";
    private final static String Intent_Extra_BusinessType = "businessType";
    private final static String Intent_Extra_CurScreenType = "curScreenType";
    private final static String Intent_Extra_BusinessEntoty = "businessEntoty";

    @Bind(R.id.tv_title)
    public TextView textTitle;

    @Bind(R.id.text_money_title)
    public TextView textMoneyTitle;
    @Bind(R.id.text_memony)
    public TextView textMoney;

    @Bind(R.id.view_1)
    public LinearLayout view1;
    @Bind(R.id.text_title_1)
    public TextView textTitle1;
    @Bind(R.id.text_1)
    public TextView text1;

    @Bind(R.id.view_2)
    public LinearLayout view2;
    @Bind(R.id.text_title_2)
    public TextView textTitle2;
    @Bind(R.id.text_2)
    public TextView text2;

    @Bind(R.id.view_3)
    public LinearLayout view3;
    @Bind(R.id.text_title_3)
    public TextView textTitle3;
    @Bind(R.id.text_3)
    public TextView text3;

    @Bind(R.id.view_4)
    public LinearLayout view4;
    @Bind(R.id.text_title_4)
    public TextView textTitle4;
    @Bind(R.id.text_4)
    public TextView text4;

    @Bind(R.id.view_5)
    public LinearLayout view5;
    @Bind(R.id.text_title_5)
    public TextView textTitle5;
    @Bind(R.id.text_5)
    public TextView text5;

    @Bind(R.id.view_6)
    public LinearLayout view6;
    @Bind(R.id.text_title_6)
    public TextView textTitle6;
    @Bind(R.id.text_6)
    public TextView text6;

    @Bind(R.id.view_7)
    public LinearLayout view7;
    @Bind(R.id.text_title_7)
    public TextView textTitle7;
    @Bind(R.id.text_7)
    public TextView text7;
    @Bind(R.id.layout_order_detail)
    public FrameLayout orderLayout;

    private String businessNo;
    private String businessType;
    private int currType = -1;
    private BillDetail billDetail;

    /***********************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_bill_detail;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        textTitle.setText("账单详情");
        BillDetailPresenter billDetailPresenter = new BillDetailPresenter(this, this);
        Intent intent = getIntent();
        businessNo = intent.getStringExtra(Intent_Extra_BusinessNo);
        businessType = intent.getStringExtra(Intent_Extra_BusinessType);
        currType = intent.getIntExtra(Intent_Extra_CurScreenType, -1);
        if (Constants.BusinessType_OrderPayCash.equals(businessType)) {
            if (BillScreenDialog.Type_FSort == currType) {
                billDetailPresenter.getJFDetail(businessNo, businessType);
                return;
            } else if (BillScreenDialog.Type_PaymentBalance == currType) {
                billDetailPresenter.getBillDetail(businessNo, businessType);
                return;
            }
        }
        if (BillScreenDialog.Type_FSort == currType) {
            billDetailPresenter.getJFDetail(businessNo, businessType);
            return;
        }
        billDetailPresenter.getBillDetail(businessNo, businessType);
    }

    @Override
    public void onResume() {
        super.onResume();
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

    @OnClick(R.id.layout_order_detail)
    public void onClickBillDetail() {
        if (Constants.BusinessType_OrderPayCash.equals(businessType)) {
            try {
                long l = Long.valueOf(businessNo);
                OrderConsumerDetailActivity.startActivity(context, l);
                finish();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }else if (Constants.BusinessType_OnlineOrder.equals(businessType)){
            IntentUtil.startActivity(context,OrderOnlineDetailsActivity.class,"id",billDetail.getOnlineOrderId(),"onlineOrderNo",billDetail.getOrderNo()+"",
                    "orderStatus",billDetail.getOrderStatus()+"");
        }
    }

    /***********************************************************************/
    @Override
    public void onBillDetailSuccess(BillDetail data) {
        this.billDetail = data;
        updateView();
    }

    @Override
    public void onBillDetailFaile(String msg) {
        showToast(msg);
    }

    /***********************************************************************/

    private void updateView() {
        if (null == billDetail)
            return;

        switch (businessType) {
            case Constants.BusinessType_Tk_Str://退款
                updateViewSqTk();
                break;

            case Constants.BusinessType_Sqzr_Str://商圈转入
                updateViewSqZr();
                break;

            case Constants.BusinessType_Sqzc_Str://商圈转出
                updateViewSqZC();
                break;

            case Constants.BusinessType_Cz_Str://充值
                updateViewChongZhi();
                break;

            case Constants.BusinessType_Tx_Str://提现
                updateViewTiXian();
                break;

            case Constants.BusinessType_Zz_Str2://转出 大写
            case Constants.BusinessType_Zz_Str://转出
                updateViewZz();
                break;

            case Constants.BusinessType_Sk_Str://转入
                updateViewSk();
                break;
            case Constants.BusinessType_OrderPayCash://订单现金支付
                if (BillScreenDialog.Type_FSort != currType) {
                    updateViewOrderPayCash();
                }
                break;
            case Constants.BusinessType_Role_Updata_Str://身份升级支付
                if (BillScreenDialog.Type_FSort != currType) {
                    updateViewUpdate();
                }
                break;
            case Constants.BusinessType_Cash_Back_Str://返现
                cashBackView();
                break;

            case Constants.BusinessType_Update_Invite_Cash://被邀请人升级奖励现金
                updateViewCash();
                break;

            case Constants.BusinessType_jtj_Cash://辅导津贴
                updateViewCash();
                break;
            case Constants.BusinessType_OnlineOrder://线上商城订单支付
                updateOnlineOrder();
                break;
            case Constants.BusinessType_invite_turn_out:
                updateViewSqZC();
                break;
            default:
                updateViewOther();
                break;
        }
        if (Constants.BusinessType_OrderPayCash.equals(businessType)||Constants.BusinessType_OnlineOrder.equals(businessType)) {
            orderLayout.setVisibility(View.VISIBLE);
        }
        if (BillScreenDialog.Type_FSort == currType) {
            updateViewJF();
        }

    }

    //商圈转出  buss_turn_out
    private void updateViewSqZC() {
        textMoneyTitle.setText("转出金额");
        textMoney.setText("￥" + StringUtil.getDouble2(billDetail.getMoney()));
        view1.setVisibility(View.VISIBLE);
        textTitle1.setText("转出至");
        text1.setText("余额");
        view2.setVisibility(View.VISIBLE);
        textTitle2.setText("当前状态");
        text2.setText(billDetail.getCashStatus() == 1 ? "成功" : "失败");
        view3.setVisibility(View.VISIBLE);
        textTitle3.setText("转出时间");
        text3.setText(DateUtil.getDateStrWithHour1(billDetail.getCreateTime()));
    }

    //商圈转出  buss_turn_out
    private void cashBackView() {
        textMoneyTitle.setText("返现金额");
        textMoney.setText("￥" + StringUtil.getDouble2(billDetail.getMoney()));
        view1.setVisibility(View.VISIBLE);
        textTitle1.setText("当前状态");
        text1.setText(billDetail.getCashStatus() == 1 ? "返现成功" : "返现失败");
        view2.setVisibility(View.VISIBLE);
        textTitle2.setText("返现时间");
        text2.setText(DateUtil.getDateStrWithHour1(billDetail.getCreateTime()));
        view3.setVisibility(View.VISIBLE);
        textTitle3.setText("返现单号");
        text3.setText(billDetail.getBusinessNo());
    }

    //奖励津贴.
    private void updateViewCash() {
        textMoneyTitle.setText("奖励金额");
        textMoney.setText("￥" + StringUtil.getDouble2(billDetail.getMoney()));
        view1.setVisibility(View.VISIBLE);
        textTitle1.setText("奖励类型");
        text1.setText(billDetail.getRemark());
        view2.setVisibility(View.VISIBLE);
        textTitle2.setText("当前状态");
        text2.setText(billDetail.getCashStatus() == 1 ? "成功" : "失败");
        view3.setVisibility(View.VISIBLE);
        textTitle3.setText("奖励时间");
        text3.setText(DateUtil.getDateStrWithHour1(billDetail.getCreateTime()));
        view4.setVisibility(View.VISIBLE);
        textTitle4.setText("交易单号");
        text4.setText(billDetail.getBusinessNo());
    }
    //线上商城订单
    private void updateOnlineOrder() {
        textMoneyTitle.setText("支付金额");
        textMoney.setText("￥" + StringUtil.getDouble2(billDetail.getRealMoney()));
        view1.setVisibility(View.VISIBLE);
        textTitle1.setText("商家名称");
        text1.setText(billDetail.getStoreName());
        view2.setVisibility(View.VISIBLE);
        textTitle2.setText("当前状态");
        text2.setText(StringUtil.getOrderOnlineStatues(billDetail.getOrderStatus()));
        view3.setVisibility(View.VISIBLE);
        textTitle3.setText("支付时间");
        text3.setText(DateUtil.getDateStrWithHour1(billDetail.getCreateTime()));
        view4.setVisibility(View.VISIBLE);
        textTitle4.setText("支付方式");
        text4.setText(StringUtil.getSafeString(StringUtil.getOrderPayCashPayType(billDetail.getPayType())));
        view5.setVisibility(View.VISIBLE);
        textTitle5.setText("交易单号");
        text5.setText(billDetail.getOrderNo()+"");
    }

    //其他
    private void updateViewOther() {
        textMoneyTitle.setText("奖励金额");
        textMoney.setText("￥" + StringUtil.getDouble2(billDetail.getMoney()));
        view1.setVisibility(View.VISIBLE);
        textTitle1.setText("当前状态");
        text1.setText(billDetail.getCashStatus() == 1 ? "成功" : "失败");
        view2.setVisibility(View.VISIBLE);
        textTitle2.setText("奖励时间");
        text2.setText(DateUtil.getDateStrWithHour1(billDetail.getCreateTime()));
        view4.setVisibility(View.VISIBLE);
        textTitle4.setText("交易单号");
        text4.setText(billDetail.getBusinessNo());
    }


    //订单现金支付  order_pay_cash  接口返回参数{} 暂时注释
    private void updateViewOrderPayCash() {
        textMoneyTitle.setText("支付金额");
        textMoney.setText("￥" + StringUtil.getDouble2(billDetail.getRealMoney()));

        view1.setVisibility(View.VISIBLE);
        textTitle1.setText("商家名称");
        text1.setText(StringUtil.getSafeString(billDetail.getStoreName()));

        view2.setVisibility(View.VISIBLE);
        textTitle2.setText("当前状态");
        text2.setText(StringUtil.getOrderConsumerStatues(billDetail.getOrderStatus()));

        view3.setVisibility(View.VISIBLE);
        textTitle3.setText("支付时间");
        text3.setText(DateUtil.getDateStrWithHour1(billDetail.getCreateTime()));

        view4.setVisibility(View.VISIBLE);
        textTitle4.setText("支付方式");
        text4.setText(StringUtil.getSafeString(StringUtil.getOrderPayCashPayType(billDetail.getPayType())));

        view5.setVisibility(View.VISIBLE);
        textTitle5.setText("交易单号");
        text5.setText(billDetail.getOrderNo() + "");


    }

    //转款（个人转个人） 转入  transfer_in  接口返回参数{} 暂时注释
    //holder.setText(R.id.text_monery, "￥" + StringUtil.fmtMicrometer(item.getMoney()));
    private void updateViewSk() {
        textMoneyTitle.setText("收款金额");
//        textMoney.setText("￥" + StringUtil.getDouble2(billDetail.getMoney()));
        textMoney.setText("￥" + StringUtil.fmtMicrometer(billDetail.getMoney()));

        view1.setVisibility(View.VISIBLE);
        textTitle1.setText("付款方");
        text1.setText(billDetail.getUserName());

        view2.setVisibility(View.VISIBLE);
        textTitle2.setText("到账金额");
        //text2.setText("￥" + StringUtil.getDouble2(billDetail.getMoney() - billDetail.getFee()));
//        text2.setText("￥" + StringUtil.getDouble2(billDetail.getMoney()));
        text2.setText("￥" + StringUtil.fmtMicrometer(billDetail.getMoney()));

        view3.setVisibility(View.VISIBLE);
        textTitle3.setText("服务费");
//        text3.setText("￥" + StringUtil.getDouble2(billDetail.getFee()));
        text3.setText("￥" + StringUtil.fmtMicrometer(billDetail.getFee()));

        view4.setVisibility(View.VISIBLE);
        textTitle4.setText("当前状态");
        text4.setText("1".equals(billDetail.getStatus()) || "Success".equals(billDetail.getStatus()) ? "成功" : "失败");

        view5.setVisibility(View.VISIBLE);
        textTitle5.setText("转账时间");
        text5.setText(DateUtil.getDateStrWithHour1(billDetail.getCreateTime()));

        view6.setVisibility(View.VISIBLE);
        textTitle6.setText("收款至");
        text6.setText(billDetail.getToAccount());

        view7.setVisibility(View.VISIBLE);
        textTitle7.setText("收款单号");
        text7.setText(billDetail.getTradeNo());

    }

    //收款 （个人转给商家）转出  TRANSFER_OUT  接口返回参数{} 暂时注释
    private void updateViewZz() {
        if ("TRANSFER_OUT".equals(billDetail.getBusinessType())) {
            textMoneyTitle.setText("付款金额");
        } else if ("TRANSFER_IN".equals(billDetail.getBusinessType())) {
            textMoneyTitle.setText("收款金额");
        }
        textMoney.setText("￥" + StringUtil.getDouble2(billDetail.getMoney()));

        view1.setVisibility(View.VISIBLE);
        textTitle1.setText("收款方");
        text1.setText(TextUtils.isEmpty(billDetail.getUserName()) ? "" : billDetail.getUserName());

        view2.setVisibility(View.VISIBLE);
        textTitle2.setText("当前状态");
        text2.setText("1".equals(billDetail.getStatus()) || "Success".equals(billDetail.getStatus()) ? "成功" : "失败");

        view3.setVisibility(View.VISIBLE);
        textTitle3.setText("支付时间");
        text3.setText(DateUtil.getDateStrWithHour1(billDetail.getCreateTime()));

        view4.setVisibility(View.VISIBLE);
        textTitle4.setText("付款单号");
        text4.setText(billDetail.getTradeNo());

    }

    //商圈转入  buss_turn_in
    private void updateViewJF() {
        textMoney.setVisibility(View.GONE);
        textMoneyTitle.setText("通贝账单详情");
        textMoneyTitle.setVisibility(View.GONE);

        view1.setVisibility(View.VISIBLE);
        textTitle1.setText("奖励通贝");
        text1.setText(StringUtil.fmtMicrometer(Double.parseDouble(billDetail.getJifen())));

        view2.setVisibility(View.VISIBLE);
        textTitle2.setText("业务类型");
        text2.setText(billDetail.getBusiTypeName());

        view3.setVisibility(View.VISIBLE);
        textTitle3.setText("当前状态");
        text3.setText("1".equals(billDetail.getStatus()) || "Success".equals(billDetail.getStatus()) ? "成功" : "失败");

        view4.setVisibility(View.VISIBLE);
        textTitle4.setText("创建时间");
        text4.setText(DateUtil.getDateStrWithHour1(billDetail.getCreateTime()));

        view5.setVisibility(View.VISIBLE);
        textTitle5.setText("业务订单号");
        text5.setText(billDetail.getTradeNo());
    }

    //商圈转入  buss_turn_in
    private void updateViewSqTk() {
        textMoneyTitle.setText("退款金额");
        textMoney.setText("￥" + StringUtil.getDouble2(billDetail.getMoney()));

        view1.setVisibility(View.VISIBLE);
        textTitle1.setText("转入至");
        text1.setText("商圈");

        view2.setVisibility(View.VISIBLE);
        textTitle2.setText("当前状态");
        text2.setText(billDetail.getCashStatus() == 1 ? "成功" : "失败");

        view3.setVisibility(View.VISIBLE);
        textTitle3.setText("转入时间");
        text3.setText(DateUtil.getDateStrWithHour1(billDetail.getCreateTime()));

    }

    //商圈转入  buss_turn_in
    private void updateViewSqZr() {
        textMoneyTitle.setText("转入金额");
        textMoney.setText("￥" + StringUtil.getDouble2(billDetail.getMoney()));

        view1.setVisibility(View.VISIBLE);
        textTitle1.setText("转入至");
        text1.setText("商圈");

        view2.setVisibility(View.VISIBLE);
        textTitle2.setText("当前状态");
        text2.setText(billDetail.getCashStatus() == 1 ? "成功" : "失败");

        view3.setVisibility(View.VISIBLE);
        textTitle3.setText("转入时间");
        text3.setText(DateUtil.getDateStrWithHour1(billDetail.getCreateTime()));

    }


    //升级  Update
    private void updateViewUpdate() {
        textMoneyTitle.setText("升级费用");
        textMoney.setText("￥" + StringUtil.getDouble2(billDetail.getAmount()));

        view1.setVisibility(View.VISIBLE);
        textTitle1.setText("升级角色");

        if ("1".equals(billDetail.getAfterType())) {
            text1.setText("享客");
        }
        if ("2".equals(billDetail.getAfterType())) {
            text1.setText("拓客");
        }
        if ("3".equals(billDetail.getAfterType())) {
            text1.setText("创客");
        }
        if ("4".equals(billDetail.getAfterType())) {
            text1.setText("创投");
        }
        view2.setVisibility(View.VISIBLE);
        textTitle2.setText("订单号");
        text2.setText(billDetail.getBusinessNo());

        view3.setVisibility(View.VISIBLE);
        textTitle3.setText("时间");
        text3.setText(DateUtil.getDateStrWithHour1(billDetail.getCreateTime()));

        view4.setVisibility(View.VISIBLE);
        textTitle4.setText("当前状态");
        text4.setText("升级成功");
    }


    //充值  recharge
    private void updateViewChongZhi() {
        textMoneyTitle.setText("充值金额");
        textMoney.setText("￥" + StringUtil.getDouble2(billDetail.getMoney()));

        view1.setVisibility(View.VISIBLE);
        textTitle1.setText("收款方");
        text1.setText(billDetail.getReceiveName());

        view2.setVisibility(View.VISIBLE);
        textTitle2.setText("当前状态");
        text2.setText("1".equals(billDetail.getStatus()) || "Success".equals(billDetail.getStatus()) ? "成功" : "失败");

        view3.setVisibility(View.VISIBLE);
        textTitle3.setText("充值时间");
        text3.setText(DateUtil.getDateStrWithHour1(billDetail.getCreateTime()));

        view4.setVisibility(View.VISIBLE);
        textTitle4.setText("支付方式");
        text4.setText(billDetail.getPayType());
      //  text4.setText(StringUtil.getSafeString(StringUtil.getOrderPayCashPayType(billDetail.getPayType())));

        view5.setVisibility(View.VISIBLE);
        textTitle5.setText("交易单号");
        text5.setText(billDetail.getOrderNo() + "");

    }

    //提现
    private void updateViewTiXian() {
        textMoneyTitle.setText("提现金额");
        textMoney.setText("￥" + StringUtil.getDouble2(billDetail.getMoney()));

        view1.setVisibility(View.VISIBLE);
        textTitle1.setText("提现金额");
        text1.setText("￥" + StringUtil.getDouble2(billDetail.getMoney()));

        view2.setVisibility(View.VISIBLE);
        textTitle2.setText("手续费");
        text2.setText("￥" + StringUtil.getDouble2(billDetail.getFeeMoney()));

        view3.setVisibility(View.VISIBLE);
        textTitle3.setText("当前状态");
        if ("1".equals(billDetail.getWithdrawStatus()) || "Success".equals(billDetail.getWithdrawStatus())) {
            text3.setText("提现成功");
        } else if ("WaitHandle".equals(billDetail.getWithdrawStatus())) {
            text3.setText("处理中");
        } else if ("Fail".equals(billDetail.getWithdrawStatus())) {
            text3.setText("提现失败");
        } else if ("Lable".equals(billDetail.getWithdrawStatus())) {
            text3.setText("安全卡异常，请绑定新卡");
        } else {
            text3.setText(billDetail.getWithdrawStatus());
        }
        view4.setVisibility(View.VISIBLE);
        textTitle4.setText("时间");
        text4.setText(DateUtil.getDateStrWithHour1(billDetail.getCreateTime()));

        view5.setVisibility(View.VISIBLE);
        textTitle5.setText("提现银行");
        text5.setText(billDetail.getPayType());

        view6.setVisibility(View.VISIBLE);
        textTitle6.setText("提现单号");
        text6.setText(billDetail.getOrderNo() + "");

    }

    public static void startActivity(Context context, String businessNo, String businessType, int curScreenType) {
        Intent intent = new Intent(context, BillDetailActivity.class);
        intent.putExtra(Intent_Extra_BusinessNo, businessNo);
        intent.putExtra(Intent_Extra_BusinessType, businessType);
        intent.putExtra(Intent_Extra_CurScreenType, curScreenType);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String businessNo, String businessType) {
        Intent intent = new Intent(context, BillDetailActivity.class);
        intent.putExtra(Intent_Extra_BusinessNo, businessNo);
        intent.putExtra(Intent_Extra_BusinessType, businessType);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, MyBillEntity data) {
        Intent intent = new Intent(context, BillDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(Intent_Extra_BusinessEntoty, data);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
