package com.futuretongfu.ui.component.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.futuretongfu.R;

/**
 * Created by ChenXiaoPeng on 2017/7/7.
 */

public class CameraSelectDialog extends AbsCustomDialog implements android.view.View.OnClickListener{

    private TextView textPhoto;
    private TextView textCamera;
    private TextView btnCancel;

    private OnCameraSelectorConfirmListener onConfirmListener;

    public CameraSelectDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        textPhoto = (TextView) findViewById(R.id.text_photo);
        textCamera = (TextView) findViewById(R.id.text_camera);
        btnCancel = (TextView) findViewById(R.id.text_cancel);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        textPhoto.setOnClickListener(this);
        textCamera.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }



    @Override
    public int getWindowAnimationsResId() {
        return R.style.BottomDialogAnim;
    }

    @Override
    public int getLayoutResID() {
        return R.layout.dialog_camera_select;
    }

    @Override
    public int getWidth() {
        return android.view.ViewGroup.LayoutParams.MATCH_PARENT;
    }

    @Override
    public int getHeight() {
        return android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public int getGravity() {
        return Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
    }

    public void setOnCameraSelectorConfirmListener(OnCameraSelectorConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_photo:
                if (onConfirmListener != null) {
                    onConfirmListener.onCameraSelectorPhoto();
                }
                dismiss();
                break;

            case R.id.text_camera:
                if (onConfirmListener != null) {
                    onConfirmListener.onCameraSelectorCamera();
                }
                dismiss();
                break;

            case R.id.text_cancel:
                dismiss();
                break;

            default:
                break;
        }
    }

    public interface OnCameraSelectorConfirmListener {
        public void onCameraSelectorPhoto();
        public void onCameraSelectorCamera();
    }

}
