package com.futuretongfu.iview;

import com.futuretongfu.bean.TransferBean;

/**
 * 转账-对方账户
 *
 * @author DoneYang 2017/7/6
 */

public interface TransferAccountIView extends IView {

    void onTransferAccountSuccess(TransferBean data);

    void onTransferAccountFail(int code, String msg);
}
