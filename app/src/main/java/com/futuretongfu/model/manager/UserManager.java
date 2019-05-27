package com.futuretongfu.model.manager;

import android.content.Context;
import android.text.TextUtils;

import com.futuretongfu.model.manager.safeSPreferences.SafeSharedPreferences;
import com.futuretongfu.WeiLaiFuApplication;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.model.entity.User;
import com.futuretongfu.utils.StringUtil;
import com.skjr.zxinglib.Intents;

/**
 * Created by ChenXiaoPeng on 2017/6/14.
 */

public class UserManager {

    private static UserManager Instance = null;

    public static UserManager getInstance() {
        if (Instance == null)
            Instance = new UserManager();

        return Instance;
    }

    private static final String Key_UserId = "userId";
    private static final String Key_AccountNumber = "accountNumber";
    private static final String Key_Password = "password";

    private static final String Key_UserType = "userType";
    private static final String Key_Referrer = "referrer";
    private static final String Key_UserKey = "userKey";

    private static final String Key_Gender = "gender";
    private static final String Key_Email = "email";
    private static final String Key_NickName = "nickName";
    private static final String Key_RealName = "realName";
    private static final String Key_Region = "region";
    private static final String Key_FaceUrl = "faceUrl";

    private static final String Key_Agent = "agent";
    private static final String Key_GesturePwd = "gesturePwd";
    private static final String Key_PayPwd = "payPwd";
    private static final String Key_Answer = "answer";
    private static final String Key_Upgrad = "upgrad";
    private static final String Key_Store = "store";

    private static final String Key_GesturePwdNative = "gesturePwdNative";

    private static final String Key_RealNameStatus = "realNameStatus";
    private static final String Key_WithdrawCard = "withdrawCard";

    private static final String Key_Token = "token";
    private static final String Key_CardId = "cardId";

    private static final String Key_UserName = "userName";
    private static final String key_user_tks = "flag";

    private static String userFilePath = "shareprefer_file_user";
    private SafeSharedPreferences shareUserFile;
    private SafeSharedPreferences.Editor editorUserFile;
    private User user;

    private UserManager() {
        shareUserFile = new SafeSharedPreferences(WeiLaiFuApplication.getContext(), userFilePath, Context.MODE_PRIVATE);
        editorUserFile = shareUserFile.edit();

        user = new User();

        user.setUserId(shareUserFile.getLong(Key_UserId, 0));
        user.setAccountNumber(shareUserFile.getString(Key_AccountNumber, ""));
        user.setPassword(shareUserFile.getString(Key_Password, ""));

        user.setUserType(shareUserFile.getInt(Key_UserType, Constants.User_Type_XK));


        user.setReferrer(shareUserFile.getString(Key_Referrer, ""));
        user.setUserKey(shareUserFile.getString(Key_UserKey, ""));

        user.setNickName(shareUserFile.getString(Key_NickName, ""));
        user.setRealName(shareUserFile.getString(Key_RealName, ""));
        user.setGender(shareUserFile.getInt(Key_Gender, Constants.Gender_Null));
        user.setEmail(shareUserFile.getString(Key_Email, ""));
        user.setRegion(shareUserFile.getString(Key_Region, ""));
        user.setFaceUrl(shareUserFile.getString(Key_FaceUrl, ""));
        user.setCardId(shareUserFile.getString(Key_CardId, ""));
        user.setUserName(shareUserFile.getString(Key_UserName,""));
        user.setAgent(shareUserFile.getBoolean(Key_Agent, false));
        user.setGesturePwd(shareUserFile.getBoolean(Key_GesturePwd, false));
        user.setPayPwd(shareUserFile.getBoolean(Key_PayPwd, false));
        user.setAnswer(shareUserFile.getBoolean(Key_Answer, false));
        user.setUpgrad(shareUserFile.getBoolean(Key_Upgrad, false));
        user.setWithdrawCard(shareUserFile.getBoolean(Key_WithdrawCard, false));
        user.setStore(shareUserFile.getBoolean(Key_Store, false));

        user.setGesturePwdNative(shareUserFile.getString(Key_GesturePwdNative, ""));
        user.setRealNameStatus(shareUserFile.getInt(Key_RealNameStatus, 0));

        user.set_token(shareUserFile.getString(Key_Token, ""));

    }

