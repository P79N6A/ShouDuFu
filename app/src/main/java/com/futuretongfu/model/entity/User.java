package com.futuretongfu.model.entity;

/**
 * Created by ChenXiaoPeng on 2017/6/14.
 */

public class User {

    private long userId;
    private String password;

    private int gender;//性别
    private String accountNumber;//账号
    private String email;//邮箱
    private String nickName;//昵称
    private String realName;//真实姓名（为null 或 空 则为 还没实名认证）
    private String region;//区域
    private String faceUrl;//头像地址
    private String cardId;//身份证号码

    private int realNameStatus;//实名认证的状态

    private boolean agent;//是否联合代理
    private boolean gesturePwd;//手势密码
    private boolean payPwd;//支付密码
    private boolean answer;//设置安全问题
    private boolean upgrad;//自动升级角色
    private boolean withdrawCard;//是否有安全卡
    private boolean store;//是否是商家

    private int userType;//用户类型
    private String referrer;//推荐人
    private String userKey;//邀请码

    private int flag;//部分100元拓
    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String userName;

    private String _token;//token cookie

    //银行卡数量  昨日转换通贝 身份证号码

    private String gesturePwdNative;//本地手势密码

    public User() {

    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public boolean getGesturePwd() {
        return gesturePwd;
    }

    public void setGesturePwd(boolean gesturePwd) {
        this.gesturePwd = gesturePwd;
    }

    public boolean getPayPwd() {
        return payPwd;
    }

    public void setPayPwd(boolean payPwd) {
        this.payPwd = payPwd;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public boolean isUpgrad() {
        return upgrad;
    }

    public void setUpgrad(boolean upgrad) {
        this.upgrad = upgrad;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }

    public boolean isAgent() {
        return agent;
    }

    public void setAgent(boolean agent) {
        this.agent = agent;
    }

    public boolean isGesturePwd() {
        return gesturePwd;
    }

    public boolean isPayPwd() {
        return payPwd;
    }

    public boolean isAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getGesturePwdNative() {
        return gesturePwdNative;
    }

    public void setGesturePwdNative(String gesturePwdNative) {
        this.gesturePwdNative = gesturePwdNative;
    }

    public int getRealNameStatus() {
        return realNameStatus;
    }

    public void setRealNameStatus(int realNameStatus) {
        this.realNameStatus = realNameStatus;
    }

    public boolean isWithdrawCard() {
        return withdrawCard;
    }

    public void setWithdrawCard(boolean withdrawCard) {
        this.withdrawCard = withdrawCard;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public boolean isStore() {
        return store;
    }

    public void setStore(boolean store) {
        this.store = store;
    }

    public String get_token() {
        return _token;
    }

    public void set_token(String _token) {
        this._token = _token;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }
}
