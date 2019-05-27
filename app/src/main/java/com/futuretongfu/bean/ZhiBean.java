package com.futuretongfu.bean;

import java.util.List;

public class ZhiBean {

    /**
     * onlineOrderId : 608
     * onlineOrderNo : 2018102117314534603
     * userId : 1153992947842800
     * onlineStoreId : 1039032620893347840
     * onlineStoreName : 迪奥女装
     * totalMoney : 2390
     * terminal : Android
     * leaveMessage :
     * logisticsFee : 0
     * realMoney : 2390
     * realCash : null
     * addressId : 786
     * ip : 114.241.221.174
     * ratio : null
     * skuList : [{"skuId":"1039319583294758912","skuName":"迪奥连衣裙 2183L489","price":2390,"amount":1,"totalPrice":2390,"skuImages":"http://admin.shoudufu.com/shoudufu/userfiles/fileupload/201809/1039319577473064960.jpg","format":"迪奥连衣裙 2183L489","isReturn":null}]
     */

    private String onlineOrderId;
    private String onlineOrderNo;
    private String userId;
    private String onlineStoreId;
    private String onlineStoreName;
    private double totalMoney;
    private String terminal;
    private String leaveMessage;
    private int logisticsFee;
    private double realMoney;
    private Object realCash;
    private String addressId;
    private String ip;
    private Object ratio;
    private List<SkuListBean> skuList;

    public String getOnlineOrderId() {
        return onlineOrderId;
    }

    public void setOnlineOrderId(String onlineOrderId) {
        this.onlineOrderId = onlineOrderId;
    }

    public String getOnlineOrderNo() {
        return onlineOrderNo;
    }

    public void setOnlineOrderNo(String onlineOrderNo) {
        this.onlineOrderNo = onlineOrderNo;
    }

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

    public String getOnlineStoreName() {
        return onlineStoreName;
    }

    public void setOnlineStoreName(String onlineStoreName) {
        this.onlineStoreName = onlineStoreName;
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

    public int getLogisticsFee() {
        return logisticsFee;
    }

    public void setLogisticsFee(int logisticsFee) {
        this.logisticsFee = logisticsFee;
    }

    public double getRealMoney() {
        return realMoney;
    }

    public void setRealMoney(double realMoney) {
        this.realMoney = realMoney;
    }

    public Object getRealCash() {
        return realCash;
    }

    public void setRealCash(Object realCash) {
        this.realCash = realCash;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Object getRatio() {
        return ratio;
    }

    public void setRatio(Object ratio) {
        this.ratio = ratio;
    }

    public List<SkuListBean> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<SkuListBean> skuList) {
        this.skuList = skuList;
    }

    public static class SkuListBean {
        /**
         * skuId : 1039319583294758912
         * skuName : 迪奥连衣裙 2183L489
         * price : 2390
         * amount : 1
         * totalPrice : 2390
         * skuImages : http://admin.shoudufu.com/shoudufu/userfiles/fileupload/201809/1039319577473064960.jpg
         * format : 迪奥连衣裙 2183L489
         * isReturn : null
         */

        private String skuId;
        private String skuName;
        private double price;
        private double amount;
        private double totalPrice;
        private String skuImages;
        private String format;
        private Object isReturn;

        public String getSkuId() {
            return skuId;
        }

        public void setSkuId(String skuId) {
            this.skuId = skuId;
        }

        public String getSkuName() {
            return skuName;
        }

        public void setSkuName(String skuName) {
            this.skuName = skuName;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public double getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(double totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getSkuImages() {
            return skuImages;
        }

        public void setSkuImages(String skuImages) {
            this.skuImages = skuImages;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public Object getIsReturn() {
            return isReturn;
        }

        public void setIsReturn(Object isReturn) {
            this.isReturn = isReturn;
        }
    }
}
