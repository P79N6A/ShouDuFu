package com.futuretongfu.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.futuretongfu.bean.FriendBean;
import com.futuretongfu.constants.RequestCode;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.R;
import com.futuretongfu.base.BaseSerializable;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.home.SearchFriendPresenter;
import com.futuretongfu.utils.CacheActivityUtil;
import com.futuretongfu.utils.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 查询好友
 *
 * @author DoneYang 2017/6/27
 */

public class SearchFriendActivity extends BaseActivity {

    @Bind(R.id.edt_search_friend_input)
    EditText edt_input;

    @Bind(R.id.image_search_friend_clear)
    ImageView image_clear;

    @Bind(R.id.tv_search_friend_hint)
    TextView tv_hint;

    @Bind(R.id.ll_search_friend_hint)
    LinearLayout ll_hint;

    @Bind(R.id.rv_search_friend_result)
    RecyclerView rv_result;

    @Bind(R.id.ll_item_tranfer_friend)
    LinearLayout ll_friend;

    @Bind(R.id.image_item_tranfer_friend_head)
    ImageView image_head;

    @Bind(R.id.tv_item_tranfer_friend_name)
    TextView tv_name;

    @Bind(R.id.tv_item_tranfer_friend_mobile)
    TextView tv_mobile;

    @Bind(R.id.ll_nodata)
    LinearLayout ll_nodata;


    private SearchFriendPresenter searchFriendPresenter;

    private List<FriendBean> friendList = new ArrayList<>();

    private FriendBean bean = null;

    private int index = -1;

    private String userId = null;

//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            StartSearch();
//        }
//    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_friend;
    }

    @Override
    protected Presenter getPresenter() {
        return searchFriendPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        CacheActivityUtil.addNewActivity(this);
        if (searchFriendPresenter == null) {
            searchFriendPresenter = new SearchFriendPresenter(this, this);
        }
        userId = "" + UserManager.getInstance().getUserId();
        index = getIntent().getIntExtra(IntentKey.WLF_BEAN, 0);
        setRecycler();
        edt_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                rv_result.setVisibility(View.GONE);
                ll_friend.setVisibility(View.GONE);
                ll_nodata.setVisibility(View.GONE);
                if (!Util.isEmpty(userId)) {
                    if (charSequence.length() >= 5) {
                        String hint = charSequence.toString();
                        searchFriendPresenter.onSearchFriend(true, userId, hint);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        hander.sendEmptyMessageDelayed(0, 300);
    }

    //延时设置打开页面就展示软键盘
    private Handler hander = new Handler() {
        public void handleMessage(android.os.Message msg) {
            edt_input.setFocusable(true);
            edt_input.setFocusableInTouchMode(true);
            edt_input.requestFocus();
            InputMethodManager inputManager = (InputMethodManager) edt_input.getContext().getSystemService(INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(edt_input, 0);
        }
    };

    /**
     * RecyclerView初始化
     */
    private void setRecycler() {
        SearchFriendAdapter mAdapter = new SearchFriendAdapter(friendList);
        Util.setRecyclerViewLayoutManager(this, rv_result, R.color.transparent);
        rv_result.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                if (index == RequestCode.FRIEND_TRANSFER) {
                    if (!Util.isEmpty(friendList) && friendList.size() > 0) {

                        if (userId.equals(friendList.get(position).userId)) {
                            showToast("不能自己转账自己");
                            return;
                        }
                        IntentUtil.startActivity(SearchFriendActivity.this, TranferAccountActivity.class
                                , IntentKey.WLF_ID, friendList.get(position).userId);
                    }

                } else if (index == RequestCode.FRIEND_LIST) {
                    if (!Util.isEmpty(friendList) && friendList.size() > 0) {

                        if (userId.equals(friendList.get(position).userId)) {
                            showToast("不能自己添加自己");
                            return;
                        }
                        if (friendList.get(position).friend) {
                            showToast("已是您的好友，请勿重复添加");
                            return;
                        }
                        IntentUtil.startActivity(SearchFriendActivity.this, FriendVerificationActivity.class
                                , IntentKey.WLF_ID, friendList.get(position).userId);
                    }
                }


            }
        });
    }

//    /**
//     * 搜索提示
//     */
//    private void StartSearch() {
//        if (TextUtils.isEmpty(edt_input.getText())) {
//            ll_hint.setVisibility(View.GONE);
//            return;
//        }
//        String hint = edt_input.getText().toString();
//        tv_hint.setText(hint.trim());
//        ll_hint.setVisibility(View.VISIBLE);
//    }

    @OnClick({R.id.image_search_friend_clear, R.id.tv_search_friend_cancel, R.id.ll_search_friend_hint
            , R.id.ll_item_tranfer_friend})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            /*清空*/
            case R.id.image_search_friend_clear:
                edt_input.setText("");
                image_clear.setVisibility(View.GONE);
                break;

            /*取消*/
            case R.id.tv_search_friend_cancel:
                finish();
                break;

            /*搜索*/
            case R.id.ll_search_friend_hint:
//                String hint = tv_hint.getText().toString();
//                if (!Util.isEmpty(userId)) {
//                    if (!StringUtil.isPhoneNumber(hint)) {
//                        showToast("请输入合法手机号");
//                        return;
//                    }
//                    searchFriendPresenter.onSearchFriend(true, userId, hint);
//                }
                break;

            case R.id.ll_item_tranfer_friend:
                if (!Util.isEmpty(bean)) {

                    if (index == RequestCode.FRIEND_TRANSFER) {

                        if (userId.equals(bean.userId)) {
                            showToast("不能自己转账自己");
                            return;
                        }
                        IntentUtil.startActivity(SearchFriendActivity.this, TranferAccountActivity.class
                                , IntentKey.WLF_ID, bean.userId);

                    } else if (index == RequestCode.FRIEND_LIST) {
                        if (!Util.isEmpty(bean.userId)) {

                            if (userId.equals(bean.userId)) {
                                showToast("不能自己添加自己");
                                return;
                            }
                            if (bean.friend) {
                                showToast("已是您的好友，请勿重复添加");
                                return;
                            }
                            IntentUtil.startActivity(SearchFriendActivity.this, FriendVerificationActivity.class
                                    , IntentKey.WLF_ID, bean.userId);
                        }
                    }

                }
                break;
        }
    }

    @Override
    public void onSuccess(BaseSerializable data) {
        super.onSuccess(data);
        ll_hint.setVisibility(View.GONE);
        tv_hint.setText("");
        if (!Util.isEmpty(data)) {
            bean = (FriendBean) data;
            if (!Util.isEmpty(bean)) {
                if (!Util.isEmpty(bean.nickName)) {
                    tv_name.setText(bean.nickName);
                } else {
                    if (!Util.isEmpty(bean.phone)) {
                        tv_name.setText("用户" + bean.phone);
                    } else {
                        tv_name.setText("未知用户");
                    }
                }
                if (!Util.isEmpty(bean.phone)) {
                    tv_mobile.setText(bean.phone);
                }
            }

            if (!Util.isEmpty(bean.faceURL)) {
                GlideLoad.loadRound(bean.faceURL, image_head);
            }
            ll_friend.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFail(int code, String msg) {
        super.onFail(code, msg);
        ll_hint.setVisibility(View.GONE);
        ll_nodata.setVisibility(View.VISIBLE);
    }

    private class SearchFriendAdapter extends BaseQuickAdapter<FriendBean, BaseViewHolder> {

        SearchFriendAdapter(@Nullable List<FriendBean> data) {
            super(R.layout.item_tranfer_friend, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, FriendBean item) {

        }
    }
}
