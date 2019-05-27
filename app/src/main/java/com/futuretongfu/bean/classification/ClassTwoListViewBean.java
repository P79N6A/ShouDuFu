package com.futuretongfu.bean.classification;

import java.util.List;

/**
 * Created by GF-Zhang PC on 2017/12/19.
 */

public class ClassTwoListViewBean {
    private String categoryCode;
    private String categoryName;
    private List<ClassThreeListViewBean> childs;

    public ClassTwoListViewBean() {
    }

    public ClassTwoListViewBean(String categoryCode, String categoryName) {
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
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

    public List<ClassThreeListViewBean> getChilds() {
        return childs;
    }

    public void setChilds(List<ClassThreeListViewBean> childs) {
        this.childs = childs;
    }
}
