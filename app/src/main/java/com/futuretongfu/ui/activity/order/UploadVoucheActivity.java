package com.futuretongfu.ui.activity.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.bean.SystemConfigBean;
import com.futuretongfu.iview.IUploadVoucheView;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.order.UploadVouchePresenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.PermissionUtil;
import com.futuretongfu.utils.PriceInputFilter;
import com.futuretongfu.view.CashierInputFilter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;

/**
 * 消费凭证
 */
public class UploadVoucheActivity extends BaseActivity implements IUploadVoucheView {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.et_consumption_price)
    EditText etConsumptionPrice;
    @Bind(R.id.tv_consumption_score)
    TextView tvConsumptionScore;
    @Bind(R.id.tv_consumption_expence)
    TextView tvConsumptionExpence;
    @Bind(R.id.img_consumption_show)
    ImageView imgConsumptionShow;
    @Bind(R.id.img_consumption_show2)
    ImageView imgConsumptionShow2;

    private UploadVouchePresenter uploadVouchePresenter;
    private int type = 0;  //1 消费凭证  2汇款凭证
    private String XiaofeiUrl = "";  //消费凭证
//    private String HuikuanUrl = "";   //2汇款凭证
    private double ratioFee,jifen,money;
    private String orderNo;
    private int fieldValue;
    private String id;
    private boolean isOpereteType;
    private int xiaofeiType;	//消费增值类型（0：与首都富平台无关的消费类型；1：与首都富平台有关的消费类型）

    /************************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_upload_vouche;
    }

    @Override
    protected Presenter getPresenter() {
        return uploadVouchePresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        tvTitle.setText("消费增值");
        uploadVouchePresenter = new UploadVouchePresenter(this, this);
        uploadVouchePresenter.systemConfig();
        initView();
    }

    private void initData() {
        isOpereteType = getIntent().getBooleanExtra("isOpereteType",false);
        xiaofeiType = getIntent().getIntExtra("xiaofeiType",0);
        orderNo = getIntent().getStringExtra("orderNo");


        money = getIntent().getDoubleExtra("money",0.0);
        jifen = money;
        ratioFee = Double.parseDouble(new DecimalFormat("#.00").format(money*fieldValue/100));
        id = getIntent().getStringExtra("id");
        if (money>0){
            etConsumptionPrice.setText(money+"");
            etConsumptionPrice.clearFocus();

            etConsumptionPrice.setCursorVisible(false);
            etConsumptionPrice.setFocusable(false);
            etConsumptionPrice.setFocusableInTouchMode(false);
        }
        tvConsumptionScore.setText("可获得通贝:"+(jifen<=0?"0":jifen));
        tvConsumptionExpence.setText("通贝服务费:¥"+(ratioFee<=0?"0":ratioFee));
    }

    private void initView() {
//        etConsumptionPrice.setInputType(InputType.TYPE_CLASS_NUMBER);
        etConsumptionPrice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        InputFilter[] filters={new PriceInputFilter(9),new InputFilter.LengthFilter(9)};
        etConsumptionPrice.setFilters(filters);
        etConsumptionPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                int len = s.toString().length();
                if (len == 1 && text.equals("0")) {
                    s.clear();
                }
                if (!TextUtils.isEmpty(s)){
                    double d = Double.parseDouble(s.toString());
                    jifen = d;
                    ratioFee = Double.parseDouble(new DecimalFormat("#.00").format(d*fieldValue/100));
                    tvConsumptionScore.setText("可获得通贝:"+jifen);
                    tvConsumptionExpence.setText("通贝服务费:¥"+ratioFee);
                }else {
                    tvConsumptionScore.setText("可获得通贝:"+0);
                    tvConsumptionExpence.setText("通贝服务费:¥"+0);
                }
            }
        });

    }

    //返回按键
    @Override
    public void onBackPressed() {
        boolean success = getSupportFragmentManager().popBackStackImmediate();
        if (!success)
            super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //图片选择器 结果
        if (requestCode == PhotoPicker.REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                switch (type) {
                    case 1:
                        XiaofeiUrl = photos.get(0);
                        imgConsumptionShow.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        GlideLoad.load(XiaofeiUrl, imgConsumptionShow);
                        break;
//                    case 2:
//                        HuikuanUrl = photos.get(0);
//                        imgConsumptionShow2.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                        GlideLoad.load(HuikuanUrl, imgConsumptionShow2);
//                        break;
                }
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

    /************************************************************************/
    @Override
    public void onUploadVoucheSuccess(boolean isOpereteType) {
        hideProgressDialog2();
        if (!isOpereteType){
            showToast("上传消费凭证成功");
        }else {
            showToast("修改成功");
        }
        delayFinish(200);
    }

    @Override
    public void onUploadVouchefaile(String msg) {
        hideProgressDialog2();
        showToast(msg);
    }

    @Override
    public void onUploadVouchePercentage(float percentage) {
        setProgressDialog2(percentage);
    }

    @Override
    public void onsystemConfigSuccess(SystemConfigBean data) {
        fieldValue = data.getFieldValue();
        initData();
    }

    @Override
    public void onsystemConfigfaile(String msg) {
        showToast(msg);
    }

    /************************************************************************/

    /**
     * 打开单选图片选择器
     */
    private void openImageSingleSelector() {
        PhotoPicker.builder()
                .setPhotoCount(1)
                .setShowCamera(true)
                .setShowGif(false)
                .setPreviewEnabled(false)
                .start(this, PhotoPicker.REQUEST_CODE);
    }

    public static void startActivity(Context context,boolean isOpereteType) {
        Intent intent = new Intent(context, UploadVoucheActivity.class);
        intent.putExtra("isOpereteType",isOpereteType);
        context.startActivity(intent);
    }


    @OnClick({R.id.bt_back, R.id.img_consumption_show, R.id.img_consumption_show2, R.id.btn_consumption_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;
            case R.id.img_consumption_show:
                type = 1;
                if (PermissionUtil.permissionReadStorage(this))
                    openImageSingleSelector();
                break;
            case R.id.img_consumption_show2:
                type = 2;
                if (PermissionUtil.permissionReadStorage(this))
                    openImageSingleSelector();
                break;
            case R.id.btn_consumption_commit:
                String money = etConsumptionPrice.getText().toString().trim();
                if (TextUtils.isEmpty(money)||Double.parseDouble(money) < 0) {
                    showToast("请填写消费金额");
                    return;
                }
                if (Double.parseDouble(money) < 1) {
                    showToast("消费金额不能小于1");
                    return;
                }
                if (TextUtils.isEmpty(XiaofeiUrl)) {
                    showToast("请先添加消费凭证");
                    return;
                }
                showProgressDialog2();

                uploadVouchePresenter.UploadVoucheImage(isOpereteType,id,money,orderNo,jifen+"",ratioFee+"",xiaofeiType,XiaofeiUrl);
                Log.e("订单编号确认收货",orderNo);
                Log.e("订单图片确认收货",XiaofeiUrl+"");

                break;
        }
    }
}
