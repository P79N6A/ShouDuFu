package com.futuretongfu.model.entity;

import java.io.Serializable;

/**
 * Created by weiang on 2017/7/27.
 */

public class MessageListInfo implements Serializable{

    /**
     * id : 8
     * type : null
     * msgType : null
     * title : 1
     * content : 按照平台市场推广战略，7月中旬已正式启动城市联合代理招募计划。按照“开放生态、合作共享”原则，未来通付联合代理商将享有代理区域内商家销售分佣、会员升级奖励等多项补贴政策。了解城市联合代理详情，请联系公司客服或登陆公司官网留言。客服电话：4001508508  网址：www.weilaitongfu.com
     * createTime : 2017-07-26
     * date : 1501071654000
     */

    private int id;
    private Object type;
    private Object msgType;
    private String title;
    private String content;
    private String createTime;
    private long date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public Object getMsgType() {
        return msgType;
    }

    public void setMsgType(Object msgType) {
        this.msgType = msgType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
