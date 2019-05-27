package com.futuretongfu.ui.activity.goods;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.futuretongfu.R;
import com.futuretongfu.bean.ChoosePupulBean;
import com.futuretongfu.bean.onlinegoods.GoodsBrandBean;
import com.futuretongfu.bean.onlinegoods.GoodsDataBean;
import com.futuretongfu.bean.onlinegoods.GoodsSearchDataBean;
import com.futuretongfu.bean.onlinegoods.GoodsSearchTermBean;
import com.futuretongfu.iview.IOnlineSearchGoodsView;
import com.futuretongfu.model.manager.HistoryRecordObject;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.goods.SearchGoodsPresenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.adapter.SearchBrandAdapter;
import com.futuretongfu.ui.adapter.SearchGoodsAdapter;
import com.futuretongfu.ui.adapter.SearchTremAdapter;
import com.futuretongfu.ui.adapter.SelectAdapter;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.Util;
import com.futuretongfu.view.CustomLayout.SpaceItemDecoration;
import com.futuretongfu.view.SupportPopupWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zhanggf on 2018/3/26.
 * 搜索排序
 */

public class SearchRankActivity extends BaseActivity implements View.OnClickListener, IOnlineSearchGoodsView, SearchGoodsAdapter.SearchGoodsAdapterListener {
    @Bind(R.id.swp_list)
    public SwipeRefreshLayout swpList;
    @Bind(R.id.tv_search_general)
    RadioButton rbSearchGeneral;
    @Bind(R.id.tv_search_selas)
    RadioButton rbSearchSelas;
    @Bind(R.id.tv_search_price)
    RadioButton rbSearchPrice;
    @Bind(R.id.tv_search_choose)
    RadioButton rbSearchChoose;
    @Bind(R.id.layout)
    RadioGroup layout;
    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.rv_searchrank_store)
    RecyclerView rvSearchrankStore;
    @Bind(R.id.et_search)
    AutoCompleteTextView etSearch;
    private SearchGoodsAdapter goodsAdapter;
    private SupportPopupWindow popupWindow;
    private String searchField="";
    private List<GoodsBrandBean> brandBeanList = new ArrayList<>();
    private List<GoodsSearchTermBean> searchTermBeanList = new ArrayList<>();
    private SearchGoodsPresenter presenter;
    private int mark=0;  //mark为0综合排序，1价格升;2价格降低;3评论;4新品;5销量;6为按条件筛选排序
    private String lowestPrice="", highestPrice="";
    private String categoryId="";
    private String brandId="";
    private String classId="";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_rank;
    }

    @Override
    protected Presenter getPresenter() {
        return presenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        searchField = getIntent().getStringExtra("searchField");
        classId = getIntent().getStringExtra("classId");
        etSearch.setText(searchField);
        Util.setRecyclerViewLayoutManager(getContext(), rvSearchrankStore, R.color.transparent, 2);
        presenter = new SearchGoodsPresenter(this,this);
        presenter.onSearchGoodsList(searchField,mark,"","","",searchTermBeanList,classId);
        List<GoodsSearchDataBean> datas = new ArrayList<>();
        goodsAdapter = new SearchGoodsAdapter(getContext(),this,datas);
        rvSearchrankStore.setAdapter(goodsAdapter);
        swpList.setColorSchemeResources(R.color.colorPrimary);
        swpList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onSearch(mark);
            }
        });
        goodsAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                presenter.onSearchGoodsListUpLoad(searchField,mark,lowestPrice,highestPrice,brandId,searchTermBeanList,classId);
            }
        }, rvSearchrankStore);
        goodsAdapter.disableLoadMoreIfNotFullPage(rvSearchrankStore);
        goodsAdapter.setEmptyView(R.layout.layout_recycler_empty_view);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
                //以下方法防止两次发送请求
                if (actionId == EditorInfo.IME_ACTION_SEND ||
                        (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    switch (keyEvent.getAction()) {
                        case KeyEvent.ACTION_DOWN:
                            if (!etSearch.getText().toString().trim().equals("")) {
                                onSearch(0);
                                return true;
                            } else {
                                showToast("请输入搜索内容");
                            }
                    }
                }
                return false;
            }
        });
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int position = (int)msg.obj;
            switch (msg.what){
                case 1001:
                    if (position==1){
                        mark = 0;
                    }else if (position==1){
                        mark = 4;
                    }else if (position==2){
                        mark = 3;
                    }
                    levelPosition = position;
                    rbSearchGeneral.setText(selectList.get(position).getName());
                    break;
                case 1002:
                    break;
            }
            onSearch(mark);
            popupWindow.dismiss();
        }
    };



    private List<ChoosePupulBean> selectList;
    private int levelPosition = 0;

    @OnClick({R.id.tv_back, R.id.tv_search_general, R.id.tv_search_selas, R.id.tv_search_price, R.id.tv_search_choose})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            // 0 综合 ；1为价格升序；2为价格降序；3评论；4新品；5销量;6为按条件筛选排序
            case R.id.tv_search_general:  //综合
                rbSearchPrice.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.mipmap.icon_search_price_default),null);
                selectList = new ArrayList<>();
                selectList.add(new ChoosePupulBean(0, "综合排序"));
                selectList.add(new ChoosePupulBean(1, "新品优先"));
                selectList.add(new ChoosePupulBean(2, "评论数从高到低"));
                showPopupWindow(1);
                break;
            case R.id.tv_search_selas:  //销售
                rbSearchPrice.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.mipmap.icon_search_price_default),null);
                mark = 5;
                onSearch(mark);
                break;
            case R.id.tv_search_price:    //价格
                if (mark!=2)
                     mark = 1;
                if (mark == 1){
                    mark = 2;
                    rbSearchPrice.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.mipmap.icon_search_price_up),null);
                }else if (mark == 2){
                    mark = 1;
                    rbSearchPrice.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.mipmap.icon_search_price_down),null);
                }
                onSearch(mark);
                break;
            case R.id.tv_search_choose:   //筛选
                if (!TextUtils.isEmpty(categoryId)){
                    rbSearchPrice.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.mipmap.icon_search_price_default),null);
                    mark = 6;
                    presenter.onSearchTermList();
                    presenter.onSearchBrandList(categoryId);
                }else {
                    showToast("暂无商品");
                }
                break;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_search_price_reset:
                GoodsSearchTermBean bean = null;
                for (int i=0;i<searchTermBeanList.size();i++){
                    bean = new GoodsSearchTermBean();
                    bean.setDictValue("0");
                }
                brandId = "";
                price_start.setText("");
                price_end.setText("");
                tremAdapter = new SearchTremAdapter(this, searchTermBeanList);
                tremAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_search_price_commit:  //筛选确认
                lowestPrice = price_start.getText().toString();
                highestPrice = price_end.getText().toString();
                searchTermBeanList = tremAdapter.getData();
                onSearch(mark);
                popupWindow.dismiss();
                break;
        }
    }

    private void onSearch(int mark) {
        searchField = etSearch.getText().toString();
        presenter.onSearchGoodsList(searchField,mark,lowestPrice,highestPrice,brandId,searchTermBeanList,classId);
    }


    //品牌列表
    @Override
    public void onSearchBrandListViewSuccess(List<GoodsBrandBean> data) {
        brandBeanList = data;
        showPopupWindow(2);
    }

    @Override
    public void onSearchBrandListViewFaile(String msg) {
        showToast(msg);
    }

    //搜索条件
    @Override
    public void onSearchTermListViewSuccess(List<GoodsSearchTermBean> data) {
        searchTermBeanList = data;
    }

    @Override
    public void onSearchTermListViewFaile(String msg) {
        showToast(msg);
    }

    @Override
    public void onSearchGoodsListViewDnUpdateSuccess(List<GoodsSearchDataBean> data) {
        if (swpList != null) {
            swpList.setRefreshing(false);
        }
        if (data!=null&&data.size()>0){
            categoryId = data.get(0).getCategoryId2();
        }
        goodsAdapter.setNewData(data);
    }

    @Override
    public void onSearchGoodsListViewDnUpdateFaile(String msg) {
        if (swpList != null) {
            swpList.setRefreshing(false);
        }
        showToast(msg);
    }

    @Override
    public void onSearchGoodsListDnUpdateUpLoadSuccess(List<GoodsSearchDataBean> datas) {
        goodsAdapter.loadMoreComplete();
        goodsAdapter.addData(datas);
    }

    @Override
    public void onSearchGoodsListDnUpdateUpLoadFaile(String msg) {
        goodsAdapter.loadMoreFail();
    }

    @Override
    public void onSearchGoodsListUpLoadNoDatas() {
        goodsAdapter.loadMoreEnd();
    }


    private TextInputEditText price_start;
    private TextInputEditText price_end;
    private SearchTremAdapter tremAdapter;
    private int brandPosition = -1;
    //选择下拉框
    private void showPopupWindow(int type) {
        popupWindow = new SupportPopupWindow(layout);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        View view = null;
        if (type == 1) {
            view = View.inflate(this, R.layout.layout_popul_search_complex, null);
            RecyclerView rcvClass = (RecyclerView) view.findViewById(R.id.rcv_class);
            Util.setRecyclerViewGridLayoutManager(this, rcvClass, R.color.transparent, 1);
            rcvClass.setAdapter(new SelectAdapter(this, selectList, mHandler, levelPosition));
        } else {
            view = View.inflate(this, R.layout.layout_popul_search_select, null);
            TextView tv_search_price_reset = (TextView) view.findViewById(R.id.tv_search_price_reset);
            TextView tv_search_price_commit = (TextView) view.findViewById(R.id.tv_search_price_commit);
            price_start = (TextInputEditText) view.findViewById(R.id.et_search_price_start);
            price_end = (TextInputEditText) view.findViewById(R.id.et_search_price_end);
            RecyclerView rv_search_brand_list = (RecyclerView) view.findViewById(R.id.rv_search_brand_list);
            RecyclerView rv_search_checkBox_list = (RecyclerView) view.findViewById(R.id.rv_search_checkBox_list);
            tv_search_price_reset.setOnClickListener(this);
            tv_search_price_commit.setOnClickListener(this);
            Util.setRecyclerViewGridLayoutManager(this, rv_search_checkBox_list, R.color.transparent, 3);
            Util.setRecyclerViewGridLayoutManager(this, rv_search_brand_list, R.color.transparent, 3);
            tremAdapter = new SearchTremAdapter(this, searchTermBeanList);
            rv_search_checkBox_list.setAdapter(tremAdapter);
            SearchBrandAdapter adapter = new SearchBrandAdapter(this, brandBeanList,brandPosition);
            rv_search_brand_list.setAdapter(adapter);
            adapter.setOnItemClickLitener(new SearchBrandAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(TextView textView, String id,int position) {
                    brandId = id;
                    levelPosition =position;
                }
            });
        }
        popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        View view1 = view.findViewById(R.id.view);
        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(layout);
    }

    @Override
    public void onSearchGoodsAdapterClick(GoodsSearchDataBean item,int flag) {
        if (flag==0){
            GoodsDetailsActivity.startActivity(1,this,item.getId());
        }else {
            GoodsSpecialDetailsActivity.startActivity(this,item.getId());
        }
    }
}
