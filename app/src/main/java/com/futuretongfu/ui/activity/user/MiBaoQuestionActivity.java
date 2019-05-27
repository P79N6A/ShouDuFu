package com.futuretongfu.ui.activity.user;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.futuretongfu.constants.RequestCode;
import com.futuretongfu.model.entity.SafeQuestion;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.user.MiBaoQuestionPresenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.adapter.SafeQuestionAdapter;
import com.futuretongfu.ui.component.dialog.PromptDialog;
import com.futuretongfu.utils.AppUtil;
import com.futuretongfu.utils.PermissionUtil;
import com.futuretongfu.R;
import com.futuretongfu.iview.IMiBaoQuestionView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static com.futuretongfu.constants.IntentKey.TYPE_RESET_PAY_PWD;

/**
 * 编辑密保问题
 *
 * @author ChenXiaoPeng
 */
public class MiBaoQuestionActivity extends BaseActivity implements IMiBaoQuestionView, SafeQuestionAdapter.SafeQuestionAdapterListener {

    public static final int Type_MiBao_Guide = 0;//密保设置引导
    public static final int Type_MiBao_New = 1;//新密保问题
    public static final int Type_MiBao_Edit = 2;//原密保验证
    public static final int Type_MiBao_Verification = 3;//密保验证

    public static final int Type_MiBao_New_CHANGE_PWD = 4;//设置保验证(修改支付密码)
    public static final int Type_MiBao_Verification_CHANGE_PWD = 5;//密保验证(修改支付密码)

    private static final int Step_1 = 1;
    private static final int Step_2 = 2;

    private static final String Intent_Extra_Type = "type";

    @Bind(R.id.bt_back)
    public ImageView imgvBack;
    @Bind(R.id.tv_title)
    public TextView textTitle;

    @Bind(R.id.text_question_title)
    public TextView textQuestionTitle;
    @Bind(R.id.imgv_spread)
    public ImageView imgvSpread;
    @Bind(R.id.recycler_list)
    public RecyclerView recyclerList;
    @Bind(R.id.text_anster)
    public TextInputEditText editAnster;

    @Bind(R.id.bt_right)
    public TextView textSkin;

    @Bind(R.id.view_forget)
    public FrameLayout viewForget;

    @Bind(R.id.btn_next)
    public Button btnNext;

    private int type = 0;
    private int step = Step_1;
    private SafeQuestionAdapter safeQuestionAdapter;
    private MiBaoQuestionPresenter miBaoQuestionPresenter;
    private SafeQuestion selectQuestion;

    /***********************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_mi_bao_question;
    }

    @Override
    protected Presenter getPresenter() {
        return miBaoQuestionPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        Intent intent = getIntent();
        type = intent.getIntExtra(Intent_Extra_Type, Type_MiBao_New);

        miBaoQuestionPresenter = new MiBaoQuestionPresenter(this, this);

        switch (type) {
            //引导页
            case Type_MiBao_Guide:
                textTitle.setText("设置密保问题");
                imgvBack.setVisibility(View.GONE);
                textSkin.setVisibility(View.VISIBLE);
                viewForget.setVisibility(View.GONE);
                textSkin.setText("跳过");
                btnNext.setEnabled(false);
                break;

            //设置新密保
            case Type_MiBao_New:
            case Type_MiBao_New_CHANGE_PWD:
                textTitle.setText("设置密保问题");
                imgvBack.setVisibility(View.VISIBLE);
                textSkin.setVisibility(View.GONE);
                viewForget.setVisibility(View.GONE);
                btnNext.setText("完成");
                btnNext.setEnabled(false);
                break;

            //验证原密保
            case Type_MiBao_Edit:
                textTitle.setText("原密保验证");
                imgvBack.setVisibility(View.VISIBLE);
                textSkin.setVisibility(View.GONE);
                viewForget.setVisibility(View.VISIBLE);
                btnNext.setText("下一步");
                btnNext.setEnabled(false);
                step = Step_1;
                break;

            case Type_MiBao_Verification:
            case Type_MiBao_Verification_CHANGE_PWD:
                textTitle.setText("密保验证");
                imgvBack.setVisibility(View.VISIBLE);
                textSkin.setVisibility(View.GONE);
                viewForget.setVisibility(View.GONE);
                btnNext.setText("完成");
                btnNext.setEnabled(false);
                break;

        }

        recyclerList.setLayoutManager(new LinearLayoutManager(getContext()));
        safeQuestionAdapter = new SafeQuestionAdapter(this, new ArrayList<SafeQuestion>(), this);
        recyclerList.setAdapter(safeQuestionAdapter);

        miBaoQuestionPresenter.queryQuestionList();

    }

    //返回按键
    @Override
    public void onBackPressed() {
        boolean success = getSupportFragmentManager().popBackStackImmediate();
        if (!success)
            super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        int result = PermissionUtil.getPermissionPhoneResult(requestCode, permissions, grantResults);
        if (PermissionUtil.Permission_Result_Allow == result) {
            openPhoneCustomerService();
        } else if (PermissionUtil.Permission_Result_Refuse == result) {
            showToast(R.string.toast_phone_refuse_permission);
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /***********************************************************************/
    @OnClick(R.id.bt_back)
    public void onClickBack() {
        finish();
    }

