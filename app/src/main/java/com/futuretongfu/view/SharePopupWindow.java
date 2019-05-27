package com.futuretongfu.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.futuretongfu.R;

/**
 * 分享PopupWindow
 *
 * @author DoneYang 2017/6/19
 */

public class SharePopupWindow extends PopupWindow implements View.OnClickListener {

    private Context context;
    private View view;
    private LinearLayout ll_wechat, ll_moments, ll_qq, ll_sina, ll_other;
    private TextView tv_cancel;
    private OnItemClickLister listener;

    public SharePopupWindow(Context context) {
        super(context);
        this.context = context;
        init();
        setPopupWindow();
    }

    /**
     * 初始化
     */
    private void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        //绑定布局
        view = inflater.inflate(R.layout.layout_share_main, null);

        ll_moments = (LinearLayout) view.findViewById(R.id.ll_wechat_moments);
        ll_qq = (LinearLayout) view.findViewById(R.id.ll_qq);
        ll_sina = (LinearLayout) view.findViewById(R.id.ll_sina);
        ll_wechat = (LinearLayout) view.findViewById(R.id.ll_wechat);
        ll_other = (LinearLayout) view.findViewById(R.id.ll_share_other);
        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);

        ll_moments.setOnClickListener(this);
        ll_qq.setOnClickListener(this);
        ll_sina.setOnClickListener(this);
        ll_wechat.setOnClickListener(this);
        ll_other.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        setContentView(view);
    }

    /**
     * 设置窗口属性
     */
    private void setPopupWindow() {
        //设置高和宽
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置可点击
        this.setFocusable(true);
        setOutsideTouchable(true);
        setTouchable(true);
        //设置动画
        setAnimationStyle(R.style.BottomDialogAnim);
        //设置背景
        this.setBackgroundDrawable(new ColorDrawable());
        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                /**
                 * 判断是不是点击了外部
                 */
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    return true;
                }
                //不是点击外部
                return false;
            }
        });
    }

    /**
     * 对外方法
     */
    public interface OnItemClickLister {
        void setOnItemClick(View view);
    }

    public void setOnItemClickListener(OnItemClickLister listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.setOnItemClick(view);
            dismiss();
        }
    }

}
