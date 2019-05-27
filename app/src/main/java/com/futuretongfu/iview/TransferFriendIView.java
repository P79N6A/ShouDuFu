package com.futuretongfu.iview;

import com.futuretongfu.bean.FriendBean;

import java.util.List;

/**
 * 转账好友
 *
 * @author DoneYang 2017/6/25
 */

public interface TransferFriendIView {

    void onTransferFriendSuccess(List<FriendBean> data);

    void TransferFriendFail(int code, String msg);
}
