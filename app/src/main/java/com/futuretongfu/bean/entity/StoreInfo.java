package com.futuretongfu.bean.entity;

/**
 * 商家信息
 *
 * @author DoneYang 2017/6/28
 */

public class StoreInfo {

    public int id;
    public String name;
    public String intro;

    public StoreInfo(String intro) {
        this.intro = intro;
    }
}
