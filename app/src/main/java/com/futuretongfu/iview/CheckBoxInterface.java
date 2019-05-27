package com.futuretongfu.iview;

/**
 * Created by Android on 2017/2/27.
 */
public interface CheckBoxInterface {
    /**
     * 当组选框状态改变时调用
     * @param groupPosition 组元素位置
     * @param isChecked     组元素选中与否
     */
    void groupCheckStatus(int groupPosition, boolean isChecked);
    /**
     * 当组元素下辖孩子选框状态改变时调用
     * @param groupPosition 组元素位置
     * @param childrenPosition 孩子元素位置
     * @param isChecked     孩子元素选中与否
     * */
    void childrenCheckStatus(int groupPosition, int childrenPosition, boolean isChecked);
}
