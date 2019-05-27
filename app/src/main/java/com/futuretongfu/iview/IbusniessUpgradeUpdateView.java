package com.futuretongfu.iview;

import com.futuretongfu.base.BaseSerializable;
import com.futuretongfu.bean.WlsqTypeBean;

import java.util.List;

/**
 * 添加收藏
 *
 * @author DoneYang 2017/6/28
 */

public interface IbusniessUpgradeUpdateView extends IView {

    void busniessUpgradeFail(int code, String msg);
    void busniessUpgradeSuccess(BaseSerializable data);

    public void onUpDataPicFaile(String msg);
    public void onUpDataPicPercentage(float percentage);




}
