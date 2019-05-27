package com.futuretongfu.ui.activity.user;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.PromptDialogUtils;
import com.futuretongfu.R;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.iview.IPayPwdView;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.bank.PayPasswordPresenter;
import com.futuretongfu.ui.activity.MainActivity;
import com.futuretongfu.ui.component.dialog.PromptDialog;
import com.futuretongfu.utils.AppUtil;
import com.futuretongfu.utils.PermissionUtil;
import com.futuretongfu.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static com.futuretongfu.constants.IntentKey.TYPE_RESET_PAY_PWD;

/**
 * 修改支付密码
 *
 * @author ChenXiaoPeng
 */
public class EditPayPasswordActivity extends BaseActivity implements IPayPwdView {

    @Bind(R.id.bt_back)
    public ImageView imgvBack;
    @Bind(R.id.tv_title)
    public TextView textTitle;
    @Bind(R.id.bt_right)
    public TextView textSkin;

    @Bind(R.id.text_tip)
    public TextView textTip;
    @Bind(R.id.btn_next)
    public Button btnNext;
    @Bind(R.id.tv_pass1)
    public TextView textView1;
    @Bind(R.id.tv_pass2)
    public TextView textView2;
    @Bind(R.id.tv_pass3)
    public TextView textView3;
    @Bind(R.id.tv_pass4)
    public TextView textView4;
    @Bind(R.id.tv_pass5)
    public TextView textView5;
    @Bind(R.id.tv_pass6)
    public TextView textView6;

    @Bind(R.id.text_input_number9)
    public TextView numberView9;
    @Bind(R.id.delete_key_layout)
    public RelativeLayout deleteView;
    @Bind(R.id.root_input_layout)
    public LinearLayout inputLayout;

    @Bind(R.id.view_forget)
    public FrameLayout viewForget;

    public static final int Type_Edit_Password_First = 0;//修改支付密码 第一步
    public static final int Type_Edit_Password_Second = 1;//修改支付密码 第二步
    public static final int Type_Edit_Password_Guild_Fir = 30;//引导第一步
    public static final int Type_Edit_Password_Guild_Snd = 31;//引导第二步
    public static final int TYPE_SET_NEW_PAY_PWD = 122;//设置支付密码标识

    public static final int TYPE_CHECK_PAY_PWD = 102;// 校验支付密码标识

    private static final String Intent_Extra_Type = "type";
    private int type = Type_Edit_Password_First;

