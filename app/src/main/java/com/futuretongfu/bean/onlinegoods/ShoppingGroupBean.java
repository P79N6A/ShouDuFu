package com.futuretongfu.bean.onlinegoods;


import java.util.List;

/**
 * Created by Android on 2017/4/7.
 */
public class ShoppingGroupBean {
    private String id;
    private String storeName;
    private List<ShoppingGoodsBean> storeList;
    protected boolean isChoosed;
    protected double groupPrice;


    public boolean getIsChoosed() {
        return isChoosed;
    }

    public void setIsChoosed(boolean isChoosed) {
        this.isChoosed = isChoosed;
    }

    public double getGroupPrice() {
        return groupPrice;
    }

    public void setGroupPrice(double groupPrice) {
        this.groupPrice = groupPrice;
    }
    public ShoppingGroupBean() {
    }

    public ShoppingGroupBean(String id, String storeName) {
        this.id = id;
        this.storeName = storeName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public List<ShoppingGoodsBean> getStoreList() {
        return storeList;
    }

    public void setStoreList(List<ShoppingGoodsBean> storeList) {
        this.storeList = storeList;
    }
}
