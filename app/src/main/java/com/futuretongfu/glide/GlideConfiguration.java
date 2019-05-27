package com.futuretongfu.glide;

import android.content.Context;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.module.GlideModule;
import com.futuretongfu.constants.AppConfigure;

import java.io.File;

/**
 * @author DoneYang
 */
public class GlideConfiguration implements GlideModule {

    @Override
    public void applyOptions(final Context context, GlideBuilder builder) {
        // Apply options to the builder here.
        builder.setDecodeFormat(DecodeFormat.DEFAULT);//DecodeFormat.ALWAYS_ARGB_8888
        final int cacheSize300MegaBytes = 104857600 * 3;
        // or any other path
        builder.setDiskCache(new DiskCache.Factory() {
            @Override
            public DiskCache build() {
                // Careful: the external cache directory doesn't enforce permissions
                File cacheLocation = new File(Environment.getExternalStorageDirectory(), AppConfigure.PHOTO_PATH);
                cacheLocation.mkdirs();
                return DiskLruCacheWrapper.get(cacheLocation, cacheSize300MegaBytes);
            }
        });

    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}