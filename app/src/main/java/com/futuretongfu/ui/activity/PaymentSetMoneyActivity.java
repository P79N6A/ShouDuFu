package com.futuretongfu.ui.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.futuretongfu.WeiLaiFuApplication;
import com.futuretongfu.bean.SystemConfigBean;
import com.futuretongfu.constants.RequestCode;
import com.futuretongfu.model.manager.safeSPreferences.SafeSharedPreferences;
import com.futuretongfu.pay.AliPay;
import com.futuretongfu.pay.WXPay;
import com.futuretongfu.bean.WxPayEntity;
import com.futuretongfu.ui.activity.order.UploadVoucheActivity;
import com.futuretongfu.ui.component.dialog.PromptDialog;
import com.futuretongfu.utils.CacheActivityUtil;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.PromptDialogUtils;
import com.futuretongfu.utils.SharedPreferencesUtils;
import com.futuretongfu.utils.To;
import com.futuretongfu.view.PayPopupWindow;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.futuretongfu.R;
import com.futuretongfu.base.BaseSerializable;
import com.futuretongfu.bean.PaySetMoneyBean;
import com.futuretongfu.bean.StoreDetailsBean;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.iview.PaymentSetMoneyIView;
import com.futuretongfu.iview.StoreDetailsIView;
import com.futuretongfu.listener.IPermissionListener;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.wlsq.PaymentSetMoneyPresenter;
import com.futuretongfu.ui.activity.user.EditPayPasswordActivity;
import com.futuretongfu.ui.activity.wlsq.PaymentSuccessActivity;
import com.futuretongfu.ui.component.dialog.PaymentDialog;
import com.futuretongfu.utils.AMapLocation;
import com.futuretongfu.utils.AppUtil;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.Logger.Logger;
import com.futuretongfu.utils.MatchUtil;
import com.futuretongfu.utils.PackageUtil;
import com.futuretongfu.utils.RxPermissionUtil;
import com.futuretongfu.utils.StringUtil;
import com.futuretongfu.utils.Util;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * 消费输入金额
 *
 * @author DoneYang 2017/6/19
 */