    public void init(User user, String password) {
        setGesturePwdNative(shareUserFile.getString(Key_GesturePwdNative, ""));

        setUserId(user.getUserId());
        setAccountNumber(StringUtil.getSafeString(user.getAccountNumber()));
        setPassword(password);

        setUserType(user.getUserType());
        setUserFlag(user.getFlag());

        setReferrer(StringUtil.getSafeString(user.getReferrer()));
        setUserKey(StringUtil.getSafeString(user.getUserKey()));
        setUserName(StringUtil.getSafeString(user.getUserName()));
        setNickName(StringUtil.getSafeString(user.getNickName()));
        setRealName(StringUtil.getSafeString(user.getRealName()));
        setGender(user.getGender());
        setEmail(StringUtil.getSafeString(user.getEmail()));
        setRegion(StringUtil.getSafeString(user.getRegion()));
        setFaceUrl(StringUtil.getSafeString(user.getFaceUrl()));
        setCardId(StringUtil.getSafeString(user.getCardId()));

        setAgent(user.isAgent());
        setGesturePwd(user.getGesturePwd());
        setPayPwd(user.getPayPwd());
        setAnswer(user.isAnswer());
        setUpgrad(user.isUpgrad());
        setWithdrawCard(user.isWithdrawCard());
        setStore(user.isStore());
        setRealNameStatus(user.getRealNameStatus());
        set_token(user.get_token());
        save();
    }


    public void updateUserInfo(User data){
        if(null == data)
            return ;

        setUserId(data.getUserId());
        setStore(data.isStore());
        setAccountNumber(StringUtil.getSafeString(data.getAccountNumber()));
        setFaceUrl(StringUtil.getSafeString(data.getFaceUrl()));
        setNickName(StringUtil.getSafeString(data.getNickName()));
        setRealName(StringUtil.getSafeString(data.getRealName()));
        setGender(data.getGender());
        setEmail(StringUtil.getSafeString(data.getEmail()));
        setAgent(data.isAgent());
        setGesturePwd(data.getGesturePwd());
        setPayPwd(data.getPayPwd());
        setAnswer(data.isAnswer());
        setUpgrad(data.isUpgrad());
        setRegion(StringUtil.getSafeString(data.getRegion()));
        setReferrer(StringUtil.getSafeString(data.getReferrer()));
        setWithdrawCard(data.isWithdrawCard());
        setUserType(data.getUserType());
        setUserFlag(data.getFlag());
        setUserKey(StringUtil.getSafeString(data.getUserKey()));
        setCardId(StringUtil.getSafeString(data.getCardId()));

        //realNameStatus一直都返回null,所以不做更新——奇葩接口
        //setRealNameStatus(data.getRealNameStatus());
        //set_token(data.get_token());

        save();

    }




    /**
     * 是否登录
     */
    public boolean isLogin() {
        if (getUserId() == 0 || TextUtils.isEmpty(getUserId() + ""))
            return false;

        return true;
    }

    /**
     * 是否100拓客
     */
    public boolean is100tk() {
        if (getUserflag() == 1)
            return true;

        return false;
    }

    public boolean isTj(){
        if (getReferrer().equals("")){
            return  false;
        }
        return true;
    }
    /**
     * 是否有手势密码
     */
    public boolean isHasGesturePwd() {
        if (getGesturePwdNative() == null || TextUtils.isEmpty(getGesturePwdNative())) {
            return false;
        }

        return true;
    }


    /**
     * 是否有支付密码
     */
    public boolean isHasPayPwd() {
        return getPayPwd();
    }

    public void clean() {
        String phone = user.getAccountNumber();
        user = new User();
        editorUserFile.clear();
        user.setAccountNumber(phone);
        save();
    }

    public void save() {
        editorUserFile.commit();
    }

    public long getUserId() {
        return user.getUserId();
    }

    public void setUserId(long userId) {
        user.setUserId(userId);
        editorUserFile.putLong(Key_UserId, userId);
    }

    public String getAccountNumber() {
        return user.getAccountNumber();
    }

    public void setAccountNumber(String phone) {
        user.setAccountNumber(phone);
        editorUserFile.putString(Key_AccountNumber, phone);
    }

    public boolean isAgent() {
        return user.isAgent();
    }

    public void setAgent(boolean agent) {
        user.setAgent(agent);
        editorUserFile.putBoolean(Key_Agent, agent);
    }

    public boolean getGesturePwd() {
        return user.getGesturePwd();
    }

    public void setGesturePwd(boolean gesturePwd) {
        user.setGesturePwd(gesturePwd);
        editorUserFile.putBoolean(Key_GesturePwd, gesturePwd);
    }

    public boolean getPayPwd() {
        return user.getPayPwd();
    }

