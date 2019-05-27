package com.futuretongfu.ui.fragment;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.appkefu.lib.interfaces.KFAPIs;
import com.futuretongfu.OkUtils;
import com.futuretongfu.R;
import com.futuretongfu.bean.onlinegoods.PersonOrderNumBean;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.RequestCode;
import com.futuretongfu.iview.ICheckUpgradeView;
import com.futuretongfu.iview.IMyFragmentView;
import com.futuretongfu.model.entity.User;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.MyFragmentPresenter;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.account.PaymentBalancePresenter;
import com.futuretongfu.presenter.user.BusinessUpgradePresenter;
import com.futuretongfu.ui.activity.AddFriendActivity;
import com.futuretongfu.ui.activity.JointAcencyActivity;
import com.futuretongfu.ui.activity.MyAttentionActivity;
import com.futuretongfu.ui.activity.MyCollectionActivity;
import com.futuretongfu.ui.activity.MyQrCodeActivity;
import com.futuretongfu.ui.activity.MyRecommendActivity;
import com.futuretongfu.ui.activity.UpgradeBusiness1Activity;
import com.futuretongfu.ui.activity.UpgradeBusiness2Activity;
import com.futuretongfu.ui.activity.UpgradeBusinessUpdateActivity;
import com.futuretongfu.ui.activity.UpgradeMember2Activity;
import com.futuretongfu.ui.activity.goods.SaleReturnActivity;
import com.futuretongfu.ui.activity.order.OrderConsumer2Activity;
import com.futuretongfu.ui.activity.order.OrderConsumerActivity;
import com.futuretongfu.ui.activity.order.OrderManagerActivity;
import com.futuretongfu.ui.activity.user.EditPayPasswordActivity;
import com.futuretongfu.ui.activity.user.LoginActivity;
import com.futuretongfu.ui.activity.user.PersonalCenterActivity;
import com.futuretongfu.ui.activity.user.SettingActivity;
import com.futuretongfu.utils.AppUtil;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.Logger.Logger;
import com.futuretongfu.utils.PromptDialogUtils;
import com.futuretongfu.utils.ShareUtil;
import com.futuretongfu.utils.SharedPreferencesUtils;
import com.futuretongfu.utils.Util;
import com.futuretongfu.view.SharePopupWindow;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.litepal.util.LogUtil;

import java.io.IOException;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by zhanggf on 2018/3/14.
 * 首页--我的
 */

public class PersonFragment extends BaseFragment implements IMyFragmentView, ICheckUpgradeView, SharePopupWindow.OnItemClickLister {
    @Bind(R.id.swp_list)
    public SwipeRefreshLayout swpList;

    @Bind(R.id.civ_fperson_head)
    ImageView civFpersonHead;
    @Bind(R.id.tv_fperson_name)
    TextView tvFpersonName;
    @Bind(R.id.tv_fperson_account)
    TextView tvFpersonAccount;
    @Bind(R.id.tv_fperson_member)
    TextView tvFpersonMember;
    @Bind(R.id.img_fperson_details)
    ImageView imgFpersonDetails;
    @Bind(R.id.tv_fmy_unpayment_num)
    TextView tvFmyUnpaymentNum;
    @Bind(R.id.tv_fmy_undelivery_num)
    TextView tvFmyUndeliveryNum;
    @Bind(R.id.tv_fmy_unobtain_num)
    TextView tvFmyUnobtainNum;
    @Bind(R.id.tv_fmy_unevaluate_num)
    TextView tvFmyUnvaluateNum;
    @Bind(R.id.tv_fmy_after_sales_num)
    TextView tvFmyAfterSalesNum;
    @Bind(R.id.tv_fperson_collectnum)
    TextView tvFpersonCollectnum;
    @Bind(R.id.tv_fperson_concernnum)
    TextView tvFpersonConcernnum;
    @Bind(R.id.tv_fperson_historynum)
    TextView tvFpersonHistorynum;
    @Bind(R.id.tv_fmytv_unevaluate)
    TextView tvFmytvUnevaluate;
    @Bind(R.id.text_unknown_real_name)
    public TextView textRealName;

