package com.futuretongfu.presenter.user;

import android.content.Context;

import com.futuretongfu.iview.IRecommendHeadView;
import com.futuretongfu.iview.IRecommendListView;
import com.futuretongfu.model.entity.RecommendHeadInfo;
import com.futuretongfu.model.entity.RecommendListInfo;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.RecommendRepository;
import com.futuretongfu.presenter.Presenter;

/**
 * 类:    RecommendPresenter
 * 描述:  我的推荐
 * 作者： weiang on 2017/6/28
 */
public class RecommendPresenter extends Presenter {

    private Context context;
    private IRecommendHeadView iRecommendView;
    private IRecommendListView iRecommendListView;
    private RecommendRepository recommendRepository;

    public RecommendPresenter(Context context, IRecommendHeadView iRecommendView) {
        super(context);
        this.context = context;
        this.iRecommendView = iRecommendView;
        recommendRepository = new RecommendRepository();
    }

    public RecommendPresenter(Context context, IRecommendListView iRecommendView) {
        super(context);
        this.context = context;
        this.iRecommendListView = iRecommendView;
        recommendRepository = new RecommendRepository();
    }

    @Override
    public void onDestroy(){
        if(recommendRepository != null)
            recommendRepository.cancel();
    }

    /**
     * 获取我的推荐奖励信息
     *
     * @param userId
     */
    public void getRecommentHead(String userId) {
        recommendRepository.recommendHeadInfo(userId, new BaseRepository.HttpCallListener<RecommendHeadInfo>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                iRecommendView.onRecommendHeadFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(RecommendHeadInfo recommendHeadInfo) {
                if (recommendHeadInfo == null) {
                    iRecommendView.onRecommendHeadFaile("数据获取失败");
                    return;
                }
                iRecommendView.onRecommendHeadSuccess(recommendHeadInfo);
            }
        });
    }

    /**
     * 推荐列表
     *
     * @param userId
     */
    public void getRecommentList(String userId, int pageNumber, int typeId) {
        recommendRepository.recommendListInfo(userId, pageNumber, typeId,new BaseRepository.HttpCallListener<RecommendListInfo>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if(iRecommendListView!=null){
                    iRecommendListView.onRecommendListFaile(msg);
                }
            }

            @Override
            public void onHttpCallSuccess(RecommendListInfo recommendListInfo) {
                if (recommendListInfo==null) {
                    iRecommendListView.onRecommendListFaile("数据获取失败");
                    return;
                }
                iRecommendListView.onRecommendListSuccess(recommendListInfo);
            }
        });
    }


}
