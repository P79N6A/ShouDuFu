package com.futuretongfu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.futuretongfu.R;
import com.futuretongfu.base.BaseSerializable;
import com.futuretongfu.bean.WlsqTypeBean;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.iview.IbusniessUpgradeUpdateView;
import com.futuretongfu.iview.IbusniessUpgradeView;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.user.BusinessUpgradePresenter;
import com.futuretongfu.presenter.user.BusinessUpgradeUpdatePresenter;
import com.futuretongfu.ui.adapter.StoreTypeAdapter;
import com.futuretongfu.ui.component.checkBox.SmoothCheckBox;
import com.futuretongfu.utils.BitmapUtil;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.PermissionUtil;
import com.futuretongfu.utils.PhotoSelectUtil;

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
 * 商家资料修改
 * @author zhanggf
 */
public class UpgradeBusinessUpdateActivity extends BaseActivity implements IbusniessUpgradeUpdateView {

    @Bind(R.id.tv_title)
    public TextView textTitle;

    @Bind(R.id.text_business_address)
    public TextInputEditText text_business_address;

    @Bind(R.id.imgv_logo)
    public ImageView imgv_logo;
    @Bind(R.id.iamge_store_pic)
    public ImageView iamge_store_pic;
    @Bind(R.id.image_pic_1)
    public ImageView image_pic_1;
    @Bind(R.id.image_pic_2)
    public ImageView image_pic_2;
    @Bind(R.id.image_pic_3)
    public ImageView image_pic_3;

    @Bind(R.id.close_logo_pic)
    public ImageView close_logo_pic;
    @Bind(R.id.close_store_pic)
    public ImageView close_store_pic;
    @Bind(R.id.close_pic_1)
    public ImageView close_pic_1;
    @Bind(R.id.close_pic_2)
    public ImageView close_pic_2;
    @Bind(R.id.close_pic_3)
    public ImageView close_pic_3;

    private String logoPath = "";
    private String busnissPic = "";
    private String imagePath1 = "";
    private String imagePath2 = "";
    private String imagePath3 = "";
    private int imageType = -1;
    private BusinessUpgradeUpdatePresenter presenter;
    private int imageurlNumber = 0;
    private File tempCropFile;
    private File tempCropFile1;
    /***********************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_upgrade_business_update;
    }

    @Override
    protected Presenter getPresenter() {
        return presenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        textTitle.setText("商家资料修改");
        presenter = new BusinessUpgradeUpdatePresenter(this, this);
    }

    //返回按键
    @Override
    public void onBackPressed() {
        boolean success = getSupportFragmentManager().popBackStackImmediate();
        if (!success)
            super.onBackPressed();
    }

    /***********************************************************************/
    @OnClick(R.id.bt_back)
    public void onClickBack() {
        finish();
    }


    /***********************************************************************/

    //商家Logo
    @OnClick(R.id.imgv_logo)
    public void onClickLogo() {
        imageType = 4;
        if (PermissionUtil.permissionReadStorage(this)) {
            openImageSingleSelector();
        }
    }

    //商家照片
    @OnClick(R.id.iamge_store_pic)
    public void onClickbusissPic() {
        imageType = 5;
        if (PermissionUtil.permissionReadStorage(this)) {
            openImageSingleSelector();
        }
    }


    @OnClick(R.id.image_pic_1)
    public void onClick1() {
        imageType = 1;
        if (PermissionUtil.permissionReadStorage(this)) {
            openImageSingleSelector();
        }
    }

    @OnClick(R.id.image_pic_2)
    public void onClick2() {
        imageType = 2;
        if (PermissionUtil.permissionReadStorage(this)) {
            openImageSingleSelector();
        }
    }

    @OnClick(R.id.image_pic_3)
    public void onClick3() {
        imageType = 3;
        if (PermissionUtil.permissionReadStorage(this)) {
            openImageSingleSelector();
        }
    }

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

    @OnClick(R.id.close_pic_1)
    public void onClickclosePic1Back() {
        GlideLoad.load("", image_pic_1);
        close_pic_1.setVisibility(View.GONE);
        imagePath1 = "";
    }

    @OnClick(R.id.close_pic_2)
    public void onClickclosePic2Back() {
        GlideLoad.load("", image_pic_2);
        close_pic_2.setVisibility(View.GONE);
        imagePath2 = "";
    }

    @OnClick(R.id.close_pic_3)
    public void onCloseBusinessClickBack() {
        GlideLoad.load("", image_pic_3);
        close_pic_3.setVisibility(View.GONE);
        imagePath3 = "";
    }

