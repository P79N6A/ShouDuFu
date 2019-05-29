package com.futuretongfu.ui.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.futuretongfu.R;
import com.futuretongfu.base.BaseSerializable;
import com.futuretongfu.bean.HomeBannerBean;
import com.futuretongfu.bean.HomeTransactionBean;
import com.futuretongfu.bean.SystemMessageBean;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.constants.RequestCode;
import com.futuretongfu.iview.BusinessWlfBalanceIView;
import com.futuretongfu.iview.HomeIView;
import com.futuretongfu.listener.IPermissionListener;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.presenter.home.HomePresenter;
import com.futuretongfu.presenter.home.TransferFriendPresenter;
import com.futuretongfu.ui.activity.AddFriendActivity;
import com.futuretongfu.ui.activity.BillDetailActivity;
import com.futuretongfu.ui.activity.HomeImageBannerActivity;
import com.futuretongfu.ui.activity.MessageActivity;
import com.futuretongfu.ui.activity.MyQrCodeActivity;
import com.futuretongfu.ui.activity.PaymentSetMoneyActivity;
import com.futuretongfu.ui.activity.ScanAddFriendActivity;
import com.futuretongfu.ui.activity.SetRechargeMoneyActivity;
import com.futuretongfu.ui.activity.ShowWebViewActivity;
import com.futuretongfu.ui.activity.StoreReceivablesCodeActivity;
import com.futuretongfu.ui.activity.TransferMoneyActivity;
import com.futuretongfu.ui.activity.order.UploadVoucheActivity;
import com.futuretongfu.ui.activity.user.LoginActivity;
import com.futuretongfu.ui.activity.user.PersonalCenterActivity;
import com.futuretongfu.ui.component.dialog.PromptDialog;
import com.futuretongfu.utils.AppUtil;
import com.futuretongfu.utils.DateUtil;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.utils.IntentUtil;
import com.futuretongfu.utils.Logger.Logger;
import com.futuretongfu.utils.PermissionUtil;
import com.futuretongfu.utils.PromptDialogUtils;
import com.futuretongfu.utils.ShareUtil;
import com.futuretongfu.utils.StringUtil;
import com.futuretongfu.utils.Util;
import com.futuretongfu.utils.oss.OSSUtil;
import com.futuretongfu.view.SharePopupWindow;
import com.skjr.zxinglib.CaptureActivity;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;

import static android.app.Activity.RESULT_OK;

/*
 * Created by ChenXiaoPeng on 2017/6/9.
 */

public class MainFragment extends BaseFragment implements SharePopupWindow.OnItemClickLister, IPermissionListener
        , HomeIView, BGABanner.Delegate<ImageView, HomeBannerBean>, BGABanner.Adapter<ImageView, HomeBannerBean>, BusinessWlfBalanceIView
        , BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.imgv_header)
    ImageView imgv_header;

    @Bind(R.id.tv_home_message_num)
    TextView tv_messageNum;

    @Bind(R.id.image_home_menu)
    ImageView image_menu;

//    @Bind(R.id.vp_home_banner)
//    NoScrollViewPager vp_banner;

//    @Bind(R.id.ll_home_dots)
//    LinearLayout ll_dots;

    @Bind(R.id.rv_home_payment)
    RecyclerView rv_payment;

    @Bind(R.id.sr_home)
    SwipeRefreshLayout sr_home;

