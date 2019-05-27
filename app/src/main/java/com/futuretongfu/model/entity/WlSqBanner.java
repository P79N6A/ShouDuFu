package com.futuretongfu.model.entity;

import com.futuretongfu.bean.HomeBannerBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Lenovo on 2017/7/31.
 */

public class WlSqBanner implements Serializable{


    /**
     * code : 1
     * msg : 成功
     * data : {"banner":[{"id":150,"imageUrl":"https://futuredf.oss-cn-shanghai.aliyuncs.com//weilaitongfu/storeBanner/wlsq_banner1.png","url":null},{"id":151,"imageUrl":"https://futuredf.oss-cn-shanghai.aliyuncs.com//weilaitongfu/storeBanner/wlsq_banner2.png","url":null},{"id":152,"imageUrl":"https://futuredf.oss-cn-shanghai.aliyuncs.com//weilaitongfu/storeBanner/wlsq_banner3.png","url":null}]}
     */

    private String code;
    private String msg;
    private DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<HomeBannerBean> banner;

        public List<HomeBannerBean> getBanner() {
            return banner;
        }

        public void setBanner(List<HomeBannerBean> banner) {
            this.banner = banner;
        }

    }
}
