package com.futuretongfu.utils;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.futuretongfu.R;
import com.futuretongfu.WeiLaiFuApplication;
import com.futuretongfu.constants.Constants;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ChenXiaoPeng on 2017/6/26.
 */

public class StringUtil {

    /**
     * 格式化数字为千分位显示；
     *
     * @param val 要格式化的数字
     * @return
     */
    public static String fmtMicrometer(double val) {
        String text = getDouble2(val);
        DecimalFormat df = null;

        if (text.indexOf(".") > 0) {
            if (text.length() - text.indexOf(".") - 1 == 0) {
                df = new DecimalFormat("###,##0.");
            } else if (text.length() - text.indexOf(".") - 1 == 1) {
                df = new DecimalFormat("###,##0.0");
            } else {
                df = new DecimalFormat("###,##0.00");
            }
        } else {
            df = new DecimalFormat("###,##0");
        }

        double number = 0.0;
        try {
            number = Double.parseDouble(text);
        } catch (Exception e) {
            number = 0.00;
        }

        return df.format(number);
    }

    public static String getDouble2(double val) {
        return String.format(WeiLaiFuApplication.getContext().getResources().getString(R.string.double_string_2), val);
    }

    public static String replaceAllSpace(String text) {
        return text.trim().replaceAll(" ", "");
    }

    public static String getSafeString(String str) {
        if (null == str)
            return "";

        return str;
    }

    public static String getNoEmptyStr(String str, String defaultStr) {
        return TextUtils.isEmpty(str) ? defaultStr : str;
    }

    public static String getGender(int gender) {
        String str;
        switch (gender) {
            case Constants.Gender_Man:
                str = "男";
                break;

            case Constants.Gender_Woman:
                str = "女";
                break;

            case Constants.Gender_Secrecy:
                str = "保密";
                break;

            default:
                str = "保密";
                break;
        }

        return str;
    }

    public static String getOrderConsumerStatues(int orderStatus) {
        String str = "";
        switch (orderStatus) {
            case Constants.OrderConsumer_Status_Pay://待付款
                str = "待付款";
                break;
            case Constants.OrderConsumer_Status_Recipient://待收货
                str = "待收货";
                break;
            case Constants.OrderConsumer_Status_Finish://已完成
                str = "已完成";
                break;
            case Constants.OrderConsumer_Status_Refunding://退款中
                str = "退款中";
                break;
            case Constants.OrderConsumer_Status_Comment://待评价
                str = "退款成功";
                break;
        }

        return str;
    }

