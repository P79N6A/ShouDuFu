package com.futuretongfu.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.futuretongfu.listener.OnRecyclerViewItemClickListener;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.contacts.ContactsPresenter;
import com.futuretongfu.ui.component.contacts.model.ContactModel;
import com.futuretongfu.ui.component.contacts.pinyin.CharacterParser;
import com.futuretongfu.ui.component.contacts.widget.SideBar;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.R;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.iview.IContactsView;
import com.futuretongfu.model.entity.ContactsFriend;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.adapter.TranferFriendAdapter;
import com.futuretongfu.ui.component.contacts.indexRecyclerView.expand.StickyRecyclerHeadersDecoration;
import com.futuretongfu.ui.component.contacts.pinyin.PinyinComparator;
import com.futuretongfu.utils.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 转账好友
 *
 * @author DoneYang 2017/6/22
 */

public class TranferFriendActivity extends BaseActivity implements OnRecyclerViewItemClickListener, IContactsView {

    @Bind(R.id.tv_title)
    TextView tv_title;

    @Bind(R.id.rv_tranfer_friend)
    RecyclerView rv_friend;

    @Bind(R.id.tv_tranfer_friend_center)
    TextView tv_center;

    @Bind(R.id.sideber_tranfer_friend)
    SideBar sideber_letter;

    @Bind(R.id.fl_transfer_friend_list)
    FrameLayout fl_friend_list;

    @Bind(R.id.ll_nodata)
    LinearLayout ll_nodata;

    @Bind(R.id.tv_layou_search_nodata_content)
    TextView tv_nodata_content;

    private ContactsPresenter mPresenter;

    private CharacterParser characterParser;
    private PinyinComparator pinyinComparator;
    private TranferFriendAdapter friendAdapter;
    private List<ContactModel.MembersEntity> datas = new ArrayList<>();

    private String userId = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_tranfer_friend;
    }

    @Override
    protected Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        tv_title.setText("选择好友");

        if (mPresenter == null) {
            mPresenter = new ContactsPresenter(this, this);
        }
        userId = "" + UserManager.getInstance().getUserId();
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();

        sideber_letter.setTextView(tv_center);
        init();
        initData();
    }

    private void initData() {
        if (!Util.isEmpty(userId)) {
            showProgressDialog();
            mPresenter.getContactsList();
        }
    }

    @OnClick({R.id.bt_back, R.id.ll_nodata})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;

            case R.id.ll_nodata:
                if (!Util.isEmpty(userId)) {
                    showProgressDialog();
                    mPresenter.getContactsList();
                }
                break;
        }
    }

    private void init() {

        sideber_letter.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {

                int position = friendAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    rv_friend.getLayoutManager().scrollToPosition(position);
                }

            }
        });

//        seperateLists();

        friendAdapter = new TranferFriendAdapter(this, null);
        friendAdapter.setOnItemClickListener(this);
        rv_friend.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rv_friend.setAdapter(friendAdapter);

        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(friendAdapter);
        rv_friend.addItemDecoration(headersDecor);

        friendAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });
    }

    private List<ContactModel.MembersEntity> seperateLists(List<ContactsFriend> srcDatas) {

        datas.clear();
        for (ContactsFriend item : srcDatas) {
            datas.add(new ContactModel.MembersEntity(item));
        }

        for (ContactModel.MembersEntity entity : datas) {
            String pinyin = characterParser.getSelling(entity.getUsername());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                entity.setSortLetters(sortString.toUpperCase());
            } else {
                entity.setSortLetters("#");
            }
        }

        Collections.sort(datas, pinyinComparator);

        friendAdapter.notifyDataSetChanged();
        return datas;
    }


    @Override
    public void onItemClick(View view, int position) {
        if (!Util.isEmpty(datas.get(position).getId())) {
            IntentUtil.startActivity(this, TranferAccountActivity.class
                    , IntentKey.WLF_ID, datas.get(position).getId());
        }
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    @Override
    public void onContactsSuccess(List<ContactsFriend> datas) {
        hideProgressDialog();
        if (!Util.isEmpty(datas) && datas.size() > 0) {
            ll_nodata.setVisibility(View.GONE);
            fl_friend_list.setVisibility(View.VISIBLE);
            friendAdapter.setDatas(seperateLists(datas));
//            seperateLists(datas);
//            friendAdapter.notifyDataSetChanged();
        } else {
            tv_nodata_content.setText("您还没有好友哦~");
            ll_nodata.setVisibility(View.VISIBLE);
            fl_friend_list.setVisibility(View.GONE);
        }
    }

    @Override
    public void onContactsFaile(String msg) {
        hideProgressDialog();
        tv_nodata_content.setText("获取好友列表失败,点击空白刷新");
        ll_nodata.setVisibility(View.VISIBLE);
        fl_friend_list.setVisibility(View.GONE);
        showToast(msg);
    }

    @Override
    public void onContactsNumberSuccess(String num) {

    }

    @Override
    public void onContactsNumberFaile(String msg) {
        showToast(msg);
    }
}
