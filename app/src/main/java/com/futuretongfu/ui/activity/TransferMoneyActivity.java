package com.futuretongfu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.futuretongfu.bean.TransferListBean;
import com.futuretongfu.constants.RequestCode;
import com.futuretongfu.iview.TransferContactIVew;
import com.futuretongfu.listener.IPermissionListener;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.home.TransferContactPresenter;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.Util;
import com.skjr.zxinglib.CaptureActivity;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.futuretongfu.R;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.utils.Logger.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 转账
 *
 * @author DoneYang 2017/6/17
 */

public class TransferMoneyActivity extends BaseActivity implements IPermissionListener, TransferContactIVew {


    @Bind(R.id.tv_title)
    TextView tv_title;

    @Bind(R.id.edt_transfer_money_search)
    TextInputEditText edt_search;

    @Bind(R.id.rv_transfer_money_lately)
    RecyclerView rv_lately;

    @Bind(R.id.tv_transfer_money_nolately)
    TextView tv_nolately;


    private TransferContactPresenter mPresenter;

    private List<TransferListBean> friendList = new ArrayList<>();

    private LatelyAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_transfermoney;
    }

    @Override
    protected Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        tv_title.setText("转账");
        if (mPresenter == null) {
            mPresenter = new TransferContactPresenter(this, this);
        }
        String userId = "" + UserManager.getInstance().getUserId();
        setRecycler();
        if (!Util.isEmpty(userId)) {
            mPresenter.onTransferContract(userId);
        }
        new RxPermissions(this);
    }

    private void setRecycler() {
        mAdapter = new LatelyAdapter(friendList);
        Util.setRecyclerViewLayoutManager(this, rv_lately, R.color.transparent, 4);
        rv_lately.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!friendList.isEmpty() && friendList.size() > position && friendList.get(position) != null && friendList.get(position).userId != 0) {
                    IntentUtil.startActivity(TransferMoneyActivity.this, TranferAccountActivity.class
                            , IntentKey.WLF_ID, "" + friendList.get(position).userId);
                }
            }
        });
    }

    @OnClick({R.id.bt_back, R.id.edt_transfer_money_search, R.id.ll_home_transfer_money_to_friend
            , R.id.ll_home_transfer_money_to_wlf, R.id.ll_home_transfer_money_to_business})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.bt_back:
                finish();
                break;

            /*搜索*/
            case R.id.edt_transfer_money_search:
                IntentUtil.startActivity(this, SearchFriendActivity.class
                        , IntentKey.WLF_BEAN, RequestCode.FRIEND_TRANSFER);
                break;

            /*转朋友*/
            case R.id.ll_home_transfer_money_to_friend:
                IntentUtil.startActivity(this, TranferFriendActivity.class);
                break;

            /*转未来付账户*/
            case R.id.ll_home_transfer_money_to_wlf:
               Intent intent = new Intent(this, TranferWlfAccountActivity.class);
               intent.putExtra(IntentKey.WLF_BEAN, RequestCode.WLF_ACCOUNT);
                startActivity(intent);
                break;

            /*转商户*/
            case R.id.ll_home_transfer_money_to_business:
                Intent intent1 = new Intent(this, TranferWlfAccountActivity.class);
                intent1.putExtra(IntentKey.WLF_BEAN, RequestCode.STORE_ACCOUNT);
                startActivity(intent1);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == RequestCode.REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(IntentKey.SCAN_CONTENT_KEY);

                if (Util.isEmpty(content)) {
                    return;
                }
                Logger.i(TAG, "解码结果：\n" + content);
                showToast(content);
                IntentUtil.startActivity(this, ScanAddFriendActivity.class
                        , IntentKey.WLF_ID, content);
            }
        }
    }

    @Override
    public void onPermissionGranted(String name) {
        IntentUtil.startActivityForResult(this, CaptureActivity.class, RequestCode.REQUEST_CODE_SCAN);
    }

    @Override
    public void onPermissionDenied(String name) {
        showToast("您拒绝了「摄像头」所需要的相关权限!");
    }

    @Override
    public void onTransferContactFail(int code, String msg) {
        rv_lately.setVisibility(View.GONE);
        tv_nolately.setVisibility(View.VISIBLE);
        showToast(msg);
    }

    @Override
    public void onTransferContactSuccess(List<TransferListBean> data) {
        if (!Util.isEmpty(data)) {
            tv_nolately.setVisibility(View.GONE);
            friendList.clear();
            friendList.addAll(data);
            mAdapter.notifyDataSetChanged();
            rv_lately.setVisibility(View.VISIBLE);
        } else {
            rv_lately.setVisibility(View.GONE);
            tv_nolately.setVisibility(View.VISIBLE);
        }
    }

    private class LatelyAdapter extends BaseQuickAdapter<TransferListBean, BaseViewHolder> {

        LatelyAdapter(@Nullable List<TransferListBean> data) {
            super(R.layout.item_tranfer_friend, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, TransferListBean item) {
            TextView tv_nickName = helper.getView(R.id.tv_item_tranfer_friend_name);
            if (item == null) {
                return;
            }
            if (!TextUtils.isEmpty(item.nickName)) {
                tv_nickName.setText(item.nickName);
            } else {
                tv_nickName.setText("");
            }
            if (!TextUtils.isEmpty(item.phone) && item.phone.length() == 11) {
                helper.setText(R.id.tv_item_tranfer_friend_mobile,item.phone.substring(0,3)+"****"+item.phone.substring(7,11));
            }
            ImageView head = helper.getView(R.id.image_item_tranfer_friend_head);
            if (!TextUtils.isEmpty(item.faceUrl)) {
                GlideLoad.loadHead(item.faceUrl, head);
            }
        }
    }
}
