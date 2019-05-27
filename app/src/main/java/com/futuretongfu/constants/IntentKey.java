package com.futuretongfu.constants;

/**
 * 跳转传参的key
 *
 * @author DoneYang 2017/6/16
 */

public interface IntentKey {

    String WLF_BEAN = "wlf_bean";

    String WLF_ID = "wlf_id";

    //金额
    String WLF_Money = "wlf_money";

    //订单单号
    String WLF_Order = "order";

    //url
    String WLF_URL = "wlf_url";

    //type
    String WLF_TYPE = "wlf_type";

    String WLF_ISBACK = "wlf_isback";

    String WLF_ISSHOWTITLE = "wlf_isshowtitle";

    //经度
    String WLF_LONGITUDE = "wlf_longitude";

    //纬度
    String WLF_LATITUDE = "wlf_latitude";

    //城市
    String WLF_CITY = "wlf_city";

    //充值金额
    String RECHARGE_MONEY = "recharge_money";

    //全部金额
    String Total_Money = "total_money";

    //余额
    String REMAINING = "remaining";

    //收款
    String COLLECT_MONEY = "collect_money";

    //搜索 
    String SEARCH_FROM = "search_from";

    //扫一扫结果
    String SCAN_CONTENT_KEY = "scan_content_key";

    //验证码类型标识
    String SMSV_TYPE_KEY = "smsv_type_key";

    //验证码手机号
    String SMSV_PHONE_KEY = "smsv_phone_key";

    //验证码卡号
    String SMSV_CARD_KEY = "smsv_card_key";

    //图片url
    String LOOKMAX_IMAGE_URL = "lookmax_image_url";

    //图片标识
    String LOOKMAX_IMAGE_POSITION = "lookmax_image_position";

    //转账
    String TRANSFER_ACCOUNT = "transfer_account";

    //转账-收款账户
    String TRANSFER_COLLECT_ACCOUNT = "transfer_collect_account";

    //设置支付密码标识
    int TYPE_SET_NEW_PAY_PWD = 122;
    //找回支付密码标识
    int TYPE_RESET_PAY_PWD = 1002;
    //银行类型
    String BANK_INNER_CODE = "innerCode";
    //身份证号
    String USER_CARD_ID = "cardID";
    //真是姓名
    String USER_REALNAME = "Realname";
    //银行卡跳转实名验证
    String SET_REALNAME_TYPE = "real_type";

    //选择银行卡
    String SET_BANK_INTENT = "bank_intent";

    //安全卡
    String SAFETY_CARD = "SafetyCard";

    //平台余额
    String PLATFORM_BALANCE = "platform_balance";

    //支付详情,升级VIP名称
    String UP_NAME_VIP = "up_name_vip";

    //支付详情,升级VIP费用
    String UP_FREE_VIP = "up_free_vip";

    //支付详情,升级VIP类型
    String UP_SELECT_TYPE = "selectUserType";

    //账单id
    String ORDER_ID = "orderId";

    //bankName
    String BANK_NAME = "bank_name";
    //省份
    String BANK_PROVICE = "bank_provice";
    //城市
    String BANK_CITY = "bank_city";


    String Notice_Details  = "Notice_Details_Id";

    String Message_Details  = "Message_Details";


}