    public static String getOrderOnlineStatues(int orderStatus) {
        String str = "";
        switch (orderStatus) {
            case Constants.OrderOnline_Status_Pay://待付款
                str = "等待买家付款";
                break;
            case Constants.OrderOnline_Status_Ship://待发货
                str = "买家已付款";
                break;
            case Constants.OrderOnline_Status_Recipient://待收货
                str = "卖家已发货";
                break;
            case Constants.OrderOnline_Status_Comment://待评价
                str = "待评价";
                break;
            case Constants.OrderOnline_Status_Refunding://退款中
                str = "退款中";
                break;
            case Constants.OrderOnline_Status_RefundingFinish://退款成功
                str = "退款成功";
                break;
            case Constants.OrderOnline_Status_Cancel://取消
                str = "买家已取消";
                break;
            case Constants.OrderOnline_Status_Expired://过期
                str = "订单已过期";
                break;
            case Constants.OrderOnline_Status_YiFinish://取消
                str = "交易完成";
                break;
        }

        return str;
    }
    /**
     * 验证手机号是否合法
     *
     * @param number
     * @return
     */
    public static boolean isPhoneNumber(String number) {
        if (TextUtils.isEmpty(number)) {
            return false;
        }
        if (number.length() != 11) {
            return false;
        }
        Pattern compile = Pattern.compile("1[34578]\\d{9}");
        Matcher matcher = compile.matcher(number);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 姓名隐藏
     *
     * @param name
     * @return
     */
    public static String setVisibilityName(String name) {
        if (name.length() == 2) {
            name = name.replace(name.substring(1, 2), "*");
        } else {
            name = name.replace(name.substring(1, name.length() - 1), "*");
        }
        return name;
    }


    /**
     * 对字符串处理:将指定位置到指定位置的字符以星号代替
     *
     * @param content 传入的字符串
     * @param begin   开始位置
     * @param end     结束位置
     * @return
     */
    public static String getStarString(String content, int begin, int end) {

        if (begin >= content.length() || begin < 0) {
            return content;
        }
        if (end >= content.length() || end < 0) {
            return content;
        }
        if (begin >= end) {
            return content;
        }
        String starStr = "";
        for (int i = begin; i < end; i++) {
            starStr = starStr + "*";
        }
        return content.substring(0, begin) + starStr + content.substring(end, content.length());

    }

    /**
     * 字符串只取第一个字符，其它字符转换成*
     *
     * @param content
     * @param begin   1
     * @param end     String.length
     * @return
     */
    public static String getStarString3(String content, int begin, int end) {

//        if (begin >= content.length() || begin < 0) {
//            return content;
//        }
//
//        if (begin >= end) {
//            return content;
//        }
        begin = 1;
        String starStr = "";
        for (int i = begin; i < end; i++) {
            starStr = starStr + "*";
        }
        return content.substring(0, begin) + starStr + content.substring(end, content.length());

    }


    //对字符串处理:将头尾位置的字符以星号代替
    public static String getStarString(String content) {

        int begin = 1;
        int end = content.length() - 1;

        if (begin >= content.length() || begin < 0) {
            return content;
        }
        if (end >= content.length() || end < 0) {
            return content;
        }
        if (begin >= end) {
            return content;
        }
        String starStr = "";
        for (int i = begin; i < end; i++) {
            starStr = starStr + "*";
        }
        return content.substring(0, begin) + starStr + content.substring(end, content.length());

    }

    /**
     * 对字符加星号处理：除前面几位和后面几位外，其他的字符以星号代替
     *
     * @param content  传入的字符串
     * @param frontNum 保留前面字符的位数
     * @param endNum   保留后面字符的位数
     * @return 带星号的字符串
     */

    public static String getStarString2(String content, int frontNum, int endNum) {

        if (frontNum >= content.length() || frontNum < 0) {
            return content;
        }
        if (endNum >= content.length() || endNum < 0) {
            return content;
        }
        if (frontNum + endNum >= content.length()) {
            return content;
        }
        String starStr = "";
        for (int i = 0; i < (content.length() - frontNum - endNum); i++) {
            starStr = starStr + "*";
        }
        return content.substring(0, frontNum) + starStr
                + content.substring(content.length() - endNum, content.length());

    }


    /**
     * 根据JSONArray String获取到List
     *
     * @param <T>
     * @param <T>
     * @param jArrayStr
     * @return
     */
    public static <T> List<T> getListByArray(Class<T> class1, String jArrayStr) {
        List<T> list = new ArrayList<>();
        JSONArray jsonArray = JSONArray.parseArray(jArrayStr);
        if (jsonArray == null || jsonArray.isEmpty()) {
            return list;//nerver return null
        }
        for (Object object : jsonArray) {
            JSONObject jsonObject = (JSONObject) object;
            T t = JSONObject.toJavaObject(jsonObject, class1);
            list.add(t);
        }
        return list;
    }


    /**
     * 校验是否为相同的字符
     *
     * @param str
     * @return
     */
    public static boolean isSameNumber(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        boolean result = false;
        char[] ch = str.toCharArray();
        String i = String.valueOf(ch[0]);
        for (char c : ch) {
            String j = String.valueOf(c);
            if (i.equals(j)) {
                result = true;
            } else {
                result = false;
                return result;
            }
        }
        return result;
    }

    // 将汉字转换为全拼
    public static String getPingYin(String src) {

        char[] t1 = null;
        t1 = src.toCharArray();
        String[] t2 = new String[t1.length];
        HanyuPinyinOutputFormat t3 = new HanyuPinyinOutputFormat();

        t3.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        t3.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        t3.setVCharType(HanyuPinyinVCharType.WITH_V);
        String t4 = "";
        int t0 = t1.length;
        try {
            for (int i = 0; i < t0; i++) {
                // 判断是否为汉字字符
                if (java.lang.Character.toString(t1[i]).matches(
                        "[\\u4E00-\\u9FA5]+")) {
                    t2 = PinyinHelper.toHanyuPinyinStringArray(t1[i], t3);
                    t4 += t2[0];
                } else
                    t4 += java.lang.Character.toString(t1[i]);
            }
            // System.out.println(t4);
            return t4;
        } catch (BadHanyuPinyinOutputFormatCombination e1) {
            e1.printStackTrace();
        }
        return t4;
    }

    public static String getOrderPayCashPayType(String payType) {
        //MPAY 汇付天下  ALIPAY  支付宝  WECHATPAY 微信
        if (payType == null || TextUtils.isEmpty(payType))
            return "";

        switch (payType) {
            case Constants.OrderPayCash_PayType_PlatformBalance://平台余额
                return "平台余额";
            case Constants.OrderPayCash_PayType_TradeBalance://商圈余额
                return "商圈余额";
            case Constants.OrderPayCash_PayType_PlatformTrade://平台+商圈余额
                return "平台+商圈余额";
            case Constants.OrderPayCash_PayType_ThirdPay://第三方支付
                return "第三方支付";
            case Constants.MPAY:
                return "银行卡";
            case Constants.ALIPAY:
                return "支付宝";
            case Constants.WECHATPAY:
                return "微信";
            case Constants.ALIPAY_BALANCE:
                return "支付宝+平台余额";
            case Constants.WECHATPAY_BALANCE:
                return "微信+平台余额";
            default:
                return "";
        }
    }

    /**
     * 四舍五入
     * @param number
     * @return
     */
    public static double mathRoundNumber(double number){
        BigDecimal num   =   new   BigDecimal(number);
        double   f   =   num.setScale(2,   RoundingMode.HALF_UP).doubleValue();
        return f;
    }

}
