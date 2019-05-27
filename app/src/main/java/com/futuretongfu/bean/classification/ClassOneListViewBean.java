package com.futuretongfu.bean.classification;

import java.util.List;

/**
 * Created by Android on 2017/3/26.
 */
public class ClassOneListViewBean {

    private String categoryCode;
    private String categoryName;
    private List<ClassTwoListViewBean> childs;

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

    public List<ClassTwoListViewBean> getChilds() {
        return childs;
    }

    public void setChilds(List<ClassTwoListViewBean> childs) {
        this.childs = childs;
    }
}
