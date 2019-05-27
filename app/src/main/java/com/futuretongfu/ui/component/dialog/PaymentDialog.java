package com.futuretongfu.ui.component.dialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.model.repository.PayPwdRepository;
import com.futuretongfu.R;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.ui.activity.user.MiBaoQuestionActivity;
import com.futuretongfu.utils.Logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenXiaoPeng on 2017/6/12.
 */

public class PaymentDialog extends AbsCustomDialog implements View.OnClickListener {

    private Context context;
    public ImageView imgv_close;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextView textView5;
    private TextView textView6;
    private TextView numberView0;
    private TextView numberView1;
    private TextView numberView2;
    private TextView numberView3;
    private TextView numberView4;
    private TextView numberView5;
    private TextView numberView6;
    private TextView numberView7;
    private TextView numberView8;
    private TextView numberView9;
    private RelativeLayout deleteView;
    private TextView textForgetPassword;

    private List<String> numberList = new ArrayList<>();
    private List<TextView> textViews = new ArrayList<>();
    private StringBuffer numberString;

    private PayPwdRepository payPwdRepository;

//    public PaymentDialog(Context context, PaymentDialogListener paymentDialogListener) {
//        super(context);
//        this.context = context;
//        payPwdRepository = new PayPwdRepository();
//        this.paymentDialogListener = paymentDialogListener;
//    }

    public PaymentDialog(Context context, PaymentDialogWithPwdListener paymentDialogWithPwdListener) {
        super(context);
        this.context = context;
        payPwdRepository = new PayPwdRepository();
        this.paymentDialogWithPwdListener = paymentDialogWithPwdListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        imgv_close = (ImageView) findViewById(R.id.imgv_close);

        textView1 = (TextView) findViewById(R.id.tv_pass1);
        textView2 = (TextView) findViewById(R.id.tv_pass2);
        textView3 = (TextView) findViewById(R.id.tv_pass3);
        textView4 = (TextView) findViewById(R.id.tv_pass4);
        textView5 = (TextView) findViewById(R.id.tv_pass5);
        textView6 = (TextView) findViewById(R.id.tv_pass6);

        numberView0 = (TextView) findViewById(R.id.text_input_number0);
        numberView1 = (TextView) findViewById(R.id.text_input_number1);
        numberView2 = (TextView) findViewById(R.id.text_input_number2);
        numberView3 = (TextView) findViewById(R.id.text_input_number3);
        numberView4 = (TextView) findViewById(R.id.text_input_number4);
        numberView5 = (TextView) findViewById(R.id.text_input_number5);
        numberView6 = (TextView) findViewById(R.id.text_input_number6);
        numberView7 = (TextView) findViewById(R.id.text_input_number7);
        numberView8 = (TextView) findViewById(R.id.text_input_number8);
        numberView9 = (TextView) findViewById(R.id.text_input_number9);

        deleteView = (RelativeLayout) findViewById(R.id.delete_key_layout);
        textForgetPassword = (TextView) findViewById(R.id.text_forget_password);
    }

    @Override
    public void initData() {
        textViews.add(textView1);
        textViews.add(textView2);
        textViews.add(textView3);
        textViews.add(textView4);
        textViews.add(textView5);
        textViews.add(textView6);
    }

    @Override
    public void initListener() {
        numberView0.setOnClickListener(this);
        numberView1.setOnClickListener(this);
        numberView2.setOnClickListener(this);
        numberView3.setOnClickListener(this);
        numberView4.setOnClickListener(this);
        numberView5.setOnClickListener(this);
        numberView6.setOnClickListener(this);
        numberView7.setOnClickListener(this);
        numberView8.setOnClickListener(this);
        numberView9.setOnClickListener(this);
        deleteView.setOnClickListener(this);

        imgv_close.setOnClickListener(this);
        textForgetPassword.setOnClickListener(this);
    }

    @Override
    public int getWindowAnimationsResId() {
        return R.style.BottomDialogAnim;
    }

    @Override
    public int getLayoutResID() {
        return R.layout.layout_dialog_payment;
    }

