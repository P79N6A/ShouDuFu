package com.futuretongfu.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.futuretongfu.bean.Vip_Bean;
import com.futuretongfu.bean.entity.RegistUserTypeBean;
import com.futuretongfu.iview.IMemberUpgradeView;
import com.futuretongfu.model.entity.WareHorseAddressEntity;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.user.MemberUpgradePresenter;
import com.futuretongfu.ui.component.dialog.RegistAddressDialog;
import com.futuretongfu.ui.component.dialog.RegistUserTypeDialog;
import com.futuretongfu.utils.AppUtil;
import com.futuretongfu.utils.PromptDialogUtils;
import com.futuretongfu.R;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.model.entity.MemberListInfo;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.utils.CacheActivityUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 会员升级 界面2
 *
 * @author ChenXiaoPeng
 */
public class UpgradeMember2Activity extends BaseActivity implements IMemberUpgradeView {

    @Bind(R.id.tv_title)
    public TextView textTitle;

    @Bind(R.id.no_data_layout)
    public View viewError;
    @Bind(R.id.view_content)
    public NestedScrollView viewContent;

    @Bind(R.id.text_tk)
    public TextView textTk;
    /*@Bind(R.id.text_ck)
    public TextView textCk;
    @Bind(R.id.text_ct)
    public TextView textCt;
    @Bind(R.id.text_cy)
    public TextView textCY;*/

    @Bind(R.id.text_tip_1)
    public TextView textTip1;
    /*@Bind(R.id.text_tip_2)
    public TextView textTip2;
    @Bind(R.id.text_tip_3)
    public TextView textTip3;
    @Bind(R.id.text_tip_4)
    public TextView textTip4;
    @Bind(R.id.text_tip_5)
    public TextView textTip5;
    @Bind(R.id.text_tip_6)
    public TextView textTip6;*/

    @Bind(R.id.btn_confirm)
    public Button btnConfirm;
    @Bind(R.id.checkbox_agreement)
    public AppCompatCheckBox checkAgreement;
    private MemberUpgradePresenter memberUpgradePresenter;
    private int selectUserType = -1;
    private double fee_type2 = -1;
    private double fee_type3 = -1;
    private double fee_type4 = -1;
    private double fee_type0 = -1;
    private String name_type2 = "";
    private String name_type3 = "";
    private String name_type4 = "";
    private String name_type0 = "";

    private double platform_balance = -1;
    String receiverName="",receiverMobile="",receiverAddress="";
    /***********************************************************************/
    @Override
    protected int getLayoutId() {
        return R.layout.activity_upgrade_member2;
    }

    @Override
    protected Presenter getPresenter() {
        return memberUpgradePresenter;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        CacheActivityUtil.addActivityWithClear(this);
//        CacheActivityUtil.addNewActivity(this);
        textTitle.setText("会员升级");
        memberUpgradePresenter = new MemberUpgradePresenter(this, this);
        Intent intent = getIntent();
        if (intent != null) {
            platform_balance = intent.getDoubleExtra(IntentKey.PLATFORM_BALANCE, 0);
        }
        viewError.setVisibility(View.VISIBLE);
        viewContent.setVisibility(View.GONE);
        onResponseBtn(Constants.User_Type_TK);
        memberUpgradePresenter.getMemberList(UserManager.getInstance().getUserId() + "");
        //TODO  隐藏升级赠送，收货地址
        memberUpgradePresenter.getWareAddressList(UserManager.getInstance().getUserId() + "");
        //TODO  刷新会员级别
       memberUpgradePresenter.getMemberVip();
        Intent intent1 = new Intent();
        intent1.setAction(Constants.REQUEST_CODE_UPGRADE);
        sendBroadcast(intent1);
    }

    /***********************************************************************/
    @OnClick(R.id.bt_back)
    public void onClickBack() {
        finish();
    }

    //会员协议
    @OnClick(R.id.text_agreement)
    public void onClickAgreement() {
        ShowWebViewActivity.startActivity(this, Constants.Url_Agreement_RegistProtocol, "首都富用户协议", true);
    }

    @OnClick(R.id.text_tk)
    public void onClickTk() {
        onResponseBtn(Constants.User_Type_TK);
//        if (-1 == fee_type2) {
//            btnConfirm.setEnabled(false);
//        }else{
//            btnConfirm.setEnabled(true);
//        }
    }

