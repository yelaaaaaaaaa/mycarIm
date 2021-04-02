package com.yryc.imlib.client;

/**
 * eventbus的消息处理
 */
public class MessageEvent {

    /**
     * 更新会话列表
     */
    public final static int ME_UPDATE_CONVERSATION_LIST = 0x01;
    /**
     * 聊天对象新的用户信息
     */
    public final static int ME_UPDATE_CONVERSATION = 0x02;
    /**
     * 聊天对象新的用户信息
     */
    public final static int ME_NEW_READ_MESSAGE = 0x03;
    /**
     * 聊天界面新消息
     */
    public final static int ME_RECEIVER_CHAT_MESSAGE = 0x11;
    /**
     * 发送成功
     */
    public final static int ME_SEND_CHAT_SUCCESS = 0x21;
    /**
     * 发送失败
     */
    public final static int ME_SEND_CHAT_FAIL = 0x22;
    /**
     * 删除消息成功、失败
     */
    public final static int ME_DELETE_MESSAGE_SUCCESS = 0x31;
    public final static int ME_DELETE_MESSAGE_FAIL = 0x32;
    /**
     * 删除会话成功、失败
     */
    public final static int ME_DELETE_CONVERSATION_SUCCESS = 0x33;
    public final static int ME_DELETE_CONVERSATION_FAIL = 0x34;
    /**
     * 添加常用语成功、失败
     */
    public final static int ME_ADD_WORD_SUCCESS = 0x35;
    public final static int ME_ADD_WORD_FAIL = 0x36;
    /**
     * 删除常用语成功、失败
     */
    public final static int ME_DELETE_WORD_SUCCESS = 0x37;
    public final static int ME_DELETE_WORD_FAIL = 0x38;
    /**
     * 连接状态更新
     */
    public final static int ME_NOTIFICATION_STATUS_UPDATE = 0x41;


    public int eventType;
    public Object[] values;

    public MessageEvent(int eventType) {
        this.eventType = eventType;
    }

    public MessageEvent(int eventType, Object... values) {
        this.eventType = eventType;
        this.values = values;
    }

}
