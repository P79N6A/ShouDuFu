package com.futuretongfu.presenter.home;

import android.content.Context;

import com.futuretongfu.bean.FriendBean;
import com.futuretongfu.iview.IView;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.HomeRepository;
import com.futuretongfu.presenter.Presenter;

/**
 * 搜索好友
 *
 * @author DoneYang 2017/6/27
 */

public class SearchFriendPresenter extends Presenter {

    private HomeRepository homeRepository;
    private IView iView;

    public SearchFriendPresenter(Context context, IView iView) {
        super(context);
        this.iView = iView;
        this.homeRepository = new HomeRepository();
    }

    @Override
    public void onDestroy(){
        if(homeRepository != null) homeRepository.cancel();
    }

    /**
     * 搜索好友
     *
     * @param isSearch
     * @param userId
     * @param key
     */
    public void onSearchFriend(boolean isSearch, String userId, String key) {
        homeRepository.onSearchFriend(isSearch, userId, key, new BaseRepository.HttpCallListener<FriendBean>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iView != null) {
                    iView.onFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(FriendBean data) {
                if (iView != null) {
                    iView.onSuccess(data);
                }
            }
        });
    }
}
