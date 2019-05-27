package com.futuretongfu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.iview.IMemberUpgradeView;
import com.futuretongfu.model.entity.MemberListInfo;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.user.MemberUpgradePresenter;
import com.futuretongfu.utils.AppUtil;
import com.futuretongfu.utils.CacheActivityUtil;
import com.futuretongfu.utils.PromptDialogUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 联合代理
 * @author zhanggf
 */
public class JointAcencyActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    public TextView textTitle;

    /***********************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_jointacency;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        CacheActivityUtil.addActivityWithClear(this);
        textTitle.setText("联合代理");
    }

    /***********************************************************************/
    @OnClick(R.id.bt_back)
    public void onClickBack() {
        finish();
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, JointAcencyActivity.class);
        context.startActivity(intent);
    }

}
