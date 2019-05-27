package com.futuretongfu.constants;

import com.futuretongfu.BuildConfig;
import com.futuretongfu.utils.CacheDirectoryUtil;

/**
 * Constants
 * Created by ChenXiaoPeng on 2017/6/8.
 */

public class Constants {

    //Tag
    public final static String Common_Tag = "WeiLaiFu";
    public final static String UMENG_APP_KEY = "5abde163f29d984d49000087";
    public final static String CHANNL_ID = "FuturePay";

    public final static String Wechat_Id = "wx5048392db50cb2dd";
    public final static String Wechat_Key = "c0caaefbe9358c5f060ce0815b8307b7";

    public final static String QQ_Id = "1106724635";
    public final static String QQ_Key = "xalXjRveyN918gn5";

    public final static String Sina_Id = "3827944293";
    public final static String Sina_Key = "ae2c0e3fab621084b593d7e2a39e6de5";
    public final static String Sina_url = "http://m.shoudufu.com/";

    public final static String PACKAGE_NAME_QQ = "com.tencent.mobileqq";
    public final static String PACKAGE_NAME_WX = "com.tencent.mm";

    //----请求基地址
    //----------------------生成环境------------------------
    // public final static String HOST = getRoot();
    public final static String HOST = "http://118.31.11.67:8112";//首都富
    public final static String HOST_TEST = "http://116.62.138.181:8112";//测试

    //public final static String WEB_HOST = getWebRoot();//web连接host地址
    //public final static String WEB_HOST = "http://m.shoudufu.com";//web连接host地址
    //public final static String WEB_HOST = "http://116.62.138.181:8080";//web连接host地址
    // public final static String WEB_HOST = "http://116.62.138.181:8112";//web连接host地址
    public final static String WEB_HOST = "http://h5.shoudufu.com";//web连接host地址


    // public final static String WEB_HOST = "http://testh5.shoudufu.com";//web连接host地址

    private static String getRoot() {
        String root = "";
        switch (BuildConfig.BUILD_TYPE) {
            case "majiabaoOne":
                root = "http://116.62.138.181:8112";//测试
                break;
            case "debug":
                root = "http://118.31.11.67:8112";  //开发环境
                break;
            case "release":
                root = "http://118.31.11.67:8112";  //开发环境
                break;
            default:
                root = "http://118.31.11.67:8112";  //开发环境
                break;
        }
        return root;
    }

    private static String getWebRoot() {
        String webroot = "";
        switch (BuildConfig.BUILD_TYPE) {
            case "majiabaoOne":
                webroot = "http://116.62.138.181:8080";//测试
                break;
            case "debug":
                webroot = "http://m.shoudufu.com";  //开发环境
                break;
            case "release":
                webroot = "http://m.shoudufu.com";  //开发环境
                break;
            default:
                webroot = "http://h5.shoudufu.com";  //开发环境
                break;
        }
        return webroot;
    }

    /***************************************接口************************************************************/
    //我的银行卡
    //public final static String Action_BANK_LIST = "getBanks";//新银行卡列表
    public final static String Action_BANK_LIST = "bankList";//银行
    // 卡列表

    //SSL 证书
    public final static String SSL_Key = "201707020000005ktfydeu1w74b6us471of03ss4kz2adgk35p7kdm7wjthanu5q";
    public final static String XXT_Key = "";
    public final static String XXT_Parameter = "parameter";
    public final static String Token = "logintoken";//cookie 名称

    //本地缓存路径
    public final static String App_Dir = CacheDirectoryUtil.getCacheDirectory("").getPath() + "/WLF/";
    public final static String Cache_Dir = App_Dir + "cache/";

    //异常错误
    public final static int Error_Code_Exception = 500;

    //APP 更新状态
    public final static int Update_Statue_Free = 0;//不强制更新
    public final static int Update_Statue_Force = 1;//强制更新

    //银行卡是否添加成
    public static boolean isBindCardSuccess = false;

    //性别
    public final static int Gender_Null = 0;//未知
    public final static int Gender_Man = 1;//男
    public final static int Gender_Woman = 2;//女
    public final static int Gender_Secrecy = 3;//保密

    //显示页面
    public final static int Show_Home = 0;//首页
    public final static int Show_Wlsq = 1;//未来商圈
    public final static int Show_Txl = 2;//通讯录
    public final static int Show_My = 3;//我的

    //PhoneCode 获取手机验证码类型
    public final static String PhoneCode_SignUp = "signUp";//注册
    public final static String PhoneCode_Login = "login";//登录
    public final static String PhoneCode_Gesture = "gesture";//手势
    public final static String PhoneCode_Pay = "pay";//支付
    public final static String PhoneCode_Question = "question";//密保问题
    public final static String PhoneCode_changePhone = "phone";//修改手机号

    //操作终端
    public final static String Terminal = "Android";
    public final static String TYPE = "sq";

