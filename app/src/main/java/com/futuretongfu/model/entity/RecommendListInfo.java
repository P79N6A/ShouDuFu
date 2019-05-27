package com.futuretongfu.model.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 类:    RecommendHeadInfo
 * 描述:  我的推荐 奖励积分信息
 * 作者：  weiang on 2017/6/28
 */
public class RecommendListInfo implements Serializable {

    /**
     * currentPage : 1
     * totalPage : 1
     * list : [{"userId":1149786328339201,"faceUrl":"http://192.168.0.200:8080/weilaifu/images/face.gif","realName":"小明","nicekName":"小亮","userType":"Sharker"}]
     */

    private int currentPage;
    private int totalPage;
    private List<ListBean> list;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * userId : 1149786328339201
         * faceUrl : http://192.168.0.200:8080/weilaifu/images/face.gif
         * realName : 小明
         * nicekName : 小亮
         * userType : Sharker
         */
        private long userId;
        private String faceUrl;
        private String realName;
        private String nickName;
        private String userType;
        private String phone;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public long getUserId() {
            return userId;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public String getFaceUrl() {
            return faceUrl;
        }

        public void setFaceUrl(String faceUrl) {
            this.faceUrl = faceUrl;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }


        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }


        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }
    }
}
