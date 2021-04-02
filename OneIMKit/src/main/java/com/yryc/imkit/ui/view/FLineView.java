package com.yryc.imkit.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.yryc.imkit.R;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2019/3/28 11:34
 * @describe :
 */


public class FLineView extends View {

    private Context context;

    public FLineView(Context context) {
        this(context, null);
    }

    public FLineView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setBackgroundColor(getContext().getResources().getColor(R.color.common_gray_3));
    }
}
