package com.futuretongfu.iview;

import com.futuretongfu.bean.NoticeBean;

import java.util.List;

/**
 * 系统公告
 *
 * @author DoneYang 2017/7/1
 */

public interface SystemNoticeListIView extends IView {

    void onSystemNoticeListFail(int code, String msg);

    void onSystemNoticeListSuccess(List<NoticeBean.ListBean> data);
}
