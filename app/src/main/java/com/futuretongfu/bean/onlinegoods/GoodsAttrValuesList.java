package com.futuretongfu.bean.onlinegoods;


import java.util.List;

/**
 * Created by zhanggf on 2018/5/18.
 * 商品属性
 */

public class GoodsAttrValuesList {
    private List<GoodsAttrValuesBean> attributeValue;
    private String attributeId;
    private String attributeName;

    public List<GoodsAttrValuesBean> getAttributeValue() {
        return attributeValue;
    }

    public void setAttributeValue(List<GoodsAttrValuesBean> attributeValue) {
        this.attributeValue = attributeValue;
    }

    public GoodsAttrValuesList() {
    }

    public GoodsAttrValuesList(String attributeId, String attributeName) {
        this.attributeId = attributeId;
        this.attributeName = attributeName;
    }

    public String getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(String attributeId) {
        this.attributeId = attributeId;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }
}
