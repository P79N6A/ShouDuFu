package com.futuretongfu.model.entity;

/**
 *     积分数据结构
 * Created by ChenXiaoPeng on 2017/6/29.
 */

public class Score {

    private double avlBal;//当前可用积分
    private double interAvlbal;//待结算积分

    public Score(){

    }

    public double getAvlBal() {
        return avlBal;
    }

    public void setAvlBal(double avlBal) {
        this.avlBal = avlBal;
    }

    public double getInterAvlbal() {
        return interAvlbal;
    }

    public void setInterAvlbal(double interAvlbal) {
        this.interAvlbal = interAvlbal;
    }
}
