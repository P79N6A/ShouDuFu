package com.futuretongfu.bean;

import com.futuretongfu.base.BaseSerializable;

import java.util.List;

/**
 * 未来商圈banner轮播图
 *
 * @author DoneYang 2017/6/14.
 */

public class WlsqBannerBean extends BaseSerializable {

    public List<Banner> banner;

    public class Banner {

        public int id;

        /**
         * title
         */
        public String name;

        /**
         * 图片url
         */
        public int resId;

        /**
         * 图片链接
         */
        public String hrefUrl;
    }
}
