package com.futuretongfu.ui.activity.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.futuretongfu.bean.UserRealBean;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.iview.IRealNameAuthView;
import com.futuretongfu.listener.IPermissionListener;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.ui.activity.BaseActivity;
import com.futuretongfu.ui.activity.ShowWebViewActivity;
import com.futuretongfu.ui.component.checkBox.SmoothCheckBox;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.KeyboardUtil;
import com.futuretongfu.utils.MatchUtil;
import com.futuretongfu.utils.PermissionUtil;
import com.futuretongfu.utils.StringUtil;
import com.futuretongfu.utils.TimerUtil;
import com.futuretongfu.R;
import com.futuretongfu.presenter.user.RealNameAuthPresenter;
import com.futuretongfu.utils.Util;
import com.futuretongfu.view.UpgradePopupWindow;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import me.iwf.photopicker.PhotoPicker;

/**
 * 实名认证
 *
 * @author ChenXiaoPeng
 */
public class RealNameAuthActivity extends BaseActivity implements IRealNameAuthView, IPermissionListener, UpgradePopupWindow.PopupWindowListener {

    private final static String Intent_Extra_Guild = "guild ";

    private final static int Type_Null = -1;
    private final static int Type_SfzZm = 0;
    private final static int Type_SfzFm = 1;
    private final static int Type_ScSfz = 2;

    @Bind(R.id.bt_back)
    public ImageView imgvBack;
    @Bind(R.id.tv_title)
    public TextView textTitle;
    @Bind(R.id.bt_right)
    public TextView textSkin;

    @Bind(R.id.view_real_name_statues_err)
    public LinearLayout viewRealNameStatuesErr;
    @Bind(R.id.text_error)
    public TextView textError;
    @Bind(R.id.view_real_name)
    public NestedScrollView viewRealName;

    @Bind(R.id.view_real_name_statue)
    public LinearLayout viewRealNameStatue;
    @Bind(R.id.imgv_photo)
    public ImageView imgvPhoto;
    @Bind(R.id.text_real_name)
    public TextView textRealName;
    @Bind(R.id.text_real_name_statue)
    public TextView textRealNameStatue;
    @Bind(R.id.text_id_string)
    public TextView textIdStr;

    @Bind(R.id.text_name)
    public TextInputEditText textName;
    @Bind(R.id.choose_address)
    public TextView choose_address;
    @Bind(R.id.view_choose_address)
    public View view_choose_address;
    @Bind(R.id.text_id)
    public TextInputEditText textID;
    @Bind(R.id.image_scan_ic)
    public ImageView image_scan_ic;
    @Bind(R.id.imgv_sfz_fm)
    public ImageView imgv_sfz_fm;
    @Bind(R.id.imgv_sfz_zm)
    public ImageView imgv_sfz_zm;
    @Bind(R.id.imgv_scsfz)
    public ImageView imgv_scsfz;
    @Bind(R.id.root_layout)
    public LinearLayout rootView;
    @Bind(R.id.checkbox_agreement)
    public SmoothCheckBox checkBoxAgreement;

    private int type = Type_Null;

    private String faceUrl = "";
    private String backUrl = "";
    private String handheldUrl = "";
    private String cardId = "";
    private UpgradePopupWindow popupWindow;
    private String proviceId = "";
    private String cityId = "";

    private RealNameAuthPresenter realNameAuthPresenter;
    private boolean isGuild;

