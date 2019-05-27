package com.futuretongfu.bean.entity;

/**
 * 转账
 *
 * @author DoneYang 2017/6/23
 */

public class Tranfer {

    public String name;
    public int money;

    public Tranfer(String name, int money) {
        this.name = name;
        this.money = money;
    }

    public Tranfer(int money) {
        this.money = money;
    }

    public Tranfer(String name) {
        this.name = name;
    }
}
