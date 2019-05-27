package com.futuretongfu.constants;

import android.os.Environment;

/**
 * 一些配置信息，如：路径的常量什么的
 *
 * @author DoneYang 2017/6/14
 */

public interface AppConfigure {

    String KEY = "android";

    String SD_CARD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    String WLF_PATH = SD_CARD_PATH + "/WLF";

    String PHOTO_PATH = WLF_PATH + "/image_cache";

}
