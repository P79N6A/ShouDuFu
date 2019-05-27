package com.futuretongfu.ui.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.futuretongfu.R;
import com.futuretongfu.bean.onlinegoods.OnlineFavoriteBean;
import com.futuretongfu.model.entity.FavoriteList;
import com.futuretongfu.ui.component.SwipeMenuLayout;
import com.futuretongfu.ui.component.dialog.PromptDialog;
import com.futuretongfu.utils.DateUtil;
import com.futuretongfu.utils.GlideLoad;

import java.util.List;

/**
 * Created by zhanggf on 2018/5/17.
 * 线上关注店铺
 */

public class OnLineAttentionAdapter extends BaseQuickAdapter<OnlineFavoriteBean, BaseViewHolder> {

    private Context context;

    public OnLineAttentionAdapter(Context context, OnLineAttentionAdapterListener onLineAttentionAdapterListener, @Nullable List<OnlineFavoriteBean> datas) {
        super(R.layout.item_myonline_attention_list, datas);
        this.context = context;
        this.onLineAttentionAdapterListener = onLineAttentionAdapterListener;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final OnlineFavoriteBean item){
        ImageView ingvPic = helper.getView(R.id.imgv_pic);
        GlideLoad.loadCrossFadeImageView2(item.getStoreLogo(), ingvPic);

        helper
                .setText(R.id.text_name, item.getStoreName())
                .setText(R.id.text_num, item.getFollowNum()+"人收藏");

        helper.setOnClickListener(R.id.view_click, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onLineAttentionAdapterListener != null)
                    onLineAttentionAdapterListener.onLineAttentionAdapterClick(item);
            }
        });

        helper.setOnClickListener(R.id.btn_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SwipeMenuLayout)helper.getView(R.id.view_swipe_menu)).smoothClose();

                new PromptDialog
                        .Builder(context)
                        .setMessage("您确定删除此条收藏？")
                        .setButton1("取消", new PromptDialog.OnClickListener() {
                            @Override
                            public void onClick(Dialog dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setButton2("确定", new PromptDialog.OnClickListener() {
                            @Override
                            public void onClick(Dialog dialog, int which) {
                                dialog.dismiss();

                                if(onLineAttentionAdapterListener != null)
                                    onLineAttentionAdapterListener.onLineAttentionAdapterDelete(item);

                            }
                        })
                        .show();
            }
        });
    }

    public void deleteItem(OnlineFavoriteBean item){
        List<OnlineFavoriteBean> favoriteList = getData();
        for(int i = 0; i < favoriteList.size(); i++){
            if(favoriteList.get(i).getId() == item.getId()){
                remove(i);
                break;
            }
        }
    }

    private OnLineAttentionAdapterListener onLineAttentionAdapterListener;
    public interface OnLineAttentionAdapterListener{
        public void onLineAttentionAdapterClick(OnlineFavoriteBean item);
        public void onLineAttentionAdapterDelete(OnlineFavoriteBean item);
    }
}
