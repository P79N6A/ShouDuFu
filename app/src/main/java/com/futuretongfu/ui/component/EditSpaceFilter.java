package com.futuretongfu.ui.component;

import android.text.InputFilter;
import android.text.Spanned;

/**
 *   空格筛选器
 * Created by ChenXiaoPeng on 2017/7/14.
 */

public class EditSpaceFilter implements InputFilter {

    public EditSpaceFilter(){
        super();
    }
    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        if(source.equals(" "))
            return "";

        return source;
    }

}
