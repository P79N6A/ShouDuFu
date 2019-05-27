package com.futuretongfu.ui.activity.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.InputFilter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.NineImgViewUtil;
import com.futuretongfu.utils.PermissionUtil;
import com.futuretongfu.R;
import com.futuretongfu.iview.IOrderConsumerCommentView;
import com.futuretongfu.presenter.order.OrderConsumerCommentPresenter;
import com.futuretongfu.ui.component.ChineseLengthFilter;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;

/**
 * 消费订单评论
 *
 * @author ChenXiaoPeng
 */
public class OrderConsumerCommentActivity extends BaseActivity implements
        NineImgViewUtil.NineImgViewUtilListener
        , IOrderConsumerCommentView {

    private final static String Intent_Extra_StoreId = "storeId";
    private final static String Intent_Extra_OrderNo = "orderNo";
    private final static String Intent_Extra_Url = "url";

    @Bind(R.id.tv_title)
    public TextView textTitle;
    @Bind(R.id.bt_right)
    public TextView textSubmit;
    @Bind(R.id.text_share)
    public TextInputEditText editShare;
    @Bind(R.id.imgv_pic)
    public ImageView imgvPic;

    @Bind(R.id.imgv_start_1)
    public ImageView imgvStart1;
    @Bind(R.id.imgv_start_2)
    public ImageView imgvStart2;
    @Bind(R.id.imgv_start_3)
    public ImageView imgvStart3;
    @Bind(R.id.imgv_start_4)
    public ImageView imgvStart4;
    @Bind(R.id.imgv_start_5)
    public ImageView imgvStart5;

    @Bind(R.id.imgv_photo_1)
    public RoundedImageView imgvPhoto1;
    @Bind(R.id.imgv_photo_2)
    public RoundedImageView imgvPhoto2;
    @Bind(R.id.imgv_photo_3)
    public RoundedImageView imgvPhoto3;

    @Bind(R.id.imgv_close_1)
    public ImageView imgvClose1;
    @Bind(R.id.imgv_close_2)
    public ImageView imgvClose2;
    @Bind(R.id.imgv_close_3)
    public ImageView imgvClose3;

    /**
     * 点赞数量
     */
    private int startCount = 0;
    private NineImgViewUtil nineImgViewUtil;

    private long storeId;
    private long orderNo;
    private String url;
    private ChineseLengthFilter chineseLengthFilter;
    private OrderConsumerCommentPresenter orderConsumerCommentPresenter;

    /***********************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_consumer_comment;
    }

    @Override
    protected Presenter getPresenter() {
        return orderConsumerCommentPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        textTitle.setText("订单评论");
        textSubmit.setVisibility(View.VISIBLE);
        textSubmit.setText("提交");

        chineseLengthFilter = new ChineseLengthFilter(240);
        editShare.setFilters(new InputFilter[]{chineseLengthFilter});

        nineImgViewUtil = new NineImgViewUtil(this, 3, true, R.mipmap.icon_comera, this);
        nineImgViewUtil.setImgaeView(imgvPhoto1, imgvPhoto2, imgvPhoto3)
                .setDeleteView(imgvClose1, imgvClose2, imgvClose3);

        orderConsumerCommentPresenter = new OrderConsumerCommentPresenter(this, this);

        Intent intent = getIntent();
        storeId = intent.getLongExtra(Intent_Extra_StoreId, 0);
        orderNo = intent.getLongExtra(Intent_Extra_OrderNo, 0);
        url = intent.getStringExtra(Intent_Extra_Url);

        GlideLoad.loadCrossFadeImageView2(url, imgvPic);
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
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (PermissionUtil.isReadStoragePermission(permissions)) {
            if (PermissionUtil.getPermissionReadStorageResult(permissions, grantResults)) {
                openImageSingleSelector();
            } else {
                showToast(R.string.err_permission_read_photo);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    /***********************************************************************/

    /***********************************************************************/
    @OnClick(R.id.bt_back)
    public void onClickBack() {
        finish();
    }

    @OnClick(R.id.bt_right)
    public void onClickComfirt() {
        if (0 == startCount) {
            showToast("请先评分");
            return;
        }

        showProgressDialog();
        orderConsumerCommentPresenter.saveComment(
                storeId
                , orderNo
                , startCount, nineImgViewUtil.getPhotoUrls(), editShare.getText().toString()
        );
    }

    @OnClick({R.id.imgv_start_1, R.id.imgv_start_2, R.id.imgv_start_3, R.id.imgv_start_4, R.id.imgv_start_5})
    public void onOnclickImgvStart(View view) {
        switch (view.getId()) {
            case R.id.imgv_start_1:
                if (imgvStart1.isSelected() && 1 == startCount)
                    updateStart(0);
                else
                    updateStart(1);
                break;

            case R.id.imgv_start_2:
                updateStart(2);
                break;

            case R.id.imgv_start_3:
                updateStart(3);
                break;

            case R.id.imgv_start_4:
                updateStart(4);
                break;

            case R.id.imgv_start_5:
                updateStart(5);
                break;

        }
    }

    /***********************************************************************/
    @Override
    public void onNineImgViewOpenImageSelector() {
        if (PermissionUtil.permissionReadStorage(this)) {
            openImageSingleSelector();
        }
    }

    /**
     * 更新点赞星星
     */
    private void updateStart(int startCount) {
        this.startCount = startCount;

        switch (startCount) {
            case 0:
                imgvStart1.setSelected(false);
                imgvStart2.setSelected(false);
                imgvStart3.setSelected(false);
                imgvStart4.setSelected(false);
                imgvStart5.setSelected(false);
                break;
            case 1:
                imgvStart1.setSelected(true);
                imgvStart2.setSelected(false);
                imgvStart3.setSelected(false);
                imgvStart4.setSelected(false);
                imgvStart5.setSelected(false);
                break;
            case 2:
                imgvStart1.setSelected(true);
                imgvStart2.setSelected(true);
                imgvStart3.setSelected(false);
                imgvStart4.setSelected(false);
                imgvStart5.setSelected(false);
                break;
            case 3:
                imgvStart1.setSelected(true);
                imgvStart2.setSelected(true);
                imgvStart3.setSelected(true);
                imgvStart4.setSelected(false);
                imgvStart5.setSelected(false);
                break;
            case 4:
                imgvStart1.setSelected(true);
                imgvStart2.setSelected(true);
                imgvStart3.setSelected(true);
                imgvStart4.setSelected(true);
                imgvStart5.setSelected(false);
                break;
            case 5:
                imgvStart1.setSelected(true);
                imgvStart2.setSelected(true);
                imgvStart3.setSelected(true);
                imgvStart4.setSelected(true);
                imgvStart5.setSelected(true);
                break;
        }
    }

    /***********************************************************/
    @Override
    public void onOrderConsumerCommentSuccess() {
        hideProgressDialog();
        showToast("评论成功");
        delayFinish(200);
    }

    @Override
    public void onOrderConsumerCommentFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
    }

    @Override
    public void onOrderConsumerCommentPercentage(float percentage) {
        //setProgressDialog2(percentage);
    }
    /***********************************************************/

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

    public static void startActivity(Context context, long storeId, long orderNo, String url) {
        Intent intent = new Intent(context, OrderConsumerCommentActivity.class);
        intent.putExtra(Intent_Extra_StoreId, storeId);
        intent.putExtra(Intent_Extra_OrderNo, orderNo);
        intent.putExtra(Intent_Extra_Url, url);
        context.startActivity(intent);
    }
}
