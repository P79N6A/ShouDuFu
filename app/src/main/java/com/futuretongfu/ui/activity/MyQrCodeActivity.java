package com.futuretongfu.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.futuretongfu.R;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.utils.DensityUtil;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.QrCodeUtils;
import com.futuretongfu.utils.ShareUtil;
import com.futuretongfu.utils.Util;
import com.futuretongfu.view.SharePopupWindow;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 我的二维码
 *
 * @author DoneYang 2017/6/17
 */

public class MyQrCodeActivity extends BaseActivity implements SharePopupWindow.OnItemClickLister {

    @Bind(R.id.tv_title)
    TextView tv_title;

    @Bind(R.id.image_my_qr_code_head)
    ImageView image_head;

//    @Bind(R.id.iamge_myqrcode_pic)
//    ImageView image_pic;

    @Bind(R.id.activity_my_qr_code_code)
    TextView tv_code;

    @Bind(R.id.tv_myqrcode_name)
    TextView tv_name;

    @Bind(R.id.image_myqrcode_code)
    ImageView image_code;
    @Bind(R.id.bt_share)
    ImageView bt_share;

    @Bind(R.id.layout_invite_code)
    LinearLayout layout_invite_code;

    private SharePopupWindow sharePopup;

    private String userId = null;
    private String userNickName = null;
    private String userFaceUrl = null;

    private String headUrl = Constants.Url_share_QrCode + "?";
    private  Bitmap bitmap;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_qr_code;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        tv_title.setText("我的二维码");
        tv_code.setTextIsSelectable(true);
        sharePopup = new SharePopupWindow(this);
        sharePopup.setOnItemClickListener(this);
        sharePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Util.setAlpha(MyQrCodeActivity.this, 1.0f);
            }
        });
//        if (!TextUtils.isEmpty(UserManager.getInstance().getUserKey())&&Constants.User_Type_XK != UserManager.getInstance().getUserType()) {
//                headUrl += "referralCode=" + UserManager.getInstance().getUserKey() + "&";
//        }
        userId = "" + UserManager.getInstance().getUserId();
        userNickName = "" + UserManager.getInstance().getNickName();
        userFaceUrl = "" + UserManager.getInstance().getFaceUrl();
        int type = UserManager.getInstance().getUserType();
        if (Constants.User_Type_XK == type) {
            layout_invite_code.setVisibility(View.GONE);
            bt_share.setVisibility(View.GONE);
        } else {
            layout_invite_code.setVisibility(View.VISIBLE);
            bt_share.setVisibility(View.VISIBLE);
        }
        if (!Util.isEmpty(userNickName)) {
            tv_name.setText(userNickName);
        } else {
            userNickName = "" + UserManager.getInstance().getAccountNumber();
            if (!Util.isEmpty(userNickName)) {
                tv_name.setText(userNickName);
            } else {
                tv_name.setText("未知用户");
            }
        }
        if (!Util.isEmpty(userFaceUrl)) {
            GlideLoad.loadHead(userFaceUrl, image_head);
        }

        if (!Util.isEmpty(userId)) {
            //1-扫描加好友  2-付款
            bitmap = QrCodeUtils.createQRImage(headUrl+"referralCode="+UserManager.getInstance().getUserKey()+"&"+"userId="+userId, DensityUtil.dp2px(460), DensityUtil.dp2px(460));
            Log.d("hhm",headUrl + "userId=" + userId);
            image_code.setImageBitmap(bitmap);
        } else {
            showToast("获取个人信息失败");
        }
        tv_code.setText(UserManager.getInstance().getUserKey());
    }

    @OnClick({R.id.bt_back, R.id.bt_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;

            case R.id.bt_share:
                sharePopup.showAtLocation(findViewById(R.id.ll_my_qr_code_main), Gravity.BOTTOM, 0, 0);
                Util.setAlpha(this, 0.7f);
                break;
        }
    }

    @Override
    public void setOnItemClick(View view) {
        if (bitmap==null)
            return;
        switch (view.getId()) {
            case R.id.ll_wechat_moments:
                if (ShareUtil.isAppAvilible(context, Constants.PACKAGE_NAME_WX)) {
                    ShareUtil.uMengShareImage(MyQrCodeActivity.this, bitmap,SHARE_MEDIA.WEIXIN_CIRCLE);
                } else {
                    showToast("您尚未安装微信，请先下载安装！");
                }
                break;

            case R.id.ll_qq:
                if (ShareUtil.isAppAvilible(context, Constants.PACKAGE_NAME_QQ)) {
                    ShareUtil.uMengShareImage(MyQrCodeActivity.this,bitmap, SHARE_MEDIA.QQ);
                } else {
                    showToast("您尚未安装QQ，请先下载安装！");
                }
                break;
            case R.id.ll_sina:
                ShareUtil.uMengShareImage(MyQrCodeActivity.this,bitmap, SHARE_MEDIA.SINA);
                break;

            case R.id.ll_wechat:
                if (ShareUtil.isAppAvilible(context, Constants.PACKAGE_NAME_WX)) {
                    ShareUtil.uMengShareImage(MyQrCodeActivity.this,bitmap, SHARE_MEDIA.WEIXIN);
                } else {
                    showToast("您尚未安装微信，请先下载安装！");
                }
                break;
        }
    }


}
