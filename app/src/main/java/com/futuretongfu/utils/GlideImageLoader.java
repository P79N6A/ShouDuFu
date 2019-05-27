package com.futuretongfu.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.youth.banner.loader.ImageLoader;

import javax.annotation.Nullable;


/**
 * Created by quantan.liu on 2017/3/24.
 */

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object url, ImageView imageView) {
        if (!Util.isEmpty(url)) {
            GlideLoad.loadBannerImage( String.valueOf(url), imageView);
//            Glide.with(context).load(String.valueOf(url))
//                    .listener(mRequestListener)
//                    .into(imageView);
        }
    }

    RequestListener mRequestListener =new RequestListener() {
        @Override
        public boolean onException(Exception e, Object model, Target target, boolean isFirstResource) {
            Log.d("debug", "onException: " + e.toString()+"  model:"+model+" isFirstResource: "+isFirstResource);
            return false;
        }

        @Override
        public boolean onResourceReady(Object resource, Object model, Target target, boolean isFromMemoryCache, boolean isFirstResource) {
            Log.e("debug",  "model:"+model+" isFirstResource: "+isFirstResource);
            return false;
        }
    };
}
