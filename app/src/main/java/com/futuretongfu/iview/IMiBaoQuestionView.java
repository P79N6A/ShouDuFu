package com.futuretongfu.iview;

import com.futuretongfu.model.entity.SafeQuestion;

import java.util.List;

/**
 * Created by ChenXiaoPeng on 2017/7/4.
 */

public interface IMiBaoQuestionView {

    public void onQueryQuestionListSuccess(List<SafeQuestion> datas);
    public void onQueryQuestionListFaile(String msg);

    public void onSetSecurityQuestionSuccess();
    public void onSetSecurityQuestionFaile(String msg);

    public void onAuthSecurityQuestionSuccess();
    public void onAuthSecurityQuestionFaile(String msg);

    public void onChangeSecurityQuestionSuccess();
    public void onChangeSecurityQuestionFaile(String msg);

}
