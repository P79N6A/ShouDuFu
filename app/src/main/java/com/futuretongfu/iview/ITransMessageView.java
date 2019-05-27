package com.futuretongfu.iview;

import com.futuretongfu.model.entity.MessageListInfo;

import java.util.List;

/**
 * Created by Lenovo on 2017/7/31.
 */

public interface ITransMessageView {

    void onTransactionMessageListFail(int code, String msg);

    void onTransactionMessageListSuccess(List<MessageListInfo> data);

}
