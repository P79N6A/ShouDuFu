package com.futuretongfu.ui.activity.goods;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.futuretongfu.OkUtils;
import com.futuretongfu.R;
import com.futuretongfu.bean.PaySetMoneyBean;
import com.futuretongfu.bean.ShoppingGoodBeans;
import com.futuretongfu.bean.WxPayEntity;
import com.futuretongfu.bean.ZhiBean;
import com.futuretongfu.bean.onlinegoods.OnlinePayBean;
import com.futuretongfu.bean.onlinegoods.ShoppingGoodsBean;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.iview.IFirmOrderView;
import com.futuretongfu.model.entity.Balance;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.entity.WareHorseAddressEntity;
import com.futuretongfu.model.exception.WlfStringCallback;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.model.repository.AccountRepository;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.pay.AliPay;
import com.futuretongfu.pay.WXPay;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.goods.FirmOrderPresenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.activity.user.EditPayPasswordActivity;
import com.futuretongfu.ui.activity.wlsq.PaymentSuccessActivity;
import com.futuretongfu.ui.adapter.FirmOrderGoodsAdapter;
import com.futuretongfu.ui.component.dialog.PaymentDialog;
import com.futuretongfu.utils.AppUtil;
import com.futuretongfu.utils.CacheActivityUtil;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.PromptDialogUtils;
import com.futuretongfu.utils.StringUtil;
import com.futuretongfu.utils.To;
import com.futuretongfu.utils.Util;
import com.futuretongfu.view.PayPopupWindow;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.request.RequestCall;

import org.litepal.util.LogUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zhanggf on 2018/5/28.
 * 确认订单
 */

