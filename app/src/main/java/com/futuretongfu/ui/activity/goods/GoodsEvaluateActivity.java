package com.futuretongfu.ui.activity.goods;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.bean.onlinegoods.AddEvaluateBaen;
import com.futuretongfu.bean.onlinegoods.OrderOnlineGoodsBean;
import com.futuretongfu.bean.onlinegoods.OrderOnlineGoodsEvaluateBean;
import com.futuretongfu.bean.onlinegoods.ShoppingGoodsBean;
import com.futuretongfu.iview.IGoodsRefundView;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.goods.GoodsEvaluatePresenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.adapter.EvaluateGoodsAdapter;
import com.futuretongfu.utils.CacheActivityUtil;
import com.futuretongfu.utils.NineImgViewUtil;
import com.futuretongfu.utils.PermissionUtil;
import com.futuretongfu.view.MyRatingBar;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;

/**
 * Created by zhanggf on 2018/6/4.
 * 商品评价
 */

public class GoodsEvaluateActivity extends BaseActivity implements IGoodsRefundView, NineImgViewUtil.NineImgViewUtilListener, EvaluateGoodsAdapter.MyAdapterListener {

    @Bind(R.id.bt_right)
    TextView btRight;
    @Bind(R.id.recycler_list)
    ListView recyclerList;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.rb_goodsevaluate_wuliu)
    MyRatingBar rbGoodsevaluateWuliu;
    @Bind(R.id.rb_goodsevaluate_evaluate)
    MyRatingBar rbGoodsevaluateEvaluate;
    private List<OrderOnlineGoodsBean> goodsList;
    private EvaluateGoodsAdapter evaluateGoodsAdapter;
    private GoodsEvaluatePresenter presenter;
    private NineImgViewUtil nineImgViewUtil;
    private int expressAttitude, serviceAttitude;
    private List<OrderOnlineGoodsEvaluateBean> listS;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_evaluate;
    }

    @Override
    protected Presenter getPresenter() {
        return presenter;
    }

    @Override

    protected void init(Bundle savedInstanceState) {
        tvTitle.setText("评价");
        btRight.setVisibility(View.VISIBLE);
        btRight.setText("发布");
        presenter = new GoodsEvaluatePresenter(this, this);
        goodsList = (List<OrderOnlineGoodsBean>) getIntent().getSerializableExtra("goodsList");
        listS = new ArrayList<>();
        OrderOnlineGoodsEvaluateBean bean;
        for (int i=0;i<goodsList.size();i++){
            bean = new OrderOnlineGoodsEvaluateBean();
            bean.setId(goodsList.get(i).getId());
            bean.setSkuId(goodsList.get(i).getSkuId());
            bean.setSkuImages(goodsList.get(i).getSkuImages());
            bean.setOnlineStoreId(goodsList.get(i).getOnlineStoreId());
            bean.setOnlineOrderId(goodsList.get(i).getOnlineOrderId());
            listS.add(bean);
        }
        evaluateGoodsAdapter = new EvaluateGoodsAdapter(this,this);
        evaluateGoodsAdapter.setList(listS);
        recyclerList.setAdapter(evaluateGoodsAdapter);
        initRatingBar();
    }

    private void initRatingBar() {
        rbGoodsevaluateWuliu.setStepSize(MyRatingBar.StepSize.Full);//设置每次点击增加一颗星还是半颗星
        rbGoodsevaluateWuliu.setOnRatingChangeListener(new MyRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float rating) {
                expressAttitude = (int) rating;
            }
        });
        rbGoodsevaluateEvaluate.setStepSize(MyRatingBar.StepSize.Full);//设置每次点击增加一颗星还是半颗星
        rbGoodsevaluateEvaluate.setOnRatingChangeListener(new MyRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float rating) {
                serviceAttitude = (int) rating;
            }
        });

    }

    @OnClick({R.id.bt_back, R.id.bt_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;
            case R.id.bt_right:
                HashSet<OrderOnlineGoodsEvaluateBean> hashSet = new HashSet<>(listS);
                ArrayList<OrderOnlineGoodsEvaluateBean> hh = new ArrayList<>(hashSet);
                for (int i = 0; i < hh.size(); i++) {
                    if (listS.get(i).getEvaluateInfo() == null) {
                        listS.get(i).setEvaluateInfo("未填写");
                    }
                }
                presenter.saveComment(listS,nineImgViewUtil.getPhotoUrls());
                break;
        }
    }

    @Override
    public void onGoodsRefundAddSuccess(FuturePayApiResult futurePayApiResult) {
        showToast("评价成功");
        finish();
    }

    @Override
    public void onGoodsRefundAddFaile(String msg) {
        showToast(msg);

    }

    @Override
    public void onOrderConsumerCommentPercentage(float percentage) {
        setProgressDialog2(percentage);

    }

    @OnClick(R.id.bt_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onNineImgViewOpenImageSelector() {
        if (PermissionUtil.permissionReadStorage(this)) {
            openImageSingleSelector();
        }
    }

    /**
     * 打开图片选择器
     */
    private void openImageSingleSelector() {
        PhotoPicker.builder()
                .setPhotoCount(nineImgViewUtil.getCanSelecCount())
                .setShowCamera(true)
                .setShowGif(false)
                .setPreviewEnabled(false)
                .start(this, PhotoPicker.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //图片选择器 结果
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                nineImgViewUtil.setImages(photos);
            }
        }
    }

    @Override
    public void onMyAdapterClick(RoundedImageView imgvPhoto1, RoundedImageView imgvPhoto2, RoundedImageView imgvPhoto3, ImageView imgvClose1, ImageView imgvClose2, ImageView imgvClose3) {
        nineImgViewUtil = new NineImgViewUtil(this, 3, true, R.mipmap.icon_comera, this);
        nineImgViewUtil.setImgaeView(imgvPhoto1, imgvPhoto2, imgvPhoto3)
                .setDeleteView(imgvClose1, imgvClose2, imgvClose3);
    }

    @Override
    public void onMyAdapter2Click(NineImgViewUtil nineImgViewUtil) {
        this.nineImgViewUtil = nineImgViewUtil;
    }
}
