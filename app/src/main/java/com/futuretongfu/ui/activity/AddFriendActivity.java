package com.futuretongfu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.RequestCode;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.utils.CacheActivityUtil;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.PermissionUtil;
import com.futuretongfu.utils.ShareUtil;
import com.futuretongfu.utils.Util;
import com.futuretongfu.view.SharePopupWindow;
import com.skjr.zxinglib.CaptureActivity;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.Log;
import com.futuretongfu.R;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.utils.Logger.Logger;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 添加朋友
 *
 * @author DoneYang 2017/6/19
 */

public class AddFriendActivity extends BaseActivity implements SharePopupWindow.OnItemClickLister {

    @Bind(R.id.tv_title)
    TextView tv_title;
    @Bind(R.id.edt_add_friend_search)
    EditText edtAddFriend;

    private SharePopupWindow sharePopup;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_addfriend;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        CacheActivityUtil.addNewActivity(this);
        tv_title.setText("添加朋友");
        new RxPermissions(this);
        sharePopup = new SharePopupWindow(this);
        sharePopup.setOnItemClickListener(this);
        sharePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Util.setAlpha(AddFriendActivity.this, 1.0f);
            }
        });
        edtAddFriend.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    IntentUtil.startActivity(AddFriendActivity.this, SearchFriendActivity.class
                            , IntentKey.WLF_BEAN, RequestCode.FRIEND_LIST);
                }
                return false;
            }
        });
    }

    @OnClick({R.id.bt_back, R.id.ll_add_friend_my_qr_code, R.id.ll_add_friend_share
            , R.id.ll_add_friend_scan, R.id.ll_add_friend_mobile_directory})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;

            /*我的二维码*/
            case R.id.ll_add_friend_my_qr_code:
                IntentUtil.startActivity(this, MyQrCodeActivity.class);
                break;

            /*分享*/
            case R.id.ll_add_friend_share:
                sharePopup.showAtLocation(findViewById(R.id.ll_addfrend_main), Gravity.BOTTOM, 0, 0);
                Util.setAlpha(this, 0.7f);
                break;

            /*扫一扫*/
            case R.id.ll_add_friend_scan:
                if (PermissionUtil.permissionCamera(this)) {
                    openScan();
                }
                break;

            /*手机通讯录*/
            case R.id.ll_add_friend_mobile_directory:
                if (PermissionUtil.permissionPhoneLinkman(this)) {
                    openPhoneLinkMan();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == RequestCode.REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(IntentKey.SCAN_CONTENT_KEY);
                if (Util.isEmpty(content)) {
                    return;
                }
                Logger.i(TAG, "解码结果：\n" + content);

                if (!content.contains("?")) {
                    showToast("二维码错误，请重新确认后重新扫码。");
                    return;
                }
                String[] codeUrl = content.split("\\?");
                if (Util.isEmpty(codeUrl) || codeUrl.length < 2) {
                    Logger.i(TAG, "code.length()=" + codeUrl.length);
                    showToast("获取扫描信息失败");
                    return;
                }
                String info = codeUrl[1];
                String type = null;
                String ids = null;
                if (!info.contains("userId") && !info.contains("storeId")) {
                    showToast("获取扫描信息失败");
                    return;
                } else if (info.contains("userId")) {
//                    ids = info.split("userId=")[1];
                    String[] aa = info.split("&");
                    String str1 = aa[0];
                    String str2="";
                    if (aa.length>=2){
                        str2 = aa[1];
                    }
                    if (str1.contains("userId")){
                        ids = str1;
                    }else {
                        ids = str2;
                    }
                    ids = ids.split("=")[1];
                    type = "userId";
                } else if (info.contains("storeId")) {
                    ids = info.split("storeId=")[1];
                    type = "storeId";
                }
                if ("userId".equals(type)) {
                    if (!(UserManager.getInstance().getUserId() + "").equals(ids)) {
                        IntentUtil.startActivity(this, ScanAddFriendActivity.class, IntentKey.WLF_ID, ids);
                    } else {
                        showToast("不能自己添加自己");
                    }
                } else if ("storeId".equals(type)) {
                    IntentUtil.startActivity(this, PaymentSetMoneyActivity.class
                            , IntentKey.WLF_ID, ids);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PermissionUtil.isCarmeraPermission(permissions)) {
            if (PermissionUtil.getPermissionCameraResult(permissions, grantResults)) {
                openScan();
            } else {
                showToast(R.string.no_have_scan);
            }
        } else if (PermissionUtil.isPhoneLinkmanPermission(permissions)) {
            if (PermissionUtil.getPermissionPhoneLinkmanResult(permissions, grantResults)) {
                openPhoneLinkMan();
            } else {
                showToast(R.string.no_have_phone_linkman);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 扫一扫
     */
    private void openScan() {
        IntentUtil.startActivityForResult(this, CaptureActivity.class, RequestCode.REQUEST_CODE_SCAN);
    }

    /**
     * 手机通讯录
     */
    private void openPhoneLinkMan() {
        IntentUtil.startActivity(AddFriendActivity.this, PhoneLinkManActivity.class);
    }

    @Override
    public void setOnItemClick(View view) {
        switch (view.getId()) {
            case R.id.ll_wechat_moments:
                if (ShareUtil.isAppAvilible(context, Constants.PACKAGE_NAME_WX)) {
                    ShareUtil.uMengShareApp(AddFriendActivity.this, SHARE_MEDIA.WEIXIN_CIRCLE);
                } else {
                    showToast("您尚未安装微信，请先下载安装！");
                }
                break;

            case R.id.ll_qq:
                if (ShareUtil.isAppAvilible(context, Constants.PACKAGE_NAME_QQ)) {
                    ShareUtil.uMengShareApp(AddFriendActivity.this, SHARE_MEDIA.QQ);
                } else {
                    showToast("您尚未安装QQ，请先下载安装！");
                }
                break;

            case R.id.ll_sina:
                ShareUtil.uMengShareApp(AddFriendActivity.this, SHARE_MEDIA.SINA);
                break;

            case R.id.ll_wechat:
                if (ShareUtil.isAppAvilible(context, Constants.PACKAGE_NAME_WX)) {
                    ShareUtil.uMengShareApp(AddFriendActivity.this, SHARE_MEDIA.WEIXIN);
                } else {
                    showToast("您尚未安装微信，请先下载安装！");
                }
                break;
        }
    }

}
