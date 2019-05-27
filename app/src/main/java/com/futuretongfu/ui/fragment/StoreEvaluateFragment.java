package com.futuretongfu.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.futuretongfu.bean.StoreEvaluateBean;
import com.futuretongfu.ui.activity.StoreDetailsActivity;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.R;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.iview.StoreEvaluateIView;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.wlsq.StoreEvaluatePresenter;
import com.futuretongfu.ui.activity.wlsq.LookBigImageMaxActivity;
import com.futuretongfu.utils.DateUtil;
import com.futuretongfu.utils.Util;
import com.futuretongfu.view.MyGridView;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import static com.futuretongfu.R.id.fragment_store_evaluate_swipe;

/**
 * 商家评价
 *
 * @author DoneYang 2017/6/19
 */

public class StoreEvaluateFragment extends BaseFragment implements StoreEvaluateIView, BaseQuickAdapter.RequestLoadMoreListener {


    @Bind(R.id.rv_frag_store_evaluate)
    RecyclerView rv_evaluate;

    @Bind(fragment_store_evaluate_swipe)
    public SwipeRefreshLayout swipe;

//    @Bind(R.id.ll_nodata)
//    LinearLayout ll_nodata;
//
//    @Bind(R.id.tv_layou_search_nodata_content)
//    TextView tv_nodata_content;

    private StoreEvaluatePresenter storeEvaluatePresenter;

    private List<StoreEvaluateBean> dataList = new ArrayList<>();
    private StoreEvaluateAdapter mAdapter;
    private int page = 1;
    private String shopId;

    @Override
    protected Presenter getPresenter() {
        return storeEvaluatePresenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_common_recycle;
    }

    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (storeEvaluatePresenter == null) {
            storeEvaluatePresenter = new StoreEvaluatePresenter(getActivity(), this);
        }
        shopId = ((StoreDetailsActivity) getActivity()).getStoreId();
        if (!Util.isEmpty(shopId)) {
            storeEvaluatePresenter.onStoreEvaluate(page, shopId);
        } else {
            showToast("id为空");
        }

        mAdapter = new StoreEvaluateAdapter(dataList);
        final LinearLayoutManager layoutManager = Util.setRecyclerViewLayoutManager2(getActivity(), rv_evaluate, R.color.transparent);
        rv_evaluate.setAdapter(mAdapter);
        rv_evaluate.setFocusable(false);

        swipe.setOnRefreshListener(listener);
        swipe.setColorSchemeColors(ContextCompat.getColor(getActivity(),R.color.colorPrimary),ContextCompat.getColor(getActivity(),R.color.green),ContextCompat.getColor(getActivity(),R.color.red));
        mAdapter.setOnLoadMoreListener(this, rv_evaluate);