    @OnClick(R.id.btn_next)
    public void onClickNext() {
        String business_address = text_business_address.getText().toString().trim();
        Map<String, String> map = new HashMap<>();
        map.put("storeId", UserManager.getInstance().getUserId() + "");
        if (!TextUtils.isEmpty(business_address)) {
            map.put("address", business_address);
        }
        if (!TextUtils.isEmpty(logoPath)) {
            imageurlNumber++;
            map.put("logoUrl",logoPath);
        }
        if (!TextUtils.isEmpty(busnissPic)) {
            imageurlNumber++;
//            map.put("bannerUrl", tempCropFile.getPath());
            map.put("bannerUrl", busnissPic);
        }
        if (!TextUtils.isEmpty(imagePath1)) {
            map.put("imageUrl1", imagePath1);
            imageurlNumber++;
        }
        if (!TextUtils.isEmpty(imagePath2)) {
            map.put("imageUrl2", imagePath2);
            imageurlNumber++;
        }
        if (!TextUtils.isEmpty(imagePath3)) {
            map.put("imageUrl3", imagePath3);
            imageurlNumber++;
        }
        if (0 == imageurlNumber&&TextUtils.isEmpty(business_address)) {
            showToast("请至少修改一处再提交");
            return;
        }
        showProgressDialog2();
        presenter.upgradeStoreSubmit(map, imageurlNumber);
    }

    /***********************************************************************/
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    GlideLoad.load(imagePath1, image_pic_1);
                    close_pic_1.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    GlideLoad.load(imagePath2, image_pic_2);
                    close_pic_2.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    GlideLoad.load(imagePath3, image_pic_3);
                    close_pic_3.setVisibility(View.VISIBLE);
                    break;
                case 4:
//                    GlideLoad.load(tempCropFile1.getPath(), imgv_logo);
                    GlideLoad.load(logoPath, imgv_logo);
                    close_logo_pic.setVisibility(View.VISIBLE);
                    break;
                case 5:
//                    GlideLoad.load(tempCropFile.getPath(), iamge_store_pic);
                    GlideLoad.load(busnissPic, iamge_store_pic);
                    close_store_pic.setVisibility(View.VISIBLE);
                    break;
            }

        }
    };


    /**
     * 图片压缩
     *
     * @param path 路径
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //图片选择器 结果
        if (requestCode == PhotoPicker.REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                File temp = new File(photos.get(0));
                switch (imageType) {
                    case 1:
                        imagePath1 = photos.get(0);
                        setTempBmp(imagePath1);
                        handler.sendEmptyMessage(1);
                        break;
                    case 2:
                        imagePath2 = photos.get(0);
                        setTempBmp(imagePath2);
                        handler.sendEmptyMessage(2);
                        break;
                    case 3:
                        imagePath3 = photos.get(0);
                        setTempBmp(imagePath3);
                        handler.sendEmptyMessage(3);
                        break;
                    case 4://logo
                        if (photos.size() < 1) {
                            break;
                        }
                        logoPath = photos.get(0);
                        setTempBmp(logoPath);
                        handler.sendEmptyMessage(4);
//                        tempCropFile1 = new File(Environment.getExternalStorageDirectory(), SystemClock.currentThreadTimeMillis() + PhotoSelectUtil.getCropImageName());
//                        PhotoSelectUtil.cropPicture(this, temp, tempCropFile1);
                        break;
                    case 5://banner
                        if (photos.size() < 1) {
                            break;
                        }
                        busnissPic = photos.get(0);
                        setTempBmp(busnissPic);
                        handler.sendEmptyMessage(5);
//                        tempCropFile = new File(Environment.getExternalStorageDirectory(), SystemClock.currentThreadTimeMillis() + PhotoSelectUtil.getCropImageName());
//                        PhotoSelectUtil.cropPicture2(this, temp, tempCropFile);
                        break;
                }
            }
        } else if (RESULT_OK == resultCode && requestCode == PhotoSelectUtil.Request_Code_CropImage) {
            switch (imageType) {
                case 4:
                    setTempBmp(tempCropFile1.getPath());
                    handler.sendEmptyMessage(4);
                    break;
                case 5:
                    setTempBmp(tempCropFile.getPath());
                    handler.sendEmptyMessage(5);
                    break;
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @Nullable String permissions[], @Nullable int[] grantResults) {
        if (PermissionUtil.isReadStoragePermission(permissions)) {
            if (!PermissionUtil.getPermissionReadStorageResult(permissions, grantResults)) {
                showToast(R.string.err_permission_read_photo);
            }
        }
        if (grantResults != null && permissions != null) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

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


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, UpgradeBusinessUpdateActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void busniessUpgradeFail(int code, String msg) {
        hideProgressDialog2();
        showToast(msg);
    }

    @Override
    public void busniessUpgradeSuccess(BaseSerializable data) {
        showToast("您的资料已经提交");
        hideProgressDialog2();
        UserManager.getInstance().setStore(true);
        UserManager.getInstance().save();
        finish();
    }

    @Override
    public void onUpDataPicFaile(String msg) {
        showToast(msg);
    }

    @Override
    public void onUpDataPicPercentage(float percentage) {
        setProgressDialog2(percentage);
    }
}
