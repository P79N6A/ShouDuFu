package com.futuretongfu.ui.component.contacts.model;

import com.futuretongfu.base.BaseSerializable;
import com.futuretongfu.model.entity.ContactsFriend;
import com.futuretongfu.ui.component.contacts.widget.Indexable;

/**
 * Created by ChenXiaoPeng on 2017/6/15.
 */

public class ContactModel {

    public static class MembersEntity extends BaseSerializable implements Indexable {

        private String id;
        private String username;//昵称
        private String realName;

        private String mobile;//电话号码
        private String faceURL;//头像URL
        private boolean friend;//是否是朋友

        private int addStatus;
        private String sortLetters;

        private boolean isAgree = false; //是否已邀请

        public boolean isAgree() {
            return isAgree;
        }

        public void setAgree(boolean agree) {
            isAgree = agree;
        }

        public MembersEntity() {
        }

        public MembersEntity(ContactsFriend item) {
            this.id = item.getUserId();
            this.username = item.getNickName() == null ? "未命名" : item.getNickName();
            this.realName = item.getRealName() == null ? "" : item.getRealName();

            this.mobile = item.getPhone();
            this.faceURL = item.getFaceURL();
            this.friend = item.isFriend();

            this.addStatus = item.getAddStatus();
        }

        public MembersEntity(String username, String mobile) {
            this.username = username;
            this.mobile = mobile;
        }

        public String getSortLetters() {
            return sortLetters;
        }

        @Override
        public String getIndex() {
            return sortLetters;
        }

        public void setSortLetters(String sortLetters) {
            this.sortLetters = sortLetters;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getId() {
            return id;
        }

        public String getUsername() {
            return username;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public int getAddStatus() {
            return addStatus;
        }

        public void setAddStatus(int addStatus) {
            this.addStatus = addStatus;
        }

        public String getFaceURL() {
            return faceURL;
        }

        public void setFaceURL(String faceURL) {
            this.faceURL = faceURL;
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
    }
}
