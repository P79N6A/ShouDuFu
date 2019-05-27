package com.futuretongfu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.futuretongfu.bean.WlsqTypeBean;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.iview.IbusniessUpgradeView;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.ui.adapter.StoreTypeAdapter;
import com.futuretongfu.utils.BitmapUtil;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.PermissionUtil;
import com.futuretongfu.R;
import com.futuretongfu.base.BaseSerializable;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.user.BusinessUpgradePresenter;
import com.futuretongfu.ui.component.checkBox.SmoothCheckBox;

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
 * 升级商家2
 *
 * @author ChenXiaoPeng
 */
public class UpgradeBusiness2Activity extends BaseActivity implements IbusniessUpgradeView {

    @Bind(R.id.tv_title)
    public TextView textTitle;

    @Bind(R.id.store_linkly)
    public TextInputEditText store_linkly;
    @Bind(R.id.store_introduction)
    public TextInputEditText store_introduction;
    @Bind(R.id.store_type)
    public TextView store_type;

    @Bind(R.id.recycler_list)
    public RecyclerView recyclerList;

    @Bind(R.id.image_pic_1)
    public ImageView image_pic_1;
    @Bind(R.id.image_pic_2)
    public ImageView image_pic_2;
    @Bind(R.id.image_pic_3)
    public ImageView image_pic_3;

    @Bind(R.id.close_pic_1)
    public ImageView close_pic_1;
    @Bind(R.id.close_pic_2)
    public ImageView close_pic_2;
    @Bind(R.id.close_pic_3)
    public ImageView close_pic_3;

    @Bind(R.id.checkbox_agreement)
    public SmoothCheckBox checkBoxAgreement;

    private String industryId = "";
    private String imagePath1 = "";
    private String imagePath2 = "";
    private String imagePath3 = "";
    private int imageType = -1;
    private BusinessUpgradePresenter presenter;
    StoreTypeAdapter adapter;
    private List<WlsqTypeBean> list;
    private int imageurlNumber = 0;

    /***********************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_upgrade_business2;
    }

    @Override
    protected Presenter getPresenter() {
        return presenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        textTitle.setText("商家详情");
        checkBoxAgreement.setChecked(true);

        list = new ArrayList<>();
        adapter = new StoreTypeAdapter(R.layout.item_edit_address, list);
        presenter = new BusinessUpgradePresenter(this, this);
        recyclerList.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerList.setAdapter(adapter);
        presenter.onStoreType();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                industryId = list.get(position).id + "";
                store_type.setText(list.get(position).hyNamePc);
                recyclerList.setVisibility(View.GONE);
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
    @OnClick(R.id.bt_back)
    public void onClickBack() {
        finish();
    }

    @OnClick(R.id.store_type)
    public void onClickStoreType() {
        recyclerList.setVisibility(View.VISIBLE);
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

    @OnClick(R.id.close_pic_1)
    public void onLogoClickBack() {
        GlideLoad.load("", image_pic_1);
        close_pic_1.setVisibility(View.GONE);
        imagePath1 = "";
    }

    @OnClick(R.id.close_pic_2)
    public void onClickcloseStoreBack() {
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

    @OnClick(R.id.btn_agreement)
    public void onClickAgreement() {
        ShowWebViewActivity.startActivity(this, Constants.Url_Agreement_ServiceProtocol, "首都富商家协议", true);
    }

    @OnClick(R.id.btn_next)
    public void onClickNext() {
        String storeIntroduction = store_introduction.getText().toString().trim();
//        if (TextUtils.isEmpty(storeIntroduction)) {
//            showToast("请输入商家简介");
//            return;
//        }

        if (TextUtils.isEmpty(industryId)) {
            showToast("请选择经营类型");
            return;
        }
        String storeLink = store_linkly.getText().toString().trim();
        Map<String, String> map = new HashMap<>();
        map.put("storeId", UserManager.getInstance().getUserId() + "");
        map.put("industryId", industryId);
        map.put("storeInfo", storeIntroduction);
        if (!TextUtils.isEmpty(storeLink)) {
            map.put("siteUrl", storeLink);
        }
        if (!TextUtils.isEmpty(imagePath1)) {
            map.put("storeImgUrl1", imagePath1);
            imageurlNumber++;
        }
        if (!TextUtils.isEmpty(imagePath2)) {
            map.put("storeImgUrl2", imagePath2);
            imageurlNumber++;
        }
        if (!TextUtils.isEmpty(imagePath3)) {
            map.put("storeImgUrl3", imagePath3);
            imageurlNumber++;
        }
        if (0 == imageurlNumber) {
            showToast("请至少上传一张图片");
            return;
        }

        if (!checkBoxAgreement.isChecked()) {
            showToast("请先同意商家协议");
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
                }
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
        Intent intent = new Intent(context, UpgradeBusiness2Activity.class);
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
    public void storeTypeSuccess(List<WlsqTypeBean> data) {
        if (!data.isEmpty()) {
            list.addAll(data);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void storeTypeFail(int code, String msg) {
        showToast(msg);
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
