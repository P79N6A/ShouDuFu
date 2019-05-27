package com.futuretongfu.ui.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.futuretongfu.listener.OnRecyclerViewItemClickListener;
import com.futuretongfu.presenter.home.PhoneLinkManPresenter;
import com.futuretongfu.ui.adapter.PhoneLinkManAdapter;
import com.futuretongfu.ui.component.contacts.model.ContactModel;
import com.futuretongfu.ui.component.contacts.pinyin.CharacterParser;
import com.futuretongfu.R;
import com.futuretongfu.iview.ISendSMSlView;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.component.contacts.indexRecyclerView.expand.StickyRecyclerHeadersDecoration;
import com.futuretongfu.ui.component.contacts.pinyin.PinyinComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 手机通讯录
 *
 * @author DoneYang 2017/6/27
 */

public class PhoneLinkManActivity extends BaseActivity implements OnRecyclerViewItemClickListener, ISendSMSlView {

    @Bind(R.id.tv_title)
    TextView tv_title;

    @Bind(R.id.edt_phone_linkman_search)
    EditText edt_search;

    @Bind(R.id.rv_phone_linkman)
    RecyclerView rv_linkman;

    @Bind(R.id.tv_layout_nodata_content)
    TextView tv_content;

    @Bind(R.id.ll_layout_nodata)
    LinearLayout ll_nodata;

    @Bind(R.id.ll_phone_linkman_search)
    LinearLayout ll_search;

    private PhoneLinkManPresenter phoneLinkManPresenter;

    private CharacterParser characterParser;
    private PinyinComparator pinyinComparator;
    private PhoneLinkManAdapter phoneLinkManAdapter;
    private List<ContactModel.MembersEntity> friendList = new ArrayList<>();
    private int position = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_phone_linkman;
    }

    @Override
    protected Presenter getPresenter() {
        return phoneLinkManPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        if (phoneLinkManPresenter == null) {
            phoneLinkManPresenter = new PhoneLinkManPresenter(this, this);
        }

        tv_title.setText("手机通讯录");

        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        info();
    }

    /**
     * 获取信息
     */
    private void info() {

        getLinkMan();
        if (friendList == null || friendList.size() == 0) {
            rv_linkman.setVisibility(View.GONE);
            ll_search.setVisibility(View.GONE);
            tv_content.setText("暂无联系人");
            ll_nodata.setVisibility(View.VISIBLE);
            return;
        }

        if (phoneLinkManAdapter == null) {

            phoneLinkManAdapter = new PhoneLinkManAdapter(getContext(), friendList);
            phoneLinkManAdapter.setOnClickListener(this);
//            friendAdapter.setOnItemClickListener(this);//不需要
            rv_linkman.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            rv_linkman.setAdapter(phoneLinkManAdapter);

            final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(phoneLinkManAdapter);
            rv_linkman.addItemDecoration(headersDecor);

            phoneLinkManAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    headersDecor.invalidateHeaders();
                }
            });
        } else {
            phoneLinkManAdapter.notifyDataSetChanged();
        }
    }

    @OnClick({R.id.bt_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;
        }
    }

//    public PhoneLinkManPresenter getPresente() {
//        if (phoneLinkManPresenter == null) {
//            phoneLinkManPresenter = new PhoneLinkManPresenter(this, this);
//        }
//        return phoneLinkManPresenter;
//    }

    /**
     * 获取手机通讯录
     */
    private void getLinkMan() {
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);
        if (cursor == null) {
            return;
        }
        //moveToNext方法返回的是一个boolean类型的数据
        while (cursor.moveToNext()) {
            //读取通讯录的姓名
            String name = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            //读取通讯录的号码
            String number = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            ContactModel.MembersEntity membersEntity = new ContactModel.MembersEntity(name, number);
            friendList.add(membersEntity);
        }

        for (ContactModel.MembersEntity entity : friendList) {
            String pinyin = characterParser.getSelling(entity.getUsername());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                entity.setSortLetters(sortString.toUpperCase());
            } else {
                entity.setSortLetters("#");
            }
        }

        Collections.sort(friendList, pinyinComparator);
    }

    @Override
    public void onItemClick(View view, int position) {
        showProgressDialog();
        this.position = position;
        phoneLinkManPresenter.sendPhoneSMS(friendList.get(position).getMobile().replaceAll(" ",""));
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    @Override
    public void onSendSMSlSuccess() {
        hideProgressDialog();
        friendList.get(position).setAgree(true);
        phoneLinkManAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSendSMSlFaile(String msg) {
        hideProgressDialog();
        friendList.get(position).setAgree(false);
        phoneLinkManAdapter.notifyDataSetChanged();
        showToast(msg);
    }
}
