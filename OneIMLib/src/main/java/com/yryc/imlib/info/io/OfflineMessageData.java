package com.yryc.imlib.info.io;

import java.util.List;

/**
 * 接受离线消息
 */
public class OfflineMessageData {

    private List<ReceiveMessageDataInfo> messages;

    public List<ReceiveMessageDataInfo> getMessages() {
        return messages;
    }

    public void setMessages(List<ReceiveMessageDataInfo> messages) {
        this.messages = messages;
    }
}
