package com.futuretongfu.bean;

public class RecommendBean {

    /**
     * code : 1
     * msg : 成功
     * data : {"avlBal":10}
     */

    private String code;
    private String msg;
    private DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * avlBal : 10.0
         */

        private double avlBal;

        public double getAvlBal() {
            return avlBal;
        }

        public void setAvlBal(double avlBal) {
            this.avlBal = avlBal;
        }
    }
}
