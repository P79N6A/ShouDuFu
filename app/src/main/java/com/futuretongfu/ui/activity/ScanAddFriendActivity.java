package com.futuretongfu.ui.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.futuretongfu.bean.FriendBean;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.component.contacts.model.ContactModel;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.StringUtil;
import com.futuretongfu.utils.Util;
import com.futuretongfu.R;
import com.futuretongfu.base.BaseSerializable;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.presenter.home.SearchFriendPresenter;
import com.futuretongfu.utils.Logger.Logger;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 扫描加好友
 *
 * @author DoneYang 2017/6/22
 */

public class ScanAddFriendActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tv_title;

    @Bind(R.id.image_scan_addfriend_head)
    ImageView image_head;

    @Bind(R.id.tv_scan_addfriend_nick_name)
    TextView tv_nickName;

    @Bind(R.id.tv_scan_addfriend_email)
    TextView tv_email;

    @Bind(R.id.tv_scan_addfriend_name)
    TextView tv_friend_name;

    @Bind(R.id.tv_scan_addfriend_add)
    TextView tv_addfriend;

    @Bind(R.id.ll_scan_addfriend_really)
    LinearLayout ll_really;

    @Bind(R.id.tv_scan_addfriend_real_sign)
    TextView tv_real_sign;

    private SearchFriendPresenter mPresenter;

    private String friendId = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_scan_addfriend;
    }

    @Override
    protected Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (mPresenter == null) {
            mPresenter = new SearchFriendPresenter(this, this);
        }
        String userId = "" + UserManager.getInstance().getUserId();
        friendId = getIntent().getStringExtra(IntentKey.WLF_ID);
        String json = getIntent().getStringExtra(IntentKey.WLF_BEAN);
        if (!Util.isEmpty(json)) {
            ContactModel.MembersEntity entity = JSONObject.parseObject(json, ContactModel.MembersEntity.class);
            if (!Util.isEmpty(entity)) {
                tv_addfriend.setEnabled(false);
                tv_addfriend.setTextColor(ContextCompat.getColor(this, R.color.front_s7));

                if (!Util.isEmpty(entity.getRealName())) {
                    tv_real_sign.setText("已实名");
                    tv_friend_name.setText(StringUtil.setVisibilityName(entity.getRealName()));
                } else {
                    tv_real_sign.setText("未实名");
                }
                ll_really.setVisibility(View.VISIBLE);
                if (!Util.isEmpty(entity.getId())) {
                    friendId = entity.getId();
                }

                if (!Util.isEmpty(entity.getUsername())) {
                    tv_nickName.setText(entity.getUsername());
                } else {
                    if (!Util.isEmpty(entity.getMobile())) {
                        tv_nickName.setText(StringUtil.getStarString(entity.getMobile(), 3, 7));
                    }
                }

                if (!Util.isEmpty(entity.getFaceURL())) {
                    GlideLoad.loadHead(entity.getFaceURL(), image_head);
                }
            }
        }
        if (!Util.isEmpty(friendId) && !Util.isEmpty(userId)) {
            Logger.i(TAG, "userId=" + userId + ",friendId=" + friendId);
            mPresenter.onSearchFriend(false, userId, friendId);
        }

    }

    @OnClick({R.id.bt_back, R.id.tv_scan_addfriend_tranfer, R.id.tv_scan_addfriend_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finishWidthAnimation();
                break;

            /*转账*/
            case R.id.tv_scan_addfriend_tranfer:
                if (!Util.isEmpty(friendId)) {
                    IntentUtil.startActivity(this, TranferAccountActivity.class, IntentKey.WLF_ID, friendId);
                }
                break;


            /*加好友*/
            case R.id.tv_scan_addfriend_add:
                if (Util.isEmpty(friendId)) {
                    return;
                }
                IntentUtil.startActivity(this, FriendVerificationActivity.class
                        , IntentKey.WLF_ID, friendId);
                break;
        }
    }

    @Override
    public void onSuccess(BaseSerializable data) {
        super.onSuccess(data);
        if (!Util.isEmpty(data)) {
            FriendBean friendBean = (FriendBean) data;
            if (!Util.isEmpty(friendBean)) {
                if (!Util.isEmpty(friendBean.realName)) {
                    tv_friend_name.setText(friendBean.realName);
                    tv_real_sign.setText("已实名");
                } else {
                    tv_real_sign.setText("未实名");
                }
                ll_really.setVisibility(View.VISIBLE);
                if (!Util.isEmpty(friendBean.nickName)) {
                    tv_nickName.setText(friendBean.nickName);
                } else {
                    if (!Util.isEmpty(friendBean.phone)) {
                        tv_nickName.setText(friendBean.phone);
                    }
                }

                if (!Util.isEmpty(friendBean.phone)) {
                    tv_email.setText(friendBean.phone);
                }

                if (!Util.isEmpty(friendBean.faceURL)) {
                    GlideLoad.loadHead(friendBean.faceURL, image_head);
                }

                if (friendBean.friend) {
                    tv_addfriend.setEnabled(false);
                    tv_addfriend.setTextColor(ContextCompat.getColor(this, R.color.front_s7));
                }
            }
        }
    }

    @Override
    public void onFail(int code, String msg) {
        super.onFail(code, msg);
        showToast(msg);
    }

    //返回按键
    @Override
    public void onBackPressed() {
        boolean success = getSupportFragmentManager().popBackStackImmediate();
        if (!success)
            finishWidthAnimation();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("deprecation")
    public  void finishWidthAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }
}
