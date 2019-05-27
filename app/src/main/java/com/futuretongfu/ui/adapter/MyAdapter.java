package com.futuretongfu.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.futuretongfu.utils.GlideLoad;
import com.futuretongfu.R;

import java.util.List;

/**
 * Created by qibin on 2015/11/7.
 */
public class MyAdapter extends BaseRecyclerAdapter<MyAdapter.MyHolder> {

    private Context context;
    private List<String> list;

    public MyAdapter(Context context, List<String> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store_intro_list, parent, false);
        return new MyHolder(layout);
    }

    public void setData( List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }



    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int RealPosition, MyHolder data) {
        if (!TextUtils.isEmpty(list.get(0))) {
            GlideLoad.loadImage(list.get(0), data.image1);
        }
        if (!TextUtils.isEmpty(list.get(1))) {
            GlideLoad.loadImage(list.get(1), data.image2);
        }
        if (!TextUtils.isEmpty(list.get(2))) {
            GlideLoad.loadImage(list.get(2), data.image3);
        }
    }

    class MyHolder extends BaseRecyclerAdapter.Holder {
        public ImageView image1, image2, image3;

        public MyHolder(View itemView) {
            super(itemView);
            image1 = (ImageView) itemView.findViewById(R.id.image_item_store_intro1);
            image2 = (ImageView) itemView.findViewById(R.id.image_item_store_intro2);
            image3 = (ImageView) itemView.findViewById(R.id.image_item_store_intro3);
        }
    }
}

//private class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageVireHolder> {
//
//    private Context context;
//    private List<String> list;
//
//    ImageAdapter(Context context, List<String> list) {
//        this.list = list;
//        this.context = context;
//    }
//
//    @Override
//    public ImageVireHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new ImageVireHolder(View.inflate(context, R.layout.item_store_intro_list, null));
//    }
//
//    @Override
//    public void onBindViewHolder(ImageVireHolder holder, int position) {
//        if (!TextUtils.isEmpty(list.get(0))) {
//            GlideLoad.loadImage(list.get(0), holder.image1);
//        }
//        if (!TextUtils.isEmpty(list.get(1))) {
//            GlideLoad.loadImage(list.get(1), holder.image2);
//        }
//        if (!TextUtils.isEmpty(list.get(2))) {
//            GlideLoad.loadImage(list.get(2), holder.image3);
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return list != null && !list.isEmpty() ? 1 : 0;
//    }
//
//    public void setData(List<String> list) {
//        this.list = list;
//        notifyDataSetChanged();
//    }
//
//    class ImageVireHolder extends RecyclerView.ViewHolder {
//
//        private final ImageView image1, image2, image3;
//
//        ImageVireHolder(View itemView) {
//            super(itemView);
//            image1 = (ImageView) itemView.findViewById(R.id.image_item_store_intro1);
//            image2 = (ImageView) itemView.findViewById(R.id.image_item_store_intro2);
//            image3 = (ImageView) itemView.findViewById(R.id.image_item_store_intro3);
//        }
//    }
//}