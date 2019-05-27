package com.futuretongfu.presenter.goods;

import android.content.Context;

import com.futuretongfu.bean.classification.ClassOneListViewBean;
import com.futuretongfu.bean.onlinegoods.HomeSortBean;
import com.futuretongfu.iview.IClassDetailsView;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.ClassGoodsRepository;
import com.futuretongfu.model.repository.HomeRepository;
import com.futuretongfu.presenter.Presenter;

import java.util.List;


/**
 * Created by zhanggf on 2018/5/10.
 * 分类
 */

public class ClassDetailsPresenter extends Presenter {

    private IClassDetailsView iClassGoodsView;
    private HomeRepository homeRepository;
    private ClassGoodsRepository classGoodsRepository;

    public ClassDetailsPresenter(Context context, IClassDetailsView iClassGoodsView) {
        super(context);
        this.iClassGoodsView = iClassGoodsView;
        classGoodsRepository = new ClassGoodsRepository();
        homeRepository = new HomeRepository();
    }

    @Override
    public void onDestroy() {
        if (classGoodsRepository != null)
            classGoodsRepository.cancel();
        if (homeRepository != null)
            homeRepository.cancel();
    }

    /**
     * 首页分类
     */
    public void onSortList(String parentId) {
        homeRepository.onHomeSort(parentId,new BaseRepository.HttpCallListener<List<HomeSortBean>>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iClassGoodsView != null) {
                    iClassGoodsView.onSortListFail(code, msg);
                }
            }

            @Override
            public void onHttpCallSuccess(List<HomeSortBean> data) {
                if (iClassGoodsView != null) {
                    iClassGoodsView.onSortListSuccess(data);
                }
            }
        });
    }

}
