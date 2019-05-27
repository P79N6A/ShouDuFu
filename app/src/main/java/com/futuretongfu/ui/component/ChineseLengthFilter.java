package com.futuretongfu.ui.component;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *   中文长度筛选器
 * Created by ChenXiaoPeng on 2017/7/13.
 */

public class ChineseLengthFilter implements InputFilter {
    private int MAX_EN;// 最大英文/数字长度 一个汉字算两个字母
    private String regEx = "[\\u4e00-\\u9fa5]"; // unicode编码，判断是否为汉字

    private int curLength;
    private boolean noSpace = false;

    public ChineseLengthFilter(int mAX_EN) {
        super();
        MAX_EN = mAX_EN;
    }

    public ChineseLengthFilter(int mAX_EN, boolean noSpace) {
        super();
        MAX_EN = mAX_EN;
        this.noSpace = noSpace;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        int destCount = dest.toString().length()
                + getChineseCount(dest.toString());
        int sourceCount = source.toString().length()
                + getChineseCount(source.toString());

        if(0 == sourceCount){
            curLength = destCount - 1;
        }
        else{
            curLength = destCount + sourceCount;
        }

        if (destCount + sourceCount > MAX_EN) {
            return "";

        } else {
            if(noSpace){
                if(source.equals(" "))
                    return "";
            }
            return source;
        }
    }

    private int getChineseCount(String str) {
        int count = 0;
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        while (m.find()) {
            for (int i = 0; i <= m.groupCount(); i++) {
                count = count + 1;
            }
        }
        return count;
    }

    public int getCurLength() {
        return curLength;
    }

}
