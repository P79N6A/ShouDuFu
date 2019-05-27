package com.futuretongfu.bean;

import com.futuretongfu.base.BaseSerializable;

public class HomeIncomeBean extends BaseSerializable {

    private String moneyByDay;
    private String moneyByMonth;

    public String getMoneyByDay() {
        return moneyByDay;
    }

    public void setMoneyByDay(String moneyByDay) {
        this.moneyByDay = moneyByDay;
    }

    public String getMoneyByMonth() {
        return moneyByMonth;
    }

    public void setMoneyByMonth(String moneyByMonth) {
        this.moneyByMonth = moneyByMonth;
    }
}
