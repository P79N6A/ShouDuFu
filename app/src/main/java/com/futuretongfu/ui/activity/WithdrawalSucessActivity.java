package com.futuretongfu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.presenter.Presenter;

import butterknife.Bind;
import butterknife.OnClick;

public class WithdrawalSucessActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    public TextView textTitle;

    /***********************************************************************/
    @Override
    protected int getLayoutId(){
        return R.layout.activity_withdrawal_sucess;
    }

    @Override
    protected Presenter getPresenter(){
        return null;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        textTitle.setText("提现结果");
    }

    /***********************************************************************/
    @OnClick(R.id.bt_back)
    public void onClickBack(){
        finish();
    }

    @OnClick(R.id.btn_finish)
    public void onClickFinish(){
        finish();
    }
    /***********************************************************************/

    public static void startActivity(Context context){
        Intent intent = new Intent(context, WithdrawalSucessActivity.class);
        context.startActivity(intent);
    }

}
