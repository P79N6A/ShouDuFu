package com.futuretongfu.model.entity;

import com.futuretongfu.ui.component.contacts.widget.Indexable;

/**
 * Created by ChenXiaoPeng on 2017/7/4.
 */

public class AddressEntity implements Indexable {

    private int areaId;//身份ID
    private int parentId;//父ID
    private String areaName;//区域名称
    private String pinyin;//拼音
    private String letters;
    private String hfAreaId;

    public AddressEntity() {

    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public void setIndex(String text) {
        this.pinyin = text;
    }

    @Override
    public String getIndex() {
        return pinyin;
    }

    public void setSortLetters(String s) {
        this.letters = s;
    }

    public String getSortLetters(){
        return letters;
    }

    public String getHfAreaId() {
        return hfAreaId;
    }

    public void setHfAreaId(String hfAreaId) {
        this.hfAreaId = hfAreaId;
    }
}
