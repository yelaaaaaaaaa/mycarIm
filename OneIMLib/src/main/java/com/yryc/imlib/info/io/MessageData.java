package com.yryc.imlib.info.io;

/**
 * 外层
 */
public class MessageData {

    private ReceiveMessageDataInfo message;

    public MessageData(ReceiveMessageDataInfo messageDataInfo) {
        this.message = message;
    }

    public ReceiveMessageDataInfo getMessage() {
        return message;
    }

    public void setMessage(ReceiveMessageDataInfo message) {
        this.message = message;
    }
}
