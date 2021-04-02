package com.yryc.imkit.listener;

import com.yryc.imlib.model.chat.ChatOrder;

public interface OnImChatItemScrollListener {
    void onChatOrderVisibilityState(ChatOrder chatOrder);
}