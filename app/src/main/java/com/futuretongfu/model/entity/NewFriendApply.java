package com.futuretongfu.model.entity;

/**
 *   好友申请记录列表 实体
 * Created by ChenXiaoPeng on 2017/6/26.
 */

public class NewFriendApply {

    private String userId;
    private String nickName;
    private String realName;
    private String phone;
    private String faceURL;
    private boolean friends;


    public NewFriendApply(){

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFaceURL() {
        return faceURL;
    }

    public void setFaceURL(String faceURL) {
        this.faceURL = faceURL;
    }

    public boolean isFriends() {
        return friends;
    }

    public void setFriends(boolean friends) {
        this.friends = friends;
    }
}
