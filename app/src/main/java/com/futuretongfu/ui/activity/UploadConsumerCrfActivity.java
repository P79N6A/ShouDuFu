package com.futuretongfu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.presenter.Presenter;

import butterknife.Bind;
import butterknife.OnClick;

/**
 *     上传消费凭证
 *
 * @author ChenXiaoPeng
 */
public class UploadConsumerCrfActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    public TextView textTitle;
    @Bind(R.id.bt_right)
    public TextView textSubmit;

    /***********************************************************************/
    @Override
    protected int getLayoutId(){
        return R.layout.activity_upload_consumer_crf;
    }

    @Override
    protected Presenter getPresenter(){
        return null;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        textTitle.setText("上传消费凭证");
        textSubmit.setVisibility(View.VISIBLE);
        textSubmit.setText("提交");
    }

    /***********************************************************************/
    @OnClick(R.id.bt_back)
    public void onClickBack(){
        finish();
    }

    /***********************************************************************/

    public static void startActivity(Context context){
        Intent intent = new Intent(context, UploadConsumerCrfActivity.class);
        context.startActivity(intent);
    }
}
