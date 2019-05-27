package com.futuretongfu.ui.fragment.goods;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.bean.onlinegoods.GoodsAttrValuesBean;
import com.futuretongfu.bean.onlinegoods.GoodsAttrValuesList;
import com.futuretongfu.bean.onlinegoods.ShoppingGoodsBean;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.RequestCode;
import com.futuretongfu.iview.IOnlineGoodsView;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.entity.OnlineGoodsDetailsResult;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.goods.GoodDetailsPresenter;
import com.futuretongfu.ui.activity.goods.FirmOrderActivity;
import com.futuretongfu.ui.activity.goods.StoreIndexActivity;
import com.futuretongfu.ui.activity.user.LoginActivity;
import com.futuretongfu.ui.adapter.GoodAttrExpandableAdapter;
import com.futuretongfu.ui.fragment.BaseFragment;
import com.futuretongfu.utils.AppUtil;
import com.futuretongfu.utils.GlideImageLoader;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.SmallFeatureUtils;
import com.futuretongfu.view.MyExpandableListView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by zhanggf on 2018/3/26.
 * 商品详情
 */

@SuppressLint("ValidFragment")
public class GoodsDescFragment extends BaseFragment implements IOnlineGoodsView, GoodAttrExpandableAdapter.MyItemListener {

    @Bind(R.id.vp_pager)
    Banner bannerLayout;
    @Bind(R.id.tv_fgoodsdesc_name)
    TextView tvFgoodsdescName;
    @Bind(R.id.tv_fgoodsdesc_collect)
    TextView tvFgoodsdescCollect;
    @Bind(R.id.tv_fgoodsdesc_price)
    TextView tvFgoodsdescPrice;
    @Bind(R.id.tv_fgoodsdesc_sales)
    TextView tvFgoodsdescSales;
    @Bind(R.id.tv_goods_weight)
    TextView tvFgoodsdescWeight;
    @Bind(R.id.tv_fgoodsdesc_choose)
    TextView tvFgoodsdescChoose;
    @Bind(R.id.img_fgoodsdesc_more)
    ImageView imgFgoodsdescMore;
    @Bind(R.id.tv_fgoodsdesc_tongbei)
    TextView tv_fgoodsdesc_tongbei;
    @Bind(R.id.tv_isReturn)
    TextView tvIsReturn;
    private GoodDetailsPresenter presenter;
    private String goodId, userId;
    private int isFavorited;
    OnlineGoodsDetailsResult dataResult;
    String format = "";
    List<OnlineGoodsDetailsResult> attrInfoList = new ArrayList<>();


    public void setId(String id) {
        this.goodId = id;
    }

