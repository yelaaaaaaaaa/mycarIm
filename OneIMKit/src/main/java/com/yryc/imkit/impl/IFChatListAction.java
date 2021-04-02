package com.yryc.imkit.impl;

import com.yryc.imlib.model.chat.ChatList;

import com.yryc.imkit.core.slim.SlimAdapter;
import com.yryc.imkit.core.slim.viewinjector.IViewInjector;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2018/12/3 16:16
 * @describe :
 */

public interface IFChatListAction {

    void registerChatLayoutAndData(SlimAdapter slimAdapter);

    int getChatListItemLayoutId();

    void injectChatListItemDataSource(ChatList chatList, IViewInjector injector);

}
