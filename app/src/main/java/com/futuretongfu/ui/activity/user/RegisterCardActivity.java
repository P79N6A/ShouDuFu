package com.futuretongfu.ui.activity.user;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.WeiLaiFuApplication;
import com.futuretongfu.bean.BindBankCardBean;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.iview.IRegistBankVerView;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.entity.SearchBankInfo;
import com.futuretongfu.model.eventType.ResgisterLoginEventType;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.user.RegistBankPresenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.activity.MainActivity;
import com.futuretongfu.ui.activity.SearchBankActivity;
import com.futuretongfu.ui.activity.ShowWebView1Activity;
import com.futuretongfu.ui.activity.ShowWebViewActivity;
import com.futuretongfu.ui.component.dialog.PromptDialog;
import com.futuretongfu.utils.CacheActivityUtil;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.KeyboardUtil;
import com.futuretongfu.utils.StringUtil;
import com.futuretongfu.utils.TimerUtil;
import com.futuretongfu.utils.Util;
import com.futuretongfu.view.UpgradePopupCityWindow;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static com.futuretongfu.R.id.btn_submit;


/**
 * Created by zhanggf on 2018/6/22.
 * 注册绑卡
 */

public class RegisterCardActivity extends BaseActivity implements  UpgradePopupCityWindow.PopupWindowListener, IRegistBankVerView {

