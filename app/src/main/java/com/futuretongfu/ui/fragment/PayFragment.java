package com.futuretongfu.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.futuretongfu.OkUtils;
import com.futuretongfu.R;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.constants.RequestCode;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.activity.ContactsActivity;
import com.futuretongfu.ui.activity.MyBankActivity;
import com.futuretongfu.ui.activity.MyBillActivity;
import com.futuretongfu.ui.activity.PaymentSetMoneyActivity;
import com.futuretongfu.ui.activity.RecommendedActivity;
import com.futuretongfu.ui.activity.ScanAddFriendActivity;
import com.futuretongfu.ui.activity.SetRechargeMoneyActivity;
import com.futuretongfu.ui.activity.StoreReceivablesCodeActivity;
import com.futuretongfu.ui.activity.TransferMoneyActivity;
import com.futuretongfu.ui.activity.account.MyBusinessCircleActivity;
import com.futuretongfu.ui.activity.account.PaymentBalanceActivity;
import com.futuretongfu.ui.activity.account.ScoreDetailActivity;
import com.futuretongfu.ui.activity.account.ScoreDetailActivity2;
import com.futuretongfu.ui.activity.order.UploadVocucheListActivity;
import com.futuretongfu.ui.activity.order.UploadVoucheActivity;
import com.futuretongfu.ui.activity.user.LoginActivity;
import com.futuretongfu.utils.AppUtil;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.Logger.Logger;
import com.futuretongfu.utils.SharedPreferencesUtils;
import com.futuretongfu.utils.Util;
import com.skjr.zxinglib.CaptureActivity;

import org.litepal.util.LogUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by zhanggf on 2018/3/14.
 * 首页--支付
 */

