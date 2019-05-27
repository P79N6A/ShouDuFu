package com.futuretongfu.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.futuretongfu.R;
import com.futuretongfu.listener.OnBankItemClickListener;


/**
 * 类:   BankPopupWindow
 * 描述: 我的银行卡页面弹框
 * 作者： weiang on 2017/6/21
 */
public class BankPopupWindow extends PopupWindow implements View.OnClickListener {

    private View rootView;
    private Context context;
    private ImageView imgv_close;
    private TextView text_secrecy, text_delete;
    private OnBankItemClickListener listener;

    public BankPopupWindow(Context context) {
        super(context);
        this.context = context;
        init(context);
        setPopupWindow();
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        //绑定布局
        rootView = inflater.inflate(R.layout.layout_popup_bank, null);
        imgv_close = (ImageView) rootView.findViewById(R.id.imgv_close);
        text_secrecy = (TextView) rootView.findViewById(R.id.text_secrecy);
        text_delete = (TextView) rootView.findViewById(R.id.text_delete);
        imgv_close.setOnClickListener(this);
        text_secrecy.setOnClickListener(this);
        text_delete.setOnClickListener(this);
        text_secrecy.setSelected(true);
        setContentView(rootView);
    }

    /**
     * 设置窗口属性
     */
    private void setPopupWindow() {
        // 设置SelectPicPopupWindow的View
        this.setContentView(rootView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        setAnimationStyle(R.style.BottomDialogAnim);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        //this.setAnimationStyle(R.style.AnimBottom);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable();
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }


    /**
     * 是否隐藏设置按钮
     */
    public void hintSecrecyButton(boolean isSecrecy) {
        if (isSecrecy) {
            text_secrecy.setVisibility(View.GONE);
        } else {
            text_secrecy.setVisibility(View.VISIBLE);
        }

    }


    public void setOnItemClickListener(OnBankItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onItemClick(view, rootView);
        }
    }
}
