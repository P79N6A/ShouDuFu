package com.futuretongfu.presenter.bank;

import android.content.Context;
import android.text.TextUtils;

import com.futuretongfu.bean.BankListBean;
import com.futuretongfu.constants.Constants;
import com.futuretongfu.iview.ISearchView;
import com.futuretongfu.model.entity.AddressEntity;
import com.futuretongfu.model.repository.BankRepository;
import com.futuretongfu.presenter.Presenter;
import com.futuretongfu.model.entity.SearchBankInfo;
import com.futuretongfu.model.repository.BaseRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 类:   SearchBankPresenter
 * 描述:  查询银行列表
 * 作者： weiang on 2017/7/6
 */
public class SearchBankPresenter extends Presenter {

    private BankRepository bankRepository;
    private ISearchView iView;

    public SearchBankPresenter(Context context, ISearchView iView) {
        super(context);
        this.iView = iView;
        bankRepository = new BankRepository();
    }

    @Override
    public void onDestroy(){
        if(bankRepository != null) bankRepository.cancel();
    }

    /**
     * 银行卡列表
     *
     * @param bankName
     * @param page
     * @param recommend
     */
    public void searchBankCardList(String bankName, int page, String recommend) {
        bankRepository.searchBankCardList(bankName, page, recommend, new BaseRepository.HttpCallListener<JSONObject>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                iView.onBankListFaile(code, msg);
            }

            @Override
            public void onHttpCallSuccess(JSONObject json) {
                List<SearchBankInfo> list = new ArrayList<SearchBankInfo>();
                JSONArray jsonArray = json.optJSONArray("list");
                try {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject oj = jsonArray.getJSONObject(i);
                        if (oj == null) {
                            return;
                        }
                        SearchBankInfo info = new SearchBankInfo();
                        if (!TextUtils.isEmpty(oj.getString("innerCode"))) {
                            info.setInnerCode(oj.getString("innerCode"));
                        }
                        if (!TextUtils.isEmpty(oj.getString("bankName"))) {
                            info.setBankName(oj.getString("bankName"));
                        }
//                        if (!TextUtils.isEmpty(oj.getString("fullName"))) {
//                            info.setFullName(oj.getString("fullName"));
//                        }
//                        if (!TextUtils.isEmpty(oj.getString("banktype"))) {
//                            info.setBanktype(oj.getString("banktype"));
//                        }
//                        if (!TextUtils.isEmpty(oj.getString("channel"))) {
//                            info.setChannel(oj.getString("channel"));
//                        }
                        list.add(info);
                    }
                    iView.onBankCardListSuccess(list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 查询银行卡列表
     */
    public void searchBankCardList2() {
        bankRepository.searchBankCardList2(new BaseRepository.HttpCallListener<List<SearchBankInfo>>() {
            @Override
            public void onHttpCallFaile(int code, String msg) {
                if (iView != null)
                    iView.onBankListFaile(code,msg);
            }

            @Override
            public void onHttpCallSuccess(List<SearchBankInfo> data) {
                iView.onBankCardListSuccess(data);
            }
        });
    }


}
