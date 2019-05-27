package com.futuretongfu.ui.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.R;
import com.futuretongfu.presenter.Presenter;

public class PhoneSMSVerifActivity extends BaseActivity {

    private final static String Intent_Extra_Type = "type";

    public final static int Type_Mibao_Find = 0;//找回密保
    private int type = Type_Mibao_Find;


    /***********************************************************************/
    @Override
    protected int getLayoutId(){
        return R.layout.activity_phone_smsverif;
    }

    @Override
    protected Presenter getPresenter(){
        return null;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        Intent intent = getIntent();
        type = intent.getIntExtra(Intent_Extra_Type, Type_Mibao_Find);

    }

    /***********************************************************************/

    public static void startActivity(Context context, int type){
        Intent intent = new Intent(context, PhoneSMSVerifActivity.class);
        intent.putExtra(Intent_Extra_Type, type);
        context.startActivity(intent);
    }
}