//    @Bind(R.id.banner_main_default)
//    BGABanner banner_main;
//
//    @Bind(R.id.image_home_banner1)
//    ImageView image_banner1;
//
//    @Bind(R.id.image_home_banner2)
//    ImageView image_banner2;
//
//    @Bind(R.id.image_home_banner)
//    ImageView image_home_banner;

    private BGABanner banner_main;
    private ImageView image_banner1;
    private ImageView image_banner2;

    private LinearLayout imageLayout;
    private ImageView image_youp;
    private ImageView image_taob;
    private ImageView image_jingd;
    private TextView textConvertJifen;  //昨日转换
 //
    private HomePresenter mPresenter;

    private TransferFriendPresenter transferFriendPresenter;

    private PopupWindow pop_menu;

    private SharePopupWindow sharePopup;

    //private List<HomeTransactionBean> transactionList = new ArrayList<>();
    private List<HomeBannerBean> bannerList = new ArrayList<>();

    private PaymentAdapter mAdapter;

    private PromptDialog mDialog;

    private int mPosition = 0;

    private String userId = null;
    private int page = 1;
    //15821830849
    //15999696850 abc123
    // 13816472021 abcd1234


    @Override
    protected Presenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mPresenter == null) {
            mPresenter = new HomePresenter(getActivity(), this);
        }

        if (transferFriendPresenter == null) {
            transferFriendPresenter = new TransferFriendPresenter(getActivity(), this);
        }

        userId = "" + UserManager.getInstance().getUserId();
        mPresenter.onBannerList();
        mPresenter.getIntegralConvert(); //昨日转换
        setRefresh();

        mAdapter = new PaymentAdapter();
        Util.setRecyclerViewLayoutManager(getActivity(), rv_payment, R.color.transparent, 18);

        mAdapter.addHeaderView(getHeadView());
        rv_payment.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //注意不同类型需要传入参数不一样
                BillDetailActivity.startActivity(getActivity(), mAdapter.getData().get(position).businessNo, mAdapter.getData().get(position).businessType);
            }
        });

        //mAdapter.setOnLoadMoreListener(this, rv_payment);

        sharePopup = new SharePopupWindow(getActivity());
        sharePopup.setOnItemClickListener(this);
        sharePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Util.setAlpha(getActivity(), 1.0f);
            }
        });

    }

    private void setBanner() {
        banner_main.setDelegate(this);
        banner_main.setAdapter(this);
    }

    //设置下拉刷新
    private void setRefresh() {
        sr_home.setColorSchemeResources(R.color.colorPrimary);
        sr_home.setOnRefreshListener(this);

        sr_home.postDelayed(new Runnable() {
            @Override
            public void run() {
                sr_home.setRefreshing(true);
                getData();
            }
        }, 800);

    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        page = 1;
        getData();
    }


    /**
     * 上拉加载
     */
    @Override
    public void onLoadMoreRequested() {
        page++;
        if (UserManager.getInstance().isLogin()) {
            mPresenter.onTransactionListMore(page, userId);
        }
    }


    /**
     * 头部
     */
    private View getHeadView() {
        View headView = getActivity().getLayoutInflater().inflate(R.layout.layout_main_head, (ViewGroup) rv_payment.getParent(), false);
        banner_main = (BGABanner) headView.findViewById(R.id.banner_main_default);
        image_banner1 = (ImageView) headView.findViewById(R.id.image_home_banner1);
        image_banner2 = (ImageView) headView.findViewById(R.id.image_home_banner2);
        imageLayout = (LinearLayout) headView.findViewById(R.id.home_youp_image_layout);
        image_youp = (ImageView) headView.findViewById(R.id.home_image_youp_view);
        image_taob = (ImageView) headView.findViewById(R.id.home_image_taob_view);
        image_jingd = (ImageView) headView.findViewById(R.id.home_image_jingd_view);
        ImageView image_home_banner = (ImageView) headView.findViewById(R.id.image_home_banner);
        TextView tv_yesterday_consume = (TextView) headView.findViewById(R.id.tv_yesterday_consume);
        TextView tv_yesterday_shop = (TextView) headView.findViewById(R.id.tv_yesterday_shop);
        textConvertJifen = (TextView) headView.findViewById(R.id.tv_yesterday_money);  //昨日转换
        setBanner();
        image_banner1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivity(getContext(), HomeImageBannerActivity.class);
            }
        });

        image_banner2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowWebViewActivity.startActivity(getActivity(), Constants.Url_Agreement_AboutUs, "关于我们", true);
            }
        });
        tv_yesterday_consume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UserManager.getInstance().isLogin()) {
                    LoginActivity.startActivity(getContext(), RequestCode.REQUEST_CODE_LOGIN_NORMAL);
                    return;
                }
                UploadVoucheActivity.startActivity(getContext(),false);
            }
        });
        tv_yesterday_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("敬请期待");
            }
        });
        image_youp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        image_taob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("敬请期待");
            }
        });

        image_jingd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("敬请期待");
            }
        });


        image_home_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), HomeImageBannerActivity.class);
                intent.putExtra("type", 1);
                getActivity().startActivity(intent);
            }
        });
        return headView;
    }

    /**
     * 获取接口数据
     */
    private void getData() {
        mPresenter.onBannerList();
        if (Util.isEmpty(userId)) {
            showToast("获取个人信息失败");
            return;
        }
        if (UserManager.getInstance().isLogin()) {
            mPresenter.onTransactionList(page, userId);
            image_banner1.setVisibility(View.GONE);
            image_banner2.setVisibility(View.GONE);
        } else {
            image_banner1.setVisibility(View.VISIBLE);
            image_banner2.setVisibility(View.VISIBLE);
        }

    }

    /*************************************************************************/
    @Override
    public void onResume() {
        super.onResume();

        if (Util.isEmpty(userId)) {
//            transactionList.clear();
//            mAdapter.notifyDataSetChanged();
            return;
        }

        if (mPresenter == null) {
            mPresenter = new HomePresenter(getActivity(), this);
            mPresenter.onSystemMessageNum(userId);
        }

        initUser();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //重新显示到最前端中
        if (!hidden) {
            initUser();
        }
    }

    /**
     * 获取用户信息
     */
    private void initUser() {
        if (!UserManager.getInstance().isLogin()) {
            return;
        }

        String userFaceUrl = "" + UserManager.getInstance().getFaceUrl();
        if (!Util.isEmpty(userFaceUrl)) {
            GlideLoad.loadCropCircleHead(userFaceUrl, imgv_header);
        }
    }

    @OnClick({R.id.image_home_menu, R.id.ll_home_scan, R.id.ll_home_transfer_money, R.id.ll_home_recharge
            , R.id.ll_home_collect_payment, R.id.rl_home_message, R.id.imgv_header})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            /*菜单*/
            case R.id.image_home_menu:
                if (!UserManager.getInstance().isLogin()) {
                    LoginActivity.startActivity(getContext(), RequestCode.REQUEST_CODE_LOGIN_NORMAL);
                    return;
                }
                showMenu();
                break;

            /*扫一扫*/
            case R.id.ll_home_scan:

                if (!UserManager.getInstance().isLogin()) {
                    LoginActivity.startActivity(getContext(), RequestCode.REQUEST_CODE_LOGIN_NORMAL);
                    return;
                }

                if (PermissionUtil.permissionCamera(getActivity())) {
                    openScan();
                }
                break;

            /*转账*/
            case R.id.ll_home_transfer_money:

                if (!UserManager.getInstance().isLogin()) {
                    LoginActivity.startActivity(getContext(), RequestCode.REQUEST_CODE_LOGIN_NORMAL);
                    return;
                }

                AppUtil.getRealNameStatus(getContext());
                int userRealNameStatus = UserManager.getInstance().getRealNameStatus();
                if (userRealNameStatus == Constants.RealNameStatus_No) {
                    PromptDialogUtils.showNotRuleNamePromptDialog(getActivity());
                } else if (userRealNameStatus == Constants.RealNameStatus_Doing) {
                    PromptDialogUtils.showRuleNameingPromptDialog(getActivity());
                } else if (userRealNameStatus == Constants.RealNameStatus_Yes) {
                    IntentUtil.startActivity(getActivity(), TransferMoneyActivity.class);
                    // transferFriendPresenter.businessIntoShow(userId);
                } else if (Constants.RealNameStatus_Faile == UserManager.getInstance().getRealNameStatus()) {
                    PromptDialogUtils.showRuleNameFailPromptDialog(getActivity());
                }
                break;

            /*充值*/
            case R.id.ll_home_recharge:

                if (!UserManager.getInstance().isLogin()) {
                    LoginActivity.startActivity(getContext(), RequestCode.REQUEST_CODE_LOGIN_NORMAL);
                    return;
                }

                AppUtil.getRealNameStatus(getContext());
                userRealNameStatus = UserManager.getInstance().getRealNameStatus();
                if (userRealNameStatus == Constants.RealNameStatus_No) {
                    PromptDialogUtils.showNotRuleNamePromptDialog(getActivity());
                } else if (userRealNameStatus == Constants.RealNameStatus_Doing) {
                    PromptDialogUtils.showRuleNameingPromptDialog(getActivity());
                } else if (Constants.RealNameStatus_Faile == UserManager.getInstance().getRealNameStatus()) {
                    PromptDialogUtils.showRuleNameFailPromptDialog(getActivity());
                }/* else if (!UserManager.getInstance().isWithdrawCard()) {
                    PromptDialogUtils.showNotBindCardPromptDialog(getActivity());
                }*/ else if (userRealNameStatus == Constants.RealNameStatus_Yes) {
                    IntentUtil.startActivity(getActivity(), SetRechargeMoneyActivity.class, IntentKey.WLF_BEAN, RequestCode.RECHARGE_MONEY);

                }

                break;

            /*收付款*/
            case R.id.ll_home_collect_payment:

                if (!UserManager.getInstance().isLogin()) {
                    LoginActivity.startActivity(getContext(), RequestCode.REQUEST_CODE_LOGIN_NORMAL);
                    return;
                }