    private List<String> numberList;
    private List<TextView> textViews;
    private String inputNumber = "";
    private String numberString;
    private PayPasswordPresenter payPasswordPresenter;
    private UserManager userManager;
    private boolean isFirstDone = false;//第一次验证是否完成

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_pay_password;
    }

    @Override
    protected Presenter getPresenter() {
        return payPasswordPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        type = intent.getIntExtra(Intent_Extra_Type, Type_Edit_Password_First);
        payPasswordPresenter = new PayPasswordPresenter(this, this);
        numberString = "";
        userManager = UserManager.getInstance();
        numberList = new ArrayList<>();
        textViews = new ArrayList<>();
        textViews.add(textView1);
        textViews.add(textView2);
        textViews.add(textView3);
        textViews.add(textView4);
        textViews.add(textView5);
        textViews.add(textView6);
        switch (type) {
            case Type_Edit_Password_Guild_Fir:
                textTitle.setText("设置支付密码");
//                textTip.setText("请输入支付密码");
                textTip.setText("请输新支付密码");
                btnNext.setVisibility(View.GONE);
                imgvBack.setVisibility(View.GONE);
                textSkin.setVisibility(View.VISIBLE);
                viewForget.setVisibility(View.GONE);
                textSkin.setText("跳过");
                break;

            case Type_Edit_Password_First:
                textTitle.setText("身份验证");
                textTip.setText("请输入原支付密码，验证身份");
                btnNext.setVisibility(View.GONE);
                imgvBack.setVisibility(View.VISIBLE);
                textSkin.setVisibility(View.GONE);
                viewForget.setVisibility(View.VISIBLE);
                break;

            case Type_Edit_Password_Second:
                textTitle.setText("设置支付密码");
                textTip.setText("请再次输入支付密码");
                btnNext.setVisibility(View.VISIBLE);
                imgvBack.setVisibility(View.VISIBLE);
                textSkin.setVisibility(View.GONE);
                viewForget.setVisibility(View.GONE);
                break;

            case IntentKey.TYPE_SET_NEW_PAY_PWD:
                textTitle.setText("设置支付密码");
                textTip.setText("请输入支付密码");
                btnNext.setVisibility(View.GONE);
                imgvBack.setVisibility(View.VISIBLE);
                textSkin.setVisibility(View.GONE);
                viewForget.setVisibility(View.GONE);
                break;

            case TYPE_RESET_PAY_PWD:
                textTitle.setText("设置支付密码");
                textTip.setText("输入新支付密码");
                btnNext.setVisibility(View.GONE);
                imgvBack.setVisibility(View.VISIBLE);
                textSkin.setVisibility(View.GONE);
                viewForget.setVisibility(View.GONE);
                break;

            case TYPE_CHECK_PAY_PWD:
                textTitle.setText("支付密码");
                textTip.setText("输入支付密码");
                btnNext.setVisibility(View.GONE);
                imgvBack.setVisibility(View.VISIBLE);
                textSkin.setVisibility(View.GONE);
                viewForget.setVisibility(View.GONE);
                break;
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        int result = PermissionUtil.getPermissionPhoneResult(requestCode, permissions, grantResults);
        if (PermissionUtil.Permission_Result_Allow == result) {
            openPhoneCustomerService();
        } else if (PermissionUtil.Permission_Result_Refuse == result) {
            showToast(R.string.toast_phone_refuse_permission);
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @OnClick(R.id.delete_key_layout)
    public void onBackDelete() {
        numberString = "";
        for (int i = numberList.size() - 1; i >= 0; i--) {
            TextView textView = textViews.get(i);
            if (!TextUtils.isEmpty(textView.getText().toString().trim())) {
                textViews.get(i).setText("");
                numberList.remove(i);
                break;
            }
            for (int j = 0; j < numberList.size() - 1; i++) {
                numberString = numberString + j;
            }
        }
    }

    /**
     * 键盘点击事件监听
     */
    @OnClick({R.id.text_input_number0, R.id.text_input_number1, R.id.text_input_number2, R.id.text_input_number3, R.id.text_input_number4, R.id.text_input_number5,
            R.id.text_input_number6, R.id.text_input_number7, R.id.text_input_number8, R.id.text_input_number9})
    public void onInputNumberClick(View v) {
        switch (v.getId()) {
            case R.id.text_input_number0: {
                inputNumber = "0";
                break;
            }

            case R.id.text_input_number1: {
                inputNumber = "1";
                break;
            }

            case R.id.text_input_number2: {
                inputNumber = "2";
                break;
            }
            case R.id.text_input_number3: {
                inputNumber = "3";
                break;
            }
            case R.id.text_input_number4: {
                inputNumber = "4";
                break;
            }
            case R.id.text_input_number5: {
                inputNumber = "5";
                break;
            }
            case R.id.text_input_number6: {
                inputNumber = "6";
                break;
            }
            case R.id.text_input_number7: {
                inputNumber = "7";
                break;
            }
            case R.id.text_input_number8: {
                inputNumber = "8";
                break;
            }
            case R.id.text_input_number9: {
                inputNumber = "9";
                break;
            }
        }
        initTextViews(inputNumber);
    }

    //联系客服
    @OnClick(R.id.btn_lxkf)
    public void onClickLxkf() {
        if (PermissionUtil.permissionPhone(this)) {
            openPhoneCustomerService();
        }
    }

    //忘记支付密码
    @OnClick(R.id.btn_forget_mibao)
    public void onClickForgetMibao() {
        findPayPwd();
    }

    String firstPayNumber = "";
    String oldPassword = "";

    /**
     * textview填充数据以及判断
     */
    private void initTextViews(String inputNumber) {
        if (numberList.size() < 6) {
            numberString = numberString.trim() + inputNumber;
            numberList.add(inputNumber);
            for (int i = 0; i < numberList.size(); i++) {
                TextView textView = textViews.get(i);
                if (TextUtils.isEmpty(textView.getText().toString().trim())) {
                    textViews.get(i).setText(numberList.get(i));
                    break;
                }
            }
        }
        if (6 == numberList.size()) {
            String userId = userManager.getUserId() + "";
            if (TextUtils.isEmpty(userId)) {
                IntentUtil.startActivity(this, LoginActivity.class);
                return;
            }
            switch (type) {
                case Type_Edit_Password_First:
                    firstPayNumber = "";
                    oldPassword = numberString.trim();
                    payPasswordPresenter.checkPassword(userId, oldPassword);
                    showProgressDialog();
                    break;
                case Type_Edit_Password_Second:
                    if (TextUtils.isEmpty(firstPayNumber)) {
                        firstPayNumber = numberString.trim();
                        if (firstPayNumber.equals(oldPassword)) {
                            showToast("新密码不能与旧密码一致");
                            firstPayNumber = "";
                            setViewNull();
                            return;
                        }
                        if (StringUtil.isSameNumber(firstPayNumber)) {
                            showToast("密码不能为相同的六位，请重新设置！");
                            firstPayNumber = "";
                            setViewNull();
                            return;
                        }
                        textTitle.setText("修改支付密码");
                        textTip.setText("请再次输入支付密码");
                        setViewNull();
                    } else {
                        String num = numberString.trim();
                        if (firstPayNumber.equals(num)) {
                            payPasswordPresenter.changePassword(userId, oldPassword, firstPayNumber);
                            showProgressDialog();
                        } else {
                            showToast("两次输入密码不一致，请重新输入");
                            firstPayNumber = "";
                            setViewNull();
                        }
                    }
                    break;
                case IntentKey.TYPE_SET_NEW_PAY_PWD:
                    if (TextUtils.isEmpty(firstPayNumber)) {
                        if (StringUtil.isSameNumber(numberString.trim())) {
                            showToast("密码不能为相同的六位，请重新设置！");
                            firstPayNumber = "";
                            setViewNull();
                            return;
                        } else {
                            firstPayNumber = numberString.trim();
                            textTip.setText("请再次输入支付密码");
                            setViewNull();
                        }
                        return;
                    } else {
                        String num = numberString.trim();
                        if (firstPayNumber.equals(num)) {
                            payPasswordPresenter.setPayPassword(userId, numberString.trim());
                            showProgressDialog();
                        } else {
                            showToast("两次输入密码不一致，请重新输入");
                            textTip.setText("请输入支付密码");
                            firstPayNumber = "";
                            setViewNull();
                        }
                    }
                    break;

                //引导设置密码第一步
                case Type_Edit_Password_Guild_Fir:
                    firstPayNumber = numberString.trim();
                    if (StringUtil.isSameNumber(firstPayNumber)) {
                        showToast("密码不能为相同的六位，请重新设置！");
                        firstPayNumber = "";
                        setViewNull();
                        return;
                    }
                    setViewNull();
                    textTip.setText("请再次输入支付密码");
                    type = Type_Edit_Password_Guild_Snd;
                    break;

                //引导设置密码第二步
                case Type_Edit_Password_Guild_Snd:
                    String num = numberString.trim();
                    if (firstPayNumber.equals(num)) {
                        payPasswordPresenter.setPayPassword(userId, numberString.trim());
                        showProgressDialog();
                    } else {
                        showToast("两次输入密码不一致，请重新输入");
                        firstPayNumber = "";
                        setViewNull();
                        type = Type_Edit_Password_Guild_Fir;
                        textTip.setText("请输入支付密码");
                    }
                    break;

                //找回支付密码
                case TYPE_RESET_PAY_PWD:
                    if (isFirstDone) {
                        num = numberString.trim();
                        if (firstPayNumber.equals(num)) {
                            showProgressDialog();
                            payPasswordPresenter.resetPassword(userId, numberString.trim());
                        } else {
                            showToast("两次输入密码不一致，请重新输入");
                            textTip.setText("请输入支付密码");
                            isFirstDone = false;
                            firstPayNumber = "";
                            setViewNull();
                        }
                    } else {
                        firstPayNumber = numberString.trim();
                        if (StringUtil.isSameNumber(firstPayNumber)) {
                            showToast("密码不能为相同的六位，请重新设置！");
                            firstPayNumber = "";
                            setViewNull();
                            return;
                        } else {
                            isFirstDone = true;
                            setViewNull();
                            textTip.setText("请再次输入支付密码");
                        }
                    }
                    break;
                case TYPE_CHECK_PAY_PWD:
                    showProgressDialog();
                    payPasswordPresenter.checkPassword(userId, numberString.trim());
                    break;
            }
        }
    }

    /**
     * 置空
     */
    public void setViewNull() {
        numberString = "";
        numberList.clear();
        for (int i = 0; i < textViews.size(); i++) {
            textViews.get(i).setText("");
        }
    }


    //返回按键
    @Override
    public void onBackPressed() {
        boolean success = getSupportFragmentManager().popBackStackImmediate();
        if (!success)
            super.onBackPressed();
    }

    @OnClick(R.id.bt_back)
    public void onClickBack() {
        finish();
    }

    /**
     * 跳过
     */
    @OnClick(R.id.bt_right)
    public void onClickSkin() {
        skinNext();
    }

    public static void startActivity(Context context, int type) {
        Intent intent = new Intent(context, EditPayPasswordActivity.class);
        intent.putExtra(Intent_Extra_Type, type);
        context.startActivity(intent);
    }

    @Override
    public void onSetPayPwdSuccess(FuturePayApiResult futurePayApiResult) {
        userManager.setPayPwd(true);
        userManager.save();
        inputLayout.setVisibility(View.GONE);
        if (Type_Edit_Password_Guild_Snd == type) {
            skinNext();
        } else {
            finish();
        }
        inputLayout.setVisibility(View.GONE);
        hideProgressDialog();
        showToast("密码设置成功");
    }

    @Override
    public void onSetPayPwdFaile(String msg) {
        hideProgressDialog();
        PromptDialogUtils.showNotSetPwdPromptDialog(EditPayPasswordActivity.this, IntentKey.TYPE_SET_NEW_PAY_PWD);
//        showToast(msg);
//        hideProgressDialog();
    }

    @Override
    public void onResetPayPwdSuccess(FuturePayApiResult futurePayApiResult) {
        showToast("密码重置成功");
        userManager.setPayPwd(true);
        userManager.save();
        inputLayout.setVisibility(View.GONE);
        setViewNull();
        if (Type_Edit_Password_Guild_Snd == type) {
            skinNext();
        } else {
            finish();
        }
        inputLayout.setVisibility(View.GONE);
        hideProgressDialog();
    }

    @Override
    public void onResetPayPwdFaile(String msg) {
        setViewNull();
        showToast(msg);
        hideProgressDialog();
    }


    @Override
    public void onChangePayPwdSuccess(FuturePayApiResult futurePayApiResult) {
        inputLayout.setVisibility(View.GONE);
        setViewNull();
        hideProgressDialog();
        showToast("修改成功");
        finish();
    }

    @Override
    public void onChangePayPwdFaile(String msg) {
        setViewNull();
        hideProgressDialog();
        showToast(msg);
    }

    @Override
    public void onCheckPayPwdSuccess(FuturePayApiResult futurePayApiResult) {
        setViewNull();
        hideProgressDialog();
        if (type == TYPE_CHECK_PAY_PWD) {
            MiBaoQuestionActivity.startActivity(this, MiBaoQuestionActivity.Type_MiBao_New_CHANGE_PWD);
            finish();
            return;
        }
        textTitle.setText("修改支付密码");
        textTip.setText("请输入新支付密码");
        viewForget.setVisibility(View.GONE);
        type = Type_Edit_Password_Second;
    }

    @Override
    public void onCheckPayPwdFaile(String msg) {
        setViewNull();
        //showToast(msg);
        hideProgressDialog();
        new PromptDialog
                .Builder(this)
                .setTitle("原支付密码错误")
                .setMessage(msg)
                .setButton1(R.string.forget_password, new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();

                        findPayPwd();

                        type = TYPE_RESET_PAY_PWD;
                        isFirstDone = false;
                        textTitle.setText("修改支付密码");
                        textTip.setText("输入新支付密码");

                    }
                })
                .setButton2(R.string.try_again, new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                        setViewNull();
                    }
                })
                .show();

    }

    private void findPayPwd() {
        Intent intent = new Intent(this, MiBaoQuestionActivity.class);
        if (UserManager.getInstance().isAnswer()) {
            intent.putExtra("type", MiBaoQuestionActivity.Type_MiBao_Verification_CHANGE_PWD);
        } else {
            intent.putExtra("type", MiBaoQuestionActivity.Type_MiBao_New_CHANGE_PWD);
        }
        startActivity(intent);
        finish();
    }

    private void openPhoneCustomerService() {
        AppUtil.openPhoneCustomerService(this);
    }


    private void skinNext() {
        //跳转到首页
        IntentUtil.startActivity(this, MainActivity.class);
        delayFinish(200);
    }
}
