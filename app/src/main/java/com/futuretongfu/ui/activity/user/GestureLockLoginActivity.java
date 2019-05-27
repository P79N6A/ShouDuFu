package com.futuretongfu.ui.activity.user;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.component.gestureLock.GestureLockViewGroup;
import com.futuretongfu.R;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.activity.MainActivity;
import com.futuretongfu.ui.component.dialog.PromptDialog;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class GestureLockLoginActivity extends BaseActivity implements
        GestureLockViewGroup.OnGestureLockViewListener {

    @Bind(R.id.text_err_tip)
    public TextView textErrTip;
    @Bind(R.id.id_gestureLockViewGroup)
    public GestureLockViewGroup gestureLockViewGroup;

    private static final int inputAllCount = 5;
    private int errCount = 0;

    /*******************************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_gesture_lock_login;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        gestureLockViewGroup.setCheckPassword(false);
        gestureLockViewGroup.setOnGestureLockViewListener(this);
    }
    /*******************************************************************************/

    /*******************************************************************************/
    /**
     * 单独选中元素的Id
     */
    @Override
    public void onBlockSelected(int cId) {

    }

    /**
     * 是否匹配
     *
     * @param matched
     */
    @Override
    public void onGestureEvent(boolean matched) {

    }

    /**
     * 超过尝试次数
     */
    @Override
    public void onUnmatchedExceedBoundary() {

    }

    /**
     * 手指抬起，传出输入的密码
     */
    @Override
    public void onGestureFinish(List<Integer> mChoose) {
        String str = getGesturePasswordStr(mChoose);
        gestureLockViewGroup.resetAndUpdateView();
        checkPassword(str);
    }

    /**
     * 重置
     */
    @Override
    public void onGestureReset() {
        textErrTip.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onGestureError() {
        return false;
    }
    /*******************************************************************************/

    /*******************************************************************************/
    @OnClick(R.id.text_forget_g_password)
    public void onClickForgetPassword() {
        VerificationLoginActivity.startActivity(this, VerificationLoginActivity.Type_GestureLockLogin);
        //LoginActivity.startActivity(this, 0);
        //delayFinish(200);
    }

    @OnClick(R.id.text_qhzh)
    public void onClickQhzh() {
        LoginActivity.startActivity(this, 0);
        //delayFinish(200);
    }

    /*******************************************************************************/
    private void checkPassword(String gesture) {
        //手势正确
        if (gesture.equals(UserManager.getInstance().getGesturePwdNative())) {
            MainActivity.startActivity(this, false);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            delayFinish(200);
            return;
        }

        //手势不正确
        errCount++;

        if (errCount == inputAllCount) {
            new PromptDialog
                    .Builder(context)
                    .setMessage("你已连续5次输错手势密码，请重新登录")
                    .setCancelable(false)
                    .setCanceledOnTouchOutside(false)
                    .setButton1(R.string.confirm, new PromptDialog.OnClickListener() {
                        @Override
                        public void onClick(Dialog dialog, int which) {
                            dialog.dismiss();
                            LoginActivity.startActivity(context, 0);
                            delayFinish(200);
                        }
                    })
                    .show();

            return;
        }

        startTextErrTipAnimation(inputAllCount - errCount);

    }

    private String getGesturePasswordStr(List<Integer> choose) {
        String strPassword = "";
        for (Integer item : choose) {
            strPassword += item;
        }

        return strPassword;
    }

    private void startTextErrTipAnimation(int count) {
        textErrTip.setText("密码错误，你还可以输入" + count + "次");
        textErrTip.setVisibility(View.VISIBLE);

        // 左右移动动画
        Animation shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.view_shake);
        textErrTip.startAnimation(shakeAnimation);
    }

    boolean doubleBackToExitPressedOnce = false;

    //返回按键
    @Override
    public void onBackPressed() {
        if (!doubleBackToExitPressedOnce) {
            showToast("再按一次返回键退出");
            doubleBackToExitPressedOnce = true;
            return;
        }
        super.onBackPressed();
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, GestureLockLoginActivity.class);
        context.startActivity(intent);
    }
}
