package com.futuretongfu.model.entity;

import java.io.Serializable;

/**
 * 类:    RecommendHeadInfo
 * 描述:  我的推荐 奖励积分信息
 * 作者：  weiang on 2017/6/28
 */
public class RecommendHeadInfo implements Serializable {

    private String cash = "";
    private String jifen = "";
    private String sharker = "";
    private String comMember = "";
    private String maker = "";
    private String cymaker = "";
    private String harker = "";
    private String venture = "";
    private String agent = "";

    public String getHarker() {
        return harker;
    }

    public void setHarker(String harker) {
        this.harker = harker;
    }

    public String getVenture() {
        return venture;
    }

    public void setVenture(String venture) {
        this.venture = venture;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getJifen() {
        return jifen;
    }

    public void setJifen(String jifen) {
        this.jifen = jifen;
    }

    public String getSharker() {
        return sharker;
    }

    public void setSharker(String sharker) {
        this.sharker = sharker;
    }

    public String getComMember() {
        return comMember;
    }

    public void setComMember(String comMember) {
        this.comMember = comMember;
    }

    public String getMaker() {
        return maker;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }


    public String getCymaker() {
        return cymaker;
    }

    public void setCymaker(String cymaker) {
        this.cymaker = cymaker;
    }
}
