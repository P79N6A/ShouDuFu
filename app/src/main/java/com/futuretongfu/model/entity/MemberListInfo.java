package com.futuretongfu.model.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 升级列表
 */
public class MemberListInfo implements Serializable {

    private int id;
    private int userType;//角色类型：1-享客,2-拓客,3-创客,4-创投
    private String name;
    private double fee;//加盟费(升级费用)

    public MemberListInfo(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }
}
