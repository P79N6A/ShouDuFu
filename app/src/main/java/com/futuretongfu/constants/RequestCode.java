package com.futuretongfu.constants;

/**
 * RequestCode常量
 *
 * @author DoneYang 2017/6/19
 */

public interface RequestCode {

    /**
     * 商家列表
     */
    int STORE_LIST = 1001;

    /**
     * 添加好友
     */
    int FRIEND_LIST = 1002;

    /**
     * 收款
     */
    int COLLECT_MONEY = 1003;

    /**
     * 未来付账户
     */
    int WLF_ACCOUNT = 1004;

    /**
     * 商户
     */
    int STORE_ACCOUNT = 1005;

    /**
     * 扫一扫
     */
    int REQUEST_CODE_SCAN = 1006;

    /**
     * 充值
     */
    int RECHARGE_MONEY = 1007;


    /**
     * 手机通讯录权限
     */
    int PHONE_LINKMAN = 1008;

    /**
     * 定位
     */
    int PHONE_LOCATION = 1009;

    /**
     * 是否是朋友
     */
    int PHONE_ISFRIEND = 1010;

    /**
     * 好友搜索
     */
    int SEARCH_FRIEND = 1011;

    /**
     * 好友转账
     */
    int FRIEND_TRANSFER = 1012;

    /**
     * 登录——进入app
     */
    int REQUEST_CODE_LOGIN_APP = 2001;

    /**
     * 登录——已经进入app
     */
    int REQUEST_CODE_LOGIN_NORMAL = 2018;

    /**
     * 登录——从主页面“我的”
     */
    int REQUEST_CODE_LOGIN_MY = 2002;
    /**
     * ——从主页面“购物车”
     */
    int REQUEST_CODE_SHOPPING = 2003;
    /**
     * 设置——退出，返回首页
     */
    int REQUEST_CODE_SET = 2004;
    /**
     * 支付余额
     */
    int REQUEST_CODE_ZYYE = 2005;
    /**
     * 奖励体验金
     */
    int REQUEST_CODE_JLTYJ = 2006;
    /**
     * 商圈
     */
    int REQUEST_CODE_SQ = 2007;
    /**
     * 密保问题
     */
    int REQUEST_CODE_MIBAAO = 2008;
}
