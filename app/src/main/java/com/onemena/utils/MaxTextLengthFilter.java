package com.onemena.utils;

import android.text.InputFilter;
import android.text.Spanned;

/**
 *
 * Created by zhaojie on 2016/7/20.
 */
public class MaxTextLengthFilter implements InputFilter {
    private int mMaxLength;
    public MaxTextLengthFilter(int max) {
        mMaxLength = max - 1;
    }
    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        int keep = mMaxLength - (dest.length() - (dend - dstart));
        if(keep < (end - start)){
            ToastUtil.showNormalShortToast("必须是6-20位的数字或字母");
        }
        if(keep <= 0){
            return "";
        }else if(keep >= end - start){
            return null;
        }else{
            return source.subSequence(start,start + keep);
        }
    }
}
