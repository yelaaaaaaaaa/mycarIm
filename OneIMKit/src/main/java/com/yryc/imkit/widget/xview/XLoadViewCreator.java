package com.yryc.imkit.widget.xview;

import android.content.Context;
import android.view.View;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2019/1/3 14:43
 * @describe :
 */


public class XLoadViewCreator extends LoadViewCreator {

    private Context context;

    public XLoadViewCreator(Context context) {
        this.context = context;
    }

    @Override
    View createSuccessView(int layoutId) {
        return View.inflate(context, layoutId, null);
    }

    @Override
    View createLoadingView(int layoutId) {
        return View.inflate(context, layoutId, null);
    }

    @Override
    View createErrorView(int layoutId) {
        return View.inflate(context, layoutId, null);
    }

    @Override
    View createEmptyView(int layoutId) {
        return View.inflate(context, layoutId, null);
    }
}
