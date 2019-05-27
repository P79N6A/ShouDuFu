package com.futuretongfu.iview;

import com.futuretongfu.bean.UploadVoucheResult;
import com.futuretongfu.model.entity.FuturePayApiResult;

/**
 * Created by ChenXiaoPeng on 2017/7/5.
 */

public interface IUploadVoucheListView {
    // 下拉刷新成功
    public void onUploadVoucheDwUpdateSuccess(UploadVoucheResult datas);
    // 下拉刷新失败
    public void onUploadVoucheDwUpdateFaile(String msg);

    // 上拉加载成功
    public void onUploadVoucheUpLoadSuccess(UploadVoucheResult datas);
    // 上拉加载失败
    public void onUploadVoucheUpLoadFaile(String msg);
    // 上拉加载 没有数据
    public void onUploadVoucheUpLoadNoDatas();
    //支付
    void onPaymentFail(int code, String msg);
    void onPaymentSuccess(String data);

    //删除
    void onUploadVoucheDelFail(int code, String msg);
    void onUploadVoucheDelSuccess(FuturePayApiResult data);
}
