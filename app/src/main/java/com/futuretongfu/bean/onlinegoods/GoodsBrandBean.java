package com.futuretongfu.bean.onlinegoods;

import com.futuretongfu.base.BaseSerializable;

/**
 * Created by zhanggf on 2018/5/18.
 */

public class GoodsBrandBean extends BaseSerializable {
    private String id;
    private String brandLogoUrl;
    private String brandName;
    private String categoryId;  //	分类id

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrandLogoUrl() {
        return brandLogoUrl;
    }

    public void setBrandLogoUrl(String brandLogoUrl) {
        this.brandLogoUrl = brandLogoUrl;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
