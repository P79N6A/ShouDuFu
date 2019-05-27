package com.futuretongfu.iview;

import com.futuretongfu.model.entity.MessageListInfo;

import java.util.List;

/**
 * Created by weiang on 2017/7/27.
 */

public interface HomeMessageListIView {

    void onSystemMessageListFail(int code, String msg);

    void onSystemMessageListSuccess(List<MessageListInfo> data);

}