    public void setPayPwd(boolean payPwd) {
        user.setPayPwd(payPwd);
        editorUserFile.putBoolean(Key_PayPwd, payPwd);
    }

    public void setUserName(String userName) {
        user.setUserName(userName);
        editorUserFile.putString(Key_UserName,userName);
    }

    public String getUserName() {
        return user.getUserName();
    }

    public int getUserType() {
        return user.getUserType();
    }
    public int getUserflag() {
        return user.getFlag();
    }
    public void setUserType(int userType) {
        user.setUserType(userType);
        editorUserFile.putInt(Key_UserType, userType);
    }
    public void setUserFlag(int flag) {
        user.setFlag(flag);
        editorUserFile.putInt(key_user_tks, flag);
    }

    public String getNickName() {
        return user.getNickName();
    }

    public void setNickName(String nickName) {
        user.setNickName(nickName);
        editorUserFile.putString(Key_NickName, nickName);
    }

    public String getRealName() {
        return user.getRealName();
    }

    public void setRealName(String realName) {
        user.setRealName(realName);
        editorUserFile.putString(Key_RealName, realName);
    }

    public int getGender() {
        return user.getGender();
    }

    public void setGender(int gender) {
        user.setGender(gender);
        editorUserFile.putInt(Key_Gender, gender);
    }

    public String getEmail() {
        return user.getEmail();
    }

    public void setEmail(String email) {
        user.setEmail(email);
        editorUserFile.putString(Key_Email, email);
    }

    public boolean isUpgrad() {
        return user.isUpgrad();
    }

    public void setUpgrad(boolean upgrad) {
        user.setUpgrad(upgrad);
        editorUserFile.putBoolean(Key_Upgrad, upgrad);
    }

    public String getRegion() {
        return user.getRegion();
    }

    public void setRegion(String region) {
        user.setRegion(region);
        editorUserFile.putString(Key_Region, region);
    }

    public String getFaceUrl() {
        return user.getFaceUrl();
    }

    public void setFaceUrl(String faceUrl) {
        user.setFaceUrl(faceUrl);
        editorUserFile.putString(Key_FaceUrl, faceUrl);
    }

    public boolean isAnswer() {
        return user.isAnswer();
    }

    public void setAnswer(boolean answer) {
        user.setAnswer(answer);
        editorUserFile.putBoolean(Key_Answer, answer);
    }

    public String getReferrer() {
        return user.getReferrer();
    }

    public void setReferrer(String referrer) {
        user.setReferrer(referrer);
        editorUserFile.putString(Key_Referrer, referrer);
    }

    public String getGesturePwdNative() {
        return user.getGesturePwdNative();
    }

    public void setGesturePwdNative(String gesturePwdNative) {
        user.setGesturePwdNative(gesturePwdNative);
        editorUserFile.putString(Key_GesturePwdNative, gesturePwdNative);
    }

    public int getRealNameStatus() {
        return user.getRealNameStatus();
    }

    public void setRealNameStatus(int realNameStatus) {
        user.setRealNameStatus(realNameStatus);
        editorUserFile.putInt(Key_RealNameStatus, realNameStatus);
    }

    public boolean isWithdrawCard() {
        return user.isWithdrawCard();
    }

    public void setWithdrawCard(boolean withdrawCard) {
        user.setWithdrawCard(withdrawCard);
        editorUserFile.putBoolean(Key_WithdrawCard, withdrawCard);
    }

    public String getPassword() {
        return user.getPassword();
    }

    public void setPassword(String password) {
        user.setPassword(password);
        editorUserFile.putString(Key_Password, password);
    }

    public String getUserKey() {
        return user.getUserKey();
    }

    public void setUserKey(String userKey) {
        user.setUserKey(userKey);
        editorUserFile.putString(Key_UserKey, userKey);
    }

//    public void setUserName(String userName) {
//        user.setUserKey(userName);
//        editorUserFile.putString(Key_UserName, userName);
//    }

    public boolean isStore() {
        return user.isStore();
    }

    public void setStore(boolean store) {
        user.setStore(store);
        editorUserFile.putBoolean(Key_Store, store);
    }

    public String get_token() {
        return user.get_token();
    }

    public void set_token(String _token) {
        user.set_token(_token);
        editorUserFile.putString(Key_Token, _token);
    }

    public String getCardId() {
        return user.getCardId();
    }

    public void setCardId(String cardId) {
        user.setCardId(cardId);
        editorUserFile.putString(Key_CardId, cardId);
    }

}