//                userType = UserManager.getInstance().getUserType();
                if (UserManager.getInstance().isStore()) {
                    IntentUtil.startActivity(getActivity(), StoreReceivablesCodeActivity.class);
                } else {
//                    showToast("该功能只针对商家使用,您还不是商家哦~");
                    showToast("该功能只针对商家使用");
                }
                break;

            /*消息*/
            case R.id.rl_home_message:

                if (!UserManager.getInstance().isLogin()) {
                    LoginActivity.startActivity(getContext(), RequestCode.REQUEST_CODE_LOGIN_NORMAL);
                    return;
                }
                IntentUtil.startActivity(getActivity(), MessageActivity.class);
                break;

            /*头像*/
            case R.id.imgv_header:

                if (!UserManager.getInstance().isLogin()) {
                    LoginActivity.startActivity(getContext(), RequestCode.REQUEST_CODE_LOGIN_NORMAL);
                    return;
                }

                PersonalCenterActivity.startActivity(getActivity(), imgv_header);
                break;
        }
    }

    @Override
    public void setOnItemClick(View view) {
        switch (view.getId()) {
            case R.id.ll_wechat_moments:
                ShareUtil.uMengShare(getActivity(), "", SHARE_MEDIA.WEIXIN_CIRCLE);
                break;

            case R.id.ll_qq:
                ShareUtil.uMengShare(getActivity(), "", SHARE_MEDIA.QQ);
                break;

            case R.id.ll_sina:
                ShareUtil.uMengShare(getActivity(), "", SHARE_MEDIA.SINA);
                break;

            case R.id.ll_wechat:
                ShareUtil.uMengShare(getActivity(), "", SHARE_MEDIA.WEIXIN);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                    showToast("二维码错误，请重新确认后重新扫码");
                    return;
                }
                String[] codeUrl = content.split("\\?");
                if (Util.isEmpty(codeUrl) || codeUrl.length < 2) {
                    Logger.i(TAG, "code.length()=" + codeUrl.length);
                    showToast("获取扫描信息失败");
                    return;
                }
                String info = codeUrl[1];

//                if (!info.contains(",")) {
//                    showToast("二维码错误，请重新确认后重新扫码");
//                    return;
//                }
//                String[] code = info.split(",");
//                if (Util.isEmpty(code) || code.length < 2) {
//                    Logger.i(TAG, "code.length()=" + code.length);
//                    showToast("获取扫描信息失败");
//                    return;
//                }
//                String type = code[0];
//                String infoId = code[1];
//                if (Util.isEmpty(type) || Util.isEmpty(infoId)) {
//                    showToast("获取扫描信息失败");
//                    return;
//                }
//                if (type.contains("&")) {
//                    type = type.split("&")[1];
//                }
                String type = null;
                String ids = null;
                if (!info.contains("userId") && !info.contains("storeId")) {
                    showToast("获取扫描信息失败");
                    return;
                } else if (info.contains("userId")) {
                    ids = info.split("userId=")[1];
                    type = "userId";
                } else if (info.contains("storeId")) {
                    ids = info.split("storeId=")[1];
                    type = "storeId";
                }

                if ("userId".equals(type)) {
                    if (!(UserManager.getInstance().getUserId() + "").equals(ids)) {
                        IntentUtil.startActivity(getActivity(), ScanAddFriendActivity.class
                                , IntentKey.WLF_ID, ids);
                    } else {
                        showToast("不能自己添加自己");
                    }
                } else if ("storeId".equals(type)) {
                    IntentUtil.startActivity(getActivity(), PaymentSetMoneyActivity.class
                            , IntentKey.WLF_ID, ids);
                }
            }
        }
    }

    @Override
    public void onPermissionGranted(String name) {
        IntentUtil.startActivityForResult(this, CaptureActivity.class, RequestCode.REQUEST_CODE_SCAN);
    }

    @Override
    public void onPermissionDenied(String name) {
        showToast("您拒绝了「扫一扫」所需要的相关权限!");
    }

    @Override
    public void onBannerListFail(int code, String msg) {
        if (sr_home != null) {
            sr_home.setRefreshing(false);
        }
//        showToast(msg);
//        mAdapter.setEnableLoadMore(true);
    }

    @Override
    public void onBannerListSuccess(List<HomeBannerBean> data) {
        if (sr_home != null) {
            sr_home.setRefreshing(false);
        }

        if (!Util.isEmpty(data)) {
            bannerList.clear();
            bannerList.addAll(data);
            banner_main.setData(bannerList, null);
        }
    }

    //交易列表
    @Override
    public void onTransactionFail(int code, String msg) {
        if (sr_home != null) {
            sr_home.setRefreshing(false);
        }

        showToast(msg);
        image_banner1.setVisibility(View.VISIBLE);
        image_banner2.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTransactionSuccess(List<HomeTransactionBean> data) {

        if (sr_home != null) {
            sr_home.setRefreshing(false);
        }

        if (!Util.isEmpty(data) && data.size() > 0) {
            rv_payment.setVisibility(View.VISIBLE);
            mAdapter.setNewData(data);
        } else {
            if (mAdapter != null) {
                mAdapter.setNewData(new ArrayList<HomeTransactionBean>());
            }
            image_banner1.setVisibility(View.VISIBLE);
            image_banner2.setVisibility(View.VISIBLE);
        }

    }

    //加载更多
    @Override
    public void onTransactionMoreFail(int code, String msg) {
        showToast(msg);
        mAdapter.loadMoreFail();
    }

    @Override
    public void onTransactionMoreSuccess(List<HomeTransactionBean> data) {

        if (Util.isEmpty(data) || data.size() == 0) {
            mAdapter.loadMoreEnd();
            return;
        }

        int page_size = 8;
        if (data.size() < page_size) {
            mAdapter.addData(data);
            mAdapter.loadMoreEnd();
            return;
        }

        mAdapter.loadMoreComplete();
        mAdapter.loadMoreEnd();

    }

    //系统消息
    @Override
    public void onSystemMessageNumFail(int code, String msg) {
        if (sr_home != null) {
            sr_home.setRefreshing(false);
        }
        showToast(msg);
    }

    @Override
    public void onSystemMessageNumSuccess(BaseSerializable data) {
        if (sr_home != null) {
            sr_home.setRefreshing(false);
        }
        if (!Util.isEmpty(data)) {
            SystemMessageBean bean = (SystemMessageBean) data;
            if (!Util.isEmpty(bean)) {
                if (bean.list.size() != 0) {
                    tv_messageNum.setText("" + bean.list.size());
                    tv_messageNum.setVisibility(View.VISIBLE);
                } else {
                    tv_messageNum.setVisibility(View.GONE);
                }
            }
        }
    }

    //忽略此动态
    @Override
    public void onRemoveMessageFail(int code, String msg) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        showToast(msg);
    }

    @Override
    public void onRemoveMessageSuccess(BaseSerializable data) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        //transactionList.remove(mPosition);
        mAdapter.remove(mPosition);
