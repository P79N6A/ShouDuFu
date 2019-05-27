package com.futuretongfu.ui.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.InputFilter;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import com.futuretongfu.model.eventType.ResetPasswordBackSafeSetFinishEventType;
import com.futuretongfu.model.eventType.ResetPasswordFinishEventType;
import com.futuretongfu.presenter.user.ResetPasswordPresenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.R;
import com.futuretongfu.iview.IResetPasswordView;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.activity.MainActivity;
import com.futuretongfu.ui.component.ChineseLengthFilter;
import com.futuretongfu.utils.MatchUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 重置密码
 *
 * @author ChenXiaoPeng
 */
public class ResetPasswordActivity extends BaseActivity implements IResetPasswordView {

    private static final String Intent_Extra_Type = "type";
    private static final String Intent_Extra_Phone = "phone";
    private static final String Intent_Extra_VrfyCode = "vrfyCode";

    public final static int Type_Back_Main = 0;
    public final static int Type_Back_safeSetting = 1;

    @Bind(R.id.tv_title)
    public TextView textTitle;
    @Bind(R.id.text_phone)
    public TextView textPhone;
    @Bind(R.id.text_password_new)
    public TextInputEditText editPasswordNew;
    @Bind(R.id.text_password_confirm)
    public TextInputEditText editPasswordConfirm;

    @Bind(R.id.btn_edit_password)
    public Button btnEditPassword;

    private int type;
    private String phone;
    private String vrfyCode;
    private ResetPasswordPresenter resetPasswordPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_reset_password;
    }

    @Override
    protected Presenter getPresenter() {
        return resetPasswordPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        type = intent.getIntExtra(Intent_Extra_Type, Type_Back_Main);
        phone = intent.getStringExtra(Intent_Extra_Phone);
        vrfyCode = intent.getStringExtra(Intent_Extra_VrfyCode);
        textTitle.setText(R.string.user_reset_password);
        textPhone.setText(phone);
        btnEditPassword.setEnabled(false);

        editPasswordNew.setFilters(new InputFilter[]{new ChineseLengthFilter(12, true)});
        editPasswordConfirm.setFilters(new InputFilter[]{new ChineseLengthFilter(12, true)});

        this.resetPasswordPresenter = new ResetPasswordPresenter(this, this);
    }

    /*********************************************************************************/
    @OnClick(R.id.bt_back)
    public void onClickBack() {
        finish();
    }

    @OnTextChanged({R.id.text_password_new, R.id.text_password_confirm})
    public void onTextChange() {
        if (TextUtils.isEmpty(editPasswordNew.getText().toString().trim()) || TextUtils.isEmpty(editPasswordConfirm.getText().toString().trim())) {
            btnEditPassword.setEnabled(false);
            return;
        }

        btnEditPassword.setEnabled(true);
    }

    @OnClick(R.id.btn_edit_password)
    public void onClickEditPassword() {
        if (isEmpty(editPasswordNew.getText().toString(), R.string.user_password_new)) {
            return;
        }

        if (isEmpty(editPasswordConfirm.getText().toString(), R.string.user_password_comfit)) {
            return;
        }

        if (!editPasswordNew.getText().toString().equals(editPasswordConfirm.getText().toString())) {
            showToast(R.string.user_password_un_same);
            return;
        }

        if (!MatchUtil.matchPassowrd(editPasswordNew.getText().toString(), 6, 12)) {
            showToast(R.string.toast_password_err_form);
            return;
        }

        showProgressDialog();
        resetPasswordPresenter.resetLoginPwd(phone, editPasswordNew.getText().toString(), vrfyCode);

    }
    /*********************************************************************************/

    /*********************************************************************************/
    @Override
    public void onResetPasswordSuccess() {
        hideProgressDialog();
        showToast("密码修改成功");

        if (Type_Back_Main == type) {
            EventBus.getDefault().post(new ResetPasswordFinishEventType());
            MainActivity.startActivity(this, false);
        } else if (Type_Back_safeSetting == type) {
            EventBus.getDefault().post(new ResetPasswordBackSafeSetFinishEventType());
            SettingSafeActivity.startActivity(this);
        }

        delayFinish(200);
    }

    @Override
    public void onResetPasswordFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
    }

    @Override
    public void onAutoLoginFaile() {
        hideProgressDialog();
        showToast("密码修改成功");
        delayFinish(200);
    }

    /*********************************************************************************/

    public static void startActivity(Context context, String phone, int type, String vrfyCode) {
        Intent intent = new Intent(context, ResetPasswordActivity.class);
        intent.putExtra(Intent_Extra_Phone, phone);
        intent.putExtra(Intent_Extra_Type, type);
        intent.putExtra(Intent_Extra_VrfyCode, vrfyCode);
        context.startActivity(intent);
    }
}
