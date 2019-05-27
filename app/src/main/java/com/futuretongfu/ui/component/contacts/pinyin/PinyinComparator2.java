package com.futuretongfu.ui.component.contacts.pinyin;

import com.futuretongfu.model.entity.AddressEntity;

import java.util.Comparator;

/**
 * Created by ChenXiaoPeng on 2017/6/15.
 */

public class PinyinComparator2 implements Comparator<AddressEntity> {

    public int compare(AddressEntity o1, AddressEntity o2) {
        if (o1.getSortLetters().equals("@")
                || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")
                || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }
}
