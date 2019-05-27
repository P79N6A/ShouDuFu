package com.futuretongfu.iview;

import com.futuretongfu.bean.onlinegoods.SaleReturnDetailsBean;


/**
 * Created by zhanggf on 2018/5/10.
 * 收货地址
 */

public interface ISaleReturnDetailsView {

    public void onSaleReturnDetailsSuccess(SaleReturnDetailsBean datas);
    public void onSaleReturnDetailsFaile(String msg);


}
