package com.futuretongfu.ui.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.futuretongfu.presenter.home.FriendVerificationPresenter;
import com.futuretongfu.R;
import com.futuretongfu.base.BaseSerializable;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.utils.CacheActivityUtil;
import com.futuretongfu.utils.Util;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 朋友验证
 *
 * @author DoneYang 2017/6/19
 */

public class FriendVerificationActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tv_title;

    @Bind(R.id.bt_right)
    TextView bt_right;

    @Bind(R.id.edt_friend_verification)
    TextInputEditText edt_input;

    @Bind(R.id.image_friend_verification_clear)
    ImageView image_clear;

    private String userId = null;

    private String friendId = null;

    private FriendVerificationPresenter friendVerificationPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_friend_verification;
    }

    @Override
    protected Presenter getPresenter() {
        return friendVerificationPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        tv_title.setText("朋友验证");
        bt_right.setText("发送");

//        edt_input.setFilters(new InputFilter[]{new EditSpaceFilter()});

        if (bt_right.getVisibility() == View.GONE) {
            bt_right.setVisibility(View.VISIBLE);
        }

        if (friendVerificationPresenter == null) {
            friendVerificationPresenter = new FriendVerificationPresenter(this, this);
        }

        Util.setVisibility(edt_input, image_clear);
        friendId = getIntent().getStringExtra(IntentKey.WLF_ID);
        userId = "" + UserManager.getInstance().getUserId();
    }

    @OnClick({R.id.bt_back, R.id.image_friend_verification_clear, R.id.bt_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;

            /*清除输入框*/
            case R.id.image_friend_verification_clear:
                edt_input.setText("");
                image_clear.setVisibility(View.GONE);
                break;

            /*发送*/
            case R.id.bt_right:
//                if (TextUtils.isEmpty(edt_input.getText())) {
//                    showToast("验证消息不能为空");
//                    return;
//                }
                bt_right.setEnabled(false);
                if (friendVerificationPresenter == null) {
                    friendVerificationPresenter = new FriendVerificationPresenter(this, this);
                }
                String message = edt_input.getText().toString();
                if (Util.isEmpty(userId) || Util.isEmpty(friendId)) {
                    return;
                }

                //userId:1149829740005200
                //friendUserId:1149829376135600
                //phone:17727983320
//                friendVerificationPresenter.onFriendVerification("1149829740005200", "1149829376135600", message);
                friendVerificationPresenter.onFriendVerification(userId, friendId, message);
                break;
        }
    }

    @Override
    public void onSuccess(BaseSerializable data) {
        super.onSuccess(data);
        bt_right.setEnabled(true);
        showToast("发送成功");
        //ScanAddFriendActivity.finishWidthAnimation();
        CacheActivityUtil.newFinishActivity();
        finish();
    }

    @Override
    public void onFail(int code, String msg) {
        super.onFail(code, msg);
        bt_right.setEnabled(true);
        showToast(msg);
    }
}