public class PayFragment extends BaseFragment {
    @Bind(R.id.remark_num)
    public View remark_num;

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_pay;
    }

    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



    }


    /**
     * @param isShow 通讯录是否显示红点
     */
    public void showFriendNum(boolean isShow) {
        if (remark_num != null) {
            if (isShow) {
                remark_num.setVisibility(View.VISIBLE);
            } else {
                remark_num.setVisibility(View.GONE);
            }
        }
    }

    @OnClick({R.id.tv_fpay_saoyisao, R.id.tv_fpay_zhuanzhang, R.id.tv_fpay_chongzhi, R.id.tv_fpay_shoukuanma, R.id.tv_fpay_contacts,
            R.id.tv_fpay_zengzhi, R.id.tv_fpay_zhangdan, R.id.tv_fpay_yue, R.id.tv_fpay_tongbao, R.id.tv_fpay_jifen, R.id.tv_fpay_card, R.id.tv_fpay_recommendationj})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_fpay_saoyisao:  //扫一扫
                openScan();
                break;
            case R.id.tv_fpay_zhuanzhang:   //转账
                if (!UserManager.getInstance().isLogin()) {
                    LoginActivity.startActivity(getContext(), RequestCode.REQUEST_CODE_LOGIN_NORMAL);
                    return;
                }
              /*  AppUtil.getRealNameStatus(getContext());
                int userRealNameStatus = UserManager.getInstance().getRealNameStatus();
                if (userRealNameStatus == Constants.RealNameStatus_No) {
                    PromptDialogUtils.showNotRuleNamePromptDialog(getActivity());
                } else if (userRealNameStatus == Constants.RealNameStatus_Doing) {
                    PromptDialogUtils.showRuleNameingPromptDialog(getActivity());
                } else if (userRealNameStatus == Constants.RealNameStatus_Yes) {
                    IntentUtil.startActivity(getActivity(), TransferMoneyActivity.class);
                    // transferFriendPresenter.businessIntoShow(userId);
                } else if (Constants.RealNameStatus_Faile == UserManager.getInstance().getRealNameStatus()) {
                    PromptDialogUtils.showRuleNameFailPromptDialog(getActivity());
                }*/
                IntentUtil.startActivity(getActivity(), TransferMoneyActivity.class);
                break;
            case R.id.tv_fpay_chongzhi:  //充值
                if (!UserManager.getInstance().isLogin()) {
                    LoginActivity.startActivity(getContext(), RequestCode.REQUEST_CODE_LOGIN_NORMAL);
                    return;
                }
              /*  AppUtil.getRealNameStatus(getContext());
                userRealNameStatus = UserManager.getInstance().getRealNameStatus();
                if (userRealNameStatus == Constants.RealNameStatus_No) {
                    PromptDialogUtils.showNotRuleNamePromptDialog(getActivity());
                } else if (userRealNameStatus == Constants.RealNameStatus_Doing) {
                    PromptDialogUtils.showRuleNameingPromptDialog(getActivity());
                } else if (Constants.RealNameStatus_Faile == UserManager.getInstance().getRealNameStatus()) {
                    PromptDialogUtils.showRuleNameFailPromptDialog(getActivity());
                } else if (!UserManager.getInstance().isWithdrawCard()) {
                    PromptDialogUtils.showNotBindCardPromptDialog(getActivity());
                } else if (userRealNameStatus == Constants.RealNameStatus_Yes) {
                    IntentUtil.startActivity(getActivity(), SetRechargeMoneyActivity.class, IntentKey.WLF_BEAN, RequestCode.RECHARGE_MONEY);
                }*/
                IntentUtil.startActivity(getActivity(), SetRechargeMoneyActivity.class, IntentKey.WLF_BEAN, RequestCode.RECHARGE_MONEY);
                break;
            case R.id.tv_fpay_shoukuanma:  //收款码
                if (!UserManager.getInstance().isLogin()) {
                    LoginActivity.startActivity(getContext(), RequestCode.REQUEST_CODE_LOGIN_NORMAL);
                    return;
                }
                if (TextUtils.isEmpty(UserManager.getInstance().getUserKey()) || Constants.User_Type_XK == UserManager.getInstance().getUserType()) {
                    showToast("请先升级到创客或创客级别以上");
                    return;
                }
                if (UserManager.getInstance().isStore()) {
                    showProgressDialog();
                    setId();
                    IntentUtil.startActivity(getActivity(), StoreReceivablesCodeActivity.class);
                }
 else {
                    showToast("该功能只针对商家使用");
                    return;
               }
                if (UserManager.getInstance().is100tk() == true) {
                    showProgressDialog();
                     setId();
                    IntentUtil.startActivity(getActivity(), StoreReceivablesCodeActivity.class);
                    return;
                }

                break;
            case R.id.tv_fpay_zengzhi:   //增值
                if (!UserManager.getInstance().isLogin()) {
                    LoginActivity.startActivity(getContext(), RequestCode.REQUEST_CODE_LOGIN_MY);
                    return;
                }
                IntentUtil.startActivity(getContext(), UploadVocucheListActivity.class);
                break;
            case R.id.tv_fpay_contacts:   //通讯录
                if (!UserManager.getInstance().isLogin()) {
                    LoginActivity.startActivity(getContext(), RequestCode.REQUEST_CODE_LOGIN_MY);
                    return;
                }
                IntentUtil.startActivity(getContext(), ContactsActivity.class);
                break;
            case R.id.tv_fpay_zhangdan:  //账单
                if (!UserManager.getInstance().isLogin()) {
                    LoginActivity.startActivity(getContext(), RequestCode.REQUEST_CODE_LOGIN_MY);
                    return;
                }
                MyBillActivity.startActivity(getContext(), Constants.Bill_Type_Cash);

                break;
            case R.id.tv_fpay_yue:  //余额
                if (!UserManager.getInstance().isLogin()) {
                    LoginActivity.startActivity(getContext(), RequestCode.REQUEST_CODE_LOGIN_MY);
                    return;
                }
                PaymentBalanceActivity.startActivity(getContext(), 1);
                break;
            case R.id.tv_fpay_recommendationj://推荐奖
                if (!UserManager.getInstance().isLogin()) {
                    LoginActivity.startActivity(getContext(), RequestCode.REQUEST_CODE_LOGIN_MY);
                    return;
                }
                RecommendedActivity.startActivity(getContext(), 1);
                break;
            case R.id.tv_fpay_tongbao:  //通宝
                if (!UserManager.getInstance().isLogin()) {
                    LoginActivity.startActivity(getContext(), RequestCode.REQUEST_CODE_LOGIN_MY);
                    return;
                }
                MyBusinessCircleActivity.startActivity(getContext());
                break;
            case R.id.tv_fpay_jifen:   //积分通贝
                if (!UserManager.getInstance().isLogin()) {
                    LoginActivity.startActivity(getContext(), RequestCode.REQUEST_CODE_LOGIN_MY);
                    return;
                }
                ScoreDetailActivity.startActivity(getContext());
