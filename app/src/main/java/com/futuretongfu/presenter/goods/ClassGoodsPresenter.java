package com.futuretongfu.presenter.goods;

import android.content.Context;

import com.futuretongfu.bean.classification.ClassOneListViewBean;
import com.futuretongfu.iview.IClassGoodsView;
import com.futuretongfu.model.repository.BaseRepository;
import com.futuretongfu.model.repository.ClassGoodsRepository;
import com.futuretongfu.presenter.Presenter;

import java.util.List;


/**
 * Created by zhanggf on 2018/5/10.
 * 分类
 */

public class ClassGoodsPresenter extends Presenter {

    private IClassGoodsView iClassGoodsView;
    private ClassGoodsRepository classGoodsRepository;

    public ClassGoodsPresenter(Context context, IClassGoodsView iClassGoodsView) {
        super(context);
        this.iClassGoodsView = iClassGoodsView;
        classGoodsRepository = new ClassGoodsRepository();
    }

    @Override
    public void onDestroy() {
        if (classGoodsRepository != null)
            classGoodsRepository.cancel();
    }

    /**
     * 获取分类列表
     */
    public void getClassGoodsList() {
        classGoodsRepository.getClassGoodsList( new BaseRepository.HttpCallListener<List<ClassOneListViewBean>>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iClassGoodsView != null)
                    iClassGoodsView.onClassGoodsListViewFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(List<ClassOneListViewBean> data) {
                if(iClassGoodsView != null) {
                    iClassGoodsView.onClassGoodsListViewSuccess(data);
                }
            }
        });
    }

    /**
     * 获取分类列表
     */
    public void getClassLastModifyTime() {
        classGoodsRepository.getClassLastModifyTime( new BaseRepository.HttpCallListener<String>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iClassGoodsView != null)
                    iClassGoodsView.onClassGoodsListViewFaile(msg);
            }

            @Override
            public void onHttpCallSuccess(String data) {
                if(iClassGoodsView != null) {
                    iClassGoodsView.gonClassLastModifyTimeSuccess(data);
                }
            }
        });
    }


}