    /*@OnClick(R.id.text_ck)
    public void onClickCk() {
        onResponseBtn(Constants.User_Type_CK);
//        if (-1 == fee_type3) {
//            btnConfirm.setEnabled(false);
//        }else{
//            btnConfirm.setEnabled(true);
//        }
    }*/

    /*@OnClick(R.id.text_ct)
    public void onClickCt() {
        onResponseBtn(Constants.User_Type_CT);
//        if (-1 == fee_type4) {
//            btnConfirm.setEnabled(false);
//        }else{
//            btnConfirm.setEnabled(true);
//        }
    }*/

    //创业
   /* @OnClick(R.id.text_cy)
    public void onClickCy() {
        onResponseBtn(Constants.User_Type_CY);
    }

    //申请联合代理
    @OnClick(R.id.text_tip_4)
    public void onApply() {
        AppUtil.openPhoneCustomerService(this);
    }*/


    @OnClick(R.id.btn_confirm)
    public void onClickConfirm() {
        if (!checkAgreement.isChecked()) {
            showToast("请先同意协议");
            return;
        }

        if (-1 == selectUserType)
            return;

        //TODO
        RegistAddressDialog registAddressDialog = new RegistAddressDialog(this,receiverName,receiverMobile,receiverAddress, new RegistAddressDialog.OnPhoneConfirmListener() {
            @Override
            public void onSelectorAdddress(String name, String phone, String address) {
                receiverName = name;
                receiverMobile = phone;
                receiverAddress = address;
                showProgressDialog();
                memberUpgradePresenter.getCommitAddress(UserManager.getInstance().getUserId()+"",name,phone,address);
            }
        });
        registAddressDialog.show();
//        intoActivity();
        //TODO 不需要实名
//        showProgressDialog();
//        memberUpgradePresenter.getRealNameStatus();//判断实名
    }

    private void intoActivity() {
        Intent intent = new Intent(UpgradeMember2Activity.this, PayDetailsActivity.class);
        intent.putExtra(IntentKey.PLATFORM_BALANCE, platform_balance);
        intent.putExtra(IntentKey.UP_SELECT_TYPE, selectUserType);
        switch (selectUserType) {
            case 2:
                intent.putExtra(IntentKey.UP_NAME_VIP, name_type2);
                intent.putExtra(IntentKey.UP_FREE_VIP, fee_type2);
                break;
            case 3:
                intent.putExtra(IntentKey.UP_NAME_VIP, name_type3);
                intent.putExtra(IntentKey.UP_FREE_VIP, fee_type3);
                break;
            case 4:
                intent.putExtra(IntentKey.UP_NAME_VIP, name_type4);
                intent.putExtra(IntentKey.UP_FREE_VIP, fee_type4);
                break;
            case 0:
                intent.putExtra(IntentKey.UP_NAME_VIP, name_type0);
                intent.putExtra(IntentKey.UP_FREE_VIP, fee_type0);
                break;
        }
        startActivity(intent);
    }

    /***********************************************************************/
    @Override
    public void onMemberListViewFaile(String msg) {
        showToast(msg);
        viewContent.setVisibility(View.GONE);
        viewError.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMemberVpi(String bean) {
        ///String data = bean.getData();
        textTip1.setText(bean);
    }

    @Override
    public void onMemberListViewSuccess(List<MemberListInfo> datas) {
        if (!datas.isEmpty()) {
            for (MemberListInfo memberListInfo : datas) {
                switch (memberListInfo.getUserType()) {
                    case 2:
                        fee_type2 = memberListInfo.getFee();
                        name_type2 = memberListInfo.getName();
                        break;
                    case 3:
                        fee_type3 = memberListInfo.getFee();
                        name_type3 = memberListInfo.getName();
                        break;
                    case 4:
                        fee_type4 = memberListInfo.getFee();
                        name_type4 = memberListInfo.getName();
                        break;
                    case 0:
                        fee_type0 = memberListInfo.getFee();
                        name_type0 = memberListInfo.getName();
                        break;
                }
            }
        }
        viewContent.setVisibility(View.VISIBLE);
        viewError.setVisibility(View.GONE);
        onResponseBtn(Constants.User_Type_TK);
        //TODO
        memberUpgradePresenter.getRegistUserList();
    }

    @Override
    public void onMemberUpFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
    }

    @Override
    public void onMemberUpSuccess(String msg) {
        hideProgressDialog();
        showToast(msg);
        UserManager.getInstance().setUserType(selectUserType);
        UserManager.getInstance().save();
        delayFinish(200);
    }