    @OnTextChanged(R.id.text_anster)
    public void onTextChange() {
        if (TextUtils.isEmpty(editAnster.getText().toString().trim())) {
            btnNext.setEnabled(false);
            return;
        }

        btnNext.setEnabled(true);
    }

    @OnClick(R.id.view_question_click)
    public void onClickQuestion() {
        if (recyclerList.isShown()) {
            recyclerList.setVisibility(View.GONE);
            showImgvSpread(false);
        } else {
            recyclerList.setVisibility(View.VISIBLE);
            showImgvSpread(true);
        }
    }

    @OnClick(R.id.btn_next)
    public void onClickNext() {
        if (null == selectQuestion) {
            showToast("请先选择密保问题");
            return;
        }

        if (isEmpty(editAnster.getText().toString(), "密保答案")) {
            return;
        }

        switch (type) {
            case Type_MiBao_New:
                showProgressDialog();
                miBaoQuestionPresenter.setSecurityQuestion(selectQuestion.getId(), editAnster.getText().toString());
                break;

            case Type_MiBao_Guide:
                showProgressDialog();
                miBaoQuestionPresenter.setSecurityQuestion(selectQuestion.getId(), editAnster.getText().toString());
                break;

            case Type_MiBao_New_CHANGE_PWD:
                showProgressDialog();
                miBaoQuestionPresenter.setSecurityQuestion(selectQuestion.getId(), editAnster.getText().toString());
                break;

            case Type_MiBao_Edit:
                if (Step_1 == step) {
                    showProgressDialog();
                    miBaoQuestionPresenter.authSecurityQuestion(editAnster.getText().toString());
                }
                if (Step_2 == step) {
                    showProgressDialog();
                    miBaoQuestionPresenter.changeSecurityQuestion(selectQuestion.getId(), editAnster.getText().toString());
                }
                break;

            case Type_MiBao_Verification:
                showProgressDialog();
                miBaoQuestionPresenter.authSecurityQuestion(editAnster.getText().toString());
                break;

            case Type_MiBao_Verification_CHANGE_PWD:
                showProgressDialog();
                miBaoQuestionPresenter.authSecurityQuestion(editAnster.getText().toString());
                break;
        }
    }

    /**
     * 跳过
     */
    @OnClick(R.id.bt_right)
    public void onClickSkin() {
        skinNext();
    }

    //联系客服
    @OnClick(R.id.btn_lxkf)
    public void onClickLxkf() {
        if (PermissionUtil.permissionPhone(this)) {
            openPhoneCustomerService();
        }
    }

    //忘记安全问题
    @OnClick(R.id.btn_forget_mibao)
    public void onClickForgetMibao() {
        EditPayPasswordActivity.startActivity(this, EditPayPasswordActivity.TYPE_CHECK_PAY_PWD);
        finish();
    }

