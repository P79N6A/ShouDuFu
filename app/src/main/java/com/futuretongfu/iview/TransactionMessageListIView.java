package com.futuretongfu.iview;

import com.futuretongfu.bean.TransactionMessageListBean;

/**
 * @author DoneYang 2017/7/3
 */

public interface TransactionMessageListIView extends IView {

    void onTransactionMessageListFail(int code, String msg);

    void onTransactionMessageListSuccess(TransactionMessageListBean data);
}
