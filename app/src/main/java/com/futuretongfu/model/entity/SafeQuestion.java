package com.futuretongfu.model.entity;

/**
 * Created by ChenXiaoPeng on 2017/7/4.
 */

public class SafeQuestion {

    private long createTime;
    private long updateTime;

    private int id;
    private String question;

    public SafeQuestion(){

    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

}
