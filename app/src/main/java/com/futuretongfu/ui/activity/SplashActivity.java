package com.futuretongfu.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.futuretongfu.OkUtils;
import com.futuretongfu.WeiLaiFuApplication;
import com.futuretongfu.constants.RequestCode;
import com.futuretongfu.iview.ISplashView;
import com.futuretongfu.model.manager.SysDataManager;
import com.futuretongfu.model.manager.UserManager;
import com.futuretongfu.presenter.user.SplashPresenter;
import com.futuretongfu.ui.activity.user.GestureLockLoginActivity;
import com.futuretongfu.ui.activity.user.LoginActivity;
import com.futuretongfu.ui.component.dialog.ProgressDialog;
import com.futuretongfu.ui.component.dialog.PromptDialog;
import com.futuretongfu.utils.AppUtil;
import com.futuretongfu.utils.CacheActivityUtil;
import com.futuretongfu.utils.HyOkHttpUtils;
import com.futuretongfu.utils.PermissionUtil;
import com.futuretongfu.utils.PromptDialogUtils;
import com.futuretongfu.utils.TimerUtil;
import com.futuretongfu.R;
import com.futuretongfu.iview.IUserLoginOffline;
import com.futuretongfu.ui.component.dialog.UpdateAppDialog;
import com.futuretongfu.utils.Logger.Logger;
import com.futuretongfu.utils.To;
import com.tbruyelle.rxpermissions.RxPermissions;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class SplashActivity extends Activity implements ISplashView, IUserLoginOffline {

    private SplashPresenter splashPresenter;
    protected ACProgressFlower progressDialog;
    //private String appUrl = "http://futuredf.oss-cn-shanghai.aliyuncs.com/apk/%E6%9C%AA%E6%9D%A5%E9%80%9A%E4%BB%98_v1.0.3.apk";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_splash);
        new RxPermissions(this);
        //禁止横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        splashPresenter = new SplashPresenter(this, this);

        TimerUtil.startTimer(400, new TimerUtil.TimerCallBackListener2() {
            @Override
            public void onEnd() {
                if (PermissionUtil.permissionReadStorage(SplashActivity.this)) {
                    splashPresenter.getApp();
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        CacheActivityUtil.addActivity(this);
        WeiLaiFuApplication.initUserLoginOffline(this);//注册 单点登录 监控器
    }

    @Override
    public void onPause() {
        super.onPause();
        WeiLaiFuApplication.relaseUserLoginOffline();//释放 单点登录 监控器
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CacheActivityUtil.removeActivity(this);
    }

    /**************************************************/
    @Override
    public void onSplashUpdateAppYes(boolean isForce, String url) {
        if(TextUtils.isEmpty(url)){
            noUpdateApp();
            return;
        }
        //是否符合更新条件
        /*if (isForce) {
            //updateAppForce(url);
            return;
        }*/
        //不强制更新
        updateAppFree(url);
    }

    @Override
    public void onSplashUpdateAppNo() {
        noUpdateApp();
        //updateAppFree(appUrl);
    }

    @Override
    public void onAutoLoginFinish() {
        Logger.d("GestureLockLoginActivity", "isHasGesturePwd = " + UserManager.getInstance().isHasGesturePwd());

        if (UserManager.getInstance().isHasGesturePwd()) {
            GestureLockLoginActivity.startActivity(SplashActivity.this);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            TimerUtil.startTimer(400, new TimerUtil.TimerCallBackListener2() {
                @Override
                public void onEnd() {
                    finish();
                }
            });
        } else {
            startMainActivity();
        }

    }

    /**************************************************/

    private void noUpdateApp() {
        if (SysDataManager.getInstance().isFirstGuide()) {
            SysDataManager.getInstance().setFirstGuide(false);
            SysDataManager.getInstance().save();

            WelcomeGuideActivity.startActivity(this);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            TimerUtil.startTimer(400, new TimerUtil.TimerCallBackListener2() {
                @Override
                public void onEnd() {
                    finish();
                }
            });

            return;
        }

        //用户已登录过
        if (UserManager.getInstance().isLogin()) {
            splashPresenter.getRealNameStatuesInfo();
            return;
        }

        //用户没有登录
        LoginActivity.startActivity(this, RequestCode.REQUEST_CODE_LOGIN_APP);
//        MainActivity.startActivity(this, false);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        TimerUtil.startTimer(800, new TimerUtil.TimerCallBackListener2() {
            @Override
            public void onEnd() {
                finish();
            }
        });
    }

    //不强制更新
    private void updateAppFree(final String url) {
        new PromptDialog.Builder(this)
                .setTitle("版本更新")
                .setMessage("有新版本您需要更新吗？")
                .setButton1("取消", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();
                        noUpdateApp();
                    }
                })
                .setButton2("更新", new PromptDialog.OnClickListener() {
                    @Override
                    public void onClick(Dialog dialog, int which) {
                        dialog.dismiss();

                        UpdateAppDialog UpdateAppDialog = new UpdateAppDialog(
                                SplashActivity.this
                                , false, url
                                , new UpdateAppDialog.UpdateAppDialogListener() {
                            @Override
                            public void onUpdateAppDialogCancel(boolean isF) {
                                noUpdateApp();
                            }

                            @Override
                            public void onUpdateAppDialogUpdate(boolean isF) {
                                if(isF){
                                    TimerUtil.startTimer(100, new TimerUtil.TimerCallBackListener2() {
                                        @Override
                                        public void onEnd() {
                                            finish();
                                        }
                                    });
                                }else{
                                    noUpdateApp();
                                }
                            }
                        }
                        );

                        UpdateAppDialog.show();

                    }
                })
                .show();
    }

    //强制更新
    private void updateAppForce(final String url) {
        UpdateAppDialog UpdateAppDialog = new UpdateAppDialog(
                SplashActivity.this
                , true, url
                , new UpdateAppDialog.UpdateAppDialogListener() {
            @Override
            public void onUpdateAppDialogCancel(boolean isForce) {
                TimerUtil.startTimer(100, new TimerUtil.TimerCallBackListener2() {
                    @Override
                    public void onEnd() {
                        finish();
                    }
                });
            }

            @Override
            public void onUpdateAppDialogUpdate(boolean isForce) {
                if(isForce){
                    TimerUtil.startTimer(100, new TimerUtil.TimerCallBackListener2() {
                        @Override
                        public void onEnd() {
                            finish();
                        }
                    });
                }else{
                    noUpdateApp();
                }

            }
        }
        );

        UpdateAppDialog.show();
    }

    @Override
    public void onUserLoginOffline() {
        AppUtil.openLoginOfflineDialog(this);
    }

    private void startMainActivity() {

        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        TimerUtil.startTimer(800, new TimerUtil.TimerCallBackListener2() {
            @Override
            public void onEnd() {
                finish();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (PermissionUtil.isReadStoragePermission(permissions)) {
            if (!PermissionUtil.getPermissionReadStorageResult(permissions, grantResults)) {
                To.s("存储权限被禁止");
            }else {
                splashPresenter.getApp();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
    public void showProgressDialog(String msg) {
        progressDialog = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text(msg)
                .fadeColor(Color.DKGRAY).build();

        progressDialog.show();
    }

}
