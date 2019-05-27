package com.futuretongfu.ui.activity;

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
import com.futuretongfu.bean.NoticeBean;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.R;
import com.futuretongfu.base.BaseSerializable;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.iview.SystemNoticeListIView;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.home.SystemNoticeListPresenter;
import com.futuretongfu.utils.DateUtil;
import com.futuretongfu.utils.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 系统公告列表
 *
 * @author DoneYang 2017/6/16
 */

public class SystemNoticeListActivity extends BaseActivity implements SystemNoticeListIView {

    @Bind(R.id.rv_system_notice)
    RecyclerView rv_notice;

    @Bind(R.id.tv_title)
    TextView tv_title;

    @Bind(R.id.tv_layou_search_nodata_content)
    TextView tv_nodata_content;

    @Bind(R.id.ll_nodata)
    LinearLayout ll_nodata;

    private SystemNoticeListPresenter mPresenter;
    private List<NoticeBean.ListBean> noticeBeanList = new ArrayList<>();
    private NoticeAdapter mAdapter;

    private String userId = null;
    private int index = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_system_notice;
    }

    @Override
    protected Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        tv_title.setText("系统公告");

        if (mPresenter == null) {
            mPresenter = new SystemNoticeListPresenter(this, this);
        }

        userId = "" + UserManager.getInstance().getUserId();
        if (!Util.isEmpty(userId)) {
            mPresenter.onSystemNoticeList(userId, 1);
        }

        mAdapter = new NoticeAdapter(noticeBeanList);
        Util.setRecyclerViewLayoutManager(this, rv_notice, R.color.transparent);
        rv_notice.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NoticeBean.ListBean  listBean = (NoticeBean.ListBean) adapter.getData().get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable(IntentKey.Notice_Details,listBean);  //实现Serializable接口的对象
                Intent intent = new Intent(SystemNoticeListActivity.this, NoticeDetailsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
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
    public void onSystemNoticeListFail(int code, String msg) {
        rv_notice.setVisibility(View.GONE);
        tv_nodata_content.setText("暂无公告");
        ll_nodata.setVisibility(View.VISIBLE);
        showToast(msg);
    }

    @Override
    public void onSystemNoticeListSuccess(List<NoticeBean.ListBean> data) {
        if (Util.isEmpty(data) || data.size() == 0) {
            rv_notice.setVisibility(View.GONE);
            tv_nodata_content.setText("暂无公告");
            ll_nodata.setVisibility(View.VISIBLE);
        } else {
            ll_nodata.setVisibility(View.GONE);
            noticeBeanList.clear();
            noticeBeanList.addAll(data);
            mAdapter.notifyDataSetChanged();
            rv_notice.setVisibility(View.VISIBLE);
            if (rv_notice.getVisibility() == View.GONE) {
                rv_notice.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onSuccess(BaseSerializable data) {
        super.onSuccess(data);
        if (!Util.isEmpty(data)) {
            if (index != -1) {
                mAdapter.notifyItemChanged(index);
            }
        }
    }

    @Override
    public void onFail(int code, String msg) {
        super.onFail(code, msg);
        showToast(msg);
    }

    private class NoticeAdapter extends BaseQuickAdapter<NoticeBean.ListBean, BaseViewHolder> {

        public NoticeAdapter(@Nullable List<NoticeBean.ListBean> data) {
            super(R.layout.item_system_notice_list, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, NoticeBean.ListBean item) {
            ImageView status = helper.getView(R.id.image_item_system_notice_read);
            helper.setText(R.id.tv_item_system_notice_title, item.getTitle())
                    .setText(R.id.tv_item_system_notice_date, DateUtil.getDateStr2(item.getCreateTime()))
                    .setText(R.id.tv_item_system_notice_content, item.getContent());
        }
    }
}
