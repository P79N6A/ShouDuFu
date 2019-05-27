package com.futuretongfu.iview;
/*
 * Created by hhm on 2017/7/26.
 */

import com.futuretongfu.bean.onlinegoods.OrderOnlineStateBean;
import com.futuretongfu.bean.onlinegoods.OrderOnlineStateResult;

import java.util.List;

public interface IOrderOnlineStateView {
    public void onOrderOnlineStateSuccess(OrderOnlineStateResult data);
    public void onOrderOnlineStateFaile(String msg);

}
