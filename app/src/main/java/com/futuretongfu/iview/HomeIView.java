package com.futuretongfu.iview;

import com.futuretongfu.base.BaseSerializable;
import com.futuretongfu.bean.HomeBannerBean;
import com.futuretongfu.bean.HomeTransactionBean;

import java.util.List;

/**
 * 交易列表
 *
 * @author DoneYang 2017/6/29
 */

public interface HomeIView extends IView {

    //首页banner
    void onBannerListFail(int code, String msg);

    void onBannerListSuccess(List<HomeBannerBean> data);

    //交易列表
    void onTransactionFail(int code, String msg);

    void onTransactionSuccess(List<HomeTransactionBean> data);

    //交易列表-更多
    void onTransactionMoreFail(int code, String msg);

    void onTransactionMoreSuccess(List<HomeTransactionBean> data);

    //消息
    void onSystemMessageNumFail(int code, String msg);

    void onSystemMessageNumSuccess(BaseSerializable data);

    //忽略此动态
    void onRemoveMessageFail(int code, String msg);

    void onRemoveMessageSuccess(BaseSerializable data);

    //获取昨日转换信息 成功
    void onIntegralConvertSuccess(String c);
    //获取昨日转换 失败
    void onIntegralConvertFaile(boolean showToast, String msg);

}
