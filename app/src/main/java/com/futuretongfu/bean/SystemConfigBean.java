package com.futuretongfu.bean;


import com.futuretongfu.base.BaseSerializable;

public class SystemConfigBean extends BaseSerializable {
    private String id;
    private String fieldName;
    private String remark;
    private int fieldValue;  //服务费率（16表示16%）

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(int fieldValue) {
        this.fieldValue = fieldValue;
    }
}
