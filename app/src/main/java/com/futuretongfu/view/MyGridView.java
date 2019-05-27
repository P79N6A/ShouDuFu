package com.futuretongfu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AbsListView;
import android.widget.GridView;

import com.futuretongfu.utils.To;
import com.futuretongfu.utils.Logger.Logger;

/**
 * 自定义gridview
 *
 * @author DoneYang 2017/7/1
 */

public class MyGridView extends GridView implements AbsListView.OnScrollListener {

    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    /**
     * 重写该方法，达到使GridView适应ScrollView的效果
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case OnScrollListener.SCROLL_STATE_FLING:
                To.s("开始滚动：SCROLL_STATE_FLING");
                break;
            case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                To.s("正在滚动：SCROLL_STATE_TOUCH_SCROLL");
                break;
            case OnScrollListener.SCROLL_STATE_IDLE:
                To.s("已经停止：SCROLL_STATE_IDLE");
                // 判断滚动到底部
                if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                    if (listener != null) {
                        listener.onScrollBottom();
                    }
                }
                break;
        }
    }

    //调用系统GridView的监听,在停止的时候判断如果已经到底部,那么调用我们自已写的回调
    //这样就是--GridView滑动到底部的回调
    private OnScrollBottomListener listener;

    public void setOnScrollBottomListener(OnScrollBottomListener listener) {
        this.setOnScrollListener(this);
        this.listener = listener;
    }

    public void removeOnScrollBottomListener() {
        listener = null;
        Logger.i("removeOnScrollBottomListener");
    }

    /**
     * 列表视图滚动到底部监听器
     */
    public interface OnScrollBottomListener {
        /**
         * 列表视图滚动到底部时响应
         */
        public void onScrollBottom();
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

    }
}
