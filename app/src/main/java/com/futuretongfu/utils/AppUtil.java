package com.futuretongfu.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.futuretongfu.constants.Constants;
import com.futuretongfu.constants.IntentKey;
import com.futuretongfu.constants.RequestCode;
import com.futuretongfu.iview.IGetRealNameStatueView;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.user.GetRealNameStatuePresenter;
import com.futuretongfu.ui.activity.MainActivity;
import com.futuretongfu.ui.activity.MessageAllActivity;
import com.futuretongfu.ui.activity.MyAttentionActivity;
import com.futuretongfu.ui.activity.goods.SearchActivity;
import com.futuretongfu.ui.activity.user.EditPayPasswordActivity;
import com.futuretongfu.ui.activity.user.LoginActivity;
import com.futuretongfu.ui.component.dialog.PromptDialog;
import com.futuretongfu.R;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * App工具类
 * Created by weiang on 2017/7/19.
 */

public class AppUtil {

    //获取实名认证信息公共方法参数类型
    public final static int GET_REALNAME_INFO_YPE_CLASS = -1;//只请求数据，成功后跳转到所传递的类中（默认）
    public final static int GET_REALNAME_INFO_TYPE_NULL = 0;//只请求数据，成功后不做任何操作
    private static Context context;
    private static int type = -1;//-1 （为默认跳转到）,1（为请求后不做处理）
    private static Class calss;
    private static GetRealNameStatuePresenter realNameStatuePresenter;


    /**
     * 描述: 传递class成功后跳转
     */
    public static void getRealNameStatus(final Context context1, final Class cas) {
        type = GET_REALNAME_INFO_YPE_CLASS;
        calss = cas;
        context = context1;
        realNameStatuePresenter = new GetRealNameStatuePresenter(context, iGetRealNameStatueView);
        realNameStatuePresenter.getRealNameStatuesInfo();
    }


    /**
     * 描述: 获取最新信息 成功后不做任何操作
     */
    public static void getRealNameStatus(final Context context1) {
        type = GET_REALNAME_INFO_TYPE_NULL;
        context = context1;
        realNameStatuePresenter = new GetRealNameStatuePresenter(context, iGetRealNameStatueView);
        realNameStatuePresenter.getRealNameStatuesInfo();
    }


    /**
     * 描述: 获取最新信息,带入监听
     */
    public static void getRealNameStatus(Context context, IGetRealNameStatueView igetRealNameStatueView) {
        realNameStatuePresenter = new GetRealNameStatuePresenter(context, igetRealNameStatueView);
        realNameStatuePresenter.getRealNameStatuesInfo();
    }


    /**
     * 回调结果处理
     */
    public static IGetRealNameStatueView iGetRealNameStatueView = new IGetRealNameStatueView() {
        @Override
        public void onGetRealNameStatueSuccess() {
            switch (UserManager.getInstance().getRealNameStatus()) {
                case Constants.RealNameStatus_No:
                    if (GET_REALNAME_INFO_YPE_CLASS == type) {
                        PromptDialogUtils.showNotRuleNamePromptDialog(context);
                    }
                    break;

                //正在实名中
                case Constants.RealNameStatus_Doing:
                    if (GET_REALNAME_INFO_YPE_CLASS == type) {
                        PromptDialogUtils.showRuleNameingPromptDialog(context);
                    }
                    break;

                //已实名
                case Constants.RealNameStatus_Yes:
                    if (GET_REALNAME_INFO_YPE_CLASS == type) {
                        IntentUtil.startActivity(context, calss);
                    }
                    break;

                //实名失败
                case Constants.RealNameStatus_Faile:
                    if (GET_REALNAME_INFO_YPE_CLASS == type) {
                        PromptDialogUtils.showRuleNameFailPromptDialog(context);
                    }
                    break;
                //不完善
                case Constants.RealNameStatus_Imperfect:
                    if (GET_REALNAME_INFO_YPE_CLASS == type) {
                        PromptDialogUtils.showImPerfectRuleNamePromptDialog(context);
                    }
                    break;
            }
        }

        @Override
        public void onGetRealNameStatueFaile(String msg) {
            if (GET_REALNAME_INFO_YPE_CLASS == type) {
                Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
            }

        }
    };

