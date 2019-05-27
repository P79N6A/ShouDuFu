package com.futuretongfu.bean;

import com.futuretongfu.base.BaseSerializable;

/**
 * 买单支付
 *
 * @author DoneYang 2017/7/3
 */

public class PaySetMoneyBean extends BaseSerializable {

    public String payType;

    public long orderNo;

    public long createTime;

    public String storeName;

    public double realMoney;

    public double bussMoney;//商圈支付金额

    public double platMoney;//平台支付金额

    public String payTypeName;

}
