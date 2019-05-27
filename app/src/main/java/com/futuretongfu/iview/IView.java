package com.futuretongfu.iview;

import com.futuretongfu.base.BaseSerializable;

/**
 * Created by ChenXiaoPeng on 2017/6/8.
 */

public interface IView {

    void onSuccess(BaseSerializable data);

    void onFail(int code,String msg);
}
