package com.futuretongfu.iview;

import com.futuretongfu.model.entity.BillDetail;

/**
 * Created by ChenXiaoPeng on 2017/7/12.
 */

public interface IBillDetailView {

    public void onBillDetailSuccess(BillDetail data);
    public void onBillDetailFaile(String msg);

}
