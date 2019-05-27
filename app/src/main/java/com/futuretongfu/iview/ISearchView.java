package com.futuretongfu.iview;


import com.futuretongfu.model.entity.SearchBankInfo;

import java.util.List;

/**
 * Created by Lenovo on 2017/7/6.
 */

public interface ISearchView {

    //获取银行列表成功
    public void onBankCardListSuccess(List<SearchBankInfo> list);

    //获取银行列表失败
    public void onBankListFaile(int code, String msg);

}
