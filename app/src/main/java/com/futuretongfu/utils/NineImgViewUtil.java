package com.futuretongfu.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.futuretongfu.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenXiaoPeng on 2017/6/21.
 */

public class NineImgViewUtil {

    private Context context;

    private int allCount;
    private boolean isDelete;

    private int addIconID = R.mipmap.icon_comera;

    private List<NineImgView> nineImgViews = new ArrayList<>();
    private List<String> photoUrls = new ArrayList<>();

    public NineImgViewUtil(Context context, int allCount, boolean isDelete, int addIconID, NineImgViewUtilListener nineImgViewUtilListener){
        this.context = context;
        this.allCount = allCount;
        this.isDelete = isDelete;
        this.addIconID = addIconID;
        this.nineImgViewUtilListener = nineImgViewUtilListener;
    }

    public NineImgViewUtil setImgaeView(ImageView... imgv){

        for(int i = 0; i < imgv.length; i++){
            NineImgView nineImgView = new NineImgView(i, imgv[i]);
            nineImgViews.add(nineImgView);
        }

        return this;
    }

    public NineImgViewUtil setDeleteView(ImageView... delete){

        for(int i = 0; i < delete.length; i++){
            nineImgViews.get(i).setDelete(delete[i]);
        }

        return this;
    }

    private void onClickImgvlistner(int index){
        if(index < photoUrls.size())
            return ;

        if(nineImgViewUtilListener != null)
            nineImgViewUtilListener.onNineImgViewOpenImageSelector();
    }

    private void onClickDeletelistner(int index){
        //处于最后一个
        if(index == (photoUrls.size() - 1)){
            if(index < (allCount - 1))
                hideView(index + 1);

            setCameraShow(index);
            photoUrls.remove(index);

            return ;
        }

        photoUrls.remove(index);

        int size = photoUrls.size();
        for (int i = 0; i < size; i++) {
            setPhotoShow(i, photoUrls.get(i));
        }

        if (photoUrls.size() < allCount)
            setCameraShow(photoUrls.size());

        if(photoUrls.size() + 1 <= (allCount - 1)){
            for(int i = (photoUrls.size() + 1); i < allCount; i++){
                hideView(i);
            }
        }

    }

    private void setPhotoShow(int index, String url){
        nineImgViews.get(index).imgv.setVisibility(View.VISIBLE);

        if(isDelete) {
            nineImgViews.get(index).delete.setVisibility(View.VISIBLE);
        }

        GlideLoad.loadImageViewDiskCache(context, url, nineImgViews.get(index).imgv);
    }

    private void setCameraShow(int index){
        nineImgViews.get(index).imgv.setVisibility(View.VISIBLE);

        if(isDelete) {
            nineImgViews.get(index).delete.setVisibility(View.GONE);
        }

        nineImgViews.get(index).imgv.setImageDrawable(context.getResources().getDrawable(addIconID));
    }

    private void hideView(int index){
        nineImgViews.get(index).imgv.setVisibility(View.GONE);

        if(isDelete) {
            nineImgViews.get(index).delete.setVisibility(View.GONE);
        }
    }

    public void setImages(List<String> urls) {
        int size = urls.size();
        int curCount = photoUrls.size();
        for (int i = curCount; i < (curCount + size); i++) {
            setPhotoShow(i, urls.get(i - curCount));
            photoUrls.add(urls.get(i - curCount));
        }

        if (photoUrls.size() < allCount)
            setCameraShow(photoUrls.size());
    }

    public int getCanSelecCount(){
        return allCount - photoUrls.size();
    }

    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    private class NineImgView{
        ImageView imgv;
        ImageView delete;
        int index;

        public NineImgView(int i, ImageView imgv){
            this.index = i;
            this.imgv = imgv;

            this.imgv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickImgvlistner(index);
                }
            });
        }

        public void setDelete(ImageView deleteView){
            this.delete = deleteView;
            this.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickDeletelistner(index);
                }
            });
        }
    }

    private NineImgViewUtilListener nineImgViewUtilListener;
    public interface NineImgViewUtilListener{
        public void onNineImgViewOpenImageSelector();
    }
}