    //我的商圈  转入 转出
    public final static int BusinessAccountTransfer_Into = 1;//转入
    public final static int BusinessAccountTransfer_Out = 2;//转出

    //账户类型
    public final static int Account_Type_Jbjjh = 1;//基本借记户
    public final static int Account_Type_Skh = 2;//收款户
    public final static int Account_Type_Fkh = 3;//付款户
    public final static int Account_Type_Jfh = 4;//积分户
    public final static int Account_Type_Fxh = 5;//返现户
    public final static int Account_Type_Sqh = 6;//商圈户

    //好友申请状态
    public final static int AddStatus_NewFriend_Request = 0;//等待确认
    public final static int AddStatus_NewFriend_Adopt = 1;//通过
    public final static int AddStatus_NewFriend_Adopt_No = -1;//不通过

    //消费订单状态  orderStatus 1待付款,2待收货,3已完成,4退款中,5退款成功
    public final static int OrderConsumer_Status_All = 6;//全部
    public final static int OrderConsumer_Status_Pay = 1;//待付款
    public final static int OrderConsumer_Status_Recipient = 2;//待收货
    public final static int OrderConsumer_Status_Finish = 3;//已完成
    public final static int OrderConsumer_Status_Refunding = 4;//退款中
    public final static int OrderConsumer_Status_Comment = 5;//退款成功

    //线上订单状态  0待付款；1已付款；2待收货；3已收货；4退款中；5退款成功
    public final static int OrderOnline_Status_All = 6;//全部
    public final static int OrderOnline_Status_Pay = 0;//待付款
    public final static int OrderOnline_Status_Ship = 1;//待发货
    public final static int OrderOnline_Status_Recipient = 2;//待收货
    public final static int OrderOnline_Status_Comment = 3;//待评价
    public final static int OrderOnline_Status_RefundingFinish = 4;//退款中
    public final static int OrderOnline_Status_Refunding = 5;//退款成功
    public final static int OrderOnline_Status_Cancel = 6;//取消
    public final static int OrderOnline_Status_Expired = 7;//过期
    public final static int OrderOnline_Status_YiFinish = 9;//完成

    //消费订单 commentStatus
    public final static int CommentStatus_Yes = 1;//已评价
    public final static int CommentStatus_No = 2;//未评价

    //消费订单 收货方式 1-自动收货 2-手动收货
    public final static int FinishStatus_Auto = 1;//自动收货
    public final static int FinishStatus_Hand = 2;//手动收货

    //消费凭证审核状态 1待审核 2审核通过 3审核未通过
    public final static int OrderCheckStatus_Examine_No = 0;//未上传审核
    public final static int OrderCheckStatus_Examine_All = -1;//全部
    public final static int OrderCheckStatus_Examine_Doing = 1;//待审核
    public final static int OrderCheckStatus_Examine_Success = 2;//审核通过
    public final static int OrderCheckStatus_Examine_Faile = 3;//审核未通过
    public final static int OrderCheckStatus_Examine_Finish = 4;//已完成

    //实名认证状态
    public final static int RealNameStatus_No = 0;//未实名
    public final static int RealNameStatus_Yes = 1;//已实名
    public final static int RealNameStatus_Doing = 2;//实名中
    public final static int RealNameStatus_Faile = 3;//审核失败
    public final static int RealNameStatus_Imperfect = 4;//身份证信息不完善

    public final static String RealNameStatus_Str_No = "Fail";//审核未通过
    public final static String RealNameStatus_Str_Yes = "Pass";//已实名
    public final static String RealNameStatus_Str_Doing = "Applying";//实名中
    public final static String RealNameStatus_Str_NAN = "NAN";//未实名
    public final static String RealNameStatus_Str_Imperfect = "Imperfect";//身份证信息不完善

    //用户类型 1-享客,2-拓客,3-创客,4-创投
    public final static int User_Type_XK = 1;//享客
    public final static int User_Type_TK = 2;//拓客
    public final static int User_Type_CK = 3;//创客
    public final static int User_Type_CT = 4;//创投
    public final static int User_Type_CY = 0;//创业

    public final static int User_Type_TKS = 11;//创业


    //账单 数据类型
//    public final static String Bill_Type_All = "all";//全部
    public final static String Bill_Type_Cash = "cash";//平台资金
    public final static String Bill_Type_Jifen = "jifen";//F积分
    public final static String Bill_Type_mall = "mall";//商圈
    public final static String Bill_Type_SYS = "SYS";//首都富系统余额


    public final static String Bill_Type_Recommend = "invite";//推荐奖


    //第三方支付
    public final static String Payment_PAYECO = "PAYECO";//易联支付
    public final static String Payment_KLTONG = "KLTONG";//开联通
    public final static String Payment_ETONEPAY = "ETONEPAY";//易通支付

