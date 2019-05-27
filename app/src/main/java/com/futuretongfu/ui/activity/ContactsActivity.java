package com.futuretongfu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.iview.IContactsView;
import com.futuretongfu.model.entity.ContactsFriend;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.contacts.ContactsPresenter;
import com.futuretongfu.ui.component.contacts.adapter.ContactAdapter;
import com.futuretongfu.ui.component.contacts.indexRecyclerView.expand.StickyRecyclerHeadersDecoration;
import com.futuretongfu.ui.component.contacts.model.ContactModel;
import com.futuretongfu.ui.component.contacts.pinyin.CharacterParser;
import com.futuretongfu.ui.component.contacts.pinyin.PinyinComparator;
import com.futuretongfu.ui.component.contacts.widget.SideBar;
import com.futuretongfu.ui.fragment.PayFragment;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.TimerUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zhanggf on 2018/4/19.
 * 通讯录
 */

public class ContactsActivity extends BaseActivity implements IContactsView {
    @Bind(R.id.tv_title)
    TextView titleView;
    @Bind(R.id.bt_add)
    ImageView addView;

    @Bind(R.id.swp_menber)
    public SwipeRefreshLayout swpMenber;
    @Bind(R.id.contact_member)
    public RecyclerView recyclerList;
    @Bind(R.id.contact_sidebar)
    public SideBar sideBar;
    @Bind(R.id.contact_dialog)
    public TextView userDialog;

    @Bind(R.id.friend_num_view)
    public TextView friend_num_view;

    @Bind(R.id.my_frind_nodata_rl)
    public RelativeLayout my_frind_nodata_rl;

    private ContactsPresenter contactsPresenter;

    private CharacterParser characterParser;
    private PinyinComparator pinyinComparator;
    private ContactAdapter contactAdapter;
    private PayFragment showFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_contacts;
    }

    @Override
    protected Presenter getPresenter() {
        return contactsPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        titleView.setText("通讯录");
        addView.setVisibility(View.VISIBLE);
        contactsPresenter = new ContactsPresenter(getContext(), this);
        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        sideBar.setTextView(userDialog);
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                int position = contactAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    recyclerList.getLayoutManager().scrollToPosition(position);
                }

            }
        });

        contactAdapter = new ContactAdapter(getContext(), null);
        recyclerList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerList.setAdapter(contactAdapter);

        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(contactAdapter);
        recyclerList.addItemDecoration(headersDecor);

        contactAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });

        swpMenber.setColorSchemeResources(R.color.colorPrimary);
        swpMenber.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                contactsPresenter.getContactsList();
            }
        });

        TimerUtil.startTimer(200, new TimerUtil.TimerCallBackListener2() {
            @Override
            public void onEnd() {
                swpMenber.setRefreshing(true);
                contactsPresenter.getContactsList();
            }
        });


    }


    @Override
    public void onResume() {
        super.onResume();
        contactsPresenter.getContactsList();
        if (UserManager.getInstance().isLogin()) {
            contactsPresenter.getueryFriendApplyNum();
        }
    }

    /*******************************************************************/

    /*******************************************************************/
    @OnClick(R.id.view_add_new_friend)
    public void onClickNewFriend() {
        Intent intent = new Intent(getContext(), MyNewFirendActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.bt_add)
    public void onClickAddFriend() {
        IntentUtil.startActivity(getContext(), AddFriendActivity.class);
    }

    @OnClick(R.id.bt_back)
    public void onBackClick() {
        finish();
    }

    @Override
    public void onContactsSuccess(List<ContactsFriend> datas) {
        swpMenber.setRefreshing(false);
        if (datas.size() > 0) {
            swpMenber.setVisibility(View.VISIBLE);
            my_frind_nodata_rl.setVisibility(View.GONE);
            contactAdapter.setDatas(seperateLists(datas));
        } else {
            swpMenber.setVisibility(View.GONE);
            my_frind_nodata_rl.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onContactsFaile(String msg) {
        swpMenber.setVisibility(View.GONE);
        my_frind_nodata_rl.setVisibility(View.VISIBLE);
        swpMenber.setRefreshing(false);
        showToast(msg);
    }

    @Override
    public void onContactsNumberSuccess(String num) {
        if (!TextUtils.isEmpty(num) && !"0".equals(num)) {
            friend_num_view.setText(num);
            friend_num_view.setVisibility(View.VISIBLE);
            if (showFragment != null) {
                showFragment.showFriendNum(true);
            }
        } else {
            if (showFragment != null) {
                showFragment.showFriendNum(false);
                friend_num_view.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onContactsNumberFaile(String msg) {
        showToast(msg);
        if (showFragment != null) {
            showFragment.showFriendNum(false);
        }
    }

    /*******************************************************************/

    private List<ContactModel.MembersEntity> seperateLists(List<ContactsFriend> srcDatas) {
        List<ContactModel.MembersEntity> datas = new ArrayList<>();

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

        return datas;
    }

}
