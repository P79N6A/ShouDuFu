package com.futuretongfu.iview;

import com.futuretongfu.bean.TransferListBean;

import java.util.List;

/**
 * 转账最近联系人
 *
 * @author DoneYang 2017/7/4
 */

public interface TransferContactIVew extends IView {

    void onTransferContactFail(int code, String msg);

    void onTransferContactSuccess(List<TransferListBean> data);
}
