package com.futuretongfu.ui.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.futuretongfu.base.BaseSerializable;
import com.futuretongfu.iview.ITransMessageView;
import com.futuretongfu.model.entity.MessageListInfo;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.activity.NoticeDetailsActivity;
import com.futuretongfu.utils.Util;
import com.futuretongfu.R;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.presenter.home.SystemNoticeListPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 交易消息列表
 *
 * @author DoneYang 2017/6/16
 */

public class TransactionMessageListActivity extends BaseActivity implements ITransMessageView,
        BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.tv_title)
    TextView tv_title;

    @Bind(R.id.tv_layou_search_nodata_content)
    TextView tv_nodata_content;

    @Bind(R.id.ll_nodata)
    LinearLayout ll_nodata;

    @Bind(R.id.rv_transaction_message_list)
    RecyclerView rv_transaction;

    private SystemNoticeListPresenter mPresenter;
    private TransMessageListAdapter mAdapter;
    private List<MessageListInfo> noticeBeanList = new ArrayList<>();
    private String userId = null;
    private int index = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_transaction_message_list;
    }

    @Override
    protected Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (mPresenter == null) {
            mPresenter = new SystemNoticeListPresenter(this, this);
        }
        tv_title.setText("交易消息");
        userId = "" + UserManager.getInstance().getUserId();
        setRecycler();
        if (!Util.isEmpty(userId)) {
            mPresenter.onTransactionMessageList(userId, 1);
        }
    }

    /**
     * 初始化
     */
    private void setRecycler() {
        mAdapter = new TransMessageListAdapter(noticeBeanList);
        Util.setRecyclerViewLayoutManager(this, rv_transaction, R.color.transparent);
        rv_transaction.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MessageListInfo listBean = (MessageListInfo) adapter.getData().get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable(IntentKey.Message_Details, listBean);  //实现Serializable接口的对象
                Intent intent = new Intent(TransactionMessageListActivity.this, NoticeDetailsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
//                if (noticeBeanList.get(position).status.equals("True")) {
//                    return;
//                }
//                if (!Util.isEmpty(userId) && noticeBeanList.get(position).id != 0) {
//                    mPresenter.onMessageStatus(userId, "" + noticeBeanList.get(position).id);
//                    index = position;
//                }

            }
        });
    }


    @OnClick({R.id.bt_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;

        }
    }

    @Override
    public void onSuccess(BaseSerializable data) {
        super.onSuccess(data);
        if (!Util.isEmpty(data)) {
            if (index != -1) {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onFail(int code, String msg) {
        super.onFail(code, msg);
        showToast(msg);
    }

    @Override
    public void onLoadMoreRequested() {

    }

    @Override
    public void onTransactionMessageListFail(int code, String msg) {
        rv_transaction.setVisibility(View.GONE);
        tv_nodata_content.setText("暂无消息");
        ll_nodata.setVisibility(View.VISIBLE);
        showToast(msg);
    }


    @Override
    public void onTransactionMessageListSuccess(List<MessageListInfo> data) {
        if (Util.isEmpty(data) || data.size() == 0) {
            rv_transaction.setVisibility(View.GONE);
            tv_nodata_content.setText("暂无消息");
            ll_nodata.setVisibility(View.VISIBLE);
        } else {
            ll_nodata.setVisibility(View.GONE);
            noticeBeanList.clear();
            noticeBeanList.addAll(data);
            mAdapter.notifyDataSetChanged();
            rv_transaction.setVisibility(View.VISIBLE);
        }
    }

    private class TransMessageListAdapter extends BaseQuickAdapter<MessageListInfo, BaseViewHolder> {

        public TransMessageListAdapter(@Nullable List<MessageListInfo> data) {
            super(R.layout.item_system_notice_list, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, MessageListInfo item) {
            ImageView status = helper.getView(R.id.image_item_system_notice_read);
            helper.setText(R.id.tv_item_system_notice_title, item.getTitle())
                    .setText(R.id.tv_item_system_notice_date, item.getCreateTime())
                    .setText(R.id.tv_item_system_notice_content, item.getContent());
//            if (item..equals("True")) {
//                status.setSelected(true);
//            } else if (item.status.equals("False")) {
//                status.setSelected(false);
//            }
        }
    }
}
