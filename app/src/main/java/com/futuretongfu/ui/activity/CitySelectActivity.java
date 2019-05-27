package com.futuretongfu.ui.activity;
/*
 * Created by hhm on 2017/7/27.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.futuretongfu.iview.ICitySelectView;
import com.futuretongfu.ui.component.contacts.indexRecyclerView.expand.StickyRecyclerHeadersAdapter;
import com.futuretongfu.ui.component.contacts.pinyin.CharacterParser;
import com.futuretongfu.ui.component.contacts.pinyin.PinyinComparator2;
import com.futuretongfu.utils.StringUtil;
import com.futuretongfu.R;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.model.entity.AddressEntity;
import com.futuretongfu.presenter.CitySelectPresenter;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.component.contacts.indexRecyclerView.expand.StickyRecyclerHeadersDecoration;
import com.futuretongfu.ui.component.contacts.widget.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class CitySelectActivity extends BaseActivity implements ICitySelectView {

    @Bind(R.id.activity_city_select_all)
    public TextView tvAll;
    @Bind(R.id.tv_title)
    public TextView textTitle;
    @Bind(R.id.contact_sidebar)
    public SideBar sideBar;
    @Bind(R.id.contact_dialog)
    public TextView userDialog;
    @Bind(R.id.activity_city_select_recyclerview)
    public RecyclerView recyclerList;
    private CitySelectPresenter mPresenter;
    private SelectCityAdapter mAdapter;
    private ArrayList<AddressEntity> lists;
    private CharacterParser characterParser;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_city_select;
    }

    @Override
    protected Presenter getPresenter() {
        return mPresenter;
    }

    //返回按键
    @Override
    public void onBackPressed() {
        boolean success = getSupportFragmentManager().popBackStackImmediate();
        if (!success)
            super.onBackPressed();
    }

    @OnClick(R.id.bt_back)
    public void onClickBack() {
        finish();
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        textTitle.setText("选择城市");
        characterParser = CharacterParser.getInstance();
        mPresenter = new CitySelectPresenter(this, this);
        lists = new ArrayList<>();
        mAdapter = new SelectCityAdapter(this, lists);
        recyclerList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerList.setAdapter(mAdapter);

        sideBar.setTextView(userDialog);
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                int position = mAdapter.getPositionForSection(s.toUpperCase().charAt(0));
                if (position != -1) {
                    recyclerList.getLayoutManager().scrollToPosition(position);
                }

            }
        });

        tvAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("cityName", "全部");
                setResult(Constants.RESULT_CODE, intent);
                finish();
            }
        });

        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(mAdapter);
        recyclerList.addItemDecoration(headersDecor);

        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });
        showProgressDialog();
        mPresenter.getCityList(Constants.ALL_CITY_SELECT_CODE);
    }

    @Override
    public void onGetCityListSuccess(List<AddressEntity> datas) {
        hideProgressDialog();
        for (int i = 0; i < datas.size(); i++) {
            datas.get(i).setIndex(StringUtil.getPingYin(datas.get(i).getAreaName()));
        }
        lists.clear();
        lists.addAll(datas);
        for (AddressEntity entity : lists) {
            String pinyin = characterParser.getSelling(entity.getIndex());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                entity.setSortLetters(sortString.toUpperCase());
            } else {
                entity.setSortLetters("#");
            }
        }
        PinyinComparator2 pinyinComparator = new PinyinComparator2();
        Collections.sort(lists, pinyinComparator);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetCityListFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
    }

    private class SelectCityAdapter extends RecyclerView.Adapter<SelectCityAdapter.ViewHolder> implements StickyRecyclerHeadersAdapter {

        private final List<AddressEntity> datas;
        private CitySelectActivity context;

        SelectCityAdapter(CitySelectActivity context, List<AddressEntity> datas) {
            if (datas != null) {
                this.datas = datas;
            } else {
                this.datas = new ArrayList<>();
            }
            this.context = context;
        }

        @Override
        public long getHeaderId(int position) {
            return datas.get(position).getIndex().charAt(0);
        }

        @Override
        public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_txl_contact_header, parent, false);
            return new RecyclerView.ViewHolder(view) {
            };
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
            TextView textView = (TextView) holder.itemView;
            String showValue = String.valueOf(datas.get(position).getIndex().charAt(0));
            textView.setText(showValue);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_edit_address, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.ivImage.setVisibility(View.INVISIBLE);
            holder.tvName.setText(datas.get(position).getAreaName());
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("cityName", datas.get(position).getAreaName());
                    context.setResult(Constants.RESULT_CODE, intent);
                    context.finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).hashCode();
        }

        int getPositionForSection(char section) {
            for (int i = 0; i < getItemCount(); i++) {
                String sortStr = datas.get(i).getIndex();
                char firstChar = sortStr.toUpperCase().charAt(0);
                if (firstChar == section) {
                    return i;
                }
            }
            return -1;
        }

        public AddressEntity getItem(int position) {
            return datas.get(position);
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private View view;
            private TextView tvName;
            private ImageView ivImage;

            public ViewHolder(View itemView) {
                super(itemView);
                this.view = itemView;
                tvName = (TextView) itemView.findViewById(R.id.text_name);
                ivImage = (ImageView) itemView.findViewById(R.id.imgv_expansion);
            }
        }
    }
}
