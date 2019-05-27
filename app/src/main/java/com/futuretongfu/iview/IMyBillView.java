package com.futuretongfu.iview;

import com.futuretongfu.model.entity.MyBillEntity;
import com.futuretongfu.model.entity.MyBillResult;

import java.util.List;

/**
 * Created by ChenXiaoPeng on 2017/7/10.
 */

public interface IMyBillView {

    public void onMyBillDnRefreshSuccess(MyBillResult datas);
    public void onMyBillDnRefreshFaile(String msg);

    public void onMyBillUpLoadSuccess(List<MyBillEntity> datas);
    public void onMyBillUpLoadFaile(String msg);
    public void onMyBillUpLoadNoDatas();
}
