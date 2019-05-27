package com.futuretongfu.ui.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.component.gestureLock.GestureLockViewGroup;
import com.futuretongfu.R;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.activity.MainActivity;
import com.futuretongfu.utils.Logger.Logger;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 手势密码
 *
 * @author ChenXiaoPeng
 */

public class GestureLockActivity extends BaseActivity implements
        GestureLockViewGroup.OnGestureLockViewListener {

    public static final int Type_Shous_New = 0;//新手势密码
    public static final int Type_Shous_Edit = 1;//编辑手势密码
    public static final int Type_Shous_Forget = 2;//忘记手势密码

    private static final int Step_1 = 0;//步骤一
    private static final int Step_2 = 1;//步骤二

    private static final String Intent_Extra_Type = "type";
    private static final int Password_Length_Min = 4;

    @Bind(R.id.bt_back)
    public ImageView imgvBack;
    @Bind(R.id.tv_title)
    public TextView textTitle;
    @Bind(R.id.bt_right)
    public TextView textNext;

    @Bind(R.id.text_tip)
    public TextView textTip;
    @Bind(R.id.id_gestureLockViewGroup)
    public GestureLockViewGroup gestureLockViewGroup;

    private int type = Type_Shous_New;
    private int step = Step_1;

    private String password1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_gesture_lock;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        type = intent.getIntExtra(Intent_Extra_Type, Type_Shous_New);

        switch (type) {
            case Type_Shous_New://新手势密码
                step = Step_1;

                textTitle.setText("设置手势密码");
                textNext.setVisibility(View.GONE);

                textTip.setText("为了您的账户安全，请绘制手势密码");
                gestureLockViewGroup.setCheckPassword(false);
                gestureLockViewGroup.setCheckError(false);

                break;

            case Type_Shous_Edit:
                step = Step_1;

                textTitle.setText("设置手势密码");
                textNext.setVisibility(View.GONE);

                textTip.setText("修改手势密码");
                gestureLockViewGroup.setCheckPassword(false);
                gestureLockViewGroup.setCheckError(false);

                break;

            case Type_Shous_Forget:
                step = Step_1;

                textTitle.setText("设置手势密码");
                textNext.setVisibility(View.GONE);

                textTip.setText("新手势密码");
                gestureLockViewGroup.setCheckPassword(false);
                gestureLockViewGroup.setCheckError(false);

                break;
        }

        this.gestureLockViewGroup.setOnGestureLockViewListener(this);

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

    @OnClick(R.id.bt_right)
    public void onClickNext() {
    }

    /*
     * 单独选中元素的Id
     */
    @Override
    public void onBlockSelected(int cId) {
    }

    /**
     * 是否匹配
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
        Logger.d("GestureLock", mChoose.toString());
        //设置手势密码
        String password2;
        if (Type_Shous_New == type) {
            if (Step_1 == step) {
                if (mChoose.size() < Password_Length_Min) {
                    setPasswordFormatErr();
                    return;
                }

                password1 = getGesturePasswordStr(mChoose);
                //进入设置手势密码第二步
                set2NewStep2();

                return;
            }

            if (Step_2 == step) {
                password2 = getGesturePasswordStr(mChoose);
                //手势密码设置成功
                if (password2.equals(password1)) {
                    UserManager.getInstance().setGesturePwdNative(password1);
                    UserManager.getInstance().save();

                    set2NewSuccess();
                    return;
                }

                //两次密码输入不一致，请重新绘制
                set2NewStep2Err();

                return;
            }
        }

        if (Type_Shous_Edit == type || Type_Shous_Forget == type) {
            if (Step_1 == step) {
                if (mChoose.size() < Password_Length_Min) {
                    setPasswordFormatErr();
                    return;
                }

                password1 = getGesturePasswordStr(mChoose);
                //进入修改手势密码第二步
                set2EditStep2();

                return;
            }

            if (Step_2 == step) {
                password2 = getGesturePasswordStr(mChoose);
                //手势密码设置成功
                if (password2.equals(password1)) {
                    UserManager.getInstance().setGesturePwdNative(password1);
                    UserManager.getInstance().save();

                    set2NewSuccess();
                    return;
                }

                //两次密码输入不一致，请重新绘制
                set2NewStep2Err();
            }
        }
    }

    /**
     * 重置
     */
    @Override
    public void onGestureReset() {
    }

    @Override
    public boolean onGestureError() {

        //设置手势密码
        if (Step_1 == step) {
            List<Integer> choose = gestureLockViewGroup.getChoose();
            return choose == null || choose.size() < Password_Length_Min;
        }

        return Step_2 == step && !password1.equals(getGesturePasswordStr(gestureLockViewGroup.getChoose()));

    }

    /***********************************************************************/

    private String getGesturePasswordStr(List<Integer> choose) {
        String strPassword = "";
        for (Integer item : choose) {
            strPassword += item;
        }

        return strPassword;
    }

    private void setPasswordFormatErr() {
        textTip.setTextColor(ContextCompat.getColor(this, R.color.color_finger_up_error));
        startTextErrTipAnimation("至少连接" + Password_Length_Min + "个点,请重新绘制");
        gestureLockViewGroup.resetAndUpdateView();
    }

    private void startTextErrTipAnimation(String text) {
        textTip.setText(text);
        textTip.setVisibility(View.VISIBLE);

        // 左右移动动画
        Animation shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.view_shake);
        textTip.startAnimation(shakeAnimation);
    }

    //进入设置手势密码第二步
    private void set2NewStep2() {
        step = Step_2;

        textTip.setTextColor(ContextCompat.getColor(this, R.color.colorTextNormalLight));
        textTip.setText("确认手势密码");

        gestureLockViewGroup.resetAndUpdateView();
    }

    //两次密码输入不一致，请重新绘制
    private void set2NewStep2Err() {
        textTip.setTextColor(ContextCompat.getColor(this,R.color.color_finger_up_error));
        startTextErrTipAnimation("两次密码输入不一致，请重新绘制");
        gestureLockViewGroup.resetAndUpdateView();
    }

    //手势密码设置成功
    private void set2NewSuccess() {
        gestureLockViewGroup.resetAndUpdateView();
        textTip.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary));
        textTip.setText("手势密码设置成功");

        if (Type_Shous_Forget == type) {
            MainActivity.startActivity(this, false);
        } else {
            delayFinish(400);
        }

    }

    //进入修改手势密码第二步
    private void set2EditStep2() {
        step = Step_2;

        textTip.setTextColor(ContextCompat.getColor(this,R.color.colorTextNormalLight));
        textTip.setText("确认手势密码");

        gestureLockViewGroup.resetAndUpdateView();
    }

    public static void startActivity(Context context, int type) {
        Intent intent = new Intent(context, GestureLockActivity.class);
        intent.putExtra(Intent_Extra_Type, type);
        context.startActivity(intent);
    }
}