    @Override
    protected Presenter getPresenter() {
        return presenter;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_goods_desc;
    }

    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenter = new GoodDetailsPresenter(getActivity(), this);
        userId = UserManager.getInstance().getUserId() + "";
        if (!TextUtils.isEmpty(goodId))
            presenter.getGoodsDetailsList(userId, goodId);
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
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(width, width);
        bannerLayout.setLayoutParams(layoutParams);
        bannerLayout.setImages(imgsLst).setImageLoader(new GlideImageLoader()).start();
        bannerLayout.setIndicatorGravity(BannerConfig.CENTER);
        //设置间隔
        bannerLayout.setDelayTime(3000);
        //设置banner动画效果
        bannerLayout.setBannerAnimation(Transformer.DepthPage);
        bannerLayout.start();
    }


    /**
     * 1 操纵类型
     *
     * @param type
     */
    public void isClickType(int type) {
        if (dataResult == null)
            return;
        switch (type) {
            case 1:
                AppUtil.openPhoneService(getActivity(), dataResult.getTellPhone());
                break;
            case 2:
                IntentUtil.startActivity(this, StoreIndexActivity.class
                        , "id", dataResult.getStoreId());
                break;
            case 3:  //添加购物车
                if (dataResult.getStockAmount() >= goodsNum) {
                    presenter.getGoodsAttrInfoList(type, dataResult.getSpuId(), dataResult.getStoreId());
                } else {
                    showToast("库存不足,请选择其他属性");
                }
                break;
            case 4:  //购买
                if (dataResult.getStockAmount() >= goodsNum) {
                    presenter.getGoodsAttrInfoList(type, dataResult.getSpuId(), dataResult.getStoreId());
                } else {
                    showToast("库存不足,请选择其他属性");
                }
                break;
            case 5:
                if (dataResult.getStockAmount() >= goodsNum) {
                    onBuyGoodover();
                } else {
                    showToast("库存不足,请选择其他属性");
                }
              break;
            default:
                break;

        }
    }



    /**
     * 1 操纵类型
     *
     * @param type
     */
    public void isClicISM(int type) {
        if (dataResult == null)
            return;
        switch (type) {
            case 1:
                if (dataResult.getStockAmount() >= goodsNum) {
                   // onBuyGoodover1("hehe1");
                    presenter.getGoodsAttrInfoList(type, dataResult.getSpuId(), dataResult.getStoreId());

                } else {
                    showToast("库存不足,请选择其他属性");
                }
                break;
            case 2:
                if (dataResult.getStockAmount() >= goodsNum) {
                   // onBuyGoodover1("hehe2");
                    presenter.getGoodsAttrInfoList(type, dataResult.getSpuId(), dataResult.getStoreId());

                } else {
                    showToast("库存不足,请选择其他属性");
                }
                break;
            case 6:  //添加购物车
                if (dataResult.getStockAmount() >= goodsNum) {
                  //  onBuyGoodover1("hehe3");
                    presenter.getGoodsAttrInfoList(type, dataResult.getSpuId(), dataResult.getStoreId());

                } else {
                    showToast("库存不足,请选择其他属性");
                }
                break;
            case 7:  //购买
                if (dataResult.getStockAmount() >= goodsNum) {
                  //  onBuyGoodover1("hehe4");
                    presenter.getGoodsAttrInfoList(type, dataResult.getSpuId(), dataResult.getStoreId());

                } else {
                    showToast("库存不足,请选择其他属性");
                }
                break;
            default:
                break;

        }
    }

    //直接购买
    private void onBuyGoods() {
        List<ShoppingGoodsBean> goodsList = new ArrayList();
        ShoppingGoodsBean bean = new ShoppingGoodsBean();
        bean.setNum(goodsNum);

        bean.setPrice(dataResult.getPrice());
        bean.setSkuImages(dataResult.getSkuImages());
        bean.setSkuName(dataResult.getSkuName());
        bean.setProductId(dataResult.getId());
        bean.setSendTongBei(dataResult.getSendTongbei());
        bean.setFormat(tvFgoodsdescChoose.getText().toString());
        goodsList.add(bean);
        Intent intent = new Intent(getActivity(), FirmOrderActivity.class);
        intent.putExtra("totalMoney", dataResult.getPrice() * goodsNum);
        intent.putExtra("goodsList", (Serializable) goodsList);
        intent.putExtra("onlineStoreId", dataResult.getStoreId());
        intent.putExtra("goodsAllTongbei", dataResult.getSendTongbei());
        intent.putExtra("skuId", dataResult.getSpuId());
        intent.putExtra("price", dataResult.getPrice() + "");
        intent.putExtra("hehe", "hhhh");
        int nums = bean.getNum();
        intent.putExtra("amount", nums + "");
        Log.e("nums", nums + "");
        intent.putExtra("format", dataResult.getSkuName());
        Log.e("tongbeis", dataResult.getSendTongbei() + "");
        startActivity(intent);
    }


    //直接购买2
    private void onBuyGoodover() {
        List<ShoppingGoodsBean> goodsList = new ArrayList();
        ShoppingGoodsBean bean = new ShoppingGoodsBean();
        bean.setNum(goodsNum);

        bean.setPrice(dataResult.getPrice());
        bean.setSkuImages(dataResult.getSkuImages());
        bean.setSkuName(dataResult.getSkuName());
        bean.setProductId(dataResult.getId());
        bean.setSendTongBei(dataResult.getSendTongbei());
        bean.setFormat(tvFgoodsdescChoose.getText().toString());
        goodsList.add(bean);
        Intent intent = new Intent(getActivity(), FirmOrderActivity.class);
        intent.putExtra("totalMoney", dataResult.getPrice() * goodsNum);
        intent.putExtra("goodsList", (Serializable) goodsList);
        intent.putExtra("onlineStoreId", dataResult.getStoreId());
        intent.putExtra("goodsAllTongbei", dataResult.getSendTongbei());
        intent.putExtra("skuId", dataResult.getSpuId());
        intent.putExtra("price", dataResult.getPrice() + "");
        intent.putExtra("hehe", "hehe");
        int nums = bean.getNum();
        intent.putExtra("amount", nums + "");
        Log.e("nums", nums + "");
        intent.putExtra("format", dataResult.getSkuName());
        Log.e("tongbeis", dataResult.getSendTongbei() + "");
        startActivity(intent);
    }

    //直接购买2
    private void onBuyGoodover1(String ism) {
        List<ShoppingGoodsBean> goodsList = new ArrayList();
        ShoppingGoodsBean bean = new ShoppingGoodsBean();
        bean.setNum(goodsNum);

        bean.setPrice(dataResult.getPrice());
        bean.setSkuImages(dataResult.getSkuImages());
        bean.setSkuName(dataResult.getSkuName());
        bean.setProductId(dataResult.getId());
        bean.setSendTongBei(dataResult.getSendTongbei());
        bean.setFormat(tvFgoodsdescChoose.getText().toString());
        goodsList.add(bean);
        Intent intent = new Intent(getActivity(), FirmOrderActivity.class);
        intent.putExtra("totalMoney", dataResult.getPrice() * goodsNum);
        intent.putExtra("goodsList", (Serializable) goodsList);
        intent.putExtra("onlineStoreId", dataResult.getStoreId());
        intent.putExtra("goodsAllTongbei", dataResult.getSendTongbei());
        intent.putExtra("skuId", dataResult.getId());
        intent.putExtra("price", dataResult.getPrice() + "");
        intent.putExtra("hehe", ism);
        int nums = bean.getNum();
        intent.putExtra("amount", nums + "");
        Log.e("nums", nums + "");
        intent.putExtra("format", dataResult.getSkuName());
        Log.e("tongbeis", dataResult.getSendTongbei() + "");
        startActivity(intent);
    }


    @OnClick({R.id.tv_fgoodsdesc_collect, R.id.img_fgoodsdesc_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_fgoodsdesc_collect:
                if (!UserManager.getInstance().isLogin()) {
                    LoginActivity.startActivity(getContext(), RequestCode.REQUEST_CODE_LOGIN_NORMAL);
                    return;
                }
                //0为未关注，1为关注
                if (isFavorited == 0) {
                    presenter.onGoodsAddFavorite(userId, goodId, "sp");
                } else {
                    presenter.onnGoodsCancelFavorite(userId, goodId);
                }
                break;
            case R.id.img_fgoodsdesc_more:
                presenter.getGoodsAttrInfoList(2, dataResult.getSpuId(), dataResult.getStoreId());
                break;
        }
    }

    /**
     * 商品属性
     *
     * @param attrValuesResult
     */
    private PopupWindow window;

    private void settingColorAndCount(int type, List<GoodsAttrValuesList> attrValuesResult) {
        window = new PopupWindow(getActivity());
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setOutsideTouchable(true);
        window.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        window.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setFocusable(true);
        View view = View.inflate(getActivity(), R.layout.layout_popul_add_shopping, null);
        window.setContentView(view);
        if (attrValuesResult != null) {
            initAttrValuesAdapter(view, type, attrValuesResult);
        }
        WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
        params.alpha = 0.6f;
        getActivity().getWindow().setAttributes(params);
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();
                params.alpha = 1f;
                getActivity().getWindow().setAttributes(params);
            }
        });
        window.setAnimationStyle(R.style.Anim_Pop_TopOrBotom);
        window.showAtLocation(bannerLayout, Gravity.BOTTOM, 0, 0);
    }

    private TextView tvReduce, tvNum, tvGoodsSerialNumber, tv_stockamount, tvGoodsSerialPrice;
    private ImageView tvGoodsSerialImage;
    private int goodsNum = 1;
    private MyExpandableListView elv_goods_attr;

    private void initAttrValuesAdapter(View view, final int type, final List<GoodsAttrValuesList> attrValuesResult) {
        elv_goods_attr = (MyExpandableListView) view.findViewById(R.id.elv_goods_attr);
        tvGoodsSerialImage = (ImageView) view.findViewById(R.id.iv_head_portrait);
        tvGoodsSerialPrice = (TextView) view.findViewById(R.id.tv_price);
        tvGoodsSerialNumber = (TextView) view.findViewById(R.id.tv_goods_serial_number);
        tv_stockamount = (TextView) view.findViewById(R.id.tv_stockamount);
        final TextView tv_choose_commit = (TextView) view.findViewById(R.id.tv_goods_pou_commit);
        tvReduce = (TextView) view.findViewById(R.id.tv_reduce);
        tvNum = (TextView) view.findViewById(R.id.tv_num);
        TextView tvAdd = (TextView) view.findViewById(R.id.tv_add);
        setExpandableListView(attrValuesResult, elv_goods_attr);
        String str[] = dataResult.getSkuImages().split("\\|");
        GlideLoad.loadImage(str[0], tvGoodsSerialImage);
        tvGoodsSerialNumber.setText("商品编号：" + dataResult.getId());
        tv_stockamount.setText("库存：" + dataResult.getStockAmount());
        tvGoodsSerialPrice.setText("¥" + dataResult.getPrice());
        tvNum.setText(goodsNum + "");
        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //数量加
                goodsNum++;
                tvNum.setText(goodsNum + "");
            }
        });
        tvReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //数量减
                if (goodsNum <= 1) {
                    tvReduce.setTextColor(getActivity().getResources().getColor(R.color.text_color_gray));
                    return;
                } else {
                    goodsNum--;
                    tvNum.setText(goodsNum + "");
                }
            }
        });
        tv_choose_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == 3) {
                    if (attrInfoList.size() > 1) {
                        showToast("请完善商品属性");
                    } else {
                        presenter.addOnlineShopping(UserManager.getInstance().getUserId() + "",dataResult.getId(), goodsNum, format, dataResult.getStoreId(),dataResult.getPrice()+"");
                        window.dismiss();
                    }
                } else if (type == 4){
                    if (attrInfoList.size() > 1) {
                        showToast("请完善商品属性");
                    } else {
                        onBuyGoods();
                        window.dismiss();

                    }
                }else if (type==1){
                    if (attrInfoList.size()>1){
                        showToast("请完善商品属性");
                    }else {
                        onBuyGoodover1("hehe1");
                        window.dismiss();

                    }
                }
                else if (type==2){
                    if (attrInfoList.size()>1){
                        showToast("请完善商品属性");
                    }else {
                        onBuyGoodover1("hehe2");
                        window.dismiss();

                    }
                }
                else if (type==6){
                    if (attrInfoList.size()>1){
                        showToast("请完善商品属性");
                    }else {
                        onBuyGoodover1("hehe3");
                        window.dismiss();

                    }
                }
                else if (type==7){
                    if (attrInfoList.size()>1){
                        showToast("请完善商品属性");
                    }else {
                        onBuyGoodover1("hehe4");
                        window.dismiss();

                    }
                }
            }
        });
    }


    /**
     * 设置商品属性数据
     *
     * @param dataGroup
     */
    private List<GoodsAttrValuesList> groups = new ArrayList<>();
    private Map<String, List<GoodsAttrValuesBean>> children = new HashMap<>();
    private GoodAttrExpandableAdapter goodAttrExpandableAdapter;

    private void setExpandableListView(final List<GoodsAttrValuesList> dataGroup, MyExpandableListView elv_goods_attr) {
        groups.clear();
        children.clear();
        for (int i = 0; i < dataGroup.size(); i++) {
            groups.add(new GoodsAttrValuesList(dataGroup.get(i).getAttributeId(), dataGroup.get(i).getAttributeName()));
            List<GoodsAttrValuesBean> child = new ArrayList<>();
            for (int j = 0; j < dataGroup.get(i).getAttributeValue().size(); j++) {
                child.add(new GoodsAttrValuesBean(dataGroup.get(i).getAttributeValue().get(j).getAttrValueId(),
                        dataGroup.get(i).getAttributeValue().get(j).getAttrValue()));
            }
            children.put(groups.get(i).getAttributeId(), child);
        }
        goodAttrExpandableAdapter = new GoodAttrExpandableAdapter(getActivity(), this, groups, children);
        goodAttrExpandableAdapter.setFormat(format);
        elv_goods_attr.setAdapter(goodAttrExpandableAdapter);   //设置它的adapter
        goodAttrExpandableAdapter.notifyDataSetChanged();
        for (int i = 0; i < goodAttrExpandableAdapter.getGroupCount(); i++) {
            elv_goods_attr.expandGroup(i);
        }
        elv_goods_attr.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // TODO Auto-generated method stub
                //设置点击不关闭（不收回）
                v.setClickable(true);
                return true;
            }
        });
    }

    /***********************************************************************************************************************/
    @Override
    public void onGoodsDetailsSuccess(OnlineGoodsDetailsResult data) {
        dataResult = data;
        tvFgoodsdescName.setText(data.getSkuName());
        tvFgoodsdescPrice.setText("¥" + data.getPrice());
        tvFgoodsdescSales.setText("月销" + data.getSales() + "件");
        tv_fgoodsdesc_tongbei.setText("赠送通贝" + data.getSendTongbei());
        isFavorited = data.getIsLike();
        if (isFavorited == 0) {  //关注
            tvFgoodsdescCollect.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.icon_goods_uncollect), null, null);
        } else {
            tvFgoodsdescCollect.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.icon_good_collect), null, null);
        }
        if (!TextUtils.isEmpty(data.getIsReturn()) && data.getIsReturn().equals("1")) {
            tvIsReturn.setText("支持七天无理由退换货");
        } else {
            tvIsReturn.setText("不支持七天无理由退换货");
        }
        String str[] = data.getSkuImages().split("\\|");
        initHomeHeader(str);
        setGoodAttrValue(data.getAttrValues());
    }

    /**
     * 色湖之选择属性
     *
     * @param attrValues
     */
    private void setGoodAttrValue(List<GoodsAttrValuesBean> attrValues) {
        if (attrValues != null && attrValues.size() > 0) {
            for (int i = 0; i < attrValues.size(); i++) {
                GoodsAttrValuesBean bean = attrValues.get(i);
                if (i == 0) {
                    format = bean.getAttrValue();
                } else {
                    format = format + "," + bean.getAttrValue();
                }
            }
        } else {
            format = "无属性";
        }
        tvFgoodsdescChoose.setText("规格：" + format);
    }


    @Override
    public void onGoodsDetailsFaile(String msg) {
        showToast(msg);
    }

    @Override
    public void onCollectFail(String msg) {
        showToast(msg);
    }

    //商品属性
    @Override
    public void onGoodsAtterInfoSuccess(int type, List<GoodsAttrValuesList> result) {
        settingColorAndCount(type, result);
    }

    @Override
    public void onGoodsAtterInfoFaile(String msg) {
        showToast(msg);
    }


    @Override
    public void onOnlineShoppingAddSuccess(FuturePayApiResult futurePayApiResult) {
        window.dismiss();
        showToast("添加购物车成功");
        Log.e("加入购物车成功",futurePayApiResult.getMsg()+"");
        //TODO  刷新购物车数量
        Intent intent = new Intent();
        intent.setAction(Constants.REQUEST_CODE_SHOPCARTNUM);
        getActivity().sendBroadcast(intent);
    }


    @Override
    public void onOnlineShoppingAddFaile(String msg) {
        showToast(msg);
    }


    @Override
    public void onCollectSuccess(int type, FuturePayApiResult result) {
        if (type == 1) {  //关注
            showToast("收藏" + result.getMsg());
            isFavorited = 1;
            tvFgoodsdescCollect.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.icon_good_collect), null, null);
        } else {
            showToast("取消收藏" + result.getMsg());
            isFavorited = 0;
            tvFgoodsdescCollect.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.icon_goods_uncollect), null, null);
        }
    }


    @Override
    public void onitemAdapterClick(String attributeId, GoodsAttrValuesBean item, String formatInfo) {
        //根据选择属性查找sku信息接口
        tvFgoodsdescChoose.setText("已选择：" + formatInfo);
        presenter.getGoodsAttrSearchGoodsList(formatInfo, dataResult.getSpuId(), dataResult.getStoreId(), attributeId, item.getAttrValueId());
    }

    @Override
    public void onGoodsAtterInfoSearchSuccess(String formatInfo, List<OnlineGoodsDetailsResult> result) {
        attrInfoList = result;
        if (result != null && result.size() > 0) {
            dataResult = result.get(0);
            String str[] = dataResult.getSkuImages().split("\\|");
            GlideLoad.loadImage(str[0], tvGoodsSerialImage);
            tvGoodsSerialNumber.setText("商品编号：" + dataResult.getId());
            tvGoodsSerialPrice.setText("¥" + dataResult.getPrice());
            tv_stockamount.setText("库存：" + dataResult.getStockAmount());
            setGoodAttrValue(dataResult.getAttrValues());
            if (!format.equals("无属性")) {
                goodAttrExpandableAdapter.setFormat(format);
            } else {
                goodAttrExpandableAdapter.setFormat(formatInfo);
            }
            goodAttrExpandableAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onGoodsAtterInfoSearchFaile(String msg) {
        showToast(msg);
    }


}