    //第三方支付---现用
    public static final String MPAY = "MPAY";   //汇付天下支付
    public static final String ALIPAY = "ALIPAY";   //支付宝支付
    public static final String WECHATPAY = "WECHATPAY";  //微信支付

    public static final String ALIPAY_BALANCE = "ALIPAY_BALANCE";//支付宝+平台余额
    public static final String WECHATPAY_BALANCE = "WECHATPAY_BALANCE";//微信+平台余额


    //账单类型 businessType
    public final static String BusinessType_Zz_Str2 = "TRANSFER_OUT";//2收款--转账收款  坑坑坑
    public final static String BusinessType_OrderPayCash = "order_pay_cash";//订单现金支付

    //消息类型
    public final static int MessageWuliu_Status = 1;//物流
    public final static int MessageGonggao_Status = 2;//公告
    public final static int MessageYouhui_Status = 3;//优惠    //String

    //商品搜索类型
    public final static int GoodsSales_Status = 1;//销量
    public final static int GoodsNews_Status = 2;//新品
    public final static int GoodsPrice_Status = 3;//价格

    //WebView类型
    public final static int WebView_Goods_Frgment = 1;//商品详情
    public final static int WebView_Store_Frgment = 2;//店铺首页

    public final static String BusinessType_JF = "businessType_JF";//积分
    public final static String BusinessType_Xf_Str = "consume_jifen";//1消费--消费获得积分
    public final static String BusinessType_Fk_Str = "consume";//1付款
    public final static String BusinessType_Sk_Str = "transfer_in";//2收款--转账收款
    public final static String BusinessType_Zz_Str = "transfer_out";//3转款--转账支出
    public final static String BusinessType_Sqzr_Str = "buss_turn_in";//4商圈转入--商圈转入
    public final static String BusinessType_Tx_Str = "cash";//5提现--提现
    public final static String Action_getWithDrawConfig = "getWithDrawConfig";//获取提现配置信息
    public final static String BusinessType_Zhjf_Str = "transfer_out_jifen";//6转换积分--转账支出获得积分
    public final static String BusinessType_Skjfjl_Str = "transfer_in_fee_jife";//7收款积分奖励--收款服务费获得积分
    public final static String BusinessType_Zfjljf_Str = "sale_fee_jifen";//8支付奖励积分--支付服务费获得积分
    public final static String BusinessType_Jfkc_Str = "refund_jifen";//10积分扣除--退款返还积分
    public final static String BusinessType_Cz_Str = "recharge";//11充值--充值
    public final static String BusinessType_Sqzc_Str = "busss_turn_out";//12商圈转出--商圈转出
    public final static String BusinessType_Tk_Str = "refund_cash";//15退款--退款
    public final static String BusinessType_Busss_Turn_Str = "busss_turn";//转账
    public final static String BusinessType_Transfer_Str = "transfer";//转账
    public final static String BusinessType_Order_Pay_YuE_Str = "order_pay_yue";//余额支付订单
    public final static String BusinessType_Role_Updata_Str = "role_update_jifen";//身份升级支付
    public final static String BusinessType_Cash_Back_Str = "jifen_cash_back";//返现

    public final static String BusinessType_Update_Invite_Cash = "role_update_invite_cash";//被邀请人升级奖励现金
    public final static String BusinessType_jtj_Cash = "jtj_cash";//辅导津贴
    public final static String BusinessType_OnlineOrder = "online_order_pay_cash";//线上订单invite_turn_out
    public final static String BusinessType_invite_turn_out = "invite_turn_out";//推荐奖账户转出


    //账单状态：1支付成功，2支付失败
    public final static int BillDetail_Pay_Statues_Success = 1;//1支付成功
    public final static int BillDetail_Pay_Statues_Faile = 2;//2支付失败

    //OrderPayCash 买单支付 - 订单现金支付 PayType 类型
    public final static String OrderPayCash_PayType_PlatformBalance = "1";//平台余额
    public final static String OrderPayCash_PayType_TradeBalance = "2";//商圈余额
    public final static String OrderPayCash_PayType_PlatformTrade = "3";//平台+商圈余额
    public final static String OrderPayCash_PayType_ThirdPay = "4";//第三方支付


    public final static String JGCONTEXT = "message";
    public final static String JGCONTEXT_Header = "jpush";
    public final static String JGCONTEXT_ss = "jpush";


