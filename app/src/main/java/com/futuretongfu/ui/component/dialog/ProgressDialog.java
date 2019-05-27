package com.futuretongfu.ui.component.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.futuretongfu.R;

/**
 * Created by ChenXiaoPeng on 2017/6/22.
 */

public class ProgressDialog extends Dialog {

    private TextView textMsg;

    private String msg = "";
    private boolean isCanceledOnTouchOutside = false;


    public ProgressDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_progress);

        setCancelable(isCanceledOnTouchOutside);
        setCanceledOnTouchOutside(isCanceledOnTouchOutside);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        textMsg = (TextView)findViewById(R.id.text_msg);
        textMsg.setText(msg);

    }

    public void setMessage(String msg){
        this.msg = msg;
    }

    public void setCancelable(boolean isCanceledOnTouchOutside){
        this.isCanceledOnTouchOutside = isCanceledOnTouchOutside;
    }

}
