package com.futuretongfu.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.futuretongfu.bean.WlsqTypeBean;
import com.futuretongfu.iview.IbusniessUpgradeView;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.utils.BitmapUtil;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.PermissionUtil;
import com.futuretongfu.utils.SoftHideKeyBoardUtil;
import com.futuretongfu.view.UpgradePopupWindow;
import com.futuretongfu.R;
import com.futuretongfu.base.BaseSerializable;
import com.futuretongfu.presenter.user.BusinessUpgradePresenter;
import com.futuretongfu.utils.PhotoSelectUtil;
import com.futuretongfu.utils.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;

/**
 * 升级商家1
 *
 * @author ChenXiaoPeng
 * @ change weiang *
 */
public class UpgradeBusiness1Activity extends BaseActivity implements UpgradePopupWindow.PopupWindowListener, IbusniessUpgradeView {
    @Bind(R.id.tv_title)
    public TextView textTitle;
    @Bind(R.id.text_business_name)
    public TextView text_business_name;

    @Bind(R.id.imgv_logo)
    public ImageView imgv_logo;
    @Bind(R.id.iamge_store_pic)
    public ImageView iamge_store_pic;
    @Bind(R.id.iamge_business_licence)
    public ImageView iamge_business_licence;
    @Bind(R.id.iamge_tax_registration)
    public ImageView iamge_tax_registration;

    @Bind(R.id.close_logo_pic)
    public ImageView close_logo_pic;
    @Bind(R.id.close_store_pic)
    public ImageView close_store_pic;
    @Bind(R.id.close_business_pic)
    public ImageView close_business_pic;
    @Bind(R.id.close_tax_pic)
    public ImageView close_tax_pic;

    @Bind(R.id.boss_name)
    public TextView boss_name;
    @Bind(R.id.text_address)
    public TextView text_address;
    @Bind(R.id.text_address_details)
    public TextInputEditText text_address_details;
    @Bind(R.id.text_phone)
    public TextInputEditText text_phone;
    @Bind(R.id.telephone)
    public TextInputEditText telephone;
    @Bind(R.id.btn_next)
    public Button btn_next;

    @Bind(R.id.root_layout)
    public LinearLayout rootView;
    private String proviceString = "";
    private String cityString = "";
    private String aearString = "";
    private String aearCode = "";
    private UpgradePopupWindow popupWindow;
    private BusinessUpgradePresenter presenter;

