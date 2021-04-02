package com.yryc.imkit.impl;

import android.view.View;

import com.yryc.imkit.ui.fragment.GroupChatListFragment;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2018/12/11 10:33
 * @describe :
 */

public interface IAGroupChatListAction<T extends GroupChatListFragment> {

    View addView2TopContainer();

    T addChatContent2BodyContainer();

    View addView2BottomContainer();

}
