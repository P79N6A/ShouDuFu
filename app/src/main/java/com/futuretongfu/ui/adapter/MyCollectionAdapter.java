package com.futuretongfu.ui.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.futuretongfu.model.entity.FavoriteList;
import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.R;
import com.futuretongfu.ui.component.SwipeMenuLayout;
import com.futuretongfu.ui.component.dialog.PromptDialog;
import com.futuretongfu.utils.DateUtil;

import java.util.List;

/**
 * Created by ChenXiaoPeng on 2017/6/19.
 */

public class MyCollectionAdapter extends BaseQuickAdapter<FavoriteList, BaseViewHolder> {

    private Context context;

    public MyCollectionAdapter(Context context, MyCollectionAdapterListener myCollectionAdapterListener, @Nullable List<FavoriteList> datas) {
        super(R.layout.item_my_collection_list, datas);
        this.context = context;
        this.myCollectionAdapterListener = myCollectionAdapterListener;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final FavoriteList item){
        ImageView ingvPic = helper.getView(R.id.imgv_pic);
        GlideLoad.loadCrossFadeImageView2(item.getLogoUrl(), ingvPic);

        helper
                .setText(R.id.text_name, item.getStoreName())
                .setText(R.id.text_time, "收藏时间：" + DateUtil.getDateStr3(item.getCreateTime()));

        helper.setOnClickListener(R.id.view_click, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myCollectionAdapterListener != null)
                    myCollectionAdapterListener.onMyCollectionAdapterClick(item);
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

                                if(myCollectionAdapterListener != null)
                                    myCollectionAdapterListener.onMyCollectionAdapterDelete(item);

                            }
                        })
                        .show();
            }
        });
    }

    public void deleteItem(FavoriteList item){
        List<FavoriteList> favoriteList = getData();
        for(int i = 0; i < favoriteList.size(); i++){
            if(favoriteList.get(i).getId() == item.getId()){
                remove(i);
                break;
            }
        }
    }

    private MyCollectionAdapterListener myCollectionAdapterListener;
    public interface MyCollectionAdapterListener{
        public void onMyCollectionAdapterClick(FavoriteList item);
        public void onMyCollectionAdapterDelete(FavoriteList item);
    }
}
