package com.futuretongfu.iview;

import com.futuretongfu.bean.onlinegoods.SaleReturnBean;
import com.futuretongfu.model.entity.FuturePayApiResult;
import com.futuretongfu.model.entity.WareHorseAddressEntity;

import java.util.List;

/**
 * Created by zhanggf on 2018/5/10.
 * 收货地址
 */

public interface ISaleReturnView {

    public void onSaleReturnSuccess(List<SaleReturnBean> datas);
    public void onSaleReturnFaile(String msg);


}