    private String logoPath = "";
    private String businessPath = "";
    private String taxPath = "";
    private String busnissPic = "";
    private int imageType = -1;
    private int imageurlNumber = 0;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    GlideLoad.load(tempCropFile1.getPath(), imgv_logo);
                    close_logo_pic.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    close_business_pic.setVisibility(View.VISIBLE);
                    GlideLoad.load(businessPath, iamge_business_licence);
                    break;
                case 3:
                    GlideLoad.load(taxPath, iamge_tax_registration);
                    close_tax_pic.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    GlideLoad.load(busnissPic, iamge_store_pic);
                    close_store_pic.setVisibility(View.VISIBLE);
                    break;
                    default:
                        break;
            }

        }
    };
    private File tempCropFile;
    private File tempCropFile1;

    /***********************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_upgrade_business1;
    }

    @Override
    protected Presenter getPresenter() {
        return presenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        SoftHideKeyBoardUtil.assistActivity(this);
        SoftHideKeyBoardUtil s = new SoftHideKeyBoardUtil(this);
        textTitle.setText("资料提交");
        presenter = new BusinessUpgradePresenter(this, this);
        popupWindow = new UpgradePopupWindow(context);
        popupWindow.setPopupWindowListener(this);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Util.setAlpha(context, 1.0f);
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

    /***********************************************************************/
    @OnClick(R.id.close_logo_pic)
    public void onLogoClickBack() {
        close_logo_pic.setVisibility(View.GONE);
        GlideLoad.load("", imgv_logo);
        logoPath = "";
    }

    @OnClick(R.id.close_store_pic)
    public void onClickcloseStoreBack() {
        close_store_pic.setVisibility(View.GONE);
        GlideLoad.load("", iamge_store_pic);
        busnissPic = "";
    }

    @OnClick(R.id.close_business_pic)
    public void onCloseBusinessClickBack() {
        close_business_pic.setVisibility(View.GONE);
        GlideLoad.load("", iamge_business_licence);
        businessPath = "";
    }

    @OnClick(R.id.close_tax_pic)
    public void onCloseBTClickBack() {
        close_tax_pic.setVisibility(View.GONE);
        GlideLoad.load("", iamge_tax_registration);
        taxPath = "";
    }

    @OnClick(R.id.bt_back)
    public void onClickBack() {
        finish();
    }


    @OnClick(R.id.iamge_business_licence)
    public void onClickBlicence() {
        imageType = 2;
        if (PermissionUtil.permissionReadStorage(this)) {
            openImageSingleSelector();
        }
    }

    @OnClick(R.id.iamge_tax_registration)
    public void onClickTax() {
        imageType = 3;
        if (PermissionUtil.permissionReadStorage(this)) {
            openImageSingleSelector();
        }
    }

    //商家Logo
    @OnClick(R.id.imgv_logo)
    public void onClickLogo() {
        imageType = 1;
        if (PermissionUtil.permissionReadStorage(this)) {
            openImageSingleSelector();
        }
    }

    //商家照片
    @OnClick(R.id.iamge_store_pic)
    public void onClickbusissPic() {
        imageType = 4;
        if (PermissionUtil.permissionReadStorage(this)) {
            openImageSingleSelector();
        }
    }


    //省份、城市、区县
    @OnClick(R.id.text_address)
    public void onClickAddress() {
        popupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
        Util.setAlpha(context, 0.5f);
    }

    @OnClick(R.id.btn_next)
    public void onClickNext() {
        String storeName = text_business_name.getText().toString().trim();
        if (TextUtils.isEmpty(storeName)) {
            showToast("请输入商家名称");
            return;
        }
        String bossName = boss_name.getText().toString().trim();
        if (TextUtils.isEmpty(bossName)) {
            showToast("请输入法人代表");
            return;
        }
        String phone = text_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            showToast("请输入手机号");
            return;
        }

        if (TextUtils.isEmpty(proviceString)) {
            showToast("选择省市地区");
            return;
        }
        if (TextUtils.isEmpty(busnissPic)) {
            showToast("请选择商家照片");
            return;
        } else {
            imageurlNumber++;
        }
        if (!TextUtils.isEmpty(tempCropFile1.getPath())) {
            imageurlNumber++;
        }

        if (TextUtils.isEmpty(businessPath)) {
            showToast("请选择营业执照");
            return;
        } else {
            imageurlNumber++;
        }

        Map<String, String> map = new HashMap<>();
        map.put("storeId", UserManager.getInstance().getUserId() + "");
        map.put("storeName", storeName);
        map.put("legalName", bossName);
        map.put("province", proviceString);
        map.put("city", cityString);
        map.put("district", aearString);
        map.put("area", aearCode);
        map.put("contacterType", phone);
        map.put("bannerUrl", busnissPic);
        map.put("licenceUrl", businessPath);

        String detailsaddress = text_address_details.getText().toString().trim();
        if (!TextUtils.isEmpty(detailsaddress)) {
            map.put("address", detailsaddress);
        }
        String telePhone = telephone.getText().toString().trim();
        if (!TextUtils.isEmpty(telePhone)) {
            map.put("tellPhone", telePhone);
        }
        if (!TextUtils.isEmpty(logoPath)) {
            imageurlNumber++;
            map.put("logoUrl", tempCropFile1.getPath());
        }
        if (!TextUtils.isEmpty(taxPath)) {
            map.put("taxCertificate", taxPath);
            imageurlNumber++;
        }
        showProgressDialog2();
        presenter.commitUpBusinessApply(map, imageurlNumber);

    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, UpgradeBusiness1Activity.class);
        context.startActivity(intent);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onChooseFinish(String provices,String proviceId, String city,String cityId, String aear, String aCode) {
        proviceString = provices;
        cityString = city;
        aearString = aear;
        aearCode = aCode;
        text_address.setText(provices + "  " + city + "  " + aear);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //图片选择器 结果
        if (requestCode == PhotoPicker.REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                File temp = new File(photos.get(0));
                switch (imageType) {
                    case 1:
                        if (photos.size() < 1) {
                            break;
                        }
                        logoPath = photos.get(0);
                        tempCropFile1 = new File(Environment.getExternalStorageDirectory(), SystemClock.currentThreadTimeMillis() + PhotoSelectUtil.getCropImageName());
                        PhotoSelectUtil.cropPicture(this, temp, tempCropFile1);
                        break;
                    case 2:
                        businessPath = photos.get(0);
                        setTempBmp(businessPath);
                        handler.sendEmptyMessage(2);
                        break;
                    case 3:
                        taxPath = photos.get(0);
                        setTempBmp(taxPath);
                        handler.sendEmptyMessage(3);

                        break;
                    case 4:
                        if (photos.size() < 1) {
                            break;
                        }
                        busnissPic = photos.get(0);
//                        tempCropFile = new File(Environment.getExternalStorageDirectory(), SystemClock.currentThreadTimeMillis() + PhotoSelectUtil.getCropImageName());
//                        PhotoSelectUtil.cropPicture(this, temp, tempCropFile);
                        setTempBmp(busnissPic);
                        handler.sendEmptyMessage(4);
                        break;
                }
            }
        } else if (RESULT_OK == resultCode && requestCode == PhotoSelectUtil.Request_Code_CropImage) {
            switch (imageType) {
                case 4:
                    setTempBmp(busnissPic);
                    handler.sendEmptyMessage(4);
                    break;
                case 1:
                    setTempBmp(tempCropFile1.getPath());
                    handler.sendEmptyMessage(1);
                    break;
            }

        }
    }

    /**
     * 图片压缩
     *
     * @param path 存储路径
     */
    private void setTempBmp(String path) {
        try {
            Bitmap tempHeaderBmp = BitmapUtil.ratio(BitmapUtil.getBitmap(path));
            tempHeaderBmp.compress(Bitmap.CompressFormat.JPEG, 50, new FileOutputStream(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (PermissionUtil.isReadStoragePermission(permissions)) {
            if (!PermissionUtil.getPermissionReadStorageResult(permissions, grantResults)) {
                showToast(R.string.err_permission_read_photo);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


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


    @Override
    public void busniessUpgradeFail(int code, String msg) {
        showToast(msg);
        hideProgressDialog2();
    }

    @Override
    public void busniessUpgradeSuccess(BaseSerializable data) {
        showToast("申请成功");
        finish();
        hideProgressDialog2();
        UpgradeBusiness2Activity.startActivity(context);
    }

    @Override
    public void storeTypeSuccess(List<WlsqTypeBean> data) {

    }

    @Override
    public void storeTypeFail(int code, String msg) {

    }

    @Override
    public void onUpDataPicFaile(String msg) {
        hideProgressDialog2();
        showToast(msg);

    }

    @Override
    public void onUpDataPicPercentage(float percentage) {
        setProgressDialog2(percentage);
    }
}