    /**
     * 拨打客服
     * @param context
     */
    public static void openPhoneCustomerService(final Context context) {

        if (Util.isEmpty(Constants.Phone_Num_CustomerService)) {
            To.s("抱歉，暂无电话信息");
            return;
        }

        new PromptDialog.Builder(context)
                .setTitle("是否拨打客服热线？")
                .setButton1("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton2("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Constants.Phone_Num_CustomerService));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        dialog.dismiss();
                    }
                })
                .setButton2TextColor(context.getResources().getColor(R.color.colorPrimary))
                .show();

    }

    /**
     * 拨打客服
     * @param context
     */
    public static void openPhoneService(final Context context, final String tellphone) {

        if (Util.isEmpty(tellphone)) {
            To.s("抱歉，暂无电话信息");
            return;
        }
        new PromptDialog.Builder(context)
                .setTitle("是否拨打"+tellphone+"？")
                .setButton1("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setButton2("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tellphone));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        dialog.dismiss();
                    }
                })
                .setButton2TextColor(context.getResources().getColor(R.color.colorPrimary))
                .show();

    }

    /**
     * 支付密码对话框
     */
    public static void showPayPasswordDialog(final Context context) {
        if (!UserManager.getInstance().isHasPayPwd()) {
            if (!UserManager.getInstance().isAnswer()) {
                PromptDialogUtils.showNotSetQuestionPromptDialog(context);
            } else {
                PromptDialogUtils.showNotSetPwdPromptDialog(context, EditPayPasswordActivity.TYPE_SET_NEW_PAY_PWD);
            }
        }
    }


    /**
     * 获取ip地址
     *
     * @return
     */
    public static String getHostIP() {
        String hostIp = null;
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;// skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        hostIp = ia.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            Log.i("yao", "SocketException");
            e.printStackTrace();
        }
        return hostIp;

    }

    /**
     * 单点登录 弹窗
     */
    public static void openLoginOfflineDialog(final Context context) {
        new PromptDialog
                .Builder(context)
                .setTitle("提示")
                .setMessage("您账号在其他地方登录，请重新登录")
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .setButton1("确定", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        //清空登录数据
                        UserManager.getInstance().clean();

                        LoginActivity.startActivity(context, RequestCode.REQUEST_CODE_LOGIN_APP);
                        CacheActivityUtil.finishActivityButLast();
                        dialog.dismiss();
                    }
                })
                //.setTitleColor(Color.parseColor("#FF4848"))
                .setMessageColor(Color.parseColor("#FF4848"))
                .setButton1TextColor(Color.parseColor("#0091EE"))
                .show();
    }

    /**
     * 显示菜单
     */
    public static PopupWindow pop_menu;
    public static void showMenu(final Activity activity,View showView) {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_goods_menu, null);
        pop_menu = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT
                , ViewGroup.LayoutParams.WRAP_CONTENT, true);
        Util.setAlpha(activity, 0.7f);
        pop_menu.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Util.setAlpha(activity, 1.0f);
            }
        });
        pop_menu.setBackgroundDrawable(new ColorDrawable());
        pop_menu.showAsDropDown(showView, 0, 0);

        LinearLayout ll_message = (LinearLayout) view.findViewById(R.id.ll_dialog_menu_message);
        LinearLayout ll_home = (LinearLayout) view.findViewById(R.id.ll_dialog_menu_add_home);
        LinearLayout ll_search = (LinearLayout) view.findViewById(R.id.ll_dialog_menu_qr_search);
        LinearLayout ll_concern = (LinearLayout) view.findViewById(R.id.ll_dialog_menu_qr_concern);

        ll_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivity(activity, MessageAllActivity.class);
                pop_menu.dismiss();
            }
        });

        ll_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivity(activity, MainActivity.class, IntentKey.WLF_BEAN, Constants.Show_Home);
                pop_menu.dismiss();
            }
        });

        ll_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivity(activity, SearchActivity.class);
                pop_menu.dismiss();
            }
        });
        ll_concern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentUtil.startActivity(activity, MyAttentionActivity.class);
                pop_menu.dismiss();
            }
        });
    }



}
