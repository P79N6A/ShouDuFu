package com.futuretongfu.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.futuretongfu.ui.activity.user.EditPayPasswordActivity;
import com.futuretongfu.R;
import com.futuretongfu.constants.IntentKey;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 类:   BankPopupWindow
 * 描述: 我的银行卡页面弹框
 * 作者： weiang on 2017/6/21
 */
public class BankPayPopupWindow extends PopupWindow {
    private View rootView;
    @Bind(R.id.imgv_close)
    public ImageView imgv_close;
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
    @Bind(R.id.text_input_number0)
    public TextView numberView0;
    @Bind(R.id.text_input_number1)
    public TextView numberView1;
    @Bind(R.id.text_input_number2)
    public TextView numberView2;
    @Bind(R.id.text_input_number3)
    public TextView numberView3;
    @Bind(R.id.text_input_number4)
    public TextView numberView4;
    @Bind(R.id.text_input_number5)
    public TextView numberView5;
    @Bind(R.id.text_input_number6)
    public TextView numberView6;
    @Bind(R.id.text_input_number7)
    public TextView numberView7;
    @Bind(R.id.text_input_number8)
    public TextView numberView8;
    @Bind(R.id.text_input_number9)
    public TextView numberView9;

    @Bind(R.id.text_forget_password)
    public TextView text_forget_password;

    @Bind(R.id.delete_key_layout)
    public RelativeLayout deleteView;
    private OnBankPayListener listener;
    private Context context;
    private List<String> numberList = new ArrayList<String>();
    private List<TextView> textViews = new ArrayList<>();

    public BankPayPopupWindow(Context context) {
        super(context);
        init(context);
        this.context = context;
        setPopupWindow();
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        //绑定布局
        rootView = inflater.inflate(R.layout.layout_popup_bank_password_input, null);
        ButterKnife.bind(this, rootView);
        textViews.add(textView1);
        textViews.add(textView2);
        textViews.add(textView3);
        textViews.add(textView4);
        textViews.add(textView5);
        textViews.add(textView6);
        setContentView(rootView);
    }

    /**
     * 设置窗口属性
     */
    private void setPopupWindow() {
        // 设置SelectPicPopupWindow的View
        this.setContentView(rootView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        setAnimationStyle(R.style.BottomDialogAnim);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        //this.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(Color.argb(112, 0, 0, 0));
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    private String inputNumber = "";
    private StringBuffer numberString = new StringBuffer();
    @OnClick(R.id.delete_key_layout)
    public void onBackDelete() {
        for (int i = numberList.size() - 1; i >= 0; i--) {
            TextView textView = textViews.get(i);
            if (!TextUtils.isEmpty(textView.getText().toString().trim())) {
                textViews.get(i).setText("");
                numberList.remove(i);
                break;
            }
        }
    }

    /**
     * 找回密码
     */
    @OnClick(R.id.text_forget_password)
    public void onforgetPwd() {
        setViewNull();
        dismiss();
        EditPayPasswordActivity.startActivity(context, IntentKey.TYPE_RESET_PAY_PWD);
    }


    @OnClick(R.id.imgv_close)
    public void onClickBack() {
        dismiss();
    }

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

    /**
     * textview填充数据
     */
    private void initTextViews(String inputNumber) {
        if (numberList.size() < 6) {
            numberList.add(inputNumber);
            numberString.append(inputNumber);
            for (int i = 0; i < numberList.size(); i++) {
                TextView textView = textViews.get(i);
                if (TextUtils.isEmpty(textView.getText().toString().trim())) {
                    textViews.get(i).setText(numberList.get(i));
                    break;
                }
            }
        }
        if (6 == numberList.size()) {
            if (listener != null) {
                listener.onBankPayInputFinish(numberString);
            }
        }
    }

    /**
     * 置空
     */
    public void setViewNull() {
        numberString = new StringBuffer();
        numberList.clear();
        for (int i = 0; i < textViews.size(); i++) {
            textViews.get(i).setText("");
        }
    }

    /**
     * 设置监听器
     *
     * @param listener
     */
    public void setBankPayListener(OnBankPayListener listener) {
        this.listener = listener;
    }

    /**
     * 键盘监听器
     */
    public interface OnBankPayListener {
        //键盘输入完
        void onBankPayInputFinish(StringBuffer numberString);
    }

}
