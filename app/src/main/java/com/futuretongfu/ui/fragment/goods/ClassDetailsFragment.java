package com.futuretongfu.ui.fragment.goods;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.futuretongfu.R;
import com.futuretongfu.bean.HomeBannerBean;
import com.futuretongfu.bean.onlinegoods.GoodsDataBean;
import com.futuretongfu.iview.HomeClasssDetailsIView;
import com.futuretongfu.model.entity.HomeRecommendGoodsResult;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.goods.HomeClassSecondPresenter;
import com.futuretongfu.ui.activity.ShowWebViewActivity;
import com.futuretongfu.ui.activity.goods.GoodsDetailsActivity;
import com.futuretongfu.ui.activity.goods.StoreIndexActivity;
import com.futuretongfu.ui.adapter.HomeClassDetailsAdapter;
import com.futuretongfu.ui.fragment.BaseFragment;
import com.futuretongfu.utils.GlideImageLoader;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.SmallFeatureUtils;
import com.futuretongfu.utils.Util;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhanggf on 2018/5/11.
 * 分类详情
 */

public class ClassDetailsFragment extends BaseFragment implements HomeClasssDetailsIView {

    @Bind(R.id.banner_class_details)
    Banner bannerLayout;
    @Bind(R.id.rv_banner_class_details)
    RecyclerView rvBannerClassDetails;
    @Bind(R.id.swp_list)
    SwipeRefreshLayout swpList;
    private String id;
    private HomeClassSecondPresenter mPresenter;
    private HomeClassDetailsAdapter adapter;

    @Override
    protected Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_class_details;
    }

    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        List<GoodsDataBean> datas = new ArrayList<>();
        mPresenter = new HomeClassSecondPresenter(getActivity(), this);
        Util.setRecyclerViewLayoutManager2(getActivity(), rvBannerClassDetails, R.color.transparent, 1);
        adapter = new HomeClassDetailsAdapter(getActivity(), datas);
        rvBannerClassDetails.setAdapter(adapter);

        mPresenter.onBannerList(1);
//        mPresenter.ononClassDetailsList("9007");
        mPresenter.ononClassDetailsList(id);
        swpList.setColorSchemeResources(R.color.colorPrimary);
        swpList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.ononClassDetailsList(id);
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.onClassDetailsUpLoad(id);   //推荐
            }
        }, rvBannerClassDetails);

        adapter.disableLoadMoreIfNotFullPage(rvBannerClassDetails);
        adapter.setEmptyView(R.layout.layout_recycler_empty_view);
    }

    public void setType(String type) {
        this.id = type;
    }

    @Override
    public void onRecomendListFail(int code, String msg) {
        if (swpList != null) {
            swpList.setRefreshing(false);
        }
        showToast(msg);
    }

    @Override
    public void onRecomendListSuccess(HomeRecommendGoodsResult data) {
        if (swpList != null) {
            swpList.setRefreshing(false);
        }
        adapter.setNewData(data.getList());
    }

    @Override
    public void onRecomendListDnUpdateUpLoadSuccess(HomeRecommendGoodsResult datas) {
        adapter.loadMoreComplete();
        adapter.addData(datas.getList());
    }

    @Override
    public void onRecomendListDnUpdateUpLoadFaile(String msg) {
        adapter.loadMoreFail();
    }

    @Override
    public void onRecomendListUpLoadNoDatas() {
        adapter.loadMoreEnd();
    }

    @Override
    public void onBannerListFail(int code, String msg) {
        showToast(msg);
    }

    @Override
    public void onBannerListSuccess(List<HomeBannerBean> data) {
        if (swpList != null) {
            swpList.setRefreshing(false);
        }
        if (!Util.isEmpty(data) && data.size() > 0) {
            initHomeHeader(data);
        }
    }


    /**
     * Banner
     *
     * @param imgLst
     */
    private void initHomeHeader(final List<HomeBannerBean> imgLst) {
        final List<String> imgsLst = new ArrayList<>();
        for (int i = 0; i < imgLst.size(); i++) {
            HomeBannerBean carouselnfo = imgLst.get(i);
            if (null != carouselnfo) {
                String imgUrl = carouselnfo.imageUrl;
                if (!TextUtils.isEmpty(imgUrl)) {
                    imgsLst.add(imgUrl);
                }
            }
        }
        int width = SmallFeatureUtils.getWindowWith();
        // 设置图片宽高
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(width, width * 2 / 3);
        bannerLayout.setLayoutParams(layoutParams);
        bannerLayout.setImages(imgsLst).setImageLoader(new GlideImageLoader()).start();
        bannerLayout.setIndicatorGravity(BannerConfig.CENTER);
        //设置间隔
        bannerLayout.setDelayTime(3000);
        //设置banner动画效果
        bannerLayout.setBannerAnimation(Transformer.DepthPage);
        bannerLayout.start();
        bannerLayout.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                HomeBannerBean bean = imgLst.get(position);
                if (null != bean) {
                    switch (bean.type) {
                        case "1":
                            ShowWebViewActivity.startActivity(getContext(), bean.toUrl, "最新推荐", true);
                            break;
                        case "2":
                            GoodsDetailsActivity.startActivity(getActivity(), bean.toUrl);
                            break;
                        case "3":
                            IntentUtil.startActivity(getActivity(), StoreIndexActivity.class
                                    , "id", bean.toUrl);
                            break;
                    }
                }
            }
        });
    }


}
