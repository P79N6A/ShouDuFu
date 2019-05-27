package com.futuretongfu.iview;

import com.futuretongfu.base.BaseSerializable;

/**
 * 添加收藏
 *
 * @author DoneYang 2017/6/28
 */

public interface AddCollectIView extends IView {

    void onAddCollectFail(int code, String msg);

    void onAddCollectSuccess(BaseSerializable data);
}
