package com.futuretongfu.presenter.home;

import android.content.Context;

import com.futuretongfu.iview.HomeMessageListIView;
import com.futuretongfu.iview.SystemMessageListIView;
import com.futuretongfu.model.entity.MessageListInfo;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.HomeRepository;

import java.util.List;

/**
 * @author DoneYang 2017/7/1
 */

public class SystemMessageListPresenter extends Presenter {

    private HomeRepository homeRepository;

    private SystemMessageListIView iView;
    private HomeMessageListIView ihomeView;

    public SystemMessageListPresenter(Context context, SystemMessageListIView iView) {
        super(context);
        this.iView = iView;
        this.homeRepository = new HomeRepository();
    }


    public SystemMessageListPresenter(Context context, HomeMessageListIView iView) {
        super(context);
        this.ihomeView = iView;
        this.homeRepository = new HomeRepository();
    }

    @Override
    public void onDestroy() {
        if (homeRepository != null) homeRepository.cancel();
    }

    public void onSystemMessageList(String userId, int page) {
        homeRepository.onSystemMessageList(userId, page, new BaseRepository.HttpCallListener<List<MessageListInfo>>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (ihomeView != null) {
                    ihomeView.onSystemMessageListFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(List<MessageListInfo> data) {
                if (ihomeView != null) {
                    ihomeView.onSystemMessageListSuccess(data);
                }
            }
        });
    }
}