public class PaymentSetMoneyActivity extends BaseActivity implements PaymentSetMoneyIView
        , StoreDetailsIView, PaymentDialog.PaymentDialogWithPwdListener, AMapLocation.MyLocationListener, PayPopupWindow.OnItemClickLister {


    @Bind(R.id.tv_title)
    TextView tv_title;

    @Bind(R.id.image_payment_setmoney_store_head)
    ImageView image_storeHead;

    @Bind(R.id.tv_payment_setmoney_store_name)
    TextView tv_storeName;

    @Bind(R.id.tv_payment_setmoney_store_address)
    TextView tv_storeAddress;

    @Bind(R.id.edt_set_payment_money_input)
    TextInputEditText edt_input;

    @Bind(R.id.tv_payment_setmoney_store_sq_money)
    TextView tv_sqMoney;

    @Bind(R.id.image_payment_setmoney_sq)
    ImageView image_sq;

    @Bind(R.id.tv_payment_setmoney_store_wlf_money)
    TextView tv_wlfMoney;

    @Bind(R.id.image_payment_setmoney_wlf)
    ImageView image_wlf;

    @Bind(R.id.tv_payment_setmoney_store_reallymoney)
    TextView tv_reallymoney;

    @Bind(R.id.tv_payment_setmoney_store_pay)
    TextView tv_pay;
    @Bind(R.id.tv_tongbei)
    TextView tv_tongbei;

    private PaymentSetMoneyPresenter mPresenter;
    private StoreDetailsBean storeDetailsBean = null;

    private boolean ChoiceSq = true;
    private boolean ChoiceWlf = true;

    private String userId = null;
    private String storeId = null;

    private String orderNo = null;
    private double realPay = -1;
    private double WlfBalance = -1;
    private double SqBalance = -1;
    private double payMentMoney = -1;
    private String money = null;
    //    private double mLongitude = 113.963524;
//    private double mLatitude = 22.545166;
    private double mLongitude = 121.4;
    private double mLatitude = 31.2;
    private PayPopupWindow payPopup;
    private static int session=0;

    private String rechargeChannel = "";//MPAY 汇付天下  ALIPAY  支付宝  WECHATPAY 微信
    @Override
    protected int getLayoutId() {
        return R.layout.activity_payment_setmoney;
    }

    @Override
    protected Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        showProgressDialog();
        tv_title.setText("输入金额");
        if (mPresenter == null) {
            mPresenter = new PaymentSetMoneyPresenter(this, this, this);
        }
//        mPresenter.systemConfig();
        CacheActivityUtil.addNewActivity(this);
        initData();
        setLocation();
        initPayPopul();

    }

    private void initPayPopul() {
        payPopup = new PayPopupWindow(this, true);
        payPopup.setOnItemClickListener(this);
        payPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Util.setAlpha(PaymentSetMoneyActivity.this, 1.0f);
            }
        });
    }

    /**
     * 初始化
     */
    private void initData() {

        if (session==1){

        }else if (session==0){
            TuiDialog();

        }

         userId = "" + UserManager.getInstance().getUserId();
        storeId = getIntent().getStringExtra(IntentKey.WLF_ID);
        money = getIntent().getStringExtra(IntentKey.WLF_Money);
        orderNo = getIntent().getStringExtra(IntentKey.WLF_Order);
        String json = getIntent().getStringExtra(IntentKey.WLF_BEAN);
        if (!Util.isEmpty(json)) {
            storeDetailsBean = JSONObject.parseObject(json, StoreDetailsBean.class);
            if (!Util.isEmpty(storeDetailsBean)) {
                setData(storeDetailsBean);
            }
           // boolean isbind = storeDetailsBean.base.isRecomUserId;
//            if (isbind==false){
//                hideProgressDialog();
//                new PromptDialog
//                        .Builder(this)
//                        .setTitle("是否绑定推荐关系")
//                        .setMessage("是否和商家绑定推荐关系")
//                        .setButton1(R.string.cancel, new PromptDialog.OnClickListener() {
//                            @Override
//                            public void onClick(Dialog dialog, int which) {
//                                //orderConsumerDetailPresenter.orderConsumerDetail(orderNo);
//                                dialog.dismiss();
//
//                            }
//                        })
//                        .setButton2("绑定", new PromptDialog.OnClickListener() {
//                            @Override
//                            public void onClick(Dialog dialog, int which) {
//                                mPresenter.onBindTui(userId,storeId);
//                                dialog.dismiss();
//                            }
//                        })
//                        .show();
//
//            }else {
//
//
//            }
        }
        if (!Util.isEmpty(userId)) {
            mPresenter.businessIntoShow(userId);
            mPresenter.getBusinessBalance(userId);
            if (!Util.isEmpty(storeId)) {
                mPresenter.onStoreDetails(userId, storeId);
            }
        }

        if (!Util.isEmpty(money)) {
            payMentMoney = Double.parseDouble(money);
            edt_input.setText(money);
            edt_input.setEnabled(false);
            tv_pay.setEnabled(true);
        }

        edt_input.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    tv_pay.setEnabled(false);
                } else {
                    tv_pay.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    return;
                }
                try {
                    payMentMoney = Double.parseDouble(editable.toString());
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                if (WlfBalance == -1 || SqBalance == -1) {
                    showToast("获取余额失败");
                    return;
                }

                if (payMentMoney == -1) {
                    return;
                }
                tv_reallymoney.setText("￥" + MatchUtil.formatFloatNumber(payMentMoney));
                if (payMentMoney <= (WlfBalance + SqBalance)) {
                    realPay = 0;
                    //tv_reallymoney.setText("￥0.00");
                } else {
                    realPay = payMentMoney - WlfBalance - SqBalance;
                    //tv_reallymoney.setText("￥" + StringUtil.getDouble2(realPay));

                }
            }
        });
    }
    public void TuiDialog(){
        if (UserManager.getInstance().isTj()==false){
            hideProgressDialog();
            new PromptDialog
                    .Builder(this)
                    .setTitle("是否绑定推荐关系")
                    .setMessage("是否和商家绑定推荐关系")
                    .setButton1(R.string.cancel, new PromptDialog.OnClickListener() {
                        @Override
                        public void onClick(Dialog dialog, int which) {
                            //orderConsumerDetailPresenter.orderConsumerDetail(orderNo);
                            dialog.dismiss();

                        }
                    })
                    .setButton2("绑定", new PromptDialog.OnClickListener() {
                        @Override
                        public void onClick(Dialog dialog, int which) {
                            session=1;
                            mPresenter.onBindTui(userId,storeId);
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }

    /**
     * 定位
     */
    AMapLocation aMapLocation;

    private void setLocation() {
        RxPermissions rxPermissions = new RxPermissions(this);
        aMapLocation = new AMapLocation();
        aMapLocation.setLocationListener(this);
        String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        RxPermissionUtil.requestPermission(this, rxPermissions, new MyPermissionCallBack(RequestCode.PHONE_LOCATION), perms);
    }

    /**
     * 设置数据
     *
     * @param bean 集合数据
     */
    private void setData(StoreDetailsBean bean) {
        if (!Util.isEmpty(bean.base)) {
            if (!Util.isEmpty(bean.base.storeName)) {
                tv_storeName.setText(bean.base.storeName);
            }
            if (!Util.isEmpty(bean.base.address)) {
                tv_storeAddress.setText(bean.base.address);
            }
            if (!Util.isEmpty(bean.base.storeId)) {
                storeId = "" + bean.base.storeId;
            }
            if (!Util.isEmpty(bean.base.logoUrl)) {
                GlideLoad.loadImage(bean.base.logoUrl, image_storeHead);
            }
            tv_tongbei.setText("优惠：系统将赠送（消费金额*" + bean.base.serviceChargeRatio / 0.32 + "%）的通贝");
        }
    }

    @Override
    public void setOnItemClick(View view) {
        switch (view.getId()) {
            case R.id.ll_wallet:
                rechargeChannel = Constants.MPAY;    //直接余额支付
                new PaymentDialog(this, this).show();
                break;
            case R.id.ll_alipy:  //支付宝
                rechargeChannel = Constants.ALIPAY;
                onPayment(1);
                break;
            case R.id.ll_wechat:   //微信
                rechargeChannel = Constants.WECHATPAY;
                onPayment(2);
                break;
        }
    }

    /**
     * 使用支付宝支付
     */
    private void onPayment(int type) {
        //待支付订单 -支付
        if (!Util.isEmpty(orderNo) && !Util.isEmpty(money)) {
            mPresenter.onAlipaySecondPayMent(type, userId, orderNo);
        } else {
            showProgressDialog();
            String deviceId = Util.getDeviceId(this);
            int versionCode = PackageUtil.getVersionCode(this);
            if (Util.isEmpty(deviceId)) {
                showToast("获取设备信息失败，请重新尝试");
                return;
            }
            if (versionCode == 0) {
                showToast("获取版本号失败，请重新尝试");
            }
            mPresenter.onAlipayPayMent(type, userId, storeId, "" + payMentMoney, deviceId, "" + versionCode, "", "");
        }
    }

    /**
     * 权限回调
     */
    private class MyPermissionCallBack implements IPermissionListener {

        private int request_code;

        MyPermissionCallBack(int request_code) {
            this.request_code = request_code;
        }

        @Override
        public void onPermissionGranted(String name) {
            if (request_code == RequestCode.PHONE_LOCATION) {
                aMapLocation.getDefaultOption(getApplicationContext());//模拟器不能定位暂时关闭
            }
        }

        @Override
        public void onPermissionDenied(String name) {
            if (request_code == RequestCode.PHONE_LOCATION) {
                showToast("您拒绝了「定位」所需要的相关权限!");
            }
        }
    }

    @OnClick({R.id.bt_back, R.id.image_payment_setmoney_sq, R.id.image_payment_setmoney_wlf, R.id.tv_payment_setmoney_store_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;

            case R.id.image_payment_setmoney_sq:
                image_sq.setSelected(ChoiceSq);
                ChoiceSq = !ChoiceSq;
                Logger.i(TAG, "ChoiceSq=" + ChoiceSq);
                break;

            case R.id.image_payment_setmoney_wlf:
                image_wlf.setSelected(ChoiceWlf);
                ChoiceWlf = !ChoiceWlf;
                Logger.i(TAG, "ChoiceWlf=" + ChoiceWlf);
                break;

            /*买单支付*/
            case R.id.tv_payment_setmoney_store_pay:
                if (Util.isEmpty(userId) || Util.isEmpty(storeId)) {
                    return;
                }
                if (payMentMoney < 1) {
                    showToast("买单金额不得小于1元");
                    return;   //TODO liusha测试
                }
               /* AppUtil.getRealNameStatus(this);
                int realNameStatus = UserManager.getInstance().getRealNameStatus();
                if (realNameStatus == Constants.RealNameStatus_No) {
                    PromptDialogUtils.showNotRuleNamePromptDialog(this);
                } else if (realNameStatus == Constants.RealNameStatus_Yes) {   //已实名
                    if (!UserManager.getInstance().isAnswer()) {
                        PromptDialogUtils.showNotSetQuestionPromptDialog(this);  //密保
                    } else {
                        if (!UserManager.getInstance().isHasPayPwd()) {   //支付密码
                            PromptDialogUtils.showNotSetPwdPromptDialog(this, EditPayPasswordActivity.TYPE_SET_NEW_PAY_PWD);
                        } else {
                            //弹框选择
                            payPopup.showAtLocation(findViewById(R.id.layout_main), Gravity.BOTTOM, 0, 0);
                            Util.setAlpha(this, 0.7f);
                        }
                    }
                } else if (realNameStatus == Constants.RealNameStatus_Doing) {
                    PromptDialogUtils.showRuleNameingPromptDialog(this);
                } else if (realNameStatus == Constants.RealNameStatus_Faile) {
                    PromptDialogUtils.showRuleNameFailPromptDialog(this);
                }
*/
                if (!UserManager.getInstance().isAnswer()) {
                    PromptDialogUtils.showNotSetQuestionPromptDialog(this);  //密保
                } else {
                    if (!UserManager.getInstance().isHasPayPwd()) {   //支付密码
                        PromptDialogUtils.showNotSetPwdPromptDialog(this, EditPayPasswordActivity.TYPE_SET_NEW_PAY_PWD);
                    } else {
                        //弹框选择
                        payPopup.showAtLocation(findViewById(R.id.layout_main), Gravity.BOTTOM, 0, 0);
                        Util.setAlpha(this, 0.7f);
                    }
                }
                break;
        }
    }

    /**
     * 未实名提示
     */
    private void showRealMessage() {
        PromptDialogUtils.showNotRuleNamePromptDialog(this);
    }

    @Override
    public void onPaymentSqBalanceFail(int code, String msg) {


        showToast(msg);
        if (edt_input != null) {
            edt_input.setEnabled(false);
        }
    }

    @Override
    public void onPaymentSqBalanceSuccess(double avlBal) {
        SqBalance = avlBal;
        tv_sqMoney.setText(StringUtil.fmtMicrometer(avlBal));
        if (WlfBalance == -1 || SqBalance == -1 || payMentMoney == -1) {
            return;
        }
        tv_reallymoney.setText("￥" + MatchUtil.formatFloatNumber(payMentMoney));
        if (payMentMoney <= (WlfBalance + SqBalance)) {
            realPay = 0;
            // tv_reallymoney.setText("￥0.00");
        } else {
            realPay = payMentMoney - WlfBalance - SqBalance;
            // tv_reallymoney.setText("￥" + StringUtil.getDouble2(realPay));
        }
    }

    @Override
    public void onPaymentWlfBalanceFail(int code, String msg) {
        showToast(msg);
        if (edt_input != null) {
            edt_input.setEnabled(false);
        }
    }

    @Override
    public void onPaymentWlfBalanceSuccess(double avlBal) {
        WlfBalance = avlBal;
        tv_wlfMoney.setText(StringUtil.fmtMicrometer(avlBal));
        if (WlfBalance == -1 || SqBalance == -1 || payMentMoney == -1) {
            return;
        }
        tv_reallymoney.setText("￥" + MatchUtil.formatFloatNumber(payMentMoney));
        if (payMentMoney <= (WlfBalance + SqBalance)) {
            realPay = 0;
            // tv_reallymoney.setText("￥0.00");
        } else {
            realPay = payMentMoney - WlfBalance - SqBalance;
            //tv_reallymoney.setText("￥" + StringUtil.getDouble2(realPay));
        }
    }

    @Override
    public void onPaymentSetOderFail(int code, String msg) {
        showToast(msg);
    }

    @Override
    public void onPaymentSetOrderSuccess(PaySetMoneyBean data) {
//        if (!Util.isEmpty(data)) {
//            if (!Util.isEmpty(data.orderNo))
//                orderNo = data.orderNo;
//        }
        new PaymentDialog(this, this).show();
    }

    @Override
    public void onPaymentSetBalanceFail(int code, String msg) {
        hideProgressDialog();
        showToast(msg);
        edt_input.setEnabled(true);
    }

    @Override
    public void onPaymentSetBalanceSuccess(final PaySetMoneyBean data) {
        hideProgressDialog();
        new PromptDialog
                .Builder(this)
                .setTitle("支付成功")
                .setMessage(R.string.dlg_confirm_shou_huo2)
                .setButton1(R.string.cancel, new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        //orderConsumerDetailPresenter.orderConsumerDetail(orderNo);

                        dialog.dismiss();
                        edt_input.setEnabled(true);
                        IntentUtil.startActivity(PaymentSetMoneyActivity.this, PaymentSuccessActivity.class, IntentKey.WLF_BEAN, JSON.toJSONString(data));
                        finish();
                    }
                })
                .setButton2("上传", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(getContext(), UploadVoucheActivity.class);
                        intent.putExtra("isOpereteType", false);
                        intent.putExtra("xiaofeiType", 1);
                        intent.putExtra("money", data.realMoney);
                        intent.putExtra("orderNo", data.orderNo + "");
                        startActivity(intent);
                        finish();
                    }
                })
                .show();

    }

    @Override
    public void onPaymentSecondePayFail(int code, String msg) {
        hideProgressDialog();
        if (2008 == code) {
            PromptDialogUtils.showNoMoneyRechargePromptDialog(context, WlfBalance, payMentMoney);
            return;
        }
        showToast(msg);
    }

    @Override
    public void onPaymentSecondePaySuccess(PaySetMoneyBean data) {
        edt_input.setEnabled(true);
        if (!Util.isEmpty(data)) {
            IntentUtil.startActivity(this, PaymentSuccessActivity.class, IntentKey.WLF_BEAN, JSON.toJSONString(data));
            finish();
        }
    }

    @Override
    public void onPaymentFail(int code, String msg) {
        showToast(msg);
    }

    @Override
    public void onPaymentSuccess(String url) {
        edt_input.setEnabled(true);
        hideProgressDialog();
        if (rechargeChannel.equals(Constants.ALIPAY)) {
            AliPay.pay(url, this, new AliPay.PayResultListener() {
                @Override
                public void onSuccess(String resultInfo, String resultStatus) {
                    finish();
                    showToast("支付成功");

                }

                @Override
                public void onFailed() {
                    finish();
                    showToast("支付失败");
                }
            });
        } else if (rechargeChannel.equals(Constants.WECHATPAY)) {
            WxPayEntity data = new Gson().fromJson(url
                    , new TypeToken<WxPayEntity>() {
                    }.getType()
            );
            WXPay.weChatPay(this, data.getPrepayid(), data.getPartnerid(), data.getNoncestr(), data.getTimestamp(), data.getSign());
        }
    }

    @Override
    public void onStoreDetailsFail(int code, String msg) {
       // showToast(msg);
    }

    @Override
    public void onStoreDetailsSuccess(BaseSerializable data) {
        hideProgressDialog();
        if (!Util.isEmpty(data)) {
            storeDetailsBean = (StoreDetailsBean) data;
            setData(storeDetailsBean);
        }
    }

    @Override
    public void onStoreBindFail(int code, String msg) {
        SharedPreferencesUtils.saveBoolean(this,"mon",false);

        Toast.makeText(PaymentSetMoneyActivity.this,"绑定失败",Toast.LENGTH_SHORT).show();
    }
    private SafeSharedPreferences shareUserFile;
    private SafeSharedPreferences.Editor editorUserFile;
   private static String userFilePath = "shareprefer_file_user";
    public void onStoreBindSuccess(String code, String data1) {
        if (code.equals("1")){
            Toast.makeText(PaymentSetMoneyActivity.this,"绑定成功",Toast.LENGTH_SHORT).show();
            session=1;
            SharedPreferencesUtils.saveString(this,"referrer",data1);

        }
    }






    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onsystemConfigSuccess(SystemConfigBean data) {
        tv_tongbei.setText("优惠：系统将赠送（消费金额*" + data.getFieldValue() + "%）的通贝");
    }

    @Override
    public void onsystemConfigfaile(String msg) {
        showToast(msg);

    }

    @Override
    public void onPaymentPwdSuccess(String password) {
        //待支付订单 -支付
        if (!Util.isEmpty(orderNo) && !Util.isEmpty(money)) {
            mPresenter.onSecondePay(userId, orderNo, password);
        } else {//直接支付
            if (realPay > 0) {
                PromptDialogUtils.showNoMoneyRechargePromptDialog(this, realPay, payMentMoney);
            } else {//余额支付
                showProgressDialog();
                String deviceId = Util.getDeviceId(this);
                int versionCode = PackageUtil.getVersionCode(this);
                if (Util.isEmpty(deviceId)) {
                    showToast("获取设备信息失败，请重新尝试");
                    return;
                }
                if (versionCode == 0) {
                    showToast("获取版本号失败，请重新尝试");
                }
                mPresenter.onPaymentSetBalance(userId, storeId, "" + payMentMoney, deviceId, Constants.Terminal, "" + versionCode, "", "", password);
            }
        }
    }

    @Override
    public void onPaymentPwdFaile(String msg) {
        showToast(msg);
    }

    @Override
    public void myLocationListener(double longitude, double latitude, String city) {
        if (longitude != 0 && latitude != 0) {
            mLongitude = longitude;
            mLatitude = latitude;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (aMapLocation != null) {
            aMapLocation.unRegisterMap();
        }
    }

}
