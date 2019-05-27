package com.futuretongfu.bean.classification;

/**
 * Created by GF-Zhang PC on 2017/12/19.
 */

public class ClassThreeListViewBean {
    private String categoryCode;
    private String categoryName;
    private String categoryImg;

    public ClassThreeListViewBean() {
    }

    public ClassThreeListViewBean(String categoryCode, String categoryName) {
        this.categoryName = categoryName;
        this.categoryCode = categoryCode;
    }

    public ClassThreeListViewBean(String categoryCode, String categoryName, String categoryImg) {
        this.categoryName = categoryName;
        this.categoryCode = categoryCode;
        this.categoryImg = categoryImg;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImg() {
        return categoryImg;
    }

    public void setCategoryImg(String categoryImg) {
        this.categoryImg = categoryImg;
    }
}
