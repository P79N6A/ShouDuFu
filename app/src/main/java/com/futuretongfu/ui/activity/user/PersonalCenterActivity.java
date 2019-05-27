package com.futuretongfu.ui.activity.user;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.R;
import com.futuretongfu.iview.IPersonalCenterView;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.user.PersonalCenterPresenter;
import com.futuretongfu.ui.component.dialog.CameraSelectDialog;
import com.futuretongfu.utils.PermissionUtil;
import com.futuretongfu.utils.PhotoSelectUtil;
import com.futuretongfu.utils.StringUtil;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;

/**
 * 个人中心
 *
 * @author ChenXiaoPeng
 */
public class PersonalCenterActivity extends BaseActivity implements IPersonalCenterView, CameraSelectDialog.OnCameraSelectorConfirmListener {

    @Bind(R.id.tv_title)
    public TextView textTitle;

    @Bind(R.id.imgv_photo)
    public ImageView imgFace;

    @Bind(R.id.text_account)
    public TextView textAccount;
    @Bind(R.id.text_name)
    public TextView textName;
    @Bind(R.id.text_gender)
    public TextView textGender;
    @Bind(R.id.text_email)
    public TextView textEmail;
    @Bind(R.id.text_address)
    public TextView textAddress;
    @Bind(R.id.text_phone)
    public TextView textPhoneNumber;

    private PersonalCenterPresenter personalCenterPresenter;
    private Bitmap tempHeaderBmp;
    private File tempHeaderFile;
    private File tempCropFile;

    /***********************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_center;
    }


    @Override
    protected Presenter getPresenter() {
        return personalCenterPresenter;
    }


    @Override
    protected void init(Bundle savedInstanceState) {
        textTitle.setText("个人中心");
        personalCenterPresenter = new PersonalCenterPresenter(this, this);
        tempHeaderFile = new File(Environment.getExternalStorageDirectory(), PhotoSelectUtil.getCameraImageName());
        tempCropFile = new File(Environment.getExternalStorageDirectory(), PhotoSelectUtil.getCropImageName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (tempHeaderBmp != null)
            tempHeaderBmp = null;
        if (tempHeaderFile != null)
            tempHeaderFile = null;
        if (tempCropFile != null)
            tempCropFile = null;
    }

    //返回按键
    @Override
    public void onBackPressed() {
        boolean success = getSupportFragmentManager().popBackStackImmediate();
        if (!success)
            finishWidthAnimation();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (RESULT_OK == resultCode) {
            switch (requestCode) {
                //相机
                case PhotoSelectUtil.Request_Code_Camera:
                    PhotoSelectUtil.cropPicture(this, tempHeaderFile, tempCropFile);
                    break;

                case PhotoPicker.REQUEST_CODE:
                    ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                    if (photos.size() < 1) {
                        break;
                    }
                    File temp = new File(photos.get(0));
                    PhotoSelectUtil.cropPicture(this, temp, tempCropFile);
                    break;

                case PhotoSelectUtil.Request_Code_CropImage:
                    showProgressDialog2();
                    personalCenterPresenter.updateHeaderImg(tempCropFile.getPath());

                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (PermissionUtil.isReadStoragePermission(permissions)) {
            if (PermissionUtil.getPermissionReadStorageResult(permissions, grantResults)) {
                openImageSingleSelector();
            } else {
                showToast(R.string.err_permission_read_photo);
            }
        } else if (PermissionUtil.isCarmeraAndStoragePermission(permissions)) {
            if (PermissionUtil.getPermissionCarmeraAndStorageResult(permissions, grantResults)) {
                PhotoSelectUtil.openCamre(this, tempHeaderFile);
            } else {
                showToast(R.string.err_permission_camera);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    protected void onResume() {
        super.onResume();
        initUser();
    }

    /**
     * TODO 测试 显示当前环境，上线是删除
     */
    int onClickNum = 0;

    @OnClick(R.id.text_account)
    public void onClickHost() {
        onClickNum ++;
        if(onClickNum == 7){
            //showToast(Constants.HOST);
            onClickNum = 0;
        }
    }

