package com.futuretongfu.iview;

/**
 * Created by ChenXiaoPeng on 2017/6/26.
 */

public interface IEditPersonalDataView {

    public void onEditPersonalDataEditNameSuccess();
    public void onEditPersonalDataEditNameFaile(String msg);

    public void onEditPersonalDataEditEmailSuccess();
    public void onEditPersonalDataEditEmailFaile(String msg);

    public void onEditPersonalDataEditGenderSuccess();
    public void onEditPersonalDataEditGenderFaile(String msg);

}
