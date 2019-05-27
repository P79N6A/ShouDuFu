package com.futuretongfu.bean;

/**
 * Created by GF-Zhang PC on 2017/12/19.
 */

public class ExchangeListBean {
    private String id;
    private String title;
    private String time;
    private String exchangePrice;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getExchangePrice() {
        return exchangePrice;
    }

    public void setExchangePrice(String exchangePrice) {
        this.exchangePrice = exchangePrice;
    }
}
