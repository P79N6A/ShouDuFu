package com.futuretongfu.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.futuretongfu.bean.NoticeBean;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.model.entity.MessageListInfo;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.utils.DateUtil;
import com.futuretongfu.utils.ShareUtil;
import com.futuretongfu.utils.Util;
import com.futuretongfu.view.SharePopupWindow;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.utils.Log;
import com.futuretongfu.R;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.utils.Logger.Logger;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 系统公告详情
 *
 * @author DoneYang 2017/6/16
 */

public class NoticeDetailsActivity extends BaseActivity implements SharePopupWindow.OnItemClickLister {

    @Bind(R.id.tv_title)
    TextView tv_title;

    @Bind(R.id.tv_noticedetails_title)
    TextView tv_head;

    @Bind(R.id.bt_share)
    ImageView image_share;

//    @Bind(R.id.tv_noticedetails_type)
//    TextView tv_type;

    @Bind(R.id.tv_noticedetails_date)
    TextView tv_date;

    @Bind(R.id.tv_noticedetails_content)
    TextView tv_content;

    private SharePopupWindow sharePopup;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_noticedetails;
    }

    @Override
    protected Presenter getPresenter() {
        return null;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        tv_title.setText("详情");
//        image_share.setVisibility(View.VISIBLE);
        //NoticeBean.ListBean  listBean = =(NoticeBean.ListBean)getIntent().getSerializableExtra("user");
        Intent intent = this.getIntent();
        NoticeBean.ListBean listBean =(NoticeBean.ListBean)intent.getSerializableExtra(IntentKey.Notice_Details);
        MessageListInfo messageListInfo =  (MessageListInfo)intent.getSerializableExtra(IntentKey.Message_Details);
        if(listBean != null && !TextUtils.isEmpty(listBean.getTitle())){
            tv_head.setText(listBean.getTitle());
            tv_date.setText(DateUtil.getDateStr2(listBean.getCreateTime()));
            tv_content.setText(Html.fromHtml(listBean.getContent()));
        }
        if(messageListInfo!=null &&!TextUtils.isEmpty(messageListInfo.getTitle())){
            tv_head.setText(messageListInfo.getTitle());
            tv_date.setText(messageListInfo.getCreateTime());
            tv_content.setText(Html.fromHtml(messageListInfo.getContent()));
        }

        sharePopup = new SharePopupWindow(this);
        sharePopup.setOnItemClickListener(this);
        sharePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Util.setAlpha(NoticeDetailsActivity.this, 1.0f);
            }
        });

    }

    @OnClick({R.id.bt_back, R.id.bt_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_share:
                sharePopup.showAtLocation(findViewById(R.id.ll_main),
                        Gravity.BOTTOM, 50, 0);
                Util.setAlpha(this, 0.7f);
                break;

            case R.id.bt_back:
                finish();
                break;
        }
    }

    @Override
    public void setOnItemClick(View view) {
        switch (view.getId()) {
            case R.id.ll_wechat_moments:
                if(ShareUtil.isAppAvilible(context, Constants.PACKAGE_NAME_WX)){
                    ShareUtil.uMengShareApp(NoticeDetailsActivity.this, SHARE_MEDIA.WEIXIN_CIRCLE);
                }else{
                    showToast("您尚未安装微信！");
                }
                break;

            case R.id.ll_qq:
                if(ShareUtil.isAppAvilible(context, Constants.PACKAGE_NAME_QQ)){
                    ShareUtil.uMengShareApp(NoticeDetailsActivity.this, SHARE_MEDIA.QQ);
                }else{
                    showToast("您尚未安装QQ！");
                }
                break;

            case R.id.ll_sina:
                ShareUtil.uMengShareApp(NoticeDetailsActivity.this,  SHARE_MEDIA.SINA);
                break;

            case R.id.ll_wechat:
                if(ShareUtil.isAppAvilible(context, Constants.PACKAGE_NAME_WX)){
                    ShareUtil.uMengShareApp(NoticeDetailsActivity.this, SHARE_MEDIA.WEIXIN_CIRCLE);
                }else{
                    showToast("您尚未安装微信！");
                }
                break;
        }
    }
}