    /***********************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_real_name_auth;
    }

    @Override
    protected Presenter getPresenter() {
        return realNameAuthPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        isGuild = intent.getBooleanExtra(Intent_Extra_Guild, false);

        textTitle.setText("实名认证");
        checkBoxAgreement.setChecked(true);

        realNameAuthPresenter = new RealNameAuthPresenter(this, this);
        popupWindow = new UpgradePopupWindow(context);
        popupWindow.setPopupWindowListener(this);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Util.setAlpha(context, 1.0f);
            }
        });
//        if (isGuild) {
//            imgvBack.setVisibility(View.GONE);
//            textSkin.setVisibility(View.VISIBLE);
//            textSkin.setText("跳过");
//        } else {
        imgvBack.setVisibility(View.VISIBLE);
        textSkin.setVisibility(View.GONE);
//        }
        showProgressDialog();
        realNameAuthPresenter.getRealNameStatuesInfo();
     /*   TimerUtil.startTimer(300, new TimerUtil.TimerCallBackListener2() {
            @Override
            public void onEnd() {
                showProgressDialog();
                realNameAuthPresenter.getRealNameStatuesInfo();
            }
        });*/
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
                    case Type_SfzZm:
                        faceUrl = photos.get(0);
                        imgv_sfz_zm.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        GlideLoad.load(faceUrl, imgv_sfz_zm);
                        break;
                    case Type_SfzFm:
                        backUrl = photos.get(0);
                        imgv_sfz_fm.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        GlideLoad.load(backUrl, imgv_sfz_fm);
                        break;
                    case Type_ScSfz:
                        handheldUrl = photos.get(0);
                        imgv_scsfz.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        GlideLoad.load(handheldUrl, imgv_scsfz);
                        break;
                }

            }
        }

        if (requestCode == 100 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            realNameAuthPresenter.uploadAndRecognize(extras.getString("path"));
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

        if (PermissionUtil.isCarmeraPermission(permissions)) {
            if (PermissionUtil.getPermissionCameraResult(permissions, grantResults)) {
                realNameAuthPresenter.checkCardNumber(this);
            } else {
                showToast("您拒绝了开启相机所需要的相关权限");
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    /***********************************************************************/
    @OnClick(R.id.bt_back)
    public void onClickBack() {
        finish();
    }

    /**
     * 跳过
     */
    @OnClick(R.id.bt_right)
    public void onClickSkin() {
        skinNext();
    }

    @OnClick(R.id.view_content)
    public void onClickContent() {
        hideKeyboard();
    }

    /**
     * 扫一扫识别身份证
     */
    @OnClick(R.id.image_scan_ic)
    public void onClickscan() {
        if (PermissionUtil.permissionCamera(this)) {
            realNameAuthPresenter.checkCardNumber(this);
        }

    }

    @Override
    public void onPermissionGranted(String name) {
        realNameAuthPresenter.checkCardNumber(this);
    }

    @Override
    public void onPermissionDenied(String name) {
        showToast("您拒绝了开启相机所需要的相关权限!");
    }

    /**
     * 身份证 正面
     */
    @OnClick(R.id.imgv_sfz_zm)
    public void onClickSfzZm() {
        hideKeyboard();
        type = Type_SfzZm;
        if (PermissionUtil.permissionReadStorage(this))
            openImageSingleSelector();
    }

    /**
     * 身份证 反面
     */
    @OnClick(R.id.imgv_sfz_fm)
    public void onClickSfzFm() {
        hideKeyboard();
        type = Type_SfzFm;
        if (PermissionUtil.permissionReadStorage(this))
            openImageSingleSelector();
    }

    /**
     * 手持身份证
     */
    @OnClick(R.id.imgv_scsfz)
    public void onClickScsfz() {
        hideKeyboard();
        type = Type_ScSfz;
        if (PermissionUtil.permissionReadStorage(this))
            openImageSingleSelector();
    }

    /**
     * 首都富实名认证隐私协议
     */
    @OnClick(R.id.btn_agreement)
    public void onClickAgreement() {
        ShowWebViewActivity.startActivity(this, Constants.Url_Agreement_PrivacyProtocol, "首都富隐私协议", true);
    }

    /**
     * 提交
     */
    @OnClick(R.id.btn_submit)
    public void onClickSubmit() {
        hideKeyboard();

        String name = textName.getText().toString().trim();
        if (isEmpty(name, "真实姓名")) {
            return;
        }
        if (TextUtils.isEmpty(proviceId)||TextUtils.isEmpty(cityId)) {
            showToast("选择户籍所在地");
            return;
        }

        String id = textID.getText().toString().trim();
        if (isEmpty(id, "身份证")) {
            return;
        }

        if (!MatchUtil.isName(name)) {
            showToast("姓名格式错误");
            return;
        }

        if (!MatchUtil.isValidatedAllIdcard(id)) {
            showToast("身份证格式错误");
            return;
        }

        if (TextUtils.isEmpty(faceUrl)) {
            showToast("还未提交身份证正面照片");
            return;
        }

        if (TextUtils.isEmpty(backUrl)) {
            showToast("还未提交身份证背面照片");
            return;
        }

        if (!checkBoxAgreement.isChecked()) {
            showToast("请先同意隐私协议");
            return;
        }

//        if (TextUtils.isEmpty(handheldUrl)) {
//            showToast("还未提交手持身份证照片");
//            return;
//        }

        showProgressDialog2();
        realNameAuthPresenter.realname(
                textName.getText().toString()
                , textID.getText().toString()
                , faceUrl, backUrl, handheldUrl,proviceId,cityId);

    }

    @OnClick(R.id.view_real_name_statues_err)
    public void onClickError() {
        showProgressDialog();
        realNameAuthPresenter.getRealNameStatuesInfo();
    }

    @OnClick(R.id.choose_address)
    public void onClickAddress() {
        hideKeyboard();
        popupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);
        Util.setAlpha(context, 0.5f);
    }
    /***********************************************************************/

    /***********************************************************************/
    @Override
    public void onRealNameAuthSuccess() {
        hideProgressDialog2();
        showToast("实名认证申请已经提交");
        if (isGuild) {
            skinNext();
        }

        delayFinish(200);
    }

    @Override
    public void onRealNameAuthFaile(String msg) {
        hideProgressDialog2();
        showToast(msg);
    }

    @Override
    public void onRealNameAuthPercentage(float percentage) {
        setProgressDialog2(percentage);
    }

    @Override
    public void onGetRealNameStatusSuccess() {
        hideProgressDialog();
        cardId = UserManager.getInstance().getCardId();
        switch (UserManager.getInstance().getRealNameStatus()) {
            case Constants.RealNameStatus_No://未实名
            case Constants.RealNameStatus_Faile://审核失败
                viewRealNameStatue.setVisibility(View.GONE);
                viewRealNameStatuesErr.setVisibility(View.GONE);
                viewRealName.setVisibility(View.VISIBLE);
                break;
            case Constants.RealNameStatus_Imperfect://身份证信息不完善
                realNameAuthPresenter.searchUserReal();  //获取身份信息
                viewRealNameStatue.setVisibility(View.GONE);
                viewRealNameStatuesErr.setVisibility(View.GONE);
                viewRealName.setVisibility(View.VISIBLE);
                break;
            case Constants.RealNameStatus_Yes://已实名
                viewRealNameStatue.setVisibility(View.VISIBLE);
                viewRealNameStatuesErr.setVisibility(View.GONE);
                viewRealName.setVisibility(View.GONE);

                GlideLoad.loadCrossFadeImageView(
                        UserManager.getInstance().getFaceUrl()
                        , R.mipmap.icon_head_2, R.mipmap.icon_head_2
                        , imgvPhoto
                );

                String realName = UserManager.getInstance().getRealName();
                textRealName.setText(StringUtil.setVisibilityName(realName));
                textIdStr.setText(StringUtil.getStarString(cardId, 6, cardId.length() - 4));
                textRealNameStatue.setBackground(getResources().getDrawable(R.drawable.shape_user_real_name));
                textRealNameStatue.setText("已实名");
                textRealNameStatue.setTextColor(getResources().getColor(R.color.colorWhite));

                break;

            case Constants.RealNameStatus_Doing://审核中
                viewRealNameStatue.setVisibility(View.VISIBLE);
                viewRealNameStatuesErr.setVisibility(View.GONE);
                viewRealName.setVisibility(View.GONE);

                GlideLoad.loadCrossFadeImageView(
                        UserManager.getInstance().getFaceUrl()
                        , R.mipmap.icon_head_2, R.mipmap.icon_head_2
                        , imgvPhoto
                );
                textRealName.setText(UserManager.getInstance().getRealName());
                textIdStr.setText(StringUtil.getStarString(cardId, 6, cardId.length() - 4));

                textRealNameStatue.setBackground(getResources().getDrawable(R.drawable.shape_user_unknown_real_name));
                textRealNameStatue.setText("审核中");
                textRealNameStatue.setTextColor(getResources().getColor(R.color.colorTextNormalLight));

                break;


//                viewRealNameStatue.setVisibility(View.GONE);
//                viewRealNameStatuesErr.setVisibility(View.GONE);
//                viewRealName.setVisibility(View.VISIBLE);

//                new PromptDialog
//                        .Builder(context)
//                        .setMessage("实名未通过，请重新实名")
//                        .setButton1("确定", new PromptDialog.OnClickListener() {
//                            @Override
//                            public void onClick(Dialog dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        })
//                        .show();

//                break;
        }

    }

    @Override
    public void onGetRealNameStatusFaile(String msg) {
        viewRealNameStatuesErr.setVisibility(View.VISIBLE);
        viewRealName.setVisibility(View.GONE);
        textError.setText(msg + " 点击重试");

        hideProgressDialog();
    }

    @Override
    public void onsearchUserRealSuccess(UserRealBean data) {
        proviceId = data.getProvinceId();
        cityId = data.getCityId();
        cardId = data.getCardId();
        choose_address.setText(data.getProvinceId()+" "+data.getCityId());
        choose_address.setVisibility(View.GONE);
        view_choose_address.setVisibility(View.GONE);
        textName.setText(data.getRealName());
        textName.setEnabled(false);
        textID.setText(data.getCardId());
        textID.setEnabled(false);
    }

    @Override
    public void onsearchUserRealFaile(String msg) {
        showToast(msg);
    }

    @Override
    public void onGetCardNumberSuccess(final String number) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textID.setText(number);
            }
        });
    }

    @Override
    public void onGetCardNumberFaile(String msg) {
        showToast(msg);
    }

    /***********************************************************************/

    private void skinNext() {
//        //设置安全问题
//        MiBaoQuestionActivity.startActivity(this, MiBaoQuestionActivity.Type_MiBao_Guide);
        delayFinish(200);
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

    private void hideKeyboard() {
        KeyboardUtil.showKeyboard(this, textName, false);
        KeyboardUtil.showKeyboard(this, textID, false);
    }

    public static void startActivity(Context context, boolean isGuild) {
        Intent intent = new Intent(context, RealNameAuthActivity.class);
        intent.putExtra(Intent_Extra_Guild, isGuild);
        context.startActivity(intent);
    }

    @Override
    public void onChooseFinish(String provices, String proId, String city, String ciId, String area, String aearCode) {
        proviceId = proId;
        cityId = ciId;
        choose_address.setText(provices + "  " + city + "  "+area );
    }
}
