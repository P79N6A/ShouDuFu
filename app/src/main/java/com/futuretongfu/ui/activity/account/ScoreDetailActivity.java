package com.futuretongfu.ui.activity.account;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


import com.futuretongfu.R;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.RequestCode;
import com.futuretongfu.iview.IScoreDetailView;
import com.futuretongfu.model.entity.Score;
import com.futuretongfu.model.manager.SysDataManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.account.ScoreDetailPresenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.activity.MyBillActivity;
import com.futuretongfu.utils.StringUtil;

import butterknife.Bind;
import butterknife.OnClick;

/**
 *     通贝详情
 *
 * @author ChenXiaoPeng
 */
public class ScoreDetailActivity extends BaseActivity implements IScoreDetailView {

    @Bind(R.id.tv_title)
    public TextView textTitle;
    @Bind(R.id.imgv_eye)
    public ImageView imgvEye;

    @Bind(R.id.text_amount)
    public TextView textAmount;
    @Bind(R.id.text_inter_avlbal)
    public TextView textInterAvlbal;

    private boolean isEyeOpen = true;
    private Score score;
    private ScoreDetailPresenter scoreDetailPresenter;

    /***********************************************************************/
    @Override
    protected int getLayoutId(){
        return R.layout.activity_score_detail;
    }

    @Override
    protected Presenter getPresenter(){
        return scoreDetailPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        textTitle.setText(R.string.jifen_f_detail);
        scoreDetailPresenter = new ScoreDetailPresenter(this, this);

        isEyeOpen = SysDataManager.getInstance().isEyeScoreDetail();
        updateView(isEyeOpen);
    }

    //返回按键
    @Override
    public void onBackPressed() {
        boolean success = getSupportFragmentManager().popBackStackImmediate();
        if (!success)
            super.onBackPressed();
    }

    @Override
    protected void onResume(){
        super.onResume();

        scoreDetailPresenter.score();
    }
    /***********************************************************************/

    /***********************************************************************/
    @OnClick(R.id.bt_back)
    public void onClickBack(){
        finish();
    }

    @OnClick(R.id.imgv_eye)
    public void onClickEye(){
        this.isEyeOpen = !isEyeOpen;

        SysDataManager.getInstance().setEyeScoreDetail(isEyeOpen);
        SysDataManager.getInstance().save();

        updateView(isEyeOpen);
    }

    @OnClick(R.id.btn_bill)
    public void onClickBill(){
        MyBillActivity.startActivity(this, Constants.Bill_Type_Jifen);
    }
    /***********************************************************************/

    /***********************************************************************/
    @Override
    public void onScoreDetailSuccess(Score data){
        this.score = data;
        updateView();
    }

    @Override
    public void onScoreDetailFaile(String msg){
        showToast(msg);
    }
    /***********************************************************************/

    private void updateView(boolean isEyeOpen){
        if(isEyeOpen){
            imgvEye.setImageDrawable(getResources().getDrawable(R.mipmap.icon_visible));
        }
        else{
            imgvEye.setImageDrawable(getResources().getDrawable(R.mipmap.icon_invisible));
        }

        updateView();
    }

    private void updateView(){
        if(isEyeOpen){
            if(null == score) {
                textAmount.setText("0.00");
                textInterAvlbal.setText("0.00");
                return;
            }
            textAmount.setText(StringUtil.fmtMicrometer(score.getAvlBal()));
            textInterAvlbal.setText(StringUtil.fmtMicrometer(score.getInterAvlbal()));
        }
        else{
            textAmount.setText("******");
            textInterAvlbal.setText("***");
        }
    }

    public static void startActivity(Context context){
        Intent intent = new Intent(context, ScoreDetailActivity.class);
        ((Activity)context).startActivityForResult(intent, RequestCode.REQUEST_CODE_JLTYJ);
    }
}
