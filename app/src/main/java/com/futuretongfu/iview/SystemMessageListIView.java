package com.futuretongfu.iview;

import com.futuretongfu.bean.NoticeBean;

import java.util.List;

/**
 * 系统消息页面
 *
 * @author DoneYang 2017/7/1
 */

public interface SystemMessageListIView extends IView {

    void onSystemMessageListFail(int code, String msg);

    void onSystemMessageListSuccess(List<NoticeBean.ListBean> data);
}
