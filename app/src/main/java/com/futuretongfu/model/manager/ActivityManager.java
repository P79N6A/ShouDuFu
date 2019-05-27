package com.futuretongfu.model.manager;

/**
 * Created by ChenXiaoPeng on 2017/6/9.
 */

public class ActivityManager {

    private static ActivityManager Instance = null;

    public static ActivityManager getInstance(){
        if(Instance == null)
            Instance = new ActivityManager();

        return Instance;
    }

    private ActivityManager(){
    }

}
