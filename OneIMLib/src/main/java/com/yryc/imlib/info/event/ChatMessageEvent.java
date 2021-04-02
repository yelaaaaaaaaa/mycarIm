package com.yryc.imlib.info.event;


import com.yryc.imlib.info.io.MessageDataInfoBean;

/**
 * @author : sklyand
 * @email :
 * @time : 2020/5/16 11:18
 * @describe ：
 */
public class ChatMessageEvent {
    private boolean isChat;
    private MessageDataInfoBean messageDataInfoBean;

    public ChatMessageEvent(boolean isChat, MessageDataInfoBean messageDataInfoBean) {
        this.isChat = isChat;
        this.messageDataInfoBean = messageDataInfoBean;
    }

    public boolean isChat() {
        return isChat;
    }

    public void setChat(boolean chat) {
        isChat = chat;
    }

    public MessageDataInfoBean getMessageDataInfoBean() {
        return messageDataInfoBean;
    }

    public void setMessageDataInfoBean(MessageDataInfoBean messageDataInfoBean) {
        this.messageDataInfoBean = messageDataInfoBean;
    }
}