    @OnClick(R.id.bt_back)
    public void onClickBack() {
        finishWidthAnimation();
    }

    /**
     * 头像点击
     */
    @OnClick(R.id.view_click_header_img)
    public void onClickPhoto() {
        CameraSelectDialog dlg = new CameraSelectDialog(this);
        dlg.setOnCameraSelectorConfirmListener(this);
        dlg.show();
    }

    //修改昵称
    @OnClick(R.id.view_nick_name)
    public void onClickNickName() {
        EditActivity.startActivityNickName(this, UserManager.getInstance().getNickName());
    }

    //修改手机
    @OnClick(R.id.phone_layout)
    public void onClickPhone() {
        //EditPhoneActivity.startActivity(this, UserManager.getInstance().getAccountNumber());
    }

    //修改性别
    @OnClick(R.id.view_sex)
    public void onClickSex() {
        EditActivity.startActivityGender(this, UserManager.getInstance().getGender());
    }

    //修改邮箱
    @OnClick(R.id.view_mail)
    public void onClickMail() {
        EditActivity.startActivity(this, EditActivity.Type_Mail);
    }

    //修改地址
    @OnClick(R.id.view_address)
    public void onClickAddress() {
        EditAddressActivity.startActivity(this);
    }

    /***********************************************************************/
    @Override
    public void onPersonalCenterFaceUrlSuccess() {
        hideProgressDialog2();
        setFaceImg();
    }

    @Override
    public void onPersonalCenterFaceUrlFaile(String msg) {
        hideProgressDialog2();
        showToast(msg);
    }

    @Override
    public void onPersonalCenterFaceUrlPercentage(float percentage) {
        setProgressDialog2(percentage);
    }

    /***********************************************************************/
    //打开相册
    @Override
    public void onCameraSelectorPhoto() {
        if (PermissionUtil.permissionReadStorage(this)) {
            openImageSingleSelector();
        }
    }

    //打开相机
    @Override
    public void onCameraSelectorCamera() {
//        if (PermissionUtil.permissionCameraAndReadStorage(this)) {
//            PhotoSelectUtil.openCamre(this);
//        }

        if (PermissionUtil.permissionCameraAndStorage(this)) {
            PhotoSelectUtil.openCamre(this, tempHeaderFile);
        }
    }


    private void initUser() {
        if (!UserManager.getInstance().isLogin())
            return;
        textAccount.setText(UserManager.getInstance().getUserName());
        textPhoneNumber.setText(UserManager.getInstance().getAccountNumber());
        textName.setText(StringUtil.getNoEmptyStr(UserManager.getInstance().getNickName(), "未设置"));
        textGender.setText(StringUtil.getGender(UserManager.getInstance().getGender()));
        textEmail.setText(StringUtil.getNoEmptyStr(UserManager.getInstance().getEmail(), "未设置"));
        textAddress.setText(StringUtil.getNoEmptyStr(UserManager.getInstance().getRegion(), "未设置"));
        setFaceImg();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("deprecation")
    private void finishWidthAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }

    /**
     * 打开单选图片选择器
     */
    private void openImageSingleSelector() {
        PhotoPicker.builder()
                .setPhotoCount(1)
                .setShowCamera(false)
                .setShowGif(false)
                .setPreviewEnabled(false)
                .start(this, PhotoPicker.REQUEST_CODE);
    }

    /*************************************************************************/

    private void setFaceImg() {
        if (UserManager.getInstance().getFaceUrl() == null || TextUtils.isEmpty(UserManager.getInstance().getFaceUrl())) {
            imgFace.setImageDrawable(ContextCompat.getDrawable(this, R.mipmap.icon_head_2));
            return;
        }

        GlideLoad.loadCrossFadeCropCircleImageView(
                UserManager.getInstance().getFaceUrl()
                , R.mipmap.icon_head_2
                , R.mipmap.icon_head_2
                , imgFace);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, PersonalCenterActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Activity context, View view) {
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(context, view, context.getResources().getString(R.string.scene_animation_personal_data_photo));

        Intent intent = new Intent(context, PersonalCenterActivity.class);
        context.startActivity(intent, options.toBundle());


    }
}
