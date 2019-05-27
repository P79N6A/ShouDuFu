package com.futuretongfu.bean.onlinegoods;

import com.futuretongfu.base.BaseSerializable;

/**
 * Created by zhanggf on 2018/5/18.
 * 商品属性
 */

public class GoodsAttrValuesBean extends BaseSerializable {
    private String attrValueId;
    private String attrValue;

    public GoodsAttrValuesBean(String attrValueId, String attrValue) {
        this.attrValueId = attrValueId;
        this.attrValue = attrValue;
    }

    public GoodsAttrValuesBean() {
    }

    public String getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }


    public String getAttrValueId() {
        return attrValueId;
    }

    public void setAttrValueId(String attrValueId) {
        this.attrValueId = attrValueId;
    }
}
