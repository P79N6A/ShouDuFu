package com.futuretongfu.iview;

/**
 * Created by ChenXiaoPeng on 2017/6/27.
 */

public interface ISettingSafeView {

    public void onSettingSafeUpgradeSuccess();
    public void onSettingSafeUpgradeFaile(String msg, boolean upgrade);

}
