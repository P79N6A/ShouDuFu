package com.futuretongfu.iview;

import com.futuretongfu.model.entity.Score;

/**
 * Created by ChenXiaoPeng on 2017/6/29.
 */

public interface IScoreDetailView {

    public void onScoreDetailSuccess(Score data);
    public void onScoreDetailFaile(String msg);

}