//        if (transactionList.size() == 0) {
//            image_banner1.setVisibility(View.VISIBLE);
//            image_banner2.setVisibility(View.VISIBLE);
//        }
        if (mAdapter.getData().size() == 0) {
            image_banner1.setVisibility(View.VISIBLE);
            image_banner2.setVisibility(View.VISIBLE);
        }
    }

    //昨日转换
    @Override
    public void onIntegralConvertSuccess(String convertJifen) {
        if (null!=textConvertJifen)
            textConvertJifen.setText(StringUtil.fmtMicrometer(Double.parseDouble(convertJifen)));
    }

    @Override
    public void onIntegralConvertFaile(boolean showToast, String msg) {
        if (null!=textConvertJifen)
            textConvertJifen.setText("0.00");
    }

    @Override
    public void fillBannerItem(BGABanner banner, ImageView itemView, HomeBannerBean model, int position) {
        if (!Util.isEmpty(model.imageUrl)) {
            GlideLoad.loadBannerImage(model.imageUrl, itemView);
        }
    }

    @Override
    public void onBannerItemClick(BGABanner banner, ImageView itemView, HomeBannerBean model, int position) {
        if (!Util.isEmpty(model.url)) {
            ShowWebViewActivity.startActivity(getContext(), model.url, "最新推荐", true);
        }

    }

    //平台余额
    @Override
    public void onPaymentWlfBalanceFail(int code, String msg) {
        showToast(msg);
    }

    @Override
    public void onPaymentWlfBalanceSuccess(double money) {
        if (0 != money) {
            IntentUtil.startActivity(getActivity(), TransferMoneyActivity.class);
        } else {
            new PromptDialog
                    .Builder(getActivity())
                    .setCanceledOnTouchOutside(true)
                    .setMessage("您的余额不足,请去充值！")
                    .setButton1(R.string.confirm, new PromptDialog.OnClickListener() {
                        @Override
                        public void onClick(Dialog dialog, int which) {
                            IntentUtil.startActivity(getActivity(), SetRechargeMoneyActivity.class
                                    , IntentKey.WLF_BEAN, RequestCode.RECHARGE_MONEY);
                            dialog.dismiss();
                        }
                    }).show();
        }
    }

    public interface MainFragmentListener {
        void openQrCodeScan();
    }

    /**
     * 显示菜单
     */
    private void showMenu() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_home_menu, null);

        pop_menu = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT, true);
        Util.setAlpha(getActivity(), 0.7f);
        pop_menu.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Util.setAlpha(getActivity(), 1.0f);
            }
        });
        pop_menu.setBackgroundDrawable(new ColorDrawable());
        pop_menu.showAsDropDown(image_menu, 0, 0);

        LinearLayout ll_share = (LinearLayout) view.findViewById(R.id.ll_dialog_menu_share);
        LinearLayout ll_friend = (LinearLayout) view.findViewById(R.id.ll_dialog_menu_add_friend);
        LinearLayout ll_qrcode = (LinearLayout) view.findViewById(R.id.ll_dialog_menu_qr_code);

        ll_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop_menu.dismiss();
                sharePopup.showAtLocation(getActivity().findViewById(R.id.ll_main),
                        Gravity.BOTTOM, 50, 0);
                Util.setAlpha(getActivity(), 0.7f);
            }
        });

        ll_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivity(getActivity(), AddFriendActivity.class);
                pop_menu.dismiss();
            }
        });

        ll_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivity(getActivity(), MyQrCodeActivity.class);
                pop_menu.dismiss();
            }
        });
    }

    //扫一扫
    public void openScan() {
        IntentUtil.startActivityForResult(this, CaptureActivity.class, RequestCode.REQUEST_CODE_SCAN);
    }

    private class PaymentAdapter extends BaseQuickAdapter<HomeTransactionBean, BaseViewHolder> {

        PaymentAdapter() {
            super(R.layout.item_home_transaction_list, new ArrayList<HomeTransactionBean>());
        }

        @Override
        protected void convert(final BaseViewHolder helper, final HomeTransactionBean transactionBean) {
            helper.setText(R.id.tv_item_home_transaction_type, transactionBean.businessTypeName.replace("提现(T+0)", "提现"))
                    .setText(R.id.tv_item_home_transaction_date, DateUtil.getDateStrWithHour1(transactionBean.createTime));

            if ("0".equals(transactionBean.tradeType)) {
                helper.setText(R.id.tv_item_home_transaction_paynum, "- ￥" + StringUtil.fmtMicrometer(transactionBean.money));
            } else if ("1".equals(transactionBean.tradeType)) {
                helper.setText(R.id.tv_item_home_transaction_paynum, "+ ￥" + StringUtil.fmtMicrometer(transactionBean.money));
            } else {
                helper.setText(R.id.tv_item_home_transaction_paynum, "￥" + StringUtil.fmtMicrometer(transactionBean.money));
            }
            TextView tv_realMoney = helper.getView(R.id.tv_item_home_transaction_getnum);
            ImageView image_type = helper.getView(R.id.image_item_home_transaction_type);
            TextView tv_fee = helper.getView(R.id.tv_item_home_transaction_fee);

            helper.getView(R.id.image_item_home_transaction_remove).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPosition = helper.getAdapterPosition() - 1;
                    mDialog = new PromptDialog
                            .Builder(getActivity())
                            .setCanceledOnTouchOutside(true)
                            .setMessage("是否忽略此动态?")
                            .setButton1(R.string.cancel, new PromptDialog.OnClickListener() {
                                @Override
                                public void onClick(Dialog dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setButton2(R.string.confirm, new PromptDialog.OnClickListener() {
                                @Override
                                public void onClick(Dialog dialog, int which) {
                                    mPresenter.onRemoveMessage("" + transactionBean.id);
                                }
                            }).show();
                }
            });

            if (!Util.isEmpty(transactionBean.businessType)) {
                String URL = OSSUtil.imageBaseUrl + "/billcion/" + transactionBean.businessType.toLowerCase() + ".png";
                GlideLoad.loadCrossFadeImageView2(URL, image_type);
                tv_realMoney.setVisibility(View.GONE);
                switch (transactionBean.businessType) {
                    /*提现*/
                    case Constants.BusinessType_Tx_Str:
                        tv_realMoney.setVisibility(View.GONE);
                        if (transactionBean.fee != 0) {
                            tv_fee.setText("手续费：￥" + StringUtil.fmtMicrometer(transactionBean.fee));
                            tv_fee.setVisibility(View.VISIBLE);
                        } else {
                            tv_fee.setVisibility(View.GONE);
                        }
                        break;

                    /*收款*/
                    case Constants.BusinessType_Sk_Str:
                        //image_type.setImageResource(R.mipmap.icon_collection);
                        if (!Util.isEmpty(transactionBean.realMoney)) {
                            tv_realMoney.setText("到账金额：￥" + StringUtil.getDouble2(transactionBean.realMoney));
                            tv_realMoney.setVisibility(View.VISIBLE);
                        } else {
                            tv_realMoney.setVisibility(View.GONE);
                        }
                        if (transactionBean.fee != 0) {
                            tv_fee.setText("服务费：￥" + StringUtil.getDouble2(transactionBean.fee));
                            tv_fee.setVisibility(View.VISIBLE);
                        } else {
                            tv_fee.setVisibility(View.GONE);
                        }
                        break;

                    case Constants.BusinessType_Update_Invite_Cash:
                        helper.setText(R.id.tv_item_home_transaction_type, "推荐用户升级奖");
                        break;

                    case Constants.BusinessType_Fk_Str:
                        tv_realMoney.setVisibility(View.GONE);
                        break;


                    default:
                        tv_fee.setVisibility(View.GONE);
                        break;
                }
            }

            switch (transactionBean.businessType) {
                case Constants.BusinessType_Tk_Str://退款
                    if ("1".equals(transactionBean.status) || "Success".equals(transactionBean.status)) {
                        helper.setText(R.id.tv_item_home_transaction_result, "退款成功");
                    } else {
                        helper.setText(R.id.tv_item_home_transaction_result, "退款失败");
                    }
                    break;

                case Constants.BusinessType_Sqzr_Str://商圈转入
                    if ("1".equals(transactionBean.status) || "Success".equals(transactionBean.status)) {
                        helper.setText(R.id.tv_item_home_transaction_result, "商圈转入成功");
                    } else {
                        helper.setText(R.id.tv_item_home_transaction_result, "商圈转入失败");
                    }
                    break;

                case Constants.BusinessType_Sqzc_Str://商圈转出
                    if ("1".equals(transactionBean.status) || "Success".equals(transactionBean.status)) {
                        helper.setText(R.id.tv_item_home_transaction_result, "商圈转出成功");
                    } else {
                        helper.setText(R.id.tv_item_home_transaction_result, "商圈转出失败");
                    }
                    break;

                case Constants.BusinessType_Cz_Str://充值
                    if ("1".equals(transactionBean.status) || "Success".equals(transactionBean.status)) {
                        helper.setText(R.id.tv_item_home_transaction_result, "充值成功");
                    } else {
                        helper.setText(R.id.tv_item_home_transaction_result, "充值失败");
                    }
                    break;

                case Constants.BusinessType_Tx_Str://提现
                    if ("1".equals(transactionBean.status) || "Success".equals(transactionBean.status)) {
                        helper.setText(R.id.tv_item_home_transaction_result, "提现成功");
                    } else if ("WaitHandle".equals(transactionBean.status)) {
                        helper.setText(R.id.tv_item_home_transaction_result, "提现处理中");
                    } else if ("Fail".equals(transactionBean.status)) {
                        helper.setText(R.id.tv_item_home_transaction_result, "提现失败");
                    } else {
                        helper.setText(R.id.tv_item_home_transaction_result, transactionBean.status);
                    }
                    break;

                case Constants.BusinessType_Zz_Str2://转出 大写
                case Constants.BusinessType_Zz_Str://转出
                    if ("1".equals(transactionBean.status) || "Success".equals(transactionBean.status)) {
                        helper.setText(R.id.tv_item_home_transaction_result, "转出成功");
                    } else {
                        helper.setText(R.id.tv_item_home_transaction_result, "转出失败");
                    }
                    break;
                case Constants.BusinessType_Sk_Str://转入
                    if ("1".equals(transactionBean.status) || "Success".equals(transactionBean.status)) {
                        helper.setText(R.id.tv_item_home_transaction_result, "转入成功");
                    } else {
                        helper.setText(R.id.tv_item_home_transaction_result, "转入失败");
                    }
                    break;
                case Constants.BusinessType_OrderPayCash://订单现金支付
                    if ("1".equals(transactionBean.status)) {
                        helper.setText(R.id.tv_item_home_transaction_result, "待付款");
                    }
                    if ("2".equals(transactionBean.status)) {
                        helper.setText(R.id.tv_item_home_transaction_result, "待收货");
                    }
                    if ("3".equals(transactionBean.status)) {
                        helper.setText(R.id.tv_item_home_transaction_result, "已完成");
                    }
                    if ("4".equals(transactionBean.status)) {
                        helper.setText(R.id.tv_item_home_transaction_result, "退款中");
                    }
                    if ("5".equals(transactionBean.status)) {
                        helper.setText(R.id.tv_item_home_transaction_result, "退款成功");
                    }
                    return;
                case Constants.BusinessType_Role_Updata_Str://身份升级支付
                    if ("1".equals(transactionBean.status) || "Success".equals(transactionBean.status)) {
                        helper.setText(R.id.tv_item_home_transaction_result, "身份升级成功");
                    } else {
                        helper.setText(R.id.tv_item_home_transaction_result, "身份升级失败");
                    }
                    break;
                case Constants.BusinessType_Cash_Back_Str://返现
                    if ("1".equals(transactionBean.status) || "Success".equals(transactionBean.status)) {
                        helper.setText(R.id.tv_item_home_transaction_result, "返现成功");
                    } else {
                        helper.setText(R.id.tv_item_home_transaction_result, "返现失败");
                    }
                    break;
                default:
                    if ("1".equals(transactionBean.status) || "Success".equals(transactionBean.status)) {
                        helper.setText(R.id.tv_item_home_transaction_result, transactionBean.businessTypeName + "成功");
                    } else {
                        helper.setText(R.id.tv_item_home_transaction_result, transactionBean.businessTypeName + "失败");
                    }

                    break;


            }


        }
    }
}
