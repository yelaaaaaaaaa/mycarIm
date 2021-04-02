package com.yryc.imkit.impl;

import com.yryc.imkit.core.slim.SlimAdapter;
import com.yryc.imlib.info.GeneralMessageDataInfo;

import java.util.List;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2018/12/3 16:16
 * @describe :
 */


public interface IFChatContentAction {

    boolean enableImChatExtension();

    void registerCustomChatLayout(SlimAdapter slimAdapter);

    Object addCustomDataSource(List<Object> chatList, GeneralMessageDataInfo generalMessageDataInfo);
}