    //网络请求module类型
    public final static String Module_Code = "user/resource";//验证码
    public final static String Module_Area = "site/area";//地区
    public final static String Module_Site = "site";//地区
    public final static String Module_User = "user";//用户
    public final static String Module_AppInfo = "site";//获取APP信息
    public final static String Module_Authentication = "user/authentication";//验证 验证码  authentication
    public final static String Module_Contacts = "user/contacts";//联系人
    public final static String Module_BANK = "user/bank";//银行
    public final static String Module_Account = "account";//账户
    public final static String Module_TradeAccount = "trade/account";//交易中心
    public final static String Module_Trade = "trade/personal";//交易中心
    public final static String Module_PAY = "pay";//支付
    public final static String Module_INVITE = "user/invite";//我的推荐
    public final static String Module_TRADE_USER = "trade/user";//升级
    public final static String Module_VIP="site/memberNotice";
    public final static String Module_TRADE_USER_NEW = "user/type";//升级新接口
    public final static String Module_Mall = "mall";//商圈
    public final static String Module_SITE_BANK = "/site/bank";//银行列表
    public final static String Module_Bill = "log";//账单
    public final static String Module_Verify = "verify";//检验银行卡

    public static String JIGUANGTUISONG = "/message/jpush/initJpushUser\n";

    //网络请求action类型
    public final static String Action_AppInfo = "index/getsysteminfo";//获取APP信息
    public final static String Action_ProvinceList = "province/list";//省份
    public final static String Action_CityList = "city/list";//城市
    public final static String Action_DistrictList = "district/list";//地区
    public final static String Action_QueryQuestionList = "queryQuestionList";//获取密保问题列表
    public final static String Action_SetSecurityQuestion = "setSecurityQuestion";//设置密保问题
    public final static String Action_SecurityQuestion = "securityQuestion";//验证密保问题
    public final static String Action_ChangeSecurityQuestion = "changeSecurityQuestion";//修改密保问题
    public final static String Action_GetVerifyCode = "getVerifyCode";//获取 图片验证码
    public final static String Action_GetPhoneCode = "getPhoneCode";//获取 手机验证码
    public final static String Action_GetPhoneCodeBindCard = "getMpaySmscode";//获取 手机验证码--绑卡
    public final static String Action_SignUp = "signUp";//用户注册
    public final static String Action_Login = "login";//用户登录
    public final static String Action_GetUserInfoByUserId = "getUserInfoByUserId";//获取个人信息
    public final static String Action_ChangeLoginPwd = "changeLoginPwd";//修改登录密码
    public final static String Action_ResetLoginPwd = "resetLoginPwd";//找回登录密码
    public final static String Action_VerifyCode = "verifyCode";//验证 图片验证码
    public final static String Action_PhoneCode = "phoneCode";//验证 手机验证码
    public final static String Action_LoginPwd = "loginPwd";//验证 登录验证码
    public final static String Action_Gesture = "gesture";//登录手势密码
    public final static String Action_SetGesture = "setGesture";//设置手势密码
    public final static String Action_ChangeGesture = "changeGesture";//修改手势密码
    public final static String Action_SetNickName = "setNickName";//设置/修改昵称
    public final static String Action_SetAutoUpgrade = "setAutoUpgrade";//自动升级设置
    public final static String Action_GetRealNameStatus = "getRealNameStatus";//是否完成实名认证
    public final static String Action_Realname = "realname";//提交实名认证
    public final static String Action_SetEmail = "setEmail";//设置/修改邮箱
    public final static String Action_SetGender = "setGender";//设置性别
    public final static String Action_SetRegion = "setRegion";//设置和修改地区
    public final static String Action_SetFaceImg = "setFaceImg";//设置/修改用户头像

    //银行卡
    public final static String Action_BANK_SET = "bankSet";//银行卡设置
    public final static String Action_BANK_DEL = "bankDel";//删除银行卡
    //    public final static String Action_BANK_ADD = "bankAdd";//添加银行卡
    public final static String Action_BANK_ADD = "bindBankCard";//添加银行卡
    public final static String Action_BANK_CARD = "bankcard";//验证银行卡

    //    public final static String Action_LIST = "list";//银行列表
    public final static String Action_LIST = "bankTypeList";//查询银行列表
    public final static String Action_GetContactsList = "getContactsList";//好友列表
    public final static String Action_GetFriendApplyList = "getFriendApplyList";//好友申请记录列表
    public final static String Action_ConfirmApply = "confirmApply";//好友申请通过/拒绝
    public final static String Action_PAY_PWD = "payPwd";//支付密码（验证）
    public final static String Action_PAY_SET_PAY_PWD = "setPayPwd";//设置支付密码
    public final static String Action_PAY_CHANGE_PAY_PWD = "changePayPwd";//修改支付密码
    public final static String Action_PAY_RESET_PAY_PWD = "resetPayPwd";//找回支付密码
    public final static String Action_Base = "base";//账户余额
    public final static String Action_RECOMMENDBASE = "userInviteBalance";//推荐奖金
    public final static String Action_RECOMMEND = "myRecommend";//我的推荐
    public final static String Action_Score = "jifen";//积分
    public final static String Action_Busi = "busi";//商圈可用余额
    public final static String Action_BusinessIntoShow = "businessIntoShow";//商圈 转入 平台可用余额
    public final static String businessAccountTransfer = "busi/transfer";//商圈 转入 平台可用余额

