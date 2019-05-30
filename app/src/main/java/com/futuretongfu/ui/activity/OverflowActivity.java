package com.futuretongfu.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.futuretongfu.R;
import com.futuretongfu.bean.HomeBannerBean;
import com.futuretongfu.bean.onlinegoods.GoodsDataBean;
import com.futuretongfu.iview.OverIView;
import com.futuretongfu.model.entity.HomeRecommendGoodsResult;
import com.futuretongfu.presenter.OverPresenter;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.activity.goods.GoodsDetailsActivity;
import com.futuretongfu.ui.activity.goods.GoodsSpecialDetailsActivity;
import com.futuretongfu.ui.adapter.HomeRecomendAdapter;
import com.futuretongfu.utils.GlideImageLoader;
import com.futuretongfu.utils.SmallFeatureUtils;
import com.futuretongfu.utils.Util;
import com.futuretongfu.view.NotifyingScrollView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class OverflowActivity extends BaseActivity implements OverIView, HomeRecomendAdapter.SearchGoodsAdapterListener {
    @Bind(R.id.rv_chaozhi)
    RecyclerView mRv_chaozhi;
    @Bind(R.id.banner_chaozhi)
    Banner bannerLayout;
    OverPresenter presenter;
    @Bind(R.id.swp_list)
    SwipeRefreshLayout swpList;
    @Bind(R.id.tv_title)
    TextView mtv_title;
    @Bind(R.id.bt_back)
    ImageView mbt_back;
    private HomeRecomendAdapter recomendAdapter;
    int nums;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_overflow;
    }

    @Override
    protected Presenter getPresenter() {
        return presenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Bundle e = getIntent().getExtras();//取出来的是个数组
        nums = e.getInt("m");
        if (presenter == null) {
            presenter = new OverPresenter(this, this);
        }
        presenter.onBannerList(2);   //Banner

        if (nums == 0) {
            mtv_title.setText("30%现金专区");
            presenter.onRecomendList1();

        } else if (nums == 1) {
            mtv_title.setText("50%现金专区");
            presenter.onRecomendList2();


        } else if (nums == 2) {
            mtv_title.setText("70%现金专区");
            presenter.onRecomendList3();


        } else if (nums == 3) {
            mtv_title.setText("90%现金专区");
            presenter.onRecomendList4();


        } else if (nums == 7) {
            mtv_title.setText("超值专区");
            presenter.onRecomendList();
        }
        mbt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initView();
    }

    private void initView() {
        swpList.setColorSchemeResources(R.color.colorPrimary);
        swpList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.onBannerList(2);
                if (nums == 0) {
                    presenter.onRecomendList1();
                } else if (nums == 1) {
                    presenter.onRecomendList2();
                } else if (nums == 2) {
                    presenter.onRecomendList3();
                } else if (nums == 3) {
                    presenter.onRecomendList4();
                } else if (nums == 7) {
                    presenter.onRecomendList();
                }

            }
        });
        //推荐品牌
        Util.setRecyclerViewGridLayoutManager(this, mRv_chaozhi, R.color.transparent, 2);
        List<GoodsDataBean> datas = new ArrayList<>();
        recomendAdapter = new HomeRecomendAdapter(OverflowActivity.this, this, datas);


        mRv_chaozhi.setAdapter(recomendAdapter);
        recomendAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (nums == 0) {
                    presenter.onRecomendListUpLoad1();
                } else if (nums == 1) {
                    presenter.onRecomendListUpLoad2();
                } else if (nums == 2) {
                    presenter.onRecomendListUpLoad3();
                } else if (nums == 3) {
                    presenter.onRecomendListUpLoad4();
                } else if (nums == 7) {
                    presenter.onRecomendListUpLoad();
                }
            }
        }, mRv_chaozhi);

        recomendAdapter.disableLoadMoreIfNotFullPage(mRv_chaozhi);
        recomendAdapter.setEmptyView(R.layout.layout_recycler_empty_view);
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
        int width = SmallFeatureUtils.getWindowWith();        // 设置图片宽高
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(width, width * 2 / 5);
        bannerLayout.setLayoutParams(layoutParams);
        bannerLayout.setImages(imgsLst).setImageLoader(new GlideImageLoader()).start();
        bannerLayout.setIndicatorGravity(BannerConfig.CENTER);
        //设置间隔
        bannerLayout.setDelayTime(3000);
        //设置banner动画效果
        bannerLayout.setBannerAnimation(Transformer.DepthPage);
        bannerLayout.start();
    }

    private NotifyingScrollView.OnScrollChangedListener onScrollChangedListener = new NotifyingScrollView.OnScrollChangedListener() {
        @Override
        public void OnScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
            if (bannerLayout != null && bannerLayout.getHeight() > 0) {
                int height = bannerLayout.getHeight();
                if (t < height) {
                } else {
                }
            }
        }
    };

    @Override
    public void onBannerListFail(int code, String msg) {
        if (swpList != null)
            swpList.setRefreshing(false);
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
        recomendAdapter.setNewData(data.getList());

    }

    @Override
    public void onRecomendListDnUpdateUpLoadSuccess(HomeRecommendGoodsResult datas) {
        recomendAdapter.loadMoreComplete();
        recomendAdapter.addData(datas.getList());
    }

    @Override
    public void onRecomendListDnUpdateUpLoadFaile(String msg) {
        recomendAdapter.loadMoreFail();
    }

    @Override
    public void onRecomendListUpLoadNoDatas() {
        recomendAdapter.loadMoreEnd();


    }

    @Override
    public void onSearchGoodsAdapterClick(GoodsDataBean item, int flag) {
        if (flag == 0) {
            if (nums == 0) {
                GoodsDetailsActivity.startActivity(3, this, item.getId());
            } else if (nums == 1) {
                GoodsDetailsActivity.startActivity(4, this, item.getId());

            } else if (nums == 2) {
                GoodsDetailsActivity.startActivity(5, this, item.getId());

            } else if (nums == 3) {
                GoodsDetailsActivity.startActivity(6, this, item.getId());

            } else if (nums == 7) {
                GoodsDetailsActivity.startActivity(0, this, item.getId());

            }

        } else {
            GoodsSpecialDetailsActivity.startActivity(this, item.getId());
        }

    }
}

