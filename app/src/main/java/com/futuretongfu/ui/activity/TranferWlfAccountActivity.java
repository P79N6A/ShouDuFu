package com.futuretongfu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.futuretongfu.bean.FriendBean;
import com.futuretongfu.constants.RequestCode;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.R;
import com.futuretongfu.base.BaseSerializable;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.presenter.home.SearchFriendPresenter;
import com.futuretongfu.utils.Logger.Logger;
import com.futuretongfu.utils.Util;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 转账未来付账户
 *
 * @author DoneYang 2017/6/17
 */

public class TranferWlfAccountActivity extends BaseActivity {

    @Bind(R.id.tv_title)
    TextView tv_title;

    @Bind(R.id.edt_tranfer_wlfaccount_money)
    EditText edt_tranfer_wlf;

    @Bind(R.id.tv_tranfer_wlfaccount_next)
    TextView tv_next;

    private SearchFriendPresenter mPresenter;

    private String userId = null;

    private FriendBean bean = null;

    private boolean isStore = false;

    private int userType = -1;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_tranferwlfaccount;
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
        userType = getIntent().getIntExtra(IntentKey.WLF_BEAN, 0);
        userId = "" + UserManager.getInstance().getUserId();
        if (userType ==  RequestCode.WLF_ACCOUNT) {
            tv_title.setText("转首都富账户");
        } else if (userType == RequestCode.STORE_ACCOUNT) {
            tv_title.setText("转商户");
        }
        Util.setEnable(edt_tranfer_wlf, tv_next);
    }

    @OnClick({R.id.bt_back, R.id.tv_tranfer_wlfaccount_next, R.id.image_tranfer_wlf_account_phone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;
            /*下一步*/
            case R.id.tv_tranfer_wlfaccount_next:
                String account = edt_tranfer_wlf.getText().toString();
                if (account.length() < 6) {
                    showToast("账号不能小于6位呦");
                    return;
                }



                if (!Util.isEmpty(userId) && !Util.isEmpty(account)) {
                    mPresenter.onSearchFriend(true, userId, account);
                }
                break;

            /*手机通讯录?*/
            case R.id.image_tranfer_wlf_account_phone:

                break;
        }
    }

    @Override
    public void onSuccess(BaseSerializable data) {
        super.onSuccess(data);
        if (!Util.isEmpty(data)) {
            bean = (FriendBean) data;
        }
        if (!Util.isEmpty(bean)) {
            Intent intent = new Intent(this, TranferAccountActivity.class);
            if (!Util.isEmpty(bean.userId)) {
                intent.putExtra(IntentKey.WLF_ID, bean.userId);
            }
            if (!Util.isEmpty(bean.nickName)) {
                intent.putExtra(IntentKey.TRANSFER_COLLECT_ACCOUNT, bean.nickName);
            }
            intent.putExtra(IntentKey.WLF_BEAN,userType);
            isStore = bean.store;
            if (userType == RequestCode.STORE_ACCOUNT && !isStore) {
                showToast("该账户还不是商家呦");
                return;
            }
            startActivity(intent);
            Logger.i(TAG, "bean.userId=" + bean.userId);
        }
    }

    @Override
    public void onFail(int code, String msg) {
        super.onFail(code, msg);
        showToast(msg);
    }
}
