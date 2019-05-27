package com.futuretongfu.iview;

/**
 * Created by ChenXiaoPeng on 2017/7/2.
 */

public interface IOrderConsumerCommentView {

    public void onOrderConsumerCommentSuccess();
    public void onOrderConsumerCommentFaile(String msg);
    public void onOrderConsumerCommentPercentage(float percentage);

}