    @Bind(R.id.tv_title)
    public TextView textTitle;
    @Bind(R.id.bank_name)
    public TextView bank_name;
    @Bind(R.id.card_name)
    public EditText cardView;
    @Bind(R.id.phone_number)
    public EditText phoneView;
    @Bind(R.id.choose_address)
    public TextView choose_address;
    @Bind(R.id.checkbox)
    public AppCompatCheckBox checkbox;
    @Bind(btn_submit)
    public Button submit;
    @Bind(R.id.text_yzm)
    public TextView yzmView;
    @Bind(R.id.yzm_edit_view)
    public EditText yzmEdit;
    @Bind(R.id.root_layout)
    public LinearLayout rootView;
    private UpgradePopupCityWindow popupWindow;
    private RegistBankPresenter mPresenter;
    private String proviceString = "";
    private String proviceId = "";
    private String cityString = "";
    private String cityId = "";
    String innerCode = "";
    String userId = "";
    String cardId,userRealName;
    private String order_id;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register_card;
    }

    @Override
    protected Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        textTitle.setText("注册绑卡");
        userId = getIntent().getStringExtra("userId");
        userRealName = getIntent().getStringExtra(IntentKey.USER_REALNAME);
        mPresenter = new RegistBankPresenter(this,this);
        CacheActivityUtil.addNewActivity(this);
        yzmView.setText(R.string.get_yzm);
        yzmView.setEnabled(true);
    }

    /***********************************************************************/

    @OnClick(R.id.bank_name)
    public void onClickBank() {
        IntentUtil.startActivityForResult(RegisterCardActivity.this, SearchBankActivity.class, 103);
    }


    @OnClick(R.id.choose_address)
    public void onClickAddress() {
        hideKeyboard();
        popupWindow = new UpgradePopupCityWindow(context,2);
        popupWindow.setPopupWindowListener(this);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Util.setAlpha(context, 1.0f);
            }
        });
        popupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
        Util.setAlpha(context, 0.5f);
    }


    @OnClick(R.id.bt_back)
    public void onClickBack() {
        showDialog();
    }

    @OnClick(R.id.text_agreement)
    public void onClickAgreement() {
        ShowWebViewActivity.startActivity(this, Constants.Url_Agreement_bindProtocol, "首都富绑卡协议", true);
    }

    /**
     * 扫一扫识别银行卡
     */
    @OnClick(R.id.image_scan_ic)
    public void onGetCardNumber() {
        mPresenter.getCardNumber(this);
    }

    @OnClick(R.id.text_yzm)
    public void onClickGetYZM() {
        String phoneNumber = phoneView.getText().toString().trim();
        if (!StringUtil.isPhoneNumber(phoneNumber)) {
            showToast(getResources().getString(R.string.input_phone_number_error));
            finish();
            return;
        }
        mPresenter.getPhoneCodebindCard(phoneNumber,cardView.getText().toString().trim(),userId);  //汇付绑卡验证
        verificationCodeTimer.start();

    }

    String yzm = "";
    @OnClick(btn_submit)
    public void onCommitClick() {
        if (TextUtils.isEmpty(cardView.getText().toString().trim())) {
            showToast(getResources().getString(R.string.hint_forget_card));
            return;
        }
        if (TextUtils.isEmpty(phoneView.getText().toString().trim())) {
            showToast(getResources().getString(R.string.hint_forget_phone));
            return;
        }
        if (TextUtils.isEmpty(innerCode)) {
            showToast("选择开户行");
            return;
        }
        if (TextUtils.isEmpty(proviceString) || TextUtils.isEmpty(cityString)) {
            showToast("选择开户地区");
            return;
        }
        if (!StringUtil.isPhoneNumber(phoneView.getText().toString().trim())) {
            showToast(getResources().getString(R.string.input_phone_number_error));
            return;
        }
        if (!checkbox.isChecked()) {
            showToast("请同意首都富绑卡协议");
            return;
        }
        yzm = yzmEdit.getText().toString().trim();
        if (TextUtils.isEmpty(yzm)) {
            showToast("请输入验证码");
            return;
        }
        //验证实名+绑卡完善信息
        showProgressDialog();
        mPresenter.addUserTrueBank(userId, cardId,userRealName,cardView.getText().toString().trim(), phoneView.getText().toString().trim(),
                yzm, innerCode, Constants.Response_bank_description,order_id, proviceId, cityId);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 103://选择银行卡code
                if (data != null && data.getExtras() != null) {
                    Bundle bundle = data.getExtras();
                    SearchBankInfo dataBean = (SearchBankInfo) bundle.getSerializable(IntentKey.SET_BANK_INTENT);
                    if (dataBean != null) {
                        if (dataBean.getInnerCode() != null) {
                            innerCode = dataBean.getInnerCode();
                        }
                        if (dataBean.getBankName() != null) {
                            bank_name.setText(dataBean.getBankName());
                        }
                    }
                }
                break;
            case 100:
                if (resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    mPresenter.uploadAndRecognize(extras.getString("path"));
                }
                break;
        }
    }


    public static void startActivity(Context context,String userId,String cardId,String realname) {
        Intent intent = new Intent(context, RegisterCardActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra(IntentKey.USER_CARD_ID, cardId);
        intent.putExtra(IntentKey.USER_REALNAME, realname);
        context.startActivity(intent);
    }
    //禁止使用返回键返回到上一页,但是可以直接退出程序**
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK){
            showDialog();
            return true;//不执行父类点击事件
        }
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }

    private void showDialog() {
        new PromptDialog.Builder(this)
                .setMessage(getResources().getString(R.string.user_back_tip))
                .setButton1("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton2("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
//                        skinNext();
                        CacheActivityUtil.newFinishActivity();
                        dialog.dismiss();
                    }
                }).setButton2TextColor(Color.parseColor("#0091EE")).show();
    }

    private void hideKeyboard() {
        KeyboardUtil.showKeyboard(this, cardView, false);
        KeyboardUtil.showKeyboard(this, phoneView, false);
    }


    private void skinNext() {
        EventBus.getDefault().post(new ResgisterLoginEventType(true));
        startMainActivity();
        delayFinish(200);
    }


    private void startMainActivity() {
        MainActivity.startActivity(this, true);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        TimerUtil.startTimer(400, new TimerUtil.TimerCallBackListener2() {
            @Override
            public void onEnd() {
                finish();
            }
        });
    }

    @Override
    public void onChooseFinish(int type,String provices, String proId, String city, String ciId, String area, String aearCode) {
        //type  1户籍   2开户银行
        if (type==2){
            proviceString = provices;
            cityString = city;
            proviceId = proId;
            cityId = ciId;
            choose_address.setText(provices + "  " + city + "  " + area);
        }
    }


    /**
     * 倒计时
     */
    private CountDownTimer verificationCodeTimer = new CountDownTimer(1000 * 60, 1000) {
        @Override
        public void onTick(long t) {
            String time = (t / 1000) + "s";
            yzmView.setEnabled(false);
            yzmView.setText(time);
        }

        @Override
        public void onFinish() {
            yzmView.setText(R.string.get_yzm);
            yzmView.setEnabled(true);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        verificationCodeTimer.cancel();
    }


    @Override
    public void onGetCardNumberFaile(String msg) {
        showToast(msg);
    }

    @Override
    public void onGetCardNumberSuccess(final String data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cardView.setText(data);
            }
        });
    }
    @Override
    public void verificationSmsFaile(String msg) {
        showToast(msg);
        cleanVerificationCodeTimer();
    }

    private void cleanVerificationCodeTimer() {
        verificationCodeTimer.cancel();
        yzmView.setText(R.string.get_yzm);
        yzmView.setEnabled(true);
    }

    @Override
    public void verificationSmsSuccess(BindBankCardBean bean) {
        if (null!=bean)
            order_id = bean.order_id;
        showToast("验证码已发送");
    }

    //实名绑卡成功
    @Override
    public void onaddUserTrueSuccess(FuturePayApiResult result) {
        hideProgressDialog();
        if ("1".equals(result.getCode())) {
            Constants.isBindCardSuccess = true;
            ShowWebView1Activity.startActivity(this, Constants.Url_Pay_Bank+yzm+"&check_value="+result.getData(), "注册绑卡", true,userId);
            CacheActivityUtil.newFinishActivity();
        }
        showToast(result.getMsg());
    }

    @Override
    public void onaddUserTrueFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
    }


}
