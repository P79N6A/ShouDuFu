package com.futuretongfu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.futuretongfu.iview.HomeMessageListIView;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.R;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.model.entity.MessageListInfo;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.home.SystemMessageListPresenter;
import com.futuretongfu.ui.activity.home.TransactionMessageListActivity;
import com.futuretongfu.utils.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 消息和公告
 *
 * @author DoneYang 2017/6/16
 */

public class MessageActivity extends BaseActivity implements HomeMessageListIView {

    @Bind(R.id.bt_back)
    ImageView bt_back;

    @Bind(R.id.tv_title)
    TextView tv_title;

    @Bind(R.id.title_bar)
    FrameLayout titleBar;

    @Bind(R.id.activity_message_recyclerview)
    RecyclerView recyclerView;

    @Bind(R.id.ll_message_notice)
    LinearLayout ll_notice;

    @Bind(R.id.ll_message_transaction)
    LinearLayout ll_transaction;

    @Bind(R.id.tv_message_notice_num)
    TextView tv_notice_num;

    @Bind(R.id.tv_message_transaction_num)
    TextView tv_transaction_num;

    private SystemMessageListPresenter presenter;

    private MessageAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    protected Presenter getPresenter() {
        return presenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        tv_title.setText("消息");
        if (presenter == null) {
            presenter = new SystemMessageListPresenter(this, this);
        }
        String userId = "" + UserManager.getInstance().getUserId();
        if (!Util.isEmpty(userId)) {
            int page = 1;
            presenter.onSystemMessageList(userId, page);
        }

        mAdapter = new MessageAdapter();
        Util.setRecyclerViewLayoutManager(this, recyclerView, R.color.transparent, 18);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MessageListInfo listBean = (MessageListInfo) adapter.getData().get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable(IntentKey.Message_Details, listBean);  //实现Serializable接口的对象
                Intent intent = new Intent(MessageActivity.this, NoticeDetailsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @OnClick({R.id.bt_back, R.id.ll_message_notice, R.id.ll_message_transaction})
    public void onViewClicked(View view) {

        Intent intent;
        switch (view.getId()) {

            case R.id.bt_back:
                finish();
                break;

            /*系统公告*/
            case R.id.ll_message_notice:
                intent = new Intent(MessageActivity.this, SystemNoticeListActivity.class);
                startActivity(intent);
                break;

            /*交易信息*/
            case R.id.ll_message_transaction:
                intent = new Intent(MessageActivity.this, TransactionMessageListActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onSystemMessageListFail(int code, String msg) {
        showToast(msg);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onSystemMessageListSuccess(List<MessageListInfo> data) {
        recyclerView.setVisibility(View.VISIBLE);
        mAdapter.setNewData(data);
    }


    private class MessageAdapter extends BaseQuickAdapter<MessageListInfo, BaseViewHolder> {

        MessageAdapter() {
            super(R.layout.item_system_notice_list, new ArrayList<MessageListInfo>());
        }

        @Override
        protected void convert(BaseViewHolder helper, MessageListInfo item) {
            helper.getView(R.id.image_item_system_notice_read);
            helper.setText(R.id.tv_item_system_notice_title, item.getTitle())
                    .setText(R.id.tv_item_system_notice_date, item.getCreateTime())
                    .setText(R.id.tv_item_system_notice_content, item.getContent());
        }


    }
}
