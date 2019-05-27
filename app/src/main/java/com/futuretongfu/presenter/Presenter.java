package com.futuretongfu.presenter;

import android.content.Context;

import com.futuretongfu.model.executer.JobExecutor;
import com.futuretongfu.model.executer.RxJavaExecuter;
import com.futuretongfu.model.executer.UIThread;

/**
 * Created by ChenXiaoPeng on 2017/6/8.
 */

public abstract class Presenter {

    protected Context context;
    protected RxJavaExecuter rxJavaExecuter;

    public Presenter(Context context){
        this.context = context;
        this.rxJavaExecuter = new RxJavaExecuter(JobExecutor.instance(), UIThread.instance());
    }

    public abstract void onDestroy();
}
