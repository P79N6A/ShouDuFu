package com.futuretongfu.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.futuretongfu.OkUtils;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.RequestCode;
import com.futuretongfu.iview.StoreDetailsIView;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.StoreEarnPresenter;
import com.futuretongfu.utils.DensityUtil;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.QrCodeUtils;
import com.futuretongfu.R;
import com.futuretongfu.base.BaseSerializable;
import com.futuretongfu.bean.StoreDetailsBean;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.utils.SharedPreferencesUtils;
import com.futuretongfu.utils.Util;

import org.litepal.util.LogUtil;

import java.io.IOException;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 商家收款码
 *
 * @author DoneYang 2017/6/19
 */

public class StoreReceivablesCodeActivity extends BaseActivity implements StoreDetailsIView {

    @Bind(R.id.tv_title)
    TextView tv_title;

    @Bind(R.id.image_store_receivables_code_store_head)
    ImageView image_storeHead;

    @Bind(R.id.tv_store_receivables_code_store_name)
    TextView tv_storeName;

    @Bind(R.id.image_store_receivables_code_main)
    ImageView image_codeMain;

    @Bind(R.id.image_store_receivables_code_pic)
    ImageView image_pic;

    private String faceUrl = null;
    private String userId = null;
    private String userName = null;
    private Bitmap mBitmap = null;
   // private String headUrl = Constants.Url_share_QrCode + "?";
    private String headUrl=Constants.WEB_HOST;


    private StoreEarnPresenter storeEarnPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_store_receivables_code;
    }

    @Override
    protected Presenter getPresenter() {
        return storeEarnPresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        userId = "" + UserManager.getInstance().getUserId();
        if (!Util.isEmpty(userId)) {
            storeEarnPresenter = new StoreEarnPresenter(this, this);
            showProgressDialog();
            storeEarnPresenter.onStoreDetails("" + userId, "" + userId);
            tv_title.setText("商家收款码");
        }
//        userName = "" + UserManager.getInstance().getNickName();
        userName = "" + UserManager.getInstance().getNickName();
        faceUrl = "" + UserManager.getInstance().getFaceUrl();
        if (!Util.isEmpty(userId)) {
            //1-扫描加好友  2-付款
            mBitmap = QrCodeUtils.createQRImage(headUrl + "/trade/bussPay/" + userId, DensityUtil.dp2px(460), DensityUtil.dp2px(460));
            image_codeMain.setImageBitmap(mBitmap);
        } else {
            showToast(R.string.get_data_error);
        }

        if (!Util.isEmpty(faceUrl)) {
            GlideLoad.loadHead(faceUrl, image_storeHead);
        }
        if (!Util.isEmpty(userName)) {
            tv_storeName.setText(userName);
        } else {
            userName = UserManager.getInstance().getRealName();
            if (!Util.isEmpty(userName)) {
                tv_storeName.setText(userName);
            } else {
                userName = UserManager.getInstance().getAccountNumber();
                if (!Util.isEmpty(userName)) {
                    tv_storeName.setText(userName);
                }
            }
        }
    }

    @OnClick({R.id.bt_back, R.id.tv_store_receivables_code_set_money, R.id.tv_store_receivables_code_collect_record})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_back:
                finish();
                break;

            /*设置金额*/
            case R.id.tv_store_receivables_code_set_money:
                IntentUtil.startActivity(this, SetRechargeMoneyActivity.class
                        , IntentKey.WLF_BEAN, RequestCode.WLF_ACCOUNT);
                break;

            /*收款记录*/
            case R.id.tv_store_receivables_code_collect_record:
                IntentUtil.startActivity(this, MyBillActivity.class
                        , IntentKey.WLF_BEAN, "type");//类型 -收款
                break;
        }
    }

    @Override
    public void onStoreDetailsFail(int code, String msg) {
        hideProgressDialog();
      //  showToast(msg);
    }

    @Override
    public void onStoreDetailsSuccess(BaseSerializable data) {
        hideProgressDialog();
        if (!Util.isEmpty(data)) {
            GlideLoad.loadHead(((StoreDetailsBean) data).base.logoUrl, image_pic);
        } else {
            showToast("获取商家信息失败,请重新尝试");
        }
    }

    @Override
    public void onStoreBindFail(int code, String msg) {

    }

    @Override
    public void onStoreBindSuccess(String data, String data1) {

    }

}
