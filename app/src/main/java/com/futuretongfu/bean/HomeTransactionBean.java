package com.futuretongfu.bean;

import com.futuretongfu.base.BaseSerializable;

/**
 * 首页交易列表
 *
 * @author DoneYang 2017/7/11
 */

public class HomeTransactionBean extends BaseSerializable {

    public int id;

    public long userId;

    public String businessType;

    public String businessTypeName;

    public String businessNo;

    public String status;

    public double money;

    public long createTime;

    public double totalMoney;

    public double realMoney;

    public double fee;

    public String tradeType;//1:收入 0:支出

}
