package com.futuretongfu.ui.activity.goods;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.bean.onlinegoods.OrderOnlineGoodsBean;
import com.futuretongfu.iview.IClassDetailsView;
import com.futuretongfu.iview.IGoodsRefundView;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.goods.GoodsRefundPresenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.utils.CacheActivityUtil;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.NineImgViewUtil;
import com.futuretongfu.utils.PermissionUtil;
import com.futuretongfu.utils.StringUtil;
import com.futuretongfu.utils.Util;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;

import static com.futuretongfu.utils.Util.menuWindow;

/**
 * Created by zhanggf on 2018/6/2.
 * 申请退款
 */

public class GoodsRefundActivity extends BaseActivity implements NineImgViewUtil.NineImgViewUtilListener, IGoodsRefundView {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.img_good_image)
    ImageView imgGoodImage;
    @Bind(R.id.tv_good_name)
    TextView tvGoodName;
    @Bind(R.id.tv_good_format)
    TextView tvGoodFormat;
    @Bind(R.id.tv_good_price)
    TextView tvGoodPrice;
    @Bind(R.id.tv_good_num)
    TextView tvGoodNum;
    OrderOnlineGoodsBean entity = null;
    @Bind(R.id.tv_agoodsrefund_state)
    TextView tvAgoodsrefundState;
    @Bind(R.id.tv_agoodsrefund_reason)
    TextView tvAgoodsrefundReason;
    @Bind(R.id.tv_agoodsrefund_money)
    TextView tvAgoodsrefundMoney;
    @Bind(R.id.tv_agoodsrefund_desc)
    TextInputEditText tvAgoodsrefundDesc;
    @Bind(R.id.imgv_photo_1)
    RoundedImageView imgvPhoto1;
    @Bind(R.id.imgv_close_1)
    ImageView imgvClose1;
    @Bind(R.id.imgv_photo_2)
    RoundedImageView imgvPhoto2;
    @Bind(R.id.imgv_close_2)
    ImageView imgvClose2;
    @Bind(R.id.imgv_photo_3)
    RoundedImageView imgvPhoto3;
    @Bind(R.id.imgv_close_3)
    ImageView imgvClose3;
    @Bind(R.id.rl_agoodsrefund_state)
    RelativeLayout rl_agoodsrefund_state;
    private int type = 0;
    private String onlineOrderNo;
    private NineImgViewUtil nineImgViewUtil;
    private LayoutInflater inflater = null;
    private GoodsRefundPresenter mPresenter;
    private String goodsState, reason;
    private String skuId,remark;
    private String onlineOrderDetailId,onlineStoreId,onlineOrderId;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_refund;
    }

    @Override
    protected Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        tvTitle.setText("申请退款");
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        mPresenter = new GoodsRefundPresenter(this,this);
        entity = (OrderOnlineGoodsBean) getIntent().getSerializableExtra("entity");
        onlineOrderNo = getIntent().getStringExtra("onlineOrderNo");
        type = getIntent().getIntExtra("type", 0);
        CacheActivityUtil.addNewActivity(this);
        if (type==0){
            rl_agoodsrefund_state.setVisibility(View.GONE);
        }else {
            rl_agoodsrefund_state.setVisibility(View.VISIBLE);
        }
        if (entity != null) {
            skuId = entity.getSkuId();
            tvGoodName.setText(StringUtil.getSafeString(entity.getSkuName()));
            tvGoodNum.setText("*" + StringUtil.getSafeString(entity.getAmount() + ""));
            tvGoodFormat.setText(StringUtil.getSafeString(entity.getFormat()));
            tvGoodPrice.setText("￥" + StringUtil.fmtMicrometer(Double.parseDouble(StringUtil.getDouble2(entity.getPrice()))));
            GlideLoad.loadCrossFadeImageView2(entity.getSkuImages(), imgGoodImage);
            tvAgoodsrefundMoney.setText("￥"+entity.getPrice()*entity.getAmount());
            onlineOrderDetailId = entity.getId();
            onlineStoreId = entity.getOnlineStoreId();
            onlineOrderId = entity.getOnlineOrderId();
        }
        nineImgViewUtil = new NineImgViewUtil(this, 3, true, R.mipmap.icon_comera, this);
        nineImgViewUtil.setImgaeView(imgvPhoto1, imgvPhoto2, imgvPhoto3)
                .setDeleteView(imgvClose1, imgvClose2, imgvClose3);


    }


    @OnClick({R.id.bt_back, R.id.tv_agoodsrefund_state, R.id.tv_agoodsrefund_reason,R.id.tv_agoodsrefund_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;
            case R.id.tv_agoodsrefund_state:
                Util.showPopwindow(this,getRefundStatePick());
                break;
            case R.id.tv_agoodsrefund_reason:
                Util.showPopwindow(this,getRefundReasonPick());
                break;
            case R.id.tv_agoodsrefund_commit:
                if (TextUtils.isEmpty(reason)){
                    showToast("请选择退款原因");
                    return;
                }
                remark = tvAgoodsrefundDesc.getText().toString();

                mPresenter.saveComment(UserManager.getInstance().getUserId()+"",onlineOrderNo,goodsState,reason,type,nineImgViewUtil.getPhotoUrls(),
                        remark,skuId,onlineOrderDetailId,onlineStoreId,onlineOrderId);
                break;
        }
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

      /**
     * 退货状态
     * @return
     */
    private View getRefundStatePick() {
        final View view = inflater.inflate(R.layout.popul_goodsrefund_state, null);
        RadioGroup radio_group_state = (RadioGroup) view.findViewById(R.id.radio_group_state);
        final RadioButton cb1 = (RadioButton) view.findViewById(R.id.cb1);
        final RadioButton cb2 = (RadioButton) view.findViewById(R.id.cb2);
        view.findViewById(R.id.tv_popul_dissmin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuWindow.dismiss();
            }
        });
        radio_group_state.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int radioButtonId = group.getCheckedRadioButtonId();//获得按下单选框的ID,并保存在radioButtonId
                RadioButton rb = (RadioButton)findViewById(radioButtonId);//根据ID将RB和单选框绑定在一起
                if (checkedId == cb1.getId()) {
                    goodsState = cb1.getText().toString();
                } else if (checkedId == cb2.getId()) {
                    goodsState = cb2.getText().toString();
                }
                menuWindow.dismiss();
                tvAgoodsrefundState.setText(goodsState);
            }
        });
        View outView = view.findViewById(R.id.outView);
        outView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuWindow.dismiss();
            }
        });
        return view;
    }


    /**
     * 退货原因
     * @return
     */
    private View getRefundReasonPick() {
        final View view = inflater.inflate(R.layout.popul_goodsrefund_reason, null);
        RadioGroup radio_group_reason = (RadioGroup) view.findViewById(R.id.radio_group_reason);
        final RadioButton cb1 = (RadioButton) view.findViewById(R.id.cb1);
        final RadioButton cb2 = (RadioButton) view.findViewById(R.id.cb2);
        final RadioButton cb3 = (RadioButton) view.findViewById(R.id.cb3);
        final RadioButton cb4 = (RadioButton) view.findViewById(R.id.cb4);
        final RadioButton cb5 = (RadioButton) view.findViewById(R.id.cb5);
        view.findViewById(R.id.tv_popul_dissmin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuWindow.dismiss();
            }
        });
        radio_group_reason.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //判断选中的id。
                if (checkedId == cb1.getId()) {
                    reason = cb1.getText().toString();
                } else if (checkedId == cb2.getId()) {
                    reason = cb2.getText().toString();
                }else if (checkedId == cb3.getId()) {
                    reason = cb3.getText().toString();
                }else if (checkedId == cb4.getId()) {
                    reason = cb4.getText().toString();
                }else if (checkedId == cb5.getId()) {
                    reason = cb5.getText().toString();
                }
                menuWindow.dismiss();
                tvAgoodsrefundReason.setText(reason);
            }
        });

        View outView = view.findViewById(R.id.outView);
        outView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuWindow.dismiss();
            }
        });
        return view;
    }


    @Override
    public void onGoodsRefundAddSuccess(FuturePayApiResult futurePayApiResult) {
        showToast("申请成功");
        CacheActivityUtil.newFinishActivity();
    }

    @Override
    public void onGoodsRefundAddFaile(String msg) {
        showToast(msg);

    }

    @Override
    public void onOrderConsumerCommentPercentage(float percentage) {
        setProgressDialog2(percentage);

    }
}
