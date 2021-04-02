package com.yryc.imkit.widget.xview;

import android.view.View;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2019/1/3 14:41
 * @describe :
 */


public abstract class LoadViewCreator {

    abstract View createSuccessView(int layoutId);

    abstract View createLoadingView(int layoutId);

    abstract View createErrorView(int layoutId);

    abstract View createEmptyView(int layoutId);

}
