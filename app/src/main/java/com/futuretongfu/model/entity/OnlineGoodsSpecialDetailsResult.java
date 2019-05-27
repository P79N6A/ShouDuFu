package com.futuretongfu.model.entity;



/**
 * Created by ChenXiaoPeng on 2017/7/12.
 */

public class OnlineGoodsSpecialDetailsResult {
    private String id;
    private String skuDetail;
    private String packSpec;
    private String warranty;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSkuDetail() {
        return skuDetail;
    }

    public void setSkuDetail(String skuDetail) {
        this.skuDetail = skuDetail;
    }

    public String getPackSpec() {
        return packSpec;
    }

    public void setPackSpec(String packSpec) {
        this.packSpec = packSpec;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }
}