//                ScoreDetailActivity2.startActivity(getContext());
                break;
            case R.id.tv_fpay_card:  //银行卡
                if (!UserManager.getInstance().isLogin()) {
                    LoginActivity.startActivity(getContext(), RequestCode.REQUEST_CODE_LOGIN_MY);
                    return;
                }
                IntentUtil.startActivity(getActivity(), MyBankActivity.class);
                break;
        }
    }

    //扫一扫
    public void openScan() {
        IntentUtil.startActivityForResult(this, CaptureActivity.class, RequestCode.REQUEST_CODE_SCAN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("二维码地址", requestCode + "");

        // 扫描二维码/条码回传
        if (requestCode == RequestCode.REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String sb;
                String content = data.getStringExtra(IntentKey.SCAN_CONTENT_KEY);
                Log.e("二维码地址", content);
                if (content.indexOf("bussPay") != -1) {
                    String image = content.substring(content.lastIndexOf("/") + 1);
                    boolean numeric = isNumeric(image);
                    if (numeric == true) {
                        IntentUtil.startActivity(this, PaymentSetMoneyActivity.class
                                , IntentKey.WLF_ID, image);
                    }

                } else if (content.indexOf("referralCode") != -1) {
                    String image = content.substring(content.lastIndexOf("=") + 1);
                    Log.e("截取", image);
                    boolean numeric = isNumeric(image);
                    if (numeric == true) {
                        if (!(UserManager.getInstance().getUserId() + "").equals(image)) {
                            IntentUtil.startActivity(this, ScanAddFriendActivity.class, IntentKey.WLF_ID, image);
                        } else {
                            showToast("不能自己添加自己");
                        }
                    }

                } else {
                    if (Util.isEmpty(content)) {
                        return;
                    }
                    Logger.i(TAG, "解码结果：\n" + content);

                    if (!content.contains("?")) {
                        showToast("二维码错误，请重新确认后重新扫码。");
                        return;
                    }


                    String[] codeUrl = content.split("\\?");
                    if (Util.isEmpty(codeUrl) || codeUrl.length < 2) {
                        Logger.i(TAG, "code.length()=" + codeUrl.length);
                        showToast("获取扫描信息失败");
                        return;
                    }
                    //String[]codeU = content.split("bussPay/")

                    String info = codeUrl[1];
                    String type = null;
                    String ids = null;
                    if (!info.contains("userId") && !info.contains("storeId")) {
                        showToast("获取扫描信息失败");
                        return;
                    } else if (info.contains("userId")) {
//                    ids = info.split("userId=")[1];
                        String[] aa = info.split("&");
                        String str1 = aa[0];
                        String str2 = "";
                        if (aa.length >= 2) {
                            str2 = aa[1];
                        }
                        if (str1.contains("userId")) {
                            ids = str1;
                        } else {
                            ids = str2;
                        }
                        ids = ids.split("=")[1];
                        type = "userId";
                    } else if (info.contains("storeId")) {
                        ids = info.split("storeId=")[1];
                        type = "storeId";
                    }
                    if ("userId".equals(type)) {
                        if (!(UserManager.getInstance().getUserId() + "").equals(ids)) {
                            IntentUtil.startActivity(this, ScanAddFriendActivity.class, IntentKey.WLF_ID, ids);
                        } else {
                            showToast("不能自己添加自己");
                        }
                    } else if ("storeId".equals(type)) {
                        IntentUtil.startActivity(this, PaymentSetMoneyActivity.class
                                , IntentKey.WLF_ID, ids);
                    }
                }

            }
        }
    }

    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
    public void setId(){
        String  regid = SharedPreferencesUtils.getString(getActivity(), "regId", "rid");
        HashMap<String, String> map = new HashMap<>();
        String userId = "" + UserManager.getInstance().getUserId();
        map.put("userId", userId);
        map.put("registerId", regid + "");
        OkUtils.UploadSJ(Constants.HOST+Constants.JIGUANGTUISONG, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                LogUtil.d("极光",s);

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        hideProgressDialog();

    }
}