        rv_evaluate.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int firstVisiblePosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                    if (firstVisiblePosition == 0) {
                        ((StoreDetailsActivity)  getActivity()).appbar.setExpanded(true, true);
                    }
                }
            }
        });
    }

    @Override
    public void onStoreEvaluateFail(int code, String msg) {
        swipe.setRefreshing(false);
        mAdapter.loadMoreFail();
        super.onFail(code, msg);
    }

    @Override
    public void onStoreEvaluateSuccess(List<StoreEvaluateBean> data) {
        swipe.setRefreshing(false);
        if (page == 1) {
            if (!Util.isEmpty(data) && data.size() > 0) {
                mAdapter.setNewData(data);
            } else {
                rv_evaluate.setVisibility(View.GONE);
//                tv_nodata_content.setText("暂无评价");
//                ll_nodata.setVisibility(View.VISIBLE);
//                ll_nodata.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
            }
            ((StoreDetailsActivity)  getActivity()).appbar.setExpanded(true, true);
        } else {
            mAdapter.loadMoreComplete();
            if (!Util.isEmpty(data) && data.size() > 0) {
//                dataList.addAll(data);
                mAdapter.addData(data);
            } else {
                mAdapter.loadMoreEnd();
            }
        }
    }

    @Override
    public void onStoreEvaluateMoreSuccess(List<StoreEvaluateBean> data) {

    }

    @Override
    public void onStoreEvaluateMoreFail(int code, String msg) {

    }

    public StoreEvaluateListener listener = new StoreEvaluateListener();

    private class StoreEvaluateListener implements SwipeRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh() {
            page = 1;
            storeEvaluatePresenter.onStoreEvaluate(page, shopId);
        }
    }

    @Override
    public void onLoadMoreRequested() {
        page++;
        storeEvaluatePresenter.onStoreEvaluate(page, shopId);
    }

    private class StoreEvaluateAdapter extends BaseQuickAdapter<StoreEvaluateBean, BaseViewHolder> {

        StoreEvaluateAdapter(@Nullable List<StoreEvaluateBean> data) {
            super(R.layout.item_store_details_evaluate_list, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, StoreEvaluateBean item) {
            ImageView strar1 = helper.getView(R.id.image_item_store_details_evaluate_star1);
            ImageView strar2 = helper.getView(R.id.image_item_store_details_evaluate_star2);
            ImageView strar3 = helper.getView(R.id.image_item_store_details_evaluate_star3);
            ImageView strar4 = helper.getView(R.id.image_item_store_details_evaluate_star4);
            ImageView strar5 = helper.getView(R.id.image_item_store_details_evaluate_star5);
            ImageView headView = helper.getView(R.id.image_item_store_details_evaluate_head);
            MyGridView gridView = helper.getView(R.id.gv_item_store_details_evaluate_picture);

            helper.setText(R.id.tv_item_store_details_evaluate_name, TextUtils.isEmpty(item.userName) ? "匿名用户" : item.userName);
            if (!TextUtils.isEmpty(item.faceUrl)) {
                GlideLoad.load(item.faceUrl, headView);
            }
            helper.setText(R.id.tv_item_store_details_evaluate_content, TextUtils.isEmpty(item.content) ? "" : "" + item.content);
            if (item.createTime != 0) {
                String date = DateUtil.getDateStr3(item.createTime);
                helper.setText(R.id.tv_item_store_details_evaluate_date, date);
            }

            int grade = item.grade;
            if (grade == 1) {
                strar1.setVisibility(View.VISIBLE);
                strar2.setVisibility(View.GONE);
                strar3.setVisibility(View.GONE);
                strar4.setVisibility(View.GONE);
                strar5.setVisibility(View.GONE);
            } else if (grade == 2) {
                strar1.setVisibility(View.VISIBLE);
                strar2.setVisibility(View.VISIBLE);
                strar3.setVisibility(View.GONE);
                strar4.setVisibility(View.GONE);
                strar5.setVisibility(View.GONE);
            } else if (grade == 3) {
                strar1.setVisibility(View.VISIBLE);
                strar2.setVisibility(View.VISIBLE);
                strar3.setVisibility(View.VISIBLE);
                strar4.setVisibility(View.GONE);
                strar5.setVisibility(View.GONE);
            } else if (grade == 4) {
                strar1.setVisibility(View.VISIBLE);
                strar2.setVisibility(View.VISIBLE);
                strar3.setVisibility(View.VISIBLE);
                strar4.setVisibility(View.VISIBLE);
                strar5.setVisibility(View.GONE);
            } else {
                strar1.setVisibility(View.VISIBLE);
                strar2.setVisibility(View.VISIBLE);
                strar3.setVisibility(View.VISIBLE);
                strar4.setVisibility(View.VISIBLE);
                strar5.setVisibility(View.VISIBLE);
            }

            if (Util.isEmpty(item.imgStr) || item.imgStr.size() == 0) {
                gridView.setVisibility(View.GONE);
                return;
            }
            gridView.setVisibility(View.VISIBLE);
            final List<String> imgStr = item.imgStr;
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getActivity(), LookBigImageMaxActivity.class);
                    intent.putExtra(IntentKey.LOOKMAX_IMAGE_POSITION, i);
                    intent.putExtra(IntentKey.LOOKMAX_IMAGE_URL, (Serializable) imgStr);
                    startActivity(intent);
                }
            });
            GridAdapter mAdapter = new GridAdapter(imgStr);
            gridView.setAdapter(mAdapter);
        }
    }

    /**
     * 图片的适配器
     */
    private class GridAdapter extends BaseAdapter {

        List<String> lt;

        GridAdapter(List<String> lt) {
            this.lt = lt;
        }

        @Override
        public int getCount() {
            return lt.size();
        }

        @Override
        public Object getItem(int i) {
            return lt.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.item_item_grid_list, viewGroup, false);
                new ImageViewHolder(view);
            }
            ImageViewHolder viewHolder = (ImageViewHolder) view.getTag();
            if (!Util.isEmpty(lt.get(i))) {
//                Logger.i(TAG,"ImageUrl="+lt.get(i));
                GlideLoad.loadRound(lt.get(i), viewHolder.iv_gv_item_icon);
            }

            return view;
        }
    }

    private class ImageViewHolder {

        ImageView iv_gv_item_icon;

        ImageViewHolder(View view) {
            iv_gv_item_icon = (ImageView) view.findViewById(R.id.image_item_item_grid);
            view.setTag(this);
        }
    }

}
