package com.futuretongfu.presenter.home;

import android.content.Context;

import com.futuretongfu.base.BaseSerializable;
import com.futuretongfu.bean.NoticeBean;
import com.futuretongfu.iview.ITransMessageView;
import com.futuretongfu.iview.SystemNoticeListIView;
import com.futuretongfu.model.entity.MessageListInfo;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.HomeRepository;
import com.futuretongfu.presenter.Presenter;

import java.util.List;

/**
 * 系统公告列表
 *
 * @author DoneYang 2017/7/1
 */

public class SystemNoticeListPresenter extends Presenter {

    private HomeRepository homeRepository;
    private SystemNoticeListIView iView;
    private ITransMessageView iTransMessageView;

    public SystemNoticeListPresenter(Context context, SystemNoticeListIView iView) {
        super(context);
        this.iView = iView;
        this.homeRepository = new HomeRepository();
    }

    public SystemNoticeListPresenter(Context context, ITransMessageView iView) {
        super(context);
        this.iTransMessageView = iView;
        this.homeRepository = new HomeRepository();
    }



    @Override
    public void onDestroy(){
        if(homeRepository != null) homeRepository.cancel();
    }

    /**
     * 系统公告
     *
     * @param userId
     * @param page
     */
    public void onSystemNoticeList(String userId, int page) {
        homeRepository.onSystemNoticeList(userId, page, new BaseRepository.HttpCallListener<List<NoticeBean.ListBean>>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iView != null) {
                    iView.onSystemNoticeListFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(List<NoticeBean.ListBean> data) {
                if (iView != null) {
                    iView.onSystemNoticeListSuccess(data);
                }
            }
        });
    }

    /**
     * 消息状态更新
     *
     * @param userId
     * @param msgId
     */
    public void onMessageStatus(String userId, String msgId) {
        homeRepository.onMessageStatus(userId, msgId, new BaseRepository.HttpCallListener<BaseSerializable>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iView != null) {
                    iView.onFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(BaseSerializable data) {
                if (iView != null) {
                    iView.onSuccess(data);
                }
            }
        });
    }

    /**
     * 交易信息列表
     * @param userId
     * @param typeId
     */
    public void onTransactionMessageList(String userId, int typeId) {
        homeRepository.onTransMessageList(userId, typeId, new BaseRepository.HttpCallListener<List<MessageListInfo>>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iTransMessageView != null) {
                    iTransMessageView.onTransactionMessageListFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(List<MessageListInfo> data) {
                if (iTransMessageView != null) {
                    iTransMessageView.onTransactionMessageListSuccess(data);
                }
            }
        });
    }
}
