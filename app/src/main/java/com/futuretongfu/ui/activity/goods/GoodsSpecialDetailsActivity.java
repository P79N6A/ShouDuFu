package com.futuretongfu.ui.activity.goods;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.RequestCode;
import com.futuretongfu.iview.IOnlineGoodsIndexView;
import com.futuretongfu.iview.IOnlineGoodsSpecialIndexView;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.entity.OnlineGoodsDetailsResult;
import com.futuretongfu.model.entity.OnlineGoodsSpecialDetailsResult;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.goods.GoodDetailsIndexPresenter;
import com.futuretongfu.presenter.goods.GoodDetailsSpecialIndexPresenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.activity.MessageAllActivity;
import com.futuretongfu.ui.activity.MyAttentionActivity;
import com.futuretongfu.ui.activity.user.LoginActivity;
import com.futuretongfu.utils.AppUtil;
import com.futuretongfu.utils.GlideImageLoader;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.ShareUtil;
import com.futuretongfu.utils.SmallFeatureUtils;
import com.futuretongfu.utils.Util;
import com.futuretongfu.view.SharePopupWindow;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhanggf on 2018/3/26.
 * 商品详情(车/房)
 */

public class GoodsSpecialDetailsActivity extends BaseActivity implements SharePopupWindow.OnItemClickLister, IOnlineGoodsSpecialIndexView, IOnlineGoodsIndexView {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.vp_pager)
    Banner bannerLayout;
    @Bind(R.id.bt_share)
    ImageView btShare;
    @Bind(R.id.bt_add)
    ImageView btAdd;
    @Bind(R.id.tv_goods_title)
    TextView tvGoodsTitle;
    @Bind(R.id.tv_goods_collect)
    TextView tvGoodsCollect;
    @Bind(R.id.tv_goods_price)
    TextView tvGoodsPrice;
    @Bind(R.id.tv_goods_desc)
    TextView tvGoodsDesc;
    @Bind(R.id.tv_goods_tell)
    TextView tvGoodsTell;
    @Bind(R.id.tv_goods_address)
    TextView tvoodsddress;
    @Bind(R.id.tv_goods_intro)
    TextView tv_goodsIntro;
    private SharePopupWindow sharePopup;
    private String id;
    private GoodDetailsSpecialIndexPresenter mPresenter;
    private GoodDetailsIndexPresenter indexPresenter;
    private int isFavorited;
    private String userId;
    private String ShareTitle,ShareImage,ShareText;
    private String TellPhone;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_special_details;
    }

    @Override
    protected Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        tvTitle.setText("商品详情");
        userId = UserManager.getInstance().getUserId() + "";
        id = getIntent().getStringExtra("id");
        indexPresenter = new GoodDetailsIndexPresenter(this, this);
        mPresenter = new GoodDetailsSpecialIndexPresenter(this, this);
        mPresenter.getGoodsSpecialDetailsList(userId, id);
        indexPresenter.getGoodsDetailsList(userId,id);
        btShare.setVisibility(View.VISIBLE);
        btAdd.setImageResource(R.mipmap.icon_more_shu);
        sharePopup = new SharePopupWindow(this);
        sharePopup.setOnItemClickListener(this);
        sharePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Util.setAlpha(GoodsSpecialDetailsActivity.this, 1.0f);
            }
        });


    }


    /**
     * Banner
     *
     * @param imgLst
     */
    private void initHomeHeader(final String[] imgLst) {
        final List<String> imgsLst = new ArrayList<>();
        for (int i = 0; i < imgLst.length; i++) {
            if (null != imgLst) {
                String imgUrl = imgLst[i];
                if (!TextUtils.isEmpty(imgUrl)) {
                    imgsLst.add(imgUrl);
                }
            }
        }
        int width = SmallFeatureUtils.getWindowWith();
        // 设置图片宽高
        RelativeLayout.LayoutParams layoutParams =
                new RelativeLayout.LayoutParams(width, width);
        bannerLayout.setLayoutParams(layoutParams);
        bannerLayout.setImages(imgsLst).setImageLoader(new GlideImageLoader()).start();
        bannerLayout.setIndicatorGravity(BannerConfig.CENTER);
        //设置间隔
        bannerLayout.setDelayTime(3000);
        //设置banner动画效果
        bannerLayout.setBannerAnimation(Transformer.DepthPage);
        bannerLayout.start();
    }


    public static void startActivity(Context context, String id) {
        Intent intent = new Intent(context, GoodsSpecialDetailsActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }


    /**
     * 显示菜单
     */
    private PopupWindow pop_menu;

    private void showMenu() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_goods_menu, null);
        pop_menu = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT, true);
        Util.setAlpha(getContext(), 0.7f);
        pop_menu.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Util.setAlpha(getContext(), 1.0f);
            }
        });
        pop_menu.setBackgroundDrawable(new ColorDrawable());
        pop_menu.showAsDropDown(btAdd, 0, 0);

        LinearLayout ll_message = (LinearLayout) view.findViewById(R.id.ll_dialog_menu_message);
        LinearLayout ll_home = (LinearLayout) view.findViewById(R.id.ll_dialog_menu_add_home);
        LinearLayout ll_search = (LinearLayout) view.findViewById(R.id.ll_dialog_menu_qr_search);
        LinearLayout ll_concern = (LinearLayout) view.findViewById(R.id.ll_dialog_menu_qr_concern);

        ll_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivity(getContext(), MessageAllActivity.class);
                pop_menu.dismiss();
            }
        });

        ll_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                pop_menu.dismiss();
            }
        });

        ll_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivity(getContext(), SearchActivity.class);
                pop_menu.dismiss();
            }
        });
        ll_concern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivity(getContext(), MyAttentionActivity.class);
                pop_menu.dismiss();
            }
        });
    }

    @Override
    public void setOnItemClick(View view) {
        switch (view.getId()) {
            case R.id.ll_wechat_moments:
                if(ShareUtil.isAppAvilible(context, Constants.PACKAGE_NAME_WX)){
                    ShareUtil.uMengShareUrl(ShareTitle,ShareText,ShareImage,id,GoodsSpecialDetailsActivity.this, SHARE_MEDIA.WEIXIN_CIRCLE);
                }else{
                    showToast("您尚未安装微信！");
                }
                break;

            case R.id.ll_qq:
                if(ShareUtil.isAppAvilible(context, Constants.PACKAGE_NAME_QQ)){
                    ShareUtil.uMengShareUrl(ShareTitle,ShareText,ShareImage,id,GoodsSpecialDetailsActivity.this, SHARE_MEDIA.QQ);
                }else{
                    showToast("您尚未安装QQ！");
                }
                break;

            case R.id.ll_sina:
                ShareUtil.uMengShareUrl(ShareTitle,ShareText,ShareImage,id,GoodsSpecialDetailsActivity.this,  SHARE_MEDIA.SINA);
                break;

            case R.id.ll_wechat:
                if(ShareUtil.isAppAvilible(context, Constants.PACKAGE_NAME_WX)){
                    ShareUtil.uMengShareUrl(ShareTitle,ShareText,ShareImage,id,GoodsSpecialDetailsActivity.this, SHARE_MEDIA.WEIXIN);
                }else{
                    showToast("您尚未安装微信！");
                }
                break;
        }
    }


    @OnClick({R.id.bt_back, R.id.bt_share, R.id.bt_add,R.id.tv_goods_collect,R.id.tv_goods_tell})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;
            case R.id.bt_share:
                sharePopup.showAtLocation(findViewById(R.id.layout), Gravity.BOTTOM, 0, 0);
                Util.setAlpha(this, 0.7f);
                break;
            case R.id.tv_goods_collect:
                if (!UserManager.getInstance().isLogin()) {
                    LoginActivity.startActivity(getContext(), RequestCode.REQUEST_CODE_LOGIN_NORMAL);
                    return;
                }
                //0为未关注，1为关注
                if (isFavorited == 0) {
                    mPresenter.onGoodsAddFavorite(userId, id, "sp");
                } else {
                    mPresenter.onnGoodsCancelFavorite(userId, id);
                }
                break;
            case R.id.bt_add:
                showMenu();
                break;
            case R.id.tv_goods_tell:
                AppUtil.openPhoneService(this, TellPhone);
                break;
            case R.id.tv_goods_address:
                String address = tvoodsddress.getText().toString().trim();
                if (isEmpty(address, "暂无地址")) {
                    return;
                }
                String allAddress = "baidumap://map/navi?query=" + address;
                String gaodeAddress = "androidamap://keywordNavi?sourceApplication=softname&keyword=" + address + "&style=2";
                if (!TextUtils.isEmpty(address)) {
                    if (ShareUtil.isAppAvilible(context, "com.baidu.BaiduMap")) {
                        Intent baidu1 = new Intent();
                        // 驾车导航
                        baidu1.setData(Uri.parse(allAddress));
                        startActivity(baidu1);
                    } else {
                        if (ShareUtil.isAppAvilible(context, "com.autonavi.minimap")) {
                            Intent baidu1 = new Intent();
                            // 驾车导航
                            baidu1.setData(Uri.parse(gaodeAddress));
                            startActivity(baidu1);
                        }
                    }
                }
                break;
        }
    }


    @Override
    public void onGoodsDetailsSuccess(OnlineGoodsDetailsResult result) {
        TellPhone = result.getTellPhone();
        tvGoodsTitle.setText(result.getSkuName());
        tvGoodsPrice.setText("¥"+result.getPrice());
        tvGoodsDesc.setText(result.getProductName());
        isFavorited = result.getIsLike();
        if (isFavorited == 0) {  //关注
            tvGoodsCollect.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.icon_attention), null, null);
        } else {
            tvGoodsCollect.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.icon_attention_pre), null, null);
        }
        String str[] = result.getSkuImages().split("\\|");
        ShareImage = str[0];
        ShareTitle = result.getProductName();
        ShareText = result.getSkuName();
        initHomeHeader(str);
    }

    @Override
    public void onGoodsDetailsFaile(String msg) {
        showToast(msg);

    }

    @Override
    public void onGoodsDetailsSpecialSuccess(OnlineGoodsSpecialDetailsResult result) {
        tv_goodsIntro.setMovementMethod(LinkMovementMethod.getInstance());//设置超链接可以打开网页
        tv_goodsIntro.setText(Html.fromHtml(result.getPackSpec(), imgGetter, null));
    }

    @Override
    public void onGoodsDetailsSpecialFaile(String msg) {
        showToast(msg);
    }

    @Override
    public void onCollectSuccess(int type, FuturePayApiResult result) {
        if (type == 1) {  //关注
            showToast("收藏" + result.getMsg());
            isFavorited = 1;
            tvGoodsCollect.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.icon_attention_pre), null, null);
        } else {
            showToast("取消收藏" + result.getMsg());
            isFavorited = 0;
            tvGoodsCollect.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.icon_attention), null, null);
        }
    }

    Html.ImageGetter imgGetter = new Html.ImageGetter() {
        public Drawable getDrawable(String source) {

            Drawable drawable = null;
            URL url;
            try {
                url = new URL(source);
                Drawable.createFromStream(url.openStream(), "");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());

            return drawable;
        }
    };

    @Override
    public void onCollectFail(String msg) {
        showToast(msg);
    }
}