    public final static String RECOMMEDNDTransfer = "/invite/transfer";//推荐奖 转入 平台可用余额

    public final static String Action_WithdrawalShow = "withdrawalShow";//余额 提现 信息展示
    public final static String Action_Withdrawal = "withdrawal";//
    public final static String Action_RECOMMEND_LIST = "recommendList";//我的推荐列表
    public final static String Action_OrderList = "userOrderList";//我的订单列表
    public final static String Action_OrderDetail = "order/detail";//我的订单详情
    public final static String Action_OrderReceive = "order/confirm/receive";//确认收货
    public final static String Action_OrderDel = "cancelOrder";//删除
    public final static String Action_OrderBack = "order/back";//申请退货
    public final static String Action_SaveComment = "saveComment";//保存订单评价
    //    public final static String Action_uploadTicket = "upload/ticket";//上传消费凭证--订单
    public final static String Action_uploadTicket_Home = "upload/ticket";//上传消费凭证--首页
    public final static String Action_uploadTicket_Update = "ticket/uploadTicketAgain";//修改消费增值接口
    public final static String Action_uploadTicket_Del = "deleteByPrimaryKey";//删除消费增值接口
    public final static String Action_uploadTicket_List = "selectByOrderUserId";//上传记录列表
    public final static String Action_uploadTicket_systemConfig = "systemConfig/62";//获取消费增值服务费比率接口
    public final static String Action_uploadTicket_systemConfigPerson = "systemConfig/63";//获取消费增值服务费比率接口
    public final static String Action_OrderSellList = "list";//销售订单列表
    public final static String Action_OrderSellDetail = "order/detail";//销售订单详情
    public final static String Action_OrderSellCancel = "userOrderCancel";//线下订单删除
    public final static String Action_OrderSellConfirmReturn = "order/confirm/return";//确认退货
    public final static String Action_GetBillList = "getBillList";//获取账单列表
    public final static String Action_GetBillDetail = "getBillDetail";//获取账单详情

    public final static String Action_log = "log";//积分
    public final static String Action_GetJFDetail = "/jifen/detail";//获取积分详情

    //会员升级、商家升级
    public final static String Action_MEMBER_LIST = "memberList";//会员升级列表
    public final static String Action_MEMBER_LIST_NEW = "upgrade/query";//会员升级列表
    public final static String Action_MEMBER_UP_GRADE = "userUpgrade";//会员升级
    public final static String Action_FavoriteList = "favoriteList";//获取收藏列表
    public final static String ACTION_STORE_UPGRAGE_CHECK = "store/upgrade/check";//检测升级
    public final static String ACTION_STORE_UPGRAGE_SUBMIT = "upgradeStoreSubmit";//提交升级
    public final static String ACTION_STORE_UPGRAGE_APPLY = "upgradeStoreApply";//提交审核
    public final static String ACTION_STORE_UPGRAGE_UPDATE = "updateStoreAndBanner";//修改商圈店铺信息

    //网络数据
    public final static String Response_Code = "code";
    public final static String Response_Msg = "msg";
    public final static String Response_Data = "data";
    public final static String Response_Desc = "desc";
    public final static String Response_bank_id = "bankId";
    public final static String Response_bank_code = "bankId";
    public final static String Response_bank_description = "bankDescription";

    public final static int Response_Result_Success = 1;
    public final static int Response_VBank_Result_Success = 0;

    public final static int Response_Result_Offline = 4001;
    public final static String Response_String_Result_Success = "1";

    //发送邀请短信
    public final static String SEND_SMS_Action = "sendRegisterPageByUserIdAndPhone";
    public final static String SEND_SMS_Module = "user";

    //获取实名信息
    public final static String REAL_INFO_Action = "getUserRealByUserId";
    public final static String REAL_Module = "user";

    //未来商圈banner
    public final static String WLSQ_Banner_Action = "banner";
    public final static String WLSQ_Banner_Module = "mall";

    //未来商圈type
    public final static String WLSQ_Type_Action = "industryList";
    public final static String WLSQ_Type_Module = "site";


    //是否绑定推荐人
    public final static String BIND_HEAD = "user";
    public final static String BIND_TUIJIANREN = "invite/bindRecommend";

    //附近商家 113.963524,22.545166
    public final static String Nearby_Store_Action = "storeHomePage";
    public final static String Nearby_Store_Module = "mall";

    //商家详情
    public final static String Store_Details_Action = "storeDetail";
    public final static String Store_Details_Module = "mall";

    //商家搜索
    public final static String Search_Store_Action = "searchStore";
    public final static String Search_Store_Module = "mall";

    //商家评价
    public final static String Store_Evaluate_Action = "commentList";
    public final static String Store_Evaluate_Module = "mall";

    //搜索好友
    public final static String Search_Friend_Action = "queryFriend";
    public final static String Search_Friend_Module = "user/contacts";

