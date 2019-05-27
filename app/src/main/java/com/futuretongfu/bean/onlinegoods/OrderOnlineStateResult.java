package com.futuretongfu.bean.onlinegoods;

import java.util.List;

/**
 * Created by ChenXiaoPeng on 2017/7/5.
 */

public class OrderOnlineStateResult {

    private String LogisticCode;
    private String EBusinessID;
    private String ShipperCode;
    private String ShipperName;
    private String State;
    private List<OrderOnlineStateBean> Traces;

    public List<OrderOnlineStateBean> getTraces() {
        return Traces;
    }

    public void setTraces(List<OrderOnlineStateBean> traces) {
        Traces = traces;
    }

    public String getEBusinessID() {
        return EBusinessID;
    }

    public void setEBusinessID(String EBusinessID) {
        this.EBusinessID = EBusinessID;
    }

    public String getShipperCode() {
        return ShipperCode;
    }

    public void setShipperCode(String shipperCode) {
        ShipperCode = shipperCode;
    }

    public String getLogisticCode() {
        return LogisticCode;
    }

    public void setLogisticCode(String logisticCode) {
        LogisticCode = logisticCode;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getShipperName() {
        return ShipperName;
    }

    public void setShipperName(String shipperName) {
        ShipperName = shipperName;
    }
}