    @Override
    public int getWidth() {
        return android.view.ViewGroup.LayoutParams.MATCH_PARENT;
    }

    @Override
    public int getHeight() {
        return android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public int getGravity() {
        return Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.text_input_number0:
                String inputNumber = "0";
                initTextViews(inputNumber);
                break;

            case R.id.text_input_number1:
                inputNumber = "1";
                initTextViews(inputNumber);
                break;

            case R.id.text_input_number2:
                inputNumber = "2";
                initTextViews(inputNumber);
                break;

            case R.id.text_input_number3:
                inputNumber = "3";
                initTextViews(inputNumber);
                break;

            case R.id.text_input_number4:
                inputNumber = "4";
                initTextViews(inputNumber);
                break;

            case R.id.text_input_number5:
                inputNumber = "5";
                initTextViews(inputNumber);
                break;

            case R.id.text_input_number6:
                inputNumber = "6";
                initTextViews(inputNumber);
                break;

            case R.id.text_input_number7:
                inputNumber = "7";
                initTextViews(inputNumber);
                break;

            case R.id.text_input_number8:
                inputNumber = "8";
                initTextViews(inputNumber);
                break;

            case R.id.text_input_number9:
                inputNumber = "9";
                initTextViews(inputNumber);
                break;

            case R.id.delete_key_layout:
                for (int i = numberList.size() - 1; i >= 0; i--) {
                    TextView textView = textViews.get(i);
                    if (!TextUtils.isEmpty(textView.getText().toString().trim())) {
                        textViews.get(i).setText("");
                        numberList.remove(i);
                        numberString.deleteCharAt(i);
                        break;
                    }
                }
                Log.d("--fff->:", numberList.toString());
                break;

            case R.id.text_forget_password:
                setViewNull();
                dismiss();
                MiBaoQuestionActivity.startActivity(context, MiBaoQuestionActivity.Type_MiBao_Verification_CHANGE_PWD);
                break;

            case R.id.imgv_close:
                dismiss();
                break;
        }
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
                    textViews.get(i).setText("*");
                    break;
                }
            }
        }

        if (6 == numberList.size()) {
            checkPayPassword();
        }
    }

    private void checkPayPassword() {
        Logger.d("PaymentDialog", "输入密码：" + numberString.toString());
        payPwdRepository.checkPayPwd(
                UserManager.getInstance().getUserId() + ""
                , numberString.toString()
                , new BaseRepository.HttpCallListener<FuturePayApiResult>() {
                    @Override
                    public void onHttpCallFaile(int code, String msg) {
                        if (paymentDialogListener != null) {
                            paymentDialogListener.onPaymentPwdFaile(msg);
                        }
                        if (paymentDialogWithPwdListener != null) {
                            paymentDialogWithPwdListener.onPaymentPwdFaile(msg);
                        }
                        setViewNull();
                    }

                    @Override
                    public void onHttpCallSuccess(FuturePayApiResult data) {
                        if (paymentDialogListener != null) {
                            paymentDialogListener.onPaymentPwdSuccess();

                        }
                        if (paymentDialogWithPwdListener != null) {
                            paymentDialogWithPwdListener.onPaymentPwdSuccess(numberString.toString());
                        }
                        dismiss();
                    }
                }
        );
    }

    /**
     * 置空
     */
    private void setViewNull() {
        numberString = new StringBuffer();
        numberList.clear();
        for (int i = 0; i < textViews.size(); i++) {
            textViews.get(i).setText("");
        }
    }

    public void show() {
        setViewNull();
        super.show();
    }

    private PaymentDialogListener paymentDialogListener;

    private interface PaymentDialogListener {
        void onPaymentPwdSuccess();

        void onPaymentPwdFaile(String msg);
    }


    /**
     * 返回密码的监听
     */
    private PaymentDialogWithPwdListener paymentDialogWithPwdListener;

    public interface PaymentDialogWithPwdListener {
        void onPaymentPwdSuccess(String password);

        void onPaymentPwdFaile(String msg);
    }

}