    //朋友验证
    public final static String Friend_Verification_Action = "friendApply";
    public final static String Friend_Verification_Module = "user/contacts";

    //二维码
    public final static String Qr_Code_Action = "qrCode";
    public final static String Qr_Code_Module = "common";

    //添加收藏
    public final static String Add_Collect_Action = "favoriteAdd";
    public final static String Add_Collect_Module = "mall";

    //取消收藏
    public final static String Del_Collect_Action = "favoriteDel";
    public final static String Del_Collect_Module = "mall";

    //系统消息num
    public final static String Notice_Num_Action = "msgNum";
    public final static String Notice_Num_Module = "user/msg";

    //交易消息
    public final static String Message_List_Action = "msgList";
    public final static String Message_List_Module = "user/msg";


    public final static String Message_Home_Action = "articleAndMsgList";
    public final static String Message_Home_Module = "site";

    public final static String Message_Convert_Action = "queryJifenConvert";
    public final static String Message_Convert_Module = "log";

    //修改手机号码
    public final static String Phone_Change_Action = "updatePhone";
    public final static String Phone_Change_Module = "user";

//    //系统公告
//    public final static String PUBLIC_ANNOUNCEMENT_MODULE = "site";
//    public final static String PUBLIC_ANNOUNCEMENT_ACTION = "articleList";

    //系统公告列表
    public final static String Notice_List_Action = "articleList";
    public final static String Notice_List_Module = "site";

    public final static String Notice_Details = "/site/articlequeryinfo";

    //买单支付-生成订单
    public final static String Payment_Set_Order_Action = "createOrder";
    public final static String Payment_Set_Order_Module = "mall";

    //买单支付-余额支付
    public final static String Payment_Set_Balance_Action = "platformPay";
    public final static String Payment_Set_Balance_Module = "trade/pay";

    //买单支付-支付-待支付订单
    public final static String Payment_Seconde_Pay_Action = "secondPay";
    public final static String Payment_Seconde_Pay_Module = "trade/pay";

    //扫一扫买单
    public final static String Scan_Payment_Money_Action = "scanPay";
    public final static String Scan_Payment_Money_Module = "trade/pay";

    //充值
    public final static String Recharge_Action = "/apply";
    public final static String Recharge_Module = "/trade/recharge";
    public final static String Recharge_Regist_Module = "rechargeAndupgrade";

    //是否首次充值
    public final static String First_Recharge_Action = "/firstRecharge/check";
    public final static String First_Recharge_Module = "/trade/bank/auth";

    //转账-最近联系人
    public final static String Transfer_Contact_Action = "toPayList";
    public final static String Transfer_Contact_Module = "trade/pay";

    //转账-搜索好友
    public final static String Transfer_Search_Action = "searchFrinend";
    public final static String Transfer_Search_Module = "trade/pay";

    //转账-对方账户-展示
    public final static String Transfer_Friend_Show_Action = "getTransferData";
    public final static String Transfer_Friend_Show_Module = "trade/pay";

    //转账-对方账户
    public final static String Transfer_Friend_Action = "transferSubmit";
    public final static String Transfer_Friend_Module = "trade/pay";

    //系统公告更新状态
    public final static String Notice_Update_Action = "msgStatus";
    public final static String Notice_Update_Module = "user/msg";

    //首页Banner
    public final static String Home_Banner_Action = "queryFirstPageBannerList";
    public final static String Home_Banner_Module = "site";

    //首页交易列表
//    public final static String Transaction_List_Action = "consumeList";
//    public final static String Transaction_List_Module = "site";
    public final static String Transaction_List_Action = "firstPageBillList";
    public final static String Transaction_List_Module = "log";

    //忽略此动态
    public final static String Remove_Transacation_Action = "updateStatus";
    public final static String Remove_Transacation_Module = "log";

    //查询充值订单详情
    public final static String RECHARGE_RE_ACTION = "result";
    public final static String RECHARGE_RE_MODULE = "trade/recharge";

    //查询申请好友的数量
    public final static String FRIEND_NUMBER_ACTION = "contacts/queryFriendApplyNum";
    public final static String FRIEND_NUMBER_MODULE = "user";

    //新注册完善实名绑卡
    public final static String REGIST_REAL_INFO_Action = "newSignUp";
    public final static String REGIST_USER_OPEN_Action = "UserOpen";  //开户
    public final static String REGIST_USER_Address_Action = "address";  //收货地址
    public final static String REGIST_USER_GetAddress_Action = "list/address1";  //收货地址
    public final static String REGIST_REAL_Module = "user";
    public final static String REGIST_USER_TYPE = "userTypeList";
    public final static String Bank_GET_SMSCODE = "getSmsCode";    //解绑发送验证码
    public final static String Bank_UN_BindCard = "unBindCard";    //解绑银行卡
    public final static String Get_RealNameInfo = "searchUserReal";    //查询用户实名信息

