package com.yryc.imkit.listener;

import com.yryc.imlib.model.chat.ChatCar;
import com.yryc.imlib.model.chat.ChatOrder;
import com.yryc.imlib.model.chat.ChatRich;

public interface OnImChatItemClickListener {
    void onChatOrderClick(ChatOrder chatOrder);

    void onChatCarClick(ChatCar chatCar);

    void onChatRichBodyClick(ChatRich.Body body);
}