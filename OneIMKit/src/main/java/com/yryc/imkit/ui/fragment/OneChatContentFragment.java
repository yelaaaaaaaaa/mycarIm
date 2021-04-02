package com.yryc.imkit.ui.fragment;


import com.yryc.imkit.core.slim.SlimAdapter;
import com.yryc.imlib.info.GeneralMessageDataInfo;

import java.util.List;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2018/12/11 11:49
 * @describe :
 */


public class OneChatContentFragment extends ChatContentFragment {

    @Override
    public boolean enableImChatExtension() {
        return true;
    }

    /**
     * 注入布局、数据
     *
     * @param slimAdapter
     */
    @Override
    public void registerCustomChatLayout(SlimAdapter slimAdapter) {

    }

    /**
     * 生成数据源
     *
     * @param chatList
     * @param generalMessageDataInfo
     * @return
     */
    @Override
    public Object addCustomDataSource(List<Object> chatList, GeneralMessageDataInfo generalMessageDataInfo) {
        return null;
    }


}
