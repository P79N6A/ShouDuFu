package com.futuretongfu.model.eventType;

/**
 *    EventBus接收：注册并主动登录成功
 * Created by ChenXiaoPeng on 2017/6/28.
 */

public class ResgisterLoginEventType {

    private boolean isFinishActivity;

    public ResgisterLoginEventType(boolean isFinishActivity){
        this.isFinishActivity = isFinishActivity;
    }

    public boolean isFinishActivity() {
        return isFinishActivity;
    }

    public void setFinishActivity(boolean finishActivity) {
        isFinishActivity = finishActivity;
    }
}