    /***********************************************************************/
    @Override
    public void onQueryQuestionListSuccess(List<SafeQuestion> datas) {
        safeQuestionAdapter = new SafeQuestionAdapter(this, datas, this);
        recyclerList.setAdapter(safeQuestionAdapter);
    }

    @Override
    public void onQueryQuestionListFaile(String msg) {
        showToast(msg);
    }

    @Override
    public void onSetSecurityQuestionSuccess() {
        hideProgressDialog();
        if (type == Type_MiBao_New_CHANGE_PWD) {
            showToast("密保问题重置成功");
        } else {
            showToast("密保问题设置成功");
        }
        UserManager.getInstance().setAnswer(true);
        UserManager.getInstance().save();
        switch (type) {
            case Type_MiBao_Guide:
                skinNext();
                break;
            default:
                delayFinish(200);
                break;
        }
    }

    @Override
    public void onSetSecurityQuestionFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
    }

    @Override
    public void onAuthSecurityQuestionSuccess() {
        hideProgressDialog();
        switch (type) {
            case Type_MiBao_New_CHANGE_PWD:
                finish();
                break;
            case Type_MiBao_Verification:
                finish();

                break;
            case Type_MiBao_Verification_CHANGE_PWD:
                Intent intent = new Intent(this, EditPayPasswordActivity.class);
                intent.putExtra(Intent_Extra_Type, TYPE_RESET_PAY_PWD);
                startActivity(intent);
                finish();
                break;
            default:
                set2EditStep2();
                break;
        }
    }

    @Override
    public void onAuthSecurityQuestionFaile(String msg) {
        hideProgressDialog();

//        showToast(msg);
        new PromptDialog.Builder(this)
                .setTitle("密保错误")
                .setMessage(msg)
                .setButton1("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton2("找回密保", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                        EditPayPasswordActivity.startActivity(MiBaoQuestionActivity.this, EditPayPasswordActivity.TYPE_CHECK_PAY_PWD);
                        finish();
                    }
                })
                .show();

    }

    @Override
    public void onChangeSecurityQuestionSuccess() {
        hideProgressDialog();
        showToast("密保问题修改成功");
        delayFinish(200);
    }

    @Override
    public void onChangeSecurityQuestionFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
    }

    /***********************************************************************/
    @Override
    public void onSafeQuestionSelect(SafeQuestion item) {
        selectQuestion = item;
        showImgvSpread(false);
        updateView();
    }

    /***********************************************************************/
    private void openPhoneCustomerService() {
        AppUtil.openPhoneCustomerService(this);
    }

    private void set2EditStep2() {
        selectQuestion = null;
        textQuestionTitle.setText("请选择安全问题");
        editAnster.setText("");

        step = Step_2;

        textTitle.setText("新密保问题");
        btnNext.setText("完成");

        viewForget.setVisibility(View.GONE);
    }

    private void showImgvSpread(boolean isUp) {
        if (isUp) {
            Animator anim = AnimatorInflater.loadAnimator(this, R.animator.spread_up);
            anim.setTarget(imgvSpread);
            anim.start();
            return;
        }

        Animator anim = AnimatorInflater.loadAnimator(this, R.animator.spread_down);
        anim.setTarget(imgvSpread);
        anim.start();
    }

    private void updateView() {
        textQuestionTitle.setText(selectQuestion.getQuestion());
        recyclerList.setVisibility(View.GONE);
    }

    private void skinNext() {
        //支付密码设置
        EditPayPasswordActivity.startActivity(this, EditPayPasswordActivity.Type_Edit_Password_Guild_Fir);
        delayFinish(200);
    }

    public static void startActivity(Context context, int type) {
        Intent intent = new Intent(context, MiBaoQuestionActivity.class);
        intent.putExtra(Intent_Extra_Type, type);
        ((Activity) context).startActivityForResult(intent, RequestCode.REQUEST_CODE_MIBAAO);
    }
}