public class FirmOrderActivity extends BaseActivity implements IFirmOrderView, PayPopupWindow.OnItemClickLister, PaymentDialog.PaymentDialogWithPwdListener, RadioGroup.OnCheckedChangeListener {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_afrimorder_name)
    TextView tvAfrimorderName;
    @Bind(R.id.tv_afrimorder_phone)
    TextView tvAfrimorderPhone;
    @Bind(R.id.tv_afrimorder_address)
    TextView tvAfrimorderAddress;
    @Bind(R.id.img_afrimorder_storeimage)
    ImageView imgAfrimorderStoreimage;
    @Bind(R.id.rv_afrimorder_storename)
    TextView rvAfrimorderStorename;
    @Bind(R.id.rv_afrimorder_goods)
    RecyclerView rvAfrimorderGoods;
    @Bind(R.id.tv_afrimorder_delivery)
    TextView tvAfrimorderDelivery;
    @Bind(R.id.et_afrimorder_notice)
    EditText etAfrimorderNotice;
    @Bind(R.id.tv_afrimorder_num)
    TextView tvAfrimorderNum;
    @Bind(R.id.tv_afrimorder_price)
    TextView tvAfrimorderPrice;
    @Bind(R.id.tv_afrimorder_totalnum)
    TextView tvAfrimorderTotalnum;
    @Bind(R.id.tv_afrimorder_totalprice)
    TextView tvAfrimorderTotalprice;
    @Bind(R.id.tv_afrimorder_commit)
    TextView tvAfrimorderCommit;
    @Bind(R.id.img_frimbottom)
    CheckBox img_bottom;
    @Bind(R.id.lin_balance)
    LinearLayout lin_balance;
    @Bind(R.id.frim_one)
    RadioButton mFrim_one;
    @Bind(R.id.frim_two)
    RadioButton mFrim_two;
    @Bind(R.id.frim_three)
    RadioButton mFrim_three;
    @Bind(R.id.frim_four)
    RadioButton mFrim_four;
    @Bind(R.id.radioGroupn)
    RadioGroup mRadiogroup;
    @Bind(R.id.tv_tongbeishu)
    TextView tv_tongbeishu;
    @Bind(R.id.relative_hehe)
    RelativeLayout mrelative_hehe;
    private FirmOrderPresenter presenter;
    private String onlineStoreId, amount, leaveMessage = "";
    private String skuId = "", format = " ";
    private String price, totalPrice;
    private double totalMoney, realMoney, goodsAllTongbei;
    private List<ShoppingGoodsBean> goodsList = new ArrayList<>();

    private FirmOrderGoodsAdapter goodsAdapter;
    private String addr_id; //地址id
    String userId;
    private boolean isPayBallance = false;
    private boolean isopen = false;

    private AccountRepository accountRepository;
    private double terrace;
    //平台余额
    private double bb;
    private String isactivity;

    String onlineOrderNo;

    private double jine;
    private String tongbei;
    List<ShoppingGoodBeans.SkuListBean> skulist = new ArrayList<>();
    private String pricez, totalPricez, formatz;
    private boolean isDj = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_firmorder;
    }

    @Override
    protected Presenter getPresenter() {
        return presenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        balance();
        mRadiogroup.setOnCheckedChangeListener(this);
        tvTitle.setText("确认订单");
        CacheActivityUtil.addNewActivity(this);
        presenter = new FirmOrderPresenter(this, this);
        userId = UserManager.getInstance().getUserId() + "";
        presenter.getWareAddressList(UserManager.getInstance().getUserId() + "");
        onlineStoreId = getIntent().getStringExtra("onlineStoreId");
        totalMoney = getIntent().getDoubleExtra("totalMoney", 0.0);
        goodsAllTongbei = getIntent().getDoubleExtra("goodsAllTongbei", 0.0);
        goodsList = (List<ShoppingGoodsBean>) getIntent().getSerializableExtra("goodsList");
        // pricez = getIntent().getStringExtra("price");
        // formatz = getIntent().getStringExtra("format");
        isactivity = getIntent().getStringExtra("hehe");
        // String ism = getIntent().getStringExtra("ism");
        //Log.e("ISM",ism);
        if (isactivity.equals("hehe")) {
            mrelative_hehe.setVisibility(View.VISIBLE);
        } else if (isactivity.equals("hehe1")) {
            mrelative_hehe.setVisibility(View.VISIBLE);
            mFrim_one.setText("30%现金+70%余额");
            mFrim_two.setText("50%现金+50%余额");
            mFrim_three.setText("70%现金+30%余额");
            mFrim_four.setText("90%现金+10%余额");

        } else if (isactivity.equals("hehe2")) {
            mrelative_hehe.setVisibility(View.VISIBLE);
            mFrim_one.setText("50%现金+50%余额");
            mFrim_two.setText("70%现金+30%余额");
            mFrim_three.setText("90%现金+10%余额");
            mFrim_four.setVisibility(View.GONE);
        } else if (isactivity.equals("hehe3")) {
            mrelative_hehe.setVisibility(View.VISIBLE);
            mFrim_one.setText("70现金+30%余额");
            mFrim_two.setText("90现金+10%余额");
            mFrim_three.setVisibility(View.GONE);
            mFrim_four.setVisibility(View.GONE);
        } else if (isactivity.equals("hehe4")) {
            mrelative_hehe.setVisibility(View.VISIBLE);
            mFrim_one.setText("90%现金+10%余额");
            mFrim_two.setVisibility(View.GONE);
            mFrim_three.setVisibility(View.GONE);
            mFrim_four.setVisibility(View.GONE);
        } else {
            mrelative_hehe.setVisibility(View.GONE);
            mFrim_two.setVisibility(View.GONE);

            mFrim_two.setVisibility(View.GONE);
            mFrim_three.setVisibility(View.GONE);
            mFrim_four.setVisibility(View.GONE);
        }
        leaveMessage = etAfrimorderNotice.getText().toString();
        realMoney = totalMoney;
        HashSet<ShoppingGoodsBean> hashSet = new HashSet<>(goodsList);
        ArrayList<ShoppingGoodsBean> hh = new ArrayList<>(hashSet);
        ShoppingGoodBeans.SkuListBean arrays = new ShoppingGoodBeans.SkuListBean();
        ShoppingGoodBeans shoppingGoodBeans = new ShoppingGoodBeans();
        for (int i = 0; i < hh.size(); i++) {
            ShoppingGoodsBean productInfo = hh.get(i);
            if (i == 0) {
                skuId = productInfo.getProductId();
                price = productInfo.getPrice() + "";
                amount = productInfo.getNum() + "";
                Log.e("我是数量", amount);
                format = TextUtils.isEmpty(productInfo.getFormat()) ? "无" : productInfo.getFormat();
                totalPrice = String.valueOf(productInfo.getPrice() * productInfo.getNum());

            } else {
            skuId = productInfo.getProductId();
                price=getIntent().getStringExtra("price");
               // price = productInfo.getPrice() + "";
                amount = productInfo.getNum() + "";
                Log.e("我是数量", amount);
                format = TextUtils.isEmpty(productInfo.getFormat()) ? "无" : productInfo.getFormat();
                totalPrice = String.valueOf(productInfo.getPrice() * productInfo.getNum());

            }

        }
        arrays.setSkuId(skuId);
        arrays.setPrice(price);
        arrays.setAmount(amount + "");
        arrays.setTotalPrice(totalPrice);
        arrays.setFormat(format);
        skulist.add(arrays);
        // Log.e("商品集合",tojson(skulist));
        //  tvAfrimorderTotalnum.setText(amount);
        //小计金额共
        tvAfrimorderPrice.setText("￥" + StringUtil.fmtMicrometer(totalMoney));
        tvAfrimorderTotalprice.setText("￥" + StringUtil.fmtMicrometer(totalMoney));
        if (goodsList != null && goodsList.size() > 0) {
            Util.setRecyclerViewLayoutManager(this, rvAfrimorderGoods, R.color.transparent, 1);
            goodsAdapter = new FirmOrderGoodsAdapter(this, goodsList);
            if (goodsList.get(0).getPrice() / goodsList.get(0).getSendTongBei() <= 2) {
                isPayBallance = true;
            } else {
                isPayBallance = false;
            }
            rvAfrimorderGoods.setAdapter(goodsAdapter);
        }
    }


    @OnClick({R.id.bt_back, R.id.rl_afrimorder_address, R.id.tv_afrimorder_commit, R.id.img_frimbottom, R.id.frim_one, R.id.frim_two, R.id.frim_three, R.id.frim_four})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;
            case R.id.rl_afrimorder_address:
                //选择收货地址
                Intent intent1 = new Intent(this, WareHorseAddressActivity.class);
                intent1.putExtra("isClick", true);
                startActivityForResult(intent1, 1002);
                break;
            case R.id.tv_afrimorder_commit:
                if (TextUtils.isEmpty(addr_id)) {
                    showToast("请先选择收货地址");
                    return;
                }

                payPopup = new PayPopupWindow(this, isPayBallance);
                payPopup.setOnItemClickListener(this);
                payPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        Util.setAlpha(FirmOrderActivity.this, 1.0f);
                    }
                });
                payPopup.showAtLocation(findViewById(R.id.activity_evaluate), Gravity.BOTTOM, 0, 0);
                Util.setAlpha(this, 0.7f);
                break;
            case R.id.img_frimbottom:
                if (img_bottom.isChecked()) {
                    isopen = true;
                    lin_balance.setVisibility(View.VISIBLE);
                    tv_tongbeishu.setVisibility(View.VISIBLE);
                    if (terrace < totalMoney * 0.8 && terrace < totalMoney * 0.6 && terrace < totalMoney * 0.4 && terrace < totalMoney * 0.2) {
                        Toast.makeText(FirmOrderActivity.this, "余额不足", Toast.LENGTH_SHORT).show();
                        mFrim_one.setEnabled(false);
                        mFrim_two.setEnabled(false);
                        mFrim_three.setEnabled(false);
                        mFrim_four.setEnabled(false);

                    }

                } else {
                    lin_balance.setVisibility(View.GONE);
                    tv_tongbeishu.setVisibility(View.GONE);

                    isopen = false;
                    tvAfrimorderPrice.setText("￥" + StringUtil.fmtMicrometer(totalMoney));

                }
                break;
        }
    }


    @Override
    public void onFirmOrderFail(String msg) {
        //
        //  showToast(msg);
    }

    private PayPopupWindow payPopup;

    @Override
    public void onFirmOrderSuccess(String result) {
        Gson gson = new Gson();
        ZhiBean zhiBean = gson.fromJson(result, ZhiBean.class);
        onlineOrderNo = zhiBean.getOnlineOrderNo();
        // totalMoney=zhiBean.getTotalMoney();
        Log.e("订单", onlineOrderNo + "");
        presenter.onPayMent(1, userId, onlineStoreId, onlineOrderNo);
        presenter.onPayMent(2, userId, onlineStoreId, onlineOrderNo);


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onWareAddressSuccess(List<WareHorseAddressEntity> datas) {
        if (datas != null && datas.size() > 0) {
            addr_id = datas.get(0).getId();
            tvAfrimorderName.setText("收件人:" + datas.get(0).getReceiverName());
            tvAfrimorderAddress.setText("收获地址：" + datas.get(0).getReceiverAddress());
            tvAfrimorderPhone.setText(datas.get(0).getReceiverMobile());
        } else {
            showToast("请设置默认收获地址");
        }
    }

    @Override
    public void onWareAddressFaile(String msg) {
        //   showToast(msg);
    }

    @Override
    public void onPaymentFail(int code, String msg) {
        // showToast(msg);
    }

    @Override
    public void onPaymentSuccess(String result) {
        hideProgressDialog();
        if (rechargeChannel.equals(Constants.ALIPAY)) {
            AliPay.pay(result, this, new AliPay.PayResultListener() {
                @Override
                public void onSuccess(String resultInfo, String resultStatus) {
                    finish();
                    showToast("支付成功");
                }

                @Override
                public void onFailed() {
                    finish();
                    //  LogUtil.e("支付失败".);
                }
            });
        } else if (rechargeChannel.equals(Constants.WECHATPAY)) {
            WxPayEntity data = new Gson().fromJson(result
                    , new TypeToken<WxPayEntity>() {
                    }.getType()
            );
            WXPay.weChatPay(this, data.getPrepayid(), data.getPartnerid(), data.getNoncestr(), data.getTimestamp(), data.getSign());



        }
    }

    @Override
    public void onPaymentSetBalanceFail(int code, String msg) {
        hideProgressDialog();
        //  showToast(msg);
        finish();
    }

    @Override
    public void onPaymentSetBalanceSuccess(PaySetMoneyBean data) {
        hideProgressDialog();
        finish();
        IntentUtil.startActivity(this, PaymentSuccessActivity.class, IntentKey.WLF_BEAN, JSON.toJSONString(data));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1002:
                if (resultCode == Activity.RESULT_OK) {
                    String getAddress = data.getStringExtra("getAddress");
                    String getName = data.getStringExtra("getName");
                    String getPhone = data.getStringExtra("getPhone");
                    addr_id = data.getStringExtra("getId");
                    tvAfrimorderName.setText("收件人：" + getName);
                    tvAfrimorderPhone.setText(getPhone);
                    tvAfrimorderAddress.setText("收货地址：" + getAddress);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String rechargeChannel = "";//MPAY 余额  ALIPAY  支付宝  WECHATPAY 微信
    private boolean ci = false;

    @Override
    public void setOnItemClick(View view) {
        switch (view.getId()) {
            case R.id.ll_wallet:
                if (!UserManager.getInstance().isAnswer()) {
                    PromptDialogUtils.showNotSetQuestionPromptDialog(this);  //密保
                } else {
                    if (!UserManager.getInstance().isHasPayPwd()) {   //支付密码
                        PromptDialogUtils.showNotSetPwdPromptDialog(this, EditPayPasswordActivity.TYPE_SET_NEW_PAY_PWD);
                    } else {
                        rechargeChannel = Constants.MPAY;    //直接余额支付
                        new PaymentDialog(this, this).show();
                    }
                }
                break;
            case R.id.ll_alipy:  //支付宝
                Log.e("我的金额支付宝", jine + "");
                tongbei = goodsAllTongbei + "";
                presenter.ShopFirm(1, userId, onlineStoreId, totalMoney + "", 0 + "", bb + "", jine + "", "Android", leaveMessage, 0 + "", realMoney + "", tongbei, addr_id, skulist);
                rechargeChannel = Constants.ALIPAY;

                break;
            case R.id.ll_wechat:   //微信
                Log.e("我的金额微信", jine + "");
                tongbei = goodsAllTongbei + "";
                presenter.ShopFirm(2, userId, onlineStoreId, totalMoney + "", 0 + "", bb + "", jine + "", "Android", leaveMessage, 0 + "", realMoney + "", tongbei, addr_id, skulist);
                rechargeChannel = Constants.WECHATPAY;
                break;
        }
    }
    //密码支付
    @Override
    public void onPaymentPwdSuccess(String password) {
        showProgressDialog();
        presenter.onPaymentSetBalance(userId, onlineOrderNo, password);
    }
    @Override
    public void onPaymentPwdFaile(String msg) {
    }
    /**
     * 余额
     */
    public void balance() {
        accountRepository = new AccountRepository();
        accountRepository.balance(
                UserManager.getInstance().getUserId() + ""
                , new BaseRepository.HttpCallListener<Balance>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                    }
                    @Override
                    public void onHttpCallSuccess(Balance data) {
                        terrace = data.getAvlBal();
                    }
                }
        );
    }
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkid) {
        switch (checkid) {
            case R.id.frim_one:
                if (isactivity.equals("hehe")) {
                    setMoneny(0.58, 0.42, 0.58);
                } else if (isactivity.equals("hehe1")) {
                    setMoneny(0.30, 0.70, 0.30);
                } else if (isactivity.equals("hehe2")) {
                    setMoneny(0.50, 0.50, 0.50);
                } else if (isactivity.equals("hehe3")) {
                    setMoneny(0.70, 0.30, 0.70);
                } else if (isactivity.equals("hehe4")) {
                    setMoneny(0.90, 0.10, 0.90);
                }
                break;
            case R.id.frim_two:
                if (isactivity.equals("hehe")) {
                    setMoneny(0.68, 0.32, 0.68);
                } else if (isactivity.equals("hehe1")) {
                    setMoneny(0.5, 0.5, 0.5);
                } else if (isactivity.equals("hehe2")) {
                    setMoneny(0.7, 0.3, 0.7);
                } else if (isactivity.equals("hehe3")) {
                    setMoneny(0.9, 0.1, 0.9);
                } else if (isactivity.equals("hehe4")) {
                    setMoneny(0.95, 0.05, 0.95);
                }
                break;
            case R.id.frim_three:
                if (isactivity.equals("hehe")) {
                    setMoneny(0.78, 0.22, 0.78);
                } else if (isactivity.equals("hehe1")) {
                    setMoneny(0.7, 0.3, 0.7);
                } else if (isactivity.equals("hehe2")) {
                    setMoneny(0.9, 0.1, 0.9);
                }
                break;
            case R.id.frim_four:
                if (isactivity.equals("hehe")) {
                    setMoneny(0.88, 0.12, 0.88);
                } else if (isactivity.equals("hehe1")) {
                    setMoneny(0.9, 0.1, 0.9);
                }
                break;
        }
    }
    public void setMoneny(double xianjin, double yue, double tbs) {
        double nn = totalMoney * xianjin;
        bb = totalMoney * yue;
        Double db1 = new Double(goodsAllTongbei);
        int tb = db1.intValue();
        int tongb = (int) (tb * tbs);
        double value = new BigDecimal(nn).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        double value1 = new BigDecimal(bb).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        jine = value;
        tongbei = tongb + "";
        bb = value1;
        tv_tongbeishu.setText("送通贝:" + tongbei + "个");
        tvAfrimorderPrice.setText("现金:" + value + "余额:" + value1);
    }

    private String tojson(Object o) {
        Gson gson = new Gson();
        String s = gson.toJson(o);
        return s;
    }

}
