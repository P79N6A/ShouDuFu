package com.futuretongfu.bean;

import com.futuretongfu.base.BaseSerializable;

/**
 * 转账-最近联系人
 *
 * @author DoneYang 2017/7/6
 */

public class TransferListBean extends BaseSerializable {

    public int id;

    public long userId = 0;

    public int userType;

    public String store;

    public boolean isAgent;

    public String userKey;

    public String loginPwd;

    public String gesturePwd;

    public String payPwd;

    public int gender;

    public String phone;

    public String email;

    public String nickName;

    public String realName;

    public String recomUserId;

    public boolean userStatus;

    public boolean dataFlag;

    public String ip;

    public int terminal;

    public long createTime;

    public String updateTime;

    public boolean upgrad;

    public boolean agent;

    public String faceUrl;
}