    @Bind(R.id.layout_my_user_info)
    public RelativeLayout useInfoLayout;
    @Bind(R.id.unlogin_user)
    public RelativeLayout unlogin_user;
    @Bind(R.id.tv_fperson_upgrad_bussness)
    TextView tvFpersonUpgradBussness;
    @Bind(R.id.tv_fperson_joint_agency)
    TextView tvFpersonJointAgency;
    @Bind(R.id.tv_fperson_membership)
    TextView tv_fpersonMemberShip;
    @Bind(R.id.tv_fperson_kefu)
    TextView tv_fperson_kefu;
    @Bind(R.id.tv_fperson_erweima)
    TextView tv_fperson_erweima;

    private MyFragmentPresenter myFragmentPresenter;
    private BusinessUpgradePresenter businessUpgradePresenter;
    public UserManager userManager;
    private int type = 0;
    private SharePopupWindow sharePopup;

    @Override
    protected Presenter getPresenter() {
        return presenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_person;
    }

    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (myFragmentPresenter == null) {
            myFragmentPresenter = new MyFragmentPresenter(getActivity(), this);
        }
        if (businessUpgradePresenter == null) {
            businessUpgradePresenter = new BusinessUpgradePresenter(getActivity(), this);
        }
        userManager = UserManager.getInstance();
        swpList.setColorSchemeResources(R.color.colorPrimary);
        swpList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (UserManager.getInstance().isLogin()) {
                    myFragmentPresenter.getUserInfoByUserId();
                    myFragmentPresenter.getPersonOrderNum();
                } else {
                    swpList.setRefreshing(false);
                }
            }
        });
        initUser();
        sharePopup = new SharePopupWindow(getActivity());
        sharePopup.setOnItemClickListener(this);
        sharePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Util.setAlpha(getActivity(), 1.0f);
            }
        });
    }


    @OnClick({R.id.tv_fperson_setting, R.id.tv_fmy_unpayment, R.id.tv_fmy_undelivery, R.id.tv_fmy_unobtain,
            R.id.tv_fmytv_unevaluate, R.id.tv_fmy_after_sales, R.id.tv_fperson_tuijian, R.id.tv_fperson_coupon,
            R.id.tv_fperson_membership, R.id.tv_fperson_invitefriend, R.id.tv_fperson_moreorder,
            R.id.ll_fperson_collect, R.id.ll_fperson_concern, R.id.ll_fperson_history, R.id.rl_fperson_details,
            R.id.img_fperson_ercode, R.id.unlogin_user, R.id.tv_fperson_erweima})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_fperson_setting:   //设置
                if (isLogin())
                    SettingActivity.startActivity(getContext());
                break;
            case R.id.rl_fperson_details:   //个人详情
                if (isLogin())
                    PersonalCenterActivity.startActivity(getActivity(), civFpersonHead);
                break;
            case R.id.ll_fperson_collect:   //收藏夹
                if (isLogin())
                    MyCollectionActivity.startActivity(getContext());
                break;
            case R.id.ll_fperson_concern:
                if (isLogin())
                    MyAttentionActivity.startActivity(getContext());
                break;
            case R.id.ll_fperson_history:
                OrderConsumerActivity.startActivity(getContext());
                break;
            case R.id.tv_fperson_moreorder:  //我的订单
                if (isLogin())
                    OrderConsumer2Activity.startActivity(getContext(), 0);
                break;
            case R.id.tv_fmy_unpayment:
                if (isLogin())
                    OrderConsumer2Activity.startActivity(getContext(), 1);
                break;
            case R.id.tv_fmy_undelivery:
                if (isLogin())
                    OrderConsumer2Activity.startActivity(getContext(), 2);
                break;
            case R.id.tv_fmy_unobtain:
                if (isLogin())
                    OrderConsumer2Activity.startActivity(getContext(), 3);
                break;
            case R.id.tv_fmytv_unevaluate:
                if (isLogin())
                    OrderConsumer2Activity.startActivity(getContext(), 4);
                break;
            case R.id.tv_fmy_after_sales:
                if (isLogin())
                    SaleReturnActivity.startActivity(getContext());
                break;
            case R.id.tv_fperson_tuijian:  //我的推荐
                if (isLogin())
                    MyRecommendActivity.startActivity(getContext());
                break;
            case R.id.tv_fperson_coupon:
                showToast("优惠券");
                break;
            case R.id.tv_fperson_membership:   //会员升级
                if (isLogin()) {
                    if (UserManager.getInstance().getUserType() == Constants.User_Type_CT) {
                        showToast("您已是会员最高級別");
                    } else {
                        UpgradeMember2Activity.startActivity(getContext());
                    }
                }
                break;
            case R.id.tv_fperson_invitefriend:   //邀请有礼
                if (isLogin()) {
                    sharePopup.showAtLocation(getActivity().findViewById(R.id.swp_list), Gravity.BOTTOM, 0, 0);
                    Util.setAlpha(getActivity(), 0.7f);
//                    IntentUtil.startActivity(getActivity(), AddFriendActivity.class);
                }
                break;
            case R.id.img_fperson_ercode:   //二维码
                if (isLogin())
                    showProgressDialog();
                IntentUtil.startActivity(getActivity(), MyQrCodeActivity.class);
                break;
            case R.id.unlogin_user:   //登陆
                IntentUtil.startActivity(getContext(), LoginActivity.class);
                break;
            case R.id.tv_fperson_erweima:
                if (isLogin())
                    showProgressDialog();
                IntentUtil.startActivity(getActivity(), MyQrCodeActivity.class);
                break;
            default:


        }
    }

    //升级商家
    @OnClick(R.id.tv_fperson_upgrad_bussness)
    public void onClickUpgradeBussness() {
        if (!UserManager.getInstance().isLogin()) {
            LoginActivity.startActivity(getContext(), RequestCode.REQUEST_CODE_LOGIN_MY);
            return;
        }
        switch (UserManager.getInstance().getUserType()) {
            case Constants.User_Type_XK://享客
                PromptDialogUtils.showStoreUpgarePromptDialog(getActivity());
                break;
            case Constants.User_Type_CY:
                PromptDialogUtils.showStoreUpgarePromptDialog(getActivity());
                break;
            case Constants.User_Type_TK://拓客
                if (UserManager.getInstance().is100tk()) {
                    UpgradeBusiness1Activity.startActivity(getContext());
                } else {
                    PromptDialogUtils.showStoreUpgarePromptDialog(getActivity());

                }
                break;
            case Constants.User_Type_CK://创客
            case Constants.User_Type_CT://创投
                AppUtil.getRealNameStatus(getActivity());
                int realNameStatus = UserManager.getInstance().getRealNameStatus();
                if (realNameStatus == Constants.RealNameStatus_No) {
                    PromptDialogUtils.showNotRuleNamePromptDialog(getActivity());
                } else if (realNameStatus == Constants.RealNameStatus_Yes) {
                    tvFpersonUpgradBussness.setEnabled(false);
                    type = 1;
                    businessUpgradePresenter.checkStoreUpgrade();
                } else if (realNameStatus == Constants.RealNameStatus_Doing) {
                    PromptDialogUtils.showRuleNameingPromptDialog(getActivity());
                } else if (realNameStatus == Constants.RealNameStatus_Faile) {
                    PromptDialogUtils.showRuleNameFailPromptDialog(getActivity());
                } else if (realNameStatus == Constants.RealNameStatus_Imperfect) {
                    PromptDialogUtils.showImPerfectRuleNamePromptDialog(getActivity());
                }
                break;
        }
    }

    //联合代理
    @OnClick(R.id.tv_fperson_joint_agency)
    public void onClickJointAgency() {
        if (!UserManager.getInstance().isLogin()) {
            LoginActivity.startActivity(getContext(), RequestCode.REQUEST_CODE_LOGIN_MY);
            return;
        }
        switch (UserManager.getInstance().getUserType()) {
            case Constants.User_Type_XK://享客
            case Constants.User_Type_TK://拓客
                PromptDialogUtils.showStoreUpgarePromptDialog(getActivity());

                break;
            case Constants.User_Type_CK://创客
            case Constants.User_Type_CT://创投
                JointAcencyActivity.startActivity(getContext());
                break;
        }
    }

    //客服
    @OnClick(R.id.tv_fperson_kefu)
    public void onClickKefu() {
        if (!UserManager.getInstance().isLogin()) {
            LoginActivity.startActivity(getContext(), RequestCode.REQUEST_CODE_LOGIN_MY);
            return;
        }
        startChat();//shoudufupingtaikefugongzuozu
    }

    private void startChat() {
        KFAPIs.setTagNickname(UserManager.getInstance().getAccountNumber(), getActivity());
        KFAPIs.startChat(getActivity(),
                "shoudufupingtaikefugongzuozu", // 1. 客服工作组ID(请务必保证大小写一致)，请在管理后台分配
                "联系客服", // 2. 会话界面标题，可自定义
                null, // 3. 附加信息，在成功对接客服之后，会自动将此信息发送给客服;
                // 如果不想发送此信息，可以将此信息设置为""或者null
                false, // 4. 是否显示自定义菜单,如果设置为显示,请务必首先在管理后台设置自定义菜单,
                // 请务必至少分配三个且只分配三个自定义菜单,多于三个的暂时将不予显示
                // 显示:true, 不显示:false
                0, // 5. 默认显示消息数量
                //修改SDK自带的头像有两种方式，1.直接替换appkefu_message_toitem和appkefu_message_fromitem.xml里面的头像，2.传递网络图片自定义
                "http://admin.weikefu.net/agent_default_avatar.png",                  //6. 修改默认客服头像，如果不想修改默认头像，设置此参数为null
                "http://admin.weikefu.net/visitor_default_avatar.png",                  //7. 修改默认用户头像, 如果不想修改默认头像，设置此参数为null
                true, // 8. 默认机器人应答
                false,  //9. 是否强制用户在关闭会话的时候 进行“满意度”评价， true:是， false:否
                null);

    }

    /**
     * 是否登录
     */
    public boolean isLogin() {
        if (!UserManager.getInstance().isLogin()) {
            LoginActivity.startActivity(getContext(), RequestCode.REQUEST_CODE_LOGIN_MY);
            return false;
        }
        return true;
    }


    //实名认证 成功
    @Override
    public void onGetRealNameStatusSuccess() {
        updateRealNameStatus();
    }

    @Override
    public void onGetRealNameStatusFaile(String msg) {
        Logger.d("onGetRealNameStatusFaile", msg);
        updateRealNameStatus();

    }

    String regid;

    //获取个人信息 成功
    @Override
    public void onUpdateUserInfoSuccess() {
        swpList.setRefreshing(false);
        initUser(false);
        if (UserManager.getInstance().isLogin()) {
            regid = SharedPreferencesUtils.getString(getActivity(), "regId", "rid");
        }
        HashMap<String, String> map = new HashMap<>();
        String userId = "" + UserManager.getInstance().getUserId();
        map.put("userId", userId);
        map.put("registerId", regid + "");
        OkUtils.UploadSJ(Constants.HOST + Constants.JIGUANGTUISONG, map, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                LogUtil.d("极光", s);

            }
        });
    }

    @Override
    public void onUpdateUserInfoFaile(boolean showToast, String msg) {
        swpList.setRefreshing(false);
        if (showToast) {
            showToast(msg);
        }
    }


    @Override
    public void onPersonOrderNumSuccess(PersonOrderNumBean data) {
        if (!TextUtils.isEmpty(data.getUnpaidNum()) && !data.getUnpaidNum().equals("0")) {
            tvFmyUnpaymentNum.setVisibility(View.VISIBLE);
            tvFmyUnpaymentNum.setText(data.getUnpaidNum());  //待付款
        } else {
            tvFmyUnpaymentNum.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(data.getPaidNum()) && !data.getPaidNum().equals("0")) {
            tvFmyUndeliveryNum.setVisibility(View.VISIBLE);
            tvFmyUndeliveryNum.setText(data.getPaidNum());   //待发货数量
        } else {
            tvFmyUndeliveryNum.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(data.getReceiveNum()) && !data.getReceiveNum().equals("0")) {
            tvFmyUnobtainNum.setVisibility(View.VISIBLE);
            tvFmyUnobtainNum.setText(data.getReceiveNum());   //待收货数量
        } else {
            tvFmyUnobtainNum.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(data.getCommentNum()) && !data.getCommentNum().equals("0")) {
            tvFmyUnvaluateNum.setVisibility(View.VISIBLE);
            tvFmyUnvaluateNum.setText(data.getCommentNum());   //待评价数量
        } else {
            tvFmyUnvaluateNum.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(data.getServiceNum()) && !data.getServiceNum().equals("0")) {
            tvFmyAfterSalesNum.setVisibility(View.VISIBLE);
            tvFmyAfterSalesNum.setText(data.getServiceNum());   //售后数量
        } else {
            tvFmyAfterSalesNum.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPersonOrderNumFaile(boolean showToast, String msg) {
        showToast(msg);
    }

    /************************************************************/
    private void initUser() {
        initUser(true);
    }

    private void initUser(boolean isRequestRealNameStatue) {
        if (UserManager.getInstance().isLogin()) {
            unlogin_user.setVisibility(View.GONE);
            useInfoLayout.setVisibility(View.VISIBLE);
        } else {
            unlogin_user.setVisibility(View.VISIBLE);
            useInfoLayout.setVisibility(View.GONE);
            return;
        }
        type = 0;
        businessUpgradePresenter.checkStoreUpgrade();
        String nickName = UserManager.getInstance().getNickName().trim();
        String trueName = UserManager.getInstance().getRealName().trim();
        if (!TextUtils.isEmpty(nickName)) {
            tvFpersonName.setText(nickName);
        } else if (!TextUtils.isEmpty(trueName)) {
            tvFpersonName.setText(trueName);
        } else {
            tvFpersonName.setText("未命名");
        }
        tvFpersonAccount.setText("账号：" + UserManager.getInstance().getAccountNumber().trim());
        switch (UserManager.getInstance().getUserType()) {
            case Constants.User_Type_XK://享客
                tvFpersonMember.setText("会员等级：享客");
                break;
            case Constants.User_Type_TK://拓客
                tvFpersonMember.setText("会员等级：拓客");
                break;
            case Constants.User_Type_CK://创客
                tvFpersonMember.setText("会员等级：创客");
                break;
            case Constants.User_Type_CT://创投
                tvFpersonMember.setText("会员等级：创投");
                break;
            case Constants.User_Type_CY://创业
                tvFpersonMember.setText("会员等级：创业合伙人");
                break;
        }

        //头像
        Logger.d("GlideLoad", "getFaceUrl = " + UserManager.getInstance().getFaceUrl());
        GlideLoad.loadCrossFadeCropCircleImageView(
                UserManager.getInstance().getFaceUrl()
                , R.mipmap.icon_head_2
                , R.mipmap.icon_head_2
                , civFpersonHead);
        //更新实名认证信息
        updateRealNameStatus();

        if (isRequestRealNameStatue)
            myFragmentPresenter.getRealNameStatus();
    }

    //更新实名认证信息
    private void updateRealNameStatus() {
        if (Constants.RealNameStatus_Yes == UserManager.getInstance().getRealNameStatus()) {
            textRealName.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_user_real_name));
            textRealName.setText("已实名");
            textRealName.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorWhite));
        } else if (Constants.RealNameStatus_Doing == UserManager.getInstance().getRealNameStatus()) {
            textRealName.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_user_unknown_real_name));
            textRealName.setText("审核中");
            textRealName.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorTextNormalLight));
        } else if (Constants.RealNameStatus_Faile == UserManager.getInstance().getRealNameStatus()) {
            textRealName.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_user_unknown_real_name));
            textRealName.setText("实名未通过");
            textRealName.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorTextNormalLight));
        } else if (Constants.RealNameStatus_Imperfect == UserManager.getInstance().getRealNameStatus()) {
            textRealName.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_user_unknown_real_name));
            textRealName.setText("实名待完善");
            textRealName.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorTextNormalLight));
        } else {
            textRealName.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.shape_user_unknown_real_name));
            textRealName.setText("未实名");
            textRealName.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorTextNormalLight));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideProgressDialog();
        type = 0;
        initUser();
        if (UserManager.getInstance().isLogin()) {
            myFragmentPresenter.getUserInfoByUserId();
            myFragmentPresenter.getPersonOrderNum();
        }
    }

    @Override
    public void onCheckStoreUpSuccess(String stype) {
        tvFpersonUpgradBussness.setEnabled(true);
        switch (stype) {
            case "1":
                if (type == 1) {

                    UpgradeBusiness2Activity.startActivity(getContext());
                }
                break;
            case "2":
                if (type == 1) {
                    if (UserManager.getInstance().is100tk()) {
                        UpgradeBusiness1Activity.startActivity(getContext());
                    }
                    UpgradeBusiness1Activity.startActivity(getContext());
                }
                break;
            case "3":
                if (type == 1) {
                    PromptDialogUtils.showStoreUpdatePromptDialog(getActivity());
                }
                break;
            case "4":
                if (type == 1) {
//                    showToast("您已升级");
                    OrderManagerActivity.startActivity(getContext());
                } else {
                    showOrder(true);
                }
                break;
            default:
                break;
        }
    }

    private void showOrder(boolean isShow) {
        if (isShow) {
            tvFpersonUpgradBussness.setText("销售订单");
            tvFpersonUpgradBussness.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.icon_mypage_tool_saleorder), null, null);
            return;
        }
        tvFpersonUpgradBussness.setText("升级商家");
        tvFpersonUpgradBussness.setCompoundDrawablesWithIntrinsicBounds(null, getResources().getDrawable(R.mipmap.icon_mypage_tool_shengji), null, null);
    }

    @Override
    public void onCheckStoreUpFaile(String msg) {
        tvFpersonUpgradBussness.setEnabled(true);
        showToast(msg);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //重新显示到最前端中
        if (!hidden) {
            initUser();
            if (UserManager.getInstance().isLogin()) {
                myFragmentPresenter.getUserInfoByUserId();
                myFragmentPresenter.getPersonOrderNum();
            }
        }
    }

    @Override
    public void setOnItemClick(View view) {
        switch (view.getId()) {
            case R.id.ll_wechat_moments:
                if (ShareUtil.isAppAvilible(getActivity(), Constants.PACKAGE_NAME_WX)) {
                    ShareUtil.uMengShareApp(getActivity(), SHARE_MEDIA.WEIXIN_CIRCLE);
                } else {
                    showToast("您尚未安装微信，请先下载安装！");
                }
                break;

            case R.id.ll_qq:
                if (ShareUtil.isAppAvilible(getActivity(), Constants.PACKAGE_NAME_QQ)) {
                    ShareUtil.uMengShareApp(getActivity(), SHARE_MEDIA.QQ);
                } else {
                    showToast("您尚未安装QQ，请先下载安装！");
                }
                break;

            case R.id.ll_sina:
                ShareUtil.uMengShareApp(getActivity(), SHARE_MEDIA.SINA);
                break;

            case R.id.ll_wechat:
                if (ShareUtil.isAppAvilible(getActivity(), Constants.PACKAGE_NAME_WX)) {
                    ShareUtil.uMengShareApp(getActivity(), SHARE_MEDIA.WEIXIN);
                } else {
                    showToast("您尚未安装微信，请先下载安装！");
                }
                break;
        }
    }
}
