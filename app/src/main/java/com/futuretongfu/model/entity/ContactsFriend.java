package com.futuretongfu.model.entity;

/**
 *   好友申请记录列表 实体
 * Created by ChenXiaoPeng on 2017/6/26.
 */

public class ContactsFriend {

    private String userId;
    private String faceURL;
    private String nickName;
    private String realName;
    private String phone;
    private boolean friend;

    //用于好友申请列表的字段
    private String msg;
    private int addStatus;
    private String friendUserId;

    public ContactsFriend(){

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFaceURL() {
        return faceURL;
    }

    public void setFaceURL(String faceURL) {
        this.faceURL = faceURL;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getAddStatus() {
        return addStatus;
    }

    public void setAddStatus(int addStatus) {
        this.addStatus = addStatus;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public boolean isFriend() {
        return friend;
    }

    public void setFriend(boolean friend) {
        this.friend = friend;
    }

    public String getFriendUserId() {
        return friendUserId;
    }

    public void setFriendUserId(String friendUserId) {
        this.friendUserId = friendUserId;
    }
}
