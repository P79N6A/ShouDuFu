package com.futuretongfu.bean;

/**
 * Created by zhanggf on 2018/3/19.
 */

public class ChoosePupulBean {
    public int id;
    public String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ChoosePupulBean(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public ChoosePupulBean() {
    }
}
