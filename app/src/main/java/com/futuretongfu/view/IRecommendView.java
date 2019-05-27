package com.futuretongfu.view;

import com.futuretongfu.bean.RecommendBean;


public interface IRecommendView {

    public void onRecommendSuccess(RecommendBean.DataBean data);
    public void onRecommendFaile(String msg);



    public void onRecommendJinSuccess(boolean data);
    public void onRecommendJinFaile(String msg);

    //实名认证
    public void onGetRealNameStatusFaile(String msg);
    public void onGetRealNameStatusSuccess(int operation);

}
