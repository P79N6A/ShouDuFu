package com.futuretongfu.bean.onlinegoods;

import com.futuretongfu.base.BaseSerializable;

/**
 * Created by zhanggf on 2018/5/18.
 */

public class GoodsSearchTermBean extends BaseSerializable {
    private String dictLabel;
    private String dictValue;
    private String dictType;
    private String description;

    public String getDictLabel() {
        return dictLabel;
    }

    public void setDictLabel(String dictLabel) {
        this.dictLabel = dictLabel;
    }

    public String getDictValue() {
        return dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
