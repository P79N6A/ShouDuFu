package com.futuretongfu.iview;


import com.futuretongfu.bean.SystemConfigBean;

/**
 * Created by ChenXiaoPeng on 2017/7/5.
 */

public interface IUploadVoucheView {

    public void onUploadVoucheSuccess(boolean isOpereteType);
    public void onUploadVouchefaile(String msg);
    public void onUploadVouchePercentage(float percentage);

    public void onsystemConfigSuccess(SystemConfigBean data);
    public void onsystemConfigfaile(String msg);


}
