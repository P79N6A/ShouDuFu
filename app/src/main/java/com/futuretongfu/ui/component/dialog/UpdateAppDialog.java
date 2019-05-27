package com.futuretongfu.ui.component.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.futuretongfu.utils.HyOkHttpUtils;
import com.futuretongfu.utils.NumberView1;
import com.futuretongfu.utils.StringUtil;
import com.futuretongfu.R;
import com.vector.update_app.view.NumberProgressBar;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


/**
 * APP 更新对话框
 * Created by ChenXiaoPeng on 2017/7/16.
 */

public class UpdateAppDialog extends Dialog {

    @Bind(R.id.content)
    NumberView1 contentProgressBar;
    @Bind(R.id.title)
    public TextView textTitle;
    @Bind(R.id.bt_confirm)
    TextView btConfirm;
    private static final String wlfProvider = "com.futuretongfu.WlfProvider";
    private Context context;
    private String url;
    private boolean isForce;
    private AsyncTask<String, Integer, Integer> task;
    private File file;
    @Bind(R.id.mysendpro)
    TextView mprotv;
    int secondaryProgress;

    public UpdateAppDialog(Context context, boolean isForce, String url, UpdateAppDialogListener updateAppDialogListener) {
        super(context, R.style.FloatDialog);
        this.isForce = isForce;
        this.url = url;
        this.updateAppDialogListener = updateAppDialogListener;
        this.context = context;

        setCancelable(false);

        setContentView(R.layout.dialog_update_app);

        ButterKnife.bind(this);
    }

    @Override
    public void show() {
        if (!TextUtils.isEmpty(url)) {
            download();
        }
        super.show();
    }
    private void download() {
        btConfirm.setEnabled(false);
        String dest = Environment.getExternalStorageDirectory().getAbsolutePath();
        HyOkHttpUtils.downNewApp(url, new FileCallBack(dest, "weilaitongfu.apk") {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(File response, int id) {
                file = response;
                btConfirm.setEnabled(true);
            }

            @Override
            public void inProgress(float progress, long total, int id) {
                super.inProgress(progress, total, id);
                double d = Double.parseDouble(String.valueOf(progress * 100));
                d = Double.parseDouble(StringUtil.getDouble2(d));
                int s = new Double(d).intValue();
//                Message msg = handler.obtainMessage();
//                msg.what=1;
//                msg.arg1=s;
//                handler.sendMessage(msg);
                mprotv.setText(d+"%");
                contentProgressBar.setProgress(s);
                contentProgressBar.setProgress(s);

            }

        });
    }
//    Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.what==1){
//                int s = msg.arg1;
//                mprotv.setText(s+"%");
//            }
//            super.handleMessage(msg);
//        }
//    };
    @OnClick({R.id.bt_cancel, R.id.bt_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_cancel:
                dismiss();
                if (updateAppDialogListener != null)
                    updateAppDialogListener.onUpdateAppDialogCancel(isForce);
                break;

            case R.id.bt_confirm:
                if (updateAppDialogListener != null) {
                    updateAppDialogListener.onUpdateAppDialogUpdate(isForce);
                }
                dismiss();
                installApk(file);
                break;
        }
    }

    private UpdateAppDialogListener updateAppDialogListener;

    public interface UpdateAppDialogListener {
        //取消升级
        public void onUpdateAppDialogCancel(boolean isForce);

        //开始升级
        public void onUpdateAppDialogUpdate(boolean isForce);
    }


    /**
     * 安装APK
     *      
     */
    /**
     * 安装 apk 文件
     *
     * @param apkFile apk 文件
     */
    private void installApk(File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //放在此处
        //由于没有在Activity环境下启动Activity,所以设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri apkUri = null;
        //判断版本是否是 7.0 及 7.0 以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            apkUri = FileProvider.getUriForFile(context, wlfProvider, file);
//            apkUri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".fileProvider", apkFile);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            apkUri = Uri.fromFile(apkFile);
        }

        intent.setDataAndType(apkUri,
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

}