    //首页收益
    public final static String Home_Income_Action = "accountBal/queryMoney";
    public final static String Home_Income_Module = "log";


    /*********************************线上商城相关接口*************************************/
    public final static String Home_Shop_Module = "mall";

    public final static String Home_VipSj = "trade/recharge";
    public final static String Home_VipSjk = "/applyAndUpgrade/";


    //首都富-webUrl
    public final static String Home_URL_Action = "common/getOnlineShopDomain";
    //首页分类
    public final static String Home_Sort_Action = "category/list";
    //首页小贴士
    public final static String Home_Notice_Tip_Action = "onlineTips";
    //分类详情
    public final static String Home_Sort_Details_Action = "sku/queryByFlbh2";
    //首页商城Banner
    public final static String Home_ShopBanner_Action = "index/queryBanner";
    //添加收货地址
    public final static String WareAddressAdd_Action = "add/address";
    //修改收货地址
    public final static String WareAddressUpdate_Action = "update/address";
    //删除地址列表
    public final static String WareAddressDelete_Action = "del/address";
    //收货地址列表
    public final static String WareAddressList_Action = "list/address";
    //设置默认收货地址
    public final static String WareAddressSet_Action = "addressSet";
    //三级分类
    public final static String ClassALL_Action = "category/allList";
    //三级分类更新
    public final static String ClassALL_LastTime_Action = "category/getLastModifyTime";
    //我的推荐接口
    public final static String Home_MyRecommend_Action = "sku/queryList";
    //秒杀专区
    public final static String Home_MyOver_Action = "sku/queryRecomend";
    public final static String Home_MyOver_Action1 = "sku/queryRecomend1";
    public final static String Home_MyOver_Action2 = "sku/queryRecomend2";

    public final static String Home_MyOver_Action3 = "sku/queryRecomend3";

    public final static String Home_MyOver_Action4 = "sku/queryRecomend4";


    //商品详情
    public final static String Online_Goods_Action = "sku/getDetail";
    //商品详情--（房车）
    public final static String Online_Goods_Special_Action = "sku/getSkuIntroduce";
    //商品-评价
    public final static String Online_GoodsComment_Action = "skuComment/commentList";
    //商品-获取商品属性列表接口
    public final static String Online_AttrInfo_Action = "sku/querySkuAttrInfo";
    //商品-根据选择属性查找sku信息
    public final static String Online_AttrInfo_SeachGood_Action = "sku/querySkuInfo";
    //线上店铺首页详情
    public final static String Store_Index_Action = "OnlineStoreDetail";
    //店铺商品列表（根据价格获取）接口
    public final static String Online_Store_Price_Action = "sku/queryListByPrice";
    //店铺商品列表（根据销量获取）接口
    public final static String Online_Store_Sales_Action = "sku/queryListBySales";
    //店铺商品列表（根据新品获取）接口
    public final static String Online_Store_CreatDate_Action = "sku/queryListByCreateDate";
    //线上关注店铺
    public final static String Store_AddFavorite_Action = "onlineFavoriteAdd";
    //取消关注
    public final static String Store_CancelFavorite_Action = "onlineFavoriteDel";
    //线上关注店铺列表
    public final static String Store_onlineFavoriteList_Action = "onlineFavoriteList";
    //热搜
    public final static String Online_HotSearch_Action = "hotSearch";
    //搜索商品筛选
    public final static String Online_Search_Good_Action = "sku/searchProduct";
    //搜索筛选-条件列表
    public final static String Online_Search_Term_Action = "onlineSearchCheckbox/list";
    //搜索筛选--品牌
    public final static String Online_Search_Brand_Action = "onlineBrand/list";
    //购物车--列表
    public final static String Online_Shopping_List_Action = "querycart";
    //购物车--添加
    public final static String Online_Shopping_Add_Action = "addShoppingCar";
    //购物车--更改数量
    public final static String Online_Shopping_UpdateNum_Action = "updateShoppingCar";
    //购物车--删除
    public final static String Online_Shopping_Delete_Action = "deleteCart";
    //购物车--确认订单
    public final static String Online_Shopping_FirmOrder_Action = "onlineOrder/saveOrder";

    //购物车--确认订单2
    public final static String Online_Shopping_FirmOrder_Actions = "onlineOrder/createOnlineOrder";

