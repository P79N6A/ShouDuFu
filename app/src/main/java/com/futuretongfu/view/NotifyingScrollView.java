package com.futuretongfu.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class NotifyingScrollView extends ScrollView {

	public interface OnScrollChangedListener{
		void OnScrollChanged(ScrollView who, int l, int t, int oldl, int oldt);
	}

	private OnScrollChangedListener onScrollChangedListener;

	public NotifyingScrollView(Context context) {
		super(context);
	}

	public NotifyingScrollView(Context context, AttributeSet attributeSet){
		super(context,attributeSet);
	}

	public NotifyingScrollView(Context context, AttributeSet attributeSet, int defStyle){
		super(context,attributeSet,defStyle);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if(onScrollChangedListener!=null){
			onScrollChangedListener.OnScrollChanged(this,l,t,oldl,oldt);
		}
	}

	public void setOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener){
		this.onScrollChangedListener=onScrollChangedListener;
	}
} 