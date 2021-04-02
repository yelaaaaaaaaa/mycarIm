package com.yryc.imkit.ui.fragment;


import com.yryc.imkit.core.slim.SlimAdapter;
import com.yryc.imlib.info.GroupMessageDataInfo;

import java.util.List;

/**
 * Description: 群聊页面
 *
 * Author: czk
 * Date：2020/5/27 14:52
 */


public class OneGroupChatContentFragment extends GroupChatContentFragment {

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
     * @param groupMessageDataInfo
     * @return
     */
    @Override
    public Object addCustomDataSource(List<Object> chatList, GroupMessageDataInfo groupMessageDataInfo) {
        return null;
    }


}
