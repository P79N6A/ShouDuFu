package com.futuretongfu.iview;

import android.view.View;

/**
 * Created by Android on 2017/2/28.
 */
public interface GoodsNumChangeInterface {
    /**
     * 增加操作
     *
     * @param groupPosition 组元素位置
     * @param childPosition 子元素位置
     * @param showCountView 用于展示变化后数量的View
     * @param isChecked     子元素选中与否
     */
    void addGoodsNumChange(int groupPosition, int childPosition, View showCountView, boolean isChecked);
    /**
     * 删减操作
     * @param groupPosition 组元素位置
     * @param childPosition 子元素位置
     * @param showCountView 用于展示变化后数量的View
     * @param isChecked     子元素选中与否
     */
    void reduceGoodsNumChange(int groupPosition, int childPosition, View showCountView, boolean isChecked);

    void deleteGoodsChange(String id);
}
