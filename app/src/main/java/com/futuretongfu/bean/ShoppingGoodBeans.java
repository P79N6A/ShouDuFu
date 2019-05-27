package com.futuretongfu.bean;

import java.io.Serializable;
import java.util.List;

public class ShoppingGoodBeans implements Serializable {

    /**
     * userId : 1152388052372576
     * onlineStoreId : 996560552650670080
     * totalMoney : 260.0
     * terminal : PC
     * leaveMessage : 请发申通快递
     * logisticsFee : 20.0
     * realMoney : 240.0
     * realCash : 50
     * addressId : 51
     * skuList : [{"skuId":"999185882988445696","price":120,"amount":1,"totalPrice":120,"format":"规格|规格|规格"},{"skuId":"996644222392537088","price":120,"amount":1,"totalPrice":120,"format":"规格|规格|规格"}]
     */

    private String userId;
    private String onlineStoreId;
    private double totalMoney;
    private String terminal;
    private String leaveMessage;
    private double logisticsFee;
    private double realMoney;
    private double realCash;
    private String addressId;
    private List<SkuListBean> skuList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOnlineStoreId() {
        return onlineStoreId;
    }

    public void setOnlineStoreId(String onlineStoreId) {
        this.onlineStoreId = onlineStoreId;
    }

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getLeaveMessage() {
        return leaveMessage;
    }

    public void setLeaveMessage(String leaveMessage) {
        this.leaveMessage = leaveMessage;
    }

    public double getLogisticsFee() {
        return logisticsFee;
    }

    public void setLogisticsFee(double logisticsFee) {
        this.logisticsFee = logisticsFee;
    }

    public double getRealMoney() {
        return realMoney;
    }

    public void setRealMoney(double realMoney) {
        this.realMoney = realMoney;
    }

    public double getRealCash() {
        return realCash;
    }

    public void setRealCash(double realCash) {
        this.realCash = realCash;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public List<SkuListBean> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<SkuListBean> skuList) {
        this.skuList = skuList;
    }

    public static class SkuListBean {
        /**
         * skuId : 999185882988445696
         * price : 120.0
         * amount : 1
         * totalPrice : 120.0
         * format : 规格|规格|规格
         */

        private String skuId;
        private String price;
        private String amount;
        private String totalPrice;
        private String format;

        public String getSkuId() {
            return skuId;
        }

        public void setSkuId(String skuId) {
            this.skuId = skuId;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(String totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }
    }
}