    //还未实名认证
    @Override
    public void onGetRealNameStatusFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
    }

    //已经实名认证
    @Override
    public void onGetRealNameStatusSuccess() {
        hideProgressDialog();
        if (UserManager.getInstance().getRealNameStatus() == Constants.RealNameStatus_No) {
            PromptDialogUtils.showNotRuleNamePromptDialog(this);
            return;
        }

        if (UserManager.getInstance().getRealNameStatus() == Constants.RealNameStatus_Doing) {
            PromptDialogUtils.showRuleNameingPromptDialog(this);
            return;
        }

        if (UserManager.getInstance().getRealNameStatus() == Constants.RealNameStatus_Faile) {
            PromptDialogUtils.showRuleNameFailPromptDialog(this);
            return;
        }

        Intent intent = new Intent(UpgradeMember2Activity.this, PayDetailsActivity.class);
        intent.putExtra(IntentKey.PLATFORM_BALANCE, platform_balance);
        intent.putExtra(IntentKey.UP_SELECT_TYPE, selectUserType);
        switch (selectUserType) {
            case 2:
                intent.putExtra(IntentKey.UP_NAME_VIP, name_type2);
                intent.putExtra(IntentKey.UP_FREE_VIP, fee_type2);
                break;
            case 3:
                intent.putExtra(IntentKey.UP_NAME_VIP, name_type3);
                intent.putExtra(IntentKey.UP_FREE_VIP, fee_type3);
                break;
            case 4:
                intent.putExtra(IntentKey.UP_NAME_VIP, name_type4);
                intent.putExtra(IntentKey.UP_FREE_VIP, fee_type4);
                break;
        }
        startActivity(intent);
    }

    @Override
    public void onCommitAddressFaile(String msg) {
        hideProgressDialog();
        showToast(msg);
    }

    @Override
    public void onCommitAddressSuccess() {
        hideProgressDialog();
        intoActivity();
    }

    @Override
    public void onWareAddressSuccess(WareHorseAddressEntity datas) {
        if (null!=datas){
            receiverName = datas.getReceiverName();
            receiverMobile = datas.getReceiverMobile();
            receiverAddress = datas.getReceiverAddress();
        }
    }

    @Override
    public void onWareAddressFaile(String msg) {
            showToast(msg);
    }
    //会员等级说明   弹窗
    @Override
    public void onUserTypeSuccess(List<RegistUserTypeBean> data) {
        RegistUserTypeDialog registUserTypeDialog = new RegistUserTypeDialog(this, data);
        //registUserTypeDialog.show();
    }

    @Override
    public void onUserTypeFaile(String msg) {
        showToast(msg);
    }

    @Override
    public void onFirmOrderFail(String msg) {

    }

    @Override
    public void onFirmOrderSuccess(String result) {

    }

    @Override
    public void onPaymentFail(int code, String msg) {

    }

    @Override
    public void onPaymentSuccess(String data) {

    }

    /***********************************************************************/

    private void onResponseBtn(int userType) {
        selectUserType = userType;
        /*updateUserTypeBtn(userType);
        updateTip(userType);*/
        updateBtnConfirm(userType);
    }

    /*private void updateUserTypeBtn(int userType) {
        switch (userType) {
            case Constants.User_Type_TK://拓客
//                if (fee_type2 > platform_balance) {
//                    showDialog();
//                    return;
//                }
                textTk.setSelected(true);
                textCk.setSelected(false);
                textCt.setSelected(false);
                textCY.setSelected(false);
                break;

            case Constants.User_Type_CK://创客
//                if (fee_type3 > platform_balance) {
//                    showDialog();
//                    return;
//                }
                textTk.setSelected(false);
                textCk.setSelected(true);
                textCt.setSelected(false);
                textCY.setSelected(false);
                break;
            case Constants.User_Type_CY://创业
                textTk.setSelected(false);
                textCk.setSelected(false);
                textCt.setSelected(false);
                textCY.setSelected(true);
                break;
            case Constants.User_Type_CT://创投

                textTk.setSelected(false);
                textCk.setSelected(false);
                textCt.setSelected(true);
                textCY.setSelected(false);
                break;
        }
    }*/

    /*private void updateTip(int userType) {
        switch (userType) {
            //拓客
            case Constants.User_Type_TK: {
                Drawable image = ContextCompat.getDrawable(this, R.mipmap.icon_return1);
                image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());
                textTip1.setCompoundDrawables(image, null, null, null);
                textTip1.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_upgrade_member_tip_bg_blue));
                textTip1.setTextColor(ContextCompat.getColor(this, R.color.color_user_type_up_mem_select_blue));
                textTip1.setText(R.string.user_type_tip_11);

                image = ContextCompat.getDrawable(this, R.mipmap.icon_recommend1);
                image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());
                textTip2.setCompoundDrawables(image, null, null, null);
                textTip2.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_upgrade_member_tip_bg_blue));
                textTip2.setTextColor(ContextCompat.getColor(this, R.color.color_user_type_up_mem_select_blue));

                image = ContextCompat.getDrawable(this, R.mipmap.icon_apply1);
                image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());
                textTip3.setCompoundDrawables(image, null, null, null);
                textTip3.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_upgrade_member_tip_bg_gray));
                textTip3.setTextColor(ContextCompat.getColor(this, R.color.color_user_type_up_mem_un_select));

                image = ContextCompat.getDrawable(this, R.mipmap.icon_partner1);
                image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());
                textTip4.setCompoundDrawables(image, null, null, null);
                textTip4.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_upgrade_member_tip_bg_gray));
                textTip4.setTextColor(ContextCompat.getColor(this, R.color.color_user_type_up_mem_un_select));

                image = ContextCompat.getDrawable(this, R.mipmap.icon_advertisement1);
                image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());
                textTip5.setCompoundDrawables(image, null, null, null);
                textTip5.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_upgrade_member_tip_bg_gray));
                textTip5.setTextColor(ContextCompat.getColor(this, R.color.color_user_type_up_mem_un_select));

                image = ContextCompat.getDrawable(this, R.mipmap.icon_award1);
                image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());
                textTip6.setCompoundDrawables(image, null, null, null);
                textTip6.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_upgrade_member_tip_bg_gray));
                textTip6.setTextColor(ContextCompat.getColor(this, R.color.color_user_type_up_mem_un_select));

                break;
            }

            //创客
            case Constants.User_Type_CK: {
                Drawable image = ContextCompat.getDrawable(this, R.mipmap.icon_return2);
                image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());
                textTip1.setCompoundDrawables(image, null, null, null);
                textTip1.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_upgrade_member_tip_bg_yellow));
                textTip1.setTextColor(ContextCompat.getColor(this, R.color.color_user_type_up_mem_select_yellow));
                textTip1.setText(R.string.user_type_tip_12);

                image = ContextCompat.getDrawable(this, R.mipmap.icon_recommend2);
                image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());
                textTip2.setCompoundDrawables(image, null, null, null);
                textTip2.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_upgrade_member_tip_bg_yellow));
                textTip2.setTextColor(ContextCompat.getColor(this, R.color.color_user_type_up_mem_select_yellow));

                image = ContextCompat.getDrawable(this, R.mipmap.icon_apply2);
                image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());
                textTip3.setCompoundDrawables(image, null, null, null);
                textTip3.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_upgrade_member_tip_bg_yellow));
                textTip3.setTextColor(ContextCompat.getColor(this, R.color.color_user_type_up_mem_select_yellow));

                image = ContextCompat.getDrawable(this, R.mipmap.icon_partner2);
                image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());
                textTip4.setCompoundDrawables(image, null, null, null);
                textTip4.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_upgrade_member_tip_bg_yellow));
                textTip4.setTextColor(ContextCompat.getColor(this, R.color.color_user_type_up_mem_select_yellow));

                image = ContextCompat.getDrawable(this, R.mipmap.icon_advertisement2);
                image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());
                textTip5.setCompoundDrawables(image, null, null, null);
                textTip5.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_upgrade_member_tip_bg_yellow));
                textTip5.setTextColor(ContextCompat.getColor(this, R.color.color_user_type_up_mem_select_yellow));

                image = ContextCompat.getDrawable(this, R.mipmap.icon_award1);
                image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());
                textTip6.setCompoundDrawables(image, null, null, null);
                textTip6.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_upgrade_member_tip_bg_gray));
                textTip6.setTextColor(ContextCompat.getColor(this, R.color.color_user_type_up_mem_un_select));

                break;
            }
            //创业
            case Constants.User_Type_CY: {
                Drawable image = ContextCompat.getDrawable(this, R.mipmap.icon_return2);
                image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());
                textTip1.setCompoundDrawables(image, null, null, null);
                textTip1.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_upgrade_member_tip_bg_yellow));
                textTip1.setTextColor(ContextCompat.getColor(this, R.color.color_user_type_up_mem_select_purple));
                textTip1.setText(R.string.user_type_tip_12);

                image = ContextCompat.getDrawable(this, R.mipmap.icon_recommend2);
                image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());
                textTip2.setCompoundDrawables(image, null, null, null);
                textTip2.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_upgrade_member_tip_bg_yellow));
                textTip2.setTextColor(ContextCompat.getColor(this, R.color.color_user_type_up_mem_select_purple));

                image = ContextCompat.getDrawable(this, R.mipmap.icon_apply2);
                image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());
                textTip3.setCompoundDrawables(image, null, null, null);
                textTip3.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_upgrade_member_tip_bg_yellow));
                textTip3.setTextColor(ContextCompat.getColor(this, R.color.color_user_type_up_mem_select_purple));

                image = ContextCompat.getDrawable(this, R.mipmap.icon_partner2);
                image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());
                textTip4.setCompoundDrawables(image, null, null, null);
                textTip4.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_upgrade_member_tip_bg_yellow));
                textTip4.setTextColor(ContextCompat.getColor(this, R.color.color_user_type_up_mem_select_purple));

                image = ContextCompat.getDrawable(this, R.mipmap.icon_advertisement2);
                image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());
                textTip5.setCompoundDrawables(image, null, null, null);
                textTip5.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_upgrade_member_tip_bg_yellow));
                textTip5.setTextColor(ContextCompat.getColor(this, R.color.color_user_type_up_mem_select_purple));

                image = ContextCompat.getDrawable(this, R.mipmap.icon_award1);
                image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());
                textTip6.setCompoundDrawables(image, null, null, null);
                textTip6.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_upgrade_member_tip_bg_gray));
                textTip6.setTextColor(ContextCompat.getColor(this, R.color.color_user_type_up_mem_un_select));

                break;
            }
            //创投
            case Constants.User_Type_CT: {
                Drawable image = ContextCompat.getDrawable(this, R.mipmap.icon_return3);
                image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());
                textTip1.setCompoundDrawables(image, null, null, null);
                textTip1.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_upgrade_member_tip_bg_red));
                textTip1.setTextColor(ContextCompat.getColor(this, R.color.color_user_type_up_mem_select_red));
                textTip1.setText(R.string.user_type_tip_13);

                image = ContextCompat.getDrawable(this, R.mipmap.icon_recommend3);
                image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());
                textTip2.setCompoundDrawables(image, null, null, null);
                textTip2.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_upgrade_member_tip_bg_red));
                textTip2.setTextColor(ContextCompat.getColor(this, R.color.color_user_type_up_mem_select_red));

                image = ContextCompat.getDrawable(this, R.mipmap.icon_apply3);
                image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());
                textTip3.setCompoundDrawables(image, null, null, null);
                textTip3.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_upgrade_member_tip_bg_red));
                textTip3.setTextColor(ContextCompat.getColor(this, R.color.color_user_type_up_mem_select_red));

                image = ContextCompat.getDrawable(this, R.mipmap.icon_partner3);
                image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());
                textTip4.setCompoundDrawables(image, null, null, null);
                textTip4.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_upgrade_member_tip_bg_red));
                textTip4.setTextColor(ContextCompat.getColor(this, R.color.color_user_type_up_mem_select_red));

                image = ContextCompat.getDrawable(this, R.mipmap.icon_advertisement3);
                image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());
                textTip5.setCompoundDrawables(image, null, null, null);
                textTip5.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_upgrade_member_tip_bg_red));
                textTip5.setTextColor(ContextCompat.getColor(this, R.color.color_user_type_up_mem_select_red));

                image = ContextCompat.getDrawable(this, R.mipmap.icon_award2);
                image.setBounds(0, 0, image.getMinimumWidth(), image.getMinimumHeight());
                textTip6.setCompoundDrawables(image, null, null, null);
                textTip6.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_upgrade_member_tip_bg_red));
                textTip6.setTextColor(ContextCompat.getColor(this, R.color.color_user_type_up_mem_select_red));

                break;
            }

        }
    }*/

    private void updateBtnConfirm(int userType) {
        if (userType>0&&UserManager.getInstance().getUserType() >= userType) {
            btnConfirm.setEnabled(false);
            return;
        }
        btnConfirm.setEnabled(true);
    }

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, UpgradeMember2Activity.class);
        context.startActivity(intent);
    }

}
