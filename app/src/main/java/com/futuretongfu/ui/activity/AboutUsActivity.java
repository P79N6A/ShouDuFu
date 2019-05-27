package com.futuretongfu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.R;

import butterknife.Bind;
import butterknife.OnClick;

/**
 *
 *
 * @author ChenXiaoPeng
 */
public class AboutUsActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    public TextView textTitle;

    /***********************************************************************/
    @Override
    protected int getLayoutId(){
        return R.layout.activity_search_bank_layout;
    }

    @Override
    protected Presenter getPresenter(){
        return null;
    }

    @Override
    protected void init(Bundle savedInstanceState){
        textTitle.setText("关于我们");
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
    public void onClickBack(){
        finish();
    }
    /***********************************************************************/

    public static void startActivity(Context context){
        Intent intent = new Intent(context, AboutUsActivity.class);
        context.startActivity(intent);
    }
}
