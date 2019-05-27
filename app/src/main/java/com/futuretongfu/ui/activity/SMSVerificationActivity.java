package com.futuretongfu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.WeiLaiFuApplication;
import com.futuretongfu.bean.BindBankCardBean;
import com.futuretongfu.bean.unBindBankCardBean;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.iview.ISmsVerificationView;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.SmsVerificationPresenter;
import com.futuretongfu.ui.activity.user.MiBaoQuestionActivity;
import com.futuretongfu.utils.StringUtil;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 短信验证
 *
 * @author ChenXiaoPeng
 */
public class SMSVerificationActivity extends BaseActivity implements ISmsVerificationView {

    private final static int Type_SMS_MiBao = 2001;//找回密保问题 手机验证码

    @Bind(R.id.tv_title)
    public TextView textTitle;
    @Bind(R.id.yzm_edit_view)
    public TextInputEditText yzmEdit;

    @Bind(R.id.text_yzm)
    public TextView yzmView;
    @Bind(R.id.btn_submit)
    public Button submit;
    public static int type = -1;//标识 1为绑定银行卡时验证，
    public static String phoneNumber = "";//手机号
    public static String cardNumber = "";//银行卡号
    public static String innerCode = "";//银行类型
    private String bankName = "";//银行卡name
    private String proviceId = "";
    private String cityId = "";

    SmsVerificationPresenter smsVPresenter;
    private String order_id;
    private String userBankId,smsOrderDate,smsOrderId;

    /***********************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_smsverification;
    }

    @Override
    protected Presenter getPresenter() {
        return smsVPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        smsVPresenter = new SmsVerificationPresenter(this, this);

        Intent intent = getIntent();
        if (intent != null) {
            type = intent.getIntExtra(IntentKey.SMSV_TYPE_KEY, -1);
            if (1 == type) {
                phoneNumber = intent.getStringExtra(IntentKey.SMSV_PHONE_KEY);
                cardNumber = intent.getStringExtra(IntentKey.SMSV_CARD_KEY);
                innerCode = intent.getStringExtra(IntentKey.BANK_INNER_CODE);
                bankName = intent.getStringExtra(IntentKey.BANK_NAME);
                proviceId = intent.getStringExtra(IntentKey.BANK_PROVICE);
                cityId = intent.getStringExtra(IntentKey.BANK_CITY);
            }else if (2==type){
                phoneNumber = intent.getStringExtra(IntentKey.SMSV_PHONE_KEY);
                userBankId = intent.getStringExtra("userBankId");
            }else if (Type_SMS_MiBao == type) {
                phoneNumber = UserManager.getInstance().getAccountNumber();
            }
        }

        if (Type_SMS_MiBao != type) {
            WeiLaiFuApplication.getInstance().addActivity(this);
        }

        textTitle.setText("短信验证");
    }

    //返回按键
    @Override
    public void onBackPressed() {
        boolean success = getSupportFragmentManager().popBackStackImmediate();
        if (!success)
            super.onBackPressed();
    }

    /***********************************************************************/
    String yzm = "";

    @OnClick(R.id.btn_submit)
    public void onClickSubmit() {
        if (1 == type) {
            yzm = yzmEdit.getText().toString().trim();
            if (TextUtils.isEmpty(yzm)) {
                showToast("请输入验证码");
                return;
            }
            //绑定添加银行卡接口
            //smsVPresenter.addBank(UserManager.getInstance().getUserId() + "", cardNumber, phoneNumber, yzm, innerCode);
            //验证银行卡接口
            showProgressDialog();
            smsVPresenter.addBank(UserManager.getInstance().getUserId() + "", cardNumber, phoneNumber, yzm, innerCode, bankName,order_id, proviceId, cityId);
        }else if (2==type){
            yzm = yzmEdit.getText().toString().trim();
            if (TextUtils.isEmpty(yzm)) {
                showToast("请输入验证码");
                return;
            }
            //解绑银行卡
            showProgressDialog();
            smsVPresenter.bankunBindCard(UserManager.getInstance().getUserId() + "", userBankId,smsOrderDate,smsOrderId,yzm);
        }else if (Type_SMS_MiBao == type) {
            yzm = StringUtil.replaceAllSpace(yzmEdit.getText().toString());
            if (TextUtils.isEmpty(yzm)) {
                showToast("请输入验证码");
                return;
            }

            showProgressDialog();
            smsVPresenter.phoneCode(phoneNumber, yzm);

        }
    }

