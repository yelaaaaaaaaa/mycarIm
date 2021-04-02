package com.yryc.imkit.im;

import com.yryc.imkit.constant.Function;
import com.yryc.imkit.listener.OnImChatAvatarClickListener;
import com.yryc.imkit.listener.OnImChatItemClickListener;
import com.yryc.imkit.listener.OnImChatItemScrollListener;
import com.yryc.imkit.listener.OnImChatListEventListener;
import com.yryc.imkit.listener.OnImChatRichItemClickListener;
import com.yryc.imkit.listener.OnImChatRichItemNetResponseListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : Mai_Xiao_Peng
 * @email : Mai_Xiao_Peng@163.com
 * @time : 2019/1/2 15:06
 * @describe :
 */


public class ImOptions {

    public String appKey = "";

    public boolean debug;

    public String token = "";

    public String deviceId = "";

    public String channelName = "";

    //启用功能
    public List<Function> functions = new ArrayList<>();

    /**
     * 聊天列表点击事件
     */
    public OnImChatListEventListener onImChatListEventListener;

    /**
     * 聊天富文本按钮点击监听
     */
    public OnImChatRichItemClickListener onImChatRichItemClickListener;

    /**
     * 聊天富文本网络响应监听
     */
    public OnImChatRichItemNetResponseListener onImChatRichItemNetResponseListener;

    /**
     * 聊天条目点击监听
     */
    public OnImChatItemClickListener onImChatItemClickListener;

    /**
     * 滑动监听
     */
    public OnImChatItemScrollListener onImChatItemScrollListener;

    /**
     * 聊天头像点击
     */
    public OnImChatAvatarClickListener onImChatAvatarClickListener;
}
