package com.futuretongfu.ui.component;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.futuretongfu.constants.Constants;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.R;
import com.futuretongfu.utils.DeviceUuidFactory;

/**
 * Created by ChenXiaoPeng on 2017/6/22.
 */

public class PicVerifyCodeView extends RelativeLayout {
    private Context context;

    private ImageView imgv;
    private ImageView btnRefresh;

    private String deviceId = "";
    private String url = Constants.HOST + "/" + Constants.Module_Code + "/" + Constants.Action_GetVerifyCode;

    public PicVerifyCodeView(Context context) {
        this(context, null);
    }

    public PicVerifyCodeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PicVerifyCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);

        this.context = context;
        initView();

    }

    private void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.common_pic_verify_code, this);
        imgv = (ImageView) view.findViewById(R.id.img_code);
        btnRefresh = (ImageView) view.findViewById(R.id.bt_refresh);

        btnRefresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
                ViewCompat.animate(btnRefresh).rotationBy(360).setDuration(500).start();

                if (picVerifyCodeViewListener != null)
                    picVerifyCodeViewListener.onPicVerifyCodeViewRefresh();
            }
        });

        deviceId = createDeviceId();
    }

    private String getPicUrl() {
        if (TextUtils.isEmpty(deviceId))
            return url + "?a=" + System.currentTimeMillis();
        else
            return url + "?deviceId=" + deviceId + "&a=" + System.currentTimeMillis();
    }

    public String createDeviceId() {
        return (new DeviceUuidFactory(context)).getDeviceUuidStr();
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void init() {
        GlideLoad.loadCrossFadeImageView(getPicUrl(), imgv);
    }

    public void setPicVerifyCodeViewListener(PicVerifyCodeViewListener picVerifyCodeViewListener) {
        this.picVerifyCodeViewListener = picVerifyCodeViewListener;
    }

    private PicVerifyCodeViewListener picVerifyCodeViewListener;

    public interface PicVerifyCodeViewListener {
        public void onPicVerifyCodeViewRefresh();
    }

}