    @OnClick(R.id.bt_back)
    public void onClickBack() {
        finish();
    }

    @OnClick(R.id.text_yzm)
    public void onClickGetYZM() {
        if (!StringUtil.isPhoneNumber(phoneNumber)) {
            showToast(getResources().getString(R.string.input_phone_number_error));
            finish();
            return;
        }

        if (Type_SMS_MiBao == type) {
            smsVPresenter.getPhoneCode(phoneNumber, Constants.PhoneCode_Question);
        }else if (2==type){
            smsVPresenter.getUnBankgetSmsCode(phoneNumber,UserManager.getInstance().getUserId()+"");  //解绑获取验证吗
        }else {   //1
//            smsVPresenter.getPhoneCode(phoneNumber, Constants.PhoneCode_Pay);  //绑卡
            smsVPresenter.getPhoneCodebindCard(phoneNumber,cardNumber,UserManager.getInstance().getUserId()+"");  //汇付绑卡验证
        }
        verificationCodeTimer.start();

    }

    /***********************************************************************/
    public static void startActivity(Context context, int type, String phone,String userBankId) {
        Intent intent = new Intent(context, SMSVerificationActivity.class);
        intent.putExtra(IntentKey.SMSV_TYPE_KEY, type);
        intent.putExtra(IntentKey.SMSV_PHONE_KEY, phone);
        intent.putExtra("userBankId", userBankId);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, int type, String phone, String card, String innerCode,String proviceString,String cityString, String bankNmae) {
        Intent intent = new Intent(context, SMSVerificationActivity.class);
        intent.putExtra(IntentKey.SMSV_TYPE_KEY, type);
        intent.putExtra(IntentKey.SMSV_PHONE_KEY, phone);
        intent.putExtra(IntentKey.SMSV_CARD_KEY, card);
        intent.putExtra(IntentKey.BANK_NAME, bankNmae);
        intent.putExtra(IntentKey.BANK_PROVICE, proviceString);
        intent.putExtra(IntentKey.BANK_CITY, cityString);
        intent.putExtra(IntentKey.BANK_INNER_CODE, innerCode);

        context.startActivity(intent);
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

    private void cleanVerificationCodeTimer() {
        verificationCodeTimer.cancel();
        yzmView.setText(R.string.get_yzm);
        yzmView.setEnabled(true);
    }


    @Override
    public void verificationSmsFaile(String msg) {
        cleanVerificationCodeTimer();
        showToast(msg);
    }

    @Override
    public void verificationSmsSuccess(BindBankCardBean bean) {
        if (null!=bean)
            order_id = bean.order_id;
        // cleanVerificationCodeTimer();
        showToast("验证码已发送");
    }

    @Override
    public void onBankAddSuccess(FuturePayApiResult result) {
        hideProgressDialog();
        if ("1".equals(result.getCode())) {
            Constants.isBindCardSuccess = true;
            ShowWebViewActivity.startActivity(this, Constants.Url_Pay_Bank+yzm+"&check_value="+result.getData(), "绑定银行卡", true);
            WeiLaiFuApplication.getInstance().exitActivity();
        }
        showToast(result.getMsg());
    }

    @Override
    public void onBankAddFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
    }

    //校验手机验证码 成功
    @Override
    public void onPhoneCodeSuccess() {
        if (Type_SMS_MiBao == type) {
            MiBaoQuestionActivity.startActivity(this, MiBaoQuestionActivity.Type_MiBao_New);
            delayFinish(200);
        }
    }

    //校验手机验证码 失败
    @Override
    public void onPhoneCodeFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
    }

    @Override
    public void verificationSmsonbankSuccess(unBindBankCardBean result) {
        showToast("验证码已发送");
        smsOrderDate = result.order_date;
        smsOrderId = result.order_id;
    }

    @Override
    public void verificationSmsonbankFaile(String msg) {
        cleanVerificationCodeTimer();
        showToast(msg);
    }

    @Override
    public void onbankunBindCardSuccess(FuturePayApiResult futurePayApiResult) {
        showToast(futurePayApiResult.getMsg());
        hideProgressDialog();
        finish();
    }

    @Override
    public void onbankunBindCardFaile(String msg) {
        showToast(msg);
        hideProgressDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        verificationCodeTimer.cancel();
    }

    public static void startActivityMiBao(Context context) {
        Intent intent = new Intent(context, SMSVerificationActivity.class);
        intent.putExtra(IntentKey.SMSV_TYPE_KEY, Type_SMS_MiBao);
        context.startActivity(intent);
    }
}
