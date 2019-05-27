package com.futuretongfu.iview;

import com.futuretongfu.bean.WlsqTypeBean;
import com.futuretongfu.base.BaseSerializable;

import java.util.List;

/**
 * 添加收藏
 *
 * @author DoneYang 2017/6/28
 */

public interface IbusniessUpgradeView extends IView {

    void busniessUpgradeFail(int code, String msg);

    void busniessUpgradeSuccess(BaseSerializable data);

    //行业type
    void storeTypeSuccess(List<WlsqTypeBean> data);

    void storeTypeFail(int code, String msg);

    public void onUpDataPicFaile(String msg);
    public void onUpDataPicPercentage(float percentage);




}