    //订单列表
    public final static String Online_OrderList_Action = "onlineOrder/queryOnlineOrderByOrderNoAndUserId";
    //订单删除
    public final static String Online_OrderDelete_Action = "onlineOrder/cancelOnlineOrder";
    //订单取消
    public final static String Online_OrderCancel_Action = "onlineOrder/onlineOrderCancel";
    //订单过期
    public final static String Online_OrderorderExpired_Action = "onlineOrder/onlineOrderOverdue";
    //提醒发货
    public final static String Online_Order_Remain_Action = "onlineOrder/remind";
    //添加评价
    public final static String Online_OrderAddEvaluate_Action = "skuComment/addComment";
    //余额支付
    public final static String Online_Platform_Alipy = "platformOnlinePay";
    //支付宝支付
    public final static String Online_Pay_Alipy = "aliscPay";
    //支付二次支付
    public final static String Online_Pay_Alipy_Second = "aliscPaySecond";
    //微信二次支付
    public final static String Online_Pay_Wechat_Second = "secondOnlineWxPay";
    //微信支付
    public final static String Online_Pay_Wechat = "wxscPay";
    //确认收货
    public final static String Online_Order_Receve_Action = "confirm/receiving";
    public final static String Online_Order_Module = "account/online";
    //订单详情
    public final static String Online_Order_Details_Action = "onlineOrder/queryDetail";
    //添加退货列表
    public final static String Online_GoodRefund_Add_Action = "addOnlineReturn";
    //订单物流
    public final static String Online_Order_State_Action = "OnlineExpress";
    //退货列表
    public final static String Online_Return_List_Action = "returnList";
    //退货列表详情
    public final static String Online_Return_Details_Action = "queryOnlineReturn";
    //用户商城通宝
    public final static String Online_Tongbao_Action = "online/mallMoney";
    //用户商城通宝
    public final static String Online_TongbaoZhuanru_Action = "onlineTransfer";
    //待付款待发货收藏数量接口
    public final static String Online_Person_OrderNum = "onlineOrder/countNum";
    //购物车数量
    public final static String Online_ShopCat_Num = "countCart";

    //客服电话
//    public final static String Phone_Num_CustomerService = "4001508508";
    public final static String Phone_Num_CustomerService = "4000681266";   //首都富

    //各类协议链接
    public final static String Url_Agreement_AboutUs = WEB_HOST + "/protocol/aboutUs";//关于我们
    public final static String Url_Agreement_RegistProtocol = WEB_HOST + "/protocol/registProtocol";//注册协议（会员升级）
    public final static String Url_Agreement_PrivacyProtocol = WEB_HOST + "/protocol/privacyProtocol";//隐私(实名)
    public final static String Url_Agreement_ServiceProtocol = WEB_HOST + "/protocol/serviceProtocol";//服务协议（商家协议）
    public final static String Url_Agreement_bindProtocol = WEB_HOST + "/protocol/bindProtocol";//绑卡协议
    public final static String Url_share_register = WEB_HOST + "/user/appDownload";//分享下载
    public final static String Url_share_QrCode = WEB_HOST + "/user/registerGuide";//分享注册
    public final static String Url_OnlineGoods_Details = "/shoudufu/web/onlineProductSku/skuInfo/";//商品详情
    public final static String Url_OnlineGoods_Share = "/shoudufu/web/onlineProductSku/shareSkuInfo/";//分享详情
    public final static String Url_OnlineStore_Details = "/shoudufu/web/onlineStore/storeIndex/";//店铺详情
    //汇付天下充值跳转
    public final static String Url_Pay = "https://finance.chinapnr.com/npay/merchantRequest?cmd_id=208&mer_cust_id=6666000003970123&version=10&check_value=";
    //汇付天下绑卡
    public final static String Url_Pay_Bank = "https://finance.chinapnr.com/npay/merchantRequest?cmd_id=103&mer_cust_id=6666000003970123&user_cust_id=6666000004085696&version=10&sms_code=";
    //城市查询  -1为全国所有城市
    public final static int ALL_CITY_SELECT_CODE = -1;

    public final static int REQUEST_CODE = 10001;
    public final static int RESULT_CODE = 10002;

    public final static int AccountState_OneLine = 1;   //线上商城
    public final static int AccountState_Offline = 2;   //线下商圈


    //汇付天下充值跳转
    public final static String Payment_Aliply_Pay_Action = "alisqPay";   //支付宝支付商圈
    public final static String Payment_Aliply_Second_Action = "secondAliPay"; //支付宝二次支付
    public final static String Payment_Wechat_Pay_Action = "wxsqPay";//微信支付商圈
    public final static String Payment_Wechat_Second_Action = "secondWxPay";  //微信二次支付
    public final static String Payment_UploadVouche_Action = "orderTicketFee";  //消费增值服务费支付接口
    public final static String Payment_Aliply_Pay_Module = "trade/pay";    //支付Module

    /*****************************************广播************************************************/
    public static final String REQUEST_CODE_SHOPPING = "com.futuretongfu.refreshcast_login";
    public static final String REQUEST_CODE_SHOPCARTNUM = "com.futuretongfu.refreshcast_shopcartnum";
    public static final String REQUEST_CODE_UPGRADE = "com.futuretongfu.refreshcast_upgrade";
    public static final String REQUEST_CODE_UPGRADE_SUCCESS = "com.futuretongfu.refreshcast_success";
    public static final String REQUEST_CODE_MONEY = "com.futuretongfu.refreshcast_money";
}
