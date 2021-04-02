package com.yryc.imlib.info.event;

import com.yryc.imlib.info.io.MessageDataInfoBean;

/**
 * @author : sklyand
 * @email :
 * @time : 2020/5/27 11:38
 * @describe ：
 */
public class GroupChatMessageEvent {
    private MessageDataInfoBean messageDataInfoBean;

    public GroupChatMessageEvent(MessageDataInfoBean messageDataInfoBean) {
        this.messageDataInfoBean = messageDataInfoBean;
    }

    public MessageDataInfoBean getMessageDataInfoBean() {
        return messageDataInfoBean;
    }

    public void setMessageDataInfoBean(MessageDataInfoBean messageDataInfoBean) {
        this.messageDataInfoBean = messageDataInfoBean;
    }
}
